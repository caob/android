package com.ergo.poc.util;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.ergo.poc.PocApplication;
import com.ergo.poc.R;

/*
 * Created by mariano on 10/21/16.
 */
public class Utils {

    public static int dpToPx(Activity context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void startActivityWithFinish(Activity context, Class open) {
        Intent intent = new Intent(context, open);
        context.startActivity(intent);
        context.finish();
    }

    public static void startActivityWithFinishWithFadeAnimation(Activity context, Class open) {
        Intent intent = new Intent(context, open);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static ProgressDialog showProgressDialog(Activity activity, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static boolean hasLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return KeySaver.getBoolSavedShare(activity, Constant.LOCATION);
        } else {
            int readLocation = activity.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            return readLocation == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static void requestLocationPermission(Context context, Fragment fragment) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.FINE_LOCATION);
        }
    }

    public static String getVersionName() {
        PackageInfo packageInfo = null;
        try {
             packageInfo = PocApplication.getContext().getPackageManager()
                    .getPackageInfo(PocApplication.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo != null ? packageInfo.versionName : "";
    }

    public static boolean validate(EditText[] fields){
        for (EditText currentField : fields) {
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError(PocApplication.getContext().getResources().getString(R.string.error_message));
                return false;
            }
        }
        return true;
    }

    public static void openDial(Activity context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format(PocApplication.getContext().getResources()
                .getString(R.string.phone_format), phone)));
        context.startActivity(intent);
    }

    public static String getMonthName(int month) {

        String monthName = "";

        switch (month) {
            case 1:
                monthName = "Enero";
                break;
            case 2:
                monthName = "Febrero";
                break;
            case 3:
                monthName = "Marzo";
                break;
            case 4:
                monthName = "Abril";
                break;
            case 5:
                monthName = "Mayo";
                break;
            case 6:
                monthName = "Junio";
                break;
            case 7:
                monthName = "Julio";
                break;
            case 8:
                monthName = "Agosto";
                break;
            case 9:
                monthName = "Septiembre";
                break;
            case 10:
                monthName = "Octubre";
                break;
            case 11:
                monthName = "Noviembre";
                break;
            case 12:
                monthName = "Diciembre";
                break;
        }

        return monthName;
    }
}
