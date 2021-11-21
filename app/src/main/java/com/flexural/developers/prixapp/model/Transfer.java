package com.flexural.developers.prixapp.model;

public class Transfer {
    public String accNo, accNoTr;

    public Transfer(String accNo, String accNoTr) {
        this.accNo = accNo;
        this.accNoTr = accNoTr;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "accNo='" + accNo + '\'' +
                ", accNoTr='" + accNoTr + '\'' +
                '}';
    }
}
