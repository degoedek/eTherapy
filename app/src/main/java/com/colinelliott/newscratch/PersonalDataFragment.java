package com.colinelliott.newscratch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


public class PersonalDataFragment extends Fragment {
    //TODO: Get patient's Assigned Exercises and sets Data
    // Use this to show the names of assigned exercises along with the amount of sets done and required
    // If progress is at 100%, show complete instead of numbers

    TextView dataTitle, progressText;
    SharedPreferences preferences;
    Patient patient;
    String setsData, exercises;
    PatientDatabase patientDb;
    CreateExerciseNoteDatabase exerciseDb;

    public PersonalDataFragment() {
    }

    public static PersonalDataFragment newInstance() {
        return new PersonalDataFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientDb = PatientDatabase.getInstance(this.getActivity());
        exerciseDb = CreateExerciseNoteDatabase.getInstance((this.getActivity()));
        preferences = this.getContext().getSharedPreferences("checkbox",Context.MODE_PRIVATE);
        int userID = preferences.getInt("User ID",-1);
        patient = patientDb.patientDao().findUserWithID(userID).get(0);
        setsData = patient.getSetsProgress(); //Start at 3 here as well
        exercises = patient.getPatientData(); //Exercises start at index 3
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_personal_data, container, false);
        dataTitle = v.findViewById(R.id.title_data);
        progressText = v.findViewById(R.id.progress_text);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        dataTitle.setText(preferences.getString("firstName","")+" "+preferences.getString("lastName","")+"'s"+" Data");
        if(!(exercises.equals(""))) {
            String patientProgressText = "";
            for (int i = 0; i < setsData.split(",").length-3; i++) { //should not be any out of bounds issues for the i+3 as that array should be 3 index longer than sets
                String exerciseName = exerciseDb.createExerciseNoteDao().getExerciseById(Integer.parseInt(exercises.split(",")[i+3])).get(0).getExerciseTitle();
                if(exercises.split(",")[1].equals(setsData.split(",")[i+3])){ //is the exercise complete
                    patientProgressText = patientProgressText + exerciseName + " is complete!\n";
                }
                else{
                    patientProgressText = patientProgressText + exerciseName + " current progress: " + setsData.split(",")[i+3] +"/"+ exercises.split(",")[1] + "\n";
                }
            }
            progressText.setText(patientProgressText);
        }
        return v;
    }
}