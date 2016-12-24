package com.ericyl.example.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.ui.BaseActivity;
import com.ericyl.utils.cryptographical.exception.AESCryptoException;
import com.ericyl.utils.util.AESKeyStoreUtils;

import java.io.File;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.OnClick;

public class AESCryptoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_source)
    EditText etSource;
    @BindView(R.id.tv_cipher)
    TextView tvCipher;
    @BindView(R.id.tv_original)
    TextView tvOriginal;

    private SecretKey secretKey;
    private byte[] iv;
    private byte[] salt;

    @Override
    public int getContentViewId() {
        return R.layout.activity_aes_crypto;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        iv = Base64.decode("e+YXMeMgVaQLjtQt4AlmkQ==", Base64.NO_WRAP);
        iv = AESKeyStoreUtils.getRandomIv();
        Log.v("iv", Base64.encodeToString(iv, Base64.NO_WRAP));
        salt = AESKeyStoreUtils.getRandomSalt();
        Log.v("salt", Base64.encodeToString(salt, Base64.NO_WRAP));
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.create_key, R.id.read_key, R.id.btn_encrypt, R.id.btn_decrypt, R.id.save_key})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_key:
                try {
                    secretKey = AESKeyStoreUtils.getSecretKey("example", salt);
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.read_key:
                try {
                    secretKey = AESKeyStoreUtils.readSecretKey(this, "aes" + File.separator + "example.keystore", "BKS", "example", "example", "example");
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_encrypt:
                String source = etSource.getText().toString();
                if (TextUtils.isEmpty(source)) {
                    Snackbar.make(etSource, "is null", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (secretKey == null) {
                    Snackbar.make(etSource, "aaaa", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String enc = AESKeyStoreUtils.encrypt(secretKey, iv, source);
                    tvCipher.setText(enc);
                    Log.v("enc", enc);
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_decrypt:
                String enc = tvCipher.getText().toString();
                if (TextUtils.isEmpty(enc)) {
                    Snackbar.make(tvCipher, "is null", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (secretKey == null) {
                    Snackbar.make(tvCipher, "aaaa", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String dec = AESKeyStoreUtils.decrypt(secretKey, iv, enc);
                    tvOriginal.setText(dec);
                    Log.v("dec", dec);
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.save_key:
                try {
                    AESKeyStoreUtils.saveInKeyStore(this, secretKey, "BKS", "aes/example.keystore", "example", "example", "example");
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
