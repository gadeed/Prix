package com.flexural.developers.prixapp.model;

public class Statements {
    public String transactionDate, sender, amount;

    public Statements(String transactionDate, String sender, String amount) {
        this.transactionDate = transactionDate;
        this.sender = sender;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Statements{" +
                "transactionDate='" + transactionDate + '\'' +
                ", sender='" + sender + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
