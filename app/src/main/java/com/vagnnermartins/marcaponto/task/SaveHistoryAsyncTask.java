package com.vagnnermartins.marcaponto.task;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.util.CSVUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by vagnnermartins on 01/04/15.
 */
public class SaveHistoryAsyncTask extends AsyncTask<Void, Void, String> {

    private final Callback callback;
    private final List<History> histories;
    private final Resources res;
    private final Map<Integer, Time> times;

    public SaveHistoryAsyncTask(Callback callback, Resources res, List<History> histories, Map<Integer, Time> times){
        this.callback = callback;
        this.histories = histories;
        this.res = res;
        this.times = times;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return CSVUtil.createCSV(histories, times, res);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onReturn(null, s);
    }
}
