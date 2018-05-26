package com.example.misonglee.login_test.pManagerManageUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pManagerReservation.ReserveData;

import java.util.ArrayList;

public class Manager_Manager_User_ListAdapter extends BaseAdapter {

    private ArrayList<UserData> items;
    private int listsize;
    private LayoutInflater inflater;

    public Manager_Manager_User_ListAdapter(ArrayList<UserData> items) {
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
            Context context = viewGroup.getContext();

            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            view = inflater.inflate(R.layout.reserve_item, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.text_name);
        TextView pw = (TextView) view.findViewById(R.id.text_pw);
        TextView birthday = (TextView) view.findViewById(R.id.text_birthday);
        TextView depart = (TextView) view.findViewById(R.id.text_depart);

        name.setText(items.get(position).name);
        pw.setText(items.get(position).pw);
        birthday.setText(items.get(position).birthday);
        depart.setText(items.get(position).depart);

        return view;
    }
}
