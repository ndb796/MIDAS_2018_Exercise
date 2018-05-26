package com.example.misonglee.login_test.pManagerMenu;

public class MenuData {
    String name;
    String price;
    String discount_rate;
    String detail;
    String picture; // 사진은 경로가 저장된다.
    public MenuData(String name, String price, String discount_rate, String detail, String pitcute){
        this.name = name;
        this.price = price;
        this.discount_rate = discount_rate;
        this.detail = detail;
        this.picture = pitcute;
    }
}
