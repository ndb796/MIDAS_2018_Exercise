package com.example.misonglee.login_test.pNotice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.misonglee.login_test.R;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter{
    private ArrayList<NoticeData> datas;
    private int data_size;
    private LayoutInflater inflater;

    public NoticeAdapter(ArrayList<NoticeData> datas) {
        this.datas = datas;
        data_size = datas.size();
    }

    @Override
    public int getCount() {
        return data_size;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.notice_item, parent, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.Date);
        TextView title = (TextView) convertView.findViewById(R.id.Title);
        TextView message = (TextView) convertView.findViewById(R.id.Message);

        date.setText(datas.get(position).date);
        title.setText(datas.get(position).title);
        message.setText(datas.get(position).message);

        return convertView;
    }
}
