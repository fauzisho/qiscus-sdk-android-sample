package com.qiscus.sdksample.qiscussamplesdk;

import android.app.Application;

import com.qiscus.sdk.chat.core.Qiscus;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupQiscus();
    }

    private void setupQiscus() {
        Qiscus.Companion.init(this, "dragongo");
    }
}