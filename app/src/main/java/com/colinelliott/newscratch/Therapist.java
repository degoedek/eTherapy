package com.colinelliott.newscratch;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"username"},
        unique = true)})
@OnConflictStrategy()
public class Therapist {
    @PrimaryKey(autoGenerate = true)
    private int theraId;
    @NonNull @ColumnInfo(name = "username")
    private final String username;
    @ColumnInfo(name = "firstName")
    private final String firstName;
    @ColumnInfo(name = "lastName")
    private final String lastName;
    @ColumnInfo(name = "password")
    private final String password;

    public Therapist(@NonNull String username, String firstName, String lastName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public void setTheraId(int theraId) {this.theraId = theraId;}

    public String getFirstName() {
        return firstName;
    }

    public int getTheraId(){return theraId;} //should probably be private

    public String getLastName() { return lastName; }

    public Therapist getUser(){return new Therapist(username,firstName,lastName,password);}

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
