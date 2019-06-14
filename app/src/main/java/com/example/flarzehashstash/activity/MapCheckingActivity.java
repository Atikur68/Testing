package com.example.flarzehashstash.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.flarzehashstash.R;

public class MapCheckingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }
}
