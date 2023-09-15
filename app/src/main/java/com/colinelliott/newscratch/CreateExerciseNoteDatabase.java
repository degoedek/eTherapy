package com.colinelliott.newscratch;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

@Database(entities = {CreateExerciseNote.class},version = 11)
public abstract class CreateExerciseNoteDatabase extends RoomDatabase {

    private static CreateExerciseNoteDatabase instance;

    public abstract CreateExerciseNoteDao createExerciseNoteDao();

    public static synchronized CreateExerciseNoteDatabase getInstance(Context context) {
        if(instance ==null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CreateExerciseNoteDatabase.class,"create_exercise_note_database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private CreateExerciseNoteDao createExerciseNoteDao;

        private PopulateDbAsyncTask(CreateExerciseNoteDatabase createExerciseNoteDatabase) {
            createExerciseNoteDao= createExerciseNoteDatabase.createExerciseNoteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            createExerciseNoteDao.insert(new CreateExerciseNote("Exercise1","ankle","Flexion/Extension", false,000,60,10,20,70,70,80,"100","100","100","100","100","100","100","100","100","100","100","100",120,12,"hi"));
            createExerciseNoteDao.insert(new CreateExerciseNote("Exercise2","knee","Abduction/Adduction",false,010,60,10,20,70,70,80,"100","100","100","100","100","100","100","100","100","100","100","100",120,12,"hi"));
            createExerciseNoteDao.insert(new CreateExerciseNote("Exercise3","shoulder","Internal/External Rotation", false,001,60,10,20,70,70,80,"100","100","100","100","100","100","100","100","100","100","100","100",120,12,"hi"));
            return null;
        }
    }

}
