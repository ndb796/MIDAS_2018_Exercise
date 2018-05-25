package com.example.misonglee.login_test.pLogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.misonglee.login_test.MainActivity;
import com.example.misonglee.login_test.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Login extends AppCompatActivity {

    private AutoCompleteTextView user_id;
    private EditText user_pw;
    private CheckBox auto_check;

    private String string_id;
    private String string_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (AutoCompleteTextView) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);
        auto_check = (CheckBox)findViewById(R.id.auto_login);


        //자동로그인
        SharedPreferences autoLogin = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
        String auto_id = autoLogin.getString("user_id",null);
        String auto_pw = autoLogin.getString("user_pw", null);

        // 자동 로그인 저장 되어 있다면
        if(auto_id != null && auto_pw != null){
            Log.d("Raon","Auto Login");
            //id, pw 채우기
            user_id.setText(auto_id);
            user_pw.setText(auto_pw);
            //check box
            auto_check.setChecked(true);
            //로그인
            CheckFromDB checkFromDB = new CheckFromDB();
            checkFromDB.execute();
        }


        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        final ActionProcessButton mvRegister = (ActionProcessButton) findViewById(R.id.mvRegister);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                string_id = user_id.getText().toString();
                string_pw = user_pw.getText().toString();

                //id 입력 안돼있을 때
                if(TextUtils.isEmpty(string_id)){
                    user_id.setError("ID를 입력하세요");
                    return;
                }
                //pw 입력 안돼있을 때
                if(TextUtils.isEmpty(string_pw)){
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
            string_id = user_id.getText().toString();
            string_pw = user_pw.getText().toString();

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
                    //successAlert();

                    //자동 로그인 체크시
                    if(auto_check.isChecked()) {
                        SharedPreferences autoLogin = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin_editor = autoLogin.edit();
                        autoLogin_editor.putString("user_id", string_id);
                        autoLogin_editor.putString("user_pw", string_pw);
                        autoLogin_editor.commit();
                    }

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

/*
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
*/

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
