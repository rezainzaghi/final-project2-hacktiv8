package com.example.fp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen1 extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 5000; // Waktu penundaan dalam milidetik (5 detik)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen1.this, SplashScreen2.class);
                startActivity(intent);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}