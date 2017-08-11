package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;

/**
 * Created by Sean on 7/31/17.
 */

public class LoggedInActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button settingsButton;
    private Button rewardsButton;

    private TextView currBalance;
    private TextView dailyLimit;
    private TextView creditAvailable;
    private TextView dailySpending;
    private TextView cashBackToDate;
    private TextView cashBackRate;
    private TextView additionalCashBack;
    private TextView balanceText;

    private ProgressBar dailyProgress;

    private ProgressDialog progressDialog;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private Account tempAccount;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        rewardsButton = (Button) findViewById(R.id.rewardsButton);

        rewardsButton.setVisibility(View.INVISIBLE);

        currBalance = (TextView) findViewById(R.id.currentBalance);
        dailyLimit = (TextView) findViewById(R.id.dailyLimitText);
        creditAvailable = (TextView) findViewById(R.id.creditAvailText);
        dailySpending = (TextView) findViewById(R.id.dailySpendingField);
        cashBackToDate = (TextView) findViewById(R.id.cashBackToDateField);
        cashBackRate = (TextView) findViewById(R.id.cashBackRateField);
        additionalCashBack = (TextView) findViewById(R.id.additionalCashBackText);
        balanceText = (TextView) findViewById(R.id.balanceText);

        dailyProgress = (ProgressBar) findViewById(R.id.dailySpendingProgress);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final DecimalFormat decimalFormatter = new DecimalFormat("##.00");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();

        currBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TransactionHistoryActivity.class);
                context.startActivity(intent);
            }
        });
        creditAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TransactionHistoryActivity.class);
                context.startActivity(intent);
            }
        });
        balanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TransactionHistoryActivity.class);
                context.startActivity(intent);
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
                        if (tempAccount != null) {
                            double totalSpent = tempAccount.getCreditUsed();
                            mDatabase.child(user.getUid()).child("creditUsed").setValue(totalSpent);
                            currBalance.setText(formatter.format(tempAccount.getCreditUsed()));
                            dailyLimit.setText(formatter.format(tempAccount.dailyLimit));

                            cashBackRate.setText(decimalFormatter.format(tempAccount.cashBackRate) + "%");
                            if (tempAccount.gpa >= 3.00) {
                                additionalCashBack.setText("(Additional 1% cash back on Gas and Dining)");
                            }
                            double cashBack = tempAccount.getCashBackToDate();
                            cashBackToDate.setText(formatter.format(cashBack));
                            mDatabase.child(user.getUid()).child("cashBackToDate").setValue(cashBack);

                            double dailyTotal = 0.0;
                            for (Transaction t : tempAccount.pendingTransactions.values()) {
                                String tDate = t.transactionDate;
                                String dateMinusTime = tDate.substring(0, 10);
                                if (dateMinusTime.equals(dateFormat.format(date).toString())) {
                                    dailyTotal += t.transactionAmount;
                                }
                            }
                            for (Transaction t : tempAccount.completedTransactions.values()) {
                                String tDate = t.transactionDate;
                                String dateMinusTime = tDate.substring(0, 10);
                                if (dateMinusTime.equals(dateFormat.format(date).toString())) {
                                    dailyTotal += t.transactionAmount;
                                }
                            }
                            double dailyProgressCalc = ((dailyTotal / tempAccount.dailyLimit) * 100);
                            dailyProgress.setProgress((int)(dailyProgressCalc));
                            creditAvailable.setText("Credit Available: "
                                    + formatter.format(tempAccount.accCreditLimit
                                    - tempAccount.getCreditUsed()));
                            dailySpending.setText("Daily Spending: " + formatter.format(dailyTotal));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Context context = v.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HelpMenuActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
