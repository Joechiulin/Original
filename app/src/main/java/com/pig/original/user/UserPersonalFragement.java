package com.pig.original.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pig.original.R;

import java.util.Calendar;

public class UserPersonalFragement extends Fragment {
    private TextView editText;
    private int year, month, day, hour, minute;


    public UserPersonalFragement(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
         View view =inflater.inflate(R.layout.personal, container, false);
         editText = view.findViewById(R.id.tvdate);
         findview(view);
        return view;


    }

    private void findview(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

    }
    private void updateDisplay() {
        editText.setText(new StringBuilder().append(year).append("-")
                .append(pad(month + 1)).append("-").append(pad(day))
                .append(" ").append(pad(hour)).append(":")
                .append(pad(minute)));
    }

    /* 若數字有十位數，直接顯示；
       若只有個位數則補0後再顯示，例如7會改成07後再顯示 */
    private String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        else {
            return "0" + String.valueOf(number);
        }
    }


}
