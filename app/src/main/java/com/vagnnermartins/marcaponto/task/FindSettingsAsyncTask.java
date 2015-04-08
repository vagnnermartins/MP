package com.vagnnermartins.marcaponto.task;

import android.content.Context;
import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.Settings;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;

/**
 * Created by vagnnermartins on 07/04/15.
 */
public class FindSettingsAsyncTask extends AsyncTask<Void, Void, Settings> {

    private final Callback callback;

    public FindSettingsAsyncTask(Callback callback){
        this.callback = callback;
    }

    @Override
    protected Settings doInBackground(Void... voids) {
        Settings qSettigns = new Settings();
        qSettigns.setId(1);
        return SingletonAdapter.getInstance().getAdapter().findFirst(qSettigns);
    }

    @Override
    protected void onPostExecute(Settings result) {
        super.onPostExecute(result);
        callback.onReturn(null, result);
    }
}
