package com.colinelliott.newscratch;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "create_exercise_note_table")
public class CreateExerciseNote {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exerciseTitle,joint,description,motion;
    private String locData;
    private Boolean isResponseTime;
    private int xyz, theraID;
    private int flexROM,extendROM,abdROM,addROM,intRotROM,extRotROM;
    private String target1,target2,target3,target4,target5,target6,target7,target8,target9,target10,target11,target12;
    private int time, reps;       //Time in seconds

    public CreateExerciseNote(String exerciseTitle, String joint, String motion, Boolean isResponseTime, int xyz, int flexROM,int extendROM,int abdROM,int addROM, int intRotROM, int extRotROM, String target1,String target2,String target3,String target4,String target5,String target6,String target7,String target8,String target9,String target10,String target11,String target12, int time, int reps, String description) {
        //Location Data is not stored when created with this constructor, must be inserted.
        //If using this constructor, follow it with a setLocData.
        this.exerciseTitle = exerciseTitle;
        this.joint = joint;
        this.motion=motion;
        this.isResponseTime = isResponseTime;
        this.xyz = xyz;
        this.flexROM=flexROM;
        this.extendROM=extendROM;
        this.abdROM=abdROM;
        this.addROM=addROM;
        this.intRotROM=intRotROM;
        this.extRotROM=extRotROM;
        this.target1=target1;
        this.target2=target2;
        this.target3=target3;
        this.target4=target4;
        this.target5=target5;
        this.target6=target6;
        this.target7=target7;
        this.target8=target8;
        this.target9=target9;
        this.target10=target10;
        this.target11=target11;
        this.target12=target12;
        this.time= time;
        this.reps = reps;
        this.description=description;
        this.locData = "";

    }
    //Setter Methods
    public void setId(int id) {
        this.id = id;
    }

    public void setTheraID(int theraID){this.theraID = theraID;}

    public void setLocData(String locData){this.locData = locData;}

    //Getter Methods
    public String getMotion() { return motion; }

    public int getTime() { return time; }

    public int getReps() { return reps; }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public Boolean getIsResponseTime(){return isResponseTime; }

    public String getJoint() {
        return joint;
    }

    public int getXyz() {
        return xyz;
    }

    public int getTheraID(){return theraID;}

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

    public String getTarget1() {
        return target1;
    }

    public String getTarget2() {
        return target2;
    }

    public String getTarget3() {
        return target3;
    }

    public String getTarget4() {
        return target4;
    }

    public String getTarget5() {
        return target5;
    }

    public String getTarget6() {
        return target6;
    }

    public String getTarget7() {
        return target7;
    }

    public String getTarget8() {
        return target8;
    }

    public String getTarget9() {
        return target9;
    }

    public String getTarget10() {
        return target10;
    }

    public String getTarget11() {
        return target11;
    }

    public String getTarget12() { return target12; }

    public String getLocData() { return locData; }
}
