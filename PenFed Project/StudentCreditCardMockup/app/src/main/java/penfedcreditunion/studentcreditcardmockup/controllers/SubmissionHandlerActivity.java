package penfedcreditunion.studentcreditcardmockup.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import penfedcreditunion.studentcreditcardmockup.model.Account;

/**
 * Created by SEAN on 8/5/17.
 */

public class SubmissionHandlerActivity extends AppCompatActivity {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    private static Account tempAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static Account findUserEmail(final String email) {
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            Account w = eachReport.getValue(Account.class);
                            if (w.email.equals(email)) {
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
        return tempAccount;
    }
}
