package com.vagnnermartins.marcaponto.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.enums.StatusEnum;
import com.vagnnermartins.marcaponto.task.FindHistoryAsyncTask;
import com.vagnnermartins.marcaponto.ui.helper.TimeSheetUIHelper;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class TimeSheetFragment extends Fragment {

    public static final int POSITION = 0;
    public static final int NAME_TAB = R.string.fragment_time_clock;

    private App app;
    private TimeSheetUIHelper ui;
    private Calendar selectedDay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ui = new TimeSheetUIHelper(inflater.inflate(R.layout.fragment_time_sheet, container, false));
        init();
        checkUpdate();
        return ui.view;
    }

    private void checkUpdate() {
        if(app.mapHistories.get(DataUtil.getMonthYear(selectedDay.getTime())) == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            loadValues();
        }
    }

    private void init() {
        selectedDay = Calendar.getInstance();
        app = (App) getActivity().getApplication();
        ui.date.setText(DataUtil.formatDateToString(selectedDay.getTime()));
        ui.register.setOnClickListener(onRegisterClickListener());
    }

    private View.OnClickListener onRegisterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        };
    }

    private void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            FindHistoryAsyncTask task = new FindHistoryAsyncTask(onFindHistoryCallback(),selectedDay);
            task.execute();
            app.registerTask(task);
        }else if(status == StatusEnum.EXECUTANDO){
            ui.progress.setVisibility(View.VISIBLE);
        }else if(status == StatusEnum.EXECUTADO){
            ui.progress.setVisibility(View.GONE);
        }
    }

    private void loadValues() {
        History history = app.mapHistories.get(DataUtil.transformDateToSting(selectedDay.getTime(), "dd/MM/yy"));
        ui.entrance.setText(history.getFormattedEntrance());
        ui.pause.setText(history.getFormattedPause());
        ui.back.setText(history.getFormattedBack());
        ui.quit.setText(history.getFormattedQuit());
    }

    private Callback onFindHistoryCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                History history = (History) objects[0];
                app.mapHistories.put(history.getFormattedDay(), history);
                loadValues();
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }
}
