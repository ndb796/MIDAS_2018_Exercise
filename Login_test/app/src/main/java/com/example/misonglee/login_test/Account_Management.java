package com.example.misonglee.login_test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.misonglee.login_test.pLogin.Login;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Account_Management extends AppCompatActivity implements View.OnClickListener {

    private String session;
    private String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_menagement);

        //세션 값
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        userID = intent.getStringExtra("userID");

        final ActionProcessButton btnLogout = (ActionProcessButton) findViewById(R.id.btnLogout);
        final ActionProcessButton mvChange = (ActionProcessButton) findViewById(R.id.mvChange);
        final ActionProcessButton btnDelete = (ActionProcessButton) findViewById(R.id.btnDelete);
        final ActionProcessButton profileUpdate = (ActionProcessButton) findViewById(R.id.profileUpdate);

        btnLogout.setOnClickListener(this);
        mvChange.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        profileUpdate.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnLogout:
                //자동 로그인 기록 지우기
                SharedPreferences autoLogin = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin_editor = autoLogin.edit();
                //자동 로그인 기록 clear
                autoLogin_editor.clear();
                autoLogin_editor.commit();
                //로그인 페이지로 이동
                Intent register_intent = new Intent(Account_Management.this, Login.class);
                startActivity(register_intent);
                break;

            case R.id.mvChange:
                //수정 페이지로 이동하기
                Intent m_intent = new Intent(Account_Management.this, Account_Change.class);
                m_intent.putExtra("userID", userID);
                m_intent.putExtra("session", session);
                startActivity(m_intent);
                break;

            case R.id.btnDelete:
                //계정 삭제하기
                DeleteDB deleteDB = new DeleteDB();
                deleteDB.execute();
                break;

            case R.id.profileUpdate:
                // 프로필 변경 페이지로 이동하기
                Intent intent = new Intent(Account_Management.this, Account_Profile_Update.class);
                intent.putExtra("userID", userID);
                intent.putExtra("session", session);
                startActivity(intent);
                break;
        }
    }

    class DeleteDB extends AsyncTask<String, Void, String> {
        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "userDelete.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon","Delete doInBackground");

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
                String verify = jsonObject.getString("verify");

                // 서버로부터 반환 된 값이 1이면 회원탈퇴 성공입니다.
                if(verify.equals("1")) {
                    // 성공 알림창을 띄우고 로그인 페이지로 이동
                    delete_successAlert();
                }
                //회원탈퇴 실패
                else {
                    delete_failAlert();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete_successAlert(){
        new AwesomeSuccessDialog(this)
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

                        //자동로그인 기록 초기화
                        SharedPreferences autoLogin = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin_editor = autoLogin.edit();
                        autoLogin_editor.clear();
                        autoLogin_editor.commit();

                        // 로그인 페이지로 이동합니다.
                        Intent register_intent = new Intent(Account_Management.this, Login.class);
                        startActivity(register_intent);
                    }
                })
                .show();
    }

    public void delete_failAlert(){
        new AwesomeWarningDialog(this)
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
