package com.flexural.developers.prixapp.model;

public class Sales {
    public String productType, productName, qty, price, total, percent, merchant;

    public Sales(String productType, String productName, String qty, String price, String total, String percent, String merchant) {
        this.productType = productType;
        this.productName = productName;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.percent = percent;
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "productType='" + productType + '\'' +
                ", productName='" + productName + '\'' +
                ", qty='" + qty + '\'' +
                ", price='" + price + '\'' +
                ", total='" + total + '\'' +
                ", percent='" + percent + '\'' +
                ", merchant='" + merchant + '\'' +
                '}';
    }
}