package com.vagnnermartins.marcaponto.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.ui.activity.TimesActivity;
import com.vagnnermartins.marcaponto.ui.helper.SettingsUIHelper;
import com.vagnnermartins.marcaponto.util.NavegacaoUtil;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SettingsFragment extends Fragment {

    public static final int POSITION = 2;
    public static final int NAME_TAB = R.string.fragment_settings;

    private SettingsUIHelper ui;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ui = new SettingsUIHelper(inflater.inflate(R.layout.fragment_settings, container, false));
        init();
        return ui.view;
    }

    private void init() {
        ui.hours.setOnClickListener(onHoursClickListener());
    }

    private View.OnClickListener onHoursClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavegacaoUtil.navegar(getActivity(), TimesActivity.class);
            }
        };
    }
}
