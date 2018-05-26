package com.example.misonglee.login_test.pManagerMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.Write_Content_Dialog;
import com.example.misonglee.login_test.Write_Menu_Dialog;
import com.example.misonglee.login_test.pClientMenu.Client_Menu_Dialog;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_Manager;
import com.example.misonglee.login_test.pNotice.NoticeData;
import com.example.misonglee.login_test.pNotice.Notice_Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Manager_Menu_Fragment extends Fragment {

    public static Manager_Menu_Fragment fragment = null;

    // 이미지를 보여주는 서버 경로
    String imageURL = MainActivity.URL + "upload/";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<MenuData> items;
    private int items_size;

    public Manager_Menu_Fragment() {
        Log.d("Manager_Menu_Fragment", "Constructor - execute");

        items = null;
        items_size = 0;
        resource = R.layout.contents_item;
    }

    /*
     * 이 부분, 다른 곳에서 정보를 가져오는 걸로 바꿔야 할까?
     * 1) Main에 저장된 공지사항 정보를 가져온다?
     * 2) 통신으로 공지사항 정보를 가져온다?
     * items : View에 넣을 데이터 전부.
     * items_size : 데이터 사이즈.
     * */
    public void SetItems(ArrayList<MenuData> items, int items_size) {
        Log.d("Manager_Menu_Fragment", "SetItems - size : " + items_size);

        this.items = items;
        this.items_size = items_size;
    }

    /*
     * 새로운 Fragment Instance 생성 후 반환
     * */
    public static Manager_Menu_Fragment newInstance(int sectionNumber) {
        Log.d("Manager_Menu_Fragment", "newInstance-Number : " + sectionNumber);
        fragment = new Manager_Menu_Fragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Manager_Menu_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.manager_fragment_menu, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.manager_menu_root);
        context = container.getContext();

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();

        // 글쓰기 버튼
        FloatingActionButton write_button = (FloatingActionButton) root_view.findViewById(R.id.menu_write_button);
        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //글쓰기 dialog
                Write_Menu_Dialog write_dialog = new Write_Menu_Dialog(context,((MainActivity_Manager)context).GetUserID(),((MainActivity_Manager)context).GetUserPW());
                write_dialog.show();
            }
        });

        return root_view;
    }

    public void SetView() {
        Log.d("Manager_Menu_Fragment", "SetView-execute");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items_size; i++) {
            View view = inflater.inflate(resource, root_layout, false);

            TextView name = (TextView) view.findViewById(R.id.contents_nameMsg);
            TextView price = (TextView) view.findViewById(R.id.contents_priceMsg);
            TextView discount_rate = (TextView) view.findViewById(R.id.contents_discount_rateMsg);
            TextView detail = (TextView) view.findViewById(R.id.contents_detailMsg);
            TextView picture = (TextView) view.findViewById(R.id.contents_pictureMsg);
            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            // final LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.contents_item);

            final int temp = i;

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imageURL + items.get(temp).picture);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        Bitmap bitmapCopy = Bitmap.createScaledBitmap(bitmap, 240, 200, true);
                        imageView.setImageBitmap(bitmapCopy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {

            }

            name.setText(items.get(i).name);
            price.setText(items.get(i).price);
            discount_rate.setText(items.get(i).discount_rate);
            detail.setText(items.get(i).detail);
            picture.setText(items.get(i).picture);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView)v.findViewById(R.id.contents_nameMsg)).getText().toString();
                    String price = ((TextView)v.findViewById(R.id.contents_priceMsg)).getText().toString();

                    final Manager_Menu_Dialog manager_menu_dialog = new Manager_Menu_Dialog(context, name, price);
                    manager_menu_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String name = manager_menu_dialog.getName();
                            String price = manager_menu_dialog.getResult();

                            // name이랑 price를 가지고 서버로 데이터 전송하기.
                        }
                    });
                    manager_menu_dialog.show();
                }
            });

            root_layout.addView(view);
        }
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "menuListView.midas";
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
                String menuTitle, menuPrice, menuDiscount, menuInformation, menuProfile;
                ArrayList<MenuData> tmp = new ArrayList<>();

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    menuTitle = object.getString("menuTitle");
                    menuPrice = object.getString("menuPrice");
                    menuInformation = object.getString("menuInformation");
                    menuProfile = object.getString("menuProfile");

                    // 임시로 지정
                    tmp.add(new MenuData(menuTitle,menuPrice, "",menuInformation, menuProfile));
                    count++;
                }

                fragment.SetItems(tmp, tmp.size());
                SetView();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
