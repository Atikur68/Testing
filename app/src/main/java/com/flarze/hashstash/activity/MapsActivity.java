package com.flarze.hashstash.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextPaint;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.data.Api;
import com.flarze.hashstash.data.FriendList_adapter;
import com.flarze.hashstash.data.HashStashList;
import com.flarze.hashstash.data.Hash_List;
import com.flarze.hashstash.data.LocationList;
import com.flarze.hashstash.data.LocationListAdapter;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    View view, vi;
    Double lat, lan;
    String latitude, longitude, locationLatitude, locationLongitude;
    private RelativeLayout rootContent;
    private MapView mapView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private Marker myMarker;
    private Hashtable<String, String> markers;
    private Toolbar toolbar;
    private Button btn_hash, btn_shash, stashCaptureButton, hashCaptureButton;
    private ImageView stashEmojibtn, hashEmojibtn;
    private TextView txt_hash_start;
    private LinearLayout camera_emoji_layout, hash_button_layout, friendlist_layout_maps;
    private EmojiconEditText edt_hash_comment, edt_shash_comment;
    public String edt_hash_comment_str = "", edt_shash_comment_str = "";
    private Animation leftToRight, rightToLeft;
    private Switch hashStashSwitch;
    private TextView switchText;

    private RecyclerView recyclerView, locationlistRecycler;
    private FriendList_adapter adapter;
    private List<Hash_List> friendLists = new ArrayList<>();
    public List<String> votedHashList = new ArrayList<>();
    public List<Hash_List> popularUserId = new ArrayList<>();
    private List<LocationList> locationLists = new ArrayList<>();
    private List<HashStashList> hashStashLists = new ArrayList<>();
    private ArrayList<LatLng> locations = new ArrayList();

    private ImageView btn_share_maps, btn_world_maps, btn_friendlist_maps;
    private CircularImageView userProfilePic;
    private TextView userName, userCountry, userHashes, userStashes;
    private boolean status = true;

    public Dialog dialog, dialogMessage;
    private LocationListAdapter locationListAdapter;

    private AppPreferences appPreferences = null;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private com.google.android.gms.location.LocationListener listener;

    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */
    private LatLng latLng;
    private boolean isPermission, permissionStatus = true;
    private Location mLocation;
    private LocationManager mLocationManager;
    int hashCount = 0, stashCount = 0;
    private ProgressDialog pDialog;

    Calendar calander;

    public String Date, Time, hashOrStash, userId, switchHashStash = "hash";
    EmojIconActions emojIconHash, emojIconStash;
    final String TAG = MapsActivity.class.getSimpleName();
    private String switching = "", locationId = "", url = null;
    public static final int RequestPermissionCode = 7;
    private static final int REQUEST_CODE = 121;
    private static int RESULT_LOAD_IMG = 1;
    private Uri selectedImage;
    private String pathss = "";
    private ProgressDialog progressDialog;
    private static boolean tutorialShown = false;

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

    public String getTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity_main);

        appPreferences = new AppPreferences(this);

        if (!appPreferences.getBoolean(AppPreferences.PERMISSIONS)) {
            RequestMultiplePermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        userId = appPreferences.getString(AppPreferences.TABLE_ID);


        calander = Calendar.getInstance();
        switching = getIntent().getStringExtra("switch");
        Time = String.valueOf(calander.getTimeInMillis() / 1000);

        viewById();
        drawerInit();
        // friendListRecycler();
        locationPermission();

        emojIconHash = new EmojIconActions(this, view, edt_hash_comment, hashEmojibtn);
        emojIconHash.ShowEmojIcon();
        emojIconHash.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.happy_grey);
        emojIconHash.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");

            }
        });


        emojIconStash = new EmojIconActions(this, view, edt_shash_comment, stashEmojibtn);
        emojIconStash.ShowEmojIcon();
        emojIconStash.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.happy_grey);
        emojIconStash.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");

            }
        });

        /////////////////////////////////////////////////////

        hashCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        stashCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });


        hashStashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchText.setText("Hash");
                    switchHashStash = "hash";
                    startLocationUpdates();


                } else {
                    switchText.setText("Stash");
                    switchHashStash = "stash";
                    startLocationUpdates();

                }
            }
        });



        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, UserProfileActivity.class));
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_hash_comment_str = convertToUTF8(edt_hash_comment.getText().toString());
                hashOrStash = "hash";

                dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.location_list_layout);

                locationlistRecycler = dialog.findViewById(R.id.locationlistRecycler);
                locationlistRecycler.setHasFixedSize(false);
                locationlistRecycler.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
                locationListShown();
                dialog.show();


            }
        });

        btn_shash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_shash_comment_str = convertToUTF8(edt_shash_comment.getText().toString());
                hashOrStash = "stash";

                dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.location_list_layout);

                locationlistRecycler = dialog.findViewById(R.id.locationlistRecycler);
                locationlistRecycler.setHasFixedSize(false);
                locationlistRecycler.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
                locationListShown();
                dialog.show();
            }
        });
