package com.flarze.hashstash.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flarze.hashstash.R;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.flarze.hashstash.fragment.HashFragment;
import com.flarze.hashstash.fragment.InviteFriend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btn_check;
    private ImageView userProfilePic;
    private String navOption;
    private AppPreferences appPreferences = null;
    private TextView userName,userCountry,userHashes,userStashes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        appPreferences = new AppPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            navOption = bundle.getString("main");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        userProfilePic = hView.findViewById(R.id.userProfilePic);
        userName=hView.findViewById(R.id.userName);
        userHashes = hView.findViewById(R.id.hashes_count);
        userStashes = hView.findViewById(R.id.stashes_count);
        userCountry = hView.findViewById(R.id.userCountry);

        if(appPreferences.getString(AppPreferences.PROFILE_PIC).contains("")){
            userProfilePic.setImageResource(R.drawable.demoman);
        }else {
            String imageValues=appPreferences.getString(AppPreferences.PROFILE_PIC);
            String imageValue="http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/"+imageValues;
            Picasso.with(this).load(imageValue).into(userProfilePic);
        }
        userName.setText(appPreferences.getString(AppPreferences.USER_NAME));
        userCountry.setText(appPreferences.getString(AppPreferences.USER_COUNTRY));
        userHashes.setText(appPreferences.getString(AppPreferences.HASHES));
        userStashes.setText(appPreferences.getString(AppPreferences.STASHES));

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });

        if (navOption.contains("invite friends")) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new InviteFriend());
            ft.commit();


        }
        if (navOption.contains("hash list")) {

            Bundle bundles = getIntent().getExtras();
            String locationLatitude = getIntent().getStringExtra("latitude");
            String locationLongitude = getIntent().getStringExtra("longitude");
            String userId = getIntent().getStringExtra("userId");
            String hashOrStash = getIntent().getStringExtra("hashOrStash");
            List<String> votedHash = bundle.getStringArrayList("votedHashes");

            bundle.putString("latitude", locationLatitude );
            bundle.putString("longitude", locationLongitude );
            bundle.putString("userId", userId );
            bundle.putString("hashOrStash", hashOrStash );
            bundle.putStringArrayList("votedHashes", (ArrayList<String>)votedHash );
            HashFragment fragInfo = new HashFragment();
            fragInfo.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragInfo);
            ft.commit();


        }

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.mainFrame, new HashFragment());
//        ft.commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_hashmap) {
            // fragment = new HashMap();
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("switch","HASH");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            // fragment = new WelcomeFragment();
            startActivity(new Intent(this, SlideShowActivity.class));
        } else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com")));
        } else if (id == R.id.nav_share) {
            fragment = new InviteFriend();
        } else if (id == R.id.nav_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com")));
        } else if (id == R.id.nav_logout) {
            appPreferences.clear();
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
