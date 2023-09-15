package com.colinelliott.newscratch;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ExercisesFragment extends Fragment {
    Button assignExercise;
    Button createExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercises, container, false);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.exercise_list_container,new ExerciseListFragment(),"exerciseListTag").commit();
        //Initialize
        assignExercise = v.findViewById(R.id.btn_assign_exercise);
        createExercise = v.findViewById(R.id.btn_create_exercise);
        createExercise.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.exercise_list_container,new ExerciseListFragment()).commit();

        //Assign Exercises Button
        assignExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"New Assignments will override the patient's current assignments and progress", Toast.LENGTH_SHORT).show();
                Intent viewExercises = new Intent(getActivity(),AssignExerciseActivity.class);
                startActivity(viewExercises);
            }
        });

        //Create Exercises Button
        createExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = getActivity().getIntent();
                Intent createExercises = new Intent(getActivity(), CreateExerciseActivity.class);
                startActivityForResult(createExercises, 1);
            }
        }
        );

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        Receives back the info given in the Exercise creation activity and inserts an exercise entity with it
         */
        try {
            data.getStringExtra(CreateExerciseActivity.EXTRA_TITLE);
        }
        catch (NullPointerException e){
            Toast.makeText(getActivity(), "Exercise Not Saved", Toast.LENGTH_SHORT).show();
            return;
        }
        String exerciseTitle = data.getStringExtra(CreateExerciseActivity.EXTRA_TITLE);
        String description = data.getStringExtra(CreateExerciseActivity.EXTRA_DESCRIPTION);
        String locData = data.getStringExtra((CreateExerciseActivity.EXTRA_POSITION));
        Boolean isResponseTime = data.getBooleanExtra(CreateExerciseActivity.EXTRA_RT, false);
        String flex = data.getStringExtra(CreateExerciseActivity.EXTRA_FLEX);
        String abd = data.getStringExtra(CreateExerciseActivity.EXTRA_ABD);
        String rot = data.getStringExtra(CreateExerciseActivity.EXTRA_ROT);
        int exerciseTime = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_TIME,-1);
        int exerciseReps = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_REPS,-1);
        int xyz = data.getIntExtra(CreateExerciseActivity.EXTRA_XYZ,-1);
        String target1 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_1);String target2 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_2);String target3 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_3);String target4 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_4);String target5 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_5);String target6 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_6);String target7 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_7);String target8 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_8);String target9 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_9);String target10 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_10);String target11 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_11);String target12 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_12);
        String joint = data.getStringExtra(CreateExerciseActivity.EXTRA_JOINT);
        String motion = flex+"\n"+abd + "\n"+ rot;

        CreateExerciseNote createExerciseNote = new CreateExerciseNote(exerciseTitle, joint, motion, isResponseTime, xyz,-1000,-1000,-1000,-1000,-1000,-1000,target1,target2,target3,target4,target5,target6,target7,target8,target9,target10,target11,target12, exerciseTime, exerciseReps, description);
        createExerciseNote.setLocData(locData);
        CreateExerciseViewModel createExerciseViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CreateExerciseViewModel.class);
        createExerciseViewModel.insert(createExerciseNote);

        Toast.makeText(getActivity(), "Exercise Saved", Toast.LENGTH_SHORT).show();

    }
}