/*

*/

        if (tutorialShown == false) {
            ShowcaseView viewHash = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(txt_hash_start))
                    .setContentText("Write a short message (hash) of your experience to the world ... ")
                    .setStyle(3)
                    .build();

            ShowcaseView viewSwitch = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(hashStashSwitch))
                    .setContentText("Toggle between hashes- public messages- and stashes- your private messages- around you ...")
                    .setStyle(3)
                    .build();
            viewSwitch.hide();

            ShowcaseView viewStash = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(btn_shash))
                    .setContentText("Share your experience privately between you and the venue hosts ...")
                    .setStyle(3)
                    .build();
            viewStash.hide();

            viewHash.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     viewHash.hide();
                     viewSwitch.show();
                     viewSwitch.overrideButtonClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewSwitch.hide();
                            viewStash.show();
                            tutorialShown = true;
                        }
                    });
                }
            });
        }

        txt_hash_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_hash_start.setVisibility(View.GONE);
                camera_emoji_layout.setVisibility(View.VISIBLE);
                hash_button_layout.setVisibility(View.VISIBLE);
                edt_hash_comment.setVisibility(View.VISIBLE);
            }
        });

        btn_share_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        btn_world_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                //intent.putExtra("switch", "hash");
                //startActivity(intent);

                startLocationUpdates();
            }
        });


        leftToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        rightToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);

        btn_friendlist_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friendListRecycler();
                friendListRecycler();

                if (status == true) {

                    friendlist_layout_maps.setVisibility(View.VISIBLE);
                    friendlist_layout_maps.startAnimation(leftToRight);
                    status = false;

                    // getUserImageFromPopular();
                } else {
                    friendlist_layout_maps.startAnimation(rightToLeft);
                    status = true;
                    friendlist_layout_maps.setVisibility(View.GONE);
                }

            }
        });

        UserHashCount();
        UserStashCount();
        getUserVote();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathss = cursor.getString(columnIndex);
                Toast.makeText(this, pathss.toString(), Toast.LENGTH_LONG).show();
                cursor.close();
                File imgFile = new File(pathss);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //  image = getStringImage(myBitmap);
                    //  Toast.makeText(UserProfileActivity.this, "" + image, Toast.LENGTH_SHORT).show();
