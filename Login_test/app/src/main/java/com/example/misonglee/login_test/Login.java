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

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


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
                }
                //pw 입력 안돼있을 때
                if(TextUtils.isEmpty(pw)){
                    user_pw.setError("PW를 입력하세요");
                }

                //id 와 pw가 등록되어있는지 비교 하기 !
                if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pw)){
                    //Intent main_intent = new Intent(Login.this, MainActivity.class);
                    //startActivity(main_intent);
                    //checkFromDB check = new checkFromDB();
                    //check.execute(id, pw);


                    //만약 맞다면 page 이동하기 !
                    if(check1){
                        Intent main_intent = new Intent(Login.this, MainActivity.class);
                        startActivity(main_intent);
                    }
                    else{
                        login_fail_Alert();
                    }

                }


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



    class checkFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon","Login doInBackground");

            try {
                /* DB 작업 */


            } catch (Exception e) {
                Log.e("Raon", "Exception: " + e.getMessage());
            }
            return null;
        }

        //서버로 부터 결과 값
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("Raon","onPostExecute");

            try{
                /* DB 작업*/


            }catch(Exception e){
                Log.e("Raon", "Exception: " + e.getMessage());
            }
        }
    }
    public void login_fail_Alert() {
        Log.d("Raon","login Alert");

        //dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("로그인 실패");
        alertDialogBuilder.setMessage("ID 혹은 PW가 등록되어있지 않습니다")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Dialog 템플릿


    }
}
