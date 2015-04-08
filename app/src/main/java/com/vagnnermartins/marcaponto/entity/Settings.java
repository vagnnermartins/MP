package com.vagnnermartins.marcaponto.entity;

import com.vagnnermartins.marcaponto.singleton.SingletonAdapter;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by vagnnermartins on 07/04/15.
 */
public class Settings {

    private long id;
    private double hourly;

    public void save() {
        Settings qSettings = new Settings();
        qSettings.setId(getId());
        SingletonAdapter.getInstance().getAdapter().update(this, qSettings);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getHourly() {
        return new BigDecimal(hourly , MathContext.DECIMAL64);
    }

    public void setHourly(BigDecimal hourly) {
        this.hourly = hourly.doubleValue();
    }
}
