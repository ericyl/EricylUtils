package com.ericyl.utils.cryptographical.aes;

import android.support.annotation.NonNull;

import com.ericyl.utils.cryptographical.CryptoKeyable;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AESCryptoKey implements CryptoKeyable<SecretKeySpec> {

    private SecretKey key;
    private byte[] keys;


    public AESCryptoKey(@NonNull SecretKey key) {
        this.key = key;
    }

    public AESCryptoKey(@NonNull byte[] keys) {
        this.keys = keys;
    }

    @Override
    public SecretKeySpec getKey() {
        if (key != null)
            keys = key.getEncoded();
        return new SecretKeySpec(keys, "AES");
    }
}
