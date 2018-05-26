package com.example.misonglee.login_test.pClientReservation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientMenu.Client_Menu_Fragment;
import com.example.misonglee.login_test.pClientMenu.MenuData;

import java.util.ArrayList;

public class Client_Reservation_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static int CODE_RESERVE_LIST = 1001;
    public static int CODE_RESERVE_TOTAL = 1002;

    private Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<ReserveData> list_items;
    private ArrayList<String> list_items_ends;
    private int list_items_size;
    private int list_items_ends_size;

    private ListView reserve_list;
    private Client_Reservation_ListAdapter adapter;

    public Client_Reservation_Fragment() {
        Log.d("Client_Reservation", "Constructor - execute");

        list_items = null;
        list_items_size = 0;
        list_items_ends = null;
        list_items_ends_size = 0;
        resource = R.layout.reserve_item;
    }


    public static Client_Reservation_Fragment newInstance(int sectionNumber) {
        Log.d("Client_Reservation", "newInstance-Number : " + sectionNumber);

        Client_Reservation_Fragment fragment = new Client_Reservation_Fragment();
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

        root_view = inflater.inflate(R.layout.user_fragment_reserve, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.user_fragment_reserve_month_root);
        reserve_list = (ListView) root_view.findViewById(R.id.user_fragment_reserve_list);
        adapter = new Client_Reservation_ListAdapter(asdf);
        reserve_list.setAdapter(adapter);
        context = container.getContext();

        //SetView();

        return root_view;
    }

    // Reserve List를 보여주는 메뉴.
    public void SetListData(ArrayList<ReserveData> data) {
        list_items = data;
        list_items_size = data.size();
    }

    public void SetListEndData(ArrayList<String> data) {
        list_items_ends = data;
        list_items_ends_size = data.size();
    }

    public void SetView(int Codenum) {
        Log.d("Client_Notice_Fragment", "SetView-execute : " + Codenum );

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < list_items_size; i++){
            View view = inflater.inflate(resource, root_layout, false);

            TextView text;
        }
/*        switch (Codenum){
            // 공지사항 처리
            case CODE_RESERVE_LIST:
                for (int i = 0; i < list_items_size; i++) {


                    TextView menu = (TextView) view.findViewById(R.id.Date);
                    TextView title = (TextView) view.findViewById(R.id.Title);
                    TextView message = (TextView) view.findViewById(R.id.Message);
                    LinearLayout titlebar = (LinearLayout) view.findViewById(R.id.TitleBar);
                    final LinearLayout messagebar = (LinearLayout) view.findViewById(R.id.MessageBar);

                    date.setText(list_items.get(i).date);
                    title.setText(list_items.get(i).title);
                    message.setText(list_items.get(i).message);
                    messagebar.setVisibility(View.GONE);
                    titlebar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (messagebar.getVisibility() == View.GONE)
                                messagebar.setVisibility(View.VISIBLE);
                            else
                                messagebar.setVisibility(View.GONE);
                        }
                    });

                    root_layout.addView(view);
                }
                break;

            // 이벤트 사항 처리
            case CODE_RESERVE_TOTAL:
                for (int i = 0; i < event_items_size; i++) {
                    View view = inflater.inflate(resource_event, root_layout, false);

                    TextView message = (TextView) view.findViewById(R.id.notice_event_message);

                    message.setText(event_items.get(i).message);

                    root_layout.addView(view);
                }
                break;
        }*/
    }
}
