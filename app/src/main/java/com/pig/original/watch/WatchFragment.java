package com.pig.original.watch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pig.original.R;

public class WatchFragment extends Fragment {
    private TabLayout tlWatch;
    private ViewPager vpWatch;
    private PagerAdapter paWatch;
    private TabItem tiHeart, tiOxygen, tiSleep;
    private View vWatch;

    public WatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vWatch = inflater.inflate( R.layout.watch, container, false );
        handleView();
        return vWatch;
    }


    private void handleView() {
        tlWatch = vWatch.findViewById( R.id.tlWatch );
        vpWatch = vWatch.findViewById( R.id.vpWatch );
        tiHeart = vWatch.findViewById( R.id.tiHeart );
        tiOxygen = vWatch.findViewById( R.id.tiOxygen );
        tiSleep = vWatch.findViewById( R.id.tiSleep );

        tlWatch.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpWatch.setCurrentItem( tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );
        vpWatch.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tlWatch ) );
        paWatch = new WatchPageAdapter( getActivity().getSupportFragmentManager(), tlWatch.getTabCount() );

        vpWatch.setAdapter( paWatch );
    }

}