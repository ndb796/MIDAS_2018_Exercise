package com.example.misonglee.login_test.pManagerMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pClientMenu.Client_Menu_Dialog;

import java.util.ArrayList;

public class Manager_Menu_Fragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<MenuData> items;
    private int items_size;

    public Manager_Menu_Fragment() {
        Log.d("Manager_Menu_Fragment", "Constructor - execute");

        items = null;
        items_size = 0;
        resource = R.layout.contents_item;
    }

    /*
     * 이 부분, 다른 곳에서 정보를 가져오는 걸로 바꿔야 할까?
     * 1) Main에 저장된 공지사항 정보를 가져온다?
     * 2) 통신으로 공지사항 정보를 가져온다?
     * items : View에 넣을 데이터 전부.
     * items_size : 데이터 사이즈.
     * */
    public void SetItems(ArrayList<MenuData> items, int items_size) {
        Log.d("Manager_Menu_Fragment", "SetItems - size : " + items_size);

        this.items = items;
        this.items_size = items_size;
    }

    /*
     * 새로운 Fragment Instance 생성 후 반환
     * */
    public static Manager_Menu_Fragment newInstance(int sectionNumber) {
        Log.d("Manager_Menu_Fragment", "newInstance-Number : " + sectionNumber);

        Manager_Menu_Fragment fragment = new Manager_Menu_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        // 임시로 지정
        ArrayList<MenuData> tmp = new ArrayList<>();
        tmp.add(new MenuData("아메리카노","1234", "10","[공지] 안녕하세요", "여기는 본문 내용입니다."));
        tmp.add(new MenuData("자바칩 플랫치노", "4432", "15","[공지] 아아아아아", "여기는 본문 내용입니다."));
        fragment.SetItems(tmp, tmp.size());

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Manager_Menu_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.manager_fragment_menu, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.manager_menu_root);
        context = container.getContext();

        SetView();

        return root_view;
    }

    public void SetView() {
        Log.d("Manager_Menu_Fragment", "SetView-execute");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items_size; i++) {
            View view = inflater.inflate(resource, root_layout, false);

            TextView name = (TextView) view.findViewById(R.id.contents_nameMsg);
            TextView price = (TextView) view.findViewById(R.id.contents_priceMsg);
            TextView discount_rate = (TextView) view.findViewById(R.id.contents_discount_rateMsg);
            TextView detail = (TextView) view.findViewById(R.id.contents_detailMsg);
            TextView picture = (TextView) view.findViewById(R.id.contents_pictureMsg);


            // final LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.contents_item);

            name.setText(items.get(i).name);
            price.setText(items.get(i).price);
            discount_rate.setText(items.get(i).discount_rate);
            detail.setText(items.get(i).detail);
            picture.setText(items.get(i).picture);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView)v.findViewById(R.id.contents_nameMsg)).getText().toString();
                    String price = ((TextView)v.findViewById(R.id.contents_priceMsg)).getText().toString();

                    final Manager_Menu_Dialog manager_menu_dialog = new Manager_Menu_Dialog(context, name, price);
                    manager_menu_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String name = manager_menu_dialog.getName();
                            String price = manager_menu_dialog.getResult();

                            // name이랑 price를 가지고 서버로 데이터 전송하기.
                        }
                    });
                    manager_menu_dialog.show();
                }
            });

            root_layout.addView(view);
        }
    }
}
