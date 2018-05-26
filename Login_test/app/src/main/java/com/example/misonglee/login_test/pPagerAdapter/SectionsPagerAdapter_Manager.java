package com.example.misonglee.login_test.pPagerAdapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.misonglee.login_test.pClientMenu.Client_Menu_Fragment;
import com.example.misonglee.login_test.pClientNotice.Client_Notice_Fragment;
import com.example.misonglee.login_test.pClientReservation.Client_Reservation_Fragment;
import com.example.misonglee.login_test.pManagerManageSub.Manager_Manager_Sub_Fragment;
import com.example.misonglee.login_test.pManagerManageUser.Manager_Manager_User_Fragment;
import com.example.misonglee.login_test.pManagerMenu.Manager_Menu_Fragment;
import com.example.misonglee.login_test.pManagerNotice.Manager_Notice_Fragment;
import com.example.misonglee.login_test.pManagerReservation.Manager_Reservation_Fragment;
import com.example.misonglee.login_test.pNotice.Notice_Fragment;
import com.example.misonglee.login_test.pSetting.Setting_Fragment;

public class SectionsPagerAdapter_Manager extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter_Manager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.d("SectionsPagerAdapter", position + " - Fragment choice");

        switch(position){
            case 0:
                return Manager_Notice_Fragment.newInstance(position);

            case 1:
                return Manager_Menu_Fragment.newInstance(position);

            case 2:
                return Manager_Reservation_Fragment.newInstance(position);

            case 3:
                return Manager_Manager_User_Fragment.newInstance(position);

            case 4:
                return Manager_Manager_Sub_Fragment.newInstance(position);
        }
        return Notice_Fragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return 6;
    }

    /**
     * 옵션 정의라서 없어도 무관하지만, TabLayout에 아무것도 노출하지 않게 됩니다.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }

}
