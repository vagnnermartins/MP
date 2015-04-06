package com.vagnnermartins.marcaponto.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.adapter.SectionsPagerAdapter;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.helper.MainUIHelper;


public class MainActivity extends ActionBarActivity {

    private MainUIHelper ui;
    private SectionsPagerAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        App app = (App) getApplication();
        ui = new MainUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
        mTabsAdapter = new SectionsPagerAdapter(app, this, getSupportFragmentManager());
        ui.viewPager.setAdapter(mTabsAdapter);
        ui.tabs.setViewPager(ui.viewPager);
        ui.tabs.setIndicatorColorResource(R.color.accent);
    }

}
