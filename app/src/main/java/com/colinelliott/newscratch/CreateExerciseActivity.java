package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CreateExerciseActivity extends AppCompatActivity {
    public static final String EXTRA_EXERCISE_ID = "com.colinelliott.newscratch.EXTRA_EXERCISE_ID";
    public static final String EXTRA_TITLE = "com.colinelliott.newscratch.EXTRA_TITLE";
    public static final String EXTRA_POSITION = "com.colinelliott.newscratch.EXTRA_POSITION";
    public static final String EXTRA_RT = "com.colinelliott.newscratch.EXTRA_RT";
    public static final String EXTRA_DESCRIPTION = "com.colinelliott.newscratch.EXTRA_DESCRIPTION";
    //Joint Motion String
    public static final String EXTRA_FLEX = "com.colinelliott.newscratch.EXTRA_FLEX";public static final String EXTRA_ABD = "com.colinelliott.newscratch.EXTRA_ABD";public static final String EXTRA_ROT = "com.colinelliott.newscratch.EXTRA_ROT";
    //Int (010)
    public static final String EXTRA_GONIOMETER_FLAG = "com.colinelliott.newscratch.EXTRA_GONIOMETER_FLAG";
    public static final String EXTRA_XYZ = "com.colinelliott.newscratch.EXTRA_XYZ";
    public static final String EXTRA_XYZ_STRING = "com.colinelliott.newscratch.EXTRA_XYZ_STRING";
    public static final String EXTRA_JOINT = "com.colinelliott.newscratch.EXTRA_JOINT";
    public static final String EXTRA_JOINT_LOCATION = "com.colinelliott.newscratch.EXTRA_JOINT_LOCATION";
    public static final String EXTRA_EXERCISE_TIME = "com.colinelliott.newscratch.EXTRA_EXERCISE_TIME";
    public static final String EXTRA_MINUTES_INT = "com.colinelliott.newscratch.EXTRA_MINUTES_INT";
    public static final String EXTRA_SECONDS_INT = "com.colinelliott.newscratch.EXTRA_SECONDS_INT";
    public static final String EXTRA_EXERCISE_REPS = "com.colinelliott.newscratch.EXTRA_EXERCISE_REPS";
    //Targets
    public static final String EXTRA_TARGET_ARRAY = "com.colinelliott.newscratch.EXTRA_TARGET_ARRAY";
    public static final String EXTRA_TARGET_1 = "com.colinelliott.newscratch.EXTRA_TARGET_1";public static final String EXTRA_TARGET_2 = "com.colinelliott.newscratch.EXTRA_TARGET_2";public static final String EXTRA_TARGET_3 = "com.colinelliott.newscratch.EXTRA_TARGET_3";public static final String EXTRA_TARGET_4 = "com.colinelliott.newscratch.EXTRA_TARGET_4";public static final String EXTRA_TARGET_5 = "com.colinelliott.newscratch.EXTRA_TARGET_5";public static final String EXTRA_TARGET_6 = "com.colinelliott.newscratch.EXTRA_TARGET_6";public static final String EXTRA_TARGET_7 = "com.colinelliott.newscratch.EXTRA_TARGET_7";public static final String EXTRA_TARGET_8 = "com.colinelliott.newscratch.EXTRA_TARGET_8";public static final String EXTRA_TARGET_9 = "com.colinelliott.newscratch.EXTRA_TARGET_9";public static final String EXTRA_TARGET_10 = "com.colinelliott.newscratch.EXTRA_TARGET_10";public static final String EXTRA_TARGET_11 = "com.colinelliott.newscratch.EXTRA_TARGET_11";public static final String EXTRA_TARGET_12 = "com.colinelliott.newscratch.EXTRA_TARGET_12";
    //ROM Int Values
    public static final String EXTRA_FLEX_ROM = "com.colinelliott.newscratch.EXTRA_FLEX_ROM";public static final String EXTRA_EXTEND_ROM = "com.colinelliott.newscratch.EXTRA_EXTEND_ROM";public static final String EXTRA_ABD_ROM = "com.colinelliott.newscratch.EXTRA_ABD_ROM";public static final String EXTRA_ADD_ROM = "com.colinelliott.newscratch.EXTRA_ADD_ROM";public static final String EXTRA_INTROT_ROM = "com.colinelliott.newscratch.EXTRA_INTROT_ROM";public static final String EXTRA_EXTROT_ROM = "com.colinelliott.newscratch.EXTRA_EXTROT_ROM";


    TextView header, flexAngle, extendAngle, abdAngle, addAngle, intRotAngle, extRotAngle, textTargetAbdAdd, textTargetFlexExtend, textTargetIntExtRot, minutesText, secondsText;
    TextView pastTargets1, pastTargets2, pastTargets3, pastTargets4, pastTargets5, pastTargets6, pastTargets7, pastTargets8, pastTargets9, pastTargets10, pastTargets11, pastTargets12;
    Button done, goniometerMode, nextTarget, flexTarget, extendTarget,abdTarget, addTarget, intRotTarget, extRotTarget, resetTargets;
    Switch flex,abd,rot, responseTime;
    Spinner chooseJoint,chooseJointLocation,lengthOfExercise;
    private int exerciseCount=0;
    EditText exerciseTitle, currentPositionText, exerciseDescription, flexExtendTarget, abdAddTarget,intExtRotTarget;
    NumberPicker minutes, seconds, repetitions;
    String flexString="", abdString="", rotString="";
    String text, targetText, location="";
    int minutesInt,secondsInt,repsInt;
    int numTargets=0;
    int maxTargets = 12;
    int [] xyz;
    int [][] targetArray =  new int[maxTargets][3];
    String[] pluginTargetArray = new String[maxTargets*3];
    int flexROM,extendROM,abdROM,addROM,intRotROM,extRotROM;
    private boolean goniometerFlag = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        getROMData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        initialize();

        setNumberPickers();
        setJointLocationSpinner();
        setLengthOfExerciseSpinner();
        setSwitchActions();
        //setGoniometerMode();
        setTargetButtons();
        setNextTargetButton();
        setResetTargetsButton();
        setJointSpinner();
        //Edit Exercise Functionality
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_EXERCISE_ID)) {
            exerciseTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            exerciseDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            currentPositionText.setText(intent.getStringExtra(EXTRA_POSITION));
            chooseJoint.setSelection(getIndex(chooseJoint,intent.getStringExtra(EXTRA_JOINT)));

            if (chooseJointLocation.getVisibility()==View.VISIBLE)
            chooseJointLocation.setSelection(getIndex(chooseJointLocation,intent.getStringExtra(EXTRA_JOINT_LOCATION)));
            if (intent.getIntExtra(EXTRA_MINUTES_INT,0)<0 && intent.getIntExtra(EXTRA_SECONDS_INT,0)<0 && intent.getIntExtra(EXTRA_EXERCISE_REPS,0)<0) {
                lengthOfExercise.setSelection(3);
            }
            else if (intent.getIntExtra(EXTRA_EXERCISE_REPS,0)>0) {
                lengthOfExercise.setSelection(2);
                repetitions.setValue(intent.getIntExtra(EXTRA_EXERCISE_REPS,0));
            }
            else {
                lengthOfExercise.setSelection(1);
                minutes.setValue(intent.getIntExtra(EXTRA_MINUTES_INT,0));
                seconds.setValue(intent.getIntExtra(EXTRA_SECONDS_INT,0));
            }

            if (intent.getStringExtra(EXTRA_FLEX)!="") {
                flex.setEnabled(true);
                flex.setChecked(true);
            }
            else {
                flex.setEnabled(false);
                flex.setChecked(false);
            }
            if (intent.getStringExtra(EXTRA_ABD)!="") {
                abd.setEnabled(true);
                abd.setChecked(true);
            }
            else {
                flex.setEnabled(false);
                flex.setChecked(false);
            }
            if (intent.getStringExtra(EXTRA_ROT)!="") {
                rot.setEnabled(true);
                rot.setChecked(true);
            }
            else {
                rot.setEnabled(false);
                rot.setChecked(false);
            }
            //Target Input Information
            pluginTargetArray = intent.getStringArrayExtra(EXTRA_TARGET_ARRAY);
            /*for(int i = 0; i<maxTargets*3; i++) {
                if(pluginTargetArray[0].equals("000") && pluginTargetArray[1].equals("000") && pluginTargetArray[2].equals("000")) {
                    pastTargets1.setText(pluginTargetArray[i]);
                }
            }*/
                pastTargets1.setVisibility(View.VISIBLE);
                pastTargets1.setText("Target 1:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[0]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[1]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[2]));

            if(!pluginTargetArray[3].equals("000") || !pluginTargetArray[4].equals("000") || !pluginTargetArray[5].equals("000")) {
                pastTargets2.setVisibility(View.VISIBLE);
                pastTargets2.setText("Target 2:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[3]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[4]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[5]));
            }
            if(!pluginTargetArray[6].equals("000") || !pluginTargetArray[7].equals("000") || !pluginTargetArray[8].equals("000")) {
                pastTargets3.setVisibility(View.VISIBLE);
                pastTargets3.setText("Target 3:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[6]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[7]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[8]));
            }
            if(!pluginTargetArray[9].equals("000") || !pluginTargetArray[10].equals("000") || !pluginTargetArray[11].equals("000")) {
                pastTargets4.setVisibility(View.VISIBLE);
                pastTargets4.setText("Target 4:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[9]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[10]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[11]));
            }
            if(!pluginTargetArray[12].equals("000") || !pluginTargetArray[13].equals("000") || !pluginTargetArray[14].equals("000")) {
                pastTargets5.setVisibility(View.VISIBLE);
                pastTargets5.setText("Target 5:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[12]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[13]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[14]));
            }
            if(!pluginTargetArray[15].equals("000") || !pluginTargetArray[16].equals("000") || !pluginTargetArray[17].equals("000")) {
                pastTargets6.setVisibility(View.VISIBLE);
                pastTargets6.setText("Target 6:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[15]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[16]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[17]));
            }
            if(!pluginTargetArray[18].equals("000") || !pluginTargetArray[19].equals("000") || !pluginTargetArray[20].equals("000")) {
                pastTargets7.setVisibility(View.VISIBLE);
                pastTargets7.setText("Target 7:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[18]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[19]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[20]));
            }
            if(!pluginTargetArray[21].equals("000") || !pluginTargetArray[22].equals("000") || !pluginTargetArray[23].equals("000")) {
                pastTargets8.setVisibility(View.VISIBLE);
                pastTargets8.setText("Target 8:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[21]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[22]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[23]));
            }
            if(!pluginTargetArray[24].equals("000") || !pluginTargetArray[25].equals("000") || !pluginTargetArray[26].equals("000")) {
                pastTargets9.setVisibility(View.VISIBLE);
                pastTargets9.setText("Target 9:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[24]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[25]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[26]));
            }
            if(!pluginTargetArray[27].equals("000") || !pluginTargetArray[28].equals("000") || !pluginTargetArray[29].equals("000")) {
                pastTargets10.setVisibility(View.VISIBLE);
                pastTargets10.setText("Target 10:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[27]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[28]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[29]));
            }
            if(!pluginTargetArray[30].equals("000") || !pluginTargetArray[31].equals("000") || !pluginTargetArray[32].equals("000")) {
                pastTargets11.setVisibility(View.VISIBLE);
                pastTargets11.setText("Target 11:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[30]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[31]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[32]));
            }
            if(!pluginTargetArray[33].equals("000") || !pluginTargetArray[34].equals("000") || !pluginTargetArray[35].equals("000")) {
                pastTargets12.setVisibility(View.VISIBLE);
                pastTargets12.setText("Target 12:\n" + flex.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[33]) + "\n" + abd.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[34]) + "\n" + rot.getText().toString() + ": " + Integer.parseInt(pluginTargetArray[35]));
            }
        }

        //Done button activities
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (title.length()>2)
                exerciseCount++;
                //Toast.makeText(getApplicationContext(),Integer.toString(exerciseCount),Toast.LENGTH_SHORT).show();

                saveCreateExerciseNote();  //And finish activity

            }
        });

        exerciseDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    done.performClick();
                }
                return false;
            }
        });
    }

    private void getROMData() {
        flexROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_FLEXION,0);
        extendROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_EXTENSION,0);
        abdROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_ABDUCTION,0);
        addROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_ADDUCTION,0);
        intRotROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_INTERNAL_ROTATION,0);
        extRotROM = getIntent().getIntExtra(TherapyActivity.EXTRA_MAX_EXTERNAL_ROTATION,0);
        //Set ROM Data Text
        flexAngle.setText(R.string.flexion_angle_text + flexROM + "°");
        extendAngle.setText(R.string.extension_angle_text + extendROM + "°");
        abdAngle.setText(R.string.abduction_angle_text + abdROM + "°");
        addAngle.setText(R.string.adduction_angle_text + addROM + "°");
        intRotAngle.setText(R.string.internal_rotation_angle_text + intRotROM + "°");
        extRotAngle.setText(R.string.external_rotation_angle_text + extRotROM + "°");
        //Set Button Text
        flexTarget.setText(flexROM);
        extendTarget.setText(extendROM);
        abdTarget.setText(abdROM);
        addTarget.setText(addROM);
        intRotTarget.setText(intRotROM);
        extRotTarget.setText(extRotROM);
    }
