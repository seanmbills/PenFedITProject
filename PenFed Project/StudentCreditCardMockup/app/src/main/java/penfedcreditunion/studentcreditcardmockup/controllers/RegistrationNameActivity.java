package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;

/**
 * Created by SEAN on 8/10/17.
 */

public class RegistrationNameActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;

    private Button nextButton;
    private Button cancelButton;

    public static Account tempAccount;
    public boolean registerBool = false;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_name_entry);

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        nextButton = (Button) findViewById(R.id.nextButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        firstName = (EditText) findViewById(R.id.firstNameEntry);
        lastName = (EditText) findViewById(R.id.lastNameEntry);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HelpMenuActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private boolean registerUser() {
        final String first = firstName.getText().toString().trim();
        final String last = lastName.getText().toString().trim();

        if (TextUtils.isEmpty(first)) {
            Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(last)) {
            Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        tempAccount = new Account(first, last, null, null, null, null, null,
                0.0, null, null, null);
        RegistrationSchoolActivity.tempAccount = tempAccount;
        Intent intent = new Intent(RegistrationNameActivity.this, RegistrationSchoolActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
