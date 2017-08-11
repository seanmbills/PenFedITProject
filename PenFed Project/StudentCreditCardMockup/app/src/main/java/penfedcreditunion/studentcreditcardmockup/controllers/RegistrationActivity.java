package penfedcreditunion.studentcreditcardmockup.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import penfedcreditunion.studentcreditcardmockup.R;
import penfedcreditunion.studentcreditcardmockup.model.Account;
import penfedcreditunion.studentcreditcardmockup.model.Transaction;
import penfedcreditunion.studentcreditcardmockup.model.TransactionType;


/**
 * Created by SEAN on 7/31/17.
 */

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameFirst;
    private EditText nameLast;
    private EditText ccNum;
    private EditText socialNum;
    private EditText email;
    private EditText pWord;
    private EditText passVerify;
    private EditText schoolName;
    private EditText gpaEntry;
    private Button cancelButton;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private EditText yearEntry;
    boolean registerBool = false;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String month;
    private Integer day;

    private OkHttpClient mClient = new OkHttpClient();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = getApplicationContext();

        progressDialog = new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        nameFirst = (EditText) findViewById(R.id.firstNameEntry);
        nameLast = (EditText) findViewById(R.id.lastNameEntry);
        socialNum = (EditText) findViewById(R.id.ssnEntry);
        email = (EditText) findViewById(R.id.emailEntry);
        pWord = (EditText) findViewById(R.id.passwordFirstEntry);
        passVerify = (EditText) findViewById(R.id.verifyPasswordEntry);
        ccNum = (EditText) findViewById(R.id.ccEntry);
        monthSpinner = (Spinner) findViewById(R.id.dobMonthSpinner);
        daySpinner = (Spinner) findViewById(R.id.dobDaySpinner);
        yearEntry = (EditText) findViewById(R.id.dobYearEntry);
        schoolName = (EditText) findViewById(R.id.schoolEntry);
        gpaEntry = (EditText) findViewById(R.id.gpaEntry);

        //monthSpinner.setOnItemSelectedListener(this);
        //daySpinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
//                try {
//                    post("https://6989a2a9.ngrok.io", new  Callback(){
//
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),"SMS Sent!",Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HelpMenuActivity.class);
                context.startActivity(intent);
            }
        });

        ccNum.addTextChangedListener(new FourDigitCardFormatWatcher());

        socialNum.addTextChangedListener(new SocialSecurityFormatWatcher());

    }

    /**
     * A method to attempt to register user
     * @return boolean if successful
     */
    private boolean registerUser() {
        final String userEmail = email.getText().toString().trim();
        final String password = pWord.getText().toString().trim();
        final String firstName = nameFirst.getText().toString().trim();
        final String lastName = nameLast.getText().toString().trim();
        final String credCardNum = ccNum.getText().toString().trim();
        final String social = socialNum.getText().toString().trim();
        final String pwordVerify = passVerify.getText().toString().trim();
        final String dobYear = yearEntry.getText().toString().trim();
        final String sName = schoolName.getText().toString().trim();
        final String gpa = gpaEntry.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter your first name.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter your last name.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(sName)) {
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

        if (gpa.length() < 4) {
            Toast.makeText(this, "A valid GPA must be of the form X.XX", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(credCardNum)) {
            Toast.makeText(this, "Please enter your credit card number.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (credCardNum.length() != 19) {
            Toast.makeText(this, "Please enter a valid credit card number.", Toast.LENGTH_SHORT).show();
            return  registerBool;
        }

        if (TextUtils.isEmpty(social)) {
            Toast.makeText(this, "Please enter your SSN.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (social.length() != 11) {
            Toast.makeText(this, "Please enter a valid SSN.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(dobYear)) {
            Toast.makeText(this, "Please enter your birth year.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please enter a valid email.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (!userEmail.contains("@") || !userEmail.contains(".")) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a valid password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(pwordVerify)) {
            Toast.makeText(this, "Please verify your password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (!password.equals(pwordVerify)) {
            Toast.makeText(this, "Passwords must match.", Toast.LENGTH_SHORT).show();
            return registerBool;
        }




        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        //creates the user and directs to correct homepage
        mAuth.createUserWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerBool = true;
                            String userID = mAuth.getCurrentUser().getUid();
                            Account newAccount = new Account(firstName, lastName, social,
                                    userEmail, password, credCardNum, userID,
                                    Double.parseDouble(gpa), sName, new HashMap<String, Transaction>(),
                                    new HashMap<String, Transaction>());

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

//                            Twilio.init("AC50edc1ea624fdc8659c2c802b2eade7a", "3a7c47d881f69aecbc431eb05436b54c");
//
//                            Message message = Message.creator(new PhoneNumber("+17034704997"),
//                                    new PhoneNumber("+12408835148"),
//                                    "This is the ship that made the Kessel Run in fourteen parsecs?").create();

//                            System.out.println(message.getSid());

                            progressDialog.dismiss();
                            Intent i = new Intent(RegistrationActivity.this, LoggedInActivity.class);
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Could not register... Please try again.",Toast.LENGTH_SHORT).show();

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
