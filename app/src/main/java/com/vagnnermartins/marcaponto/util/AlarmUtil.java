package com.vagnnermartins.marcaponto.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.receiver.AlarmReceiver;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagnnermartins on 11/01/15.
 */
public class AlarmUtil {

    private static void scheduleNotification(Context context, History history, long dateTime, int title) {
        if(Calendar.getInstance().getTimeInMillis() < dateTime){
            Calendar alarm = Calendar.getInstance();
            alarm.setTime(new Date(dateTime));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(AlarmReceiver.HISTORY, history.getDay());
            intent.putExtra(AlarmReceiver.TITLE, title);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) history.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
        }
    }

    private static void scheduleHistoryNotifications(Context context, List<History> histories, List<Time> times){
        for(History history : histories){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DataUtil.transformStringToDate("dd/MM/yyyy", history.getDay()));
            Time dayOfWeek = times.get(calendar.get(Calendar.DAY_OF_WEEK) -1);
            if(dayOfWeek.getEntrance() != 0 && history.getEntrance() == 0){
                scheduleNotification(context, history, dayOfWeek.getEntrance(), R.string.entrance);
            }
            if(dayOfWeek.getEntrance() != 0 && history.getPause() == 0){
                scheduleNotification(context, history, dayOfWeek.getPause(), R.string.pause);
            }
            if(dayOfWeek.getBack() != 0 && history.getBack() == 0){
                scheduleNotification(context, history, dayOfWeek.getBack(), R.string.back);
            }
            if(dayOfWeek.getQuit() != 0 && history.getQuit() == 0){
                scheduleNotification(context, history, dayOfWeek.getQuit(), R.string.quit);
            }
        }
    }

    public static void scheduleAllNotification(Context context) {
        if(SessionUtil.getValue(context, SettingsFragment.NOTIFICATION)){
            List<Time> times = SingletonAdapter.getInstance(context).getAdapter().findAll(Time.class);
            Calendar calendar = Calendar.getInstance();
            List<History> result = new ArrayList<>();
            List<History> myHistories = History.findHistoryByMonthYear(DataUtil.getMonthYear(calendar.getTime()));
            Map<String, History> mapHistories = new HashMap<>();
            for(History item : myHistories){
                mapHistories.put(item.getDay(), item);
            }

            for (int i = 0 ; i < 5; i++){
                History history = mapHistories.get(DataUtil.transformDateToSting(calendar.getTime(), "dd/MM/yyyy"));
                if(history == null){
                    history = new History();
                    history.setDay(DataUtil.transformDateToSting(calendar.getTime(), "dd/MM/yyyy"));
                    history.setMonthYear(DataUtil.getMonthYear(calendar.getTime()));
                }
                result.add(history);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            scheduleHistoryNotifications(context, result, times);
        }
    }

    public static void cancelNotification(Context context, int id){
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(id);
    }
}
