package com.vagnnermartins.marcaponto.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.adapter.HistoryAdapter;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.enums.StatusEnum;
import com.vagnnermartins.marcaponto.task.FindHistoriesAsyncTask;
import com.vagnnermartins.marcaponto.task.SaveHistoryAsyncTask;
import com.vagnnermartins.marcaponto.ui.helper.HistoryUIHelper;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class HistoryFragment extends Fragment {

    public static final int POSITION = 1;
    public static final int NAME_TAB = R.string.fragment_history;
    private static final int REQUEST_SAVE = 0;
    private static final int REQUEST_SHARE = 1;

    private App app;
    private HistoryUIHelper ui;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ui = new HistoryUIHelper(inflater.inflate(R.layout.fragment_history, container, false));
        init();
        checkUpdate();
        return ui.view;
    }

    private void checkUpdate() {
        if(app.mapListHistories.get(DataUtil.getMonthYear(app.dateHistory.getTime())) == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            loadValues();
        }
    }

    private void init() {
        setHasOptionsMenu(true);
        app = (App) getActivity().getApplication();
        ui.date.setOnClickListener(onDateClickListener());
        ui.previous.setOnClickListener(onPreviousClickListener());
        ui.next.setOnClickListener(onNextClickListener());
        ui.list.setOnItemClickListener(onItemClickListener());
        loadDate();
    }

    public void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            FindHistoriesAsyncTask task = new FindHistoriesAsyncTask(getActivity(), onFindHistoryCallback(), app.dateHistory.getTime());
            task.execute();
            app.registerTask(task);
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(status == StatusEnum.EXECUTANDO){
            ui.progress.setVisibility(View.VISIBLE);
        }else if(status == StatusEnum.EXECUTADO){
            ui.progress.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View detail = view.findViewById(R.id.item_history_detail);
                if(detail.getVisibility() == View.GONE){
                    detail.setVisibility(View.VISIBLE);
                    view.setSelected(true);
                }else{
                    detail.setVisibility(View.GONE);
                    view.setSelected(false);
                }
            }
        };
    }

    private void loadDate(){
        String date = DataUtil.getMonth(app.dateHistory.get(Calendar.DAY_OF_MONTH), getResources()) +
                " / " + app.dateHistory.get(Calendar.YEAR);
        ui.date.setText(date);
    }

    private void loadValues(){
        ui.list.setAdapter(new HistoryAdapter(getActivity(),
                R.layout.item_history,
                app.mapListHistories.get(DataUtil.getMonthYear(app.dateHistory.getTime())),
                app.mapTimes));
        calcMonthBalance(app.mapListHistories.get(DataUtil.getMonthYear(app.dateHistory.getTime())));
    }

    private Callback onFindHistoryCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                List<History> histories = (List<History>) objects[0];
                app.mapListHistories.put(DataUtil.getMonthYear(app.dateHistory.getTime()), histories);
                loadValues();
                calcMonthBalance(histories);
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private void calcMonthBalance(List<History> histories) {
        int totalWorkedHours = 0;
        int totalBalance = 0;
        for(History item : histories){
            Calendar calendar = Calendar.getInstance();
            Date date = DataUtil.transformStringToDate("dd/MM/yyyy", item.getDay());
            calendar.setTime(date);
            Time time = app.mapTimes.get(calendar.get(Calendar.DAY_OF_WEEK));
            totalWorkedHours += item.getTotalDifferencesSecond();
            if(item.getTotalDifferencesSecond() != 0 && time.getTotalDifferencesSecond() != 0){
                totalBalance += time.getTotalDifferencesSecond();
                totalBalance -= item.getTotalDifferencesSecond();
            }
        }
        updateValues(totalWorkedHours, totalBalance);
    }

    private void updateValues(int totalWorkedHours, int totalBalance) {
        ui.totalHours.setText(DataUtil.transformSecondsInHourMinutes(totalWorkedHours));
        ui.totalBalance.setText(DataUtil.transformSecondsInHourMinutes(Math.abs(totalBalance)));
        if(totalBalance < 0){
            ui.totalBalance.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }else if(totalBalance > 0){
            ui.totalBalance.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }else if(totalBalance == 0){
            ui.totalBalance.setText(R.string.zero_hour);
            ui.totalBalance.setTextColor(getResources().getColor(R.color.second_text));
        }
        if(totalWorkedHours == 0){
            ui.totalHours.setText(R.string.zero_hour);
        }
    }

    private View.OnClickListener onDateClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener(),
                        app.dateHistory.get(Calendar.YEAR), app.dateHistory.get(Calendar.MONTH),
                        app.dateHistory.get(Calendar.MONTH));
                dialog.getDatePicker().
                        findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                dialog.show();
            }

            private DatePickerDialog.OnDateSetListener onDateSetListener() {
                return new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        app.dateHistory.set(Calendar.YEAR, year);
                        app.dateHistory.set(Calendar.MONTH, month);
                        loadDate();
                        checkUpdate();
                    }
                };
            }
        };
    }

    private View.OnClickListener onPreviousClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.dateHistory.add(Calendar.MONTH, -1);
                loadDate();
                checkUpdate();
            }
        };
    }

    private View.OnClickListener onNextClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.dateHistory.add(Calendar.MONTH, 1);
                loadDate();
                checkUpdate();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_history_save:
                saveOrShareHistory(REQUEST_SAVE);
                break;
            case R.id.menu_history_share:
                saveOrShareHistory(REQUEST_SHARE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveOrShareHistory(int request) {
        SaveHistoryAsyncTask task = new SaveHistoryAsyncTask(onSaveOrShareHistoryCallback(request),
                getResources(),
                app.mapListHistories.get(DataUtil.getMonthYear(app.dateHistory.getTime())),
                app.mapTimes);
        app.registerTask(task);
        task.execute();
    }

    private Callback onSaveOrShareHistoryCallback(final int request) {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                if(error == null){
                    String path = (String) objects[0];
                    switch (request){
                        case REQUEST_SAVE:
                            Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
                            break;
                        case REQUEST_SHARE:
                            share(path);
                            break;
                    }
                }
            }

            private void share(String path) {
                String subject = getString(R.string.app_name) + " " +
                        DataUtil.getMonth(app.dateHistory.get(Calendar.MONTH), getResources()) +
                        " / " + app.dateHistory.get(Calendar.YEAR);
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                sendIntent.setType("text/html");
                startActivity(sendIntent);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_history, menu);
    }
}
