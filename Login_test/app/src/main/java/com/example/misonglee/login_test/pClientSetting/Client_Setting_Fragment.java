package com.example.misonglee.login_test.pClientSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.misonglee.login_test.Account_Management;
import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_User;
import com.example.misonglee.login_test.pSetting.Setting_Fragment;

public class Client_Setting_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View root_view;
    private LinearLayout root_layout;
    private Context context;


    public Client_Setting_Fragment(){

    }

    public static Client_Setting_Fragment newInstance(int sectionNumber) {
        Log.d("Setting_Fragment", "newInstance-Number : "+sectionNumber);
        Client_Setting_Fragment fragment = new Client_Setting_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Setting_Fragment", "onCreateView - execute");

        root_view = inflater.inflate(R.layout.user_fragment_setting, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.SettingRoot);
        context = container.getContext();

        SetView();

        return root_view;
    }

    private void SetView(){
        Log.d("Setting_Fragment", "SetView - execute");

        TextView welcome = (TextView) root_view.findViewById(R.id.welcome);
        welcome.setText("환영합니다, <" + MainActivity_User.userID + "> 님!");

        final ActionProcessButton btnAccountTest = (ActionProcessButton) root_view.findViewById(R.id.btnAccountTest);
        btnAccountTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 테스트 페이지로 이동합니다.
                Intent intent = new Intent(context, Account_Management.class);
                // 테스트 페이지로 넘어갈 때 아이디와 세션 정보를 저장합니다.
                intent.putExtra("userID", MainActivity_User.GetUserID());
                intent.putExtra("session", MainActivity_User.GetUserPW());
                startActivity(intent);
            }
        });
    }

}
