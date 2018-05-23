package com.example.misonglee.login_test;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

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
        }
        if( string_pw.length() < 8){
            register_pw.setError("8-12자의 영문 소문자, 숫자만 사용가능 합니다.");
        }

        //DB에 정보 삽입
        RegisterDB register_db = new RegisterDB();
        register_db.execute();

        // 성공 시 Alert
        register_Alert();

    }

    class RegisterDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Raon","Register doInBackground");

            try{
                /* DB 작업 */



            }catch (Exception e){
                Log.e("Raon", "Exception: " + e.getMessage());
            }
            return null;
        }
    }



    public void register_Alert(){
        //회원가입 Alert
        Log.d("Raon","Register Alert");

        //dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("회원가입 성공");
        alertDialogBuilder.setMessage("축하합니다 회원가입 되셨습니다.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
