package com.flarze.hashstash.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.data.LocationList;
import com.flarze.hashstash.data.LocationListAdapter;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.flarze.hashstash.data.instagram_login.AuthenticationDialog;
import com.flarze.hashstash.data.instagram_login.AuthenticationListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.flarze.hashstash.activity.MapsActivity.RequestPermissionCode;

public class SigninActivity extends AppCompatActivity implements AuthenticationListener {


    private Button btn_signin, btn_sign_instragram;
    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private View info = null;
    private String nameStr, userNameStr, profile_pic, insta_Id, id;

    private void launchMapsScreen() {


        if (appPreferences.getString(AppPreferences.USER_NAME) == null) {
           // startActivity(new Intent(SigninActivity.this, VerifyPhoneActivity.class));
            id=null;
        } else {
            startActivity(new Intent(SigninActivity.this, MapsActivity.class));
        }
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPreferences = new AppPreferences(this);
        if (appPreferences.getString(AppPreferences.USER_NAME) != null) {
            launchMapsScreen();
            finish();
        }

        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  LinearLayout layout=(LinearLayout) findViewById(R.id.signingLayout);
        //  layout.setBackgroundResource(R.drawable.signing_background);


        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);


        btn_signin = findViewById(R.id.btn_sign);
        btn_sign_instragram = findViewById(R.id.btn_sign_instragram);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(SigninActivity.this, VerifyPhoneActivity.class));

            }
        });

//        btn_sign_instragram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(token!=null)
//                {
//                    getUserInfoByAccessToken(token);
//                }
//                else {
//                    authenticationDialog = new AuthenticationDialog(SigninActivity.this, this);
//                    authenticationDialog.setCancelable(true);
//                    authenticationDialog.show();
//                }
//            }
//        });

    }

    public void onClick(View view) {
        if (token != null) {
            getUserInfoByAccessToken(token);
        } else {
            authenticationDialog = new AuthenticationDialog(this, this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();
        }
    }

    public void getUsers(String insta_id) {
        String urlSting = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/phone/";
        String HttpUrl = urlSting.concat(insta_id);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            // Process your json here as required

                            appPreferences.putString(AppPreferences.TABLE_ID, jsonObject.getString("id"));
                            appPreferences.putString(AppPreferences.USER_NAME, jsonObject.getString("username"));
                            appPreferences.putString(AppPreferences.NAME, jsonObject.getString("name"));
                            appPreferences.putString(AppPreferences.USER_PASSWORD, jsonObject.getString("password"));
                            appPreferences.putString(AppPreferences.USER_PHONE, jsonObject.getString("phone"));
                            appPreferences.putString(AppPreferences.USER_EMAIL, jsonObject.getString("email"));
                            appPreferences.putString(AppPreferences.USER_COUNTRY, jsonObject.getString("country"));
                            appPreferences.putString(AppPreferences.PROFILE_PIC, jsonObject.getString("image"));

                            startActivity(new Intent(SigninActivity.this, MapsActivity.class));

                        } catch (JSONException e) {
                            login();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        login();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    public void login() {

        Map<String, String> params = new HashMap<String, String>();
        // Adding All values to Params.

        params.put("name", nameStr);
        params.put("username", userNameStr);
        params.put("password", "");
        params.put("phone", insta_Id);
        params.put("email", "");
        params.put("country", "USA");
        params.put("image", profile_pic);

        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HttpUrl, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response:", response.toString());
                try {
                    // id = response.getString("phone");
                    appPreferences.putString(AppPreferences.TABLE_ID, response.getString("id"));
                    appPreferences.putString(AppPreferences.USER_NAME, response.getString("username"));
                    appPreferences.putString(AppPreferences.NAME, response.getString("name"));
                    appPreferences.putString(AppPreferences.USER_PASSWORD, response.getString("password"));
                    appPreferences.putString(AppPreferences.USER_PHONE, response.getString("phone"));
                    appPreferences.putString(AppPreferences.USER_EMAIL, response.getString("email"));
                    appPreferences.putString(AppPreferences.USER_COUNTRY, response.getString("country"));
                    appPreferences.putString(AppPreferences.PROFILE_PIC, response.getString("image"));
                    startActivity(new Intent(SigninActivity.this, MapsActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(SigninActivity.this, "" + response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        //  Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsonObjReq);
        startActivity(new Intent(SigninActivity.this, MapsActivity.class));
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        getUserInfoByAccessToken(token);
    }

    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    // JSONObject jsonData = jsonObject.getJSONObject("sig");
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя

                        insta_Id = jsonData.getString("id");
                        nameStr = jsonData.getString("full_name");
                        userNameStr = jsonData.getString("username");
                        profile_pic = jsonData.getString("profile_picture");

//                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
//                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
//                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));

                        //TODO: сохранить еще данные
                        getUsers(insta_Id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);

            }
        }
    }

    private void locationListShownn() {

        String HttpUrl = "https://api.tomtom.com/search/2/search/food.json?key=7eIbPrsJGRDCSQzW4tSaa7N3nDtH03lj&lat=24.848078&lon=89.372963&radius=100000";

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("results")) {

                            try {
                                JSONObject obj = new JSONObject(response);

                                JSONArray heroArray = obj.optJSONArray("results");

                                for (int i = 0; i < heroArray.length(); i++) {

                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    String heroArrays = heroObject.getString("poi");

                                    JSONObject heroObjectPosition = heroArray.getJSONObject(i);
                                    String heroArraysPosition = heroObjectPosition.getString("position");


                                    JSONObject object = new JSONObject(heroArrays);
                                    String heroArrayss = object.getString("name");

                                    JSONObject objectPosition = new JSONObject(heroArraysPosition);
                                    String heroArrayssPositionLat = objectPosition.getString("lat");
                                    String heroArrayssPositionLon = objectPosition.getString("lon");

                                    Toast.makeText(SigninActivity.this, heroArrayss+"..."+heroArrayssPositionLat +","+ heroArrayssPositionLon, Toast.LENGTH_SHORT).show();


                                }


                            } catch (JSONException e) {
                                // pDialog.hide();
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SigninActivity.this, "not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SigninActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(SigninActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        // makeJsonObjectRequest();

        // GetUserForNavigation("3103467261");
    }

}
