package com.example.misonglee.login_test.pPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.misonglee.login_test.pContents.Contents_Fragment;
import com.example.misonglee.login_test.pNotice.Notice_Fragment;
import com.example.misonglee.login_test.pSetting.Setting_Fragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.d("SectionsPagerAdapter", position + " - Fragment choice");

        switch(position){
            case 0:
                return Notice_Fragment.newInstance(position);

            case 1:
                return Contents_Fragment.newInstance(position);

            case 2:
                return Setting_Fragment.newInstance(position);
        }
        return Notice_Fragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
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