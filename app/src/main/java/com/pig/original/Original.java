package com.pig.original;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.pig.original.plan.PlanFragment;
import com.pig.original.sport.SportFragment;
import com.pig.original.user.Friendlistfragement;
import com.pig.original.user.UserFragment;
import com.pig.original.watch.WatchFragment;

public class Original extends AppCompatActivity {
    private BottomNavigationView oriNavigation,navigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.itemCommunity:
                    fragment = new CommunityFragment();
                    break;
                case R.id.itemSport:
                    fragment = new SportFragment();
                    break;
                case R.id.itemWatch:
                    fragment =new WatchFragment();
                    break;
                case R.id.itemPlan:
                    fragment =new PlanFragment();
                    break;
                case R.id.itemMyself:
                    fragment=new Friendlistfragement();
                    break;

            }
            item.setChecked( true );
            //選取選項反藍
            changeFragment( fragment );
            setTitle( item.getTitle() );
            return false;
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.itemAchievement:
                    fragment= new Friendlistfragement();
                case  R.id.itemFriends:
                    fragment= new Friendlistfragement();
                    break;
            }
            item.setChecked( true );
            //選取選項反藍
            changeFragment( fragment );
            setTitle( item.getTitle() );
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_original );
        oriNavigation = findViewById( R.id.oriNavigation );
        oriNavigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        initContent();
    }

    private void initContent() {
        oriNavigation.setSelectedItemId( R.id.itemCommunity );
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.oriFrameLayout, fragment );
        fragmentTransaction.commit();
    }


}