//                    profile_image.setImageBitmap(myBitmap);
//                    editProfile.setVisibility(GONE);
//                    editProfileDone.setVisibility(VISIBLE);

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


    private void locationPermission() {

        //  if(requestSinglePermission()) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone
        //   }

    }

    private void drawerInit() {
        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_maps);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // set username and pic on nav drawer
        View hView = navigationView.getHeaderView(0);
        userProfilePic = hView.findViewById(R.id.userProfilePic);
        userName = hView.findViewById(R.id.userName);
        userHashes = hView.findViewById(R.id.hashes_count);
        userStashes = hView.findViewById(R.id.stashes_count);
        userCountry = hView.findViewById(R.id.userCountry);

        if (!appPreferences.getString(AppPreferences.PROFILE_PIC).contains("images")) {
            userProfilePic.setImageResource(R.drawable.demoman);
        } else {
            // String imageValues = appPreferences.getString(AppPreferences.PROFILE_PIC).substring(47);
            String imageValues = appPreferences.getString(AppPreferences.PROFILE_PIC);
            String imageValue = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/" + imageValues;
            Picasso.with(this).load(imageValue).into(userProfilePic);

        }
        userName.setText(appPreferences.getString(AppPreferences.USER_NAME));
        userCountry.setText(appPreferences.getString(AppPreferences.USER_COUNTRY));

    }

    private void friendListRecycler() {

        for (int j = 0; j < popularUserId.size(); j++) {

            String popUserId = popularUserId.get(j).getVotedHashId();
            String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + popUserId;

            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(MapsActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                friendLists.clear();
                                final JSONObject jsonObject = new JSONObject(response);

                                String popularUsersImage = jsonObject.getString("image");
                                String popularUsersName = jsonObject.getString("username");
                                friendLists.add(new Hash_List(popularUsersImage, popularUsersName));

                                // Toast.makeText(MapsActivity.this, ""+popularUsersImage+"  ..."+popularUsersName, Toast.LENGTH_SHORT).show();
                                adapter = new FriendList_adapter(getApplicationContext(), friendLists);
                                recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                // pDialog.hide();
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                        }
                    });

            // Creating RequestQueue.
            requestQueue = Volley.newRequestQueue(MapsActivity.this);
            // Adding the StringRequest object into requestQueue.
            requestQueue.add(stringRequest);

            // }
        }


        //  friendLists.add(new Hash_List(R.drawable.newfriend_icon, "New"));


    }

    private void viewById() {
        toolbar = findViewById(R.id.toolbar_maps);
        btn_hash = findViewById(R.id.btn_hash);
        btn_shash = findViewById(R.id.btn_shash);
        txt_hash_start = findViewById(R.id.txt_hash_start);
        camera_emoji_layout = findViewById(R.id.camera_emoji_layout);
        hash_button_layout = findViewById(R.id.hash_button_layout);
        edt_hash_comment = findViewById(R.id.edt_hash_comment);
        edt_shash_comment = findViewById(R.id.edt_shash_comment);
        recyclerView = findViewById(R.id.friendlist_recycler);
        btn_friendlist_maps = findViewById(R.id.btn_friendlist_maps);
        btn_world_maps = findViewById(R.id.btn_world_maps);
        btn_share_maps = findViewById(R.id.btn_share_maps);
        friendlist_layout_maps = findViewById(R.id.friendlist_layout_maps);
        rootContent = (RelativeLayout) findViewById(R.id.root_content);
        hashStashSwitch = findViewById(R.id.hashStashSwitch);
        switchText = findViewById(R.id.switchText);
        stashCaptureButton = findViewById(R.id.stashCaptureButton);
        hashCaptureButton = findViewById(R.id.hashCaptureButton);
        stashEmojibtn = findViewById(R.id.stashEmojibtn);
        hashEmojibtn = findViewById(R.id.hashEmojibtn);
        view = findViewById(R.id.root_content);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void mapInit() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void locationListShown() {

        String HttpUrl = "https://api.tomtom.com/search/2/search/store.json?key=7eIbPrsJGRDCSQzW4tSaa7N3nDtH03lj&lat=" + latitude + "&lon=" + longitude + "&radius=2000";
        // String HttpUrl = "https://api.tomtom.com/search/2/search/food.json?key=7eIbPrsJGRDCSQzW4tSaa7N3nDtH03lj&lat=23.777176&lon=90.399452&radius=100000";

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("results")) {

                            try {
                                JSONObject obj = new JSONObject(response);

                                JSONArray heroArray = obj.optJSONArray("results");
                                locationLists.clear();
                                //locationLists.add(new LocationList(R.mipmap.ic_launcher, "Shikerpur", "22.8151625", "90.0611301", "123459"));

                                for (int i = 0; i < heroArray.length(); i++) {

                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    String heroArrays = heroObject.getString("poi");


                                    JSONObject heroObjectPosition = heroArray.getJSONObject(i);
                                    String heroArraysPosition = heroObjectPosition.getString("position");

                                    JSONObject object = new JSONObject(heroArrays);
                                    String locationName = object.getString("name");

                                    JSONObject objectCatagory = new JSONObject(heroArrays);
                                    String locationCatagory = objectCatagory.getString("categorySet");

                                    JSONObject objectPosition = new JSONObject(heroArraysPosition);
                                    String locationPositionLat = objectPosition.getString("lat");
                                    String locationPositionLon = objectPosition.getString("lon");


                                    locationLists.add(new LocationList(R.mipmap.ic_launcher, locationName, locationPositionLat, locationPositionLon, "123459"));

                                }

                                locationListAdapter = new LocationListAdapter(locationLists, MapsActivity.this);
                                locationlistRecycler.setAdapter(locationListAdapter);

                            } catch (JSONException e) {
                                // pDialog.hide();
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(MapsActivity.this, "not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            //startActivity(new Intent(MapsActivity.this, SigninActivity.class));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_hashmap) {
            // fragment = new HashMap();
            //   startActivity(new Intent(this,MapsActivity.class));
        } else if (id == R.id.nav_slideshow) {
            // fragment = new WelcomeFragment();
            startActivity(new Intent(this, SlideShowActivity.class));
        } else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com")));
        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("main", "invite friends");
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com")));
        } else if (id == R.id.nav_logout) {
            appPreferences.clear();
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }


        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (latLng != null) {
            // mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        markers = new Hashtable<String, String>();

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra("main", "hash list");
                intent.putExtra("latitude", locationLatitude);
                intent.putExtra("longitude", locationLongitude);
                intent.putExtra("userId", userId);
                intent.putExtra("hashOrStash", switchHashStash);
                intent.putStringArrayListExtra("votedHashes", (ArrayList<String>) votedHashList);
                startActivity(intent);

            }


        });

        getHashorStashWithinKm();


