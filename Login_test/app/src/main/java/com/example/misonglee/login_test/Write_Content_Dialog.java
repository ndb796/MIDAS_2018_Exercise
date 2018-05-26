package com.example.misonglee.login_test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Write_Content_Dialog extends Dialog implements View.OnClickListener {

    private EditText write_title;
    private EditText write_content;
    private String string_title;
    private String string_content;

    public Write_Content_Dialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Write_Content_Dialog", "onCreate - execute");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_dialog);


        write_title = (EditText) findViewById(R.id.write_title);
        write_content = (EditText) findViewById(R.id.write_content);

        ImageButton write_cancle = (ImageButton)findViewById(R.id.write_cancle);
        ImageButton write_save = (ImageButton)findViewById(R.id.write_save);
        ImageButton write_upload = (ImageButton)findViewById(R.id.write_upload);

        write_cancle.setOnClickListener(this);
        write_save.setOnClickListener(this);
        write_upload.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write_cancle:
                cancel();
                break;
            case R.id.write_upload:
                upload_content();
                break;
        }
    }



    public void upload_content(){
        Log.d("Write_Content_Dialog", "upload_content - execute");

        //글 업로드



    }


}
