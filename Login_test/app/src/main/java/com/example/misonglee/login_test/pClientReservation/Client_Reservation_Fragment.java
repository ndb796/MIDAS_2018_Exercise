package com.example.misonglee.login_test.pClientReservation;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientMenu.Client_Menu_Fragment;
import com.example.misonglee.login_test.pClientMenu.MenuData;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_User;
import com.example.misonglee.login_test.pNotice.NoticeData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Client_Reservation_Fragment extends Fragment {

    public static Client_Reservation_Fragment fragment = null;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static int CODE_RESERVE_LIST = 1001;
    public static int CODE_RESERVE_TOTAL = 1002;

    private Context context;
    private ViewGroup _container;
    private int resource;
    private View root_view;
    public static ArrayList<ReserveData> list_items;
    private ArrayList<ReserveEndData> list_items_ends;
    public static HashMap<Integer, String> list_menu;
    private int list_items_size;
    private int list_items_ends_size;

    private ListView reserve_list;
    private Client_Reservation_ListAdapter adapter;
    private LinearLayout reserve_list_end_root;
    private TextView reserve_show_year;
    private TextView reserve_show_month;
    private Button left_btn;
    private Button right_btn;

    private String year;
    private String month;

    public Client_Reservation_Fragment() {
        Log.d("Client_Reservation", "Constructor - execute");

        list_menu = new HashMap<>();
        list_items = null;
        list_items_size = 0;
        list_items_ends = new ArrayList<>();
        list_items_ends_size = 0;
        resource = R.layout.reserve_item_finished;

        BackgroundTask_GetMenu backgroundTask_getMenu = new BackgroundTask_GetMenu();
        backgroundTask_getMenu.execute();

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

        //user의 예약 내용을 가져옴
        BackgroundTask_Reservation backgroundTask_reservation = new BackgroundTask_Reservation();
        backgroundTask_reservation.execute();

        list_items = MainActivity_User.list_items;
        //Log.d("asdfasdf",  " " +list_items.size());
        context = container.getContext();
        root_view = inflater.inflate(R.layout.user_fragment_reserve, container, false);
        reserve_list_end_root = (LinearLayout) root_view.findViewById(R.id.user_fragment_reserve_month_root);
        reserve_list = (ListView) root_view.findViewById(R.id.user_fragment_reserve_list);
        reserve_show_year = (TextView) root_view.findViewById(R.id.user_fragment_reserve_show_year);
        reserve_show_month = (TextView) root_view.findViewById(R.id.user_fragment_reserve_show_month);
        left_btn = (Button) root_view.findViewById(R.id.user_fragment_reserve_left_btn);
        right_btn = (Button) root_view.findViewById(R.id.user_fragment_reserve_right_btn);

        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.valueOf(reserve_show_year.getText().toString());
                int month = Integer.valueOf(reserve_show_month.getText().toString());

                if(month - 1 == 0){
                    year--;
                    month = 12;
                }else{
                    month--;
                }

                reserve_show_year.setText(String.valueOf(year));
                reserve_show_month.setText(String.format("%02d", month));

                SetView(String.valueOf(year), String.format("%02d", month));
            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.valueOf(reserve_show_year.getText().toString());
                int month = Integer.valueOf(reserve_show_month.getText().toString());

                if(month + 1 == 13){
                    year++;
                    month = 1;
                }else{
                    month++;
                }

                reserve_show_year.setText(String.valueOf(year));
                reserve_show_month.setText(String.format("%02d", month));

                SetView(String.valueOf(year), String.format("%02d", month));
            }
        });

        // 현재 날짜 구하기.
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        String[] values = getTime.split("-");
        String year = values[0];
        final String month = values[1];

        reserve_show_year.setText(year);
        reserve_show_month.setText(month);

        //SetView(year, month);
        this.year = year;
        this.month = month;

        return root_view;
    }

    // Reserve List를 보여주는 메뉴.
    public void SetListData(ArrayList<ReserveData> data) {
        Log.d("asdfasdf", "setListData + " + data);
        list_items = data;
        list_items_size = data.size();
        MainActivity_User.list_items = data;

        SetView(year, month);
    }

    public void SetListEndData(ArrayList<ReserveEndData> data) {
        list_items_ends = data;
        list_items_ends_size = data.size();
    }

    public void SetView(String year, String month) {
        Log.d("Client_Notice_Fragment", "SetView-execute");
        Log.d("Client_Notice_Fragment", year + " " + month);

        // 사용자의 전체 예약 정보 데이터 구분
        for(int i = 0; i<list_items.size(); i++){
            list_items_ends.add(new ReserveEndData(list_items.get(i).reservationDate, list_items.get(i).menuID, list_items.get(i).menuCount));
            parsing(list_items_ends.get(i));
        }

        reserve_list_end_root.removeAllViews();
        adapter = new Client_Reservation_ListAdapter(list_items);
        reserve_list.setAdapter(adapter);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list_items_size; i++) {
            if(list_items_ends.get(i).year.equals(year) == true){
                Log.d("Client_Notice_Fragment", "년도 같음");
                if(list_items_ends.get(i).month.equals(month) == true){
                    Log.d("Client_Notice_Fragment", "달도 같음");
                    View view = inflater.inflate(resource, reserve_list_end_root, false);

                    TextView date = (TextView) view.findViewById(R.id.reserve_item_finished_date);
                    TextView menu = (TextView) view.findViewById(R.id.reserve_item_finished_menu);
                    TextView count = (TextView) view.findViewById(R.id.reserve_item_finished_count);

                    date.setText(list_items_ends.get(i).date);
                    menu.setText(list_menu.get(list_items_ends.get(i).menuNum));
                    //menu.setText(Integer.toString(list_items_ends.get(i).menuNum));
                    //menu.setText(MainActivity_User.menu[list_items_ends.get(i).menuNum]);
                    count.setText(Integer.toString(list_items_ends.get(i).menuCount) + "개");

                    reserve_list_end_root.addView(view);
                }
            }
        }
    }

    private void parsing(final ReserveEndData data) {

        String date = data.date;
        String[] values = date.split("-");
        String year = values[0];
        final String month = values[1];
        Log.d("parsing", year + month);
        data.year = year;
        data.month = month;
    }

    class BackgroundTask_GetMenu extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "menuListView.midas";
                Log.d("Reservation",target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Reservation"," doInBackground");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try{
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e){
                Log.e("Reservation", "Exception: " + e.getMessage());
            }
            return null;
        }
        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        // 문자열을 JSON 형태로 처리합니다.
        @Override
        public void onPostExecute(String result) {
            try {
                Log.d("Reservation"," onPostExecute");

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                int menuID;
                String menuTitle;
                int count = 0;


                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    menuID = object.getInt("menuID");
                    menuTitle = object.getString("menuTitle");

                    list_menu.put(menuID, menuTitle);

                    count++;
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask_Reservation extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "reservationListProcessingByUserIDView.midas?userID="+ URLEncoder.encode(((MainActivity_User)context).GetUserID(),"UTF-8");
                Log.d("Reservation",target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Reservation"," doInBackground");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try{
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e){
                Log.e("Reservation", "Exception: " + e.getMessage());
            }
            return null;
        }
        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        // 문자열을 JSON 형태로 처리합니다.
        @Override
        public void onPostExecute(String result) {
            try {
                Log.d("Reservation"," onPostExecute");

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                int count = 0;

                ArrayList<ReserveData> tmp = new ArrayList<>();
                int menuID, menuCount,reservationProcess, reservationID;
                String reservationDate;

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);


                    menuID = object.getInt("menuID");
                    menuCount = object.getInt("menuCount");
                    reservationProcess = object.getInt("reservationProcess");
                    reservationDate = object.getString("reservationDate");
                    reservationID = object.getInt("reservationID");
                    tmp.add(new ReserveData(menuID, menuCount, reservationProcess, reservationID,reservationDate));

                    count++;
                }
                //fragment.SetListData(tmp);
                SetListData(tmp);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
