package com.ericyl.utils.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ericyl.utils.cryptographical.Cryptographical;
import com.ericyl.utils.cryptographical.aes.AESCryptoImpl;
import com.ericyl.utils.cryptographical.aes.AESCryptoKey;
import com.ericyl.utils.cryptographical.exception.AESCryptoException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AESKeyStoreUtils {

    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 32;

    public static byte[] getRandomIv() {
        byte[] iv = new byte[IV_SIZE];
        getRandomBytes(iv);
        return iv;
    }

    public static byte[] getRandomSalt() {
        byte[] salt = new byte[KEY_SIZE];
        getRandomBytes(salt);
        return salt;
    }

    private static void getRandomBytes(byte[] bytes) {
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
    }

    public static SecretKey getSecretKey(@NonNull String password, @NonNull byte[] salt) throws AESCryptoException {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    1000, KEY_SIZE * 8);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

    @Deprecated
    public static SecretKey getSecretKey(@NonNull String password) throws AESCryptoException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            secureRandom.setSeed(password.getBytes("UTF-8"));
            keyGen.init(128, secureRandom);
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

    public static void saveInKeyStore(@NonNull Context context, @NonNull SecretKey key, @Nullable String type, @NonNull String fileName, @NonNull String keyStorePwd, @NonNull String secretKeyEntryAlias, @NonNull String secretKeyEntryPwd) throws AESCryptoException {
        FileOutputStream fos = null;
        try {
            if (TextUtils.isEmpty(type))
                type = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(type);
            keyStore.load(null, null);

            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
            keyStore.setEntry(secretKeyEntryAlias, secretKeyEntry,
                    new KeyStore.PasswordProtection(secretKeyEntryPwd.toCharArray()));
            fos = new FileOutputStream(context.getExternalFilesDir("file") + File.separator + fileName);
            keyStore.store(fos, keyStorePwd.toCharArray());
        } catch (Exception e) {
            throw new AESCryptoException(e);
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


    }

    public static void saveAsKey(@NonNull Context context, @NonNull SecretKey key, @NonNull String fileName) throws AESCryptoException {
        saveAsKey(context, key.getEncoded(), fileName);
    }

    public static void saveAsKey(@NonNull Context context, @NonNull byte[] key, @NonNull String fileName) throws AESCryptoException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(context.getExternalFilesDir("file") + File.separator + fileName);
            fos.write(key);
            fos.flush();
        } catch (Exception e) {
            throw new AESCryptoException(e);
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static SecretKey readSecretKey(@NonNull Context context, @NonNull String fileName, @Nullable String type, @NonNull String keyStorePwd, @NonNull String secretKeyEntryAlias, @NonNull String secretKeyEntryPwd) throws AESCryptoException {
        InputStream fis = null;
        try {
            fis = context.getResources().getAssets().open(fileName);
            if (TextUtils.isEmpty(type))
                type = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(type);
            keyStore.load(fis, keyStorePwd.toCharArray());
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(secretKeyEntryAlias, new KeyStore.PasswordProtection(secretKeyEntryPwd.toCharArray()));
            return secretKeyEntry.getSecretKey();
        } catch (Exception e) {
            throw new AESCryptoException(e);
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    public static byte[] readSecretKeyBytes(@NonNull Context context, @NonNull String fileName, @Nullable String type, @NonNull String keyStorePwd, @NonNull String secretKeyEntryAlias, @NonNull String secretKeyEntryPwd) throws AESCryptoException {
        SecretKey secretKey = readSecretKey(context, fileName, type, keyStorePwd, secretKeyEntryAlias, secretKeyEntryPwd);
        return secretKey.getEncoded();
    }


    public static String encrypt(@NonNull SecretKey secretKey, @NonNull byte[] iv, @NonNull String plainText) throws AESCryptoException {
        try {
            Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(secretKey), iv);
            return crypto.encrypt(plainText);
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

    public static String decrypt(@NonNull SecretKey secretKey, @NonNull byte[] iv, @NonNull String cipherText) throws AESCryptoException {
        try {
            Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(secretKey), iv);
            return crypto.decrypt(cipherText);
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

    public static String encrypt(@NonNull byte[] key, @NonNull byte[] iv, @NonNull String plainText) throws AESCryptoException {
        try {
            Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(key), iv);
            return crypto.encrypt(plainText);
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

    public static String decrypt(@NonNull byte[] key, @NonNull byte[] iv, @NonNull String cipherText) throws AESCryptoException {
        try {
            Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(key), iv);
            return crypto.decrypt(cipherText);
        } catch (Exception e) {
            throw new AESCryptoException(e);
        }
    }

}
