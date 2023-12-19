package com.example.fp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userNameTV, userEmailTV, userPasswordTV;
    private Button btnLogOut;
    private LinearLayout btnHomeUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        userNameTV = findViewById(R.id.NamaUser);
        userEmailTV = findViewById(R.id.emailUser);
        userPasswordTV = findViewById(R.id.passwordUser);
        btnLogOut = findViewById(R.id.btn_LogOut);
        btnHomeUser = findViewById(R.id.homeUser);

        btnHomeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileUser.this, HomeUser.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(view -> logout());

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            userEmailTV.setText(email);

            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nama = document.getString("nama");
                        String password = document.getString("password");

                        userNameTV.setText(nama);
                        userPasswordTV.setText(password);
                    }
                }
            });
        }
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(ProfileUser.this, LoginUser.class));
        finish();
    }
}

