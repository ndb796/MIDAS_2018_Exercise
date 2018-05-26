package com.example.misonglee.login_test.pLogin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
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

public class Register extends AppCompatActivity{

    private AutoCompleteTextView register_id;
    private EditText register_pw;
    private EditText register_name;

    private TextView outputDate;

    private String string_id;
    private String string_pw;
    private String register_depart;

    private int register_year, register_month, register_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_id = (AutoCompleteTextView) findViewById(R.id.register_id);
        register_pw = (EditText) findViewById(R.id.register_pw);
        outputDate = (TextView)findViewById(R.id.outputDate);
        register_name = (EditText) findViewById(R.id.register_name);

        final ActionProcessButton btnRegister = (ActionProcessButton) findViewById(R.id.btnRegister);
        final Spinner spinner_depart = (Spinner) findViewById(R.id.register_depart);
        final String [] depart = {"개발부서", "영업부서","인사부서"};

        //부서 보여주기
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, depart);
        spinner_depart.setAdapter(adapter);
        spinner_depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                register_depart = (String)depart[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user_Register();
            }
        });

    }


    public void InputDate(View view){
        //날짜 선택하기
        final String[] string_year = new String[1];
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        register_year = year;
                        register_month = month+1;
                        register_day = day;
                        outputDate.setText(register_year+"-" + register_month + "-" + register_day);
                    }
                } , 2000, 1, 1);
        dialog.show();
    }

    private void user_Register(){

        //입력 값 예외 처리
        string_id = register_id.getText().toString();
        string_pw = register_pw.getText().toString();

        if( string_id.length() < 5) {
            register_id.setError("5-10자의 영문 소문자, 숫자만 사용가능 합니다.");
            return;
        }
        if( string_pw.length() < 8){
            register_pw.setError("8-12자의 영문 소문자, 숫자만 사용가능 합니다.");
            return;
        }



        //정보 확인 메세지
        Toast.makeText(Register.this, "부서 : "+ register_depart + "생년월일 : "+ register_year+"-" + register_month + "-" + register_day,Toast.LENGTH_SHORT).show();


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

            //TODO:넘겨줄 데이터들
            //회원타입 = 무조건 일반회원으로
            //이름 register_name.getText().toString();
            //부서 register_depart
            //생일 outputDate.getText().toString();

            try {
                target = MainActivity.URL + "userJoin.midas?userID=" + URLEncoder.encode(string_id, "UTF-8") + "&userPassword=" + URLEncoder.encode(string_pw, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("RegisterDB","doInBackground execute");

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
                Log.e("RegisterDB", "Exception: " + e.getMessage());
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
                    // 성공 알림창을 띄우고 로그인 페이지로 이동.
                    successAlert();

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
        Log.d("Register","Register Alert execute");

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
                        // 로그인 페이지로 이동합니다.
                        Intent register_intent = new Intent(Register.this, Login.class);
                        startActivity(register_intent);
                    }
                })
                .show();
    }

    public void failAlert() {
        Log.d("Register","login Alert execute");

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
