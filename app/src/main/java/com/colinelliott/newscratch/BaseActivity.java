package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.wearnotch.service.NotchAndroidService;
import com.wearnotch.service.network.NotchService;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements ServiceConnection {
    private final List<NotchServiceConnection> mNotchServiceConnections =
            new ArrayList<NotchServiceConnection>();

    private boolean mServiceBound;
    private ComponentName mNotchServiceComponent;
    protected NotchService mNotchService;

    public void addNotchServiceConnection(NotchServiceConnection c) {
        if (!mServiceBound) {
            mServiceBound = true;
            Intent controlServiceIntent = new Intent(this, NotchAndroidService.class);
            bindService(controlServiceIntent, this, BIND_AUTO_CREATE);
        }

        if (!mNotchServiceConnections.contains(c)) {
            mNotchServiceConnections.add(c);
        }

        if (mNotchService != null) {
            c.onServiceConnected(mNotchService);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    protected void onDestroy() {
        if (mServiceBound) {
            mServiceBound = false;
            unbindService(this);
        }

        super.onDestroy();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service instanceof NotchService) {
            mNotchServiceComponent = name;
            mNotchService = (NotchService) service;
            fireNotchServiceChange();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (name.equals(mNotchServiceComponent)) {
            mNotchServiceComponent = null;
            mNotchService = null;
            fireNotchServiceChange();
        }
    }

    private void fireNotchServiceChange() {
        for (NotchServiceConnection c : mNotchServiceConnections) {
            if (mNotchService != null) {
                c.onServiceConnected(mNotchService);
            } else {
                c.onServiceDisconnected();
            }
        }
    }
}