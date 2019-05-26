package com.orlinskas.bookread;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        AppContext.mContext = mContext;
    }
}