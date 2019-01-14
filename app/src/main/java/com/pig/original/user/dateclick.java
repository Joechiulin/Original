package com.pig.original.user;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pig.original.R;

import java.util.Calendar;

public class dateclick extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{
    private TextView editText;

    private int year, month, day, hour, minute;
    private void updateDisplay() {
        editText=findViewById(R.id.tvdate);
        editText.setText(new StringBuilder().append(year).append("-")
                .append(pad(month + 1)).append("-").append(pad(day))
                .append(" ").append(pad(hour)).append(":")
                .append(pad(minute)));
    }
    private String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        else {
            return "0" + String.valueOf(number);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        this.year = year;
        this.month = month;
        this.day = day;
        updateDisplay();

    }
    public static class DatePickerDialogFragment extends DialogFragment {
        /* 覆寫onCreateDialog()以提供想要顯示的日期挑選器 */
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            /* 呼叫getActivity()會取得此DialogFragment所依附的MainActivity物件 */
            dateclick activity = (dateclick) getActivity();
            /* DatePickerDialog建構式第2個參數為OnDateSetListener物件。
               因為MainActivity有實作OnDateSetListener的onDateSet方法，
               所以MainActivity物件亦為OnDateSetListener物件。
               year、month、day會成為日期挑選器預選的年月日 */
            return new DatePickerDialog( //datepickerdialog.getdatepicker()->datepicker datepicker.setmaxdate|.setmindate可設定最大獲最小選取日期
                    activity, activity,
                    activity.year, activity.month, activity.day);
        }
    }
    public void ondateclick(View view) {
        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        datePickerFragment.show(fm, "datePicker");
    }
}

