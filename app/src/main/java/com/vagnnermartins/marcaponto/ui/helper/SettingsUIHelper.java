package com.vagnnermartins.marcaponto.ui.helper;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SettingsUIHelper {

    public View view;
    public View hours;
    public View hourlyMain;
    public TextView hourly;
    public CheckBox checkBox;
    public View checkBoxMain;
    public AdView adView;

    public SettingsUIHelper(View view) {
        this.view = view;
        this.hours = view.findViewById(R.id.fragment_settings_hours);
        this.checkBox = (CheckBox) view.findViewById(R.id.fragment_settings_notification);
        this.checkBoxMain = view.findViewById(R.id.fragment_settings_notification_main);
        this.adView = (AdView) view.findViewById(R.id.fragment_time_settings_adview);
        this.hourlyMain = view.findViewById(R.id.fragment_settings_hourly_main);
        this.hourly = (TextView) view.findViewById(R.id.fragment_settings_hourly);
    }

}
