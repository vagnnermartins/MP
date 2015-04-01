package com.vagnnermartins.marcaponto.task;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vagnnermartins on 25/03/15.
 */
public class FindHistoriesAsyncTask extends AsyncTask<Void, Void, List<History>> {

    private final Calendar calendar;
    private final Callback callback;
    private final Context context;

    public FindHistoriesAsyncTask(Context context, Callback callback, Date date){
        this.context = context;
        this.callback = callback;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);
    }

    @Override
    protected List<History> doInBackground(Void... voids) {
        List<History> result = new ArrayList<>();
        Resources res = context.getResources();
        List<History> myHistories = History.findHistoryByMonthYear(DataUtil.getMonthYear(calendar.getTime(), res));
        Map<String, History> mapHistories = new HashMap<>();
        for(History item : myHistories){
            mapHistories.put(item.getDay(), item);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0 ; i < totalDays; i++){
            History history = mapHistories.get(DataUtil.transformDateToSting(calendar.getTime(), "dd/MM/yyyy"));
            if(history == null){
                history = new History();
                history.setDay(DataUtil.transformDateToSting(calendar.getTime(), "dd/MM/yyyy"));
                history.setMonthYear(DataUtil.getMonthYear(calendar.getTime(), res));
            }
            result.add(history);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<History> result) {
        super.onPostExecute(result);
        callback.onReturn(null, result);
    }
}
