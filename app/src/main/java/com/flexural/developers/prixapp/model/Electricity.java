package com.flexural.developers.prixapp.model;

public class Electricity {
    public String price;

    public Electricity(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Electricity{" +
                "price='" + price + '\'' +
                '}';
    }
}
