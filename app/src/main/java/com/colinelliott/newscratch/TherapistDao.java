package com.colinelliott.newscratch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TherapistDao {

    @Insert
    void insert(Therapist therapist);

    @Update
    void update(Therapist therapist);

    @Query("SELECT * FROM therapist WHERE username LIKE :inUser")
    List<Therapist> findUserWithName(String inUser);
    /*
    @Query("SELECT username FROM therapist")
    String getUsername();

    @Query("SELECT firstName FROM therapist")
    String getFirstName(String username);

    @Query("SELECT lastName FROM therapist")
    String getLastName(String username);
    */
}
