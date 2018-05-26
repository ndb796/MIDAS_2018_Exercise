package com.example.misonglee.login_test.pClientReservation;

public class ReserveEndData {
    String date;
    int menuNum;
    int menuCount;
    String year;
    String month;

    public ReserveEndData(String date, int menuNum, int menuCount) {
        this.date = date;
        this.menuNum = menuNum;
        this.menuCount = menuCount;
    }
}
