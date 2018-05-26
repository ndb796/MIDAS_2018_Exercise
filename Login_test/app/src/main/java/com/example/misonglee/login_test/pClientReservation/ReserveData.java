package com.example.misonglee.login_test.pClientReservation;

public class ReserveData {
    int menuID;
    int menuCount;
    int reservationProcess;
    int reservationID;
    String reservationDate;

    public ReserveData(int menuID, int menuCount, int reservationProcess, int reservationID, String reservationDate) {
        this.menuID = menuID;
        this.menuCount = menuCount;
        this.reservationProcess = reservationProcess;
        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
    }
}
