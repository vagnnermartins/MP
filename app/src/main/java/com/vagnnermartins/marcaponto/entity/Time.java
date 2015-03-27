package com.vagnnermartins.marcaponto.entity;

import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.Date;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class Time {

    private int id;
    private String entrance;
    private String pause;
    private String back;
    private String quit;

    public void save() {
        Time qHistory = new Time();
        qHistory.setId(getId());
        SingletonAdapter.getInstance().getAdapter().update(this, qHistory);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEntrance() {
        return Long.parseLong(entrance);
    }

    public void setEntrance(long entrance) {
        this.entrance = entrance + "";
    }

    public String getFormattedEntrance() {
        return DataUtil.getFormattedHours(entrance);
    }

    public long getPause() {
        return Long.parseLong(pause);
    }

    public void setPause(long pause) {
        this.pause = pause + "";
    }

    public String getFormattedPause() {
        return DataUtil.getFormattedHours(pause);
    }

    public long getBack() {
        return Long.parseLong(back);
    }

    public void setBack(long back) {
        this.back = back + "";
    }

    public String getFormattedBack() {
        return DataUtil.getFormattedHours(back);
    }

    public long getQuit() {
        return Long.parseLong(quit);
    }

    public void setQuit(long quit) {
        this.quit = quit + "";
    }

    public String getFormattedQuit() {
        return DataUtil.getFormattedHours(quit);
    }

    public int getTotalDifferencesSecond() {
        return (int) (DataUtil.differencesBetweenToDatesInSeconds(new Date(Long.parseLong(pause)), new Date(Long.parseLong(entrance)))
                + DataUtil.differencesBetweenToDatesInSeconds(new Date(Long.parseLong(quit)), new Date(Long.parseLong(back))));
    }

    public boolean hasTime() {
        return !entrance.equals(0) || !pause.equals(0) || !back.equals(0) || !quit.equals(0);
    }
}
