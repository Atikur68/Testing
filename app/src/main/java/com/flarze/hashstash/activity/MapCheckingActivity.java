package com.flarze.hashstash.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.flarze.hashstash.R;

public class MapCheckingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }
}
