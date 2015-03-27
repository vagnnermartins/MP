package com.vagnnermartins.marcaponto.entity;

import com.codeslap.persistence.Constraint;
import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by vagnnermartins on 24/03/15.
 */
public class History {

    private long id;
    private String monthYear;
    private long day;
    private String entrance;
    private String pause;
    private String back;
    private String quit;

    public void save(){
        History qHistory = new History();
        qHistory.setId(getId());
        SingletonAdapter.getInstance().getAdapter().update(this, qHistory);
    }

    public static List<History> findHistoryByMonthYear(String monthYear){
        History qHistory = new History();
        qHistory.setMonthYear(monthYear);
        return  SingletonAdapter.getInstance().getAdapter().findAll(qHistory, new Constraint().orderBy("day"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getFormattedDay(){
        return DataUtil.transformDateToSting(new Date(day), "dd/MM/yy");
    }

    public long getEntrance() {
        if(entrance == null || entrance.equals("")){
            entrance = "0";
        }
        return Long.parseLong(entrance);
    }

    public void setEntrance(long entrance) {
        this.entrance = entrance + "";
    }

    public String getFormattedEntrance(){
        return DataUtil.getFormattedHours(entrance);
    }

    public long getPause() {
        if(pause == null || pause.equals("")){
            pause = "0";
        }
        return Long.parseLong(pause);
    }

    public void setPause(long pause) {
        this.pause = pause + "";
    }

    public String getFormattedPause(){
        return DataUtil.getFormattedHours(pause);
    }

    public long getBack() {
        if(back == null || back.equals("")){
            back = "0";
        }
        return Long.parseLong(back);
    }

    public void setBack(long back) {
        this.back = back + "";
    }

    public String getFormattedBack(){
        return DataUtil.getFormattedHours(back);
    }

    public long getQuit() {
        if(quit == null || quit.equals("")){
            quit = "0";
        }
        return Long.parseLong(quit);
    }

    public void setQuit(long quit) {
        this.quit = quit + "";
    }

    public String getFormattedQuit(){
        return DataUtil.getFormattedHours(quit);
    }

    public int getTotalDifferencesSecond(){
        return (int) (DataUtil.differencesBetweenToDatesInSeconds(new Date(getPause()),new Date(getEntrance()))
                + DataUtil.differencesBetweenToDatesInSeconds(new Date(getQuit()),new Date(getBack())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof History)) return false;

        History history = (History) o;

        if (!monthYear.equals(history.monthYear)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return monthYear.hashCode();
    }
}
