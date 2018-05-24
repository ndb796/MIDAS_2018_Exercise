package com.example.misonglee.login_test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Login extends AppCompatActivity {

    private AutoCompleteTextView user_id;
    private EditText user_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (AutoCompleteTextView) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);


        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        final ActionProcessButton mvRegister = (ActionProcessButton) findViewById(R.id.mvRegister);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String id = user_id.getText().toString();
                String pw = user_pw.getText().toString();

                Boolean check1 = Boolean.FALSE;

                //id 입력 안돼있을 때
                if(TextUtils.isEmpty(id)){
                    user_id.setError("ID를 입력하세요");
                    return;
                }
                //pw 입력 안돼있을 때
                if(TextUtils.isEmpty(pw)){
                    user_pw.setError("PW를 입력하세요");
                    return;
                }

                // DB에서 회원 정보 확인
                CheckFromDB checkFromDB = new CheckFromDB();
                checkFromDB.execute();
            }
        });

        mvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(Login.this, Register.class);
                startActivity(register_intent);
            }
        });

    }



    class CheckFromDB extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            String string_id = user_id.getText().toString();
            String string_pw = user_pw.getText().toString();
            try {
                target = MainActivity.URL + "userLogin.midas?userID=" + URLEncoder.encode(string_id, "UTF-8") + "&userPassword=" + URLEncoder.encode(string_pw, "UTF-8");
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
                String session = jsonObject.getString("session");
                // 서버로부터 반환 된 값이 1이면 로그인 성공입니다.
                if(verify.equals("1")) {
                    // 성공 알림창을 띄웁니다.
                    successAlert();
                    // 로그인 페이지로 이동합니다.
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    // 메인 페이지로 넘어갈 때 아이디와 세션 정보를 저장합니다.
                    intent.putExtra("userID", user_id.getText().toString());
                    intent.putExtra("session", session);
                    startActivity(intent);
                }
                // 그 외에는 로그인 실패 알림창을 띄웁니다.
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
                .setTitle("로그인 성공")
                .setMessage("로그인이 정상적으로 완료되었습니다.")
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
                .setTitle("로그인 실패")
                .setMessage("ID 혹은 PW가 맞지 않습니다")
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
