package com.colinelliott.newscratch;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wearnotch.db.NotchDataBase;
import com.wearnotch.db.model.Device;
import com.wearnotch.framework.ActionDevice;
import com.wearnotch.framework.ColorPair;
import com.wearnotch.framework.Measurement;
import com.wearnotch.framework.MeasurementType;
import com.wearnotch.framework.NotchChannel;
import com.wearnotch.framework.NotchNetwork;
import com.wearnotch.framework.Skeleton;
import com.wearnotch.framework.Workout;
import com.wearnotch.framework.visualiser.VisualiserData;
import com.wearnotch.internal.util.IOUtil;
import com.wearnotch.service.common.Cancellable;
import com.wearnotch.service.common.NotchCallback;
import com.wearnotch.service.common.NotchError;
import com.wearnotch.service.common.NotchProgress;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PairFragment extends BaseFragment {

    public static final String EXTRA_STRING_XYZ = "com.colinelliott.newscratch.EXTRA_STRING_XYZ";
    public static final String EXTRA_GONIOMETER_FLAG_PAIR = "com.colinelliott.newscratch.EXTRA_GONIOMETER_FLAG_PAIR";
    public static final String EXTRA_JOINT_NAME = "com.colinelliott.newscratch.EXTRA_JOINT_NAME";


    private Activity mActivity,eTherapyActivity;
    private NotchDataBase mDB;
    private NotchChannel mSelectedChannel;
    private Workout mWorkout;
    private Measurement mCurrentMeasurement;
    private static final int REQUEST_ALL_PERMISSION = 1;
    private static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String DEFAULT_USER_LICENSE = "9bIEo9O0D8kd5n5Oi4SY";  // License Code tzzTodvfEuSvTXc8tBCR
    private static final String TAG = PairFragment.class.getSimpleName();
    private String mUser;
    private boolean mChangingChannel;
    private boolean goniometerFlag = false;
    private boolean playFlag = false;
    private boolean isCalibrating = false;
    private boolean calibrated = false;
    private boolean gotCalibrationData = false;
    private final boolean mRealTime = true;
    private boolean firstChangedChannel = false;
    private enum State {CALIBRATION, STEADY, CAPTURE}
    private State mState;
    private static final long CALIBRATION_TIME = 7000L;
    private VisualiserData mRealTimeData;
    android.app.AlertDialog channelDialog;
    String xyzString, joint, side;
    List<CreateExerciseNote> exercises;
    String[] exerciseNames;
    int selectedExercisePos = -1;
    private int rawResource;
    private ToggleButton rightLeft;
    Button standardButton, customButton, playButton;
    boolean customFlag = false;
    Intent gData;
    TherapistDatabase theraDb;
    CreateExerciseNoteDatabase exerciseDb;
    int exerciseId;

    TextView textPairedNotches, mSelectedChannelTxt, textCurrentNetwork, text, pairTitleText, textLastCalibration, textNotchPlacement;
    ImageView mDockImg;
    AnimationDrawable mDockAnimation;
    Button connect, pair, calibrate, changeChannel, startCalibration, finishCalibration, setExercise, startSteady, finishSteady, start, goniometer;
    ProgressBar progressBar;
    Spinner spinner, playSpinner;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    RelativeLayout pairButtonLayout, calibrateButtonLayout, channelButtonLayout, exerciseButtonLayout;
    LinearLayout topMenu;
    ConstraintLayout pairScreen, calibrateScreen, channelScreen, exerciseScreen;
    Cancellable c = new Cancellable() {
        @Override
        public void cancel() {
            //Do nothing
        }
    };


    public static PairFragment newInstance() {
        return new PairFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindNotchService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = getActivity().getApplicationContext();
        mActivity = getBaseActivity();
        mDB = NotchDataBase.getInst();
        if (!hasPermissions(mActivity, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pair, container, false);
        //Initialize
        progressBar = v.findViewById(R.id.progress_bar);
        mDockImg = v.findViewById(R.id.dock_image);
        text = v.findViewById(R.id.counter_text);
        //Top Menu
        topMenu = v.findViewById(R.id.top_menu);
        pairTitleText = v.findViewById(R.id.pair_title_text);
        pairButtonLayout = v.findViewById(R.id.pair_button_layout);
        calibrateButtonLayout = v.findViewById(R.id.calibrate_button_layout);
        channelButtonLayout = v.findViewById(R.id.channel_button_layout);
        exerciseButtonLayout = v.findViewById(R.id.exercise_button_layout);
        textCurrentNetwork = v.findViewById(R.id.text_current_network);
        //Screens: Constraint Layouts
        pairScreen = v.findViewById(R.id.pair_screen_layout);
        calibrateScreen = v.findViewById(R.id.calibrate_screen_layout);
        channelScreen = v.findViewById(R.id.channel_screen_layout);
        exerciseScreen = v.findViewById(R.id.exercise_screen_layout);
        //Layout-Specific Elements
        textPairedNotches = v.findViewById(R.id.text_paired_notches);
        textLastCalibration = v.findViewById(R.id.text_last_calibration);
        spinner = v.findViewById(R.id.spinner_select_exercise);
        playSpinner = v.findViewById(R.id.spinner_play);
        textNotchPlacement = v.findViewById(R.id.text_notch_placement);
        //Buttons
        pair = v.findViewById(R.id.btn_pair);
        connect = v.findViewById(R.id.btn_connect);
        calibrate = v.findViewById(R.id.btn_calibrate);
        changeChannel = v.findViewById(R.id.btn_change_channel);
        startCalibration = v.findViewById(R.id.btn_start_calibration);
        finishCalibration = v.findViewById(R.id.btn_finish_calibration);
        setExercise = v.findViewById(R.id.btn_set_exercise);
        startSteady = v.findViewById(R.id.btn_start_steady);
        finishSteady = v.findViewById(R.id.btn_get_steady_data);
        start = v.findViewById(R.id.btn_start_therapy);
        goniometer = v.findViewById(R.id.btn_start_goniometer);
        rightLeft = v.findViewById(R.id.toggle_right_left);

        standardButton = v.findViewById(R.id.standardModeButton); //deprecated
        standardButton.setVisibility(View.GONE);
        customButton = v.findViewById(R.id.customModeButton);
        playButton = v.findViewById(R.id.playButton);

        getPatientData();
        buildChannelDialog();
        pair();
        connect();
        changeChannel();
        calibrate();
        modeSelect();
        selectExercise();
        goniometer();
        begin();
        play();
        playSelect();
        mHandler.postDelayed(mSetDefaultUser, 1000L);

        // Animation
        mDockImg.setBackgroundResource(R.drawable.sensor_anim);
        mDockAnimation = (AnimationDrawable) mDockImg.getBackground();
        mDockImg.setVisibility(View.INVISIBLE);

        //sets begin and goniometer buttons invisible until steady is complete
        start.setVisibility(View.GONE);
        goniometer.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        playSpinner.setVisibility(View.GONE);

        return v;
    }

    private void getPatientData() {
        xyzString = getActivity().getIntent().getStringExtra(CreateExerciseActivity.EXTRA_XYZ_STRING);
        goniometerFlag = getActivity().getIntent().getBooleanExtra(CreateExerciseActivity.EXTRA_GONIOMETER_FLAG,false);
        gData = new Intent();
        gData.putExtra(EXTRA_STRING_XYZ,xyzString);
        gData.putExtra(EXTRA_GONIOMETER_FLAG_PAIR,goniometerFlag);
        gData.putExtra("Play Flag",playFlag);
    }

    private void setTopMenu() {
        pairButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calibrateScreen.getVisibility()==View.VISIBLE||channelScreen.getVisibility()==View.VISIBLE||exerciseScreen.getVisibility()==View.VISIBLE) {
                    calibrateScreen.setVisibility(View.GONE);channelScreen.setVisibility(View.GONE);exerciseScreen.setVisibility(View.GONE);
                }
                pairScreen.setVisibility(View.VISIBLE);
            }
        });
        calibrateButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pairScreen.getVisibility()==View.VISIBLE||channelScreen.getVisibility()==View.VISIBLE||exerciseScreen.getVisibility()==View.VISIBLE) {
                    pairScreen.setVisibility(View.GONE);channelScreen.setVisibility(View.GONE);exerciseScreen.setVisibility(View.GONE);
                }
                calibrateScreen.setVisibility(View.VISIBLE);
            }
        });
        channelButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calibrateScreen.getVisibility()==View.VISIBLE||pairScreen.getVisibility()==View.VISIBLE||exerciseScreen.getVisibility()==View.VISIBLE) {
                    calibrateScreen.setVisibility(View.GONE);pairScreen.setVisibility(View.GONE);exerciseScreen.setVisibility(View.GONE);
                }
                channelScreen.setVisibility(View.VISIBLE);
            }
        });
        exerciseButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calibrateScreen.getVisibility()==View.VISIBLE||channelScreen.getVisibility()==View.VISIBLE||pairScreen.getVisibility()==View.VISIBLE) {
                    calibrateScreen.setVisibility(View.GONE);channelScreen.setVisibility(View.GONE);pairScreen.setVisibility(View.GONE);
                }
                exerciseScreen.setVisibility(View.VISIBLE);
            }
        });
    }

    public void capture() {
        eTherapyActivity = null;
        if (mRealTime) {
            resetButtons();
            c = mNotchService.capture(new NotchCallback<Void>() {
                @Override
                public void onProgress(NotchProgress progress) {
                    if (progress.getState() == NotchProgress.State.REALTIME_UPDATE) {
                        mRealTimeData = (VisualiserData) progress.getObject();
                        if (eTherapyActivity == null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    eTherapyActivity = new TherapyActivity();
                                }
                            });
                            Intent i = TherapyActivity.createIntent(getActivity(), mRealTimeData, mRealTime);
                            i.putExtra(EXTRA_JOINT_NAME,joint);
                            i.putExtra(EXTRA_GONIOMETER_FLAG_PAIR,goniometerFlag);
                            i.putExtra("Play Flag", playFlag);
                            if (playFlag){
                                i.putExtra("Exercise ID" , exerciseId);
                                // i.putExtra("Exercise ID",exerciseId);
                            }
                            startActivity(i);
                        } else {
                            EventBus.getDefault().post(mRealTimeData);
                        }
                    }
                }

                @Override
                public void onSuccess(Void nothing) {
                }

                @Override
                public void onFailure(NotchError notchError) {
                    toast("Error!");
                }

                @Override
                public void onCancelled() {
                    toast("Real-time measurement stopped!");
                }
            });
        } else {
            mNotchService.timedCapture(new EmptyCallback<Measurement>() {
                @Override
                public void onSuccess(Measurement measurement) {
                    mCurrentMeasurement = measurement;
                    toast("Capture finished");
                    doneProgress();
                }
            });
        }
    }
    private void configureCapture() {
        inProgress();
        mNotchService.configureCapture(false, new EmptyCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                super.onSuccess(unused);
                toast("Capture Configured!");
                doneProgress();
                mState = PairFragment.State.CAPTURE;
                mCountDown.start();
            }

            @Override
            public void onFailure(NotchError notchError) {
                super.onFailure(notchError);
                doneProgress();
                toast("Failed Capture Configuration!");
            }
        });
    }

    public void modeSelect(){
        setExercise.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        standardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.GONE);
                setExercise.setVisibility(View.VISIBLE);
                textNotchPlacement.setVisibility(View.VISIBLE);
                standardButton.setVisibility(View.GONE);
                customButton.setVisibility(View.GONE);
            }
        });

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        joint = parent.getItemAtPosition(position).toString();
                        if (position >= 0) {
                            rightLeft.setVisibility(View.GONE);
                        }
                        if (joint.equals("Ankle"))
                            rawResource = R.raw.ankle;
                        else if (joint.equals("Cervical Spine"))
                            rawResource = R.raw.neck;
                        else if (joint.equals("Elbow")) {
                            rawResource = R.raw.right_elbow;
                            rightLeft.setVisibility(View.VISIBLE);
                        }
                        else if (joint.equals("Hip"))
                            rawResource = R.raw.hip;
                        else if (joint.equals("Knee"))
                            rawResource = R.raw.knee;
                        else if (joint.equals("Lumbar Spine"))
                            rawResource = R.raw.lumbar_spine;
                        else if (joint.equals("Neck"))
                            rawResource = R.raw.neck;
                        else if (joint.equals("Shoulder"))
                            rawResource = R.raw.shoulder;
                        else if (joint.equals("Wrist")){
                            rawResource = R.raw.right_wrist;
                            rightLeft.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        joint = "elbow";
                        rawResource = R.raw.elbow;
                    }
                });
                startSteady.setVisibility(View.VISIBLE);
                standardButton.setVisibility(View.GONE);
                customButton.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                customFlag = true;
                setExercise.performClick();
                textNotchPlacement.setVisibility(View.GONE);
                toast("Please wait for the sensors to change color");
            }
        });
    }


    public void begin () {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goniometerFlag = false;
                playFlag = false;
                Skeleton skeleton;
                try {
                    skeleton = Skeleton.from(new InputStreamReader(mApplicationContext.getResources().openRawResource(R.raw.skeleton_male), "UTF-8"));
                    Workout workout = Workout.from("Demo_config", skeleton, IOUtil.readAll(new InputStreamReader(mApplicationContext.getResources().openRawResource(rawResource))));// I changed this to be the right arm instead of the one on chest
                    if (mRealTime) {
                        workout = workout.withRealTime(true);
                        workout = workout.withMeasurementType(MeasurementType.STEADY_SKIP);
                    }
                    mWorkout = workout;
                    inProgress();
                    mNotchService.init(mSelectedChannel, workout, new EmptyCallback<NotchNetwork>() {
                        @Override
                        public void onSuccess(NotchNetwork notchNetwork) {
                            doneProgress();
                            updateNetwork();
                            super.onSuccess(notchNetwork);
                            configureCapture();
                        }

                        @Override
                        public void onFailure(NotchError notchError) {
                            toast("Connect more/less notches!");
                            super.onFailure(notchError);
                        }
                    });
                    toast("Loading, Please Hold");
                    //resetButtons();
                } catch (Exception e) {
                    Log.e(TAG, "Error while loading skeleton file!", e);
                    toast("Error Loading Skeleton!");
                }
                //Configure Capture
            }
        });

    }

    private void goniometer() {
        goniometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goniometerFlag = true;
                playFlag = false;
                Skeleton skeleton;
                try {
                    skeleton = Skeleton.from(new InputStreamReader(mApplicationContext.getResources().openRawResource(R.raw.skeleton_male), "UTF-8"));
                    Workout workout = Workout.from("Demo_config", skeleton, IOUtil.readAll(new InputStreamReader(mApplicationContext.getResources().openRawResource(rawResource))));// I changed this to be the right arm instead of the one on chest
                    if (mRealTime) {
                        workout = workout.withRealTime(true);
                        workout = workout.withMeasurementType(MeasurementType.STEADY_SKIP);
                    }
                    mWorkout = workout;
                    inProgress();
                    mNotchService.init(mSelectedChannel, workout, new EmptyCallback<NotchNetwork>() {
                        @Override
                        public void onSuccess(NotchNetwork notchNetwork) {
                            doneProgress();
                            updateNetwork();
                            super.onSuccess(notchNetwork);
                            configureCapture();
                        }

                        @Override
                        public void onFailure(NotchError notchError) {
                            toast("Connect more/less notches!");
                            super.onFailure(notchError);
                        }

                    });
                    toast("Loading, Please Hold");
                    rightLeft.setVisibility(View.GONE);
                    playButton.setVisibility(View.GONE);
                    playSpinner.setVisibility(View.GONE);
                    goniometer.setVisibility(View.GONE);


                } catch (Exception e) {
                    Log.e(TAG, "Error while loading skeleton file!", e);
                    toast("Error Loading Skeleton!");
                }
            }
        });
    }

    private void play(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExercisePos != -1) {
                    goniometerFlag = false;
                    playFlag = true;
                    exerciseId = exercises.get(selectedExercisePos).getId();
                    toast("Initiating "+exerciseNames[selectedExercisePos]);
                    Skeleton skeleton;
                    try {
                        skeleton = Skeleton.from(new InputStreamReader(mApplicationContext.getResources().openRawResource(R.raw.skeleton_male), "UTF-8"));
                        Workout workout = Workout.from("Demo_config", skeleton, IOUtil.readAll(new InputStreamReader(mApplicationContext.getResources().openRawResource(rawResource))));// I changed this to be the right arm instead of the one on chest
                        if (mRealTime) {
                            workout = workout.withRealTime(true);
                            workout = workout.withMeasurementType(MeasurementType.STEADY_SKIP);
                        }
                        mWorkout = workout;
                        inProgress();
                        mNotchService.init(mSelectedChannel, workout, new EmptyCallback<NotchNetwork>() {
                            @Override
                            public void onSuccess(NotchNetwork notchNetwork) {
                                doneProgress();
                                updateNetwork();
                                super.onSuccess(notchNetwork);
                                configureCapture();
                            }

                            @Override
                            public void onFailure(NotchError notchError) {
                                toast("Connect more/less notches!");
                                super.onFailure(notchError);
                            }

                        });
                        //toast("Loading, Please Hold");
                        rightLeft.setVisibility(View.GONE);
                        playButton.setVisibility(View.GONE);
                        playSpinner.setVisibility(View.GONE);
                        goniometer.setVisibility(View.GONE);

                    } catch (Exception e) {
                        Log.e(TAG, "Error while loading skeleton file!", e);
                        toast("Error Loading Skeleton!");
                    }
                }
                else{
                    toast("Please Select an Exercise to Initiate");
                }
            }
        });
    }

    private void playSelect(){
        theraDb = TherapistDatabase.getInstance(this.getActivity());
        exerciseDb = CreateExerciseNoteDatabase.getInstance(this.getActivity());
        SharedPreferences preferences = this.getContext().getSharedPreferences("checkbox",Context.MODE_PRIVATE);
        if(preferences.getBoolean("Is Therapist",false)) {
            int theraID = preferences.getInt("User ID", -1);
            exercises = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(theraID);
            //exercises = exerciseDb.createExerciseNoteDao().getAllCreateExerciseNotesList();
            exerciseNames = new String[exercises.size()];
            for (int i = 0; i < exercises.size(); i++) {
                exerciseNames[i] = exercises.get(i).getExerciseTitle();
            }
        }
        else{
            int patientID = preferences.getInt("User ID", -1);
            PatientDatabase patientDb = PatientDatabase.getInstance(this.getActivity());
            String patientAssignedExercises = patientDb.patientDao().findUserWithID(patientID).get(0).getPatientData();
            String exerciseIdList[] = patientAssignedExercises.split(",");
            //Storing the sets for therapy
            preferences.edit().putInt("Reps",Integer.parseInt((exerciseIdList[2]))).apply();

            //Must have atleast 1 assigned exercise (index of 3 is where exercise ID start within patient data)
            exercises = exerciseDb.createExerciseNoteDao().getExerciseById(Integer.parseInt(exerciseIdList[3]));
            if(exerciseIdList.length > 4) {
                for (int i = 4; i < exerciseIdList.length; i++) {
                    exercises.add(exerciseDb.createExerciseNoteDao().getExerciseById(Integer.parseInt(exerciseIdList[i])).get(0));
                }
            }
            //Separate loops so that the size of exerciseNames is based on the final size of exercises
            exerciseNames = new String[exercises.size()];
            for(int i = 0; i < exercises.size(); i++){
                exerciseNames[i] = exercises.get(i).getExerciseTitle();
            }
        }

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this.getActivity(),
                android.R.layout.simple_spinner_item, exerciseNames);
        adapter.setDropDownViewResource(R.layout.spinner_goniometer_dropdown);
        playSpinner.setAdapter(adapter);
        playSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExercisePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExercisePos = -1;
            }
        });

    }

    private void resetButtons(){
        spinner.setVisibility(View.GONE);
        setExercise.setVisibility(View.GONE);
        textNotchPlacement.setVisibility(View.GONE);
        startSteady.setVisibility(View.GONE);
        rightLeft.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        playSpinner.setVisibility(View.GONE);
        goniometer.setVisibility(View.GONE);
        start.setVisibility(View.GONE);
        customButton.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        customFlag = false;

    }

    private void changeTopMenu() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pairTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                calibrateButtonLayout.setVisibility(View.VISIBLE);
                channelButtonLayout.setVisibility(View.VISIBLE);
                exerciseButtonLayout.setVisibility(View.VISIBLE);
                setTopMenu();
            }
        });
    }

    private void configureSteady() {
        // Display bone-notch configuration
        StringBuilder sb = new StringBuilder();
        sb.append("Measured bones:\n\n");
        if (mWorkout != null) {
            for (Workout.BoneInfo info : mWorkout.getBones().values()) {
                ColorPair colors = info.getColor();
                sb.append(info.getBone().getName()).append(": ");
                sb.append(colors.getPrimary().toString());
                sb.append(colors.getPrimary().equals(colors.getSecondary()) ? "" : ", " + colors.getSecondary().toString());
                sb.append("\n");
            }
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textNotchPlacement.setText(sb.toString());
                textNotchPlacement.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            }
        });
        mNotchService.configureSteady(MeasurementType.STEADY_SIMPLE, true, new EmptyCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Do nothing, leave text visible

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startSteady.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(NotchError notchError) {
                super.onFailure(notchError);
                toast("Steady Configuration Failed!");
            }
        });
    }

    public void selectExercise() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.joint_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                joint = parent.getItemAtPosition(position).toString();
                if (position >= 0) {
                    rightLeft.setVisibility(View.GONE);
                }
                if (joint.equals("Ankle"))
                    rawResource = R.raw.ankle;
                else if (joint.equals("Cervical Spine"))
                    rawResource = R.raw.neck;
                else if (joint.equals("Elbow")) {
                    rawResource = R.raw.right_elbow;
                    rightLeft.setVisibility(View.VISIBLE);
                }
                else if (joint.equals("Hip"))
                    rawResource = R.raw.hip;
                else if (joint.equals("Knee"))
                    rawResource = R.raw.knee;
                else if (joint.equals("Lumbar Spine"))
                    rawResource = R.raw.lumbar_spine;
                else if (joint.equals("Neck"))
                    rawResource = R.raw.neck;
                else if (joint.equals("Shoulder"))
                    rawResource = R.raw.shoulder;
                else if (joint.equals("Wrist")){
                    rawResource = R.raw.right_wrist;
                    rightLeft.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        rightLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && joint.equals("Elbow")) {
                    side = "Right";
                    rawResource = R.raw.right_elbow;
                }
                else if (isChecked && joint.equals("Elbow")) {
                    side = "Left";
                    rawResource = R.raw.left_elbow;
                }
                else if (!isChecked && joint.equals("Wrist")) {
                    side = "Right";
                    rawResource = R.raw.right_wrist;
                }
                else if (isChecked && joint.equals("Wrist")) {
                    side = "Left";
                    rawResource = R.raw.left_wrist;
                }
                else
                    toast("Error Select Appropriate Exercise");
            }
        });

        setExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1-Notch Init
                Skeleton skeleton;
                try {
                    skeleton = Skeleton.from(new InputStreamReader(mApplicationContext.getResources().openRawResource(R.raw.skeleton_male), "UTF-8"));
                    Workout workout = Workout.from("Demo_config", skeleton, IOUtil.readAll(new InputStreamReader(mApplicationContext.getResources().openRawResource(rawResource))));// I changed this to be the R.raw.rightarm instead of the one on chest
                    if (mRealTime) {
                        workout = workout.withRealTime(true);
                        workout = workout.withMeasurementType(MeasurementType.STEADY_SKIP);
                    }
                    mWorkout = workout;
                    inProgress();
                    mNotchService.init(mSelectedChannel, workout, new EmptyCallback<NotchNetwork>() {
                        @Override
                        public void onSuccess(NotchNetwork notchNetwork) {
                            doneProgress();
                            updateNetwork();
                            super.onSuccess(notchNetwork);
                            configureSteady();
                        }
                        @Override
                        public void onFailure(NotchError notchError) {
                            toast("Connect more/less notches!");
                            super.onFailure(notchError);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error while loading skeleton file!", e);
                    toast("Error Loading Skeleton!");
                }
        //Configure Steady

                //Start Steady
        startSteady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSteady.setVisibility(View.GONE);
                mState = PairFragment.State.STEADY;
                mCountDown.start();
            }
        });
            //Get Steady Data (Finish Steady)
            finishSteady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishSteady.setVisibility(View.GONE);
                    if(customFlag){
                        SharedPreferences preferences = getContext().getSharedPreferences("checkbox",Context.MODE_PRIVATE);
                        if(preferences.getBoolean("Is Therapist",false)) {
                            goniometer.setVisibility(View.VISIBLE);
                        }
                        playButton.setVisibility(View.VISIBLE);
                        playSpinner.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                    else{
                        start.setVisibility(View.VISIBLE);
                    }
                    inProgress();
                    mNotchService.getSteadyData(new EmptyCallback<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            doneProgress();
                            toast("Steady Data Received!");
                            super.onSuccess(unused);
                        }
                        @Override
                        public void onFailure(NotchError notchError) {
                            doneProgress();
                            toast("Error Receiving Steady Data!");
                            super.onFailure(notchError);
                        }
                    });
                }
            });
        }
    });

    }


    private void changeChannel() {
        changeChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstChangedChannel = false) {
                    //Select First Channel
                    firstChangedChannel = true;
                    mChangingChannel = false;
                    channelDialog.show();
                }
                else {
                    //Change Channel
                    mChangingChannel = true;
                    mSelectedChannel = null;
                    channelDialog.show();
                }
            }
        });
    }

    void uncheckedinit() {
        inProgress();
        mNotchService.uncheckedInit(mSelectedChannel, new EmptyCallback<NotchNetwork>() {
            @Override
            public void onSuccess(NotchNetwork notchNetwork) {
                super.onSuccess(notchNetwork);
                doneProgress();
                updateNetwork();
                Toast.makeText(getActivity(),"Unchecked init successful", Toast.LENGTH_SHORT).show();
                if(isCalibrating=true)
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            configureCalibration();
                            isCalibrating=false;
                        }
                    });
            }
        });
    }

    private void configureCalibration() {
        inProgress();
        mNotchService.configureCalibration(true, new EmptyCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                super.onSuccess(unused);
                doneProgress();
                toast("Calibration Configured!");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startCalibration.setVisibility(View.VISIBLE);
                    }
                });
            }
            @Override
            public void onFailure(NotchError notchError) {
                super.onFailure(notchError);
                doneProgress();
                toast("Failed to configure calibration!");
            }
        });
    }

    private void calibrate() {
        calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCalibrating = true;
                //Unchecked init
                uncheckedinit();
                //Configure Calibration
                //Calibrate
                startCalibration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startCalibration.setVisibility(View.GONE);
                        mState = PairFragment.State.CALIBRATION;
                        mCountDown.start();
                    }
                });
                //Get Calibration Data
                //finishCalibration.setVisibility(View.VISIBLE);
                finishCalibration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishCalibration.setVisibility(View.GONE);
                        inProgress();
                        mNotchService.getCalibrationData(new EmptyCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                super.onSuccess(aBoolean);
                                doneProgress();
                                gotCalibrationData=true;
                                toast("Calibration Data Received");
                                if(gotCalibrationData) {
                                    calibrated=false;
                                    gotCalibrationData=false;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
                                    String currentTime = sdf.format(new Date());
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textLastCalibration.setText("Last Calibration: " + currentTime);
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFailure(NotchError notchError) {
                                super.onFailure(notchError);
                                doneProgress();
                                toast("Calibration Data Not Received!");
                            }
                        });
                    }
                });

            }
        });
    }


    public void pair () {
        pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress();
                mNotchService.pair(new EmptyCallback<Device>() {
                    @Override
                    public void onSuccess(Device device) {
                        doneProgress();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mDB == null)
                                    mDB = NotchDataBase.getInst();
                                StringBuilder sb = new StringBuilder();
                                sb.append("Device list:\n");
                                for (Device device : mDB.findAllDevices(mUser)) {
                                    sb.append("Notch ").append(device.getNotchDevice().getNetworkId()).append(" (");
                                    sb.append(device.getNotchDevice().getDeviceMac()).append(") ");
                                    sb.append("FW: " + device.getSwVersion() + ", ");
                                    sb.append("Ch: " + device.getChannel().toChar() + "\n");
                                }
                                textPairedNotches.setText(sb.toString());
                            }
                        });
                        updateUser(mNotchService.getLicense());
                        shutdown();
                    }
                    @Override
                    public void onFailure(NotchError notchError) {
                        super.onFailure(notchError);
                        doneProgress();
                        toast("Pairing Failed!");
                    }
                });
            }
        });
    }

    void connect() {
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress();
                mNotchService.disconnect(new EmptyCallback<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        super.onSuccess(unused);
                        mNotchService.uncheckedInit(mSelectedChannel, new EmptyCallback<NotchNetwork>() {
                            @Override
                            public void onSuccess(NotchNetwork notchNetwork) {
                                super.onSuccess(notchNetwork);
                                doneProgress();
                                toast("Connected");
                                updateNetwork();
                                updateUser(mNotchService.getLicense());
                                changeTopMenu();
                            }
                            @Override
                            public void onFailure(NotchError notchError) {
                                super.onFailure(notchError);
                                doneProgress();
                                Toast.makeText(getActivity(),"Failed to Connect. Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void inProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void doneProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    Runnable mSetDefaultUser = new Runnable() {
        @Override
        public void run() {
            if (mNotchService != null && mUser == null) {
                mUser = DEFAULT_USER_LICENSE;
                if (DEFAULT_USER_LICENSE.length() > 0) {
                    updateUser(mUser);
                    mNotchService.setLicense(mUser);
                }
            }
        }
    };

    private void updateNetwork() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("Current network: ");
                if (mNotchService.getNetwork() != null) {
                    for (ActionDevice device : mNotchService.getNetwork().getDeviceSet()) {
                        sb.append(device.getNetworkId()).append(", ");
                    }
                }
                textCurrentNetwork.setText(sb.toString());
                doneProgress();
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

    public void updateUser(final String user) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mDB==null)
                    mDB = NotchDataBase.getInst();
                StringBuilder sb = new StringBuilder();
                sb.append("Device list:\n");
                for (Device device : mDB.findAllDevices(user)) {
                    sb.append("Notch ").append(device.getNotchDevice().getNetworkId()).append(" (");
                    sb.append(device.getNotchDevice().getDeviceMac()).append(") ");
                    sb.append("FW: " + device.getSwVersion() + ", ");
                    sb.append("Ch: " + device.getChannel().toChar() + "\n");
                }
                textPairedNotches.setText(sb.toString());
                /*if(mSelectedChannel==null) {
                    mSelectedChannelTxt.setText("SELECTED CHANNEL: NULL");
                }
                else {
                    mSelectedChannelTxt.setText("SELECTED CHANNEL: " + mSelectedChannel);
                }*/
            }
        });
    }

    private CountDownTimer mCountDown = new CountDownTimer(3250, 500) {
        @Override
        public void onTick(long millisUntilFinished) {
            //Update UI with new count
            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 120);
                        text.setText(""+Math.round((float) millisUntilFinished / 1000.0f));
                    }
                });
            }
        }

        @Override
        public void onFinish() {
            switch (mState) {
                case CALIBRATION:
                mNotchService.calibration(new EmptyCallback<Measurement>() {
                    @Override
                    public void onSuccess(Measurement measurement) {
                        super.onSuccess(measurement);
                        gotCalibrationData=true;
                        toast("Calibrated!");
                    }

                    @Override
                    public void onFailure(NotchError notchError) {
                        super.onFailure(notchError);
                        toast("Failed to Calibrate!");
                    }
                });
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDockImg.setVisibility(View.VISIBLE);
                        mDockAnimation.setVisible(false, true);
                        mDockAnimation.start();
                    }
                });
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDockImg.setVisibility(View.GONE);
                        mDockAnimation.stop();
                        finishCalibration.setVisibility(View.VISIBLE);
                    }
                }, CALIBRATION_TIME);
                break;

                case STEADY:
                    inProgress();
                    mNotchService.steady(new EmptyCallback<Measurement>() {
                        @Override
                        public void onSuccess(Measurement measurement) {
                            super.onSuccess(measurement);
                            doneProgress();
                            toast("Success!");
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //finishSteady.setVisibility(View.VISIBLE);
                                    finishSteady.performClick();
                                }
                            });
                        }
                        @Override
                        public void onFailure(NotchError notchError) {
                            super.onFailure(notchError);
                            doneProgress();
                            toast("Failure");
                        }
                    });
                    break;

                case CAPTURE:
                    capture();
                    break;
            }
            text.setText("");
        }
    };

    private void buildChannelDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        builder.setTitle("Choose a channel!");
        final String[] names = new String[]{"UNSPECIFIED", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "U", "V", "W", "X", "Y", "Z",};

        int initial = 0;
        mSelectedChannel = null;

        builder.setSingleChoiceItems(names, initial, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mSelectedChannel = null;
                } else {
                    mSelectedChannel = NotchChannel.fromChar(names[which].charAt(0));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mChangingChannel) {
                    inProgress();
                    if (mSelectedChannel != null) {
                        mNotchService.changeChannel(mSelectedChannel, new EmptyCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                super.onSuccess(aVoid);
                                doneProgress();
                                mSelectedChannel = null;
                                updateUser(mNotchService.getLicense());
                            }

                            @Override
                            public void onFailure(NotchError notchError) {
                                super.onFailure(notchError);
                                doneProgress();
                                mSelectedChannel = null;
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(),"Select a channel (A-Z)!",Toast.LENGTH_SHORT).show();
                        doneProgress();
                    }

                } /*else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSelectedChannel == null) {
                                mSelectedChannelTxt.setText("SELECTED CHANNEL: UNSPECIFIED");
                            } else {
                                mSelectedChannelTxt.setText("SELECTED CHANNEL: " + mSelectedChannel.toChar());
                            }
                        }
                    });
                }*/
            }
        });
        channelDialog = builder.create();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ALL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mApplicationContext, "Permissions granted!", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissions(permissions, REQUEST_ALL_PERMISSION);
                    Toast.makeText(mApplicationContext, "Permissions granted!", Toast.LENGTH_SHORT).show();
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
    void toast(String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

    }




}


