package com.vagnnermartins.marcaponto.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.activity.SplashScreenActivity;
import com.vagnnermartins.marcaponto.ui.fragment.SettingsFragment;
import com.vagnnermartins.marcaponto.util.DataUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String HISTORY = "history";
    public static final String TITLE = "title";
    public static final int REQUEST_YES = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        History history = findHistory(intent);
        int title = intent.getIntExtra(TITLE, 0);
        if (SessionUtil.getValue(context, SettingsFragment.NOTIFICATION) &&
                emptyHistory(history, title)) {
            String message = getMessage(context, title);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(context.getString(title))
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(getPendingIntent(context))
                            .addAction(R.mipmap.ic_clock, context.getString(R.string.time_clock_register), getActionPendingIntent(context, history, title));
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Integer id = Integer.parseInt(history.getDay().replaceAll("[^0-9]", ""));
            mNotifyMgr.notify(id, mBuilder.build());
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

    private boolean emptyHistory(History history, int title){
        boolean result = false;
        switch (title){
            case R.string.entrance:
                result = history.getEntrance() == 0;
                break;
            case R.string.pause:
                result = history.getPause() == 0;
                break;
            case R.string.back:
                result = history.getBack() == 0;
                break;
            case R.string.quit:
                result = history.getQuit() == 0;
                break;
        }
        return result;
    }

    private PendingIntent getActionPendingIntent(Context context, History history, int title) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_REGISTER);
        intent.putExtra(NotificationReceiver.HISTORY, history.getDay());
        intent.putExtra(NotificationReceiver.WHICH, title);
        return PendingIntent.getBroadcast(context, REQUEST_YES, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, SplashScreenActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getMessage(Context context, int title) {
        String message = context.getString(R.string.notification_message);
        return String.format(message, context.getString(title));
    }
}
