package com.vagnnermartins.marcaponto.ui.helper;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class HistoryUIHelper {

    public View view;
    public TextView date;
    public TextView previous;
    public TextView next;
    public ListView list;
    public TextView totalHours;
    public TextView totalBalance;
    public View progress;
    public AdView adView;

    public HistoryUIHelper(View view){
        this.view = view;
        this.date = (TextView) view.findViewById(R.id.fragment_history_date);
        this.previous = (TextView) view.findViewById(R.id.fragment_history_previous);
        this.next = (TextView) view.findViewById(R.id.fragment_history_next);
        this.list = (ListView) view.findViewById(R.id.fragment_history_list);
        this.totalHours = (TextView) view.findViewById(R.id.fragment_history_total_hours);
        this.totalBalance = (TextView) view.findViewById(R.id.fragment_history_total_balance);
        this.progress = view.findViewById(R.id.fragment_history_progress);
        this.adView = (AdView) view.findViewById(R.id.fragment_time_history_adview);
    }

}
