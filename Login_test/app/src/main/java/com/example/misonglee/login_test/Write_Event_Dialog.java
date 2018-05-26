package com.example.misonglee.login_test;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.misonglee.login_test.pMainActivity.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Write_Event_Dialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String userID;
    private String session;
    private String eventID;

    private String string_menuID, string_eventTitle, string_eventType, string_eventDiscount, string_eventStartDate, string_eventEndDate;
    private EditText menuID,eventTitle, eventType, eventDiscount;
    private TextView eventStartDate, eventEndDate;

    public Write_Event_Dialog(@NonNull Context _context, String _userID, String _session) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        context = _context;
        userID = _userID;
        session = _session;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Write_Content_Dialog", "onCreate - execute");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_event_dialog);

        ImageButton write_cancle = (ImageButton)findViewById(R.id.write_cancle);
        ImageButton delete = (ImageButton)findViewById(R.id.delete);
        ImageButton write_upload = (ImageButton)findViewById(R.id.write_upload);

        menuID = (EditText)findViewById(R.id.menuID);
        eventTitle = (EditText)findViewById(R.id.eventTitle);
        eventType = (EditText)findViewById(R.id.eventType);
        eventDiscount = (EditText)findViewById(R.id.eventDiscount);
        eventStartDate = (TextView)findViewById(R.id.eventStartDate);
        eventEndDate = (TextView)findViewById(R.id.eventEndDate);

        Button btnStartDate = (Button)findViewById(R.id.btnStartDate);
        Button btnEndDate = (Button)findViewById(R.id.btnEndDate);

        btnStartDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);

        write_cancle.setOnClickListener(this);
        write_upload.setOnClickListener(this);


        //내용이 있다면 보여줌
        if(eventTitle!=null){


        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write_cancle:
                cancel();
                break;
            case R.id.write_upload:
                WriteDB writeDB = new WriteDB();
                writeDB.execute();
                break;
            case R.id.btnStartDate:
                //날짜 선택하기
                DatePickerDialog start_dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                string_eventStartDate = String.format("%d-%02d-%02d",year, month+1, day);
                                eventStartDate.setText(string_eventStartDate);
                            }
                        } , 2018, 5, 1);
                start_dialog.show();
                break;
            case R.id.btnEndDate:
                DatePickerDialog end_dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                string_eventEndDate = String.format("%d-%02d-%02d",year, month+1, day);
                                eventEndDate.setText(string_eventEndDate);
                            }
                        } , 2018, 5, 1);
                end_dialog.show();
                break;
        }
    }



    class WriteDB extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {

            try {
                string_menuID = menuID.getText().toString();
                string_eventDiscount = eventDiscount.getText().toString();
                string_eventTitle = eventTitle.getText().toString();
                string_eventType = eventType.getText().toString();

                target = MainActivity.URL + "eventAdd.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8") + "&menuID="+ URLEncoder.encode(string_menuID, "UTF-8") + "&eventTitle=" + URLEncoder.encode(string_eventTitle, "UTF-8")+"&eventType="+URLEncoder.encode(string_eventType, "UTF-8") + "&eventDiscount="+URLEncoder.encode(string_eventDiscount, "UTF-8") + "&eventStartDate="+URLEncoder.encode(string_eventStartDate, "UTF-8") +"&eventEndDate="+URLEncoder.encode(string_eventEndDate, "UTF-8");
                Log.d("WriteDB", target);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("WriteDB", "doInBackground - execute");

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
                Log.e("WriteDB", "Exception: " + e.getMessage());
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

                switch (verify){
                    case "1":
                        //글 작성 성공
                        successAlert("글 작성 성공", "글 작성이 정상적으로 완료되었습니다.");
                        break;
                    case "-1":
                        //권한 없음
                        failAlert("글 작성 실패", "글 작성할 권한이 없습니다.");
                        break;
                    default:
                        failAlert("글 작성 실패","글 작성에 실패했습니다.");
                        //글 작성 실패
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }







    public void successAlert(String title, String message){
        Log.d("Write_Content_Dialog","successAlert execute");

        //회원가입 dialog
        new AwesomeSuccessDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("확인")
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        cancel();
                    }
                })
                .show();
    }

    public void failAlert(String title, String message) {
        Log.d("Write_Content_Dialog","failAlert Alert");

        //dialog
        new AwesomeWarningDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.dialogWarningBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_warning, R.color.black)
                .setCancelable(true)
                .setButtonText("확인")
                .setButtonBackgroundColor(R.color.dialogWarningBackgroundColor)
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        cancel();
                    }
                })
                .show();
    }




}
