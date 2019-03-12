package com.pig.original.watch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pig.original.Common.Common;
import com.pig.original.R;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SleepFragment extends Fragment {
    private String userAccount;
    private WatchCommonTask wctSleep;
    private float sum =0;
    private static final String TAG = "SleepFragment";
    List<Float> yInput=new ArrayList<>(  );
    public SleepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences pref = getActivity().getSharedPreferences( Common.PREF_FILE, Context.MODE_PRIVATE );
        userAccount = pref.getString( "userAccount", "pig200a@hotmail.com" );
        userAccount = "pig200a@hotmail.com";
        View view = inflater.inflate( R.layout.sleep, container, false );
        BarChart barChart = view.findViewById( R.id.barChart );
        List<WatchSleep> watchSleeps = getSleep();
        List<BarEntry> sleepEntries = getSleepEntries( watchSleeps );

        List<BarEntry> deepSleepEntries =getDeepSleepEntries( watchSleeps );
        BarDataSet sleepBarDataSet = new BarDataSet( sleepEntries, "lightSleep" );
        BarDataSet deepSleepBarDataSet =new BarDataSet( deepSleepEntries,"DeepSleep" );
        sleepBarDataSet.setColor(Color.GREEN);
        deepSleepBarDataSet.setColor( Color.RED );
        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(sleepBarDataSet);
        dataSets.add(deepSleepBarDataSet);
        BarData barData = new BarData(dataSets);
        barChart.setData(barData);
        barChart.invalidate();
        handleView( view );
        return view;
    }

    private List<WatchSleep> getSleep() {
        List<WatchSleep> watchSleeps = null;
        if (WatchCommon.networkConnected( getActivity() )) {
            String url = WatchCommon.URL + "/WatchServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty( "action", "getSleep" );
            jsonObject.addProperty( "userAccount", userAccount );
            String jsonOut = jsonObject.toString();
            wctSleep = new WatchCommonTask( url, jsonOut );
            try {
                String jsonIn = wctSleep.execute().get();
                Type listType = new TypeToken<List<WatchSleep>>() {
                }.getType();
                watchSleeps = new Gson().fromJson( jsonIn, listType );
            } catch (Exception e) {
                Log.e( TAG, e.toString() );
            }
            if (watchSleeps == null || watchSleeps.isEmpty()) {
                WatchCommon.showToast( getActivity(), R.string.heart_no_found );
            }
        } else {
            WatchCommon.showToast( getActivity(), R.string.msg_NoNetwork );
        }
        return watchSleeps;
    }

    private List<BarEntry> getSleepEntries(List<WatchSleep> watchSleeps) {
        List<BarEntry> sleepEntries = new ArrayList<>();
        for (int l = 0; l < watchSleeps.size(); l++) {
            WatchSleep watchSleep = watchSleeps.get(l);
            float s = watchSleep.getSleep();
            float d = watchSleep.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("DD");
            float y = s/(60*60*1000);
            String b = sdf.format( d );
            float x = Float.valueOf( b );
            sleepEntries.add( new BarEntry( x, y ) );
        }
        return sleepEntries;
    }
    private List<BarEntry> getDeepSleepEntries(List<WatchSleep> watchSleeps) {
        List<BarEntry> deepSleepEntries = new ArrayList<>();
        for (int l = 0; l < watchSleeps.size(); l++) {
            WatchSleep watchSleep = watchSleeps.get(l);
            float s = watchSleep.getDeepSleep();
            float d = watchSleep.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("DD");
            float a = s/(60*60*1000);
            String b = sdf.format( d );
            float x = Float.valueOf( b );
            yInput.add( a );
            deepSleepEntries.add( new BarEntry( x, a ) );
        }
        return deepSleepEntries;
    }
    private void handleView(View view){
        float yMax = Collections.max( yInput );
        float yMin = Collections.min( yInput );
        float average =sum/yInput.size();
        DecimalFormat df = new DecimalFormat( ".00" );
        String sMAX =df.format( yMax );
        String sAVe =df.format( average );
        String sMin =df.format( yMin );
        TextView tvMAX = view.findViewById( R.id.tvMax );
        TextView tvAVe =view.findViewById( R.id.tvAVe );
        TextView tvMin =view.findViewById( R.id.tvMin );
        tvMAX.setText( sMAX );
        tvAVe.setText( sAVe );
        tvMin.setText( sMin );
    }
}
