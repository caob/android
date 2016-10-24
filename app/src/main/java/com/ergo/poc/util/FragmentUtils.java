package com.ergo.poc.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.ergo.poc.R;
import com.ergo.poc.ui.fragment.onboarding.CompleteProfileFragment;

/*
 * Created by mariano on 10/21/16.
 */
public class FragmentUtils {

    private static FragmentUtils instance = null;

    public static FragmentUtils getInstance() {
        if (instance == null) {
            instance = new FragmentUtils();
        }
        return instance;
    }

    public void replaceFragment(Activity activity, int container, boolean withAnimation, Fragment fragment) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        if (withAnimation) fragmentTransaction.setCustomAnimations(R.animator.slide_right, R.animator.slide_left);
        fragmentTransaction.addToBackStack(null);
        activity.getFragmentManager().popBackStack();
        if (fragment instanceof CompleteProfileFragment) {
            fragmentTransaction.replace(container, fragment).commitAllowingStateLoss();
        } else {
            fragmentTransaction.replace(container, fragment).commit();
        }
    }
}
