package com.vagnnermartins.marcaponto.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vagnnermartins.marcaponto.ui.fragment.HistoryFragment;
import com.vagnnermartins.marcaponto.ui.fragment.SettingsFragment;
import com.vagnnermartins.marcaponto.ui.fragment.TimeSheetFragment;

/**
 * Created by vagnnermartins on 07/08/14.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_TAB = 3;
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case TimeSheetFragment.POSITION:
                fragment = Fragment.instantiate(context, TimeSheetFragment.class.getName());
                break;
            case HistoryFragment.POSITION:
                fragment = Fragment.instantiate(context, HistoryFragment.class.getName());
                break;
            case SettingsFragment.POSITION:
                fragment = Fragment.instantiate(context, SettingsFragment.class.getName());
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence result = null;
        switch (position) {
            case TimeSheetFragment.POSITION:
                result = context.getString(TimeSheetFragment.NAME_TAB).toUpperCase();
                break;
            case HistoryFragment.POSITION:
                result = context.getString(HistoryFragment.NAME_TAB).toUpperCase();
                break;
            case SettingsFragment.POSITION:
                result = context.getString(SettingsFragment.NAME_TAB).toUpperCase();
                break;
        }
        return result;
    }
}