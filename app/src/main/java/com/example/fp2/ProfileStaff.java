package com.example.fp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileStaff extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userNameTV, userEmailTV, userPasswordTV, userPhoneTV, userAddressTV;
    private Button btnLogOut;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_staff);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameTV = findViewById(R.id.Nama);
        userEmailTV = findViewById(R.id.email);
        userPasswordTV = findViewById(R.id.password);
        userPhoneTV = findViewById(R.id.nomorHp);
        userAddressTV = findViewById(R.id.Alamat);
        btnLogOut = findViewById(R.id.btn_LogOut);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            userEmailTV.setText(email);

            DocumentReference userRef = db.collection("staff").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nama = document.getString("nama");
                        String password = document.getString("password");
                        String phone = document.getString("nomorHp");
                        String address = document.getString("alamat");

                        userNameTV.setText(nama);
                        userPasswordTV.setText(password);
                        userPhoneTV.setText(phone);
                        userAddressTV.setText(address);

                        btnLogOut.setOnClickListener(view -> logout());
                    }
                }
            });
        }
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(ProfileStaff.this, LoginStaff.class));
        finish();
    }
}

