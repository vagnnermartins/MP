package com.vagnnermartins.marcaponto.util;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.vagnnermartins.marcaponto.ui.widget.TimesheetWidget;

/**
 * Created by vagnnermartins on 02/04/15.
 */
public class WidgetUtil {

    public static void updateWidget(Context context){
        Intent intent = new Intent(context,TimesheetWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TimesheetWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
    }
}
