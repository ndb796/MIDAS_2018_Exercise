package com.example.misonglee.login_test.pNotice;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Notice_Fragment extends Fragment {

    public static Notice_Fragment fragment = null;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<NoticeData> items;
    private int items_size;

    public Notice_Fragment() {
        Log.d("Notice_Fragment", "Constructor - execute");
        items = null;
        items_size = 0;
        resource = R.layout.notice_item;
    }

    /*
     * 이 부분, 다른 곳에서 정보를 가져오는 걸로 바꿔야 할까?
     * 1) Main에 저장된 공지사항 정보를 가져온다?
     * 2) 통신으로 공지사항 정보를 가져온다?
     * items : View에 넣을 데이터 전부.
     * items_size : 데이터 사이즈.
     * */
    public void SetItems(ArrayList<NoticeData> items, int items_size) {
        Log.d("Notice_Fragment", "SetItems - size : " + items_size);

        this.items = items;
        this.items_size = items_size;
    }

    /*
     * 새로운 Fragment Instance 생성 후 반환
     * */
    public static Notice_Fragment newInstance(int sectionNumber) {
        Log.d("Notice_Fragment", "newInstance-Number : "+sectionNumber);
        fragment = new Notice_Fragment();
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

    class BackgroundTask extends AsyncTask<String, Void, String> {

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
                ArrayList<NoticeData> tmp = new ArrayList<>();

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeTitle = object.getString("noticeTitle");
                    noticeContent = object.getString("noticeContent");
                    noticeDate = object.getString("noticeDate");
                    tmp.add(new NoticeData(noticeDate, noticeTitle, noticeContent));
                    count++;
                }

                fragment.SetItems(tmp, tmp.size());
                SetView();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Notice_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.fragment_notice, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.NoticeRoot);
        context = container.getContext();

        BackgroundTask task = new BackgroundTask();
        task.execute();

        return root_view;
    }

    public void SetView() {
        Log.d("Notice_Fragment", "SetView-execute");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items_size; i++) {
            View view = inflater.inflate(resource, root_layout, false);

            TextView date = (TextView) view.findViewById(R.id.Date);
            TextView title = (TextView) view.findViewById(R.id.Title);
            TextView message = (TextView) view.findViewById(R.id.Message);
            LinearLayout titlebar = (LinearLayout) view.findViewById(R.id.TitleBar);
            final LinearLayout messagebar = (LinearLayout) view.findViewById(R.id.MessageBar);

            date.setText(items.get(i).date);
            title.setText(items.get(i).title);
            message.setText(items.get(i).message);
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
    }
}
