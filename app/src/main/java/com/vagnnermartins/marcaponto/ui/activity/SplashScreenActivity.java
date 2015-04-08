package com.vagnnermartins.marcaponto.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.app.App;
import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.entity.Settings;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.enums.StatusEnum;
import com.vagnnermartins.marcaponto.task.BridgeAsyncTask;
import com.vagnnermartins.marcaponto.task.FindSettingsAsyncTask;
import com.vagnnermartins.marcaponto.task.FindTimesAsyncTask;
import com.vagnnermartins.marcaponto.ui.helper.SplashUIHelper;
import com.vagnnermartins.marcaponto.util.NavegacaoUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

import java.util.List;

public class SplashScreenActivity extends ActionBarActivity {

    private App app;
    private SplashUIHelper ui;
    private int updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
        checkStatus(StatusEnum.INICIO);
    }

    private void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            statusInicio();
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutado();
        }
    }

    private void init() {
        app = (App) getApplication();
        ui = new SplashUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
    }

    private void statusInicio() {
        FindTimesAsyncTask findTimesTask = new FindTimesAsyncTask(onFindTimesCallback());
        findTimesTask.execute();
        FindSettingsAsyncTask findSettingsTask = new FindSettingsAsyncTask(onFindSettingsCallback());
        findSettingsTask.execute();
        app.registerTask(findTimesTask, findSettingsTask);
        checkBridge();
    }

    private void checkBridge() {
        if(SessionUtil.getValue(this, Constants.UPDATED)){
            BridgeAsyncTask task = new BridgeAsyncTask(this, onBridgeCallback());
            task.execute();
            app.registerTask(task);
            ui.message.setVisibility(View.VISIBLE);
        }else{
            updated++;
        }
    }

    private void statusExecutado() {
        updated++;
        if(updated == 3){
            NavegacaoUtil.navegar(SplashScreenActivity.this, MainActivity.class);
            finish();
        }
    }

    private Callback onFindTimesCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                List<Time> times = (List<Time>) objects[0];
                for(Time time : times){
                    app.mapTimes.put(time.getId(), time);
                }
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private Callback onFindSettingsCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                app.settings = (Settings) objects[0];
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private Callback onBridgeCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                checkStatus(StatusEnum.EXECUTADO);
                SessionUtil.addValue(SplashScreenActivity.this, Constants.UPDATED, false);
            }
        };
    }

}
