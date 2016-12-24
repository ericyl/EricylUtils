package com.ericyl.utils.cryptographical;

import java.security.Key;

public interface CryptoKeyable<T extends Key> {

    T getKey();
}