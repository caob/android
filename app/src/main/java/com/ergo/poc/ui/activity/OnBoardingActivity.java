package com.ergo.poc.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ergo.poc.R;
import com.ergo.poc.ui.fragment.onboarding.LegalFragment;

/*
 * Created by mariano on 10/21/16.
 */
public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.on_boarding_container, new LegalFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
