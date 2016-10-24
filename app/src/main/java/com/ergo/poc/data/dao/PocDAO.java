package com.ergo.poc.data.dao;

import com.ergo.poc.PocApplication;
import com.ergo.poc.R;
import com.ergo.poc.data.api.CentersAPI;
import com.ergo.poc.data.api.LoginAPI;
import com.ergo.poc.data.api.NotificationsAPI;
import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.model.Centers;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.data.model.Notifications;
import com.ergo.poc.data.model.User;
import com.ergo.poc.util.CheckConnection;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.CurrentUser;
import com.ergo.poc.util.KeySaver;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by mariano on 10/21/16.
 */
public class PocDAO {

    private Realm realm;

    private RealmResults<Centers> centers;
    private RealmResults<Notifications> notifications;

    private List<Centers> centersList;
    private List<Notifications> notificationsList;

    private Retrofit getBaseAPI() {

        return new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private Retrofit getBaseMockUpAPI() {
        return new Retrofit.Builder()
                .baseUrl(Constant.API_MOCK_UP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getNotifications(final ResultListener<List<Notifications>> resultListener
            , boolean updateValues
            , Boolean read, boolean order, List<Notification> view) {

        resultListener.loading();

        realm = null;
        realm = Realm.getDefaultInstance();

        if (updateValues) {
            getNotificationsWithConnection(resultListener, read, order, view);
        } else {
            if (read != null) {

                int countTrue = 0;
                RealmQuery<Notifications> query = realm.where(Notifications.class);
                for (int i = 0; i < view.size(); i++) {
                    if (view.get(i).isState()) {
                        ++countTrue;
                        if (countTrue == 1) {
                            query.beginGroup();
                            query.equalTo(Constant.READ, read);
                        }
                        query.or();
                        query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                    }
                }

                if (countTrue > 0) {
                    query.endGroup();
                    notifications = query.findAll().sort(new String[]{Constant.READ, Constant.TIMESTAMP}, new Sort[]{
                            order ? Sort.ASCENDING : Sort.DESCENDING,
                            order ? Sort.DESCENDING : Sort.ASCENDING});
                }

            } else {

                int countTrue = 0;
                RealmQuery<Notifications> query = realm.where(Notifications.class);
                for (int i = 0; i < view.size(); i++) {
                    if (view.get(i).isState()) {
                        ++countTrue;
                        if (countTrue == 1) {
                            query.beginGroup();
                            query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                        }

                        if (countTrue > 1) {
                            query.or();
                            query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                        }
                    }
                }

                if (countTrue > 0) {
                    query.endGroup();
                    notifications = query.findAll().sort(new String[]{Constant.READ, Constant.TIMESTAMP}, new Sort[]{
                            order ? Sort.ASCENDING : Sort.DESCENDING,
                            order ? Sort.DESCENDING : Sort.ASCENDING});
                }
            }

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    if (notifications != null) {
                        if (notifications.size() > 0) {
                            resultListener.finish(new ArrayList<>(notifications));
                        } else {
                            if (CheckConnection.isConnected(PocApplication.getContext())) {
                                getNotificationsWithConnection(resultListener);
                            } else {
                                resultListener.error(new Exception(PocApplication.getContext()
                                        .getResources().getString(R.string.data_need_connection)));
                            }
                        }
                    } else {
                        resultListener.error(new Exception(PocApplication.getContext()
                                .getResources().getString(R.string.data_need_subscription)));
                    }
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    resultListener.error(error);
                }
            });
        }
    }

