package com.big0soft.websocket;

import androidx.annotation.NonNull;

public class TAGs {

    @NonNull
    public static final String TAG = TAGs.class.getPackage().getName();

    public static String TAG(Class<?> cClass) {
        return cClass.getSimpleName();
    }

    public static String TAGPackage(Class<?> cClass) {
        return cClass.getPackage().getName()+"."+cClass.getSimpleName();
    }
}
