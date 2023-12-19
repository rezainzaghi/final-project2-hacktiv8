package com.example.fp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashScreen2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
    }

    public void onNewUserClick(View view) {
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    public void onAlreadyMemberClick(View view) {
        Intent intent = new Intent(this, LoginUser.class);
        startActivity(intent);
    }
    public void onAdminLoginPage(View view) {
        Intent intent = new Intent(this, LoginAdmin.class);
        startActivity(intent);
    }

    public void onStaffLoginPage(View view) {
        Intent intent = new Intent(this, LoginStaff.class);
        startActivity(intent);
    }
}