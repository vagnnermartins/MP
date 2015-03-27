package com.vagnnermartins.marcaponto.task;

import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;

import java.util.List;

/**
 * Created by vagnnermartins on 26/03/15.
 */
public class FindTimesAsyncTask extends AsyncTask<Void, Void, List<Time>> {

    private final Callback callback;

    public FindTimesAsyncTask(Callback callback){
        this.callback = callback;
    }

    @Override
    protected List<Time> doInBackground(Void... voids) {
        return SingletonAdapter.getInstance().getAdapter().findAll(Time.class);
    }

    @Override
    protected void onPostExecute(List<Time> result) {
        super.onPostExecute(result);
        callback.onReturn(null, result);
    }
}
