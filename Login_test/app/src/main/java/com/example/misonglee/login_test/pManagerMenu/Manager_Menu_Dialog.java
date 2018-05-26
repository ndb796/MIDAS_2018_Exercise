package com.example.misonglee.login_test.pManagerMenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.misonglee.login_test.R;

public class Manager_Menu_Dialog extends Dialog {

    private String name;
    private String price;
    private boolean result;
    private Integer item[];

    private Spinner spinner;
    private ArrayAdapter<Integer> adapter;
    private TextView price_result;
    private Button okay_btn;
    private Button cancel_btn;

    public Manager_Menu_Dialog(@NonNull Context context, String name, String price) {
        super(context);

        this.name = name;
        this.price = price;
        item = new Integer[50];
        for(int i = 1; i<51; i++){
            item[i - 1] = i;
        }
        adapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setTitle(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Manager_Menu_Dialog", "onCreate - execute");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_menu_dialog);

        spinner = (Spinner) findViewById(R.id.manager_dialog_spinner);
        price_result = (TextView) findViewById(R.id.manager_menu_dialog_priceresult);
        okay_btn = (Button) findViewById(R.id.manager_menu_dialog_btn_okay);
        cancel_btn = (Button) findViewById(R.id.manager_menu_dialog_btn_cancel);

        Log.d("asdfasdf", "asdfasdf" + price + "asdfasdf" + price_result);

        price_result.setText(String.valueOf(Integer.valueOf(price) * 1));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price_result.setText(String.valueOf(Integer.valueOf(price) * (i+1)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                price_result.setText(String.valueOf(Integer.valueOf(price) * 1));
            }
        });

        okay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = true;
                dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = false;
                dismiss();
            }
        });
    }

    public String getName(){
        return name;
    }

    public String getResult(){
        return price_result.getText().toString();
    }
}
