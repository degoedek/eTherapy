package com.colinelliott.newscratch;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Entity(tableName = "patient_table")
public class Patient {
    @PrimaryKey(autoGenerate = true)
    private int patientId;
    public String patientData, setsProgress; //Holds assigned exercises from the therapist
    private String firstName, lastName, comments;
    private int therapistID;
    private int password;
    private String jointArray;
    private String exerciseArray;
    private int flexROM,extendROM,abdROM,addROM,intRotROM,extRotROM;;

    public Patient(String firstName, String lastName, int therapistID, String jointArray, String exerciseArray,int flexROM,int extendROM,int abdROM,int addROM, int intRotROM, int extRotROM, String comments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.therapistID = therapistID;
        this.patientData = "";
        Random generator = new Random(patientId + (int) lastName.charAt(0) + (int) firstName.charAt(0));
        this.password = 10000000 + generator.nextInt(90000000); // generates a random 8-digit password

       //this.assignmentMap = new HashMap<String,ArrayList<Object>>();
        this.jointArray = jointArray;
        this.exerciseArray = exerciseArray;
        this.flexROM=flexROM;
        this.extendROM=extendROM;
        this.abdROM=abdROM;
        this.addROM=addROM;
        this.intRotROM=intRotROM;
        this.extRotROM=extRotROM;
        this.comments = comments;

    }
    //Setter Methods
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setPassword(int password) {this.password = password;}

    public void setPatientData(String patientData) {this.patientData = patientData;}

    //Getter Methods
    public int getPatientId() {
        return patientId;
    }

    public String getPatientData(){return patientData;}

    public String getSetsProgress(){return setsProgress;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPassword(){
        return password;
    }

    public int getTherapistID(){return therapistID;}

    public String getJointArray() {
        return jointArray;
    }

    public String getExerciseArray() {
        return exerciseArray;
    }

    public int getFlexROM() {
        return flexROM;
    }

    public int getExtendROM() {
        return extendROM;
    }

    public int getAbdROM() {
        return abdROM;
    }

    public int getAddROM() {
        return addROM;
    }

    public int getIntRotROM() {
        return intRotROM;
    }

    public int getExtRotROM() {
        return extRotROM;
    }

    public String getComments() {
        return comments;
    }
}
