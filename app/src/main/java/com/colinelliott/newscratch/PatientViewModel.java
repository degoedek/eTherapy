package com.colinelliott.newscratch;

import static android.content.Context.MODE_PRIVATE;
import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.colinelliott.newscratch.Patient;
import com.colinelliott.newscratch.PatientRepository;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class PatientViewModel extends AndroidViewModel {
    private PatientRepository repository;
    private LiveData<List<Patient>> allPatients;
    private LiveData<List<Patient>> allPatientsByID;

    public PatientViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new PatientRepository(application);
        allPatients = repository.getAllPatients();

        //TODO: Implement Query by ID
        TherapistDatabase theraDb = TherapistDatabase.getInstance(application);
        SharedPreferences preferences = application.getSharedPreferences("checkbox",MODE_PRIVATE);
        String username = preferences.getString("username","");
        int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
        allPatientsByID = repository.getAllPatientsByID(id);
    }

    public void insert(Patient patient) {
        repository.insert(patient);
    }

    public void update (Patient patient) {
        repository.update(patient);
    }

    public void delete (Patient patient) {
        repository.delete(patient);
    }

    public void deleteAllNotes (Patient patient) {
        repository.deleteAllNotes();
    }

    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }

    public LiveData<List<Patient>> getAllPatientsByID(int id){return allPatientsByID;}
}
