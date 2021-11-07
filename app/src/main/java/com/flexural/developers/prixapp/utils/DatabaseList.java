package com.flexural.developers.prixapp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatabaseList {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("prod_id")
    @Expose
    String prod_id;

    @SerializedName("pin_no")
    @Expose
    String pin_no;

    @SerializedName("serial_no")
    @Expose
    String serial_no;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("expired_date")
    @Expose
    String expired_date;

    public DatabaseList(String id, String prod_id, String pin_no, String serial_no, String status, String expired_date) {
        this.id = id;
        this.prod_id = prod_id;
        this.pin_no = pin_no;
        this.serial_no = serial_no;
        this.status = status;
        this.expired_date = expired_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getPin_no() {
        return pin_no;
    }

    public void setPin_no(String pin_no) {
        this.pin_no = pin_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }
}
