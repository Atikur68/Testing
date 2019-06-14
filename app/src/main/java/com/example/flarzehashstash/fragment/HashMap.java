package com.example.flarzehashstash.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.flarzehashstash.R;
import com.example.flarzehashstash.activity.MapsActivity;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.Hashtable;


public class HashMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener,
        LocationListener, GoogleMap.OnMarkerClickListener {


    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    View view, vi;
    Double lat, lan;
    String lati, longi;
    private RelativeLayout rootContent;
    private MapView mapView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;


    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private Marker myMarker;
    private Hashtable<String, String> markers;


    public HashMap() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hash_map, container, false);

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap = googleMap;

        markers = new Hashtable<String, String>();

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                if (arg0 != null && arg0.getTitle().equals("English")) {
//                    Intent intent1 = new Intent(MapsActivity.this, English.class);
//                    startActivity(intent1);
                    LatLng latLng = arg0.getPosition();
                    anotherMarker(latLng);
                    arg0.remove();

                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("German")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker))
                            .snippet("Click on me: Here)"));

                    Toast.makeText(getActivity(), "English", Toast.LENGTH_SHORT).show();

                }

                if (arg0 != null && arg0.getTitle().equals("German")) {
//                    Intent intent2 = new Intent(MapsActivity.this, German.class);
//                    startActivity(intent2);
                    Toast.makeText(getActivity(), "German", Toast.LENGTH_SHORT).show();
                }

                if (arg0 != null && arg0.getTitle().equals("Italian")) {
//                    Intent intent3 = new Intent(MapsActivity.this, Italian.class);
//                    startActivity(intent3);
                    Toast.makeText(getActivity(), "Italian", Toast.LENGTH_SHORT).show();
                }

                if (arg0 != null && arg0.getTitle().equals("Spanish")) {
//                    Intent intent4 = new Intent(MapsActivity.this, Spanish.class);
//                    startActivity(intent4);
                    Toast.makeText(getActivity(), "Spanish", Toast.LENGTH_SHORT).show();
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
        markers.put(english.getId(),"");

        final Marker german = mMap.addMarker(new MarkerOptions()
                .position(germany)
                .title("German")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(german.getId(),"");


        final Marker italian = mMap.addMarker(new MarkerOptions()
                .position(italy)
                .title("Italian")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(italian.getId(),"");

        final Marker spanish = mMap.addMarker(new MarkerOptions()
                .position(spain)
                .title("Spanish")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_gap_icon))
                .snippet("Click on me:)"));
        markers.put(spanish.getId(),"");

        mMap.moveCamera(CameraUpdateFactory.newLatLng(greatBritain));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(germany));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(italy));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spain));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


//        myMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("My Spot")
//                .snippet("This is my spot!")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (marker != null && marker.getTitle().equals("Spanish")) {
//                    marker.remove();
//
//                    mMap.addMarker(new MarkerOptions()
//                            .position(marker.getPosition())
//                            .title("German")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker))
//                            .snippet("Click on me: Here)"));
//                }
//                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getAddressFromLatLng(latLng));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Log.i("Location", getAddressFromLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getAddressFromLatLng(latLng));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

            }
        });
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
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


    private void anotherMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("German")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Click on me: Here)"));
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getAddressFromLatLng(latLng));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(getContext(), "Hello Sydny", Toast.LENGTH_SHORT).show();

        String name = marker.getTitle();

        if (name.equalsIgnoreCase("My Spot")) {
            Toast.makeText(getContext(), "Hello Sydny", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
