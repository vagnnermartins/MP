package com.vagnnermartins.marcaponto.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.task.FindTimesAsyncTask;
import com.vagnnermartins.marcaponto.util.NavegacaoUtil;

import java.util.List;

public class SplashScreenActivity extends ActionBarActivity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {
        app = (App) getApplication();
        FindTimesAsyncTask task = new FindTimesAsyncTask(onFindTimesCallback());
        task.execute();
        app.registerTask(task);
    }

    private Callback onFindTimesCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                List<Time> times = (List<Time>) objects[0];
                for(Time time : times){
                    app.mapTimes.put(time.getId(), time);
                }
                NavegacaoUtil.navegar(SplashScreenActivity.this, MainActivity.class);
                finish();
            }
        };
    }

}
