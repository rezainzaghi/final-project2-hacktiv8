package com.example.fp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminStaff extends AppCompatActivity {

    private static final String TAG = "FirestoreActivity";
    private FirebaseFirestore db;
    Button addButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff); // Pastikan layout Anda memiliki ID main_layout

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ketika tombol "Add" ditekan, arahkan ke activity AddStaffActivity
                Intent intent = new Intent(AdminStaff.this, HomeAdmin.class);
                startActivity(intent);
            }
        });

        addButton = findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ketika tombol "Add" ditekan, arahkan ke activity AddStaffActivity
                Intent intent = new Intent(AdminStaff.this, StaffRegis.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();

        // Ambil data dari Firestore
        db.collection("staff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Ambil data yang dibutuhkan
                                String documentId = document.getId();
                                String nama = document.getString("nama");
                                String email = document.getString("email");
                                String password = document.getString("password");
                                String nomorHp = document.getString("nomor_hp");
                                String alamat = document.getString("alamat");

                                // Buat kartu untuk setiap data
                                buatKartu(documentId, nama, email, password, nomorHp, alamat);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void buatKartu(String documentId, String nama, String email, String password, String nomorHp, String alamat) {
        // Buat layout untuk kartu
        CardView cardView = new CardView(this);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 16);
        cardView.setLayoutParams(layoutParams);

        // Buat layout untuk menampilkan data
        LinearLayout dataLayout = new LinearLayout(this);
        dataLayout.setOrientation(LinearLayout.VERTICAL);
        dataLayout.setPadding(16, 16, 16, 16);
        cardView.addView(dataLayout);

        // Tampilkan data dalam TextView di dalam CardView
        TextView textView = new TextView(this);
        textView.setText("Nama: " + nama + "\nEmail: " + email + "\nPassword: " + password +
                "\nNomor HP: " + nomorHp + "\nAlamat: " + alamat);
        dataLayout.addView(textView);

        // Buat tombol Delete di bawah data
        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(0, 16, 0, 0);
        btnDelete.setLayoutParams(btnParams);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hapus data dari Firestore berdasarkan documentId
                db.collection("staff").document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Jika penghapusan berhasil, hapus kartu dari tampilan
                                ((ViewGroup) cardView.getParent()).removeView(cardView);
                                Toast.makeText(AdminStaff.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                                Toast.makeText(AdminStaff.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dataLayout.addView(btnDelete);

        // Tambahkan CardView ke dalam layout utama
        LinearLayout adminLayout = findViewById(R.id.admin_layout);
        adminLayout.addView(cardView);
    }
}