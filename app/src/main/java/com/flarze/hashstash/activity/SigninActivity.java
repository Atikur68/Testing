package com.flarze.hashstash.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flarze.hashstash.R;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SigninActivity extends AppCompatActivity implements AuthenticationListener {


    private Button btn_signin,btn_sign_instragram;
    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private View info = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      //  LinearLayout layout=(LinearLayout) findViewById(R.id.signingLayout);
      //  layout.setBackgroundResource(R.drawable.signing_background);

        appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);


        btn_signin=findViewById(R.id.btn_sign);
        btn_sign_instragram=findViewById(R.id.btn_sign_instragram);

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
        if(token!=null)
        {
            getUserInfoByAccessToken(token);
        }
        else {
            authenticationDialog = new AuthenticationDialog(this, this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();
        }
    }

    public void login() {
        startActivity(new Intent(SigninActivity.this,MapsActivity.class));
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
                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));

                        //TODO: сохранить еще данные
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);

            }
        }
    }
}
