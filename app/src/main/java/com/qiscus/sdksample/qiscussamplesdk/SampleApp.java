package com.qiscus.sdksample.qiscussamplesdk;

import android.app.Application;

import com.qiscus.sdk.Qiscus;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupQiscus();
    }

    private void setupQiscus() {
        Qiscus.init(this, "dragongo");

        Qiscus.getChatConfig()
                .setSwipeRefreshColorScheme(R.color.colorPrimary, R.color.colorAccent)
                .setLeftBubbleColor(R.color.leftBubble)
                .setLeftBubbleTextColor(R.color.qiscus_primary_text)
                .setLeftBubbleTimeColor(R.color.qiscus_secondary_text)
                .setLeftLinkTextColor(R.color.qiscus_primary_text)
                .setLeftProgressFinishedColor(R.color.colorPrimary)
                .setRightBubbleColor(R.color.colorPrimaryLight)
                .setRightProgressFinishedColor(R.color.colorPrimaryLight)
                .setSelectedBubbleBackgroundColor(R.color.colorPrimary)
                .setReadIconColor(R.color.colorPrimary)
                .setAppBarColor(R.color.colorPrimary)
                .setStatusBarColor(R.color.colorPrimaryDark)
                .setSendButtonIcon(R.drawable.ic_default_send)
                .setStopRecordIcon(R.drawable.ic_send_record)
                .setCancelRecordIcon(R.drawable.ic_cancel_record);
    }
}