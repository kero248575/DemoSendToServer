package com.example.auser.demosendtoserver;

/**
 * Created by auser on 2017/11/27.
 */

public class Data {
    String address;
    String phoneNumber;

    public Data() {
        this("台北市","");
    }//沒有參數建構方法,可以內建資料近來,是測試的一組固定資料

    public Data(String address, String phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
