package com.example.misonglee.login_test.pClientReservation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientMenu.Client_Menu_Fragment;
import com.example.misonglee.login_test.pClientMenu.MenuData;

import java.util.ArrayList;

public class Client_Reservation_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<MenuData> items;
    private int items_size;

    public Client_Reservation_Fragment() {
        Log.d("Client_Reservation", "Constructor - execute");

        //resource =

    }

    public static Client_Reservation_Fragment newInstance(int sectionNumber) {
        Log.d("Client_Reservation", "newInstance-Number : " + sectionNumber);

        Client_Reservation_Fragment fragment = new Client_Reservation_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
/*
        // 임시로 지정
        ArrayList<MenuData> tmp = new ArrayList<>();
        tmp.add(new MenuData("아메리카노","1234", "10","[공지] 안녕하세요", "여기는 본문 내용입니다."));
        tmp.add(new MenuData("자바칩 플랫치노", "4432", "15","[공지] 아아아아아", "여기는 본문 내용입니다."));
        fragment.SetItems(tmp, tmp.size());
*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Client_Menu_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.user_fragment_menu, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.user_menu_root);
        context = container.getContext();

        //SetView();

        return root_view;
    }


}
