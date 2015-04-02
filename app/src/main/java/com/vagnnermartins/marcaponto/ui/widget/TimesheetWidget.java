package com.vagnnermartins.marcaponto.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.receiver.NotificationReceiver;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.activity.SplashScreenActivity;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class TimesheetWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        History history = findHistory(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timesheet_widget);
        loadValues(history, views);
        onWidgetClickListener(context, views);
        views.setOnClickPendingIntent(R.id.widget_register, getRegisterPendingIntent(context, history));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void loadValues(History history, RemoteViews views) {
        views.setTextViewText(R.id.widget_entrance, history.getFormattedEntrance());
        views.setTextViewText(R.id.widget_pause, history.getFormattedPause());
        views.setTextViewText(R.id.widget_back, history.getFormattedBack());
        views.setTextViewText(R.id.widget_quit, history.getFormattedQuit());
    }

    private static void onWidgetClickListener(Context context, RemoteViews view) {
        Intent configIntent = new Intent(context, SplashScreenActivity.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.widget_main, configPendingIntent);
    }

    private static PendingIntent getRegisterPendingIntent(Context context, History history) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_REGISTER);
        intent.putExtra(NotificationReceiver.HISTORY, history.getDay());
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static History findHistory(Context context) {
        History qHistory = new History();
        qHistory.setDay(DataUtil.transformDateToSting(new Date(), "dd/MM/yyyy"));
        SingletonAdapter adapter = SingletonAdapter.getInstance(context);
        History result = adapter.getAdapter().findFirst(qHistory);
        if (result == null) {
            result = new History();
            result.setDay(DataUtil.transformDateToSting(new Date(), "dd/MM/yyyy"));
        }
        return result;
    }
}


