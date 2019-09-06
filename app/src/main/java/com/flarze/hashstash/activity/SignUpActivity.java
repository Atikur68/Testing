package com.flarze.hashstash.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.data.Api;
import com.flarze.hashstash.data.User;
import com.flarze.hashstash.data.instagram_login.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolBarSignup;
    private EditText editTextName, editTextUserName, editTextPassword, editTextPhone, editTextEmail, editTextCountry;
    private Button buttonNext;
    private String editTextNameStr = "", editTextUserNameStr = "", editTextPasswordStr = "", editTextPhoneStr = "", editTextEmailStr = "", editTextCountryStr = "";

    private String jsonResponse,mobile;

    private User mUser, loadedUser;
    private AppPreferences appPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        appPreferences = new AppPreferences(this);

        mobile=getIntent().getStringExtra("mobile");

        toolBarSignup = findViewById(R.id.toolBarSignup);
        editTextName = findViewById(R.id.editTextName);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
       // editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCountry = findViewById(R.id.editTextCountry);
        buttonNext = findViewById(R.id.buttonNext);


        setSupportActionBar(toolBarSignup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;

                editTextNameStr = editTextName.getText().toString().trim();
                editTextUserNameStr = editTextUserName.getText().toString().trim();
                editTextPasswordStr = editTextPassword.getText().toString().trim();
               // editTextPhoneStr = editTextPhone.getText().toString().trim();
                editTextEmailStr = editTextEmail.getText().toString().trim();
                editTextCountryStr = editTextCountry.getText().toString().trim();

                if (editTextNameStr.isEmpty()) {
                    editTextName.setError("Please fill up");
                    editTextName.requestFocus();
                    status = false;
                }
                if (editTextUserNameStr.isEmpty()) {
                    editTextUserName.setError("Please fill up");
                    editTextUserName.requestFocus();
                    status = false;
                }

                if (editTextCountryStr.isEmpty()) {
                    editTextCountry.setError("Please fill up");
                    editTextCountry.requestFocus();
                    status = false;
                }

                if (editTextPasswordStr.isEmpty() || editTextPasswordStr.length() < 8) {
                    editTextPassword.setError("At least 8 letters");
                    editTextPassword.requestFocus();
                    status = false;
                }

//                if (editTextPhoneStr.isEmpty() || editTextPhoneStr.length() < 10) {
//                    editTextPhone.setError("Please enter valid number");
//                    editTextPhone.requestFocus();
//                    status = false;
//                }

                if (!isValidEmailId(editTextEmailStr)) {
                    editTextEmail.setError("Please enter valid email");
                    editTextEmail.requestFocus();
                    status = false;
                }

                if (status == true) {

                    makeJsonObjectRequest();

                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void makeJsonObjectRequest() {

        Map<String, String> params = new HashMap<String, String>();
        // Adding All values to Params.

        params.put("name", editTextNameStr);
        params.put("username", editTextUserNameStr);
        params.put("password", editTextPasswordStr);
        params.put("phone", mobile);
        params.put("email", editTextEmailStr);
        params.put("country", editTextCountryStr);
        params.put("image", "XX");



        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HttpUrl, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("response:", response.toString());
                try {

                    appPreferences.clear();
                    Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    appPreferences.putString(AppPreferences.TABLE_ID, response.getString("id"));
                    appPreferences.putString(AppPreferences.USER_NAME, response.getString("username"));
                    appPreferences.putString(AppPreferences.NAME, response.getString("name"));
                    appPreferences.putString(AppPreferences.USER_PASSWORD, response.getString("password"));
                    appPreferences.putString(AppPreferences.USER_PHONE, response.getString("phone"));
                    appPreferences.putString(AppPreferences.USER_EMAIL, response.getString("email"));
                    appPreferences.putString(AppPreferences.USER_COUNTRY, response.getString("country"));
                    appPreferences.putString(AppPreferences.PROFILE_PIC, response.getString("image"));
                    startActivity(new Intent(SignUpActivity.this,MapsActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue;
        //  Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsonObjReq);

        // Adding request to request queue
        // AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
