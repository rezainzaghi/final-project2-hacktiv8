package com.example.fp2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StaffRegis extends AppCompatActivity {

    // Deklarasi variabel
    EditText editTextNama, editTextEmail, editTextPassword, editTextNomorHp, editTextAlamat;
    Button submitButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_regis);

        // Inisialisasi FirebaseAuth dan FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inisialisasi tampilan
        editTextNama = findViewById(R.id.editTextNama);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNomorHp = findViewById(R.id.editTextNomorHp);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        submitButton = findViewById(R.id.submit);

        // Mengatur OnClickListener untuk tombol submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Jika user berhasil terautentikasi, lanjutkan dengan menyimpan data ke Firestore
                        saveToFirestore(editTextNama.getText().toString().trim(),
                                editTextEmail.getText().toString().trim(),
                                editTextPassword.getText().toString().trim(),
                                editTextNomorHp.getText().toString().trim(),
                                editTextAlamat.getText().toString().trim());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Jika terjadi kesalahan saat autentikasi
                        Toast.makeText(getApplicationContext(), "Gagal mendaftar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToFirestore(String nama, String email, String password, String nomorHp, String alamat) {
        Map<String, Object> user = new HashMap<>();
        user.put("nama", nama);
        user.put("email", email);
        user.put("password", password);
        user.put("nomor_hp", nomorHp);
        user.put("alamat", alamat);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        db.collection("staff")
                .document(firebaseUser.getUid()) // Menggunakan UID dari pengguna sebagai ID dokumen
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Jika berhasil disimpan
                        Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan ke Firestore", Toast.LENGTH_SHORT).show();

                        // Redirect ke halaman login
                        Intent intent = new Intent(getApplicationContext(), AdminStaff.class);
                        startActivity(intent);
                        finish(); // Optional: Menutup aktivitas saat ini agar tidak kembali ke halaman registrasi saat tombol back ditekan
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Jika terjadi kesalahan saat menyimpan
                        Toast.makeText(getApplicationContext(), "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "EROR" + e.getMessage());
                    }
                });
    }
}
