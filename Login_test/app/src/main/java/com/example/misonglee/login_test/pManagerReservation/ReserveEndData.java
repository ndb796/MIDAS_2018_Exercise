package com.example.misonglee.login_test.pManagerReservation;

public class ReserveEndData {
    String date;
    int menuNum;
    int menuCount;
    int reservationID;
    String name;
    String year;
    String month;

    public ReserveEndData(String date, int menuNum, int menuCount, int reservationID, String name) {
        this.date = date;
        this.menuNum = menuNum;
        this.menuCount = menuCount;
        this.reservationID = reservationID;
        this.name = name;
    }
}
