package com.ericyl.utils.cryptographical;

import android.support.annotation.NonNull;

public interface Cryptographical {

    String encrypt(@NonNull String originalText);

    String decrypt(@NonNull String cipherText);
}