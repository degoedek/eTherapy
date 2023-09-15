package com.colinelliott.newscratch;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

@Database(entities = {Patient.class},version = 4)
public abstract class PatientDatabase extends RoomDatabase {

    private static PatientDatabase instance;

    public abstract PatientDao patientDao();


    public static synchronized PatientDatabase getInstance(Context context) {
        if(instance ==null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PatientDatabase.class,"patient_database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

   /* private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private PatientDao patientDao;

        private PopulateDbAsyncTask(PatientDatabase patientDatabase) {
            patientDao= patientDatabase.patientDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            patientDao.insert(new Patient("Colin","ankle","ankle","stretchband",-1000,-1000,-1000,-1000,-1000,-1000,"cool dude"));
            patientDao.insert(new Patient("Bro","knee","ankle","stretchband",-1000,-1000,-1000,-1000,-1000,-1000,"cool dude"));
            patientDao.insert(new Patient("Yo","shoulder","ankle","stretchband",-1000,-1000,-1000,-1000,-1000,-1000,"cool dude"));
            return null;
        }
    }*/

}
