package com.vagnnermartins.marcaponto.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.fragment.SettingsFragment;
import com.vagnnermartins.marcaponto.util.AlarmUtil;
import com.vagnnermartins.marcaponto.util.DataUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WakeUpReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtil.scheduleAllNotification(context);
    }
}
