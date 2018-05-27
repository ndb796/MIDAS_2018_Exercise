package com.example.misonglee.login_test.pManagerManageUser;

public class UserData {
    String name;
    String pw;
    String depart;
    String birthday;
    String userID;
    String userType;

    public UserData(String name, String pw, String birthday,String depart, String userID, String userType) {
        this.name = name;
        this.pw = pw;
        this.depart = depart;
        this.birthday = birthday;
        this.userID = userID;
        this.userType = userType;
    }
}
