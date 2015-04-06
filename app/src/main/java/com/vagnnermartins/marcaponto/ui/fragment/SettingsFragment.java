package com.vagnnermartins.marcaponto.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.ads.AdRequest;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.enums.TrackerName;
import com.vagnnermartins.marcaponto.ui.activity.TimesActivity;
import com.vagnnermartins.marcaponto.ui.helper.SettingsUIHelper;
import com.vagnnermartins.marcaponto.util.NavegacaoUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SettingsFragment extends Fragment {

    public static final int POSITION = 2;
    public static final int NAME_TAB = R.string.fragment_settings;
    public static final String NOTIFICATION = "notification";

    private App app;
    private SettingsUIHelper ui;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ui = new SettingsUIHelper(inflater.inflate(R.layout.fragment_settings, container, false));
        init();
        return ui.view;
    }

    @Override
    public void onResume() {
        super.onResume();
        app.showInterstitial();
    }

    private void init() {
        app = (App) getActivity().getApplication();
        ui.hours.setOnClickListener(onHoursClickListener());
        ui.checkBoxMain.setOnClickListener(onCheckBoxMainClickListener());
        ui.checkBox.setChecked(SessionUtil.getValue(getActivity(), NOTIFICATION));
        ui.checkBox.setOnCheckedChangeListener(onCheckedChangeListener());
        initAdmob();
        initAnalytics();
    }

    private void initAnalytics() {
        Tracker t = app.getTracker( TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);
        t.setScreenName(Constants.ANALYTICS_SETTINGS);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initAdmob() {
        AdRequest adRequestProd = new AdRequest.Builder()
                .addTestDevice("2C9AEADE49D960B9D04C47AD8B18EAEB")
                .build();
        ui.adView.loadAd(adRequestProd);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                changeNotificationListener(checked);
            }
        };
    }

    private void changeNotificationListener(boolean checked) {
        Tracker t = app.getTracker( TrackerName.APP_TRACKER);
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        builder.setAction(Constants.ANALYTICS_NOTIFICATION);
        ui.checkBox.setChecked(checked);
        SessionUtil.addValue(getActivity(), NOTIFICATION, checked);
        String message = getString(R.string.fragment_settings_changed_notification);
        if(checked){
            message = String.format(message, getString(R.string.enabled));
            builder.setValue(1);
        }else{
            message = String.format(message, getString(R.string.disabled));
            builder.setValue(0);
        }
        t.send(builder.build());
        new SnackBar(getActivity(), message).show();
    }

    private View.OnClickListener onHoursClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavegacaoUtil.navegar(getActivity(), TimesActivity.class);
            }
        };
    }

    private View.OnClickListener onCheckBoxMainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNotificationListener(!ui.checkBox.isChecked());
            }
        };
    }

}
