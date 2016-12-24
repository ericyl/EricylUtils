package com.ericyl.example.ui.fragment.home;


import android.view.View;

import com.ericyl.example.R;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.activity.AESCryptoActivity;
import com.ericyl.utils.util.ActivityUtils;

import butterknife.OnClick;


public class CryptoInHomeFragment extends BaseFragment implements View.OnClickListener {



    @Override
    public int getContentViewId() {
        return R.layout.fragment_crypto_in_home;
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
