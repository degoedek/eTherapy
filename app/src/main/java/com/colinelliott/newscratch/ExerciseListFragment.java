package com.colinelliott.newscratch;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExerciseListFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private CreateExerciseViewModel createExerciseViewModel;
    //private ArrayList<ExerciseListItem> arrayList;
    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;
    //private SharedViewModel viewModel;



        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_exercise_list, container, false);
            //Initializing
            FloatingActionButton addButton = v.findViewById(R.id.btn_create_exercise);
            listView = v.findViewById(R.id.exercise_list_view);

            //Create Exercise Button
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),CreateExerciseActivity.class);
                    startActivityForResult(intent,ADD_NOTE_REQUEST);
                }
            });
            buildRecyclerView();


            return v;


    }

    /*public void removeItem(int position) {
        createExerciseNoteArrayList.remove(position);
        adapter.notifyItemRemoved(position);
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void buildRecyclerView() {
        listView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());

        final ExerciseListAdapter adapter = new ExerciseListAdapter();
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        createExerciseViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CreateExerciseViewModel.class);

        createExerciseViewModel.getAllCreateExerciseNotes().observe(getActivity(), new Observer<List<CreateExerciseNote>>() {
            @Override
            public void onChanged(List<CreateExerciseNote> createExerciseNotes) {
                adapter.setCreateExerciseNotes(createExerciseNotes);
            }
        });
        //Swipe to Delete Functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Get a list of all created exercises
                TherapistDatabase theraDb = TherapistDatabase.getInstance(getContext());
                PatientDatabase patientDb = PatientDatabase.getInstance((getContext()));
                CreateExerciseNoteDatabase exerciseDb = CreateExerciseNoteDatabase.getInstance(getContext());
                SharedPreferences preferences = getContext().getSharedPreferences("checkbox",MODE_PRIVATE);
                String username = preferences.getString("username","");
                int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
                int preDeleteCount = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(id).size();
                List<CreateExerciseNote> exercises = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(id);

                createExerciseViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(),"Exercise Deleted",Toast.LENGTH_SHORT).show();
                //TODO: Check all patients the therapist has, then check if this exercise is assigned
                //TODO: If so, remove its id from the data array and its sets progress data
                //Then after deletion, check which exercise is now missing
                //Account for only being 1 exercise(in which case, reset all assignments)
                /*
                boolean noExeLeftFlag = false;
                String deletedExerciseId;
                List<CreateExerciseNote> exercisesPostDelete = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(id);
                if(preDeleteCount == 1){
                    noExeLeftFlag = true;
                }
                else{
                    
                }
                */
                //Fetch the patients(same method as patientListFrag)
                if(patientDb.patientDao().getAllPatientsByIDList(id).size()>0) {//Indicates there are patients created
                    Toast.makeText(getActivity(),"Remember to reset patient assignments for changes made",Toast.LENGTH_SHORT).show();
                    //Check iterate through the list of patients and then iterate through each list of pData
                    for(int i = 0; i < patientDb.patientDao().getAllPatientsByIDList(id).size(); i++){
                        Patient patient = patientDb.patientDao().getAllPatientsByIDList(id).get(i);
                        //Check if the patient has any assignments
                        //if(noExeLeftFlag) {
                        patientDb.patientDao().updateSets("", patient.getPatientId());
                        patientDb.patientDao().update("", patient.getPatientId());
                        //}
                        /*
                        else if(patient.getPatientData().split(",").length>1){
                            //If the flag is true, delete all assignments from the patient
                            for(int j = 3; j<patient.getPatientData().split(",").length; j++){
                                //Check if the patient was assigned the deleted exercise
                                if(patient.getPatientData().split(",")[j].equals())
                            }
                        }
                        */
                    }
                    //If pData contains the ID(after the first 3 elements), log the index, erase the item
                    //Using the logged index, erase that index from setsProgress
                    //Make sure to finish by calling the Dao update statements

                }
            }
        }).attachToRecyclerView(listView);
        //Edit Exercise Button
        adapter.setOnItemClickListener(new ExerciseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CreateExerciseNote createExerciseNote) {
                Intent intent = new Intent(getActivity(),CreateExerciseActivity.class);
                intent.putExtra(CreateExerciseActivity.EXTRA_EXERCISE_ID,createExerciseNote.getId());
                intent.putExtra(CreateExerciseActivity.EXTRA_TITLE,createExerciseNote.getExerciseTitle());
                intent.putExtra(CreateExerciseActivity.EXTRA_DESCRIPTION,createExerciseNote.getDescription());
                intent.putExtra(CreateExerciseActivity.EXTRA_POSITION,createExerciseNote.getLocData());

                String[] motionArray = new String[3];
                motionArray = createExerciseNote.getMotion().split("\n",3);
                intent.putExtra(CreateExerciseActivity.EXTRA_FLEX,motionArray[0]); //.substring(0,17)
                intent.putExtra(CreateExerciseActivity.EXTRA_ABD,motionArray[1]);  //.substring(19,20)
                intent.putExtra(CreateExerciseActivity.EXTRA_ROT,motionArray[2]);
                intent.putExtra(CreateExerciseActivity.EXTRA_JOINT,createExerciseNote.getJoint());
                intent.putExtra(CreateExerciseActivity.EXTRA_EXERCISE_TIME,createExerciseNote.getTime());
                intent.putExtra(CreateExerciseActivity.EXTRA_MINUTES_INT,createExerciseNote.getTime()/60);
                intent.putExtra(CreateExerciseActivity.EXTRA_SECONDS_INT,createExerciseNote.getTime()%60);
                intent.putExtra(CreateExerciseActivity.EXTRA_EXERCISE_REPS,createExerciseNote.getReps());
                intent.putExtra(CreateExerciseActivity.EXTRA_JOINT_LOCATION,createExerciseNote.getJoint());
                String[] pluginTargetArray = new String[36];
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i]= createExerciseNote.getTarget1().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+3]= createExerciseNote.getTarget2().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+6]= createExerciseNote.getTarget3().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+9]= createExerciseNote.getTarget4().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+12]= createExerciseNote.getTarget5().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+15]= createExerciseNote.getTarget6().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+18]= createExerciseNote.getTarget7().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+21]= createExerciseNote.getTarget8().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+24]= createExerciseNote.getTarget9().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+27]= createExerciseNote.getTarget10().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+30]= createExerciseNote.getTarget11().substring(3*i,3*(i+1));
                for(int i = 0;i<3;i++)
                    pluginTargetArray[i+33]= createExerciseNote.getTarget12().substring(3*i,3*(i+1));
                intent.putExtra(CreateExerciseActivity.EXTRA_TARGET_ARRAY,pluginTargetArray);


                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            String exerciseTitle = data.getStringExtra(CreateExerciseActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CreateExerciseActivity.EXTRA_DESCRIPTION);
            String locData = data.getStringExtra(CreateExerciseActivity.EXTRA_POSITION);
            Boolean isResponseTime = data.getBooleanExtra(CreateExerciseActivity.EXTRA_RT, false);
            String flex = data.getStringExtra(CreateExerciseActivity.EXTRA_FLEX);
            String abd = data.getStringExtra(CreateExerciseActivity.EXTRA_ABD);
            String rot = data.getStringExtra(CreateExerciseActivity.EXTRA_ROT);
            int exerciseTime = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_TIME,-1);
            int minutesInt = data.getIntExtra(CreateExerciseActivity.EXTRA_MINUTES_INT,-1);
            int secondsInt = data.getIntExtra(CreateExerciseActivity.EXTRA_SECONDS_INT,-1);
            int exerciseReps = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_REPS,-1);
            int xyz = data.getIntExtra(CreateExerciseActivity.EXTRA_XYZ,-1);
            String target1 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_1);String target2 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_2);String target3 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_3);String target4 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_4);String target5 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_5);String target6 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_6);String target7 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_7);String target8 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_8);String target9 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_9);String target10 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_10);String target11 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_11);String target12 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_12);
            String joint = data.getStringExtra(CreateExerciseActivity.EXTRA_JOINT);
            String jointLocation = data.getStringExtra(CreateExerciseActivity.EXTRA_JOINT_LOCATION);
            String[] pluginTargetArray = data.getStringArrayExtra(CreateExerciseActivity.EXTRA_TARGET_ARRAY);

            String motion = flex+"\n"+abd + "\n"+ rot;
            /*String[] motionArray = motion.split("\n");
            if (abd == "" && rot == "") {
                motion = motionArray[0];
            }
            else if (flex =="" && abd == ""){
                motion = motionArray[2];

            }
            else if( flex=="" && rot=="") {
                motion = motionArray[1];
            }
            else if (abd == "") {
                motion = motionArray[0] + "\n" + motionArray[2];
            }
            else if (rot == "") {
                motion = motionArray[0] + "\n" + motionArray[1] ;
            }
            else
                motion = motionArray[1]+"\n" + motionArray[2];*/

            CreateExerciseNote createExerciseNote = new CreateExerciseNote(exerciseTitle, joint, motion, isResponseTime, xyz,-1000,-1000,-1000,-1000,-1000,-1000,target1,target2,target3,target4,target5,target6,target7,target8,target9,target10,target11,target12, exerciseTime, exerciseReps, description);
            createExerciseNote.setLocData(locData);
            createExerciseViewModel.insert(createExerciseNote);

            SharedPreferences preferences = this.getContext().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
            int theraID = preferences.getInt("User ID", -1);
            createExerciseNote.setTheraID(theraID);
            Toast.makeText(getActivity(),Integer.toString(theraID),Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity(), "Exercise Saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Exercise Not Updated!", Toast.LENGTH_SHORT).show();
                return;
            }
            String exerciseTitle = data.getStringExtra(CreateExerciseActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CreateExerciseActivity.EXTRA_DESCRIPTION);
            String locData = data.getStringExtra(CreateExerciseActivity.EXTRA_POSITION);
            Boolean isResponseTime = data.getBooleanExtra(CreateExerciseActivity.EXTRA_RT, false);
            String flex = data.getStringExtra(CreateExerciseActivity.EXTRA_FLEX);
            String abd = data.getStringExtra(CreateExerciseActivity.EXTRA_ABD);
            String rot = data.getStringExtra(CreateExerciseActivity.EXTRA_ROT);
            int exerciseTime = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_TIME,-1);
            int minutesInt = data.getIntExtra(CreateExerciseActivity.EXTRA_MINUTES_INT,-1);
            int secondsInt = data.getIntExtra(CreateExerciseActivity.EXTRA_SECONDS_INT,-1);
            int exerciseReps = data.getIntExtra(CreateExerciseActivity.EXTRA_EXERCISE_REPS,-1);
            int xyz = data.getIntExtra(CreateExerciseActivity.EXTRA_XYZ,-1);
            String target1 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_1);String target2 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_2);String target3 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_3);String target4 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_4);String target5 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_5);String target6 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_6);String target7 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_7);String target8 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_8);String target9 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_9);String target10 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_10);String target11 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_11);String target12 = data.getStringExtra(CreateExerciseActivity.EXTRA_TARGET_12);
            String joint = data.getStringExtra(CreateExerciseActivity.EXTRA_JOINT);
            String jointLocation = data.getStringExtra(CreateExerciseActivity.EXTRA_JOINT_LOCATION);
            String motion = flex+"\n"+abd + "\n"+ rot;
            String[] motionArray = motion.split("\n");
            if (abd == "")
                motion = motionArray[0] + "\n" + motionArray[2];
            if (rot == "")
                motion = motionArray[0] + "\n" + motionArray[1];
            if (abd == "" && rot == "")
                motion = motionArray[0];

            CreateExerciseNote createExerciseNote = new CreateExerciseNote(exerciseTitle, joint, motion, isResponseTime, xyz,-1000,-1000,-1000,-1000,-1000,-1000,target1,target2,target3,target4,target5,target6,target7,target8,target9,target10,target11,target12, exerciseTime, exerciseReps, description);
            createExerciseNote.setId(id);
            createExerciseNote.setLocData(locData);
            createExerciseViewModel.update(createExerciseNote);

            Toast.makeText(getActivity(),"Exercise Updated",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Exercise Not Saved",Toast.LENGTH_SHORT).show();
        }
    }
}