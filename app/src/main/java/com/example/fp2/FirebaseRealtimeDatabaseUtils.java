package com.example.fp2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRealtimeDatabaseUtils {
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase
            .getInstance("https://fproject2-73b4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    public  static final String PRODUCT_PATH = "produk";

    public static DatabaseReference getRefrence(String path){
        return firebaseDatabase.getReference(path);
    }
}
