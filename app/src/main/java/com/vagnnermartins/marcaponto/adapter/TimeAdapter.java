package com.vagnnermartins.marcaponto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.Time;
import com.vagnnermartins.marcaponto.util.DataUtil;

import java.util.List;

/**
 * Created by vagnnermartins on 26/03/15.
 */
public class TimeAdapter extends ArrayAdapter<Time> {

    public TimeAdapter(Context context, int resource, List<Time> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_time, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Time item = getItem(position);
        holder.day.setText(DataUtil.getDayOfWeek(item.getId()));
        holder.entrance.setText(item.getFormattedEntrance());
        holder.pause.setText(item.getFormattedPause());
        holder.back.setText(item.getFormattedBack());
        holder.quit.setText(item.getFormattedQuit());
        return convertView;
    }

    class ViewHolder{

        TextView day;
        TextView entrance;
        TextView pause;
        TextView back;
        TextView quit;

        public ViewHolder(View view){
            this.day = (TextView) view.findViewById(R.id.item_time_day);
            this.entrance = (TextView) view.findViewById(R.id.item_time_entrance);
            this.pause = (TextView) view.findViewById(R.id.item_time_pause);
            this.back = (TextView) view.findViewById(R.id.item_time_back);
            this.quit = (TextView) view.findViewById(R.id.item_time_quit);
        }
    }
}
