package com.vagnnermartins.marcaponto.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.vagnnermartins.marcaponto.dto.PontoDTO;

import org.json.JSONException;

public class SessionUtil {

    private final static String PREFERENCES = "map";

    public static void addValue(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        settings.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        return preferences.getBoolean(key, true);
    }

    public static PontoDTO recuperarPonto(Context context, String chave){
        PontoDTO ponto = null;
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
            String retorno = preferences.getString(chave, "");
            if(!retorno.equals("")){
                ponto = PontoDTO.fromJsonToObject(retorno);
            }
            ponto.setDiaFormatado(chave);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ponto;
    }
}