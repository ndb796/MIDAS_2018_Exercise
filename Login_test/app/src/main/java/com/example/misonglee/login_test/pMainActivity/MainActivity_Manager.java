package com.example.misonglee.login_test.pMainActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misonglee.login_test.R;
import com.example.misonglee.login_test.pPagerAdapter.SectionsPagerAdapter_Manager;
import com.example.misonglee.login_test.Write_Content_Dialog;
import com.example.misonglee.login_test.pContents.ContentsData;

public class MainActivity_Manager extends AppCompatActivity{

    /* 로컬 서버 */
    public static String URL = "http://10.0.2.2:8080/MIDAS_Challenge_Application/";
    /* 카페24 호스팅 서버(나동빈) */
    // public static String URL = "http://www.dowellcomputer.com/MIDAS/";

    /* 아이디와 세션 값은 서버와 통신할 때마다 파라미터로 보내줘야 합니다. */
    public static String userID;
    public static String session;

    /* CODE 변수 */
    public static final int CODE_SHOW_DETAIL_CONTENTS = 1001;

    /* Message Handler */
    public MessageHandler messageHandler;

    /* 기본 UI와 관련된 변수 들 */
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter_Manager sectionsPagerAdapter;
    private LinearLayout detailcontents;
    private Button detailcontents_btn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity_Manager", "onCreate - execute");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_main);

        // 로그인 시 전달 받은 세션 값도 함께 저장합니다.
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        session = intent.getStringExtra("session");

        init();

        // 글쓰기 버튼
        FloatingActionButton write_button = (FloatingActionButton) findViewById(R.id.write_button);
        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //글쓰기 dialog
                Write_Content_Dialog write_dialog = new Write_Content_Dialog(MainActivity_Manager.this);
                write_dialog.show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        Log.d("MainActivity_Manager", "init - execute");

        // toolbar 초기화
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MIDAS_Example");
        setSupportActionBar(toolbar);

        // viewpager와 FragmentAdapter 연결
        sectionsPagerAdapter = new SectionsPagerAdapter_Manager(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        // tablayout과 viewPager 연결
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        Log.d("MainActivity", "init-finish");

        // Handler 초기화
        messageHandler = new MessageHandler();

        initActionbar();
    }

    /* 옵션바 초기화
     * 세부사항은 values->strings, styles를 확인할 것!
     * */
    private void initActionbar() {
        Log.d("MainActivity_Manager", "initActionbar - execute");

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("MIDAS_3조");
        actionBar.setIcon(R.mipmap.ic_launcher);

        // 앱의 왼쪽 위, 홈버튼을 사용하려면 활성화ㄱㄱ
        /*actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        // 옵션바 활성화, 비활성화 기능. 상황에 따라서 ㄱㄱ
        actionBar.hide();
        actionBar.show();
    }

    /* 옵션바에 대한 설정이 이루어지는 곳
     * 앱이 시작하면서 자동 실행
     * */
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity_Manager", "onCreateOptionsMenu - execute");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* 기본적으로 false반환. 제대로 실행이 되었다면, true를 반환해줘야 한다.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity_Manager", "onOptionsItemSelected - execute");

        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "item1 choice", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item2:
                Toast.makeText(this, "item2 choice", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item3:
                Toast.makeText(this, "item3 choice", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /* 지금은 받아온 데이터로 처리하지만, 나중에는 data를 토대로 정보를 찾아 출력해주는 걸로 바꾸면 됩니다. */
    private void ShowDetailContents(ContentsData data) {
        Log.d("MainActivity_Manager", "ShowDetailContents - execute");

        TextView date = (TextView) findViewById(R.id.detailContents_date);
        TextView title = (TextView) findViewById(R.id.detailContents_title);
        TextView message = (TextView) findViewById(R.id.detailContents_message);

        date.setText(data.getDate());
        title.setText(data.getTitle());
        message.setText(data.getMessage());

        detailcontents.setClickable(true);
        detailcontents.setVisibility(View.VISIBLE);
    }

    class MessageHandler extends Handler {
        Bundle bundle;

        @Override
        public void handleMessage(Message msg) {
            Log.d("MainActivity", "handleMessage - " + msg.what);
            super.handleMessage(msg);

            bundle = msg.getData();

            switch (msg.what) {
                case CODE_SHOW_DETAIL_CONTENTS:
                    ContentsData tmp = new ContentsData(bundle.getString("date"), bundle.getString("title"), bundle.getString("message"));
                    ShowDetailContents(tmp);
                    break;
            }
        }
    }

    public static String GetUserID() {
        return userID;
    }

    public static String GetUserPW() {
        return session;
    }
}
