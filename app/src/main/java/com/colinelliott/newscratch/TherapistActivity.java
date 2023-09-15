package com.colinelliott.newscratch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class TherapistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist);
        getSupportFragmentManager().beginTransaction().replace(R.id.therapist_container,new HomeFragment()).commit();

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String welcome = getString(R.string.welcome) + preferences.getString("firstName","");
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();

        //Fragment Navigation
        BottomNavigationView navMenu = findViewById(R.id.nav_menu_therapist);
        navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment selectedFragment = null;
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_exercises:
                        selectedFragment = new ExercisesFragment();
                        break;
                    case R.id.nav_patients:
                        selectedFragment = new PatientsFragment();
                        break;
                    default:
                        selectedFragment = new ActivitySelectionFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.therapist_container,selectedFragment).commit();
                //Selects item
                return true;
            }
        });



    }
}