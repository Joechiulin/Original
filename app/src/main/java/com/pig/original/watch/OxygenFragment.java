package com.pig.original.watch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class OxygenFragment extends Fragment {
    private String userAccount;
    private WatchCommonTask wctOxygen;
    private float sum =0;
    private static final String TAG = "OxygenFragment";
    List<Float> yInput=new ArrayList<>(  );
    List<Float> dInput=new ArrayList<>(  );



    public OxygenFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.oxygen,container,false );
        SharedPreferences pref = getActivity().getSharedPreferences( Common.PREF_FILE, Context.MODE_PRIVATE );
        userAccount = pref.getString( "userAccount", "pig200a@hotmail.com" );
        userAccount = "pig200a@hotmail.com";
        LineChart lineChart = view.findViewById( R.id.lcOxygen );
        List<Entry>
                oxygenEntries = getOxygenEntries(getWatchOxygen());
        handleView(view);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        float dataDate =Collections.max( dInput );
        SimpleDateFormat sdf =new SimpleDateFormat( "YYYY:MM:DD" );
        String stringDate = sdf.format( dataDate );
        Description description = new Description();
        description.setText("pig200a BloodOxygen on "+stringDate);
        description.setTextSize(16);
        lineChart.setDescription( description );

        LineDataSet lineDataSet = new LineDataSet( oxygenEntries, "BloodOxygen" );
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add( lineDataSet );
        LineData lineData = new LineData( dataSets );
        lineChart.setData( lineData );
        lineChart.invalidate();
        return view;
    }

    private List<WatchOxygen> getWatchOxygen() {
        List<WatchOxygen> watchOxygens = null;
        if (WatchCommon.networkConnected( getActivity() )) {
            String url = WatchCommon.URL + "/WatchServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty( "action", "getOxygen" );
            jsonObject.addProperty( "userAccount", userAccount );
            String jsonOut = jsonObject.toString();
            wctOxygen = new WatchCommonTask( url, jsonOut );
            try {
                String jsonIn = wctOxygen.execute().get();
                Type listType = new TypeToken<List<WatchOxygen>>() {
                }.getType();
                watchOxygens = new Gson().fromJson( jsonIn, listType );
            } catch (Exception e) {
                Log.e( TAG, e.toString() );
            }
            if (watchOxygens == null || watchOxygens.isEmpty()) {
                WatchCommon.showToast( getActivity(), R.string.oxygen_no_found );
            }
        } else {
            WatchCommon.showToast( getActivity(), R.string.msg_NoNetwork );
        }
        return watchOxygens;
    }

    private List<Entry> getOxygenEntries(List<WatchOxygen> watchOxygens) {
        List<Entry> oxygenEntries = new ArrayList<>();
        for (int l = 0; l < watchOxygens.size(); l++) {
            WatchOxygen watchOxygen = watchOxygens.get( l );
            float y = watchOxygen.getBloodoxygen();
            float date = watchOxygen.getDate();
            float x = ((date % (60 * 60 * 24 * 1000)) / (60 * 60 * 1000)) + 8;
            oxygenEntries.add( new Entry( x, y ) );
            sum +=y;
            yInput.add( y );
            dInput.add(date);
        }
        return oxygenEntries;
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
