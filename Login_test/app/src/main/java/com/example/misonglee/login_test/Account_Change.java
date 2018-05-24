package com.example.misonglee.login_test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Account_Change extends AppCompatActivity {

    private String session;
    private String userID;
    private EditText change_pw;
    private String string_pw;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change);

        //세션 값
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        userID = intent.getStringExtra("userID");

        change_pw = (EditText) findViewById(R.id.change_pw);

        final ActionProcessButton btnChange = (ActionProcessButton) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });

    }
    public void change(){

        string_pw = change_pw.getText().toString();

        // 예외 처리들
        if(string_pw.length() < 8){
            change_pw.setError("8-12자의 영문 소문자, 숫자만 사용가능 합니다.");
        }

        //DB에 정보 수정
        ChangeDB changeDB = new ChangeDB();
        changeDB.execute();

    }
    class ChangeDB extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity.URL + "userPasswordChange.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" +  URLEncoder.encode(session, "UTF-8") + "&userPassword=" + URLEncoder.encode(string_pw, "UTF-8");
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
                String verify = jsonObject.getString("verify");
                // 서버로부터 반환 된 값이 1이면 수정 성공입니다.
                if(verify.equals("1")) {
                    // 성공 알림창을 띄우고 로그인 페이지로 이동.
                    change_successAlert();
                }
                // 그 외에는 실패
                else {
                    change_failAlert();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void change_successAlert(){
        new AwesomeSuccessDialog(this)
                .setTitle("비밀번호 변경 성공")
                .setMessage("비밀번호가 정상적으로 변경되었습니다.")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(false)
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
                        Intent register_intent = new Intent(Account_Change.this, Login.class);
                        startActivity(register_intent);
                    }
                })
                .show();
    }

    public void change_failAlert(){
        new AwesomeWarningDialog(this)
                .setTitle("비밀번호 변경 실패")
                .setMessage("비밀번호 변경에 실패했습니다.\n 다시 시도 해주세요")
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
