package com.vagnnermartins.marcaponto.task;

import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;

import java.util.Calendar;

/**
 * Created by vagnnermartins on 27/03/15.
 */
public class FindHistoryAsyncTask extends AsyncTask<Void, Void, History> {

    private final Callback callback;
    private Calendar day;

    public FindHistoryAsyncTask(Callback callback, Calendar day){
        this.callback = callback;
        this.day = day;
    }

    @Override
    protected History doInBackground(Void... voids) {
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        History qHistory = new History();
        qHistory.setDay(day.getTime().getTime());
        return SingletonAdapter.getInstance().getAdapter().findFirst(qHistory);
    }

    @Override
    protected void onPostExecute(History result) {
        super.onPostExecute(result);
    }
}
