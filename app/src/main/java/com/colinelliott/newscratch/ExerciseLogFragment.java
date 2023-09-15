package com.colinelliott.newscratch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseLogFragment extends Fragment {
    private TextView logExercise, logDate, logReps, logTime, logTargetsSkipped;
    private ImageButton closeLog;
    private String exercise, date, reps, time;
    private String targetsSkipped;
    public ExerciseLogFragment exerciseLogFragment;


    public ExerciseLogFragment() {
        exerciseLogFragment = this;
        // Required empty public constructor
    }

    /*public static ExerciseLogFragment newInstance(String param1, String param2) {
        ExerciseLogFragment fragment = new ExerciseLogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise   = getArguments().getString("logTitle");
            date = getArguments().getString("logDate");
            reps = getArguments().getString("logReps");
            time = getArguments().getString("logTime");
            targetsSkipped = getArguments().getString("logSkippedTargets");


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercise_log, container, false);

        closeLog = v.findViewById(R.id.btn_close_log);
        logExercise = v.findViewById(R.id.log_exercise);
        logDate = v.findViewById(R.id.log_date);
        logReps = v.findViewById(R.id.log_reps);
        logTime = v.findViewById(R.id.log_time);
        logTargetsSkipped = v.findViewById(R.id.log_targets_skipped);

        logExercise.setText(exercise);
        logDate.setText(date);
        logReps.setText(reps);
        logTime.setText(time);
        logTargetsSkipped.setText(targetsSkipped);

        closeLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(exerciseLogFragment).commit();
                ((TherapyActivity)getActivity()).showExerciseCompleteDialog();
                ((TherapyActivity)getActivity()).setButtonsVisible();
            }
        });


        return v;
    }
}