package com.colinelliott.newscratch;

import static android.content.Context.MODE_PRIVATE;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.colinelliott.newscratch.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class PatientListFragment extends Fragment {
    private static final int ADD_PATIENT_REQUEST = 3;
    private static final int EDIT_PATIENT_REQUEST = 4;


    private PatientViewModel patientViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_list, container, false);
        //Initialize
        FloatingActionButton createPatient = v.findViewById(R.id.btn_create_patient);
        recyclerView = v.findViewById(R.id.patient_recycler_view);

        createPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreatePatientActivity.class);
                startActivityForResult(intent,ADD_PATIENT_REQUEST);
            }
        });
        buildRecyclerView();



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent gata) {
        super.onActivityResult(requestCode, resultCode, gata);

        //TODO: import the creating therapist's ID
        TherapistDatabase theraDb = TherapistDatabase.getInstance(this.getContext());
        SharedPreferences preferences = this.getContext().getSharedPreferences("checkbox",MODE_PRIVATE);
        String username = preferences.getString("username","");
        int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
        //int id = 1;

        if (requestCode == ADD_PATIENT_REQUEST && resultCode == Activity.RESULT_OK) {
            String firstName = gata.getStringExtra(CreatePatientActivity.EXTRA_FIRST_NAME);
            String lastName = gata.getStringExtra(CreatePatientActivity.EXTRA_LAST_NAME);
            String comments = gata.getStringExtra(CreatePatientActivity.EXTRA_COMMENTS);

            //TODO make this constructor accept a therapist ID
            Patient patient = new Patient(firstName,lastName, id, "hi","hi",-1000,-1000,-1000,-1000,-1000,-1000,comments);
            patientViewModel.insert(patient);

            Toast.makeText(getActivity(), "Patient Information Saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_PATIENT_REQUEST && resultCode == Activity.RESULT_OK) {
            int patientId = gata.getIntExtra(ViewPatientActivity.EXTRA_PATIENT_ID, -1);
            if (patientId == -1) {
                Toast.makeText(getActivity(), "Patient Data Not Updated!", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] nameSplit = gata.getStringExtra(ViewPatientActivity.EXTRA_PATIENT_NAME).split(" ");
            String firstName = nameSplit[0];
            String lastName = nameSplit[1];
            String comments = gata.getStringExtra(ViewPatientActivity.EXTRA_PATIENT_COMMENTS);

            //TODO make this constructor accept a therapist ID
            Patient patient = new Patient(firstName,lastName, id, "hi","hi",-1000,-1000,-1000,-1000,-1000,-1000,comments);
            patient.setPatientId(patientId);
            patientViewModel.update(patient);

            Toast.makeText(getActivity(), "Patient Data Saved", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getActivity(),"Patient Data Not Saved",Toast.LENGTH_SHORT).show();
        }
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        final PatientAdapter adapter = new PatientAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        patientViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(PatientViewModel.class);

        //TODO Implement Query by ID
        TherapistDatabase theraDb = TherapistDatabase.getInstance(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("checkbox",MODE_PRIVATE);
        String username = preferences.getString("username","");
        int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
        patientViewModel.getAllPatientsByID(id).observe(getActivity(), new Observer<List<Patient>>() {
        //patientViewModel.getAllPatients().observe(getActivity(), new Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                adapter.setPatients(patients);
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
                // Deletion confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this patient?").setTitle("Deletion Confirmation");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        patientViewModel.delete(adapter.getPatientAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(getActivity(),"Patient Data Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);
        //Edit Patient By Clicking Item
        adapter.setOnItemClickListener(new PatientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                Intent intent = new Intent(getActivity(),ViewPatientActivity.class);
                intent.putExtra(CreatePatientActivity.EXTRA_PATIENT_ID,patient.getPatientId());
                intent.putExtra(CreatePatientActivity.EXTRA_FIRST_NAME,patient.getFirstName());
                intent.putExtra(CreatePatientActivity.EXTRA_LAST_NAME,patient.getLastName());
                intent.putExtra(CreatePatientActivity.EXTRA_COMMENTS,patient.getComments());

                startActivityForResult(intent,EDIT_PATIENT_REQUEST);
            }
        });

    }
}