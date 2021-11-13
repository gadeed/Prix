package com.flexural.developers.prixapp.model;

public class Data {
    public String dataPrice, dataAmount, description;

    public Data(String dataPrice, String dataAmount, String description) {
        this.dataPrice = dataPrice;
        this.dataAmount = dataAmount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Data{" +
                "dataPrice='" + dataPrice + '\'' +
                ", dataAmount='" + dataAmount + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
