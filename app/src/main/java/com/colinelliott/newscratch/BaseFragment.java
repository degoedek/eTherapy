package com.colinelliott.newscratch;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.wearnotch.service.network.NotchService;

/**
 * Base class for fragments.
 */
public class BaseFragment extends Fragment implements NotchServiceConnection {

    protected Context mApplicationContext;
    protected NotchService mNotchService;

    protected void bindNotchService() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.addNotchServiceConnection(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = getActivity().getApplicationContext();
    }



    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


    /**
     * Override point for subclasses.
     */
    @Override
    public void onServiceConnected(NotchService notchService) {
        mNotchService = notchService;
    }

    /**
     * Override point for subclasses.
     */
    @Override
    public void onServiceDisconnected() {
        mNotchService = null;
    }


}
