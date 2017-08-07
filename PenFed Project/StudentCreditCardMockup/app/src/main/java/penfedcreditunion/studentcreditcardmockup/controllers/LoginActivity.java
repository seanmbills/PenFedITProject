package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import penfedcreditunion.studentcreditcardmockup.R;

public class LoginActivity extends AppCompatActivity {

    // UI references
    private EditText signInId;
    private EditText signInPassword;
    private Button signInButton;
    private Button helpButton;
    private CheckBox rememberUsernameCheckbox;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    private String username;

    private ProgressDialog progressDialog;
    boolean signInBool = false;
    boolean registerBool = false;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        progressDialog = new ProgressDialog(this);
        helpButton = (Button) findViewById(R.id.helpButton);
        signInButton = (Button) findViewById(R.id.loginButton);
        signInId = (EditText) findViewById(R.id.userIdField);
        signInPassword = (EditText) findViewById(R.id.passwordField);
        rememberUsernameCheckbox = (CheckBox) findViewById(R.id.rememberEmailCheckBox);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            signInId.setText(loginPreferences.getString("username", ""));
            rememberUsernameCheckbox.setChecked(true);
        }

        rememberUsernameCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckbox();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HelpMenuActivity.class);
                context.startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    /**
     * Method to Sign-In User
     * @return boolean if successful
     */
    private boolean signIn() {
        String userId = signInId.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "Please enter a valid User ID.",Toast.LENGTH_SHORT).show();
            return signInBool;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.",Toast.LENGTH_SHORT).show();
            return signInBool;
        }

        progressDialog.setMessage("Signing In User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userId,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                            final String uID = currentUser.getUid();
                            mDatabase.child(uID).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Intent i = new Intent(LoginActivity.this, LoggedInActivity.class);

                                            startActivity(i);

                                            signInBool = true;
                                            progressDialog.dismiss();


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,
                                    "Could not Sign In... Please check your username and password.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        return signInBool;

    }

    public void updateCheckbox() {
        username = signInId.getText().toString();
        if (rememberUsernameCheckbox.isChecked()) {
            loginPrefsEditor.clear();
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
