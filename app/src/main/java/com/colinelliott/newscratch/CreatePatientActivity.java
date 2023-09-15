package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePatientActivity extends AppCompatActivity {
    public static final String EXTRA_FIRST_NAME = "com.colinelliott.newscratch.EXTRA_FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "com.colinelliott.newscratch.EXTRA_LAST_NAME";
    public static final String EXTRA_COMMENTS = "com.colinelliott.newscratch.EXTRA_COMMENTS";
    public static final String EXTRA_PATIENT_ID = "com.colinelliott.newscratch.EXTRA_PATIENT_ID";

    EditText patientFirst, patientLast, comments;
    Button savePatient, goniometerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);
        //Initialize
        patientFirst = findViewById(R.id.edittext_patient_first_name);
        patientLast = findViewById(R.id.edittext_patient_last_name);
        comments = findViewById(R.id.edittext_patient_comments);
        savePatient = findViewById(R.id.btn_save_patient_information);
        //goniometerMode = findViewById(R.id.btn_goniometer_mode_activity_screen);

        //Button Save Patient
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePatientInformation();  //Also finishes activity
            }
        });

        comments.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    savePatient.performClick();
                }

                return false;
            }
        });
    }

    private void savePatientInformation() {
        String first = patientFirst.getText().toString();
        String last = patientLast.getText().toString();
        String comment = comments.getText().toString();

        if(first.trim().isEmpty()||last.trim().isEmpty()) {
            Toast.makeText(this,"Missing First or Last Name",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent patientData = new Intent();
        patientData.putExtra(EXTRA_FIRST_NAME,first);
        patientData.putExtra(EXTRA_LAST_NAME,last);
        patientData.putExtra(EXTRA_COMMENTS,comment);

        int patientId = getIntent().getIntExtra(EXTRA_PATIENT_ID,-1);
        if(patientId!=-1) {
            patientData.putExtra(EXTRA_PATIENT_ID,patientId);
        }
        setResult(Activity.RESULT_OK,patientData);
        finish();
    }




}