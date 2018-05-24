package com.example.misonglee.login_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

public class MainActivity extends AppCompatActivity {

    /* 로컬 서버 */
    // public static String URL = "http://10.0.2.2:8080/MIDAS_Challenge_Application/";
    /* 카페24 호스팅 서버(나동빈) */
    public static String URL = "http://www.dowellcomputer.com/MIDAS/";

    /* 아이디와 세션 값은 서버와 통신할 때마다 파라미터로 보내줘야 합니다. */
    public static String userID;
    public static String session;

    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 시 전달 받은 세션 값도 함께 저장합니다.
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        session = intent.getStringExtra("session");

        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("환영합니다, <" + userID + ">님!");

        final ActionProcessButton btnAccountTest = (ActionProcessButton) findViewById(R.id.btnAccountTest);
        btnAccountTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 테스트 페이지로 이동합니다.
                Intent intent = new Intent(MainActivity.this, Account_Management.class);
                // 테스트 페이지로 넘어갈 때 아이디와 세션 정보를 저장합니다.
                intent.putExtra("userID", userID);
                intent.putExtra("session", session);
                startActivity(intent);
            }
        });



    }




}

