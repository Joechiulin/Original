package com.pig.original.watch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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


public class HeartFragment extends Fragment {
    private LineChart lineChart;
    private WatchCommonTask wctHeart;
    private static final String TAG = "HeartFragment";
    private String userAccount;
    private float sum =0;
    List<Float> yInput=new ArrayList<>(  );
    List<Float> dInput=new ArrayList<>(  );

    public HeartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences pref = getActivity().getSharedPreferences( Common.PREF_FILE, Context.MODE_PRIVATE );
        userAccount = pref.getString( "userAccount", "pig200a@hotmail.com" );
        userAccount = "pig200a@hotmail.com";
        View view = inflater.inflate( R.layout.heart, container, false );
        lineChart = view.findViewById( R.id.lineChart );
        List<WatchHeart> watchHearts = getWatchHeart();
        List<Entry>
                heartEntries = getHeartEntries( watchHearts );
        handleView(view);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        float dataDate =Collections.max( dInput );
        SimpleDateFormat sdf =new SimpleDateFormat( "YYYY:MM:DD" );
        String stringDate = sdf.format( dataDate );
        Description description = new Description();
        description.setText("pig200a HeartBeat on "+stringDate);
        description.setTextSize(16);
        lineChart.setDescription( description );

        LineDataSet lineDataSet = new LineDataSet( heartEntries, "HeartBeat" );
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add( lineDataSet );
        LineData lineData = new LineData( dataSets );
        lineChart.setData( lineData );
        lineChart.invalidate();
        return view;
    }

    private List<WatchHeart> getWatchHeart() {
        List<WatchHeart> watchHearts = null;
        if (WatchCommon.networkConnected( getActivity() )) {
            String url = WatchCommon.URL + "/WatchServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty( "action", "getHeart" );
            jsonObject.addProperty( "userAccount", userAccount );
            String jsonOut = jsonObject.toString();
            wctHeart = new WatchCommonTask( url, jsonOut );
            try {
                String jsonIn = wctHeart.execute().get();
                Type listType = new TypeToken<List<WatchHeart>>() {
                }.getType();
                watchHearts = new Gson().fromJson( jsonIn, listType );
            } catch (Exception e) {
                Log.e( TAG, e.toString() );
            }
            if (watchHearts == null || watchHearts.isEmpty()) {
                WatchCommon.showToast( getActivity(), R.string.heart_no_found );
            }
        } else {
            WatchCommon.showToast( getActivity(), R.string.msg_NoNetwork );
        }
        return watchHearts;
    }

    private List<Entry> getHeartEntries(List<WatchHeart> watchHearts) {
        List<Entry> heartEntries = new ArrayList<>();
        for (int l = 0; l < watchHearts.size(); l++) {
            WatchHeart watchHeart = watchHearts.get( l );
            float y = watchHeart.getHeart();
            float date = watchHeart.getDate();
            float x = ((date % (60 * 60 * 24 * 1000)) / (60 * 60 * 1000)) + 8;
            heartEntries.add( new Entry( x, y ) );
            sum +=y;
            yInput.add( y );
            dInput.add(date);
        }
        return heartEntries;
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


