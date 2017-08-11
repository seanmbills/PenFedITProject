package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class RegistrationAccountActivity extends AppCompatActivity {
    private EditText credCardNum;
    private EditText ssn;

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
        setContentView(R.layout.activity_registration_account_information);

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        nextButton = (Button) findViewById(R.id.nextButton);
        backButton = (Button) findViewById(R.id.backButton);

        credCardNum = (EditText) findViewById(R.id.ccEntry);
        ssn = (EditText) findViewById(R.id.ssnEntry);

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
                Intent intent = new Intent(context, RegistrationBirthActivity.class);
                RegistrationBirthActivity.tempAccount = tempAccount;
                context.startActivity(intent);
            }
        });

        credCardNum.addTextChangedListener(new FourDigitCardFormatWatcher());

        ssn.addTextChangedListener(new SocialSecurityFormatWatcher());

    }

    private boolean registerUser() {
        final String cardNum = credCardNum.getText().toString().trim();
        final String SSN = ssn.getText().toString().trim();

        if (TextUtils.isEmpty(cardNum)) {
            Toast.makeText(this, "Please enter your credit card number.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (credCardNum.length() != 19) {
            Toast.makeText(this, "Please enter a valid credit card number.", Toast.LENGTH_SHORT).show();
            return  registerBool;
        }

        if (TextUtils.isEmpty(SSN)) {
            Toast.makeText(this, "Please enter your Social Security Number.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (SSN.length() != 11) {
            Toast.makeText(this, "Please enter a valid SSN.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        tempAccount.cardNum = cardNum;
        tempAccount.SSN = SSN;
        RegistrationLoginActivity.tempAccount = tempAccount;
        Intent intent = new Intent(RegistrationAccountActivity.this, RegistrationLoginActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * Formats the watched EditText to a credit card number
     */
    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }
    /**
     * Formats the watched EditText to a credit card number
     */
    public static class SocialSecurityFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Insert char where needed.
            if (s.length() > 0 && (s.length() == 4 || s.length() == 7)) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
