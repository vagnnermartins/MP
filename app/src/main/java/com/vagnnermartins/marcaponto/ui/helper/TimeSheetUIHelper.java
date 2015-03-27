package com.vagnnermartins.marcaponto.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vagnnermartins.marcaponto.R;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class TimeSheetUIHelper {

    public View view;
    public TextView date;
    public View register;
    public View timeMain;
    public TextView entrance;
    public TextView pause;
    public TextView back;
    public TextView quit;
    public View progress;

    public TimeSheetUIHelper(View view){
        this.view = view;
        this.date = (TextView) view.findViewById(R.id.fragment_time_sheet_date);
        this.register = view.findViewById(R.id.fragment_time_sheet_register);
        this.timeMain = view.findViewById(R.id.fragment_time_sheet_time_main);
        this.entrance = (TextView) view.findViewById(R.id.fragment_time_sheet_entrance);
        this.pause = (TextView) view.findViewById(R.id.fragment_time_sheet_pause);
        this.back = (TextView) view.findViewById(R.id.fragment_time_sheet_back);
        this.quit = (TextView) view.findViewById(R.id.fragment_time_sheet_quit);
        this.progress = view.findViewById(R.id.fragment_time_sheet_progress);
    }
}
