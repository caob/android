package com.ergo.poc.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.ui.fragment.home.AboutFragment;
import com.ergo.poc.ui.fragment.home.CentersFragment;
import com.ergo.poc.ui.fragment.home.DashboardFragment;
import com.ergo.poc.ui.fragment.home.InboxFragment;
import com.ergo.poc.ui.fragment.home.ProfileFragment;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.CurrentUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/21/16.
 */
public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.home_drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.home_navigation_view)
    NavigationView navigationView;

    private int container;

    public Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initToolbar();
        initNavigationMenu();
        initNavigationLayout();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MENSAJESPUSH", "Key: " + key + " Value: " + value);
            }
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "Refreshed token: " + refreshedToken);

        FirebaseMessaging.getInstance().subscribeToTopic("Noticias");
    }

    private void initToolbar() {
        toolbar.setTitle(R.string.home);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
    }

    private void initNavigationMenu() {

        container = R.id.home_fragment_container;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        checkItem(R.id.nav_home);
        fragmentTransaction.replace(container, new DashboardFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_home:
                        setFragmentCommit(transaction, new DashboardFragment());
                        break;
                    case R.id.nav_inbox:
                        setFragmentCommit(transaction, new InboxFragment());
                        break;
                    case R.id.nav_centers:
                        setFragmentCommit(transaction, new CentersFragment());
                        break;
                    case R.id.nav_about:
                        setFragmentCommit(transaction, new AboutFragment());
                        break;
                    case R.id.nav_more_apps:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PUBLISHER)));
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    private void initNavigationLayout() {

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this
                , drawerLayout
                , toolbar
                , R.string.drawer_open
                , R.string.drawer_close){

            @Override
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        RelativeLayout headerIcon = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.header_root_view);
        headerIcon.setOnClickListener(iconListener);

        LinearLayout footerIcon = (LinearLayout) navigationView.findViewById(R.id.nav_logout_footer);
        footerIcon.setOnClickListener(footerListener);

        TextView headerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_user);
        headerName.setText(CurrentUser.getInstance().getUser() != null
                ? String.format(getResources().getString(R.string.name_format)
                    , CurrentUser.getInstance().getUser().getName()
                    , CurrentUser.getInstance().getUser().getLastName())
                : getResources().getString(R.string.empty));
    }

    private View.OnClickListener iconListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setFragmentCommit(getFragmentManager().beginTransaction(), new ProfileFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    };

    private View.OnClickListener footerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void setFragmentCommit(FragmentTransaction transaction, Fragment fragmentToCommit) {
        fragment = fragmentToCommit;
        transaction.addToBackStack(null);
        getFragmentManager().popBackStack();
        transaction.replace(container, fragmentToCommit).commit();
    }

    public void checkItem(int id) {
        MenuItem menuItem = navigationView.getMenu().findItem(id);
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
