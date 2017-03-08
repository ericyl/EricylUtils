package com.ericyl.example.ui.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.ui.activity.AESCryptoActivity;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.utils.util.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class CryptoInHomeFragment extends BaseFragment implements View.OnClickListener {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto_in_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.aes})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aes:
                ActivityUtils.jumpActivity(getContext(), AESCryptoActivity.class);
                break;
        }
    }
}
