package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewPatientActivity extends AppCompatActivity {
    public static final String EXTRA_PATIENT_ID = "com.colinelliott.newscratch.EXTRA_PATIENT_ID";
    public static final String EXTRA_PATIENT_NAME = "com.colinelliott.newscratch.EXTRA_PATIENT_NAME";
    public static final String EXTRA_PATIENT_COMMENTS = "com.colinelliott.newscratch.EXTRA_PATIENT_COMMENTS";

    EditText name, comment, password;
    TextView textFlexROM,textExtendROM, textAbdROM, textAddROM, textIntRotROM, textExtRotROM, id;
    TextView dataFlexROM, dataExtendROM, dataAbdROM, dataAddROM, dataIntRotROM, dataExtRotROM, exeNames;
    Button savePatient, goniometer;
    String patientAssignments[], patientAssignmentText;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        PatientDatabase patientDb = PatientDatabase.getInstance(this);

        //Initialize
        name = findViewById(R.id.edittext_patient_information_name);
        comment = findViewById(R.id.edittext_patient_information_comments);
        textFlexROM = findViewById(R.id.text_patient_flex_ROM);
        textExtendROM = findViewById(R.id.text_patient_extend_ROM);
        textAbdROM = findViewById(R.id.text_patient_abd_ROM);
        textAddROM = findViewById(R.id.text_patient_abd_ROM);
        textIntRotROM = findViewById(R.id.text_patient_int_rot_ROM);
        textExtRotROM = findViewById(R.id.text_patient_ext_rot_ROM);
        dataFlexROM = findViewById(R.id.personal_flex_ROM_data);
        dataExtendROM = findViewById(R.id.personal_extend_ROM_data);
        dataAbdROM = findViewById(R.id.personal_abd_ROM_data);
        dataAddROM = findViewById(R.id.personal_add_ROM_data);
        dataIntRotROM = findViewById(R.id.personal_int_rot_ROM_data);
        dataExtRotROM = findViewById(R.id.personal_ext_rot_ROM_data);
        savePatient = findViewById(R.id.btn_save_patient_that_exists);
        password=findViewById(R.id.PasswordEditText);
        id = findViewById(R.id.IDEditText);
        exeNames = findViewById(R.id.exerciseNameList);

        //Edit Patient Functionality/ Receiving Data from PatientListFragment and Incorporating it into Screen
        Intent intent = getIntent();
        if(intent.hasExtra(CreatePatientActivity.EXTRA_PATIENT_ID)) {
            name.setText(intent.getStringExtra(CreatePatientActivity.EXTRA_FIRST_NAME) + " " + intent.getStringExtra(CreatePatientActivity.EXTRA_LAST_NAME));
            comment.setText(intent.getStringExtra(CreatePatientActivity.EXTRA_COMMENTS));
        }
        Patient patient = patientDb.patientDao().findUserWithID(intent.getIntExtra(EXTRA_PATIENT_ID,-1)).get(0);
        //Show editable ID/Pass
        password.setText(Integer.toString(patient.getPassword()));
        id.setText(Integer.toString(patient.getTherapistID()));

        //Show patient's exercises and catch if there are none assigned
        CreateExerciseNoteDatabase exerciseDb = CreateExerciseNoteDatabase.getInstance(this);
        String exerciseName;
        patientAssignments = patient.getPatientData().split(",");
        if(patientAssignments.length>3){ //value 3 is arbitrary, mostly just checking that the default value is not there
            patientAssignmentText = "";
            for(int i = 3; i < patientAssignments.length; i++){ //Order is Sets Freq, Sets#, Reps#, ExeNames
                exerciseName = exerciseDb.createExerciseNoteDao().getExerciseById(Integer.parseInt(patientAssignments[i])).get(0).getExerciseTitle();
                patientAssignmentText = patientAssignmentText + patientAssignments[1] + " sets of " + exerciseName + " for "+patientAssignments[2]+ " reps "+patientAssignments[0] +"\n";
            }
            exeNames.setText(patientAssignmentText);
        }

        //Save Button
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String commentsText = comment.getText().toString();

                Intent patientData = new Intent();
                patientData.putExtra(EXTRA_PATIENT_NAME,nameText);
                patientData.putExtra(EXTRA_PATIENT_COMMENTS,commentsText);

                int patientId = getIntent().getIntExtra(EXTRA_PATIENT_ID,-1);
                if(patientId!=-1) {
                    patientData.putExtra(EXTRA_PATIENT_ID,patientId);
                }
                setResult(Activity.RESULT_OK,patientData);
                finish();

            }
        });
    }
}