package com.vagnnermartins.marcaponto.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;

import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class TimesUIHelper {

    public Toolbar toolbar;
    public LinearLayout list;
    public AdView adView;

    public TimesUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.list = (LinearLayout) view.findViewById(R.id.times_list);
        this.adView = (AdView) view.findViewById(R.id.times_adview);
    }
}
