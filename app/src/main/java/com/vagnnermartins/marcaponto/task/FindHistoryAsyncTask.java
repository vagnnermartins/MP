package com.vagnnermartins.marcaponto.task;

import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;

/**
 * Created by vagnnermartins on 27/03/15.
 */
public class FindHistoryAsyncTask extends AsyncTask<Void, Void, History> {

    private final Callback callback;
    private String day;

    public FindHistoryAsyncTask(Callback callback, String day){
        this.callback = callback;
        this.day = day;
    }

    @Override
    protected History doInBackground(Void... voids) {
        History qHistory = new History();
        qHistory.setDay(day);
        return SingletonAdapter.getInstance().getAdapter().findFirst(qHistory);
    }

    @Override
    protected void onPostExecute(History result) {
        super.onPostExecute(result);
        callback.onReturn(null, result);
    }
}