//////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    private void getHashorStashWithinKm() {

        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + switchHashStash + "/coordinates/" + latitude + "/" + longitude + "/2.0";
        // String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/5/hash-or-stash/hash/coordinates/22.84564/89.540329/2.0";
        // Toast.makeText(this, "" + HttpUrl, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, HttpUrl, (JSONArray) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {

                    hashStashLists.clear();
                    locations.clear();
                    mMap.clear();
                    markers.clear();

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject hashStash = response.getJSONObject(i);

                        String hashStashId = hashStash.getString("id");
                        String hashStashComments = hashStash.getString("comments");
                        String hashStashcmtTime = hashStash.getString("cmtTime");
                        String hashStashlocation = hashStash.getString("location");
                        String hashStashlocationId = hashStash.getString("locationId");
                        String hashStashlatitude = hashStash.getString("latitude");
                        String hashStashlongitude = hashStash.getString("longitude");
                        String hashStashduration = hashStash.getString("duration");
                        String hashStashUserImage = hashStash.getString("image");

                        Double lat = Double.parseDouble(hashStash.getString("latitude"));
                        Double lan = Double.parseDouble(hashStash.getString("longitude"));

                        locations.add(new LatLng(lat, lan));
                        Marker mark = mMap.addMarker(new MarkerOptions()
                                .position(locations.get(i))
                                .title("" + i)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                                .snippet("Click on me:)"));
                        markers.put(mark.getId(), "" + i);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(i)));

                        hashStashLists.add(new HashStashList(hashStashId, hashStashComments, hashStashcmtTime, hashStashlocation, hashStashlocationId, hashStashlatitude, hashStashlongitude, hashStashduration, hashStashUserImage));

                    }

                } catch (JSONException e) {
                    // pDialog.hide();
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsArrayRequest);


    }

    @Override
    public void onLocationChanged(Location location) {

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        getHashorStashWithinKm();
        getUserFromPopular();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // .setInterval(UPDATE_INTERVAL)
        // .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        getHashorStashWithinKm();
        // getUserFromPopular();


    }


    public void dialogDismiss(String hashStashId, String selector) {
        dialog.dismiss();

        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/hash-or-stash/" + hashStashId + "/";
        // String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + appPreferences.getString(AppPreferences.TABLE_ID) + "/";
        if (selectedImage != null) {
            uploadFile(selectedImage, HttpUrl, selector);

        } else {
            progressDialog = new ProgressDialog(MapsActivity.this);
            if (selector.contentEquals("HASH")) {
                progressDialog.setMessage("Sharing your hash with people nearby for two minutes"); // Setting Message
                // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reloadActivity();

                        }
                    });
                }
            }).start();


        }
    }

    private void uploadFile(Uri fileUri, String HttpUrl, String selector) {
        final String[] successMessage = {""};
        // create upload service client
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getRealPathFromUri(MapsActivity.this, fileUri));

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
        String path = time + "." + extension;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);

        // finally, execute the request
        Call<String> call = api.UploadImage(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                // successMessage[0] = response.body();
                Toast.makeText(MapsActivity.this, "success" + response, Toast.LENGTH_SHORT).show();
                //  SharedPref.write("user", mUser);
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }

        });


        if (selector.contentEquals("HASH")) {
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Sharing your hash with people nearby for two minutes"); // Setting Message
            // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reloadActivity();

                    }
                });
            }
        }).start();


    }

    private void reloadActivity() {
        progressDialog.dismiss();
        //startActivity(new Intent(this, this.getClass()));
        startLocationUpdates();
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    /*  Show screenshot Bitmap */

    private void takeScreenshot() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;

                OutputStream fout = null;

                String filePath = System.currentTimeMillis() + ".jpeg";

                try {
                    fout = openFileOutput(filePath, Context.MODE_PRIVATE);

                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }

                openShareImageDialog(filePath);
            }
        };

        mMap.snapshot(callback);
    }

    public void openShareImageDialog(String filePath) {
        File file = this.getFileStreamPath(filePath);

        if (!filePath.equals("")) {
            final ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            final Uri contentUriFile = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(android.content.Intent.EXTRA_STREAM, contentUriFile);
            startActivity(Intent.createChooser(intent, "Share Image"));
        } else {
            //This is a custom class I use to show dialogs...simply replace this with whatever you want to show an error message, Toast, etc.
            //  DialogUtilities.showOkDialogWithText(this, R.string.shareImageFailed);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_marker_layout,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null
                    && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            marker = marker;


            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final CircularImageView image = ((CircularImageView) view.findViewById(R.id.profile_pic));
            final TextView storeName = view.findViewById(R.id.storeName);
            final TextView popularHash = view.findViewById(R.id.popularHash);


            //  image.setImageResource(R.drawable.demoman);
            String imageValues = hashStashLists.get(Integer.parseInt(url)).getHashStashUserImage();
            String imageValue = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/" + imageValues;
            Picasso.with(MapsActivity.this).load(imageValue).into(image);
            String markerInfo = marker.getId().substring(1);

            storeName.setText(hashStashLists.get(Integer.parseInt(url)).getHashStashlocation());
            popularHash.setText(hashStashLists.get(Integer.parseInt(url)).getHashStashComments());


            locationId = hashStashLists.get(Integer.parseInt(url)).getHashStashlocationId();
            locationLatitude = hashStashLists.get(Integer.parseInt(url)).getHashStashlatitude();
            locationLongitude = hashStashLists.get(Integer.parseInt(url)).getHashStashlongitude();


            return view;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Single Permission is granted
                        //  Toast.makeText(MapsActivity.this, "Single permission is granted!", Toast.LENGTH_SHORT).show();
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return isPermission;

    }


    private String convertToUTF8(String str) {
        byte[] byteArray = str.getBytes(UTF_8);
        return new String(byteArray, UTF_8);
    }

    //Permission function starts from here
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                {
                        ACCESS_FINE_LOCATION,
                        WRITE_EXTERNAL_STORAGE,
                        READ_CONTACTS
                        // READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean SendSMSPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    // boolean GetAccountsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && RecordAudioPermission && SendSMSPermission) {

                        Toast.makeText(MapsActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        appPreferences.putBoolean(AppPreferences.PERMISSIONS, true);
                        startActivity(new Intent(this, this.getClass()));

                    } else {
                        Toast.makeText(MapsActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }

    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        // int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void UserHashCount() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/hashes";
        final JsonArrayRequest jsonObjReq1 = new JsonArrayRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    hashCount++;
                }
                appPreferences.putString(AppPreferences.HASHES, "" + hashCount);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        requestQueue.add(jsonObjReq1);
        userHashes.setText(appPreferences.getString(AppPreferences.HASHES));


    }

    private void UserStashCount() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/stashes";
        final JsonArrayRequest jsonObjReq1 = new JsonArrayRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    stashCount++;
                }
                appPreferences.putString(AppPreferences.STASHES, "" + stashCount);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        requestQueue.add(jsonObjReq1);

        userStashes.setText(appPreferences.getString(AppPreferences.STASHES));
    }

    public void getUserVote() {
        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/votes";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, HttpUrl, (JSONArray) null, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            votedHashList.clear();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject hashStash = response.getJSONObject(i);
                                String votedHashStashId = hashStash.getString("hashStashId");
                                votedHashList.add(votedHashStashId);
                            }

                        } catch (JSONException e) {
                            // pDialog.hide();
                            e.printStackTrace();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                // Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                            }
                        });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsArrayRequest);

    }

    public void getUserFromPopular() {
        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/hash-or-stash/locations/" + latitude + "/" + longitude + "/populars/radius/1";
        // Toast.makeText(this, "" + HttpUrl, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, HttpUrl, (JSONArray) null, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            popularUserId.clear();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject hashStash = response.getJSONObject(i);
                                String popularUsersId = hashStash.getString("userId");
                                popularUserId.add(new Hash_List(popularUsersId));
                            }

                        } catch (JSONException e) {
                            // pDialog.hide();
                            e.printStackTrace();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                // Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                            }
                        });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsArrayRequest);


    }

    public void getUserImageFromPopular() {
        //  if (popularUserId.size() > 0) {
        // for (int j = 0; j < popularUserId.size(); j++) {

        // String popUserId = popularUserId.get(j);
        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/2";

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            final JSONObject jsonObject = new JSONObject(response);

                            String popularUsersImage = jsonObject.getString("image");
                            String popularUsersName = jsonObject.getString("username");
                            //  friendLists.add(new Hash_List(popularUsersImage, popularUsersName));

                            Toast.makeText(MapsActivity.this, "" + popularUsersImage + "  ..." + popularUsersName, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            // pDialog.hide();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MapsActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        //  }


        //  }
    }
}
