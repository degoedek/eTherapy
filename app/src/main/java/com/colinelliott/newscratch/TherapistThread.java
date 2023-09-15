package com.colinelliott.newscratch;

public class TherapistThread extends Thread{
    TherapistDatabase db;
    String username, password, firstname, lastname;

    public TherapistThread(TherapistDatabase db, String username, String password, String firstname, String lastname){
    this.db = db;
    this.username = username;
    this.password=password;
    this.firstname=firstname;
    this.lastname=lastname;
    }

    public void run(){
        db.therapistDao().insert(new Therapist(username, password, firstname, lastname));
    }
}
