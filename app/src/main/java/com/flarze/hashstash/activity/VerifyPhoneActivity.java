package com.flarze.hashstash.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.flarze.hashstash.R;
import com.flarze.hashstash.data.SharedPref;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static com.flarze.hashstash.data.Constants.IS_LOGIN;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String mVerificationId;
    private EditText editTextNumber;
    private TextView resend, resendTitle;
    private PinView editTextCode;
    private RelativeLayout layoutPhone, layoutVerify;
    private FirebaseAuth mAuth;
    private InputMethodManager imm;
    public String mobile;
    private Toolbar toolBarPhoneVerify;
    private GestureDetectorCompat gestureDetectorCompat = null;
    private AppPreferences appPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        appPreferences = new AppPreferences(this);


        toolBarPhoneVerify = findViewById(R.id.toolBarPhoneVerify);


        setSupportActionBar(toolBarPhoneVerify);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("bn");
        editTextNumber = findViewById(R.id.editTextMobile);
        editTextCode = findViewById(R.id.editTextCode);
        resend = findViewById(R.id.text_resend);
        resendTitle = findViewById(R.id.text_title);

        layoutPhone = findViewById(R.id.container);
        layoutVerify = findViewById(R.id.container2);
        layoutPhone.setVisibility(View.VISIBLE);
        layoutVerify.setVisibility(View.GONE);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = editTextNumber.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 11) {
                    editTextNumber.setError("Enter a valid mobile");
                    editTextNumber.requestFocus();
                } else {
                    VerifyPhoneActivity.this.sendVerificationCode(mobile);
                    resendTitle.append("\n" + mobile);
                    layoutPhone.setVisibility(View.GONE);
                    layoutVerify.setVisibility(View.VISIBLE);
                    editTextCode.setFocusableInTouchMode(true);
                    editTextCode.setFocusable(true);
                    editTextCode.requestFocus();
                    imm.showSoftInput(editTextCode, InputMethodManager.SHOW_FORCED);
                    getUser();
                }
            }
        });

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                VerifyPhoneActivity.this.verifyVerificationCode(code);
            }
        });


        resend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyPhoneActivity.this.sendVerificationCode(mobile);
            }
        }));

    }

    public void getUser() {
        String urlSting = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/phone/";
        String HttpUrl = urlSting.concat(mobile);

        Toast.makeText(this, mobile, Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(VerifyPhoneActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            // Process your json here as required

                            Toast.makeText(VerifyPhoneActivity.this, response, Toast.LENGTH_SHORT).show();
                            Log.d("response:", response);

                            appPreferences.putString(AppPreferences.TABLE_ID, jsonObject.getString("id"));
                            appPreferences.putString(AppPreferences.USER_NAME, jsonObject.getString("username"));
                            appPreferences.putString(AppPreferences.NAME, jsonObject.getString("name"));
                            appPreferences.putString(AppPreferences.USER_PASSWORD, jsonObject.getString("password"));
                            appPreferences.putString(AppPreferences.USER_PHONE, mobile);
                            appPreferences.putString(AppPreferences.USER_EMAIL, jsonObject.getString("email"));
                            appPreferences.putString(AppPreferences.USER_COUNTRY, jsonObject.getString("country"));
                            appPreferences.putString(AppPreferences.PROFILE_PIC, jsonObject.getString("image"));

                        } catch (JSONException e) {
                            // Handle json exception as needed
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // startActivity(new Intent(VerifyPhoneActivity.this,SignUpActivity.class));
                        appPreferences.clear();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(VerifyPhoneActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        // makeJsonObjectRequest();

        // GetUserForNavigation("3103467261");
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("Failed", "onVerificationFailed: " + e.getMessage());
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // Pass activity on touch event to the gesture detector.
//        gestureDetectorCompat.onTouchEvent(event);
//        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                                    User user = new User();
//                                    user.setPhone(mobile);
                            //verification successful we will start the profile activity
                            // SharedPref.write(IS_LOGIN, true);
                            if (appPreferences.getString(AppPreferences.USER_NAME) == null) {
                                Intent intent = new Intent(VerifyPhoneActivity.this, SignUpActivity.class);
                                intent.putExtra("mobile", mobile);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(VerifyPhoneActivity.this, MapsActivity.class);
                                  intent.putExtra("switch", "hash");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                Toast.makeText(VerifyPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(VerifyPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}