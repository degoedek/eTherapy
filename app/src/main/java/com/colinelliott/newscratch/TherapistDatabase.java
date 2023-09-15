package com.colinelliott.newscratch;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Therapist.class},version = 8, exportSchema = false)
public abstract class TherapistDatabase extends RoomDatabase {
    private static TherapistDatabase INSTANCE;

    public abstract TherapistDao therapistDao();

    //public static TherapistDao therapistDao() {return null;}

    public static synchronized TherapistDatabase getInstance(Context context) {
        if(INSTANCE ==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TherapistDatabase.class,"therapistDatabase")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
