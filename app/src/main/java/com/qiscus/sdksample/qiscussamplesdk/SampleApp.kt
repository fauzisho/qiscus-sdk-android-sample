package com.qiscus.sdksample.qiscussamplesdk

import android.app.Application

import com.qiscus.sdk.Qiscus

/**
 * Created by evanpurnama on 9/9/16.
 */
class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Qiscus.init(this, "dragongo")
    }
}