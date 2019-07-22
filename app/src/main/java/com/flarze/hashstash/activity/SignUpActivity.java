package com.flarze.hashstash.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private String jsonResponse;

    private User mUser, loadedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolBarSignup = findViewById(R.id.toolBarSignup);
        editTextName = findViewById(R.id.editTextName);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);
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
                editTextPhoneStr = editTextPhone.getText().toString().trim();
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

                if (editTextPhoneStr.isEmpty() || editTextPhoneStr.length() < 10) {
                    editTextPhone.setError("Please enter valid number");
                    editTextPhone.requestFocus();
                    status = false;
                }

                if (!isValidEmailId(editTextEmailStr)) {
                    editTextEmail.setError("Please enter valid email");
                    editTextEmail.requestFocus();
                    status = false;
                }

                if (status == true) {

                    String urlSting="http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/phone/";
                    String HttpUrl =urlSting.concat(editTextPhoneStr);

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response);
                                        // Process your json here as required
                                        String name = jsonObject.getString("name");
                                        Toast.makeText(SignUpActivity.this, name, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        // Handle json exception as needed
                                    }
                                }
                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(SignUpActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Creating RequestQueue.
                    requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

                   // makeJsonObjectRequest();

                    // GetUserForNavigation("3103467261");

                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void GetUserForNavigation(String identity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<User>> call = api.ProfileApi(identity);//
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> heroList = response.body();
                for (int i = 0; i < heroList.size(); i++) {
                    loadedUser = heroList.get(i);
                    Toast.makeText(getApplicationContext(), "" + loadedUser, Toast.LENGTH_SHORT).show();
                    //  SharedPref.write("user", loadedUser);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeJsonObjectRequest() {

        String urlSting="http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/phone/";
        String HttpUrl =urlSting.concat(editTextPhoneStr);
        Toast.makeText(SignUpActivity.this, HttpUrl, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                HttpUrl, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("name");
                    String email = response.getString("email");
                    JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("username");
                    String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse += "Name: " + name + "\n\n";
                    jsonResponse += "Email: " + email + "\n\n";
                    jsonResponse += "username: " + home + "\n\n";
                    jsonResponse += "Mobile: " + mobile + "\n\n";

                    Toast.makeText(SignUpActivity.this, "" + response, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        {
//            @Override
//            protected Map<String, String> getParams() {
//
//                // Creating Map String Params.
//                Map<String, String> params = new HashMap<String, String>();
//                // Adding All values to Params.
//
//                params.put("phone", editTextPhoneStr);
//              //  params.put("phone",3103467261);
//
//                return params;
//            }
//        };

        // Adding request to request queue
        //  AppController.getInstance().addToRequestQueue(jsonObjReq);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(SignUpActivity.this);
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