/*
    private void setGoniometerMode() {
        goniometerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goniometerFlag = true;
                Intent intent1 = new Intent();
                if (flex.isChecked() && flex.isEnabled())
                    xyz[0] = 1;
                else
                    xyz[0] = 0;
                if (abd.isChecked() && abd.isEnabled())
                    xyz[1] = 1;
                else
                    xyz[1] = 0;
                if (rot.isChecked() && rot.isEnabled())
                    xyz[2] = 1;
                else
                    xyz[2] = 0;

                String xyzStringValue = String.valueOf(xyz[0])+String.valueOf(xyz[1])+String.valueOf(xyz[2]);
                intent1.putExtra(EXTRA_XYZ_STRING,xyzStringValue);
                intent1.putExtra(EXTRA_GONIOMETER_FLAG,goniometerFlag);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.create_exercise_activity_container, PairFragment.newInstance())
                        .commit();
            }
        });
    }
*/
    private void setResetTargetsButton() {
        resetTargets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<maxTargets;i++ ) {
                    for (int j =0; j<3;j++) {
                        targetArray[i][j]=0;
                    }
                }
                //pastTargets1.setText("");pastTargets2.setText("");pastTargets3.setText("");pastTargets4.setText("");pastTargets5.setText("");pastTargets6.setText("");pastTargets7.setText("");pastTargets8.setText("");pastTargets9.setText("");pastTargets10.setText("");pastTargets11.setText("");
                pastTargets1.setVisibility(View.GONE);pastTargets2.setVisibility(View.GONE);pastTargets3.setVisibility(View.GONE);pastTargets4.setVisibility(View.GONE);pastTargets5.setVisibility(View.GONE);pastTargets6.setVisibility(View.GONE);pastTargets7.setVisibility(View.GONE);pastTargets8.setVisibility(View.GONE);pastTargets9.setVisibility(View.GONE);pastTargets10.setVisibility(View.GONE);pastTargets11.setVisibility(View.GONE);

            }
        });
    }

    private void setJointSpinner() {
        //Spinner ChooseJoint Actions
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateExerciseActivity.this,R.array.joint_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseJoint.setAdapter(adapter);
        chooseJoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();
                if (position==0) {
                    flex.setEnabled(false);
                    flex.setChecked(false);
                    abd.setEnabled(false);
                    abd.setChecked(false);
                    rot.setEnabled(false);
                    rot.setChecked(false);
                }
                if (position>0) {
                    Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                    flex.setEnabled(true);
                    flex.setChecked(true);
                    abd.setEnabled(true);
                    abd.setChecked(true);
                    rot.setEnabled(true);
                    rot.setChecked(true);
                    flex.setText(R.string.switch_text_flexion_extension);
                    abd.setText(R.string.switch_text_abduction_adduction);
                    rot.setText(R.string.switch_text_rotation);
                    textTargetFlexExtend.setText("Flexion/\nExtension");
                    textTargetAbdAdd.setText("Abduction/\nAdduction");
                    textTargetIntExtRot.setText("Internal/\nExternal\nRotation");
                    chooseJointLocation.setVisibility(View.INVISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Ankle")) { //Dorsiflexion/plantarflexion and inversion/eversion
                    abd.setEnabled(false);
                    abd.setChecked(false);
                    flex.setText(R.string.switch_text_dorsiflexion_plantarflexion);
                    textTargetFlexExtend.setText("Dorsiflexion/\nPlantarFlexion");
                    rot.setText(R.string.switch_text_inversion_eversion);
                    textTargetIntExtRot.setText("Inversion/\nEversion");
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Cervical Spine")) { //Ant/post flexion, left/right flexion, left/right rotation
                    flex.setText(R.string.switch_text_anterior_posterior_flexion);
                    textTargetFlexExtend.setText("Anterior/\nPosterior\nFlexion");
                    abd.setText(R.string.switch_text_left_right_flexion);
                    textTargetAbdAdd.setText("Left/Right\nFlexion");
                    rot.setText(R.string.switch_text_left_right_rotation);
                    textTargetIntExtRot.setText("Left/Right\nRotation");
                }
                if (text.equals("Elbow")) { //Flexion/Extension, sup/pronation
                    abd.setEnabled(false);
                    abd.setChecked(false);
                    rot.setText(R.string.switch_text_supination_pronation);
                    textTargetIntExtRot.setText("Supination/\nPronation");
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Hip")) { //Flexion/Extension, Abduction/adduction, internal/external rotation
                    //Do nothing
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Knee")) { //Flexion/Extension
                    abd.setEnabled(false);
                    abd.setChecked(false);
                    rot.setEnabled(false);
                    rot.setChecked(false);
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Lumbar Spine")) { //Ant/post flexion, left/right flexion, left/right rotation
                    flex.setText(R.string.switch_text_anterior_posterior_flexion);
                    textTargetFlexExtend.setText("Anterior/\nPosterior\nFlexion");
                    abd.setText(R.string.switch_text_left_right_flexion);
                    textTargetAbdAdd.setText("Left/Right\nFlexion");
                    rot.setText(R.string.switch_text_left_right_rotation);
                    textTargetIntExtRot.setText("Left/Right\nRotation");
                }
                if (text.equals("Neck")) { //Anterior/Posterior flexion, Left/Right Flexion, Left/Right Rotation
                    flex.setText(R.string.switch_text_anterior_posterior_flexion);
                    textTargetFlexExtend.setText("Anterior/\nPosterior\nFlexion");
                    abd.setText(R.string.switch_text_left_right_flexion);
                    textTargetAbdAdd.setText("Left/Right\nFlexion");
                    rot.setText(R.string.switch_text_left_right_rotation);
                    textTargetIntExtRot.setText("Left/Right\nRotation");
                }
                if (text.equals("Shoulder")) { //Flexion/Extension, Abduction/adduction, internal/external rotation
                    //Do nothing
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
                if (text.equals("Wrist")) { //Flexion/Extension and Abduction/Adduction
                    rot.setEnabled(false);
                    rot.setChecked(false);
                    chooseJointLocation.setVisibility(View.VISIBLE);
                    chooseJointLocation.setSelection(getIndex(chooseJointLocation,"Select Location of Joint on Patient Body"));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setNumberPickers() {
        minutes.setMinValue(0);
        minutes.setMaxValue(120);
        seconds.setMinValue(0);
        seconds.setMaxValue(60);
        repetitions.setMinValue(0);
        repetitions.setMaxValue(1000);
    }

    private void setSwitchActions() {
        flex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false) {
                    //ROM Angle Section
                    flexAngle.setVisibility(View.GONE);
                    extendAngle.setVisibility(View.GONE);
                    //Target Section
                    textTargetFlexExtend.setVisibility(View.GONE);
                    flexTarget.setVisibility(View.GONE);
                    flexTarget.setEnabled(false);
                    extendTarget.setVisibility(View.GONE);
                    extendTarget.setEnabled(false);
                    flexExtendTarget.setVisibility(View.GONE);
                    flexExtendTarget.setEnabled(false);
                }
                else {
                    //ROM Angle Section
                    flexAngle.setVisibility(View.VISIBLE);
                    extendAngle.setVisibility(View.VISIBLE);
                    //Target Section
                    textTargetFlexExtend.setVisibility(View.VISIBLE);
                    flexTarget.setVisibility(View.VISIBLE);
                    flexTarget.setEnabled(true);
                    extendTarget.setVisibility(View.VISIBLE);
                    extendTarget.setEnabled(true);
                    flexExtendTarget.setVisibility(View.VISIBLE);
                    flexExtendTarget.setEnabled(true);
                }
            }
        });
        abd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false) {
                    //ROM Angle Section
                    abdAngle.setVisibility(View.GONE);
                    addAngle.setVisibility(View.GONE);
                    //Target Section
                    textTargetAbdAdd.setVisibility(View.GONE);
                    abdTarget.setVisibility(View.GONE);
                    abdTarget.setEnabled(false);
                    addTarget.setVisibility(View.GONE);
                    addTarget.setEnabled(false);
                    abdAddTarget.setVisibility(View.GONE);
                    abdAddTarget.setEnabled(false);
                }
                else {
                    //ROM Angle Section
                    abdAngle.setVisibility(View.VISIBLE);
                    addAngle.setVisibility(View.VISIBLE);
                    //Target Section
                    textTargetAbdAdd.setVisibility(View.VISIBLE);
                    abdTarget.setVisibility(View.VISIBLE);
                    abdTarget.setEnabled(true);
                    addTarget.setVisibility(View.VISIBLE);
                    addTarget.setEnabled(true);
                    abdAddTarget.setVisibility(View.VISIBLE);
                    abdAddTarget.setEnabled(true);
                }
            }
        });
        rot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false) {
                    //ROM Angle Section
                    intRotAngle.setVisibility(View.GONE);
                    extRotAngle.setVisibility(View.GONE);
                    //Target Section
                    textTargetIntExtRot.setVisibility(View.GONE);
                    intRotTarget.setVisibility(View.GONE);    //Button
                    intRotTarget.setEnabled(false);
                    extRotTarget.setVisibility(View.GONE);
                    extRotTarget.setEnabled(false);
                    intExtRotTarget.setVisibility(View.GONE);
                    intExtRotTarget.setEnabled(false);
                }
                else {
                    //ROM Angle Section
                    intRotAngle.setVisibility(View.VISIBLE);
                    extRotAngle.setVisibility(View.VISIBLE);
                    //Target Section
                    textTargetIntExtRot.setVisibility(View.VISIBLE);
                    intRotTarget.setVisibility(View.VISIBLE);    //Button
                    intRotTarget.setEnabled(true);
                    extRotTarget.setVisibility(View.VISIBLE);
                    extRotTarget.setEnabled(true);
                    intExtRotTarget.setVisibility(View.VISIBLE);
                    intExtRotTarget.setEnabled(true);
                }
            }
        });
        responseTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    responseTime.setChecked(true);
                }
                else{
                    responseTime.setChecked(false);
                }
            }
        });
    }

    private void setNextTargetButton() {
        nextTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer.valueOf(flexExtendTarget.getText().toString()) ==0 &&flexExtendTarget.getVisibility()==View.VISIBLE)||(Integer.valueOf(abdAddTarget.getText().toString())==0 &&abdAddTarget.getVisibility()==View.VISIBLE)||(Integer.valueOf(intExtRotTarget.getText().toString())==0 &&intExtRotTarget.getVisibility()==View.VISIBLE)) {
                    Toast.makeText(CreateExerciseActivity.this,"Missing Target(s)",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(numTargets>=10) {
                    Toast.makeText(CreateExerciseActivity.this,"Maximum Targets Reached",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(flexExtendTarget.getVisibility()==View.VISIBLE)
                    targetArray[numTargets][0]=Integer.valueOf(flexExtendTarget.getText().toString());
                else
                    targetArray[numTargets][0]=0;
                if(abdAddTarget.getVisibility()==View.VISIBLE)
                    targetArray[numTargets][1]=Integer.valueOf(abdAddTarget.getText().toString());
                else
                    targetArray[numTargets][1]=0;
                if(intExtRotTarget.getVisibility()==View.VISIBLE)
                    targetArray[numTargets][2]=Integer.valueOf(intExtRotTarget.getText().toString());
                else
                    targetArray[numTargets][2]=0;

                numTargets++;
                flexExtendTarget.setText("0");
                abdAddTarget.setText("0");
                intExtRotTarget.setText("0");
                pastTargets1.setVisibility(View.VISIBLE);

                //Past Targets Information
                if(numTargets==1) {
                    pastTargets1.setText("Target 1:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets2.setVisibility(View.VISIBLE);
                }
                else if (numTargets==2) {
                    pastTargets2.setText("Target 2:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets3.setVisibility(View.VISIBLE);
                }
                else if (numTargets==3) {
                    pastTargets3.setText("Target 3:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets4.setVisibility(View.VISIBLE);
                }
                else if (numTargets==4) {
                    pastTargets4.setText("Target 4:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets5.setVisibility(View.VISIBLE);
                }
                else if (numTargets==5) {
                    pastTargets5.setText("Target 5:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets6.setVisibility(View.VISIBLE);
                }
                else if (numTargets==6) {
                    pastTargets6.setText("Target 6:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets7.setVisibility(View.VISIBLE);
                }
                else if (numTargets==7) {
                    pastTargets7.setText("Target 7:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets8.setVisibility(View.VISIBLE);
                }
                else if (numTargets==8) {
                    pastTargets8.setText("Target 8:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets9.setVisibility(View.VISIBLE);
                }
                else if (numTargets==9) {
                    pastTargets9.setText("Target 9:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets10.setVisibility(View.VISIBLE);
                }
                else if (numTargets==10) {
                    pastTargets10.setText("Target 10:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                    pastTargets11.setVisibility(View.VISIBLE);
                }
                else if (numTargets==11) {
                    pastTargets11.setText("Target 11:\n" + flex.getText().toString() + ": " + targetArray[numTargets-1][0] + "\n" + abd.getText().toString() + ": " + targetArray[numTargets-1][1] + "\n" + rot.getText().toString() + ": " + targetArray[numTargets-1][2]);
                }

            }
        });
    }
    private void setTargetButtons() {
        flexTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = flexTarget.getText().toString();
                flexExtendTarget.setText(targetText);
            }
        });
        extendTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = extendTarget.getText().toString();
                flexExtendTarget.setText(targetText);
            }
        });
        abdTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = abdTarget.getText().toString();
                abdAddTarget.setText(targetText);
            }
        });
        addTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = addTarget.getText().toString();
                abdAddTarget.setText(targetText);
            }
        });
        intRotTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = intRotTarget.getText().toString();
                intExtRotTarget.setText(targetText);
            }
        });
        extRotTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetText = extRotTarget.getText().toString();
                intExtRotTarget.setText(targetText);
            }
        });
    }

    private void setLengthOfExerciseSpinner() {
        ArrayAdapter<CharSequence> adapterLength = ArrayAdapter.createFromResource(CreateExerciseActivity.this,R.array.target_end_condition_array, android.R.layout.simple_spinner_dropdown_item);
        adapterLength.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthOfExercise.setAdapter(adapterLength);
        lengthOfExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    minutes.setVisibility(View.INVISIBLE);
                    minutes.setEnabled(false);
                    seconds.setVisibility(View.INVISIBLE);
                    seconds.setEnabled(false);
                    minutesText.setVisibility(View.INVISIBLE);
                    secondsText.setVisibility(View.INVISIBLE);
                    repetitions.setVisibility(View.INVISIBLE);
                    repetitions.setEnabled(false);
                }
                if(position==1){   //Timer
                    minutes.setVisibility(View.VISIBLE);
                    minutes.setEnabled(true);
                    seconds.setVisibility(View.VISIBLE);
                    seconds.setEnabled(true);
                    minutesText.setVisibility(View.VISIBLE);
                    secondsText.setVisibility(View.VISIBLE);
                }
                if(position==2) {  //Repetitions
                    repetitions.setVisibility(View.VISIBLE);
                    repetitions.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setJointLocationSpinner() {
        ArrayAdapter<CharSequence> adapterL = ArrayAdapter.createFromResource(CreateExerciseActivity.this,R.array.joint_location_array, android.R.layout.simple_spinner_dropdown_item);
        adapterL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseJointLocation.setAdapter(adapterL);
        chooseJointLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getIndex(Spinner spinner, String string) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(string)){
                index = i;
            }
        }
        return index;
    }


    public int getExerciseCount() {
        return exerciseCount;
    }

    private void saveCreateExerciseNote() {
        String exercise = exerciseTitle.getText().toString();
        String currentPosition = currentPositionText.getText().toString();
        String jointString;
        String joint = text;
        if(chooseJointLocation.getVisibility()==View.INVISIBLE)
            location = "";
        parseROM();
        if(text.equals("Cervical Spine")||text.equals("Lumbar Spine")||text.equals("Neck"))
            jointString = text;
        else
            jointString = location+" "+text;
        int targeta,targetb,targetc;
        if(flexExtendTarget.getVisibility()==View.VISIBLE)
            targeta =Integer.valueOf(flexExtendTarget.getText().toString());
        else
            targeta=0;
        if(abdAddTarget.getVisibility()==View.VISIBLE)
            targetb=Integer.valueOf(abdAddTarget.getText().toString());
        else
            targetb=0;
        if(intExtRotTarget.getVisibility()==View.VISIBLE)
            targetc=Integer.valueOf(intExtRotTarget.getText().toString());
        else
            targetc=0;

        minutesInt = minutes.getValue();
        secondsInt = seconds.getValue();
        int totalTime = (60*minutesInt)+secondsInt;
        repsInt = repetitions.getValue();
        String description = exerciseDescription.getText().toString();
        if (flex.isChecked() && flex.isEnabled()) {
            flexString = (String) flex.getText();
            xyz[0] = 1;
        }
        else {
            flexString = "";
            xyz[0] = 0;
        }
        if (abd.isChecked() && abd.isEnabled()) {
            abdString = (String) abd.getText();
            xyz[1] = 1;
        }
        else {
            abdString = "";
            xyz[1] = 0;
        }
        if (rot.isChecked() && rot.isEnabled()) {
            rotString = (String) rot.getText();
            xyz[2] = 1;
        }
        else {
            rotString = "";
            xyz[2] = 0;
        }
        String xyzStringValue = String.valueOf(xyz[0])+String.valueOf(xyz[1])+String.valueOf(xyz[2]);
        int xyzNumberValue = Integer.parseInt(xyzStringValue);

        if(exercise.trim().isEmpty()||targeta <-180 &&flexExtendTarget.getVisibility()==View.VISIBLE||targeta>180 &&flexExtendTarget.getVisibility()==View.VISIBLE||targetb<-180 && abdAddTarget.getVisibility()==View.VISIBLE||targetb>180 && abdAddTarget.getVisibility()==View.VISIBLE|| targetc<-180 && intExtRotTarget.getVisibility()==View.VISIBLE||targetc>180 && intExtRotTarget.getVisibility()==View.VISIBLE||(!flex.isChecked() && !abd.isChecked() && !rot.isChecked())) {
            Toast.makeText(this,"Missing/Incorrect Information",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,exercise);
        data.putExtra(EXTRA_POSITION,currentPosition);
        data.putExtra(EXTRA_JOINT,joint);
        data.putExtra(EXTRA_JOINT_LOCATION,location);
        data.putExtra(EXTRA_RT,responseTime.isChecked());
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_FLEX,flexString);
        data.putExtra(EXTRA_ABD,abdString);
        data.putExtra(EXTRA_ROT,rotString);
        data.putExtra(EXTRA_XYZ,xyzNumberValue);
        if (lengthOfExercise.getSelectedItem().toString().equals("Time")) {
            data.putExtra(EXTRA_EXERCISE_TIME,totalTime);
            data.putExtra(EXTRA_EXERCISE_REPS,-1);
            data.putExtra(EXTRA_MINUTES_INT,minutesInt);
            data.putExtra(EXTRA_SECONDS_INT,secondsInt);
        }
        else if(lengthOfExercise.getSelectedItem().toString().equals("Repetitions")) {
            data.putExtra(EXTRA_EXERCISE_TIME,-1);
            data.putExtra(EXTRA_MINUTES_INT,-1);
            data.putExtra(EXTRA_SECONDS_INT,-1);
            data.putExtra(EXTRA_EXERCISE_REPS,repsInt);
        }
        else {
            data.putExtra(EXTRA_EXERCISE_TIME,-1);
            data.putExtra(EXTRA_EXERCISE_REPS,-1);
        }
        //Passing ROM Data

        //Passing Target Data
        targetArray[numTargets][0] = targeta;
        targetArray[numTargets][1] = targetb;
        targetArray[numTargets][2] = targetc;
        String[] finalTargetArray = new String[maxTargets*3];
        for(int i = 0; i<finalTargetArray.length;i++) {
            finalTargetArray[i] = "0";
        }
        int s = 0;
        for (int i = 0; i<maxTargets; i++) {
            for(int j = 0; j<3; j++) {
                    if(String.valueOf(targetArray[i][j]).length() ==3)
                        finalTargetArray[s] = String.valueOf(targetArray[i][j]);
                    else if(String.valueOf(targetArray[i][j]).length() ==2)
                        finalTargetArray[s] = "0"+targetArray[i][j];
                    else
                        finalTargetArray[s] = "00"+targetArray[i][j];
                    s++;
            }
        }

        /*for (int i = 0; i<finalTargetArray.length;i++) {
            if (i==0 && String.valueOf(finalTargetArray[i]).length() == 3)
                target1 = Integer.parseInt(String.valueOf(finalTargetArray[0]) + String.valueOf(finalTargetArray[1]) + String.valueOf(finalTargetArray[2]));
            else if (i==0 && String.valueOf(finalTargetArray[i]).length() == 2)
                target1 = Integer.parseInt("0"+String.valueOf(finalTargetArray[0]) + "0"+String.valueOf(finalTargetArray[1]) + "0"+String.valueOf(finalTargetArray[2]));
            else if (i==0 && String.valueOf(finalTargetArray[i]).length()==1)
                target1 = Integer.parseInt("00"+String.valueOf(finalTargetArray[0]) + "00"+String.valueOf(finalTargetArray[1]) + "0"+String.valueOf(finalTargetArray[2]));

        }*/

        String target1 = finalTargetArray[0]+finalTargetArray[1]+finalTargetArray[2];String target2 = finalTargetArray[3]+finalTargetArray[4]+finalTargetArray[5];String target3 = finalTargetArray[6]+finalTargetArray[7]+finalTargetArray[8];String target4 = finalTargetArray[9]+finalTargetArray[10]+finalTargetArray[11];String target5 = finalTargetArray[12]+finalTargetArray[13]+finalTargetArray[14];String target6 = finalTargetArray[15]+finalTargetArray[16]+finalTargetArray[17];String target7 = finalTargetArray[18]+finalTargetArray[19]+finalTargetArray[20];String target8 = finalTargetArray[21]+finalTargetArray[22]+finalTargetArray[23];String target9 = finalTargetArray[24]+finalTargetArray[25]+finalTargetArray[26];String target10 = finalTargetArray[27]+finalTargetArray[28]+finalTargetArray[29];String target11 = finalTargetArray[30]+finalTargetArray[31]+finalTargetArray[32];String target12 = finalTargetArray[33]+finalTargetArray[34]+finalTargetArray[35];

        data.putExtra(EXTRA_TARGET_ARRAY,finalTargetArray); //36 element String array
        data.putExtra(EXTRA_TARGET_1,target1);data.putExtra(EXTRA_TARGET_2,target2);data.putExtra(EXTRA_TARGET_3,target3);data.putExtra(EXTRA_TARGET_4,target4);data.putExtra(EXTRA_TARGET_5,target5);data.putExtra(EXTRA_TARGET_6,target6);data.putExtra(EXTRA_TARGET_7,target7);data.putExtra(EXTRA_TARGET_8,target8);data.putExtra(EXTRA_TARGET_9,target9);data.putExtra(EXTRA_TARGET_10,target10);data.putExtra(EXTRA_TARGET_11,target11);data.putExtra(EXTRA_TARGET_12,target12);


        int id = getIntent().getIntExtra(EXTRA_EXERCISE_ID,-1);
        if(id!=-1) {
            data.putExtra(EXTRA_EXERCISE_ID,id);
        }
        setResult(Activity.RESULT_OK,data);
        numTargets=0;
        finish();
    }

    private void parseROM() { //Once we code goniometer mode we will code this and pass it to ExerciseListFragment
        /*String textFlexROM = flexAngle.getText().toString();
        String [] f = textFlexROM.split(": ");
        textFlexROM = f[1];
        String textExtendROM = extendAngle.getText().toString();
        String [] e = textExtendROM.split(": ");
        textExtendROM = e[1];
        String textAbdROM = abdAngle.getText().toString();
        String [] ab = textAbdROM.split(": ");
        textAbdROM = ab[1];
        String textAddROM = addAngle.getText().toString();
        String [] ad = textAddROM.split(": ");
        textAddROM = ad[1];
        String textIntRotROM = intRotAngle.getText().toString();
        String [] in = textIntRotROM.split(": ");
        textIntRotROM = in[1];
        String textExtRotROM = extRotAngle.getText().toString();
        String [] ex = textExtRotROM.split(": ");
        textExtRotROM = ex[1];*/
    }

    private void initialize() {
        //Initialize
        header= findViewById(R.id.create_exercise_header);
        exerciseTitle = findViewById(R.id.title_exercise);
        currentPositionText = findViewById(R.id.coordinatesText);
        done = findViewById(R.id.btn_done);
        flex = findViewById(R.id.switch_flexion_extension);
        abd = findViewById(R.id.switch_abduction_adduction);
        rot = findViewById(R.id.switch_rotation);
        responseTime = findViewById(R.id.responseTimeTesting);
        chooseJoint = findViewById(R.id.spinner_choose_joint);
        exerciseDescription = findViewById(R.id.create_exercise_description);
        chooseJointLocation = findViewById(R.id.spinner_location_joint);
        lengthOfExercise = findViewById(R.id.spinner_length_of_exercise);
        flexAngle = findViewById(R.id.text_flex_angle);
        extendAngle = findViewById(R.id.text_extend_angle);
        abdAngle = findViewById(R.id.text_abd_angle);
        addAngle = findViewById(R.id.text_adduct_angle);
        intRotAngle = findViewById(R.id.text_rot_angle);
        extRotAngle = findViewById(R.id.text_external_rot_angle);
        nextTarget = findViewById(R.id.btn_next_target);
        flexTarget = findViewById(R.id.btn_target_flex_low);
        extendTarget = findViewById(R.id.btn_target_extend_high);
        abdTarget = findViewById(R.id.btn_target_abd);
        addTarget = findViewById(R.id.btn_target_add);
        intRotTarget = findViewById(R.id.btn_target_int_rot);
        extRotTarget = findViewById(R.id.btn_target_ext_rot);
        flexExtendTarget = findViewById(R.id.edittext_target_f_e_data);
        abdAddTarget = findViewById(R.id.edittext_target_abd_add_data);
        intExtRotTarget = findViewById(R.id.edittext_target_int_ext_rot_data);
        minutes = findViewById(R.id.numberpicker_minutes);
        seconds = findViewById(R.id.numberpicker_seconds);
        repetitions = findViewById(R.id.numberpicker_repetitions);
        minutesText = findViewById(R.id.text_minutes);
        secondsText = findViewById(R.id.text_seconds);
        textTargetFlexExtend = findViewById(R.id.text_target_f_e);
        textTargetAbdAdd = findViewById(R.id.text_target_abd_add);
        textTargetIntExtRot = findViewById(R.id.text_target_int_ext_rot);
        pastTargets1 = findViewById(R.id.textview_past_targets1);
        pastTargets2 = findViewById(R.id.textview_past_targets2);
        pastTargets3 = findViewById(R.id.textview_past_targets3);
        pastTargets4 = findViewById(R.id.textview_past_targets4);
        pastTargets5 = findViewById(R.id.textview_past_targets5);
        pastTargets6 = findViewById(R.id.textview_past_targets6);
        pastTargets7 = findViewById(R.id.textview_past_targets7);
        pastTargets8 = findViewById(R.id.textview_past_targets8);
        pastTargets9 = findViewById(R.id.textview_past_targets9);
        pastTargets10 = findViewById(R.id.textview_past_targets10);
        pastTargets11 = findViewById(R.id.textview_past_targets11);
        pastTargets12 = findViewById(R.id.textview_past_targets12);
        resetTargets = findViewById(R.id.btn_reset_targets);

        flexExtendTarget.setText("0");
        abdAddTarget.setText("0");
        intExtRotTarget.setText("0");
        xyz = new int[3];

    }


}