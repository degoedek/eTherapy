package com.colinelliott.newscratch;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private CreateExerciseNoteDao createExerciseNoteDao;
    private LiveData<List<CreateExerciseNote>> allCreateExerciseNotes;

    public NoteRepository(Application application) {
        CreateExerciseNoteDatabase createExerciseNoteDatabase = CreateExerciseNoteDatabase.getInstance(application);
        createExerciseNoteDao = createExerciseNoteDatabase.createExerciseNoteDao();
        allCreateExerciseNotes = createExerciseNoteDao.getAllCreateExerciseNotes();
    }

    public void insert (CreateExerciseNote createExerciseNote) {
        new InsertNoteAsyncTask(createExerciseNoteDao).execute(createExerciseNote);
    }
    public void update (CreateExerciseNote createExerciseNote) {
        new UpdateNoteAsyncTask(createExerciseNoteDao).execute(createExerciseNote);
    }
    public void delete (CreateExerciseNote createExerciseNote) {
        new DeleteNoteAsyncTask(createExerciseNoteDao).execute(createExerciseNote);
    }
    public void deleteAllNotes () {
        new DeleteAllNotesAsyncTask(createExerciseNoteDao).execute();
    }
    public LiveData <List<CreateExerciseNote>> getAllCreateExerciseNotes() {
        return allCreateExerciseNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<CreateExerciseNote,Void,Void> {
       private CreateExerciseNoteDao createExerciseNoteDao;

       private InsertNoteAsyncTask (CreateExerciseNoteDao createExerciseNoteDao) {
           this.createExerciseNoteDao=createExerciseNoteDao;
       }
        @Override
        protected Void doInBackground(CreateExerciseNote... createExerciseNotes) {
           createExerciseNoteDao.insert(createExerciseNotes[0]);
           return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<CreateExerciseNote,Void,Void> {
        private CreateExerciseNoteDao createExerciseNoteDao;

        private UpdateNoteAsyncTask (CreateExerciseNoteDao createExerciseNoteDao) {
            this.createExerciseNoteDao=createExerciseNoteDao;
        }
        @Override
        protected Void doInBackground(CreateExerciseNote... createExerciseNotes) {
            createExerciseNoteDao.update(createExerciseNotes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<CreateExerciseNote,Void,Void> {
        private CreateExerciseNoteDao createExerciseNoteDao;

        private DeleteNoteAsyncTask (CreateExerciseNoteDao createExerciseNoteDao) {
            this.createExerciseNoteDao=createExerciseNoteDao;
        }
        @Override
        protected Void doInBackground(CreateExerciseNote... createExerciseNotes) {
            createExerciseNoteDao.delete(createExerciseNotes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void,Void> {
        private CreateExerciseNoteDao createExerciseNoteDao;

        private DeleteAllNotesAsyncTask (CreateExerciseNoteDao createExerciseNoteDao) {
            this.createExerciseNoteDao=createExerciseNoteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            createExerciseNoteDao.deleteAllNotes();
            return null;
        }
    }


}