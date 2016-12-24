package com.ericyl.utils.cryptographical.exception;


public class AESCryptoException extends Exception {

    public AESCryptoException() {
    }

    public AESCryptoException(String message) {
        super(message);
    }

    public AESCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AESCryptoException(Throwable cause) {
        super(cause);
    }

}
