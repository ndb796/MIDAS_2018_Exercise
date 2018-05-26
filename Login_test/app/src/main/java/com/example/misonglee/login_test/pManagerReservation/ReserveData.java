package com.example.misonglee.login_test.pManagerReservation;

public class ReserveData {
    int menuNum;
    int menuCount;
    int reservationProcess;
    String reservationDate;

    public ReserveData(int menuNum, int menuCount, int reservationProcess, String reservationDate) {
        this.menuNum = menuNum;
        this.menuCount = menuCount;
        this.reservationProcess = reservationProcess;
        this.reservationDate = reservationDate;
    }
}
