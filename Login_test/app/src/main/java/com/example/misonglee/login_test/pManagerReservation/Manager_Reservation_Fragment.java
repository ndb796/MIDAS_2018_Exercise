package com.example.misonglee.login_test.pManagerReservation;

import android.content.Context;
import android.os.AsyncTask;
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
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_Manager;
import com.example.misonglee.login_test.pMainActivity.MainActivity_User;
import com.example.misonglee.login_test.pManagerReservation.ReserveData;
import com.example.misonglee.login_test.pManagerReservation.ReserveEndData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Manager_Reservation_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    public static ArrayList<ReserveData> list_items;
    private ArrayList<ReserveEndData> search_list_items;
    public static HashMap<Integer, String> list_menu;
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

        BackgroundTask_GetMenu backgroundTask_getMenu = new BackgroundTask_GetMenu();
        backgroundTask_getMenu.execute();
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



        //예약 내용을 다 가져온다
        BackgroundTask_Reservation backgroundTask_reservation = new BackgroundTask_Reservation();
        backgroundTask_reservation.execute();

        context = container.getContext();
        root_view = inflater.inflate(R.layout.manager_fragment_reserve, container, false);
        reserve_search_list_root = (LinearLayout) root_view.findViewById(R.id.manager_fragment_reserve_search_root);
        reserve_list = (ListView) root_view.findViewById(R.id.manager_fragment_reserve_list);

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
        SetView();
    }

    public void SetView() {
        Log.d("Client_Notice_Fragment", "SetView-execute");

        reserve_search_list_root.removeAllViews();
        adapter = new Manager_Reservation_ListAdapter(list_items);
        reserve_list.setAdapter(adapter);

        reserve_search_list_root.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < search_list_items_size; i++) {

            View view = inflater.inflate(resource, reserve_search_list_root, false);

            TextView date = (TextView) view.findViewById(R.id.reserve_item_finished_date);
            TextView menu = (TextView) view.findViewById(R.id.reserve_item_finished_menu);
            TextView count = (TextView) view.findViewById(R.id.reserve_item_finished_count);

            date.setText(search_list_items.get(i).date);
            menu.setText(list_menu.get(search_list_items.get(i).menuNum));
            //menu.setText(Integer.toString(search_list_items.get(i).menuNum));
            count.setText(Integer.toString(search_list_items.get(i).menuCount) + "개");

            reserve_search_list_root.addView(view);
        }
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
                target = MainActivity_Manager.URL + "reservationListProcessingByUserIDView.midas?userID="+ URLEncoder.encode(((MainActivity_User)context).GetUserID(),"UTF-8");
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
                BackgroundTask_Complete_Reservation backgroundTask_complete_reservation = new BackgroundTask_Complete_Reservation();
                backgroundTask_complete_reservation.execute();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask_Complete_Reservation extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "reservationListCompletedByAllUserView.midas?userID="+ URLEncoder.encode(((MainActivity_User)context).GetUserID(),"UTF-8");
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

                ArrayList<ReserveEndData> tmp = new ArrayList<>();
                int menuID, menuCount,reservationProcess, reservationID;
                String reservationDate;

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);


                    menuID = object.getInt("menuID");
                    menuCount = object.getInt("menuCount");
                    reservationDate = object.getString("reservationDate");
                    reservationID = object.getInt("reservationID");
                    tmp.add(new ReserveEndData(reservationDate, menuID, menuCount,reservationID));

                    count++;
                }
                //fragment.SetListData(tmp);
                SetSearchListData(tmp);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