    private void getNotificationsWithConnection(final ResultListener<List<Notifications>> resultListener) {
        NotificationsAPI notificationsAPI = getBaseMockUpAPI().create(NotificationsAPI.class);

        Callback<List<Notifications>> callback = new Callback<List<Notifications>>() {
            @Override
            public void success(Result<List<Notifications>> result) {

                notificationsList = result.data;

                try {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(new ArrayList<>(notificationsList));
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            notifications = realm.where(Notifications.class)
                                    .findAllAsync()
                                    .sort(new String[]{Constant.READ, Constant.TIMESTAMP}
                                            , new Sort[]{Sort.ASCENDING, Sort.DESCENDING});

                            resultListener.finish(new ArrayList<>(notifications));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                if (CheckConnection.isConnected(PocApplication.getContext())) {
                    resultListener.error(exception);
                } else {
                    resultListener.error(new Exception(PocApplication.getContext()
                            .getResources().getString(R.string.data_need_connection)));
                }
            }
        };

        Call<List<Notifications>> call = notificationsAPI.loadNotifications(null);
        call.enqueue(callback);
    }

    private void getNotificationsWithConnection(final ResultListener<List<Notifications>> resultListener
            , final Boolean read, final boolean order, final List<Notification> view) {
        NotificationsAPI notificationsAPI = getBaseMockUpAPI().create(NotificationsAPI.class);

        Callback<List<Notifications>> callback = new Callback<List<Notifications>>() {
            @Override
            public void success(Result<List<Notifications>> result) {

                notificationsList = result.data;

                try {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(new ArrayList<>(notificationsList));
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            if (read != null) {

                                int countTrue = 0;
                                RealmQuery<Notifications> query = realm.where(Notifications.class);
                                for (int i = 0; i < view.size(); i++) {
                                    if (view.get(i).isState()) {
                                        ++countTrue;
                                        if (countTrue == 1) {
                                            query.beginGroup();
                                            query.equalTo(Constant.READ, read);
                                        }
                                        query.or();
                                        query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                                    }
                                }

                                if (countTrue > 0) {
                                    query.endGroup();
                                    notifications = query.findAll().sort(new String[]{Constant.READ, Constant.TIMESTAMP}, new Sort[]{
                                            order ? Sort.ASCENDING : Sort.DESCENDING,
                                            order ? Sort.DESCENDING : Sort.ASCENDING});
                                }
                            } else {

                                int countTrue = 0;
                                RealmQuery<Notifications> query = realm.where(Notifications.class);
                                for (int i = 0; i < view.size(); i++) {
                                    if (view.get(i).isState()) {
                                        ++countTrue;
                                        if (countTrue == 1) {
                                            query.beginGroup();
                                            query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                                        }

                                        if (countTrue > 1) {
                                            query.or();
                                            query.equalTo(Constant.CATEGORY, view.get(i).getCategory());
                                        }
                                    }
                                }

                                if (countTrue > 0) {
                                    query.endGroup();
                                    notifications = query.findAll().sort(new String[]{Constant.READ, Constant.TIMESTAMP}, new Sort[]{
                                            order ? Sort.ASCENDING : Sort.DESCENDING,
                                            order ? Sort.DESCENDING : Sort.ASCENDING});
                                }

                            }
                            if (notifications != null) {
                                resultListener.finish(new ArrayList<>(notifications));
                            } else {
                                resultListener.error(new Exception(PocApplication.getContext()
                                        .getResources().getString(R.string.data_need_subscription)));
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                if (CheckConnection.isConnected(PocApplication.getContext())) {
                    resultListener.error(exception);
                } else {
                    resultListener.error(new Exception(PocApplication.getContext()
                            .getResources().getString(R.string.data_need_connection)));
                }
            }
        };

        Call<List<Notifications>> call = notificationsAPI.loadNotifications(null);
        call.enqueue(callback);
    }

    public void getCenters(final ResultListener<List<Centers>> resultListener) {

        resultListener.loading();

        realm = Realm.getDefaultInstance();
        centers = realm.where(Centers.class).findAllAsync();

        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    if (centers.size() > 0) {
                        resultListener.finish(new ArrayList<>(centers));
                    } else {
                        if (CheckConnection.isConnected(PocApplication.getContext())) {
                            getCentersWithConnection(resultListener);
                        } else {
                            resultListener.error(new Exception(PocApplication.getContext()
                                    .getResources().getString(R.string.data_need_connection)));
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCentersWithConnection(final ResultListener<List<Centers>> resultListener) {

        CentersAPI centersAPI = getBaseAPI().create(CentersAPI.class);

        Callback<List<Centers>> callback = new Callback<List<Centers>>() {

            @SuppressWarnings("unchecked")
            @Override
            public void success(Result<List<Centers>> result) {

                centersList = new ArrayList<>();

                for (int i = 0; i < 800; i++) {
                    centersList.add(result.data.get(i));
                }

                try {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(centersList);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                resultListener.finish(centersList);
            }

            @Override
            public void failure(TwitterException exception) {
                resultListener.error(exception);
            }
        };

        Call<List<Centers>> call = centersAPI.loadCenters();
        call.enqueue(callback);
    }

    public void loginUser(final ResultListener<User> resultListener) {

        resultListener.loading();

        LoginAPI loginAPI = getBaseAPI().create(LoginAPI.class);

        RequestBody body = RequestBody.create(MediaType.parse(Constant.CONTENT_TYPE_JSON)
                , KeySaver.getStringSavedShare(PocApplication.getContext(), Constant.AUTH_USER));
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {

            @Override
            public void success(Result<ResponseBody> result) {
                try {
                    resultListener.finish(new Gson().fromJson(result.data.string(), User.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                resultListener.error(exception);
            }
        };

        Call<ResponseBody> response = loginAPI.loginUser(body);
        if (CheckConnection.isConnected(PocApplication.getContext())) {
            response.enqueue(callback);
        } else {
            resultListener.error(new Exception(PocApplication.getContext()
                    .getResources().getString(R.string.data_need_connection)));
        }
    }

    public void updateUser(final ResultListener<User> resultListener) {

        resultListener.loading();

        LoginAPI loginAPI = getBaseAPI().create(LoginAPI.class);

        RequestBody body = RequestBody.create(MediaType.parse(Constant.CONTENT_TYPE_JSON)
                , KeySaver.getStringSavedShare(PocApplication.getContext(), Constant.UPDATE_USER));

        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void success(Result<ResponseBody> result) {
                try {
                    resultListener.finish(new Gson().fromJson(result.data.string(), User.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                resultListener.error(exception);
            }
        };

        Call<ResponseBody> response = loginAPI.updateUser(CurrentUser.getInstance().getUser().getStringId(), body);

        if (CheckConnection.isConnected(PocApplication.getContext())) {
            response.enqueue(callback);
        } else {
            resultListener.error(new Exception(PocApplication.getContext()
                    .getResources().getString(R.string.data_need_connection)));
        }
    }

}
