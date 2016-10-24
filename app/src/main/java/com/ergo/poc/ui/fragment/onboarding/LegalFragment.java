package com.ergo.poc.ui.fragment.onboarding;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.KeySaver;
import com.ergo.poc.util.FragmentUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by mariano on 10/21/16.
 */
public class LegalFragment extends Fragment {

    @BindView(R.id.legal_text)
    TextView legalText;

    @BindString(R.string.lorem_ipsum)
    String loremIpsum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal, container, false);
        ButterKnife.bind(this, view);

        if (KeySaver.isExist(getActivity(), Constant.LEGAL_OK)) {
            FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.on_boarding_container
                    , false, new PhoneFragment());
        }

        legalText.setMovementMethod(new ScrollingMovementMethod());
        legalText.setText(loremIpsum);

        return view;
    }

    @OnClick(R.id.legal_button)
    public void accept() {
        KeySaver.saveShare(getActivity(), Constant.LEGAL_OK, true);
        FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.on_boarding_container
                , true, new PhoneFragment());
    }

}
