package com.flexural.developers.prixapp.model;

public class Transfer {
    public String shopName, accNo, shopAccNo;

    public Transfer(String accNo, String shopName, String shopAccNo) {
        this.accNo = accNo;
        this.shopName = shopName;
        this.shopAccNo = shopAccNo;
    }


// filkaan waxaan kusoo darna 2 daan method ee hoose
// oo noosoo qabanaya accNo iyo shopName
// waxaa laga soo wacayaa adapterka fiiri REF1
    public String getAccNo() {
        return accNo;
    }

    public String getShopName() {
        return shopName;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "shopName='" + shopName + '\'' +
                ", accNo='" + accNo + '\'' +
                ", shopAccNo='" + shopAccNo + '\'' +
                '}';
    }
}
