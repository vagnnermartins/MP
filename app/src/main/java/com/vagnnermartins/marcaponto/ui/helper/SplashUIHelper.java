package com.vagnnermartins.marcaponto.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class SplashUIHelper {

    public TextView message;

    public SplashUIHelper(View view){
        this.message = (TextView) view.findViewById(R.id.splash_message);
    }
}
