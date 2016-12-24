package com.ericyl.utils.cryptographical.aes;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.ericyl.utils.cryptographical.CryptoKeyable;
import com.ericyl.utils.cryptographical.Cryptographical;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptoImpl implements Cryptographical {

    private IvParameterSpec ivSpec;
    private SecretKeySpec key;
    private Cipher cipher;

    private AESCryptoImpl(SecretKeySpec key, byte[] iv) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        this.key = key;
        this.cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
        this.ivSpec = new IvParameterSpec(iv);
    }

    private AESCryptoImpl(SecretKeySpec key, String Algorithm, IvParameterSpec ivSpec) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        this.key = key;
        this.cipher = Cipher.getInstance(Algorithm);
        this.ivSpec = ivSpec;
    }

    public static Cryptographical initialize(CryptoKeyable key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        return new AESCryptoImpl((SecretKeySpec) key.getKey(), iv);
    }

    public String encrypt(@NonNull String originalText) {
        try {
            initCipher(Cipher.ENCRYPT_MODE);
            return Base64.encodeToString(cipher.doFinal(originalText.getBytes("UTF-8")), Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(@NonNull String cipherText) {
        try {
            initCipher(Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP)),
                    "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initCipher(int opmode) throws InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(opmode, key, ivSpec);
    }
}