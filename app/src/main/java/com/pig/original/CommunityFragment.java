package com.pig.original;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pig.original.Community.ComPageAdapter;
import com.pig.original.user.HomePageAdapter;

public class CommunityFragment extends Fragment {
    private Toolbar comToolbar;
    private TabLayout comtabLayout;
    private ViewPager comViewPager;
    private PagerAdapter comPagerAdapter;
    private TabItem comTabcom, comTabFriend, comTabPersonal;
    private View com_fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView( inflater, container, savedInstanceState );

          com_fragment =inflater.inflate( R.layout.communitymain,container,false );
        handalviews();
        return com_fragment;
    }

    private void handalviews() {

//        homeToolbar = home_fragment.findViewById(R.id.homeToolbar);
        comtabLayout = com_fragment.findViewById(R.id.comTabLayout);
        comViewPager = com_fragment.findViewById(R.id.comViewPager);
        comTabcom = com_fragment.findViewById(R.id.comTabCom);
        comTabFriend = com_fragment.findViewById(R.id.comTabFriend);
        comTabPersonal = com_fragment.findViewById(R.id.comTabFriend);

        //tabLayout監聽
        comtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                comViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //刷新 Fragment 頁面
        comViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(comtabLayout));
        //取得FragmentManager權限 並取得目前分頁所在的頁數
        comPagerAdapter = new ComPageAdapter(getActivity().getSupportFragmentManager(), comtabLayout.getTabCount());
        //將剛剛取到的分頁所在的頁數 顯示在Fragment上
        comViewPager.setAdapter(comPagerAdapter);


    }
}
