package com.colinelliott.newscratch;

import android.app.Application;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.internal.Util;
import com.wearnotch.service.common.NotchCallback;
import com.wearnotch.service.common.NotchError;
import com.wearnotch.service.common.NotchProgress;

public class EmptyCallback<T> implements NotchCallback<T> {

    @Override
    public void onProgress(NotchProgress notchProgress) {

    }

    @Override
    public void onSuccess(T t) {
        //Toast.makeText(,"Success!",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onFailure(NotchError notchError) {
        //Toast.makeText(NewScratchApplication.getInst(),"Error! Try Again.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancelled() {
    }
}
