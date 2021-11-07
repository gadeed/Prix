package com.flexural.developers.prixapp.model;

public class Airtime {
    public String prod_id, pin_no, serial_no, status, expired_date;

    public Airtime(String prod_id, String pin_no, String serial_no, String status, String expired_date) {
        this.prod_id = prod_id;
        this.pin_no = pin_no;
        this.serial_no = serial_no;
        this.status = status;
        this.expired_date = expired_date;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public void setPin_no(String pin_no) {
        this.pin_no = pin_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    @Override
    public String toString() {
        return "Airtime{" +
                "prod_id='" + prod_id + '\'' +
                ", pin_no='" + pin_no + '\'' +
                ", serial_no='" + serial_no + '\'' +
                ", status='" + status + '\'' +
                ", expired_date='" + expired_date + '\'' +
                '}';
    }
}
