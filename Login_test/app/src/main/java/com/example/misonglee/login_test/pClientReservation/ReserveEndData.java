package com.example.misonglee.login_test.pClientReservation;

public class ReserveEndData {
    String date;
    int menuNum;
    int menuCount;
    int reservationID;
    String year;
    String month;

    public ReserveEndData(String date, int menuNum, int menuCount, int reservationID) {
        this.date = date;
        this.menuNum = menuNum;
        this.menuCount = menuCount;
        this.reservationID = reservationID;
    }
}
