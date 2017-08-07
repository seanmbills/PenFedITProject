package penfedcreditunion.studentcreditcardmockup.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import penfedcreditunion.studentcreditcardmockup.R;

/**
 * Created by Sean on 8/1/17.
 */

public class HelpMenuActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button registerButton;
    private Button resetPasswordButton;
    private Button resetEmailButton;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        registerButton = (Button) findViewById(R.id.registerAccountButton);
        resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        resetEmailButton = (Button) findViewById(R.id.resetEmailButton);
        TextView emailReset = (TextView) findViewById(R.id.forgotEmailText);
        TextView registerText = (TextView) findViewById(R.id.registerAccountText);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        if (user == null) {
            resetEmailButton.setVisibility(View.INVISIBLE);
            emailReset.setVisibility(View.INVISIBLE);
        } else {
            registerButton.setVisibility(View.INVISIBLE);
            registerText.setVisibility(View.INVISIBLE);
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LoggedInActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        resetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UpdateEmailActivity.class);
                context.startActivity(intent);
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UpdatePasswordActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}