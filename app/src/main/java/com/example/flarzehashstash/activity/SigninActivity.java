package com.example.flarzehashstash.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.flarzehashstash.R;

public class SigninActivity extends AppCompatActivity {


    Button btn_signin,btn_sign_instragram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      //  LinearLayout layout=(LinearLayout) findViewById(R.id.signingLayout);
      //  layout.setBackgroundResource(R.drawable.signing_background);

        btn_signin=findViewById(R.id.btn_sign);
        btn_sign_instragram=findViewById(R.id.btn_sign_instragram);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, MapsActivity.class));
            }
        });

        btn_sign_instragram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SigninActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
