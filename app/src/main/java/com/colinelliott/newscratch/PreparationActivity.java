package com.colinelliott.newscratch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.wearnotch.db.NotchDataBase;
import com.wearnotch.db.model.Device;
import com.wearnotch.framework.ActionDevice;
import com.wearnotch.framework.DiagnosticNetworkMap;
import com.wearnotch.framework.Measurement;
import com.wearnotch.framework.MeasurementType;
import com.wearnotch.framework.NotchChannel;
import com.wearnotch.framework.NotchColor;
import com.wearnotch.framework.NotchNetwork;
import com.wearnotch.framework.Workout;
import com.wearnotch.service.NotchAndroidService;
import com.wearnotch.service.common.Cancellable;
import com.wearnotch.service.common.NotchCallback;
import com.wearnotch.service.common.NotchError;
import com.wearnotch.service.common.NotchLogger;
import com.wearnotch.service.common.NotchProgress;
import com.wearnotch.service.common.Stoppable;
import com.wearnotch.service.network.NotchBluetoothDevice;
import com.wearnotch.service.network.NotchService;
import com.wearnotch.service.network.impl.android.d;
import com.wearnotch.service.network.impl.g;
import com.wearnotch.service.network.impl.o;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
//extends AppCompatActivity

public class PreparationActivity extends BaseActivity implements NotchServiceConnection{
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    protected NotchService notchService = new NotchService() {
        @Override
        public void setLicense(@Nullable String s) {

        }

        @Nullable
        @Override
        public String getLicense() {
            return null;
        }

        @Override
        public boolean isExtendedLicense(@Nullable String s) {
            return false;
        }

        @NotNull
        @Override
        public Cancellable pair(@NotNull NotchCallback<? super Device> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable syncPairedDevices(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable deletePairedDevices(@Nullable Device device, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable scan(@NotNull NotchCallback<? super List<NotchBluetoothDevice>> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable init(@NotNull Workout workout, @NotNull NotchCallback<? super NotchNetwork> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable init(@Nullable NotchChannel notchChannel, @NotNull Workout workout, @NotNull NotchCallback<? super NotchNetwork> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable uncheckedInit(@NotNull NotchCallback<? super NotchNetwork> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable uncheckedInit(@Nullable NotchChannel notchChannel, @NotNull NotchCallback<? super NotchNetwork> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable checkBatteryStatus(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @Override
        public void stopDiscovery() {

        }

        @NotNull
        @Override
        public Cancellable color(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable colorSingle(@NotNull NotchColor notchColor, @Nullable Integer integer, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable colorSingle(@Nullable ActionDevice actionDevice, @NotNull NotchColor notchColor, @Nullable Integer integer, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public void refreshWorkout(@NotNull Workout workout) {

        }

        @NotNull
        @Override
        public Cancellable disconnect(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @Nullable
        @Override
        public NotchNetwork getNetwork() {
            return null;
        }

        @NotNull
        @Override
        public Cancellable erase(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable erase(@Nullable ActionDevice actionDevice, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable configureSteady(@Nullable MeasurementType measurementType, boolean b, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable steady(@NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable configureCalibration(boolean b, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable calibration(@NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @Override
        public boolean isSteadyValid() {
            return false;
        }

        @NotNull
        @Override
        public Cancellable configureCapture(boolean b, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable capture(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable capture(@Nullable File file, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable stop(@NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable configureTimedCapture(long l, boolean b, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Stoppable timedCapture(@NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Stoppable timedCapture(boolean b, @NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @Override
        public boolean isCapturing() {
            return false;
        }

        @NotNull
        @Override
        public Cancellable download(@NotNull File file, @NotNull Measurement measurement, @NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable download(@NotNull File file, boolean b, @NotNull Measurement measurement, @NotNull NotchCallback<? super Measurement> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable getCalibrationData(@NotNull NotchCallback<? super Boolean> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable getSteadyData(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable getSensorTestData(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable shutDown(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable shutDown(@Nullable ActionDevice actionDevice, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable softReset(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable diagnosticInit(boolean b, @Nullable NotchColor[] notchColors, @NotNull NotchCallback<? super DiagnosticNetworkMap> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable diagnosticInit(boolean b, @NotNull NotchCallback<? super DiagnosticNetworkMap> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable changeChannel(@NotNull NotchChannel notchChannel, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable changeChannel(@Nullable ActionDevice actionDevice, @NotNull NotchChannel notchChannel, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public List<Device> findAllDevices() {
            return null;
        }

        @Override
        public boolean resetAdapterIfNeeded(boolean b, @NotNull NotchCallback<? super Void> notchCallback) {
            return false;
        }

        @Override
        public void setLogger(@Nullable NotchLogger notchLogger) {

        }

        @Override
        public void setExperimentalFlags(long l) {

        }

        @NotNull
        @Override
        public o getHandler() {
            return null;
        }

        @Override
        public void close() {

        }

        @NotNull
        @Override
        public Cancellable uploadMeasurement(String s, @NotNull String s1, @NotNull NotchCallback<? super String> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable firmwareUpdate(@NotNull NotchBluetoothDevice notchBluetoothDevice, @NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @NotNull
        @Override
        public Cancellable dataSync(@NotNull NotchCallback<? super Void> notchCallback) {
            return null;
        }

        @Override
        public boolean isFirmwareUpdatePaused() {
            return false;
        }

        @Override
        public void resumeFirmwareUpdate() {

        }

        @Override
        public void serializeMeasurement(@NotNull Measurement measurement, @NotNull File file, @NotNull NotchCallback<? super Void> notchCallback) {

        }

        @Override
        public void deserializeMeasurement(@NotNull File file, @NotNull NotchCallback<? super Measurement> notchCallback) {

        }
    };
    private static final String DEFAULT_USER_LICENSE = "9bIEo9O0D8kd5n5Oi4SY";
    private static final int REQUEST_ALL_PERMISSION = 1;
    private NotchChannel selectedChannel;
    private NotchDataBase notchDataBase;
    Button pair;
    TextView pairedNotches, textSelectedChannel, textCurrentNetwork;
    private static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    String user;
    ProgressBar progressBar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);

        addNotchServiceConnection(this);
            //
        notchDataBase = NotchDataBase.getInst();
        //Initialize
        pairedNotches = findViewById(R.id.text_paired_notches);
        textCurrentNetwork = findViewById(R.id.text_current_network);
        progressBar = findViewById(R.id.progress_bar);
        //Start Service
        Intent controlServiceIntent = new Intent(this, NotchAndroidService.class);
        startService(controlServiceIntent);
        //controlServiceIntent.putExtra("MOCK", true);
        bindService(controlServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        //notchService.setLicense(DEFAULT_USER_LICENSE);
        //mNotchService.setLicense("9bIEo9O0D8kd5n5Oi4SY");
        if (!hasPermissions(this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION);
        }

        //Pairing Notches
        pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSelectedChannel.setText("Click worked");
                progressBar.setVisibility(View.VISIBLE);
                //notchservice.pair
                mNotchService.pair(new NotchCallback<Device>() {
                    @Override
                    public void onProgress(@NotNull NotchProgress notchProgress) {
                    }

                    @Override
                    public void onSuccess(@Nullable Device device) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        //pairedNotches.setText("Success!");
                        Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                        updatePairedDevices(notchService.getLicense());
                        shutdown();
                    }

                    @Override
                    public void onFailure(@NotNull NotchError notchError) {
                        //pairedNotches.setText("Failure");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        Toast.makeText(getApplicationContext(),"Error! Try Again.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled() {

                    }

                });

            }
        });


    }

    /*public void onServiceConnected(ComponentName name, IBinder service) {
        if (service instanceof NotchService) {
            notchService = (NotchService) service;
        }
        // Start using the SDK
    }*/

    public void updatePairedDevices(final String user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(notchDataBase==null)
                    notchDataBase = NotchDataBase.getInst();
                StringBuilder sb = new StringBuilder();
                sb.append("Device list:\n");
                for (Device device : notchDataBase.findAllDevices(user)) {
                    sb.append("Notch ").append(device.getNotchDevice().getNetworkId()).append(" (");
                    sb.append(device.getNotchDevice().getDeviceMac()).append(") ");
                    sb.append("FW: " + device.getSwVersion() + ", ");
                    sb.append("Ch: " + device.getChannel().toChar() + "\n");
                }
                pairedNotches.setText(sb.toString());
                if(selectedChannel==null) {
                    textSelectedChannel.setText("SELECTED CHANNEL: NULL");
                }
                else {
                    textSelectedChannel.setText("SELECTED CHANNEL: " + selectedChannel);
                }
            }
        });

    }

    void shutdown() {
        //Insert loading progress bar
        mNotchService.shutDown(new EmptyCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateNetwork();
                super.onSuccess(aVoid);
            }
        });
    }

    private void updateNetwork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("Current network:\n");
                if (mNotchService.getNetwork() != null) {
                    for (ActionDevice device : mNotchService.getNetwork().getDeviceSet()) {
                        sb.append(device.getNetworkId()).append(", ");
                    }
                }
                textCurrentNetwork.setText(sb.toString());
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ALL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permissions granted!", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissions(permissions, REQUEST_ALL_PERMISSION);
                    Toast.makeText(getApplicationContext(), "Permissions granted!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public static boolean hasPermissions(Context context, String permissions[]) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onServiceConnected(NotchService notchService) {
        //this.notchService = notchService;
        mNotchService = notchService;
    }

    @Override
    public void onServiceDisconnected() {
        //this.notchService = null;
        mNotchService = null;
    }

    Runnable mSetDefaultUser = new Runnable() {
        @Override
        public void run() {
            if (mNotchService != null && user == null) {
                //user = DEFAULT_USER_LICENSE;
                user = "tzzTodvfEuSvTXc8tBCR";
                if (DEFAULT_USER_LICENSE.length() > 0) {
                    updatePairedDevices(user);
                    mNotchService.setLicense(user);
                }
            }
        }
    };
}