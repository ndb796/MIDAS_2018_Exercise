package com.example.misonglee.login_test.pManagerManageUser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pManagerReservation.Manager_Reservation_Fragment;
import com.example.misonglee.login_test.pManagerReservation.ReserveData;
import com.example.misonglee.login_test.pNotice.NoticeData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Manager_Manager_User_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Manager_Manager_User_Fragment fragment = null;

    private Context context;
    private View rootView;
    private LinearLayout rootLayout;
    private int item_resource;
    private ArrayList<UserData> list_items;
    private int list_items_size;

    private Manager_Manager_User_ListAdapter adapter;

    private Button search_btn;
    private EditText search_text;

    public Manager_Manager_User_Fragment() {

        list_items = null;
        list_items_size = 0;
        item_resource = R.layout.manage_item;
    }

    public static Manager_Manager_User_Fragment newInstance(int sectionNumber) {
        Log.d("Client_Reservation", "newInstance-Number : " + sectionNumber);

        fragment = new Manager_Manager_User_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manager_fragment_manage_user, container, false);
        rootLayout = (LinearLayout) rootView.findViewById(R.id.manager_fragment_manage_user_root);

        context = container.getContext();
/*        search_text = (EditText) rootView.findViewById(R.id.manager_fragment_manage_user_search);
        search_btn = (Button) rootView.findViewById(R.id.manager_fragment_manage_user_search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = search_text.getText().toString();

                // name 기반으로 정보 받아오기.
            }
        });*/


        return rootView;
    }

    public void SetView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < list_items_size; i++) {
            View view = inflater.inflate(item_resource, rootLayout, false);

            TextView name = (TextView) view.findViewById(R.id.text_name);
            TextView pw = (TextView) view.findViewById(R.id.text_pw);
            TextView birthday = (TextView) view.findViewById(R.id.text_birthday);
            TextView depart = (TextView) view.findViewById(R.id.text_depart);

            name.setText(list_items.get(i).name);
            pw.setText(list_items.get(i).pw);
            birthday.setText(list_items.get(i).birthday);
            depart.setText(list_items.get(i).depart);

            rootLayout.addView(view);
        }
    }

    public void SetListData(ArrayList<UserData> data) {
        list_items = data;
        list_items_size = data.size();
    }

    class BackgroundTask_User extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "noticeListView.midas";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon", "ChangeDB doInBackground");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try {
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                Log.e("Raon", "Exception: " + e.getMessage());
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                int count = 0;
                String userName, userPassword, userBirthday, userDepart;
                ArrayList<UserData> tmp = new ArrayList<>();

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    userName = object.getString("userName");
                    userPassword = object.getString("userPassword");
                    userBirthday = object.getString("userBirthday");
                    userDepart = object.getString("userDepart");
                    tmp.add(new UserData(userName, userPassword, userBirthday, userDepart));
                    count++;
                }

                fragment.SetListData(tmp);
                SetView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
