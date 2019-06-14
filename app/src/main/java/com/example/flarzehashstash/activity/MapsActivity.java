package com.example.flarzehashstash.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.flarzehashstash.R;
import com.example.flarzehashstash.data.FriendList_adapter;
import com.example.flarzehashstash.data.Hash_List;
import com.example.flarzehashstash.data.Hash_adapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private GoogleMap mMap;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    View view, vi;
    Double lat, lan;
    String lati, longi;
    private RelativeLayout rootContent;
    private MapView mapView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private Marker myMarker;
    private Hashtable<String, String> markers;
    private Toolbar toolbar;
    private Button btn_hash;
    private TextView txt_hash_start;
    private LinearLayout camera_emoji_layout, hash_button_layout, friendlist_layout_maps;
    private EditText edt_hash_comment;
    private Animation leftToRight, rightToLeft;

    RecyclerView recyclerView;
    FriendList_adapter adapter;
    List<Hash_List> friendLists = new ArrayList<>();

    private ImageView btn_share_maps, btn_world_maps, btn_friendlist_maps;
    private CircularImageView userProfilePic;
    private boolean status = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //  getSupportActionBar().hide();
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = findViewById(R.id.toolbar_maps);
        // setSupportActionBar(toolbar);

        btn_hash = findViewById(R.id.btn_hash);
        txt_hash_start = findViewById(R.id.txt_hash_start);
        camera_emoji_layout = findViewById(R.id.camera_emoji_layout);
        hash_button_layout = findViewById(R.id.hash_button_layout);
        edt_hash_comment = findViewById(R.id.edt_hash_comment);
        recyclerView = findViewById(R.id.friendlist_recycler);
        btn_friendlist_maps = findViewById(R.id.btn_friendlist_maps);
        btn_world_maps = findViewById(R.id.btn_world_maps);
        btn_share_maps = findViewById(R.id.btn_share_maps);
        friendlist_layout_maps = findViewById(R.id.friendlist_layout_maps);
        //   userProfilePic = findViewById(R.id.userProfilePic);

        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_maps);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        userProfilePic = hView.findViewById(R.id.userProfilePic);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false));


        friendLists.add(new Hash_List(R.drawable.newfriend_icon, "New"));
        friendLists.add(new Hash_List(R.drawable.demoman, "You"));
        friendLists.add(new Hash_List(R.drawable.demoman, "John"));
        friendLists.add(new Hash_List(R.drawable.demoman, "Atik"));
        adapter = new FriendList_adapter(this, friendLists);
        recyclerView.setAdapter(adapter);


        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, UserProfileActivity.class));
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

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
                Toast.makeText(MapsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btn_world_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        leftToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        rightToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);

        btn_friendlist_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == true) {
                    friendlist_layout_maps.setVisibility(View.VISIBLE);
                    friendlist_layout_maps.startAnimation(leftToRight);
                    status = false;
                } else {
                    friendlist_layout_maps.startAnimation(rightToLeft);
                    status = true;
                    friendlist_layout_maps.setVisibility(View.GONE);
                }
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com")));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, SigninActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.maps_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markers = new Hashtable<String, String>();

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                if (arg0 != null && arg0.getTitle().equals("English")) {
//                    Intent intent1 = new Intent(MapsActivity.this, English.class);
//                    startActivity(intent1);
//                    LatLng latLng = arg0.getPosition();
//                    anotherMarker(latLng);
//                    arg0.remove();

//                    mMap.addMarker(new MarkerOptions()
//                            .position(arg0.getPosition())
//                            .title("German")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker))
//                            .snippet("Click on me: Here)"));

                    //    Toast.makeText(MapsActivity.this, "English", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MapsActivity.this, MainActivity.class));


                }

                if (arg0 != null && arg0.getTitle().equals("German")) {
//                    Intent intent2 = new Intent(MapsActivity.this, German.class);
//                    startActivity(intent2);
                    //  Toast.makeText(MapsActivity.this, "German", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                }

                if (arg0 != null && arg0.getTitle().equals("Italian")) {
//                    Intent intent3 = new Intent(MapsActivity.this, Italian.class);
//                    startActivity(intent3);
                    //  Toast.makeText(MapsActivity.this, "Italian", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                }

                if (arg0 != null && arg0.getTitle().equals("Spanish")) {
//                    Intent intent4 = new Intent(MapsActivity.this, Spanish.class);
//                    startActivity(intent4);
                    // Toast.makeText(MapsActivity.this, "Spanish", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                }
            }


        });
        LatLng greatBritain = new LatLng(51.30, -0.07);
        LatLng germany = new LatLng(52.3107, 13.2430);
        LatLng italy = new LatLng(41.53, 12.29);
        LatLng spain = new LatLng(40.25, -3.41);
        final Marker english = mMap.addMarker(new MarkerOptions()
                .position(greatBritain)
                .title("English")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(english.getId(), "");

        final Marker german = mMap.addMarker(new MarkerOptions()
                .position(germany)
                .title("German")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(german.getId(), "");


        final Marker italian = mMap.addMarker(new MarkerOptions()
                .position(italy)
                .title("Italian")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(italian.getId(), "");

        final Marker spanish = mMap.addMarker(new MarkerOptions()
                .position(spain)
                .title("Spanish")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(spanish.getId(), "");

        mMap.moveCamera(CameraUpdateFactory.newLatLng(greatBritain));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(germany));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(italy));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spain));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getAddressFromLatLng(latLng));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final CircularImageView image = ((CircularImageView) view.findViewById(R.id.profile_pic));


            image.setImageResource(R.drawable.demoman);


            return view;
        }
    }
}
