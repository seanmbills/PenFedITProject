package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;

/**
 * Created by SEAN on 8/10/17.
 */

public class RegistrationBirthActivity extends AppCompatActivity {
    private EditText birthYear;
    private Spinner monthSpinner;
    private Spinner daySpinner;

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
        setContentView(R.layout.activity_birthday_entry_information);

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        nextButton = (Button) findViewById(R.id.nextButton);
        backButton = (Button) findViewById(R.id.backButton);

        birthYear = (EditText) findViewById(R.id.birthYearEntry);
        monthSpinner = (Spinner) findViewById(R.id.birthMonthSpinner);
        daySpinner = (Spinner) findViewById(R.id.birthDaySpinner);

        List<String> months = new ArrayList<>();
        String[] monthList = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December"};
        months.addAll(Arrays.asList(monthList));

        ArrayAdapter<String> monthDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        monthDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthDataAdapter);
        monthSpinner.setSelection(0);


        List<Integer> days = new ArrayList<>();
        Integer[] dayList = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        days.addAll(Arrays.asList(dayList));

        ArrayAdapter<Integer> dayDataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        dayDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(dayDataAdapter);
        daySpinner.setSelection(0);

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
                Intent intent = new Intent(context, RegistrationSchoolActivity.class);
                RegistrationSchoolActivity.tempAccount = tempAccount;
                context.startActivity(intent);
            }
        });

    }

    private boolean registerUser() {
        final String year = birthYear.getText().toString().trim();
        final String month = monthSpinner.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(year)) {
            Toast.makeText(this, "Please enter your birth year.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        RegistrationAccountActivity.tempAccount = tempAccount;
        Intent intent = new Intent(RegistrationBirthActivity.this, RegistrationAccountActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
