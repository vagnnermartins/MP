package com.vagnnermartins.marcaponto.ui.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.widgets.SnackBar;
import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.enums.StatusEnum;
import com.vagnnermartins.marcaponto.ui.helper.TimesUIHelper;
import com.vagnnermartins.marcaponto.util.AlarmUtil;
import com.vagnnermartins.marcaponto.util.DataUtil;

import com.google.android.gms.ads.AdRequest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimesActivity extends ActionBarActivity {

    private App app;
    private TimesUIHelper ui;
    private boolean cancel;
    private TextView selectedTextView;
    private Time selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        init();
        loadValues();
    }

    private void init() {
        app = (App) getApplication();
        ui = new TimesUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initAdmob();
    }

    private void initAdmob() {
        AdRequest adRequestProd = new AdRequest.Builder()
                .addTestDevice("2C9AEADE49D960B9D04C47AD8B18EAEB")
                .build();
        ui.adView.loadAd(adRequestProd);
    }

    private void loadValues(){
        List<Time> times = new ArrayList<>(app.mapTimes.values());
        for(Time time : times){
            View itemView = getLayoutInflater().inflate(R.layout.item_time, null);
            setValue(itemView, R.id.item_time_day, DataUtil.getDayOfWeek(time.getId(), getResources()), null);
            setValue(itemView, R.id.item_time_entrance, time.getFormattedEntrance(), onEntranceClickListener());
            setValue(itemView, R.id.item_time_pause, time.getFormattedPause(), onPauseClickListener());
            setValue(itemView, R.id.item_time_back, time.getFormattedBack(), onBackClickListener());
            setValue(itemView, R.id.item_time_quit, time.getFormattedQuit(), onQuitClickListener());
            itemView.setTag(time);
            ui.list.addView(itemView);
        }
    }

    private View.OnClickListener onEntranceClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTime = (Time) ((ViewGroup) view.getParent()).getTag();
                selectedTextView = (TextView) view;
                Calendar calendar = Calendar.getInstance();
                if(selectedTime.getFormattedEntrance().equals("--:--")){
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                }else{
                    calendar.setTime(new Date(selectedTime.getEntrance()));
                }

                showTimePicker(R.string.entrance, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            }
        };
    }

    private View.OnClickListener onPauseClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTime = (Time) ((ViewGroup) view.getParent()).getTag();
                selectedTextView = (TextView) view;
                Calendar calendar = Calendar.getInstance();
                if(selectedTime.getFormattedPause().equals("--:--")){
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                }else{
                    calendar.setTime(new Date(selectedTime.getPause()));
                }
                showTimePicker(R.string.pause, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            }
        };
    }

    private View.OnClickListener onBackClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTime = (Time) ((ViewGroup) view.getParent()).getTag();
                selectedTextView = (TextView) view;
                Calendar calendar = Calendar.getInstance();
                if(selectedTime.getFormattedBack().equals("--:--")){
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                }else{
                    calendar.setTime(new Date(selectedTime.getBack()));
                }
                showTimePicker(R.string.back, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            }
        };
    }

    private View.OnClickListener onQuitClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTime = (Time) ((ViewGroup) view.getParent()).getTag();
                selectedTextView = (TextView) view;
                Calendar calendar = Calendar.getInstance();
                if(selectedTime.getFormattedQuit().equals("--:--")){
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                }else{
                    calendar.setTime(new Date(selectedTime.getQuit()));
                }
                showTimePicker(R.string.quit, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            }
        };
    }

    private void showTimePicker(int title, int hour, int minute){
        TimePickerDialog dialog = new TimePickerDialog(this, onTimeSetListener(), hour, minute, true);
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), onCancelClickListener());
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.delete), onDeleteClickListener());
        cancel = false;
        dialog.show();
    }

    private DialogInterface.OnClickListener onDeleteClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel = true;
                new SnackBar(TimesActivity.this, getString(R.string.deleted_message)).show();
                selectedTextView.setText(R.string.no_hour);
                prepareSaveTime(getString(R.string.no_hour), 0);
            }
        };
    }

    private DialogInterface.OnClickListener onCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel = true;
                selectedTime = null;
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener() {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(!cancel){
                    Calendar selectedDay = Calendar.getInstance();
                    selectedDay.setTime(new Date(0));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    DecimalFormat format = new DecimalFormat("00");
                    String text = format.format(selectedHour) + ":" + format.format(selectedMinute);
                    prepareSaveTime(text, calendar.getTime().getTime());
                }
            }
        };
    }

    private void prepareSaveTime(String text, long time) {
        selectedTextView.setText(text);
        switch (selectedTextView.getId()){
            case R.id.item_time_entrance:
                selectedTime.setEntrance(time);
                break;
            case R.id.item_time_pause:
                selectedTime.setPause(time);
                break;
            case R.id.item_time_back:
                selectedTime.setBack(time);
                break;
            case R.id.item_time_quit:
                selectedTime.setQuit(time);
                break;
        }
        selectedTime.save();
        app.historyFragment.checkStatus(StatusEnum.INICIO);
        AlarmUtil.cancellAllNotifications(this);
        AlarmUtil.scheduleAllNotification(this);
    }

    private void setValue(View view, int textViewId, String text, View.OnClickListener onClickListener){
        TextView textView = (TextView) view.findViewById(textViewId);
        textView.setText(text);
        textView.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
