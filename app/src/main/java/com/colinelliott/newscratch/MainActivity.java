package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import com.wearnotch.service.NotchAndroidService;
import com.wearnotch.service.common.NotchCallback;
import com.wearnotch.service.common.NotchError;
import com.wearnotch.service.common.NotchProgress;
import com.wearnotch.service.network.NotchService;

public class MainActivity extends AppCompatActivity {
    //protected NotchService notchService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Go to Welcome Screen
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.welcomeContainer, new WelcomeScreen()).commit();




        //Setup Notch SDK
    /*Intent controlServiceIntent = new Intent(this, NotchAndroidService.class);
    startService(controlServiceIntent);
    bindService(controlServiceIntent, (ServiceConnection) this, BIND_AUTO_CREATE);
    notchService.setLicense(license: "9bIEo9O0D8kd5n5Oi4SY");
*/
    }


    /*public void onServiceConnected(ComponentName name,IBinder service) {
        if (service instanceof NotchService) {
            notchService = (NotchService) service;
        }
        // Start using the SDK
    }*/
}



