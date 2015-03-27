package com.vagnnermartins.marcaponto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by vagnnermartins on 25/03/15.
 */
public class HistoryAdapter extends ArrayAdapter<History> {

    private final Map<Integer, Time> times;

    public HistoryAdapter(Context context, int resource, List<History> objects, Map<Integer, Time> times) {
        super(context, resource, objects);
        this.times = times;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_history, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        History item = getItem(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DataUtil.transformStringToDate("dd/MM/yyyy", item.getDay()));
        holder.day.setText(DataUtil.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        holder.date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.totalHour.setText(DataUtil.transformSecondsInHourMinutes(item.getTotalDifferencesSecond()));
        holder.entrance.setText(item.getFormattedEntrance());
        holder.pause.setText(item.getFormattedPause());
        holder.back.setText(item.getFormattedBack());
        holder.quit.setText(item.getFormattedQuit());
        if(times.get(calendar.get(Calendar.DAY_OF_WEEK)) != null){
            calcBalance(item, calendar.get(Calendar.DAY_OF_WEEK), holder);
        }
        return convertView;
    }

    private void calcBalance(History history, int dayOfWeek, ViewHolder holder) {
        Time time = times.get(dayOfWeek);
        int difference = history.getTotalDifferencesSecond() - time.getTotalDifferencesSecond();
        if(history.getTotalDifferencesSecond() == 0){
            holder.balance.setText(R.string.no_hour);
            holder.balance.setTextColor(getContext().getResources().getColor(R.color.second_text));
        }else if(time.getTotalDifferencesSecond() != 0){
            holder.balance.setText(DataUtil.transformSecondsInHourMinutes(Math.abs(difference)));
            if(difference > 0){
                holder.balance.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
            }else if(difference < 0){
                holder.balance.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
            }else if(difference == 0){
                holder.balance.setText(R.string.zero_hour);
                holder.balance.setTextColor(getContext().getResources().getColor(R.color.second_text));
            }
        }
    }

    class ViewHolder{

        TextView day;
        TextView date;
        TextView totalHour;
        TextView balance;
        TextView entrance;
        TextView pause;
        TextView back;
        TextView quit;

        public ViewHolder(View view){
            this.day = (TextView) view.findViewById(R.id.item_history_day);
            this.date = (TextView) view.findViewById(R.id.item_history_date);
            this.totalHour = (TextView) view.findViewById(R.id.item_history_total);
            this.balance = (TextView) view.findViewById(R.id.item_history_balance);
            this.entrance = (TextView) view.findViewById(R.id.item_history_entrance);
            this.pause = (TextView) view.findViewById(R.id.item_history_pause);
            this.back = (TextView) view.findViewById(R.id.item_history_back);
            this.quit = (TextView) view.findViewById(R.id.item_history_quit);
        }
    }
}
