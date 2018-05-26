package com.example.misonglee.login_test.pManagerReservation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pManagerReservation.ReserveData;
import com.example.misonglee.login_test.pManagerReservation.ReserveEndData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Manager_Reservation_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    private ArrayList<ReserveData> list_items;
    private ArrayList<ReserveEndData> search_list_items;
    private int list_items_size;
    private int search_list_items_size;

    private ListView reserve_list;
    private Manager_Reservation_ListAdapter adapter;
    private LinearLayout reserve_search_list_root;
    private Button reserve_search_btn;
    private EditText search_text;


    public Manager_Reservation_Fragment() {
        Log.d("Client_Reservation", "Constructor - execute");

        list_items = null;
        list_items_size = 0;
        search_list_items = null;
        search_list_items_size = 0;

        // 주문이 끝난 layout 정보 획득
        resource = R.layout.reserve_item_finished;
    }

    public static Manager_Reservation_Fragment newInstance(int sectionNumber) {
        Log.d("Client_Reservation", "newInstance-Number : " + sectionNumber);

        Manager_Reservation_Fragment fragment = new Manager_Reservation_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Client_Menu_Fragment", "onCreateView-execute");

        // 여기서 먼저 데이터를 다 받아올까...?

        ArrayList<ReserveData> asdf = new ArrayList<>();
        ReserveData a = new ReserveData(1, 2, 2, "asdf");
        ReserveData b = new ReserveData(2, 1, 2, "bbbb");
        asdf.add(a);
        asdf.add(b);

        SetListData(asdf);

        ArrayList<ReserveEndData> fdsa = new ArrayList<>();
        ReserveEndData aa = new ReserveEndData("2017-03-21", 5, 1);
        ReserveEndData bb = new ReserveEndData("2017-08-21", 4, 2);
        ReserveEndData cc = new ReserveEndData("2016-03-21", 3, 3);
        ReserveEndData dd = new ReserveEndData("2018-03-21", 2, 4);
        ReserveEndData ee = new ReserveEndData("2018-03-25", 1, 5);
        fdsa.add(aa);
        fdsa.add(bb);
        fdsa.add(cc);
        fdsa.add(dd);
        fdsa.add(ee);

        SetSearchListData(fdsa);

        root_view = inflater.inflate(R.layout.manager_fragment_reserve, container, false);
        reserve_search_list_root = (LinearLayout) root_view.findViewById(R.id.manager_fragment_reserve_search_root);
        reserve_list = (ListView) root_view.findViewById(R.id.manager_fragment_reserve_list);
        adapter = new Manager_Reservation_ListAdapter(list_items);
        reserve_list.setAdapter(adapter);
        context = container.getContext();
        reserve_search_btn = (Button) root_view.findViewById(R.id.manager_fragment_reserve_search_btn);
        search_text = (EditText) root_view.findViewById(R.id.manager_fragment_reserve_search);

        reserve_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 이름을 서버로 보내서, 해당 정보를 획득한 뒤, SetView 한다.
                String name = search_text.getText().toString();
                Log.d("ㅁㄴㅇㄻㄴㅇㄹ", name);
            }
        });

        // 일단 먼저 전체 유저에 대한 정보를 획득한다!
        SetView();

        return root_view;
    }

    // Reserve List를 보여주는 메뉴.
    public void SetListData(ArrayList<ReserveData> data) {
        list_items = data;
        list_items_size = data.size();
    }

    public void SetSearchListData(ArrayList<ReserveEndData> data) {
        search_list_items = data;
        search_list_items_size  = data.size();
    }

    public void SetView() {
        Log.d("Client_Notice_Fragment", "SetView-execute");

        reserve_search_list_root.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < search_list_items_size; i++) {

            View view = inflater.inflate(resource, reserve_search_list_root, false);

            TextView date = (TextView) view.findViewById(R.id.reserve_item_finished_date);
            TextView menu = (TextView) view.findViewById(R.id.reserve_item_finished_menu);
            TextView count = (TextView) view.findViewById(R.id.reserve_item_finished_count);

            date.setText(search_list_items.get(i).date);
            menu.setText(Integer.toString(search_list_items.get(i).menuNum));
            count.setText(Integer.toString(search_list_items.get(i).menuCount));

            reserve_search_list_root.addView(view);
        }
    }
}
