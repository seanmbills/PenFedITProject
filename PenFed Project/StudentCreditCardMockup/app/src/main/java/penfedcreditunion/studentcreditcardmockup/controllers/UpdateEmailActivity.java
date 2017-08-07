package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;

/**
 * Created by SEAN on 8/1/17.
 */

public class UpdateEmailActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button submitButton;
    private EditText currentEmail;
    private EditText password;
    private EditText newEmail;
    private EditText verifyNewEmail;

    private ProgressDialog progressDialog;

    boolean updateEmail;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userID;
    private Account tempAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        submitButton = (Button) findViewById(R.id.submitButton);

        progressDialog = new ProgressDialog(this);

        currentEmail = (EditText) findViewById(R.id.currentEmailField);
        password = (EditText) findViewById(R.id.passwordField);
        newEmail = (EditText) findViewById(R.id.newPasswordField);
        verifyNewEmail = (EditText) findViewById(R.id.newPasswordVerifyField);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HelpMenuActivity.class);
                context.startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmail();
            }
        });

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            Account w = eachReport.getValue(Account.class);
                            if (w.email.equals(user.getEmail())) {
                                tempAccount = w;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );

    }

    private boolean editEmail() {
        final String currEmail = currentEmail.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String newE = newEmail.getText().toString().trim();
        final String newEVerify = verifyNewEmail.getText().toString().trim();

        if (TextUtils.isEmpty(currEmail)) {
            Toast.makeText(this, "Please enter your current email.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        if (tempAccount != null) {
            if (!tempAccount.email.equals(currEmail)) {
                Toast.makeText(this, "Please enter your current email.", Toast.LENGTH_SHORT).show();
                return updateEmail;
            }
            if (!tempAccount.password.equals(pass)) {
                Toast.makeText(this, "Please enter your current password.",
                        Toast.LENGTH_SHORT).show();
                return updateEmail;
            }
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        if (TextUtils.isEmpty(newE)) {
            Toast.makeText(this, "Please enter your new email.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        if (!newE.contains("@") || !newE.contains(".")) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        if (TextUtils.isEmpty(newEVerify)) {
            Toast.makeText(this, "Please verify your new email.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        if (!newE.equals(newEVerify)) {
            Toast.makeText(this, "New emails must match.", Toast.LENGTH_SHORT).show();
            return updateEmail;
        }

        progressDialog.setMessage("Updating email...");
        progressDialog.show();

        if (newE.equals(newEVerify)) {

            String firstName = tempAccount.firstName;
            String lastName = tempAccount.lastName;
            String ssn = tempAccount.SSN;
            String cardNum = tempAccount.cardNum;
            String password = tempAccount.password;
            double gpa = tempAccount.gpa;
            HashMap<String, Transaction> pending = tempAccount.pendingTransactions;
            HashMap<String, Transaction> completed = tempAccount.completedTransactions;
            String schoolName = tempAccount.schoolName;

            Account newAccount = new Account(firstName, lastName, ssn,
                    newE, password, cardNum, userID,
                    gpa, schoolName, new HashMap<String, Transaction>(),
                    new HashMap<String, Transaction>());
            DatabaseReference userDB = mDatabase.child(userID);
            userDB.setValue(newAccount);
            for (Transaction t : pending.values()) {
                if (t.status.equals("PENDING")) {
                    userDB.child("pendingTransactions").push().setValue(t);
                } else if (t.status.equals("COMPLETED")) {
                    userDB.child("completedTransactions").push().setValue(t);
                }
            }
            for (Transaction t : completed.values()) {
                if (t.status.equals("PENDING")) {
                    userDB.child("pendingTransactions").push().setValue(t);
                } else if (t.status.equals("COMPLETED")) {
                    userDB.child("completedTransactions").push().setValue(t);
                }
            }
            user.updateEmail(newE)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateEmail = true;
                                    progressDialog.dismiss();
                                    mAuth.signOut();
                                    Intent i = new Intent(UpdateEmailActivity.this,
                                            LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateEmailActivity.this,
                                            "Could not update email...Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }}
                        });
        }
        return updateEmail;
    }

    @Override
    public void onBackPressed() {

    }
}
