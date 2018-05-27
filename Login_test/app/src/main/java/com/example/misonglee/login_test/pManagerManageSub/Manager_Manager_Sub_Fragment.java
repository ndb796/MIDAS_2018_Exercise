package com.example.misonglee.login_test.pManagerManageSub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.misonglee.login_test.Account_Change;
import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pLogin.Register;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pManagerManageUser.Manager_Manager_User_Fragment;
import com.example.misonglee.login_test.pManagerManageUser.Manager_Manager_User_ListAdapter;
import com.example.misonglee.login_test.pManagerManageSub.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Manager_Manager_Sub_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Manager_Manager_Sub_Fragment fragment = null;

    private Context context;
    private View rootView;
    private LinearLayout rootLayout;
    private int item_resource;
    private ArrayList<UserData> list_items;
    private int list_items_size;
    private String select_id;


    private Manager_Manager_User_ListAdapter adapter;

    private Button add_sub_manager;
    private EditText search_text;

    public Manager_Manager_Sub_Fragment() {

        list_items = null;
        list_items_size = 0;
        item_resource = R.layout.manage_item;
    }

    public static Manager_Manager_Sub_Fragment newInstance(int sectionNumber) {
        Log.d("Client_Reservation", "newInstance-Number : " + sectionNumber);

        fragment = new Manager_Manager_Sub_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manager_fragment_manage_sub, container, false);
        rootLayout = (LinearLayout) rootView.findViewById(R.id.sub_manager_root);

        context = container.getContext();

        add_sub_manager = (Button) rootView.findViewById(R.id.add_sub_manager);
        add_sub_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sub register이동!
                Intent register_intent = new Intent(context, Register.class);
                register_intent.putExtra("mode","sub");
                startActivity(register_intent);
            }
        });

        BackgroundTask_Sub backgroundTask_sub = new BackgroundTask_Sub();
        backgroundTask_sub.execute();


        return rootView;
    }

    public void SetView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list_items_size; i++) {
            if(list_items.get(i).userType.equals("2")) {

                View view = inflater.inflate(item_resource, rootLayout, false);

                TextView name = (TextView) view.findViewById(R.id.text_nameMsg);
                TextView pw = (TextView) view.findViewById(R.id.text_idMsg);
                TextView birthday = (TextView) view.findViewById(R.id.text_birthday);
                TextView depart = (TextView) view.findViewById(R.id.text_depart);

                name.setText(list_items.get(i).name);
                pw.setText(list_items.get(i).userID);
                birthday.setText(list_items.get(i).birthday);
                depart.setText(list_items.get(i).depart);

                registerForContextMenu(view);

                rootLayout.addView(view);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        TextView tv_id = (TextView)v.findViewById(R.id.text_idMsg);
        select_id = tv_id.getText().toString();


        menu.setHeaderTitle("Menu");
        menu.add(0, 1, 100, "수정");
        menu.add(0, 2, 100, "삭제");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://수정
                Intent m_intent = new Intent(context, Account_Change.class);
                m_intent.putExtra("userID", select_id);
                startActivity(m_intent);
                return true;
            case 2://삭제
                DeleteDB deleteDB = new DeleteDB();
                deleteDB.execute(select_id);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void SetListData(ArrayList<UserData> data) {
        list_items = data;
        list_items_size = data.size();
    }

    class DeleteDB extends AsyncTask<String, Void, String> {
        String target;


        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon", "Delete doInBackground");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try {
                //선택한 사용자 id, pw 받아서 삭제 하기
                target = MainActivity.URL + "userDelete.midas?userID=" + URLEncoder.encode(strings[0], "UTF-8");

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
                String verify = jsonObject.getString("verify");

                // 서버로부터 반환 된 값이 1이면 회원탈퇴 성공입니다.
                if (verify.equals("1")) {
                    // 성공 알림창을 띄우고 로그인 페이지로 이동
                    delete_successAlert();
                }
                //회원탈퇴 실패
                else {
                    delete_failAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class BackgroundTask_Sub extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "userListView.midas";
                Log.d("TAG",target);

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
                String userName, userPassword, userBirthday, userDepart, userType, userID;
                ArrayList<UserData> tmp = new ArrayList<>();

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    userName = object.getString("userName");
                    userPassword = object.getString("userPassword");
                    userBirthday = object.getString("userBirthday");
                    userDepart = object.getString("userDepartment");
                    userType = object.getString("userType");
                    userID = object.getString("userID");

                    tmp.add(new UserData(userName, userPassword, userBirthday, userDepart, userType, userID));
                    count++;
                }
                fragment.SetListData(tmp);
                SetView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete_successAlert() {
        new AwesomeSuccessDialog(context)
                .setTitle("회원탈퇴 성공")
                .setMessage("회원탈퇴가 정상적으로 완료되었습니다.")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("확인")
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {

                    }
                })
                .show();
    }

    public void delete_failAlert() {
        new AwesomeWarningDialog(context)
                .setTitle("회원탈퇴 실패")
                .setMessage("회원 탈퇴에 실패했습니다.\n 다시 시도 해주세요")
                .setColoredCircle(R.color.dialogWarningBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_warning, R.color.black)
                .setCancelable(true)
                .setButtonText("확인")
                .setButtonBackgroundColor(R.color.dialogWarningBackgroundColor)
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        // click
                    }
                })
                .show();
    }
}
