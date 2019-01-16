package com.pig.original.Community;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pig.original.CommunityFragment;
import com.pig.original.user.Friendlistfragement;
import com.pig.original.user.UserPersonalFragement;

public class ComPageAdapter extends FragmentStatePagerAdapter{
        //建立屬性 分頁頁數計數器
        private int numOfTabs;

        //建構式
        public ComPageAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        //取得分頁畫面與內容
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Community();
                case 1:
                    return new ComFriend();
                case 2:
                    return new ComPersonal();
                default:
                    return null;
            }

        }

        //取得分頁頁數
        @Override
        public int getCount() {
            return numOfTabs;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



}
