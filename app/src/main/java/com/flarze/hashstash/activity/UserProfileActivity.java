package com.flarze.hashstash.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.flarze.hashstash.R;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolBarProfile;
    private GestureDetectorCompat gestureDetectorCompat = null;
    private CircularImageView profile_image;
    private AppPreferences appPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        appPreferences = new AppPreferences(this);

        toolBarProfile = findViewById(R.id.toolBarProfile);
        profile_image = findViewById(R.id.profile_image);

        Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(profile_image);


        setSupportActionBar(toolBarProfile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
