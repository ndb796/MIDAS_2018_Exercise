package com.example.misonglee.login_test.pClientMenu;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientReservation.ReserveData;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Client_Menu_Dialog extends Dialog {

    private Context context;
    private String name;
    private String price;
    private String menuID;
    private String userID;
    private String session;
    private String menuCount;

    private boolean result;
    private Integer item[];

    private Spinner spinner;
    private ArrayAdapter<Integer> adapter;
    private TextView price_result;
    private TextView barText;
    private Button okay_btn;
    private Button cancel_btn;

    public Client_Menu_Dialog(@NonNull Context context, String name, String price, String menuID , String userID, String session) {


        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);

        Log.d("Client_Menu_Dialog", "Client_Menu_Dialog - execute");

        this.name = name;
        this.price = price;
        this.menuID = menuID;
        this.userID = userID;
        this.session = session;
        this.context = context;

        item = new Integer[50];
        for(int i = 1; i<51; i++){
            item[i - 1] = i;
        }
        adapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setTitle(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Client_Menu_Dialog", "onCreate - execute");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu_dialog);

        spinner = (Spinner) findViewById(R.id.user_dialog_spinner);
        price_result = (TextView) findViewById(R.id.user_menu_dialog_priceresult);
        okay_btn = (Button) findViewById(R.id.user_menu_dialog_btn_okay);
        cancel_btn = (Button) findViewById(R.id.user_menu_dialog_btn_cancel);
        barText = (TextView)findViewById(R.id.barText);
        barText.setText(name + "주문");

        price_result.setText(String.valueOf(Integer.valueOf(price) * 1));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price_result.setText(String.valueOf(Integer.valueOf(price) * (i+1)));
                menuCount = String.valueOf(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                price_result.setText(String.valueOf(Integer.valueOf(price) * 1));
                menuCount = "1";
            }
        });

        okay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReserveDB reserveDB = new ReserveDB();
                reserveDB.execute();
                //MainActivity_User.list_items.add(new ReserveData(menuID, menuCount, 0)))
                //result = true;
                dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //result = false;
                //dismiss();
                cancel();
            }
        });
    }

    class ReserveDB extends AsyncTask<String, Void, String> {
        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {

            try {
                target = MainActivity.URL + "reservationReserve.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8") + "&menuID="+ URLEncoder.encode(menuID, "UTF-8") + "&menuCount=" + URLEncoder.encode(menuCount, "UTF-8");
                Log.d("ReserveDB", target);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("ReserveDB", "doInBackground - execute");
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
                Log.e("ReserveDB", "Exception: " + e.getMessage());
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

                if(verify.equals("1")){
                    //성공 Alert
                    new AwesomeSuccessDialog(context)
                            .setTitle("예약 성공")
                            .setMessage("예약이 완료되었습니다")
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
                    MainActivity_User.sectionsPagerAdapter.notifyDataSetChanged();
                }
                else{
                    //실패 Alert
                    new AwesomeWarningDialog(context)
                            .setTitle("예약 실패")
                            .setMessage("예약에 실패했습니다.")
                            .setColoredCircle(R.color.dialogWarningBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_warning, R.color.black)
                            .setCancelable(true)
                            .setButtonText("확인")
                            .setButtonBackgroundColor(R.color.dialogWarningBackgroundColor)
                            .setWarningButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                }
                            })
                            .show();

                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }


    public String getName(){
        return name;
    }

    public String getResult(){
        return price_result.getText().toString();
    }




}
