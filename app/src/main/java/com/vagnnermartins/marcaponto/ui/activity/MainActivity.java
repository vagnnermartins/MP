package com.vagnnermartins.marcaponto.ui.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.adapter.SectionsPagerAdapter;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.ui.helper.MainUIHelper;


public class MainActivity extends ActionBarActivity {

    private MainUIHelper ui;
    private SectionsPagerAdapter mSectionsPagerAdapter;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(app, this, getSupportFragmentManager());
        ui.viewPager.setAdapter(mSectionsPagerAdapter);
        ui.viewPager.setOnPageChangeListener(onPageChangeListener());
    }

    private ViewPager.OnPageChangeListener onPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }
}
