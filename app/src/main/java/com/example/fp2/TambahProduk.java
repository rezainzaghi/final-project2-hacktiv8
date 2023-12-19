package com.example.fp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class TambahProduk extends AppCompatActivity {

    private EditText namaProdukEt, detailProdukEt, namaFileEt;
    private Button btnPilihGambar, btnSimpan;
    private String selectedCategory = "";
    private Uri imageUri;

    private DatabaseReference database;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        namaProdukEt = findViewById(R.id.namaProdukEt);
        namaFileEt = findViewById(R.id.namaFileEt);
        detailProdukEt = findViewById(R.id.detailProduk);
        btnPilihGambar = findViewById(R.id.btnPilihFile);
        btnSimpan = findViewById(R.id.btnAddProduk);
        database = FirebaseDatabase.getInstance().getReference("produk");
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fproject2-73b4c.appspot.com");

        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        List<String> categories = new ArrayList<>();
        categories.add("Book");
        categories.add("Electronics");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle nothing selected if needed
            }
        });

        btnPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Panggil metode untuk memilih gambar dari penyimpanan perangkat
                openFileChooser();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaProduk = namaProdukEt.getText().toString();
                String detail = detailProdukEt.getText().toString();
                String namaFile = namaFileEt.getText().toString();

                if (namaProduk.isEmpty() || detail.isEmpty() || selectedCategory.isEmpty() || namaFile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Masih ada yang kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    // Panggil metode untuk mengunggah gambar ke Firebase Storage
                    uploadFile(namaProduk, namaFile, detail);
                }
            }
        });
    }

    // Metode untuk memilih gambar dari penyimpanan perangkat
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Metode untuk menangani hasil dari pemilihan gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.imageUri = data.getData();

            // Dapatkan nama file dari URI gambar yang dipilih
            String fileName = getFileName(imageUri);

            // Set nama file ke EditText
            namaFileEt.setText(fileName);
        }
    }

    // Metode untuk mendapatkan nama file dari URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // Metode untuk mengunggah gambar ke Firebase Storage
    private void uploadFile(String namaProduk, String namaFile, String detail) {
        StorageReference fileReference = storageReference.child("images/" + namaFile);

        // Cek apakah nama file sudah digunakan
        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Jika URI ditemukan, berarti nama file sudah ada
                Toast.makeText(getApplicationContext(), "Nama file sudah digunakan, silakan pilih nama file yang berbeda.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Jika URI tidak ditemukan, berarti nama file belum ada, maka lakukan pengunggahan
                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Dapatkan URL unduhan gambar
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        // Ambil quantity dari input atau beri default value (misalnya 0)
                                        int quantity = 0;

                                        // Simpan data ke Firebase Database
                                        DatabaseReference produkRef = database.child(namaProduk);
                                        produkRef.child("namaProduk").setValue(namaProduk);
                                        produkRef.child("category").setValue(selectedCategory);
                                        produkRef.child("detail").setValue(detail);
                                        produkRef.child("imageUrl").setValue(imageUrl);
                                        produkRef.child("quantity").setValue(quantity);

                                        Toast.makeText(getApplicationContext(), "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                                        // Kosongkan input setelah simpan berhasil
                                        namaProdukEt.setText("");
                                        namaFileEt.setText("");
                                        detailProdukEt.setText("");

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}