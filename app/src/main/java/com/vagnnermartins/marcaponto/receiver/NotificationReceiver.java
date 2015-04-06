package com.vagnnermartins.marcaponto.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.enums.WhichRegisterEnum;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.util.AlarmUtil;
import com.vagnnermartins.marcaponto.util.DataUtil;
import com.vagnnermartins.marcaponto.util.WidgetUtil;

import java.util.Calendar;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_REGISTER = "register";

    public static final String HISTORY = "history";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REGISTER.equals(action)) {
                SingletonAdapter.getInstance(context);
                History history = findHistory(intent);
                WhichRegisterEnum which = WhichRegisterEnum.ENTRANCE;
                Calendar now = configDate();
                if (history.getEntrance() == 0) {
                    history.setEntrance(now.getTime().getTime());
                } else if (history.getPause() == 0) {
                    history.setPause(now.getTime().getTime());
                    which = WhichRegisterEnum.PAUSE;
                } else if (history.getBack() == 0) {
                    history.setBack(now.getTime().getTime());
                    which = WhichRegisterEnum.BACK;
                } else if (history.getQuit() == 0) {
                    history.setQuit(now.getTime().getTime());
                    which = WhichRegisterEnum.QUIT;
                }
                history.saveOrUpdate();
                Toast.makeText(context, R.string.notification_message_success, Toast.LENGTH_LONG).show();
                String sId = history.getDay() + which.getWhich();
                Integer id = Integer.parseInt(sId.replaceAll("[^0-9]", ""));
                AlarmUtil.cancelNotification(context, id);
                WidgetUtil.updateWidget(context);
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

    private Calendar configDate() {
        Calendar reseted = DataUtil.getResetedDay();
        Calendar now = Calendar.getInstance();
        reseted.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        reseted.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        return reseted;
    }
}
