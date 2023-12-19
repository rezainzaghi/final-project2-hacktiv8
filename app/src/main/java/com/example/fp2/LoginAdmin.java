package com.example.fp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginAdmin extends AppCompatActivity {

    private Button btnLoginAdmin;
    private EditText usernameAdminEt, passwordAdminEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        usernameAdminEt = findViewById(R.id.etEmailAdmin);
        passwordAdminEt = findViewById(R.id.etPasswordAdmin);


        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameAdminEt.getText().toString();
                String password = passwordAdminEt.getText().toString();

                if (username.equals("admin") && password.equals("admin")){

                    Toast.makeText(getApplicationContext(), "Login Admin Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), HomeAdmin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}