package com.example.misonglee.login_test.pContents;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.R;

import java.util.ArrayList;

import static com.example.misonglee.login_test.pMainActivity.MainActivity.CODE_SHOW_DETAIL_CONTENTS;

public class Contents_Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Context context;
    private int resource;
    private View root_view;
    private LinearLayout root_layout;
    private ArrayList<ContentsData> items;
    private int items_size;

    public Contents_Fragment() {
        Log.d("Contents_Fragment", "Constructor - execute");

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
    public void SetItems(ArrayList<ContentsData> items, int items_size) {
        Log.d("Contents_Fragment", "SetItems - size : " + items_size);

        this.items = items;
        this.items_size = items_size;
    }

    /*
     * 새로운 Fragment Instance 생성 후 반환
     * */
    public static Contents_Fragment newInstance(int sectionNumber) {
        Log.d("Contents_Fragment", "newInstance-Number : " + sectionNumber);

        Contents_Fragment fragment = new Contents_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        // 임시로 지정
        ArrayList<ContentsData> tmp = new ArrayList<>();
        tmp.add(new ContentsData("2018-08-05", "[공지] 안녕하세요", "여기는 본문 내용입니다."));
        tmp.add(new ContentsData("2018-08-01", "[공지] 아아아아아", "여기는 본문 내용입니다."));
        fragment.SetItems(tmp, tmp.size());

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Contents_Fragment", "onCreateView-execute");

        root_view = inflater.inflate(R.layout.fragment_contents, container, false);
        root_layout = (LinearLayout) root_view.findViewById(R.id.ContentsRoot);
        context = container.getContext();

        //SetView();

        return root_view;
    }
/*
    public void SetView() {
        Log.d("Contents_Fragment", "SetView-execute");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items_size; i++) {
            View view = inflater.inflate(resource, root_layout, false);

            TextView date = (TextView) view.findViewById(R.id.contents_dateMsg);
            TextView title = (TextView) view.findViewById(R.id.contents_titleMsg);
            TextView message = (TextView) view.findViewById(R.id.contents_messageMsg);

            // final LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.contents_item);

            date.setText(items.get(i).date);
            title.setText(items.get(i).title);
            message.setText(items.get(i).message);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // MainActivity에서 FrameLayout을 건드릴 수 있는 거로 ㅇㅇ
                    // 변수로 데이터 넘기고, 그 데이터를 기반으로 Main에 저장되어있는 내용 보여주기 ㅇㅇ.
                    // 어차피 tab에서 보여주는 내용은 간추린 내용이기 때문이다.

                    Handler handler = ((MainActivity) context).messageHandler;
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();

                    // 데이터 처리작업.
                    String date = ((TextView)v.findViewById(R.id.contents_dateMsg)).getText().toString();
                    String title = ((TextView)v.findViewById(R.id.contents_titleMsg)).getText().toString();
                    String message = ((TextView)v.findViewById(R.id.contents_messageMsg)).getText().toString();

                    Log.d("asdfasdf", date + title + message);

                    bundle.putString("date",date);
                    bundle.putString("title",title);
                    bundle.putString("message",message);

                    msg.setData(bundle);
                    msg.what = CODE_SHOW_DETAIL_CONTENTS;

                    handler.sendMessage(msg);
                }
            });

            root_layout.addView(view);
        }
    }*/
}
