package com.vagnnermartins.marcaponto.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.enums.TrackerName;
import com.vagnnermartins.marcaponto.ui.activity.TimesActivity;
import com.vagnnermartins.marcaponto.ui.helper.SettingsUIHelper;
import com.vagnnermartins.marcaponto.util.CurrencyUtils;
import com.vagnnermartins.marcaponto.util.NavegacaoUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SettingsFragment extends Fragment {

    public static final int POSITION = 2;
    public static final int NAME_TAB = R.string.fragment_settings;

    private App app;
    private SettingsUIHelper ui;
    private String current = "";

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
        ui.checkBox.setChecked(SessionUtil.getValue(getActivity(), Constants.NOTIFICATION));
        ui.checkBox.setOnCheckedChangeListener(onCheckedChangeListener());
        ui.hourlyMain.setOnClickListener(onHourlyMainClickListener());
        initAdmob();
        initAnalytics();
        loadValues();
    }

    private void initAnalytics() {
        Tracker t = app.getTracker( TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);
        t.setScreenName(Constants.ANALYTICS_SETTINGS);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initAdmob() {
        AdRequest adRequestProd = new AdRequest.Builder()
//                .addTestDevice("2C9AEADE49D960B9D04C47AD8B18EAEB")
                .build();
        ui.adView.loadAd(adRequestProd);
    }

    private void loadValues() {
        ui.hourly.setText(CurrencyUtils.format(app.settings.getHourly()));
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
        SessionUtil.addValue(getActivity(), Constants.NOTIFICATION, checked);
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

    private View.OnClickListener onHourlyMainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.dialog_hourly, null);
                editText.addTextChangedListener(onTextChangedListener(editText));
                editText.setText(CurrencyUtils.format(app.settings.getHourly()));
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.fragment_settings_hourly)
                        .setView(editText)
                        .setPositiveButton(android.R.string.ok, onPositiveButton(editText)).setNegativeButton(android.R.string.cancel, null).show();
            }

            private DialogInterface.OnClickListener onPositiveButton(final EditText editText) {
                return new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            String sValue = editText.getText().toString();
                            BigDecimal decimal = new BigDecimal((Double) NumberFormat.getCurrencyInstance().parse(sValue));
                            app.settings.setHourly(decimal);
                            app.settings.save();
                            loadValues();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }

            private TextWatcher onTextChangedListener(final EditText editText) {
                return new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!s.toString().equals(current)){
                            editText.removeTextChangedListener(this);
                            String cleanString = s.toString().replaceAll("[^0-9]", "");
                            double parsed = Double.parseDouble(cleanString);
                            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
                            current = formatted;
                            editText.setText(formatted);
                            editText.setSelection(formatted.length());
                            editText.addTextChangedListener(this);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                };
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
