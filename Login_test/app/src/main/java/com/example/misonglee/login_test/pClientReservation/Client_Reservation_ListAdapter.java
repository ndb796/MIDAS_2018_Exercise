package com.example.misonglee.login_test.pClientReservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.misonglee.login_test.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Client_Reservation_ListAdapter extends BaseAdapter {

    private ArrayList<ReserveData> items;
    private int listsize;
    private LayoutInflater inflater;

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
            Context context = viewGroup.getContext();

            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            view = inflater.inflate(R.layout.reserve_item, viewGroup, false);
        }

        TextView menu = (TextView) view.findViewById(R.id.reserve_item_menu);
        TextView menu_count = (TextView) view.findViewById(R.id.reserve_item_menucount);
        TextView process = (TextView) view.findViewById(R.id.reserve_item_process);

        menu.setText(Integer.toString(items.get(position).menuID));
        menu_count.setText(Integer.toString(items.get(position).menuCount));
        process.setText(Integer.toString(items.get(position).reservationProcess));

        return view;
    }
}
