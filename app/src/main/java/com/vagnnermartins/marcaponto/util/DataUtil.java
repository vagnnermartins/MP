package com.vagnnermartins.marcaponto.util;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.vagnnermartins.marcaponto.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DataUtil {
    public static String transformDateToSting(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date transformStringToDate(String format, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String formatDateToString(Date time, Resources res){
        String result = "";
        if(res.getConfiguration().locale.getLanguage().equals("pt")){
            result = getPtDateToString(time, res);
        }else if(res.getConfiguration().locale.getLanguage().equals("es")){
            result = getEsDateToString(time, res);
        }else{
            result = getDefaultDateToString(time, res);
        }
        return result;
    }

    private static String getPtDateToString(Date time, Resources res){
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        sb.append(checkDay(calendar, res));
        sb.append(", ");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(" de ");
        sb.append(getMonth(calendar.get(Calendar.MONTH), res));
        sb.append(" de ");
        sb.append(calendar.get(Calendar.YEAR));
        return sb.toString();
    }

    private static String getDefaultDateToString(Date time, Resources res){
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        sb.append(checkDay(calendar, res));
        sb.append(", ");
        sb.append(getMonth(calendar.get(Calendar.MONTH), res));
        sb.append(" ");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(", ");
        sb.append(calendar.get(Calendar.YEAR));
        return sb.toString();
    }

    private static String getEsDateToString(Date time, Resources res){
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        sb.append(checkDay(calendar, res));
        sb.append(", ");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(" de ");
        sb.append(getMonth(calendar.get(Calendar.MONTH), res));
        sb.append(" ");
        sb.append(calendar.get(Calendar.YEAR));
        return sb.toString();
    }

    public static Calendar getResetedDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private static String checkDay(Calendar calendar, Resources res) {
        String result = "";
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        if(today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = res.getString(R.string.today);
        }else if(tomorrow.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                tomorrow.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                tomorrow.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = res.getString(R.string.tomorrow);
        }else if(yesterday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                yesterday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                yesterday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = res.getString(R.string.yesterday);
        }else{
            result = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK), res);
        }
        return result;
    }

    public static String getMonthYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String formattedDate = calendar.get(Calendar.MONTH) + " / ";
        formattedDate += DataUtil.transformDateToSting(calendar.getTime(), "yyyy");
        return formattedDate;
    }

    public static long differencesBetweenToDatesInSeconds(Date bigger, Date smaller){
        return (bigger.getTime() - smaller.getTime()) / 1000;
    }

    public static String getFormattedHours(String date){
        String result = "";
        if(date == null || date.equals("0") || date.equals("")){
            result = "--:--";
        }else{
            result = DataUtil.transformDateToSting(new Date(Long.parseLong(date)), "HH:mm");
        }
        return result;
    }

    public static String transformSecondsInHourMinutes(int totalSeconds){
        int minutes = totalSeconds / 60;
        int minute = minutes % 60;
        int hour = minutes / 60;
        DecimalFormat format = new DecimalFormat("00");
        String result = format.format(hour) + ":" + format.format(minute);
        if(result.equals("00:00") || result.equals("")){
            result = "--:--";
        }
        return result;
    }

    public static String getMonth(int month, Resources res){
        String retorno;
        switch (month) {
            case Calendar.JANUARY:
                retorno = res.getString(R.string.january);
                break;
            case Calendar.FEBRUARY:
                retorno = res.getString(R.string.february);
                break;
            case Calendar.MARCH:
                retorno = res.getString(R.string.march);
                break;
            case Calendar.APRIL:
                retorno = res.getString(R.string.april);
                break;
            case Calendar.MAY:
                retorno = res.getString(R.string.may);
                break;
            case Calendar.JUNE:
                retorno = res.getString(R.string.june);
                break;
            case Calendar.JULY:
                retorno = res.getString(R.string.july);
                break;
            case Calendar.AUGUST:
                retorno = res.getString(R.string.august);
                break;
            case Calendar.SEPTEMBER:
                retorno = res.getString(R.string.september);
                break;
            case Calendar.OCTOBER:
                retorno = res.getString(R.string.october);
                break;
            case Calendar.NOVEMBER:
                retorno = res.getString(R.string.november);
                break;
            default:
                retorno = res.getString(R.string.december);
                break;
        }
        return retorno;
    }

	public static String getDayOfWeek(int dia, Resources res){
		String retorno;
		switch (dia) {
		case Calendar.SUNDAY:
			retorno = res.getString(R.string.sunday);
			break;
		case Calendar.MONDAY:
            retorno = res.getString(R.string.monday);
			break;
		case Calendar.TUESDAY:
            retorno = res.getString(R.string.tuesday);
			break;
		case Calendar.WEDNESDAY:
            retorno = res.getString(R.string.wednesday);
			break;
		case Calendar.THURSDAY:
            retorno = res.getString(R.string.thursday);
			break;
		case Calendar.FRIDAY:
            retorno = res.getString(R.string.friday);
			break;
		default:
            retorno = res.getString(R.string.saturday);
			break;
		}
		return retorno;
	}
}