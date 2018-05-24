package com.example.misonglee.login_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Register extends AppCompatActivity{

    private AutoCompleteTextView register_id;
    private EditText register_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_id = (AutoCompleteTextView) findViewById(R.id.register_id);
        register_pw = (EditText) findViewById(R.id.register_pw);

        final ActionProcessButton btnRegister = (ActionProcessButton) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user_Register();
            }
        });

    }

    private void user_Register(){

        //입력 값 예외 처리
        String string_id = register_id.getText().toString();
        String string_pw = register_pw.getText().toString();

        if( string_id.length() < 5) {
            register_id.setError("5-10자의 영문 소문자, 숫자만 사용가능 합니다.");
            return;
        }
        if( string_pw.length() < 8){
            register_pw.setError("8-12자의 영문 소문자, 숫자만 사용가능 합니다.");
            return;
        }

        //DB에 정보 삽입
        RegisterDB registerDB = new RegisterDB();
        registerDB.execute();

    }

    class RegisterDB extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            String string_id = register_id.getText().toString();
            String string_pw = register_pw.getText().toString();
            try {
                target = MainActivity.URL + "userJoin.midas?userID=" + URLEncoder.encode(string_id, "UTF-8") + "&userPassword=" + URLEncoder.encode(string_pw, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon","Register doInBackground");

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
                // 서버로부터 반환 된 값이 1이면 회원가입 성공입니다.
                if(verify.equals("1")) {
                    // 성공 알림창을 띄웁니다.
                    successAlert();
                    // 로그인 페이지로 이동합니다.
                    Intent register_intent = new Intent(Register.this, Login.class);
                    startActivity(register_intent);
                }
                // 그 외에는 이미 존재하는 아이디로 처리합니다.
                else {
                    failAlert();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void successAlert(){
        //회원가입 Alert
        Log.d("Raon","Register Alert");

        //회원가입 dialog
        new AwesomeSuccessDialog(this)
                .setTitle("회원가입 성공")
                .setMessage("회원가입이 정상적으로 완료되었습니다.")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("확인")
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click
                    }
                })
                .show();
    }


    public void failAlert() {
        Log.d("Raon","login Alert");

        //dialog
        new AwesomeWarningDialog(this)
                .setTitle("회원가입 실패")
                .setMessage("이미 존재하는 아이디입니다.")
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
