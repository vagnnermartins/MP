package com.vagnnermartins.marcaponto.app;

import android.app.Application;
import android.os.AsyncTask;

import com.codeslap.persistence.DatabaseSpec;
import com.codeslap.persistence.PersistenceConfig;
import com.vagnnermartins.marcaponto.db.Database;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.fragment.HistoryFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class App extends Application {

    public Calendar dateHistory;
    public Map<String, List<History>> mapListHistories;
    public Map<Integer, Time> mapTimes;
    public Map<String, History> mapHistories;
    public HistoryFragment historyFragment;

    private List<AsyncTask<?,?,?>> tasks;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initDataBase();
        mapListHistories = new HashMap<>();
        mapTimes = new HashMap<>();
        mapHistories = new HashMap<>();
        dateHistory = Calendar.getInstance();
        tasks = new ArrayList<>();
    }

    private void initDataBase() {
        Database db = new Database(getApplicationContext());
        db.getWritableDatabase();
        DatabaseSpec database = PersistenceConfig.registerSpec(Database.DATABASE_SPEC, Database.DATABASE_VERSION);
        database.match(Time.class);
        database.match(History.class);
        SingletonAdapter.getInstance(this);
    }

    public void registerTask(AsyncTask<?,?,?> task){
        tasks.add(task);
    }

    private void unregisterTask(AsyncTask<?,?,?> task){
        if(task != null){
            task.cancel(true);
            tasks.remove(task);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for(AsyncTask task : tasks){
            unregisterTask(task);
        }
    }
}
