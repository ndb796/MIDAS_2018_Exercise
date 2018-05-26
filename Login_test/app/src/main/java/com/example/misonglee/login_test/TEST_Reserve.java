package com.example.misonglee.login_test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TEST_Reserve extends AppCompatActivity {
    private ListView reserve_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate - execute");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_reserve);
        reserve_list = (ListView)findViewById(R.id.reserve_list);

        //TEST
        final String [] depart = {"개발부서", "영업부서","인사부서"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, depart);
        reserve_list.setAdapter(adapter);

        reserve_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Position에 따라 다이얼로그 내용 보여주면 됨
                AlertDialog builder = new AlertDialog.Builder(TEST_Reserve.this)
                        .setTitle(depart[position])
                        .setMessage("내용입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        }).show();

            }
        });

    }

}
