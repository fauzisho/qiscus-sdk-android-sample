package com.qiscus.sdksample.qiscussamplesdk;

import android.app.Application;

import com.qiscus.sdk.Qiscus;

/**
 * Created by evanpurnama on 9/9/16.
 */
public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Qiscus.init(this, "DRAGONFLY");
    }
}