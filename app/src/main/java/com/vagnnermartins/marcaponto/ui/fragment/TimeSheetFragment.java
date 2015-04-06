package com.vagnnermartins.marcaponto.ui.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.enums.StatusEnum;
import com.vagnnermartins.marcaponto.enums.TrackerName;
import com.vagnnermartins.marcaponto.enums.WhichRegisterEnum;
import com.vagnnermartins.marcaponto.task.FindHistoryAsyncTask;
import com.vagnnermartins.marcaponto.ui.helper.TimeSheetUIHelper;
import com.vagnnermartins.marcaponto.util.AlarmUtil;
import com.vagnnermartins.marcaponto.util.DataUtil;
import com.vagnnermartins.marcaponto.util.WidgetUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
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
    private History currentHistory;
    private Calendar selectedDay;
    private boolean cancel;
    private TextView selectedTime;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ui = new TimeSheetUIHelper(inflater.inflate(R.layout.fragment_time_sheet, container, false));
        init();
        return ui.view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkStatus(StatusEnum.INICIO);
    }

    private void checkUpdate() {
        if (app.mapHistories.get(DataUtil.getMonthYear(selectedDay.getTime())) == null) {
            checkStatus(StatusEnum.INICIO);
        } else {
            loadValues();
        }
    }

    private void init() {
        selectedDay = Calendar.getInstance();
        app = (App) getActivity().getApplication();
        ui.date.setText(DataUtil.formatDateToString(selectedDay.getTime(), getResources()));
        ui.register.setOnClickListener(onRegisterClickListener());
        ui.entranceMain.setOnClickListener(onTimesClickListener());
        ui.pauseMain.setOnClickListener(onTimesClickListener());
        ui.backMain.setOnClickListener(onTimesClickListener());
        ui.quitMain.setOnClickListener(onTimesClickListener());
        ui.timeMain.setOnClickListener(onTimeClickListener());
        initAdmob();
        initAnalytics();
    }

    private void initAnalytics() {
        Tracker t = app.getTracker( TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);
        t.setScreenName(Constants.ANALYTICS_TIMES_SHEET);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initAdmob() {
        AdRequest adRequestProd = new AdRequest.Builder()
                .addTestDevice("2C9AEADE49D960B9D04C47AD8B18EAEB")
                .build();
        ui.adView.loadAd(adRequestProd);
    }

    private void checkStatus(StatusEnum status) {
        if (status == StatusEnum.INICIO) {
            FindHistoryAsyncTask task = new FindHistoryAsyncTask(onFindHistoryCallback(),
                    DataUtil.transformDateToSting(selectedDay.getTime(), "dd/MM/yyyy"));
            task.execute();
            app.registerTask(task);
        } else if (status == StatusEnum.EXECUTANDO) {
            ui.progress.setVisibility(View.VISIBLE);
            ui.register.setEnabled(false);
        } else if (status == StatusEnum.EXECUTADO) {
            ui.progress.setVisibility(View.GONE);
            ui.register.setEnabled(true);
        }
    }

    private void loadValues() {
        currentHistory = app.mapHistories.get(DataUtil.transformDateToSting(selectedDay.getTime(), "dd/MM/yyyy"));
        ui.entrance.setText(currentHistory.getFormattedEntrance());
        ui.pause.setText(currentHistory.getFormattedPause());
        ui.back.setText(currentHistory.getFormattedBack());
        ui.quit.setText(currentHistory.getFormattedQuit());
    }

    private Callback onFindHistoryCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                currentHistory = (History) objects[0];
                if (currentHistory == null) {
                    currentHistory = new History();
                    currentHistory.setDay(DataUtil.transformDateToSting(selectedDay.getTime(), "dd/MM/yyyy"));
                }
                app.mapHistories.put(currentHistory.getDay(), currentHistory);
                loadValues();
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private View.OnClickListener onTimesClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                switch (view.getId()) {
                    case R.id.fragment_time_sheet_entrance_main:
                        onEntranceClickListener(calendar);
                        break;
                    case R.id.fragment_time_sheet_pause_main:
                        onPauseClickListener(calendar);
                        break;
                    case R.id.fragment_time_sheet_back_main:
                        onBackClickListener(calendar);
                        break;
                    case R.id.fragment_time_sheet_quit_main:
                        onQuitClickListener(calendar);
                        break;
                }
            }

            private void onQuitClickListener(Calendar calendar) {
                if (currentHistory.getFormattedQuit().equals("--:--")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                } else {
                    calendar.setTime(new Date(currentHistory.getQuit()));
                }
                showTimePicker(R.string.quit, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                selectedTime = ui.quit;
            }

            private void onBackClickListener(Calendar calendar) {
                if (currentHistory.getFormattedBack().equals("--:--")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                } else {
                    calendar.setTime(new Date(currentHistory.getBack()));
                }
                showTimePicker(R.string.back, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                selectedTime = ui.back;
            }

            private void onPauseClickListener(Calendar calendar) {
                if (currentHistory.getFormattedPause().equals("--:--")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                } else {
                    calendar.setTime(new Date(currentHistory.getPause()));
                }
                showTimePicker(R.string.pause, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                selectedTime = ui.pause;
            }

            private void onEntranceClickListener(Calendar calendar) {
                if (currentHistory.getFormattedEntrance().equals("--:--")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                } else {
                    calendar.setTime(new Date(currentHistory.getEntrance()));
                }
                showTimePicker(R.string.entrance, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                selectedTime = ui.entrance;
            }
        };
    }

    private View.OnClickListener onRegisterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar reseted = DataUtil.getResetedDay();
                Calendar now = Calendar.getInstance();
                reseted.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
                reseted.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
                if (currentHistory.getEntrance() == 0) {
                    currentHistory.setEntrance(reseted.getTime().getTime());
                    ui.entrance.setText(currentHistory.getFormattedEntrance());
                    save();
                } else if (currentHistory.getPause() == 0) {
                    currentHistory.setPause(reseted.getTime().getTime());
                    ui.pause.setText(currentHistory.getFormattedPause());
                    save();
                } else if (currentHistory.getBack() == 0) {
                    currentHistory.setBack(reseted.getTime().getTime());
                    ui.back.setText(currentHistory.getFormattedBack());
                    save();
                } else if (currentHistory.getQuit() == 0) {
                    currentHistory.setQuit(reseted.getTime().getTime());
                    ui.quit.setText(currentHistory.getFormattedQuit());
                    save();
                }
            }

            private void save() {
                currentHistory.saveOrUpdate();
                app.mapListHistories.put(currentHistory.getMonthYear(), null);
                app.historyFragment.checkStatus(StatusEnum.INICIO);
                WidgetUtil.updateWidget(getActivity());
            }
        };
    }

    private View.OnClickListener onTimeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener(),
                        selectedDay.get(Calendar.YEAR), selectedDay.get(Calendar.MONTH),
                        selectedDay.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }

            private DatePickerDialog.OnDateSetListener onDateSetListener() {
                return new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedDay.set(Calendar.YEAR, year);
                        selectedDay.set(Calendar.MONTH, month);
                        selectedDay.set(Calendar.DAY_OF_MONTH, day);
                        ui.date.setText(DataUtil.formatDateToString(selectedDay.getTime(), getResources()));
                        checkUpdate();
                    }
                };
            }
        };
    }

    private void showTimePicker(int title, int hour, int minute) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), onTimeSetListener(), hour, minute, true);
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), onCancelClickListener());
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.delete), onDeleteClickListener());
        cancel = false;
        dialog.show();
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener() {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (!cancel) {
                    Calendar calendar = configDate(selectedHour, selectedMinute);
                    DecimalFormat format = new DecimalFormat("00");
                    selectedTime.setText(format.format(selectedHour) + ":" + format.format(selectedMinute));
                    switch (selectedTime.getId()) {
                        case R.id.fragment_time_sheet_entrance:
                            currentHistory.setEntrance(calendar.getTime().getTime());
                            updateNotifications(WhichRegisterEnum.ENTRANCE);
                            break;
                        case R.id.fragment_time_sheet_pause:
                            currentHistory.setPause(calendar.getTime().getTime());
                            updateNotifications(WhichRegisterEnum.PAUSE);
                            break;
                        case R.id.fragment_time_sheet_back:
                            currentHistory.setBack(calendar.getTime().getTime());
                            updateNotifications(WhichRegisterEnum.BACK);
                            break;
                        case R.id.fragment_time_sheet_quit:
                            currentHistory.setQuit(calendar.getTime().getTime());
                            updateNotifications(WhichRegisterEnum.QUIT);
                            break;
                    }
                    currentHistory.saveOrUpdate();
                    app.historyFragment.checkStatus(StatusEnum.INICIO);
                    WidgetUtil.updateWidget(getActivity());
                }
            }

            private Calendar configDate(int selectedHour, int selectedMinute) {
                Calendar calendar = DataUtil.getResetedDay();
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar;
            }
        };
    }

    private DialogInterface.OnClickListener onDeleteClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel = true;
                selectedTime.setText(R.string.no_hour);
                switch (selectedTime.getId()) {
                    case R.id.fragment_time_sheet_entrance:
                        currentHistory.setEntrance(0);
                        updateNotifications(WhichRegisterEnum.ENTRANCE);
                        break;
                    case R.id.fragment_time_sheet_pause:
                        currentHistory.setPause(0);
                        updateNotifications(WhichRegisterEnum.PAUSE);
                        break;
                    case R.id.fragment_time_sheet_back:
                        currentHistory.setBack(0);
                        updateNotifications(WhichRegisterEnum.BACK);
                        break;
                    case R.id.fragment_time_sheet_quit:
                        currentHistory.setQuit(0);
                        updateNotifications(WhichRegisterEnum.QUIT);
                        break;
                }
                currentHistory.saveOrUpdate();
                app.historyFragment.checkStatus(StatusEnum.INICIO);
                WidgetUtil.updateWidget(getActivity());
            }
        };
    }

    private void updateNotifications(WhichRegisterEnum which) {
        String sId = currentHistory.getDay() + which.getWhich();
        Integer id = Integer.parseInt(sId.replaceAll("[^0-9]", ""));
        AlarmUtil.cancelNotification(getActivity(), id);
        AlarmUtil.scheduleAllNotification(getActivity());
    }

    private DialogInterface.OnClickListener onCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel = true;
            }
        };
    }
}
