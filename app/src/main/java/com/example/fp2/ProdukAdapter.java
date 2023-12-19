package com.example.fp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>{
    private List<ProdukModel> produkList;

    public ProdukAdapter(List<ProdukModel> produkList) {
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_admin, parent, false);
        return new ProdukViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTv, quantityProdukTv, categoryTv;
        ImageView produkImg;
        Button plusQuantityBtn, minQuantityBtn, deleteProdukBtn;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTv = itemView.findViewById(R.id.namaProdukTv);
            quantityProdukTv = itemView.findViewById(R.id.quantityProduk); // Tambahkan ini
            produkImg = itemView.findViewById(R.id.ProdukImg);
            plusQuantityBtn = itemView.findViewById(R.id.plusQuantity);
            minQuantityBtn = itemView.findViewById(R.id.minQuantity);
            deleteProdukBtn = itemView.findViewById(R.id.deleteProduk);
            categoryTv = itemView.findViewById(R.id.category);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        ProdukModel produk = produkList.get(position);

        holder.namaProdukTv.setText(produk.getNamaProduk());
        holder.quantityProdukTv.setText(String.valueOf(produk.getQuantity()));
        holder.categoryTv.setText(String.valueOf(produk.getCategory()));// Tambahkan ini

        // Menggunakan Glide atau library lain untuk menampilkan gambar dari URL
        Glide.with(holder.itemView.getContext())
                .load(produk.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.produkImg);

        // ... (kode lainnya tetap sama)

        holder.plusQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = produk.getQuantity() + 1;
                produk.setQuantity(newQuantity);
                holder.quantityProdukTv.setText(String.valueOf(newQuantity));

                // Update jumlah produk di Firebase
                updateQuantityInFirebase(produk.getNamaProduk(), newQuantity);
            }
        });

        holder.minQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = produk.getQuantity();
                if (currentQuantity > 0) {
                    int newQuantity = currentQuantity - 1;
                    produk.setQuantity(newQuantity);
                    holder.quantityProdukTv.setText(String.valueOf(newQuantity));

                    // Update jumlah produk di Firebase
                    updateQuantityInFirebase(produk.getNamaProduk(), newQuantity);
                }
            }
        });

        holder.deleteProdukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hapus produk dari Firebase
                deleteProdukFromFirebase(holder.itemView.getContext(), produk.getNamaProduk(), produk.getImageUrl());
            }
        });
    }

    private void updateQuantityInFirebase(String namaProduk, int newQuantity) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("produk");
        databaseReference.child(namaProduk).child("quantity").setValue(newQuantity);
    }

    private void deleteProdukFromFirebase(Context context, String namaProduk, String imageUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("produk");
        databaseReference.child(namaProduk).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Hapus gambar dari Firebase Storage
                        deleteImageFromStorage(context, imageUrl);
                        Toast.makeText(context, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Gagal menghapus produk", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteImageFromStorage(Context context, String imageUrl) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Gambar dihapus dari Storage
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Gagal menghapus gambar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

