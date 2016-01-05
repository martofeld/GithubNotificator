package com.mfeldsztejn.githubnotificator.application;

import android.app.Application;

import quickutils.core.QuickUtils;

/**
 * Created by mfeldsztejn on 4/1/16.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        QuickUtils.init(this);
    }
}
