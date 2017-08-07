package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;

/**
 * Created by SEAN on 8/2/17.
 */

public class ShowTransactionInfoActivity extends AppCompatActivity {
	private Button backButton;
	private EditText businessEndpoint;
	private EditText transactionAmount;
	private EditText transactionDate;
	private EditText transactionStatus;

	private FirebaseUser user;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private DatabaseReference mDatabase;

	private ProgressDialog progressDialog;

	public static String endpoint;
	public static String amount;
	public static String date;
	public static String status;
	public static Transaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_info);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		backButton = (Button) findViewById(R.id.backButton);

		businessEndpoint = (EditText) findViewById(R.id.transactionBusinessField);
		transactionAmount = (EditText) findViewById(R.id.transactionAmountField);
		transactionDate = (EditText) findViewById(R.id.transactionDateField);
		transactionStatus = (EditText) findViewById(R.id.transactionStatusField);

		mAuth = FirebaseAuth.getInstance();
		user = mAuth.getCurrentUser();
		mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

		final NumberFormat formatter = NumberFormat.getCurrencyInstance();

		businessEndpoint.setText(endpoint);
		transactionAmount.setText(amount);
		transactionDate.setText(date);
		transactionStatus.setText(status);


		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				Intent intent = new Intent(context, LoggedInActivity.class);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {

	}
}
