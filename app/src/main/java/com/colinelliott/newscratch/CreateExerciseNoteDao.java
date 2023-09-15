package com.colinelliott.newscratch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CreateExerciseNoteDao {

    @Insert
    void insert(CreateExerciseNote note);

    @Update
    void update(CreateExerciseNote note);

    @Delete
    void delete(CreateExerciseNote note);

    @Query("DELETE FROM create_exercise_note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM create_exercise_note_table ORDER BY exerciseTitle ASC")
    LiveData<List<CreateExerciseNote>> getAllCreateExerciseNotes();

    @Query("SELECT * FROM create_exercise_note_table ORDER BY exerciseTitle ASC")
    List<CreateExerciseNote> getAllCreateExerciseNotesList();

    @Query("SELECT * FROM create_exercise_note_table WHERE id LIKE :id")
    List<CreateExerciseNote> getExerciseById(int id);

    @Query("SELECT * FROM create_exercise_note_table WHERE theraID LIKE :id")
    List<CreateExerciseNote> getExerciseByTherapistList(int id);


    @Query("UPDATE CREATE_EXERCISE_NOTE_TABLE SET locData = :locationData WHERE id = :id")
    void update(String locationData, int id);

}


