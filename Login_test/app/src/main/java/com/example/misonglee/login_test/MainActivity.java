package com.example.misonglee.login_test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 시 전달 받은 세션 값도 함께 저장합니다.
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        session = intent.getStringExtra("session");

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        Log.d("MainActivity", "init-excute");

        // toolbar 초기화
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MIDAS_Example");
        setSupportActionBar(toolbar);

        // viewpager와 FragmentAdapter 연결
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        // tablayout과 viewPager 연결
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        Log.d("MainActivity", "init-finish");
    }

    public static String GetUserID(){
        return userID;
    }

    public static String GetUserPW(){
        return session;
    }
}

