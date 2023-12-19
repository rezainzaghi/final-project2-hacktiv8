package com.example.fp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeUser extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<ProdukModel> hotProductList = new ArrayList<>();
    private HotProductAdapter hotProductAdapter;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        LinearLayout btnprofileUser = findViewById(R.id.userProfil);
        recyclerView = findViewById(R.id.rv_hot_item);
        recyclerView.setHasFixedSize(true);

        btnprofileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeUser.this, ProfileUser.class);
                startActivity(intent);
            }
        });

        //Untuk Bentuk List
        //LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());

        //Untuk Bentuk Grid
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        hotProductList = new ArrayList<>();
        hotProductAdapter = new HotProductAdapter(getApplicationContext(), hotProductList);
        recyclerView.setAdapter(hotProductAdapter);

        databaseReference = FirebaseRealtimeDatabaseUtils.getRefrence(FirebaseRealtimeDatabaseUtils.PRODUCT_PATH);
        databaseReference.orderByChild("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotProductList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProdukModel produkModel = dataSnapshot.getValue(ProdukModel.class);
                    hotProductList.add(produkModel);
                }
                hotProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}