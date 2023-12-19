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

public class HotProductAdapter extends RecyclerView.Adapter<HotProductAdapter.ListViewHolder> {
    private final Context context;
    private final List<ProdukModel> list;


    public HotProductAdapter(Context context, List<ProdukModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HotProductAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HotProductAdapter.ListViewHolder holder, int position) {
        holder.nameProduct.setText(list.get(position).getNamaProduk());
        holder.priceProduct.setText("Rp"+list.get(position).getPrice());
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct, priceProduct;
        ImageView imageProduct;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.name_product);
            priceProduct = itemView.findViewById(R.id.price_product);
            imageProduct = itemView.findViewById(R.id.image_product);
        }
    }
}

