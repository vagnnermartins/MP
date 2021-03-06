package com.vagnnermartins.marcaponto.ui.helper;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class MainUIHelper {

    public Toolbar toolbar;
    public ViewPager viewPager;
    public PagerSlidingTabStrip tabs;

    public MainUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager) view.findViewById(R.id.main_pager);
        this.tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pager_sliding);
    }
}
