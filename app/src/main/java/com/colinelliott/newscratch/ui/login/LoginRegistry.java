package com.colinelliott.newscratch.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.colinelliott.newscratch.NewScratchApplication;
import com.colinelliott.newscratch.R;
import com.colinelliott.newscratch.Therapist;
import com.colinelliott.newscratch.TherapistActivity;
import com.colinelliott.newscratch.TherapistDao;
import com.colinelliott.newscratch.TherapistDatabase;
import com.colinelliott.newscratch.TherapistThread;

public class LoginRegistry extends AppCompatActivity {


     private EditText usernameEditText;
     private EditText passwordEditText;
     private EditText firstNameEditText;
     private EditText lastNameEditText;
     private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registry);

        //TherapistRepository repository = new TherapistRepository(NewScratchApplication.getInst());
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(v -> register(usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),firstNameEditText.getText().toString()
                ,lastNameEditText.getText().toString()));

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    loginButton.performClick();
                }
                return false;
            }
        });

    }
    public void register(String username, String password, String firstName, String lastName){
        /*
        When the register button is clicked, the user's data is added to the database and they are
        routed back to the main page so they may log in. It displays a (toast) message if they did
        so and then routes them to the main log in page. If this fails (a null value in a box or in
        use username) it should allow for re-input and then put a Toast message with the issue.
        This method also initializes the database of therapists if it does not yet exist.
        */
        TherapistDatabase theraDb = TherapistDatabase.getInstance(this);

        // TODO: Send the data to the database
        //TherapistThread thread = new TherapistThread(theraDb,username,password,firstName,lastName);
        //thread.run();
        try {
            theraDb.therapistDao().insert(new Therapist(username, firstName, lastName, password));
            // TODO: Print the username back as confirmation
            Context contex = getApplicationContext();
            Toast toast = Toast.makeText(contex, "Registered: "+ username, Toast.LENGTH_SHORT);
            toast.show();

            // TODO: Route the user to the main page
            Intent intent = new Intent(this, LogInHubActivity.class);
            startActivity(intent);

        }catch(SQLiteConstraintException e){
            Toast.makeText(this.getApplicationContext(),"Invalid Username/Password", Toast.LENGTH_SHORT).show();
        }
    }
}