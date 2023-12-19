package com.example.fp2;

public class ProdukModel {
    private String namaProduk;
    private String imageUrl;
    private int quantity;
    private String category;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ProdukModel() {
    }

    public ProdukModel(String namaProduk, String imageUrl, String category, String price) {
        this.namaProduk = namaProduk;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
