package com.example.mynotes;

public class Product {

    private String text;
    private int price;
    private boolean isAvailable;

    public Product(String text, int price, boolean isAvailable) {
        this.text = text;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public Product() {
    }

    public String getText() {
        return text;
    }

    public void setName(String text) {
        this.text = text;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "text='" + text + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
