package com.example.misonglee.login_test.pClientReservation;

public class ReserveData {
    int menuID;
    int menuCount;
    int reservationProcess;
    String reservationDate;

    public ReserveData(int menuID, int menuCount, int reservationProcess, String reservationDate) {
        this.menuID = menuID;
        this.menuCount = menuCount;
        this.reservationProcess = reservationProcess;
        this.reservationDate = reservationDate;
    }
}
