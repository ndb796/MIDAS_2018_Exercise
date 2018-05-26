package com.example.misonglee.login_test.pClientReservation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientMenu.MenuData;
import com.example.misonglee.login_test.pMainActivity.MainActivity_User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Client_Reservation_ListAdapter extends BaseAdapter {

    private ArrayList<ReserveData> items;
    private int listsize;
    private LayoutInflater inflater;
    private Context context;
    private int reservationID;

    public Client_Reservation_ListAdapter(ArrayList<ReserveData> items) {
        this.items = items;
        listsize = items.size();
    }

    @Override
    public int getCount() {
        return listsize;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            context = viewGroup.getContext();

            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            view = inflater.inflate(R.layout.reserve_item, viewGroup, false);
        }

        final TextView reserID = (TextView) view.findViewById(R.id.reservationID);
        TextView menu = (TextView) view.findViewById(R.id.reserve_item_menu);
        TextView menu_count = (TextView) view.findViewById(R.id.reserve_item_menucount);
        TextView process = (TextView) view.findViewById(R.id.reserve_item_process);

        reserID.setText(String.valueOf(items.get(position).reservationID));
        //menu.setText(Integer.toString(items.get(position).menuID));
        menu.setText(Client_Reservation_Fragment.list_menu.get(items.get(position).menuID));
        menu_count.setText(Integer.toString(items.get(position).menuCount) + "개");
        switch (items.get(position).reservationProcess) {
            case 0:
                process.setText("접수 완료");
                break;

            case 1:
                process.setText("진행 중");
                break;

            case 2:
                process.setText("제작 완료");
                break;
        }
        //process.setText(Integer.toString(items.get(position).reservationProcess));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ClientReservation", "click되었습니다.");
                TextView process = (TextView) view.findViewById(R.id.reserve_item_process);
                TextView id = (TextView) view.findViewById(R.id.reservationID);
                reservationID = Integer.valueOf(id.getText().toString());
                Log.d("ClientReservation", process.getText().toString());
                if (process.getText().toString().equals("제작 완료") == true) {
                    Log.d("ClientReservation", "알람실행!");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    alertDialogBuilder.setTitle("제작 확정");

                    alertDialogBuilder
                            .setMessage("제작 확정 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("네",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 프로그램을 종료한다
                                            BackgroundTask_TakeOut backgroundTask_takeOut = new BackgroundTask_TakeOut();
                                            backgroundTask_takeOut.execute();
                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton("아니요",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();
                                        }
                                    }).show();

                }

            }
        });
        return view;
    }

    class BackgroundTask_TakeOut extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {
            try {
                target = MainActivity_User.URL + "reservationComplete.midas?userID=" + MainActivity_User.GetUserID() + "&session="+MainActivity_User.GetUserPW()+"&reservationID=" + reservationID;
                Log.d("Raon",target);
                Log.d("Raon",reservationID +" ");
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

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
