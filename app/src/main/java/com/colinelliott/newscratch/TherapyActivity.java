package com.colinelliott.newscratch;

import static java.lang.Math.abs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wearnotch.framework.Bone;
import com.wearnotch.framework.Constraint;
import com.wearnotch.framework.Skeleton;
import com.wearnotch.framework.visualiser.VisualiserData;
import com.wearnotch.notchmaths.fvec3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TherapyActivity extends AppCompatActivity{
    private static final String TAG = TherapyActivity.class.getSimpleName();

    private static final int REQUEST_OPEN =1;
    SharedPreferences preferences;
    public static final String PARAM_INPUT_ZIP = "INPUT_ZIP";
    public static final String PARAM_INPUT_RAW = "INPUT_RAW";
    public static final String PARAM_INPUT_DATA = "INPUT_DATA";
    public static final String PARAM_REALTIME = "REALTIME";

    public static final String EXTRA_MAX_FLEXION = "com.colinelliott.newscratch.EXTRA_MAX_FLEXION";
    public static final String EXTRA_MAX_EXTENSION = "com.colinelliott.newscratch.EXTRA_MAX_EXTENSION";
    public static final String EXTRA_MAX_ABDUCTION = "com.colinelliott.newscratch.EXTRA_MAX_ABDUCTION";
    public static final String EXTRA_MAX_ADDUCTION = "com.colinelliott.newscratch.EXTRA_MAX_ADDUCTION";
    public static final String EXTRA_MAX_INTERNAL_ROTATION = "com.colinelliott.newscratch.EXTRA_MAX_INTERNAL_ROTATION";
    public static final String EXTRA_MAX_EXTERNAL_ROTATION = "com.colinelliott.newscratch.EXTRA_MAX_EXTERNAL_ROTATION";

    private static final int MAX_RT_VISUALIZATION_LENGTH_SEC = 5;

    private Context mApplicationContext;
    private volatile List<Bone> mBonesToShow = new ArrayList<Bone>();
    private VisualiserData mData;
    private Skeleton mSkeleton;
    private boolean mRealTime = true;
    private boolean mShowAngles = true;
    private boolean dontTargetCounter;
    private int mMaxFrameCount, mFrameCount, initX, initY, finalX, finalY, nowX, nowY;
    private int minute = 0;
    private float height,width;
    private float mFrequency;
    private Parcelable[] mZipUri;
    DecimalFormat decimalFormat;
    private int flexAngle, abdAngle, rotAngle;
    private int tol = 2;   //Tolerance in degrees for each target to hit
    private int goalInitX, goalInitY;
    private int skippedTargets;
    long responseTestTime;
    private boolean countDownStarted;
    String textTargetsSkipped;
    private final int holdTolerance = 4;

    private Map<Bone, List<fvec3>> realTimePlotData = new HashMap<>();
    int [] currentLocations = new int[2];
    int [] initUserLocations = new int[2];
    int [] initGoalLocations = new int[2];
    int currentX, currentY;
    String xyzString;
    private boolean rotation = true;
    private boolean flexion = true;
    private boolean abduction = true;
    private boolean target1InProgress = true;
    private boolean target1Complete, target2Complete, target3Complete;
    private boolean goniometerFlag = false;
    private boolean playFlag = false;
    private boolean therapistFlag = false;
    private boolean responseTimeTargetPlacedFlag = false;
    fvec3 chestAngles, elbowAngles, exerciseAngles;
    int[] arrayROM = new int[20];
    int maxFlexion=0;int maxAbduction = 0; int maxInternalRotation = 0;int maxExtension=0;int maxAdduction = 0; int maxExternalRotation = 0;
    int target1, target2, target3;
    int totalReps = 2;
    int currentRep = 0;
    int targetCounter = 0;
    int numTargets = 20;   //Changed from 3
    private boolean [] targetComplete = new boolean[numTargets];
    private int [][] target;
    private int [][] targetCopy;
    private int [] holdTime = new int[numTargets];  //measured in seconds... holdTime *mFrequency = counterTotal
    private long countdownLength;
    private int holdCounter = 0;
    private int addedHeight;
    RelativeLayout logView;
    String joint;
    int playId;
    Boolean isResponseTime, checkingTime;
    long startTime, endTime, avgTime;

    String currentPos = "";
    int selectedExerciseIdPosition;
    int selectedExerciseId;
    List<CreateExerciseNote> exercises;
    String[] exerciseNames;
    TherapistDatabase theraDb;
    CreateExerciseNoteDatabase exerciseDb;
    PatientDatabase patientDb;
    String[] sets;
    CountDownTimer timer;
    boolean timerStarted = true;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable action;
    private volatile int mFrameIndex;
    private long currentTime, displayTime;
    private int exerciseTime;
    ViewTreeObserver viewTreeObserver;
    Dialog completionDialog;
    TextView dialogTitle, dialogDesc, dialogMsg;
    Button nextExerciseDialog, showLog;
    ImageView closeDialog;
    AnimationDrawable holdAnimation;
    private boolean movingBackground;

    protected TextView mAnglesText, mElapsedTimeText, testTextUser, textROM, textReps, whatToDo, testTextGoal, textCountDown;
    private ImageView userCircle,goalCircle, userCircleNotch, goalCircleNotch;
    Button grabROM, stop, showAngles;
    Button lockFlex, lockAbd, lockRot, restartActivity;
    ImageButton skipTarget;
    Spinner spinnerXYZ;
    private ConstraintLayout constraintLayout;
    MediaPlayer hitTarget, victory;
    private CountDownTimer mCountDown;
    String[] dialogMsgInfo;

    public static Intent createIntent(Context context, VisualiserData data, boolean realtime) {
        Intent i = new Intent(context, TherapyActivity.class);
        i.putExtra(PARAM_INPUT_DATA, data);
        i.putExtra(PARAM_REALTIME, realtime);
        return i;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void loadNextExerciseSpinner(){
        //ExerciseDB will be initialized when this is ran (only on finish and after checking for playFlag true)
        //Check if this is a therapist or patient
        if(therapistFlag) {
            //Fill the spinner with exercise names
            //exercises = exerciseDb.createExerciseNoteDao().getAllCreateExerciseNotesList();
            int theraID = preferences.getInt("User ID", -1);
            exercises = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(theraID);
            exerciseNames = new String[exercises.size()];
            for(int i = 0; i < exercises.size(); i++){
                exerciseNames[i] = exercises.get(i).getExerciseTitle();
            }
        }
        else {
            //Process: Get exercises string, split it, loop all exercise id to retrieve exercise objects, create list of names to display
            //Exercises go into exercises and the names into exerciseNames both of which are the global lists used by a therapist as well
            int patientID = preferences.getInt("User ID", -1);
            String patientAssignedExercises = patientDb.patientDao().findUserWithID(patientID).get(0).getPatientData();
            String exerciseIdList[] = patientAssignedExercises.split(",");
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
            sets = patientDb.patientDao().findUserWithID(patientID).get(0).getSetsProgress().split(",");
        }

        //Populate and show spinner & Button
        spinnerXYZ.setVisibility(View.VISIBLE);
        restartActivity.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, exerciseNames);
        adapter.setDropDownViewResource(R.layout.spinner_goniometer_dropdown);
        spinnerXYZ.setAdapter(adapter);
        spinnerXYZ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExerciseIdPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExerciseIdPosition = -1;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = getApplicationContext();
        setContentView(R.layout.activity_therapy);
        patientDb = PatientDatabase.getInstance(this);
        preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels-285;
        width = displayMetrics.widthPixels-215;

        victory = MediaPlayer.create(this,R.raw.sound_victory);
        hitTarget = MediaPlayer.create(this,R.raw.success_trimmed);
        targetComplete[0] = true;
        target  = new int[numTargets][3];
        targetCopy = new int[numTargets][3];
        holdTime[0] = 2; // was 3
        holdTime[1] = 1;
        holdTime[2] = 1;
        holdTime[3] = 1;
        countdownLength = 1000*holdTime[targetCounter];

        completionDialog = new Dialog(this);
        avgTime = 0;
        checkingTime = false;

        mAnglesText = findViewById(R.id.text_angles);
        mElapsedTimeText = findViewById(R.id.text_elapsed_time);
        testTextGoal = findViewById(R.id.test_text_goal); //Current Time Millis
        userCircle = (ImageView)findViewById(R.id.circle_user);
        goalCircle = (ImageView)findViewById(R.id.circle_goal);
        userCircleNotch = (ImageView)findViewById(R.id.circle_user_with_notch);
        goalCircleNotch = (ImageView)findViewById(R.id.circle_goal_with_notch);
        constraintLayout = findViewById(R.id.constraint_layout_therapy);
        grabROM = findViewById(R.id.btn_grab_rom);
        lockAbd = findViewById(R.id.lockAbduction);
        lockFlex = findViewById(R.id.lockFlexion);
        lockRot = findViewById(R.id.lockRotation);
        spinnerXYZ = findViewById(R.id.spinner_xyz);
        textROM = findViewById(R.id.text_ROM);
        stop = findViewById(R.id.btn_stop);
        textReps = findViewById(R.id.text_reps);
        testTextUser = findViewById(R.id.test_text_user);
        whatToDo = findViewById(R.id.text_what_to_do);
        skipTarget = findViewById(R.id.btn_skip_target);
        textCountDown = findViewById(R.id.text_hold_countdown);
        showAngles = findViewById(R.id.btn_show_angles);
        restartActivity = findViewById(R.id.restartActivity);

        getInitialLocation();
        setButtons();
        //lockFlex.setVisibility(View.GONE);
        //lockAbd.setVisibility(View.GONE);
        //lockRot.setVisibility(View.GONE);

        restartActivity.setVisibility(View.GONE);

        getExerciseInfo();
        if(playFlag) {
            textReps.setText(currentRep + "/" + totalReps);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTarget(target[0][0], target[0][1], target[0][2]);
                }
            },500);
        }
        if(playFlag) {
            proximityAudio();
        }

        currentTime = (long)System.currentTimeMillis();
        elbowAngles = new fvec3();
        chestAngles = new fvec3();
        exerciseAngles = new fvec3();

        mZipUri = getIntent().getParcelableArrayExtra(PARAM_INPUT_ZIP);
        mData = (VisualiserData) getIntent().getSerializableExtra(PARAM_INPUT_DATA);
        mRealTime = getIntent().getBooleanExtra(PARAM_REALTIME, true);
        mFrequency = mData.getFrequency();
        mSkeleton = mData.getSkeleton();
        initRenderer();

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.000", otherSymbols);

        mMaxFrameCount = MAX_RT_VISUALIZATION_LENGTH_SEC * (int) mData.getFrequency();

        //Timer for Hold Segments
        mCountDown = new CountDownTimer(countdownLength, 500) { //(long) ((holdTime[targetCounter]*1000))
            @Override
            public void onTick(long millisUntilFinished) {
                //Update UI with new count
                textCountDown.setText("" + Math.round((float) millisUntilFinished / 1000.0f));
            }
            @Override
            public void onFinish() {
                proximityAudioOnCountdown();
                textCountDown.setVisibility(View.GONE);
                holdAnimation.stop();
                constraintLayout.setBackgroundResource(0);
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(target[targetCounter][0] == 0 && target[targetCounter][1] == 0 && target[targetCounter][2] == 0) {
                    skipTarget.performClick(); //skips empty 0,0,0 targets
                }
                if(go) {
                    if (rotation || playFlag) {
                        userCircleNotch.setRotation(0);
                        goalCircleNotch.setX(0);
                        goalCircleNotch.setY(0);
                        goalCircleNotch.setRotation(0);
                    }
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
        };
    }

    private void setButtons() {
        //These lock button enables should all be true by default, this is just to make sure
        lockFlex.setEnabled(true);
        lockAbd.setEnabled(true);
        lockRot.setEnabled(true);

        showAngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnglesText.getVisibility()==View.VISIBLE)
                    mAnglesText.setVisibility(View.INVISIBLE);
                else
                    mAnglesText.setVisibility(View.VISIBLE);
            }
        });

        restartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                selectedExerciseId = exercises.get(selectedExerciseIdPosition).getId();
                intent.putExtra("Exercise ID", selectedExerciseId);
                //Restart Activity
                finish();
                startActivity(intent);

            }
        });

        grabROM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flexion){
                    exerciseAngles.set(0,0);
                }
                if(!abduction){
                    exerciseAngles.set(2,0);
                }
                if(!rotation){
                    exerciseAngles.set(1,0);
                }
                currentPos = currentPos+","+exerciseAngles.get(0)+","+exerciseAngles.get(1)+","+exerciseAngles.get(2);
                //Toast.makeText(mApplicationContext, currentPos, Toast.LENGTH_SHORT).show();
            }
        });

        lockFlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flexion){
                    flexion = false;
                    Toast.makeText(mApplicationContext, "Flexion Locked", Toast.LENGTH_SHORT).show();
                }
                else{
                    flexion = true;
                    Toast.makeText(mApplicationContext, "Flexion Unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lockAbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(abduction){
                    abduction = false;
                    Toast.makeText(mApplicationContext, "Abduction Locked", Toast.LENGTH_SHORT).show();
                }
                else{
                    abduction = true;
                    Toast.makeText(mApplicationContext, "Abduction Unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lockRot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rotation){
                    rotation = false;
                    Toast.makeText(mApplicationContext, "Rotation Locked", Toast.LENGTH_SHORT).show();
                }
                else{
                    rotation = true;
                    Toast.makeText(mApplicationContext, "Rotation Unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goniometerFlag) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_MAX_FLEXION, maxFlexion);
                    intent.putExtra(EXTRA_MAX_EXTENSION, maxExtension);
                    intent.putExtra(EXTRA_MAX_ABDUCTION, maxAbduction);
                    intent.putExtra(EXTRA_MAX_ADDUCTION, maxAdduction);
                    intent.putExtra(EXTRA_MAX_INTERNAL_ROTATION, maxInternalRotation);
                    intent.putExtra(EXTRA_MAX_EXTERNAL_ROTATION, maxExternalRotation);

                    //Store locational data in the selected exercise
                    if(selectedExerciseIdPosition != -1){
                        String exerciseName = exerciseNames[selectedExerciseIdPosition];
                        selectedExerciseId = exercises.get(selectedExerciseIdPosition).getId();
                        exerciseDb.createExerciseNoteDao().update(currentPos,selectedExerciseId);
                        Toast.makeText(mApplicationContext, "Updated " +exerciseName+" location data to "+currentPos, Toast.LENGTH_SHORT).show();
                    }

                }
                    goniometerFlag = false;
                    playFlag = false;
                    hitTarget.release();
                    victory.release();
                    finish();
            }
        });
        skipTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skippedTargets++;
                if(target[targetCounter][0] == 0 && target[targetCounter][1] == 0 && target[targetCounter][2] == 0)
                    skippedTargets--;
                StringBuilder sb = new StringBuilder();
                sb.append(targetCounter+1 + ", ");
                textTargetsSkipped = sb.toString();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(target[targetCounter][0] == 0 && target[targetCounter][1] == 0 && target[targetCounter][2] == 0) {
                    skipTarget.performClick(); //skips empty 0,0,0 targets
                }
                if (go)
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
            }
        });
    }


    @Subscribe
    public void onDataUpdate (VisualiserData data) {
        if (data !=null) {
            mData = data;
            mFrameIndex = mData.getFrameCount()-1;
            refreshUI();

        }

    }

    public void refreshUI() {
        refreshAngles();
    }

    // Show angles
    public void refreshAngles() {
        if (mData == null) {
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                displayTime = ((int)(System.currentTimeMillis()-currentTime)/1000);
                if(displayTime%60<10)
                    mElapsedTimeText.setText(displayTime/60+":0"+displayTime%60);
                else
                    mElapsedTimeText.setText(displayTime/60+":"+displayTime%60);
                //mElapsedTimeText.setText(String.valueOf((int)displayTime));
                calculateAngles(mFrameIndex); //causes the user's circle to move in real time
                if(playFlag)
                    checkTarget(flexion,abduction,rotation,exerciseAngles);
            }
        });
    }


    // Calculate angles
    private void calculateAngles(int frameIndex) {   //int frameIndex
        Bone root = mSkeleton.getRoot();
        Bone chestTop = mSkeleton.getBone("ChestTop");
        Bone foreArm = mSkeleton.getBone("RightForeArm");
        Bone upperArm = mSkeleton.getBone("RightUpperArm");
        Bone hand = mSkeleton.getBone("RightHand");
        Bone chestBottom = mSkeleton.getBone("ChestBottom");
        Bone neck = mSkeleton.getBone("Neck");
        Bone head = mSkeleton.getBone("Head");
        Bone hip = mSkeleton.getBone("Hip");
        Bone thigh = mSkeleton.getBone("RightThigh");
        Bone lowerLeg = mSkeleton.getBone("RightLowerLeg");
        Bone footTop = mSkeleton.getBone("RightFootTop");
        Bone child, parent;

        if (joint.equals("Ankle")) {
            child = footTop;
            parent = lowerLeg;
        } else if (joint.equals("Cervical Spine")) {
            child = chestTop;   // Switched child and parent
            parent = head;
        } else if (joint.equals("Elbow")) {
            child = foreArm;
            parent = upperArm;
        } else if (joint.equals("Hip")) {
            child = thigh;
            parent = hip;
        } else if (joint.equals("Knee")) {
            child = thigh;
            parent = lowerLeg;
        } else if (joint.equals("Lumbar Spine")) {
            child = hip;
            parent = chestBottom;
        } else if (joint.equals("Neck")) {
            child = head;
            parent = chestTop;
        } else if (joint.equals("Shoulder")) {
            child = upperArm;
            parent = chestTop;
        } else if (joint.equals("Wrist")) {
            child = hand;
            parent = foreArm;
        } else {
            child = foreArm;
            parent = upperArm;
        }


        /*chestAngles = new fvec3();
        elbowAngles = new fvec3();*/

        // Calculate forearm angles with respect to upper arm (determine elbow joint angles).
        // Angles correspond to rotations around X,Y and Z axis of the paren bone's coordinate system, respectively.
        // The coordinate system is X-left, Y-up, Z-front aligned.
        // Default orientations are defined in the steady pose (in the skeleton file)
        // Usage: calculateRelativeAngle(Bone child, Bone parent, int frameIndex, fvec3 output)
        // mData.calculateRelativeAngle(foreArm, upperArm, frameIndex, exerciseAngles);
        mData.calculateRelativeAngle(child, parent, frameIndex, exerciseAngles);
        if (!joint.equals("Wrist")) {
            flexAngle = (int) exerciseAngles.get(0);
            abdAngle = (int) -exerciseAngles.get(2);
            rotAngle = (int) -exerciseAngles.get(1);
        }
        else {
            abdAngle = (int) -exerciseAngles.get(0); //was flex+ now is rot-
            flexAngle = (int) -exerciseAngles.get(2);
            rotAngle = (int) -exerciseAngles.get(1); //was rot- now flex+
        }

        // Calculate chest angles with respect root, i.e. absolute angles
        // The root orientation is the always the same as in the steady pose.
        // mData.calculateRelativeAngle(chestTop, root, frameIndex, chestAngles);
        // Show angles
        StringBuilder sb = new StringBuilder();
        sb.append(joint + " angles:\n")
                // Extension/flexion is rotation around the upperarm's X-axis
                .append("Extension(+)/Flexion(-): ").append(flexAngle).append("°\n")
                // Supination/pronation is rotation around the upperarm's Y-axis
                .append("Left Rotation(+)/Right Rotation(-): ").append(rotAngle).append("°\n")
                .append("Lateral tilt left(-)/right(+): ").append(abdAngle).append("°\n");
                /*.append("\nChest angles:\n")
                // Anterior/posterior tilt (forward/backward bend) is rotation around global X axis
                .append("Anterior(+)/posterior(-) tilt: ").append((int) chestAngles.get(0)).append("°\n")
                // Rotation to left/right is rotation around the global Y axis
                .append("Rotation left(+)/right(-): ").append((int) chestAngles.get(1)).append("°\n")
                // Lateral tilt (side bend) is rotation around global Z axis
                .append("Lateral tilt left(-)/right(+): ").append((int) chestAngles.get(2)).append("°\n");*/
        int flexDisplay, abdDisplay, rotDisplay;
        flexDisplay = (int) ((flexAngle * (height / 180)) / 2);
        abdDisplay = (int) ((abdAngle * (width / 180)) / 2);
        rotDisplay = (int) rotAngle;

        mAnglesText.setText(sb.toString());
        if(rotation)
            userCircleNotch.setRotation(rotDisplay);
        if(abduction)
            userCircleNotch.setX(abdDisplay+initX);
        if(flexion)
            userCircleNotch.setY(flexDisplay+initY);
        userCircleNotch.invalidate();
    }

    private int getPixels (int dp) {
        int dpi = (int) getResources().getDisplayMetrics().densityDpi;
        int pixels = dp * (dpi/160);
        return pixels;
    }

    private void setTarget (int flexExtend, int intExtRot, int abdAdd) {
        int id = getIntent().getIntExtra("Exercise ID", -1);
        exerciseDb = CreateExerciseNoteDatabase.getInstance(this);
        isResponseTime = exerciseDb.createExerciseNoteDao().getExerciseById(id).get(0).getIsResponseTime();
        StringBuilder sb = new StringBuilder();
        //rotation = false; abduction = false; flexion = false;

        if(playFlag){
            if(isResponseTime && flexExtend != 1 && intExtRot != 1 && abdAdd != 1){
                goalCircleNotch.setVisibility(View.GONE);
                //if(isResponseTime && !responseTimeTargetPlacedFlag) {
                int i;
                for(i = 19; i > 0; i--) {
                    if (target[i][0] != 0 || target[i][1] != 0 || target[i][2] != 0) {
                        break;
                    }
                }
                //toast(targetCopy[0][0] +" "+targetCopy[0][1]+" "+targetCopy[0][2]);
                //responseTimeTargetPlacedFlag = true;
                //The length of target will tell me how many different coordinates there are to choose from
                if(i>0) { //If not true (1 target) no need to do anything special here
                    int randomIndex = (int)(Math.random() * (i+1));
                    //Toast.makeText(mApplicationContext, Integer.toString(randomIndex), Toast.LENGTH_SHORT).show();
                    flexExtend = targetCopy[randomIndex][0]; //setting the local variables for display purposes
                    intExtRot = targetCopy[randomIndex][1];
                    abdAdd = targetCopy[randomIndex][2];
                    //target[targetCounter][0] = flexExtend; //setting target for checking purposes
                    //target[targetCounter][1] = intExtRot;
                    //target[targetCounter][2] = abdAdd;
                    target[0][0] = targetCopy[randomIndex][0]; //setting target for checking purposes
                    target[0][1] = targetCopy[randomIndex][1];
                    target[0][2] = targetCopy[randomIndex][2];
                    target[1][0] = 1;
                    target[1][1] = 1;
                    target[1][2] = 1;

                    if(i>1){
                        for(int j = 2; j <= i; j++){
                            target[j][0] = 0; //Setting to a value close to the origin for tolerance to automate but not 0 to avoid auto skip
                            target[j][1] = 0; //If statement at the start of function ensures this is not done repeatedly
                            target[j][2] = 0;
                        }
                    }
                }
            }
            //rotation = true; abduction = true; flexion = true;
            //goalCircleNotch.setX(abdAdd*(width/180)/2); Runs off the screen
            //goalCircleNotch.setY(flexExtend*(height/180)/2);
            goalCircleNotch.setVisibility(View.VISIBLE);
            goalCircleNotch.setX(-abdAdd * (width / 180) / 2 + goalInitX);
            goalCircleNotch.setY(flexExtend * (height / 180) / 2 + goalInitY);
            goalCircleNotch.setRotation(intExtRot);
            sb.append("Custom Exercise");
            if(isResponseTime && (abdAdd != 1 || flexExtend != 1 || intExtRot != 1)) {
                checkingTime = true;
                //toast(String.valueOf(System.currentTimeMillis()));
                startTime = System.currentTimeMillis();
            }
        }
        else {
            if (targetComplete[targetCounter]) {
                if (flexExtend != 0) {
                    userCircleNotch.setX(initX);
                    goalCircleNotch.setX(goalInitX);
                    goalCircleNotch.setY(((-1 * target[targetCounter][0] * (height / 180) / 2) + goalInitY)); // changed from +initY -(R.integer.goalRadius-R.integer.userRadius)
                    //flexion = true;
                    sb.append("Flex/Extend");
                }
                if (abdAdd != 0) {
                    userCircleNotch.setY(initY);
                    goalCircleNotch.setY(goalInitY);
                    goalCircleNotch.setX((target[targetCounter][1] * (width / 180) / 2) + goalInitX);
                    //abduction = true;
                    sb.append("Abduct/Adduct");
                }
                if (intExtRot != 0) {
                    userCircleNotch.setX(initX);
                    goalCircleNotch.setX(goalInitX);
                    userCircleNotch.setY(initY);
                    goalCircleNotch.setY(goalInitY);
                    goalCircleNotch.setRotation(target[targetCounter][2]);
                    //rotation = true;
                    sb.append("Rotate");
                }
            }
        }
        whatToDo.setText(sb.toString());
    }

    private void checkTarget(boolean flex, boolean abd, boolean rot, fvec3 vector) {
        tol = 15;
        boolean flexCondition, abdCondition, rotCondition;

        //flexCondition = (Math.abs(flexAngle) <= (Math.abs(target[targetCounter][0]) + tol) && Math.abs(flexAngle) >= (Math.abs(target[targetCounter][0]) - tol));
        //abdCondition = (Math.abs(abdAngle) <= (Math.abs(target[targetCounter][2]) + tol) && Math.abs(abdAngle) >= (Math.abs(target[targetCounter][2]) - tol));
        //rotCondition = (Math.abs(rotAngle) <= (Math.abs(target[targetCounter][1]) + tol) && Math.abs(rotAngle) >= (Math.abs(target[targetCounter][1]) - tol));

        flexCondition = flexAngle <= (target[targetCounter][0] + tol) && flexAngle >= (target[targetCounter][0] - tol);
        abdCondition = abdAngle <= (target[targetCounter][2] + tol) && abdAngle >= (target[targetCounter][2] - tol);
        rotCondition = rotAngle <= (target[targetCounter][1] + tol) && rotAngle >= (target[targetCounter][1] - tol);

        //Purpose of this boolean logic is to check for a matching target if the player and target align on that axis or if that axis's tracking is locked
        flexCondition = flexCondition || !flex;
        abdCondition = abdCondition || !abd;
        rotCondition = rotCondition || !rot;

        if(flexCondition && abdCondition && rotCondition && holdTime[targetCounter]==0){
            /*
            toast(isResponseTime +" "+checkingTime);
            if(isResponseTime && checkingTime) {
                endTime = System.currentTimeMillis();
                avgTime = avgTime + (endTime-startTime);
                //toast(String.valueOf(avgTime));
                checkingTime = false; //needed to ensure repeated checking does not occur if user falls off of target and re-enters
            }
            */
            hitTarget.start();
            boolean go = checkRep();
            targetCounter++;
            targetComplete[targetCounter] = true;
            checkExerciseComplete();
            if(go) {
                userCircleNotch.setRotation(0);
                goalCircleNotch.setRotation(0);
                setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
            }
        }
        else if(flexCondition && abdCondition && rotCondition && holdTime[targetCounter]!=0) {
            checkHold();
        }
        else if((!flexCondition || !abdCondition || !rotCondition) && holdTime[targetCounter]!=0) {
            if (countDownStarted) {
                proximityAudioOnCountdown();
                mCountDown.cancel();
                countDownStarted = false;
                textCountDown.setVisibility(View.GONE);
                movingBackground = false;
                holdAnimation.stop();
                constraintLayout.setBackgroundResource(0);
            }
        }

        /*
        else {
            if (!joint.equals("Wrist")) {
            flexCondition = (-flexAngle <= target[targetCounter][0] + tol && -flexAngle >= target[targetCounter][0] - tol && target[targetCounter][0] > 0) || (-flexAngle >= target[targetCounter][0] - tol && -flexAngle <= target[targetCounter][0] + tol && target[targetCounter][0] < 0);
            abdCondition = (abdAngle <= target[targetCounter][1] + tol && abdAngle >= target[targetCounter][1] - tol && target[targetCounter][1] > 0) || (abdAngle >= target[targetCounter][1] - tol && abdAngle <= target[targetCounter][1] + tol && target[targetCounter][1] < 0);
            rotCondition = (rotAngle <= target[targetCounter][2] + tol && rotAngle >= target[targetCounter][2] - tol && target[targetCounter][2] > 0) || (rotAngle >= target[targetCounter][2] - tol && rotAngle <= target[targetCounter][2] + tol && target[targetCounter][2] < 0);
            }
            else {
            flexCondition = (-flexAngle <= target[targetCounter][0] + tol && -flexAngle >= target[targetCounter][0] - tol && target[targetCounter][0] > 0) || (-flexAngle >= target[targetCounter][0] - tol && -flexAngle <= target[targetCounter][0] + tol && target[targetCounter][0] < 0);
            abdCondition = (abdAngle <= target[targetCounter][1] + tol && abdAngle >= target[targetCounter][1] - tol && target[targetCounter][1] > 0) || (abdAngle >= target[targetCounter][1] - tol && abdAngle <= target[targetCounter][1] + tol && target[targetCounter][1] < 0);
            rotCondition = (rotAngle <= target[targetCounter][2] + tol && rotAngle >= target[targetCounter][2] - tol && target[targetCounter][2] > 0) || (rotAngle >= target[targetCounter][2] - tol && rotAngle <= target[targetCounter][2] + tol && target[targetCounter][2] < 0);
            }
        }
        //toast(flexCondition+ " "+abdCondition + " " + rotCondition);

        if(flex && !abd && !rot) {
            if(flexCondition && holdTime[targetCounter]==0) {
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if (go)
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
            }
            else if(flexCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if(!flexCondition && holdTime[targetCounter]!=0) {
                if(countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                 }
            }
        }

        else if(abd && !flex && !rot) {
            if(abdCondition && holdTime[targetCounter]==0) {
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if (go) {
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
            else if(abdCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if(!abdCondition && holdTime[targetCounter]!=0) {
                if(countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                }
            }
        }

        else if(rot && !flex && !abd) {
            if(rotCondition && holdTime[targetCounter]==0){
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(go) {
                    userCircleNotch.setRotation(0);
                    goalCircleNotch.setRotation(0);
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
            else if(rotCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if(!rotCondition && holdTime[targetCounter]!=0) {
                if (countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                }
            }
        }
        else if(flex && abd && !rot) {
            if(flexCondition && abdCondition && holdTime[targetCounter]==0){
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(go)
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
            }
            else if(flexCondition && abdCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if((!flexCondition || !abdCondition) && holdTime[targetCounter]!=0) {
                if (countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                }
            }
        }
        else if(flex && !abd && rot) {
            if(flexCondition && rotCondition && holdTime[targetCounter]==0){
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(go) {
                    userCircleNotch.setRotation(0);
                    goalCircleNotch.setRotation(0);
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
            else if(flexCondition && rotCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if((!flexCondition || !rotCondition) && holdTime[targetCounter]!=0) {
                if (countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                }
            }
        }
        else if(!flex && abd && rot) {
            if(abdCondition && rotCondition && holdTime[targetCounter]==0) {
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if (go) {
                    userCircleNotch.setRotation(0);
                    goalCircleNotch.setRotation(0);
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
            else if(abdCondition && rotCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if((!abdCondition || !rotCondition) && holdTime[targetCounter]!=0) {
                if (countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);

                }
            }
        }
        else if(flex && abd && rot) {
            if(flexCondition && abdCondition && rotCondition && holdTime[targetCounter]==0){
                hitTarget.start();
                boolean go = checkRep();
                targetCounter++;
                targetComplete[targetCounter] = true;
                checkExerciseComplete();
                if(go) {
                    userCircleNotch.setRotation(0);
                    goalCircleNotch.setRotation(0);
                    setTarget(target[targetCounter][0], target[targetCounter][1], target[targetCounter][2]);
                }
            }
            else if(flexCondition && abdCondition && rotCondition && holdTime[targetCounter]!=0) {
                checkHold();
            }
            else if((!flexCondition || !abdCondition || !rotCondition) && holdTime[targetCounter]!=0) {
                if (countDownStarted) {
                    mCountDown.cancel();
                    countDownStarted = false;
                    textCountDown.setVisibility(View.GONE);
                    movingBackground = false;
                    holdAnimation.stop();
                    constraintLayout.setBackgroundResource(0);
                }
            }
        }
        */
    }

    private void checkHold() {
        getLocation(userCircleNotch);
        textCountDown.setVisibility(View.VISIBLE);

        if(!countDownStarted) {
            countDownStarted = true;
            //toast(isResponseTime +" "+checkingTime);
            if(isResponseTime && checkingTime) {
                endTime = System.currentTimeMillis();
                avgTime = avgTime + (endTime-startTime);
                //toast(String.valueOf(avgTime));
                checkingTime = false; //needed to ensure repeated checking does not occur if user falls off of target and re-enters
            }
            if (!movingBackground) {
                movingBackground = true;
                constraintLayout.setBackgroundResource(R.drawable.background_hold_list);
                holdAnimation = (AnimationDrawable) constraintLayout.getBackground();
                holdAnimation.start();
            }
            proximityAudioOnCountdown();
            mCountDown.start();
        }
    }

    private boolean checkRep() {
        boolean continueExercise = true;
        if(targetCounter == numTargets - 1) {
            targetCounter = -1;
            if(currentRep < totalReps) {
                currentRep++;
                textReps.setText(currentRep + " / " + totalReps);
            }
            if(currentRep==totalReps) {
                //rotation = false; abduction = false; flexion = false;
                continueExercise = false;
                dialogMsgInfo = new String[3];
                //Modify sets progress if patient
                if(!therapistFlag){
                    int patientID = preferences.getInt("User ID", -1);
                    sets = patientDb.patientDao().findUserWithID(patientID).get(0).getSetsProgress().split(",");
                    int ex_id = getIntent().getIntExtra("Exercise ID", -1);
                    String[] pData = patientDb.patientDao().findUserWithID(preferences.getInt("User ID", -1)).get(0).getPatientData().split(",");
                    int id;
                    for(id = 3; id < pData.length; id++){
                        if(ex_id == Integer.parseInt(pData[id])){
                            break; //will set id (index where the exercise is in the sets and pData lists)
                        }
                    }
                    int val = Integer.parseInt(sets[id]); //Amount of sets complete for the given assigned exercise
                    dialogMsgInfo[0] = Integer.toString(val+1);
                    dialogMsgInfo[1] = "/"+pData[1];
                    dialogMsgInfo[2] = Integer.toString(skippedTargets);

                    if(val < Integer.parseInt(pData[1])){ //Index 1 of patient data is amt of sets to complete
                        val++;
                        Toast.makeText(getApplicationContext(),dialogMsgInfo[1],Toast.LENGTH_SHORT).show();

                        sets[id] = Integer.toString(val);
                        StringBuilder setsSb = new StringBuilder("");
                        for(int i = 0; i < sets.length; i++){
                            setsSb.append(sets[i]+",");
                        }
                        patientDb.patientDao().updateSets(setsSb.toString(), preferences.getInt("User ID", -1));
                    }
                    if(val >= Integer.parseInt(pData[1])){ //check if after increasing, the exe is complete
                        dialogMsgInfo[0] = "Complete";
                        dialogMsgInfo[1] = "";
                    }
                }
                if(isResponseTime){
                    avgTime = avgTime/(totalReps-skippedTargets);
                }

                showExerciseCompleteDialog();
            }
        }
        return continueExercise;
    }

    private void checkExerciseComplete() {
        if(currentRep == totalReps) {
            exerciseTime = (int) displayTime;
            victory.start();
            //rotation =false;
            //abduction = false;
            //flexion = false;

            targetCounter = 0;
            toast("Exercise Complete!");
            loadNextExerciseSpinner();
        }
    }

    public void getExerciseInfo() {
        totalReps = preferences.getInt("Reps", 2);
        getIntent().getStringExtra(CreateExerciseActivity.EXTRA_TITLE);
        xyzString = getIntent().getStringExtra(PairFragment.EXTRA_STRING_XYZ);
        joint = getIntent().getStringExtra(PairFragment.EXTRA_JOINT_NAME);
        goniometerFlag = getIntent().getBooleanExtra(PairFragment.EXTRA_GONIOMETER_FLAG_PAIR,false);
        playFlag = getIntent().getBooleanExtra("Play Flag", false);
        therapistFlag = preferences.getBoolean("Is Therapist",false);
        //therapistFlag = getIntent().getBooleanExtra("Is Therapist", false);

        if(goniometerFlag) {
            playFlag = false;
            setGoniometerView();
        }
        if(playFlag){
            goniometerFlag = false;
            setPlayView();
        }

        if(joint.equals("Ankle")){
            abduction = false;
        }
        else if(joint.equals("Knee")){
            abduction = false;
            rotation = false;
        }
        else if(joint.equals("Elbow")){
            abduction = false;
        }
        else if(joint.equals("Wrist")){
            rotation = false;
        }

    }

    private void setGoniometerView() {
        goalCircleNotch.setVisibility(View.GONE);
        whatToDo.setVisibility(View.GONE);
        textReps.setVisibility(View.GONE);
        mElapsedTimeText.setVisibility(View.GONE);
        skipTarget.setVisibility(View.GONE);
        textROM.setVisibility(View.VISIBLE);
        grabROM.setVisibility(View.VISIBLE);
        grabROM.setEnabled(true);
        //lockRot.setVisibility(View.VISIBLE);
        //lockRot.setEnabled(true);
        //lockFlex.setVisibility(View.VISIBLE);
        //lockFlex.setEnabled(true);
        //lockAbd.setVisibility(View.VISIBLE);
        //lockAbd.setEnabled(true);
        spinnerXYZ.setVisibility(View.VISIBLE);
        restartActivity.setVisibility(View.GONE);

        // Importing the list of exercises
        theraDb = TherapistDatabase.getInstance(this);
        exerciseDb = CreateExerciseNoteDatabase.getInstance(this);
        //exercises = exerciseDb.createExerciseNoteDao().getAllCreateExerciseNotesList();
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        int theraID = sharedPreferences.getInt("User ID", -1);
        exercises = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(theraID);

        exerciseNames = new String[exercises.size()];
        for(int i = 0; i < exercises.size(); i++){
            exerciseNames[i] = exercises.get(i).getExerciseTitle();
        }

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, exerciseNames);
        adapter.setDropDownViewResource(R.layout.spinner_goniometer_dropdown);
        spinnerXYZ.setAdapter(adapter);
        spinnerXYZ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExerciseIdPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExerciseIdPosition = -1;
            }
        });

    }

    private void setPlayView(){
        int id = getIntent().getIntExtra("Exercise ID", -1);
        String locData, isResponseTest;
        String [] locDataArray;
        if(id != -1){
            exerciseDb = CreateExerciseNoteDatabase.getInstance(this);
            locData = exerciseDb.createExerciseNoteDao().getExerciseById(id).get(0).getLocData();

            //toast(locData);
            //locDataArray = new String[locData.split(",").length];
            locDataArray = locData.split(",");
            int counter = 0;
            //toast(locDataArray[0] + locDataArray[1] + locDataArray[2]);
            for(int i = 1; i <= (locDataArray.length)-1;i++){
                target[(i-1)/3][counter] = (int) Double.parseDouble(locDataArray[i]); //first [] ensures the first three are col 0 then the next 3 at 1 etc
                counter ++;
                if(counter == 3){
                    counter = 0;
                }
            }
            for(int i = 0; i < target.length; i++){
                for(int j = 0; j < target[i].length; j++){
                    targetCopy[i][j] = target[i][j];
                }
            }
            //toast(Integer.toString(target[0][0])+" "+Integer.toString(target[0][1])+" "+Integer.toString(target[0][2]));
            //setTarget(target[0][0], target[0][1], target[0][2]);
        }
    }
    private void proximityAudio(){
        //TODO: Start a timer here for every 2s which calls a method, proximityAudioSoundCue
            //That method will take the current position compared to the target to play a beep with variable volume
            timer = new CountDownTimer(1000000000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                proximityAudioSoundCue();
            }

            @Override
            public void onFinish() {

            }

        };
        timer.start();
    }
    private void proximityAudioSoundCue(){
        //Take the abs distance of x and y of current from target
        //Then take each distance and divide it by the target value (bigger value = further away)

        double xDis, yDis, volume;
        //xDis = abs(target[targetCounter][0] - userCircleNotch.getX());
        //yDis = abs(target[targetCounter][1] - userCircleNotch.getY());
        xDis = abs(goalCircleNotch.getX() - userCircleNotch.getX());
        yDis = abs(goalCircleNotch.getY() - userCircleNotch.getY());
        volume = (xDis+yDis)/2/500; //500 is arbitrarily chosen
        if(volume>1.0){
            volume = 1.0;
        }
        volume = 1.0-volume; //Reverse volume so that high number indicates far away low is close
        MediaPlayer mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.beep_02);
        mediaPlayer.setVolume((float)volume,(float)volume);
        mediaPlayer.start();
    }

    private void proximityAudioOnCountdown(){
        if(timerStarted) {
            timer.cancel();
            timerStarted = false;
        }
        else {
            timer.start();
            timerStarted = true;
        }
    }

    private int getMax() {
        int max=arrayROM[0];
        for (int i = 0; i<arrayROM.length; i++) {
            if (max<arrayROM[i+1])
                max = arrayROM[i+1];
        }
        return max;
    }

    private void getInitialLocation() {
        userCircleNotch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                userCircleNotch.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                userCircleNotch.getLocationOnScreen(initUserLocations);
                initX = initUserLocations[0];
                initY = initUserLocations[1];
            }
        });
        goalCircleNotch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                userCircleNotch.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                goalCircleNotch.getLocationOnScreen(initGoalLocations);
                goalInitX = initGoalLocations[0];
                goalInitY = initGoalLocations[1];
            }
        });
    }

    private int[] getLocation(ImageView circle) {
        circle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                circle.getLocationOnScreen(currentLocations);
                int x = currentLocations[0];
                int y = currentLocations[1];
                //testText.setText(x+" ,"+y);

            }
        });
        return currentLocations;
    }

    @SuppressLint("StaticFieldLeak")
    private void initRenderer() {
        action = new Runnable() {
            @Override
            public void run() {
                try {
                    int dataIndex = 0;
                    if (mZipUri != null) {
                        for (Parcelable uri : mZipUri) {
                            VisualiserData data = VisualiserData.fromStream(new FileInputStream(new File(((Uri) uri).getPath())));
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "NotchSkeletonRenderer exception", e);
                }
                mFrameCount = mData.getFrameCount();
                mFrequency = mData.getFrequency();
                mSkeleton = mData.getSkeleton();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OPEN:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    setData(data.getData());
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void setData(final Uri zipUri) {
        new AsyncTask<Void, Void, VisualiserData>() {
            @Override
            protected VisualiserData doInBackground(Void... params) {
                try {
                    return VisualiserData.fromStream(new FileInputStream(new File(zipUri.getPath())));
                } catch (Exception e) {
                    Log.e(TAG, "NotchSkeletonRenderer exception", e);
                    return null;
                }
            }
            @Override
            protected void onPostExecute(final VisualiserData data) {
                super.onPostExecute(data);
                if (data != null) {
                    mZipUri = Arrays.copyOf(mZipUri, mZipUri.length + 1);
                    mZipUri[mZipUri.length - 1] = zipUri;
                    getIntent().putExtra(PARAM_INPUT_ZIP, mZipUri);
                    mData = data;
                    refreshUI();
                } else {
                    toast("Error in measurement");
                }
            }
        }.execute();
    }

    private void mergeRealTimePlotData() {
        if (mSkeleton != null) {
            for (Bone bone : mBonesToShow) {
                List<fvec3> points = realTimePlotData.get(bone);
                if (points == null) {
                    points = new ArrayList<>();
                    for (int i = 0; i < mMaxFrameCount; i++) {
                        points.add(new fvec3());
                    }
                }
                points.add(mData.getPos(bone, mFrameIndex));
                if (points.size() > mMaxFrameCount) {
                    points.remove(0);
                }
                realTimePlotData.put(bone, points);
            }
        }
    }



        public void showExerciseCompleteDialog() {
        completionDialog.setContentView(R.layout.dialog_exercise_complete);
        closeDialog = completionDialog.findViewById(R.id.btn_close_exercise_complete_dialog);
        dialogDesc = completionDialog.findViewById(R.id.dialog_exercise_complete_description);
        dialogTitle = completionDialog.findViewById(R.id.dialog_exercise_complete_title);
        dialogMsg = completionDialog.findViewById(R.id.dialog_exercise_complete_message);
        if(!therapistFlag) {
            String dialogTextInfo = "Sets Progress: " + dialogMsgInfo[1] + dialogMsgInfo[0] + "\nSkips Used: " + dialogMsgInfo[2];
            dialogMsg.setText(dialogTextInfo);
        }
        else{
            String dialogTextInfo = "Great work, now try assigning this to a patient";
            dialogMsg.setText(dialogTextInfo);
        }
        if(isResponseTime){
            String dialogTextInfo = "Average Response Time: "+((double)avgTime)/1000 +" Seconds";
            dialogMsg.setText(dialogTextInfo);
        }
        showLog = completionDialog.findViewById(R.id.btn_show_log);
        showLog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completionDialog.dismiss();
            }
        });
        showLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completionDialog.dismiss();
                showLog();
            }
        });
        completionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        completionDialog.show();
    }

    public void showLog() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy 'at' HH:mm a");
        String currentDate = sdf.format(new Date());
        Bundle bundle = new Bundle();
        bundle.putString("logTitle", "ExerciseTitle");
        bundle.putString("logDate", currentDate);
        bundle.putString("logReps", currentRep + "/" + totalReps);
        if(exerciseTime%60<10)
            bundle.putString("logTime", exerciseTime/60+":0"+exerciseTime%60);
        else
            bundle.putString("logTime", exerciseTime/60+":"+exerciseTime%60);
        bundle.putString("logSkippedTargets", textTargetsSkipped);
        ExerciseLogFragment logFragment = new ExerciseLogFragment();
        logFragment.setArguments(bundle);

        setButtonsInvisible();
        getSupportFragmentManager().beginTransaction().add(R.id.constraint_layout_therapy, logFragment).commit();

    }

    public void setButtonsInvisible() {
        skipTarget.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        showAngles.setVisibility(View.INVISIBLE);
    }

    public void setButtonsVisible() {
        skipTarget.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
        showAngles.setVisibility(View.VISIBLE);
    }

    private void toast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mApplicationContext,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

}