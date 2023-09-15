package com.colinelliott.newscratch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignExerciseActivity extends AppCompatActivity{
    Spinner spinner;
    Spinner setsSpinner;
    String patientName;
    int patientIndex;
    int setsIndex;
    EditText setsText;
    String[] setsOptions = {"Daily","Weekly","Monthly"};
    EditText repsText;
    Button assignButton;
    TherapistDatabase theraDb;
    PatientDatabase patientDb;
    CreateExerciseNoteDatabase exerciseDb;
    TextView multiSelect;
    boolean[] selectedExercise;
    ArrayList<Integer> exerciseArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_exercise);
        spinner = findViewById(R.id.search_patient);
        setsSpinner = findViewById(R.id.setsSpinner);
        setsText = findViewById(R.id.setsText);
        repsText = findViewById(R.id.repsText);
        assignButton = findViewById(R.id.btn_add_exercise);

        //Getting the list of patients
        theraDb = TherapistDatabase.getInstance(this);
        SharedPreferences preferences = this.getSharedPreferences("checkbox", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
        patientDb = PatientDatabase.getInstance(this);
        List<Patient> patientsList = patientDb.patientDao().getAllPatientsByIDList(id);
        //Getting each patient's name
        String[] patientNames = new String[patientsList.size()];
        for(int i = 0; i <patientsList.size();i++){
            patientNames[i] = patientsList.get(i).getFirstName() + "," + patientsList.get(i).getLastName();
        }
        //Finalizing the spinner for patient name
        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, patientNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patientIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Generating the spinner for sets
        ArrayAdapter<CharSequence> setsAdapter =  new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, setsOptions);
        setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setsSpinner.setAdapter(setsAdapter);
        setsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setsIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Getting the list of exercises
        exerciseDb = CreateExerciseNoteDatabase.getInstance(this);
        int theraID = preferences.getInt("User ID", -1);
        //        List<CreateExerciseNote> exercisesList = exerciseDb.createExerciseNoteDao().getAllCreateExerciseNotesList();
        List<CreateExerciseNote> exercisesList = exerciseDb.createExerciseNoteDao().getExerciseByTherapistList(theraID);
        //Getting an array of exercise names
        String[] exerciseNames = new String[exercisesList.size()];
        for(int i = 0; i <exercisesList.size();i++){
            exerciseNames[i] = exercisesList.get(i).getExerciseTitle();
        }
        selectedExercise = new boolean[exercisesList.size()];
        //Generating the MultiSelect
        multiSelect = findViewById(R.id.selectExercisesText);
        multiSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssignExerciseActivity.this);
                builder.setTitle("Select Exercises");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(exerciseNames, selectedExercise, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            exerciseArrayList.add(i);
                            // Sort array list
                            Collections.sort(exerciseArrayList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            exerciseArrayList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < exerciseArrayList.size(); j++) {
                            // concat array value
                            stringBuilder.append(exerciseNames[exerciseArrayList.get(j)]);
                            // check condition
                            if (j != exerciseArrayList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        multiSelect.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedExercise.length; j++) {
                            // remove all selection
                            selectedExercise[j] = false;
                            // clear language list
                            exerciseArrayList.clear();
                            // clear text view value
                            multiSelect.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();

            }
        });
        //Finalizing the assignment
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"ID: "+ Integer.toString(patientsList.get(patientIndex).getPatientId()),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),setsText.getText() + " "+ setsOptions[setsIndex],Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),repsText.getText() + " Reps",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),multiSelect.getText(),Toast.LENGTH_SHORT).show();

                StringBuilder patientData = new StringBuilder(setsSpinner.getSelectedItem().toString());
                patientData.append(",").append(setsText.getText().toString()).append(",").append(repsText.getText().toString());
                //Multiselect.getText should only return checked items
                String[] exercises = multiSelect.getText().toString().split(",");
                int i;
                for(i = 0; i < exercises.length; i++) {
                    //For each exercise named in the list, retrieve the ID and concat it to patientData
                    //To do this, grab the name, find its index in exerciseNames, then fetch the id of that index in exercisesList
                    int index = -1;
                    for(int j = 0; j < exerciseNames.length; j++){
                        if(exercises[i].trim().equals(exerciseNames[j].trim())) {
                            index = j;
                            //Toast.makeText(getApplicationContext(), exercises[i] + Integer.toString(index), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    patientData.append(",").append(exercisesList.get(index).getId());
                }
                StringBuilder setsData = new StringBuilder("");
                for(int k = 0; k < patientData.toString().split(",").length; k++){
                    setsData.append("0,"); //Initializes each exercise to have 0 sets complete
                }

                //Grab the patient object from the list via the saved index value
                Patient selectedPatient = patientsList.get(patientIndex);
                //Overwrite the patient's data
                patientDb.patientDao().update(patientData.toString(), selectedPatient.getPatientId());
                patientDb.patientDao().updateSets(setsData.toString(), selectedPatient.getPatientId());

                //Display success message & return to the homepage
                Toast.makeText(getApplicationContext(), patientData, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Exercises Assigned", Toast.LENGTH_SHORT).show();
                changeActivity();
            }
        });
    }

    private void changeActivity(){
        Intent intent = new Intent(this, TherapistActivity.class);
        startActivity(intent);
    }

}