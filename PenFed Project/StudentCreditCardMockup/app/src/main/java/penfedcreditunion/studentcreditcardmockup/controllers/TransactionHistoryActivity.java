package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;

/**
 * Created by SEAN on 8/2/17.
 */

public class TransactionHistoryActivity extends AppCompatActivity {

    private TextView accBalance;
    private TextView credAvailable;

    private ListView completedList;
    private ListView pendingList;

    private Button backButton;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference transactionDatabase;

    private ArrayList<Transaction> array_of_pending_transactions;
    private ArrayList<Transaction> array_of_completed_transactions;

    private ProgressDialog progressDialog;

    private Account tempAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        accBalance = (TextView) findViewById(R.id.balanceText);
        credAvailable = (TextView) findViewById(R.id.credAvailText);

        completedList = (ListView) findViewById(R.id.completedTransactionList);
        pendingList = (ListView) findViewById(R.id.pendingTransactionList);

        backButton = (Button) findViewById(R.id.backButton);

        progressDialog = new ProgressDialog(this);

        array_of_pending_transactions = new ArrayList<Transaction>();
        array_of_completed_transactions = new ArrayList<Transaction>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final NumberFormat formatter = NumberFormat.getCurrencyInstance();

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
                        if (tempAccount != null) {
                            if (tempAccount.pendingTransactions != null) {
                                for (Transaction t : tempAccount.pendingTransactions.values()) {
                                    if (t.status.equals("PENDING")) {
                                        array_of_pending_transactions.add(t);
                                    }
                                }
                            }
                            if (tempAccount.completedTransactions != null) {
                                for (Transaction t : tempAccount.completedTransactions.values()) {
                                    if (t.status.equals("COMPLETED")) {
                                        array_of_completed_transactions.add(t);
                                    }
                                }
                            }
                            accBalance.setText(formatter.format(tempAccount.creditUsed));
                            credAvailable.setText("Credit Available: "
                                    + formatter.format(tempAccount.accCreditLimit
                                    - tempAccount.creditUsed));
                        }

                        if (array_of_pending_transactions != null) {
                            ArrayAdapter<Transaction> pendingAdapter = new ArrayAdapter<Transaction>(
                                    TransactionHistoryActivity.this,
                                    android.R.layout.simple_list_item_1, array_of_pending_transactions);

                            pendingList.setAdapter(pendingAdapter);

                        }
                        if (array_of_completed_transactions != null) {
                            ArrayAdapter<Transaction> completedAdapter = new ArrayAdapter<Transaction>(
                                    TransactionHistoryActivity.this,
                                    android.R.layout.simple_list_item_1, array_of_completed_transactions);

                            completedList.setAdapter(completedAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LoggedInActivity.class);
                context.startActivity(intent);
            }
        });

        /*pendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction clickedTransaction = (Transaction) parent.getAdapter().getItem(position);
                ShowTransactionInfoActivity.amount = clickedTransaction.getTransactionAmount();
                ShowTransactionInfoActivity.endpoint = clickedTransaction.getBusinessEndpoint();
                ShowTransactionInfoActivity.status = clickedTransaction.getStatus();
                ShowTransactionInfoActivity.date = clickedTransaction.getTransactionDate();
                ShowTransactionInfoActivity.transaction = clickedTransaction;
                System.out.println("Clicked Transaction...");
                System.out.println(clickedTransaction.getTransactionAmount());

                Context context = getApplicationContext();
                Intent intent = new Intent(context, ShowTransactionInfoActivity.class);
                context.startActivity(intent);
            }

        });*/

    }

    @Override
    public void onBackPressed() {

    }
}
