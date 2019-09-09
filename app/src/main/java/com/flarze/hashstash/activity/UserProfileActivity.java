package com.flarze.hashstash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.data.Api;
import com.flarze.hashstash.data.LocationList;
import com.flarze.hashstash.data.LocationListAdapter;
import com.flarze.hashstash.data.VolleyMultipartRequest;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolBarProfile;
    private static int RESULT_LOAD_IMG = 1;
    private GestureDetectorCompat gestureDetectorCompat = null;
    private CircularImageView profile_image;
    private AppPreferences appPreferences = null;
    private ImageView editProfile, editProfileDone;
    private Uri selectedImage;
    private String pathss = "";
    private String image;
    private ProgressDialog progressDialog;
    public String HttpUrl;

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        appPreferences = new AppPreferences(this);

        HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + appPreferences.getString(AppPreferences.TABLE_ID) + "/images";

        toolBarProfile = findViewById(R.id.toolBarProfile);
        profile_image = findViewById(R.id.profile_image);
        editProfile = findViewById(R.id.editProfile);
        editProfileDone = findViewById(R.id.editProfileDone);

        if (appPreferences.getString(AppPreferences.PROFILE_PIC).contains("")) {
            profile_image.setImageResource(R.drawable.demoman);
        } else {
            Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(profile_image);
        }

        setSupportActionBar(toolBarProfile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        editProfileDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage != null)
                   // uploadFile(image);
                uploadFile(selectedImage);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathss = cursor.getString(columnIndex);
                Toast.makeText(this, pathss.toString(),
                        Toast.LENGTH_LONG).show();
                cursor.close();
                File imgFile = new File(pathss);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                  //  image = getStringImage(myBitmap);
                  //  Toast.makeText(UserProfileActivity.this, "" + image, Toast.LENGTH_SHORT).show();
                    profile_image.setImageBitmap(myBitmap);
                    editProfile.setVisibility(GONE);
                    editProfileDone.setVisibility(VISIBLE);

                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e.toString(), Toast.LENGTH_LONG)
                    .show();


        }
    }


    private void uploadFile(final String image) {

    //    Toast.makeText(UserProfileActivity.this, image, Toast.LENGTH_SHORT).show();

        //       Map<String, String> params = new HashMap<String, String>();
        // Adding All values to Params.
//        params.put("id", appPreferences.getString(AppPreferences.TABLE_ID));
//        params.put("name", appPreferences.getString(AppPreferences.NAME));
//        params.put("username", appPreferences.getString(AppPreferences.USER_NAME));
//        params.put("password", appPreferences.getString(AppPreferences.USER_PASSWORD));
//        params.put("phone", appPreferences.getString(AppPreferences.USER_PHONE));
//        params.put("email", appPreferences.getString(AppPreferences.USER_EMAIL));
//        params.put("country", appPreferences.getString(AppPreferences.USER_COUNTRY));
        //       params.put("image", image);


//        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/"+appPreferences.getString(AppPreferences.TABLE_ID)+"/images";
//
//        Toast.makeText(this, ""+HttpUrl, Toast.LENGTH_SHORT).show();
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
//                HttpUrl, new JSONObject(params), new com.android.volley.Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.d("response:", response.toString());
//                try {
//
//                    appPreferences.clear();
//                    Toast.makeText(UserProfileActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                    appPreferences.putString(AppPreferences.TABLE_ID, response.getString("id"));
//                    appPreferences.putString(AppPreferences.USER_NAME, response.getString("username"));
//                    appPreferences.putString(AppPreferences.NAME, response.getString("name"));
//                    appPreferences.putString(AppPreferences.USER_PASSWORD, response.getString("password"));
//                    appPreferences.putString(AppPreferences.USER_PHONE, response.getString("phone"));
//                    appPreferences.putString(AppPreferences.USER_EMAIL, response.getString("email"));
//                    appPreferences.putString(AppPreferences.USER_COUNTRY, response.getString("country"));
//                    appPreferences.putString(AppPreferences.PROFILE_PIC, response.getString("image"));
//
//                    editProfile.setVisibility(VISIBLE);
//                    editProfileDone.setVisibility(GONE);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue;
//        //  Creating RequestQueue.
//        requestQueue = Volley.newRequestQueue(this);
//        // Adding the StringRequest object into requestQueue.
//        requestQueue.add(jsonObjReq);


//        Map<String, String> params = new HashMap<String, String>();
//        // Adding All values to Params.
//        params.put("name", appPreferences.getString(AppPreferences.NAME));
//        params.put("username", appPreferences.getString(AppPreferences.USER_NAME));
//        params.put("password", appPreferences.getString(AppPreferences.USER_PASSWORD));
//        params.put("phone", appPreferences.getString(AppPreferences.USER_PHONE));
//        params.put("email", appPreferences.getString(AppPreferences.USER_EMAIL));
//        params.put("country", appPreferences.getString(AppPreferences.USER_COUNTRY));
//        params.put("image", image);



        RequestQueue requestQueue;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading... ");
        progressDialog.show();
        requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                       // Toast.makeText(UserProfileActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(UserProfileActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", image);
                return params;
            }

        };

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(volleyMultipartRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }


    private void uploadFile(Uri fileUri) {
        final String[] successMessage = {""};
        // create upload service client


        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getRealPathFromUri(this, fileUri));

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), file);

        // RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        //  RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "file");
        // RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());
        RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, file.getName());

        //------------------------
        long time = System.currentTimeMillis();
        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
        String path=time + "." + extension;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        mUser.setName(userNameEditText.getText().toString());
//        mUser.setCountry(locationEditText.getText().toString());
//        // mUser.setImage(file.getName());
//        mUser.setImage(time + "." + extension);

        //to delete image and store at same name
//        if (mUser.getPhone() != "Default_mobile")
//            mUser.setImage(mUser.getPhone() + "." + extension);
//        else if (mUser.getEmail() != "default_email@gmail.com") {
//            String email = mUser.getEmail();
//            mUser.setImage(email.substring(0, email.lastIndexOf("@")) + "." + extension);
//        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);

        // finally, execute the request
        Call<String> call = api.UploadImage(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   retrofit2.Response<String> response) {

               // successMessage[0] = response.body();
                Toast.makeText(UserProfileActivity.this, ""+response, Toast.LENGTH_SHORT).show();
              //  SharedPref.write("user", mUser);
                Log.v("Upload", "success");
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
