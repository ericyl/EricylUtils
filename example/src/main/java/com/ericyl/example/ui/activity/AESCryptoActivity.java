package com.ericyl.example.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.util.AESTableUtils;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.utils.cryptographical.exception.AESCryptoException;
import com.ericyl.utils.util.AESKeyStoreUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private String name, type, keyStorePwd, secretKeyEntryAlias, secretKeyEntryPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes_crypto);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initActionBar(toolbar);
        iv = Base64.decode("e+YXMeMgVaQLjtQt4AlmkQ==", Base64.NO_WRAP);
//        iv = AESKeyStoreUtils.getRandomIv();
        Log.v("iv", Base64.encodeToString(iv, Base64.NO_WRAP));
        salt = AESKeyStoreUtils.getRandomSalt();
        Log.v("salt", Base64.encodeToString(salt, Base64.NO_WRAP));


        Observable.just("example.keystore").subscribeOn(Schedulers.io()).map(new Func1<String, Void>() {
            @Override
            public Void call(String str) {
                SQLiteDatabase.loadLibs(AESCryptoActivity.this);
                SQLiteDatabase database = null;
                Cursor cursor = null;
                try {
                    database = SQLiteDatabase.openDatabase(AppProperties.getDatabaseDir(DatabaseUtils.DATA_DATABASE_NAME + ".db").getAbsolutePath(), "test", null, SQLiteDatabase.OPEN_READONLY);
                    cursor = database.query(AESTableUtils.TABLE_NAME, null, AESTableUtils.NAME + " = ? ", new String[]{"example.keystore"}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(AESTableUtils.NAME));
                        type = cursor.getString(cursor.getColumnIndex(AESTableUtils.KEY_TYPE));
                        keyStorePwd = cursor.getString(cursor.getColumnIndex(AESTableUtils.KEY_STORE_PWD));
                        secretKeyEntryAlias = cursor.getString(cursor.getColumnIndex(AESTableUtils.SECRET_KEY_ENTRY_ALIAS));
                        secretKeyEntryPwd = cursor.getString(cursor.getColumnIndex(AESTableUtils.SECRET_KEY_ENTRY_PWD));
                    } else
                        throw new RuntimeException("null");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    if (database != null)
                        database.close();
                    if (cursor != null)
                        cursor.close();
                }
                return null;

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
            @Override
            public void call(Void v) {
            }

        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                name = "example.keystore";
                type = "BKS";
                keyStorePwd = "example";
                secretKeyEntryAlias = "example";
                secretKeyEntryPwd = "example";
                throwable.printStackTrace();
            }
        });
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
                    secretKey = AESKeyStoreUtils.readSecretKey(this, "aes" + File.separator + name, type, keyStorePwd, secretKeyEntryAlias, secretKeyEntryPwd);
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_encrypt:
                String source = etSource.getText().toString();
                if (TextUtils.isEmpty(source)) {
                    Snackbar.make(etSource, R.string.source_text_is_null, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (secretKey == null) {
                    Snackbar.make(etSource, R.string.secret_key_is_null, Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(tvCipher, R.string.cipher_text_is_null, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (secretKey == null) {
                    Snackbar.make(tvCipher, R.string.secret_key_is_null, Snackbar.LENGTH_SHORT).show();
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
                    AESKeyStoreUtils.saveInKeyStore(this, secretKey, type, name, keyStorePwd, secretKeyEntryAlias, secretKeyEntryPwd);
                } catch (AESCryptoException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
