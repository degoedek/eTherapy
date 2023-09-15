package com.colinelliott.newscratch.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.colinelliott.newscratch.R;

public class LogInHubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_hub);
        Button registerButton, therapistButton, clientButton;
        registerButton = findViewById(R.id.registerButton);
        therapistButton = findViewById(R.id.therapistButton);

        registerButton.setOnClickListener(v -> openRegistrationActivity());
        therapistButton.setOnClickListener(v -> openOldPage());
    }
    public void openRegistrationActivity(){
        Intent intent = new Intent(this,LoginRegistry.class);
        startActivity(intent);
    }
    public void openOldPage(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}