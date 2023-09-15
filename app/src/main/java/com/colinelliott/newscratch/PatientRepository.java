package com.colinelliott.newscratch;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.colinelliott.newscratch.Patient;
import com.colinelliott.newscratch.PatientDao;
import com.colinelliott.newscratch.PatientDatabase;

import java.util.List;

public class PatientRepository {
    private PatientDao patientDao;
    private LiveData<List<Patient>> allPatients;
    private LiveData<List<Patient>> allPatientsByID;

    public PatientRepository(Application application) {
        PatientDatabase patientDatabase = PatientDatabase.getInstance(application);
        patientDao = patientDatabase.patientDao();
        allPatients = patientDao.getAllPatients();

        //TODO: Implement Query by ID
        TherapistDatabase theraDb = TherapistDatabase.getInstance(application);
        SharedPreferences preferences = application.getSharedPreferences("checkbox",MODE_PRIVATE);
        String username = preferences.getString("username","");
        int id = theraDb.therapistDao().findUserWithName(username).get(0).getTheraId();
        allPatientsByID = patientDao.getAllPatientsByID(id);
    }

    public void insert (Patient patient) {
        new InsertPatientAsyncTask(patientDao).execute(patient);
    }
    public void update (Patient patient) {
        new UpdateNoteAsyncTask(patientDao).execute(patient);
    }
    public void delete (Patient patient) {
        new DeleteNoteAsyncTask(patientDao).execute(patient);
    }
    public void deleteAllNotes () {
        new DeleteAllNotesAsyncTask(patientDao).execute();
    }
    public LiveData <List<Patient>> getAllPatients() {
        return allPatients;
    }

    //TODO Further implement the ID query
    public LiveData<List<Patient>> getAllPatientsByID(int id){return allPatientsByID;}

    private static class InsertPatientAsyncTask extends AsyncTask<Patient,Void,Void> {
        private PatientDao patientDao;

        private InsertPatientAsyncTask (PatientDao patientDao) {
            this.patientDao=patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.insert(patients[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Patient,Void,Void> {
        private PatientDao patientDao;

        private UpdateNoteAsyncTask (PatientDao patientDao) {
            this.patientDao=patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.update(patients[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Patient,Void,Void> {
        private PatientDao patientDao;

        private DeleteNoteAsyncTask (PatientDao patientDao) {
            this.patientDao=patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.delete(patients[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void,Void> {
        private PatientDao patientDao;

        private DeleteAllNotesAsyncTask (PatientDao patientDao) {
            this.patientDao=patientDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            patientDao.deleteAllNotes();
            return null;
        }
    }
}
