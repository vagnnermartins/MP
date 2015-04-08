package com.vagnnermartins.marcaponto.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.vagnnermartins.marcaponto.callback.Callback;
import com.vagnnermartins.marcaponto.contants.Constants;
import com.vagnnermartins.marcaponto.dto.PontoDTO;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.util.DataUtil;
import com.vagnnermartins.marcaponto.util.SessionUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by vagnnermartins on 08/04/15.
 */
public class BridgeAsyncTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final Callback callback;

    public BridgeAsyncTask(Context context, Callback callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SharedPreferences preferences = context.getSharedPreferences("map", 0);
        Map<String, ?> map = preferences.getAll();
        map.remove(Constants.NOTIFICATION);
        for (Map.Entry<String, ?> s : map.entrySet()){
            PontoDTO item = SessionUtil.recuperarPonto(context, s.getKey());
            if(item != null){
                History history = new History();
                history.setDay(item.getDiaFormatado());

                Calendar entrance = DataUtil.getResetedDay();
                entrance.set(Calendar.HOUR_OF_DAY, item.getHoraEntrada().get(Calendar.HOUR_OF_DAY));
                entrance.set(Calendar.MINUTE, item.getHoraEntrada().get(Calendar.MINUTE));
                history.setEntrance(entrance.getTime().getTime());

                Calendar pause = DataUtil.getResetedDay();
                pause.set(Calendar.HOUR_OF_DAY, item.getHoraSaidaPausa().get(Calendar.HOUR_OF_DAY));
                pause.set(Calendar.MINUTE, item.getHoraSaidaPausa().get(Calendar.MINUTE));
                history.setPause(pause.getTime().getTime());

                Calendar back = DataUtil.getResetedDay();
                back.set(Calendar.HOUR_OF_DAY, item.getHoraVolta().get(Calendar.HOUR_OF_DAY));
                back.set(Calendar.MINUTE, item.getHoraVolta().get(Calendar.MINUTE));
                history.setBack(back.getTime().getTime());

                Calendar quit = DataUtil.getResetedDay();
                quit.set(Calendar.HOUR_OF_DAY, item.getHoraSaida().get(Calendar.HOUR_OF_DAY));
                quit.set(Calendar.MINUTE, item.getHoraSaida().get(Calendar.MINUTE));
                history.setQuit(quit.getTime().getTime());
                history.saveOrUpdate();
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(s.getKey());
                editor.commit();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.onReturn(null);
    }
}
