package com.example.misonglee.login_test.pContents;

public class ContentsData {
    String date;
    String title;
    String message;
    // 이미지 데이터 추가해야 함.

    public ContentsData(String date, String title, String message) {
        this.date = date;
        this.title = title;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
