package com.example.fp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrudAdmin extends AppCompatActivity {

    private Button btnAdd;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<ProdukModel> produkList;
    private ProdukAdapter produkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_admin);

        btnAdd =  findViewById(R.id.btnAdd1);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(getApplicationContext(), TambahProduk.class);
                startActivity(add);
            }
        });

        recyclerView = findViewById(R.id.recyclerviewAdmin);
        databaseReference = FirebaseDatabase.getInstance().getReference("produk");
        produkList = new ArrayList<>();
        produkAdapter = new ProdukAdapter(produkList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(produkAdapter);

        fetchDataFromFirebase();

    }
    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produkList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProdukModel produk = dataSnapshot.getValue(ProdukModel.class);
                    if (produk != null) {
                        produkList.add(produk);
                    }
                }
                produkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}