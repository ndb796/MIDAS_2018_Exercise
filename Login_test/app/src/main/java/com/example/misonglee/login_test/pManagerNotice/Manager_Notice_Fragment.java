package com.example.misonglee.login_test.pManagerNotice;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.Write_Content_Dialog;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_Manager;
import com.example.misonglee.login_test.pNotice.EventData;
import com.example.misonglee.login_test.pNotice.NoticeData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Manager_Notice_Fragment extends Fragment {

    public static Manager_Notice_Fragment fragment = null;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static final int CODE_NOTICE = 1001;
    public static final int CODE_EVENT = 1002;

    public Context context;

    private int resource_notice;
    private int resource_event;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<NoticeData> notice_items;
    private ArrayList<EventData> event_items;
    private int notice_items_size;
    private int event_items_size;

    public Manager_Notice_Fragment() {
        Log.d("Manager_Notice_Fragment", "Constructor - execute");
        notice_items = null;
        notice_items_size = 0;
        event_items = null;
        notice_items_size = 0;
        resource_notice = R.layout.notice_item;
        resource_event = R.layout.notice_item_event;
    }

    /*
     * 이 부분, 다른 곳에서 정보를 가져오는 걸로 바꿔야 할까?
     * 1) Main에 저장된 공지사항 정보를 가져온다?
     * 2) 통신으로 공지사항 정보를 가져온다?
     * items : View에 넣을 데이터 전부.
     * items_size : 데이터 사이즈.
     * */
    public void SetNoticeData(ArrayList<NoticeData> items, int items_size) {
        Log.d("Manager_Notice_Fragment", "SetItems - size : " + items_size);

        this.notice_items = items;
        this.notice_items_size = items_size;
    }

    public void SetEventData(ArrayList<EventData> items, int items_size) {
        Log.d("Manager_Notice_Fragment", "SetItems - size : " + items_size);

        this.event_items = items;
        this.event_items_size = items_size;
    }

    /*
     * 새로운 Fragment Instance 생성 후 반환
     * */
    public static Manager_Notice_Fragment newInstance(int sectionNumber) {
        Log.d("Manager_Notice_Fragment", "newInstance-Number : "+sectionNumber);
        fragment = new Manager_Notice_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        /*
        ArrayList<NoticeData> tmp = new ArrayList<>();
        tmp.add(new NoticeData("2018-08-05", "[공지] 안녕하세요", "여기는 본문 내용입니다."));
        tmp.add(new NoticeData("2018-08-01", "[공지] 아아아아아", "여기는 본문 내용입니다."));
        fragment.SetItems(tmp, tmp.size());
        */

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Notice_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.manager_fragment_notice, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.manager_notice_root);
        context = container.getContext();

        BackgroundTask_Notice task = new BackgroundTask_Notice();
        task.execute();

        BackgroundTask_Event task2 = new BackgroundTask_Event();
        task2.execute();

        // 글쓰기 버튼
        FloatingActionButton write_button = (FloatingActionButton) root_view.findViewById(R.id.notice_write_button);
        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //글쓰기 dialog
                Write_Content_Dialog write_dialog = new Write_Content_Dialog(context, ((MainActivity_Manager)context).GetUserID(), ((MainActivity_Manager)context).GetUserPW());
                write_dialog.show();
            }
        });

        return root_view;
    }

    public void SetView(int Codenum) {
        Log.d("Manager_Notice_Fragment", "SetView-execute : " + Codenum );

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (Codenum){
            // 공지사항 처리
            case CODE_NOTICE:
                for (int i = 0; i < notice_items_size; i++) {
                    View view = inflater.inflate(resource_notice, root_layout, false);

                    TextView date = (TextView) view.findViewById(R.id.Date);
                    TextView title = (TextView) view.findViewById(R.id.Title);
                    TextView message = (TextView) view.findViewById(R.id.Message);
                    Button button = (Button) view.findViewById(R.id.notice_item_setting);
                    LinearLayout titlebar = (LinearLayout) view.findViewById(R.id.TitleBar);
                    final LinearLayout messagebar = (LinearLayout) view.findViewById(R.id.MessageBar);

                    final String string_title = notice_items.get(i).title;
                    final String string_message = notice_items.get(i).message;
                    final int int_id = notice_items.get(i).id;


                    date.setText(notice_items.get(i).date);
                    title.setText(string_title);
                    message.setText(string_message);
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

                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Log.d("Manager_Notice_Fragment", "button click -execute : " );

                            //글쓰기 dialog
                            Write_Content_Dialog write_dialog = new Write_Content_Dialog(context, ((MainActivity_Manager)context).GetUserID(), ((MainActivity_Manager)context).GetUserPW());
                            write_dialog.setContent(string_title, string_message, int_id);
                            write_dialog.show();

                        }
                    });

                    root_layout.addView(view);
                }
                break;

            // 이벤트 사항 처리
            case CODE_EVENT:
                for (int i = 0; i < event_items_size; i++) {
                    View view = inflater.inflate(resource_event, root_layout, false);

                    TextView message = (TextView) view.findViewById(R.id.notice_event_message);

                    message.setText(event_items.get(i).message);

                    root_layout.addView(view);
                }
                break;
        }


    }

    class BackgroundTask_Notice extends AsyncTask<String, Void, String> {

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
            Log.d("Raon","ChangeDB doInBackground");

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
                String noticeTitle, noticeContent, noticeDate;
                int noticeID;
                ArrayList<NoticeData> tmp = new ArrayList<>();

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeTitle = object.getString("noticeTitle");
                    noticeContent = object.getString("noticeContent");
                    noticeDate = object.getString("noticeDate");
                    noticeID = object.getInt("noticeID");
                    tmp.add(new NoticeData(noticeDate, noticeTitle, noticeContent, noticeID));
                    count++;
                }

                fragment.SetNoticeData(tmp, tmp.size());
                SetView(CODE_NOTICE);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask_Event extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "eventListView.midas";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon","ChangeDB doInBackground");

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
                String eventMessage;
                ArrayList<EventData> tmp = new ArrayList<>();

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    eventMessage = object.getString("eventMessage");
                    tmp.add(new EventData(eventMessage));
                    count++;
                }

                fragment.SetEventData(tmp, tmp.size());
                SetView(CODE_EVENT);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
