package com.colinelliott.newscratch.ui.login;

import android.app.Activity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.colinelliott.newscratch.ActivitySelectionFragment;
import com.colinelliott.newscratch.HubActivity;
import com.colinelliott.newscratch.MainActivity;
import com.colinelliott.newscratch.Patient;
import com.colinelliott.newscratch.PatientDatabase;
import com.colinelliott.newscratch.R;
import com.colinelliott.newscratch.TherapistActivity;
import com.colinelliott.newscratch.TherapistDatabase;
import com.colinelliott.newscratch.WelcomeScreen;
import com.colinelliott.newscratch.ui.login.LoginViewModel;
import com.colinelliott.newscratch.ui.login.LoginViewModelFactory;
import com.colinelliott.newscratch.databinding.ActivityLoginBinding;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    int userID;
    CheckBox remember;
    Spinner spinner;
    boolean therapist=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set View
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        remember = findViewById(R.id.rememberMe);

        //Spinner Adapter
        spinner = findViewById(R.id.dropdown_menu);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dropdown_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Remember Me Login Function
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if (checkbox.equals("true")){
            Intent intent = new Intent(LoginActivity.this, HubActivity.class);
            startActivity(intent);
        }
        else if (checkbox.equals("false")) {
            Toast.makeText(this,"Please Sign In.",Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Checked",Toast.LENGTH_SHORT).show();
                }
                else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();


            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sharing Data for First and Last Name
                if (therapist) {
                    TherapistDatabase theraDb = TherapistDatabase.getInstance(LoginActivity.this);

                    if (!verifyUser(theraDb, usernameEditText, passwordEditText)) {
                        Context contex = getApplicationContext();
                        Toast toast = Toast.makeText(contex, "Invalid Username/Password", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    // String first = preferences.getString("firstName", "");
                    // String last = preferences.getString("lastName", "");
                    String firstEdit = theraDb.therapistDao().findUserWithName(usernameEditText.getText().toString()).get(0).getFirstName();
                    //String firstEdit = firstName.getText().toString();
                    String lastEdit = theraDb.therapistDao().findUserWithName(usernameEditText.getText().toString()).get(0).getLastName();
                    //String lastEdit = lastName.getText().toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("firstName", firstEdit);
                    editor.putString("lastName", lastEdit);
                    editor.putString("username", usernameEditText.getText().toString());
                    userID = theraDb.therapistDao().findUserWithName(usernameEditText.getText().toString()).get(0).getTheraId();
                    editor.putInt("User ID",userID);
                    //Toast.makeText(getApplicationContext(), Integer.toString(userID), Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Is Therapist", true);
                    editor.apply();
                }
                else{
                    PatientDatabase patientDb = PatientDatabase.getInstance(LoginActivity.this);
                    if (!verifyPatient(patientDb, usernameEditText, passwordEditText)) {
                        Context contex = getApplicationContext();
                        Toast toast = Toast.makeText(contex, "Invalid Username/Password", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    int id = Integer.parseInt(usernameEditText.getText().toString());
                    int pass = Integer.parseInt(passwordEditText.getText().toString());
                    String firstEdit = patientDb.patientDao().findUserWithIdPass(id,pass).get(0).getFirstName();
                    String lastEdit = patientDb.patientDao().findUserWithIdPass(id,pass).get(0).getLastName();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("firstName", firstEdit);
                    editor.putString("lastName", lastEdit);
                    editor.putString("username", usernameEditText.getText().toString());
                    userID = patientDb.patientDao().findUserWithIdPass(id,pass).get(0).getPatientId();
                    editor.putInt("User ID",userID);
                    editor.putBoolean("Is Therapist", false);
                    editor.apply();

                }
            }
        });

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

    public boolean verifyUser(TherapistDatabase theraDb, EditText usernameEdit, EditText passwordEdit){
        /*
        This function receives an instance of the database as well as both the inputted username and password
        It first verifies if the username is in the database and if not, returns false
        Then, if the user does exist, it checks if the password matches and if not, returns false
        If both conditions are successful, the function returns true
         */
        if(theraDb.therapistDao().findUserWithName(usernameEdit.getText().toString()).size() < 1){
            return false;
        }
        if(!Objects.equals(theraDb.therapistDao().findUserWithName(usernameEdit.getText().toString()).get(0).getPassword(), passwordEdit.getText().toString())){
            return false;
        }
        return true;
    }

    public boolean verifyPatient(PatientDatabase patientDb, EditText idEdit, EditText passwordEdit){
        /*
        This function is a derivative of the verifyUser function which works for patients instead of
        therapists. It receives an instance of the patient database and two edit text fields for id
        and password. It returns a boolean depending on if the id and password combo exist.
         */
        int pass, id;
        try {
            pass = Integer.parseInt(passwordEdit.getText().toString());
            id = Integer.parseInt(idEdit.getText().toString());
        }catch(NumberFormatException e){
            return false;
        }
        if(patientDb.patientDao().getAllPatientsByIDList(Integer.parseInt(idEdit.getText().toString())).size()>0)
            if(patientDb.patientDao().findUserWithIdPass(id,pass).size()>0)
                return true;

        return false;
    }

    private void updateUiWithUser(LoggedInUserView model) {
        model.getDisplayName();
        //SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        //String welcome = getString(R.string.welcome) + preferences.getString("firstName","");
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();
        if(!therapist) {
            Intent intent1 = new Intent(this, HubActivity.class);
            //intent1.putExtra("Is Therapist", false);
            startActivity(intent1);
        }
        else {
            Intent intent2 = new Intent(this, TherapistActivity.class);
            //intent2.putExtra("Is Therapist", true);
            startActivity(intent2);
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    //Spinner Implemented Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position==0) {
            therapist=false;
        }
        else {
            therapist = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}