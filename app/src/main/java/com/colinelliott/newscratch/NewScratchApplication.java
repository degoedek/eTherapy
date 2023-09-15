package com.colinelliott.newscratch;

import android.app.Application;
import android.view.View;

public class NewScratchApplication extends Application {

    private static NewScratchApplication mInst;

    public static NewScratchApplication getInst() {
        return mInst;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInst = this;
    }
}
