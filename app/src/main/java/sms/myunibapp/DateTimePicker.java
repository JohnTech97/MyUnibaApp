package sms.myunibapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePicker {


    public static void getDateTime(Context ctx, TextView value){

        Calendar clr=Calendar.getInstance();
        new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {//definisco il funzionamento del date picker
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                clr.set(Calendar.YEAR, year);
                clr.set(Calendar.MONTH, month);
                clr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

                value.setText(dateFormat.format(clr.getTime()));

                new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {//funzionamento del time picker
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        clr.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        clr.set(Calendar.MINUTE, minute);
                        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");

                        value.append(" "+timeFormat.format(clr.getTime()));
                    }
                }, clr.get(Calendar.HOUR_OF_DAY), clr.get(Calendar.MINUTE), true).show();
            }
        }, clr.get(Calendar.YEAR), clr.get(Calendar.MONTH), clr.get(Calendar.DAY_OF_MONTH)).show();
    }

}