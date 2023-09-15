package com.colinelliott.newscratch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.colinelliott.newscratch.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("DELETE FROM patient_table")
    void deleteAllNotes();

    @Query("SELECT * FROM patient_table WHERE therapistID LIKE :id AND password LIKE :pass")
    List<Patient> findUserWithIdPass(int id, int pass); //used for login to verify an id/pass combo

    @Query("SELECT * FROM patient_table WHERE patientId LIKE :id")
    List<Patient> findUserWithID(int id); //For finding a patient by their Patient's ID(unique)

    @Query("SELECT * FROM patient_table WHERE therapistID LIKE :id")
    List<Patient> getAllPatientsByIDList(int id); //the get all by id function but not live data

    @Query("SELECT * FROM patient_table ORDER BY lastName ASC")
    LiveData<List<Patient>> getAllPatients();

    @Query("SELECT * FROM patient_table WHERE therapistID LIKE :id ORDER BY lastName ASC")
    LiveData<List<Patient>> getAllPatientsByID(int id); //gets every patient of a therapist

    @Query("UPDATE patient_table SET patientData = :patientData WHERE patientId = :id")
    void update(String patientData, int id);

    @Query("UPDATE patient_table SET setsProgress = :setsProgress WHERE patientId = :id")
    void updateSets(String setsProgress, int id);

}
