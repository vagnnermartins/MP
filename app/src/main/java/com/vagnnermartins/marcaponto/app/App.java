package com.vagnnermartins.marcaponto.app;

import android.app.Application;
import android.os.AsyncTask;

import com.codeslap.persistence.DatabaseSpec;
import com.codeslap.persistence.PersistenceConfig;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.db.Database;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.enums.TrackerName;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.ui.fragment.HistoryFragment;
import com.vagnnermartins.marcaponto.util.AlarmUtil;
import com.vagnnermartins.marcaponto.util.WidgetUtil;

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
    private HashMap<TrackerName, Tracker> mTrackers;
    public PublisherInterstitialAd mPublisherInterstitialAd;
    public int showInterstitial = 1;

    private List<AsyncTask<?,?,?>> tasks;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initDataBase();
        requestNewInterstitial();
        mapListHistories = new HashMap<>();
        mapTimes = new HashMap<>();
        mapHistories = new HashMap<>();
        dateHistory = Calendar.getInstance();
        tasks = new ArrayList<>();
        mTrackers = new HashMap<>();
        WidgetUtil.updateWidget(this);
        AlarmUtil.cancellAllNotifications(this);
    }

    public void requestNewInterstitial() {
        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId(getString(R.string.ADMOB_INTERSTITIAL));
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherInterstitialAd.loadAd(adRequest);
    }

    public void showInterstitial() {
        if(showInterstitial == 7 || showInterstitial % 40 == 0) {
            if(mPublisherInterstitialAd.isLoaded()){
                mPublisherInterstitialAd.show();
                requestNewInterstitial();
            }
        }
        showInterstitial++;
    }

    private void initDataBase() {
        Database db = new Database(getApplicationContext());
        db.getWritableDatabase();
        DatabaseSpec database = PersistenceConfig.registerSpec(Database.DATABASE_SPEC, Database.DATABASE_VERSION);
        database.match(Time.class);
        database.match(History.class);
        SingletonAdapter.getInstance(this);
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(Constants.ANALYTICS_ID);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
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
