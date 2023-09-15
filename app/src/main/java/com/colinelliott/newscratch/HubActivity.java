package com.colinelliott.newscratch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;


public class HubActivity extends AppCompatActivity {
Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        getSupportFragmentManager().beginTransaction().replace(R.id.hub_container,new HomeFragment()).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.hub_container,new ActivitySelectionFragment()).commit();
        //Logout Button
        logout = findViewById(R.id.btn_logout);
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        logout.setText("Logout " + preferences.getString("firstName", ""));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","false");
                editor.apply();
                finish();
            }
        });

        //Fragment Navigation
        BottomNavigationView navMenu = findViewById(R.id.nav_menu);
        navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment selectedFragment = null;
                switch(item.getItemId()) {
                    case R.id.nav_activities:
                        //selectedFragment = new ActivitySelectionFragment();
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_data:
                        selectedFragment = new PersonalDataFragment();
                        break;
                    case R.id.nav_toDoList:
                        selectedFragment = new ToDoListFragment();
                        break;
                    default:
                        selectedFragment = new ActivitySelectionFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.hub_container,selectedFragment).commit();
           //Selects item
            return true;
            }
        });

    }
}