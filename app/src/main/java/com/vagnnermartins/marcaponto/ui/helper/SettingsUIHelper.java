package com.vagnnermartins.marcaponto.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gc.materialdesign.views.CheckBox;
import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SettingsUIHelper {

    public View view;
    public View hours;
    public CheckBox checkBox;
    public View checkBoxMain;

    public SettingsUIHelper(View view) {
        this.view = view;
        this.hours = view.findViewById(R.id.fragment_settings_hours);
        this.checkBox = (CheckBox) view.findViewById(R.id.fragment_settings_notification);
        this.checkBoxMain = view.findViewById(R.id.fragment_settings_notification_main);
        this.checkBoxMain.setOnClickListener(onCheckBoxMainClickListener());
    }

    private View.OnClickListener onCheckBoxMainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(!checkBox.isCheck());
            }
        };
    }

}
