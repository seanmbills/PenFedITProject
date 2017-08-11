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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;
import penfedcreditunion.studentcreditcardmockup.model.TransactionType;

/**
 * Created by SEAN on 8/10/17.
 */

public class RegistrationLoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText passwordVerify;

    private Button finishButton;
    private Button backButton;

    public static Account tempAccount;
    public boolean registerBool = false;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private OkHttpClient mClient = new OkHttpClient();
    private Context mContext;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login_credentials);

        progressDialog = new ProgressDialog(this);
        mContext = getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        finishButton = (Button) findViewById(R.id.finishButton);
        backButton = (Button) findViewById(R.id.backButton);

        email = (EditText) findViewById(R.id.emailEntry);
        password = (EditText) findViewById(R.id.passwordEntry);
        passwordVerify = (EditText) findViewById(R.id.passwordVerifyEntry);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RegistrationAccountActivity.class);
                RegistrationAccountActivity.tempAccount = tempAccount;
                context.startActivity(intent);
            }
        });

    }

    private boolean registerUser() {
        final String userEmail = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();
        final String passVerify = passwordVerify.getText().toString().trim();


        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please enter a valid email.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (!userEmail.contains("@") || !userEmail.contains(".")) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please enter a valid password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(passVerify)) {
            Toast.makeText(this, "Please verify your password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (!userPassword.equals(passVerify)) {
            Toast.makeText(this, "Passwords must match.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }


        tempAccount.email = userEmail;
        tempAccount.password = userPassword;

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        //creates the user and directs to correct homepage
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerBool = true;
                            String userID = mAuth.getCurrentUser().getUid();
                            tempAccount.accId = userID;
                            tempAccount.pendingTransactions = new HashMap<String, Transaction>();
                            tempAccount.completedTransactions = new HashMap<String, Transaction>();

                            Account newAccount = new Account(tempAccount.firstName,
                                    tempAccount.lastName, tempAccount.SSN, tempAccount.email,
                                    tempAccount.password, tempAccount.cardNum, tempAccount.accId,
                                    tempAccount.gpa, tempAccount.schoolName,
                                    tempAccount.pendingTransactions, tempAccount.completedTransactions);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();

                            Transaction t1 = new Transaction(50.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.DINING);
                            Transaction t2 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.GAS);
                            Transaction t3 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.GROCERIES);
                            Transaction t4 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.DINING);
                            Transaction t5 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.TRAVEL);
                            Transaction t6 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.GAS);
                            Transaction t7 = new Transaction(30.00, dateFormat.format(date), "PenFed", "PENDING", TransactionType.DINING);
                            Transaction t8 = new Transaction(30.00, dateFormat.format(date), "PenFed", "COMPLETED", TransactionType.GAS);
                            Transaction t9 = new Transaction(30.00, dateFormat.format(date), "PenFed", "COMPLETED", TransactionType.TRAVEL);
                            Transaction t10 = new Transaction(30.00, dateFormat.format(date), "PenFed", "COMPLETED", TransactionType.GROCERIES);
                            Transaction t11 = new Transaction(30.00, dateFormat.format(date), "PenFed", "COMPLETED", TransactionType.GAS);

                            List<Transaction> list = new ArrayList<Transaction>();
                            list.add(t1);
                            list.add(t2);
                            list.add(t3);
                            list.add(t4);
                            list.add(t5);
                            list.add(t6);
                            list.add(t7);
                            list.add(t8);
                            list.add(t9);
                            list.add(t10);
                            list.add(t11);

                            DatabaseReference currentDB = mDatabase.child(userID).child("pendingTransactions");
                            DatabaseReference userDB = mDatabase.child(userID);

                            userDB.setValue(newAccount);
                            for (Transaction t : list) {
                                if (t.status.equals("PENDING")) {
                                    userDB.child("pendingTransactions").push().setValue(t);
                                } else if (t.status.equals("COMPLETED")) {
                                    userDB.child("completedTransactions").push().setValue(t);
                                }
                            }

                            try {
                                post("https://4fb57f7b.ngrok.io/sms", new  Callback(){

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"SMS Sent!",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            Twilio.init("AC50edc1ea624fdc8659c2c802b2eade7a", "3a7c47d881f69aecbc431eb05436b54c");
//
//                            Message message = Message.creator(new PhoneNumber("+15558675309"),
//                                    new PhoneNumber("+17034704997"),
//                                    "This is the ship that made the Kessel Run in fourteen parsecs?").create();
//
//                            System.out.println(message.getSid());

                            progressDialog.dismiss();
                            Intent i = new Intent(RegistrationLoginActivity.this, LoggedInActivity.class);
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationLoginActivity.this, "An account already exists with this email.\nPlease select a new one.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        return registerBool;
    }

    Call post(String url, Callback callback) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("To", "+17034704997")
                .add("Body", "This is a test text message.")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;
    }

    @Override
    public void onBackPressed() {

    }
}
