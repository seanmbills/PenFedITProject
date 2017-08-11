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

public class RegistrationSchoolActivity extends AppCompatActivity {
    private EditText schoolName;
    private EditText gpaEntry;

    private Button nextButton;
    private Button backButton;

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
        setContentView(R.layout.activity_registration_school_information);

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        nextButton = (Button) findViewById(R.id.nextButton);
        backButton = (Button) findViewById(R.id.backButton);

        schoolName = (EditText) findViewById(R.id.schoolNameEntry);
        gpaEntry = (EditText) findViewById(R.id.gpaEntry);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RegistrationNameActivity.class);
                RegistrationNameActivity.tempAccount = tempAccount;
                context.startActivity(intent);
            }
        });

    }

    private boolean registerUser() {
        final String school = schoolName.getText().toString().trim();
        final String gpa = gpaEntry.getText().toString().trim();

        if (TextUtils.isEmpty(school)) {
            Toast.makeText(this, "Please enter your school's name.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(gpa)) {
            Toast.makeText(this, "Please enter your GPA.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (!gpa.contains(".")) {
            Toast.makeText(this, "A valid GPA must include a decimal point.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (Double.parseDouble(gpa) > 4.00) {
            Toast.makeText(this, "Please enter a valid GPA.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (gpa.length() < 4) {
            Toast.makeText(this, "A valid GPA must be of the form X.XX", Toast.LENGTH_SHORT).show();
            return registerBool;
        }


        tempAccount.schoolName = school;
        tempAccount.gpa = Double.parseDouble(gpa);
        RegistrationBirthActivity.tempAccount = tempAccount;
        Intent intent = new Intent(RegistrationSchoolActivity.this, RegistrationBirthActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
