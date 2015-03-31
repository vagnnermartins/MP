package com.vagnnermartins.marcaponto.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.Calendar;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_REGISTER = "register";

    public static final String WHICH = "which";
    public static final String HISTORY = "history";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REGISTER.equals(action)) {
                SingletonAdapter.getInstance(context);
                History history = findHistory(intent);
                final int which = intent.getIntExtra(WHICH, 0);
                Calendar now = Calendar.getInstance();
                now = configDate(history, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
                switch (which){
                    case R.string.entrance:
                        history.setEntrance(now.getTime().getTime());
                        break;
                    case R.string.pause:
                        history.setPause(now.getTime().getTime());
                        break;
                    case R.string.back:
                        history.setBack(now.getTime().getTime());
                        break;
                    case R.string.quit:
                        history.setQuit(now.getTime().getTime());
                        break;
                }
                history.saveOrUpdate();
            }
        }
    }

    private History findHistory(Intent intent){
        String day = intent.getStringExtra(HISTORY);
        History qHistory = new History();
        qHistory.setDay(day);
        qHistory = SingletonAdapter.getInstance().getAdapter().findFirst(qHistory);
        if(qHistory == null){
            qHistory = new History();
            Date date = DataUtil.transformStringToDate("dd/MM/yyyy", day);
            qHistory.setDay(day);
            qHistory.setMonthYear(DataUtil.getMonthYear(date));
        }
        return qHistory;
    }

    private Calendar configDate(History history, int selectedHour, int selectedMinute) {
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.setTime(DataUtil.transformStringToDate("dd/MM/yyyy", history.getDay()));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay.get(Calendar.DAY_OF_MONTH));
        return calendar;
    }
}
