package com.vagnnermartins.marcaponto.util;

import android.annotation.SuppressLint;

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

	public static Date transformStringToDate(String format, String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}

    public static String formatDateToString(Date time){
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        sb.append(checkDay(calendar));
        sb.append(", ");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(" de ");
        sb.append(getMonth(calendar.get(Calendar.MONTH)));
        sb.append(" de ");
        sb.append(calendar.get(Calendar.YEAR));
        return sb.toString();
    }

    private static String checkDay(Calendar calendar) {
        String result = "";
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        if(today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = "Hoje";
        }else if(tomorrow.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                tomorrow.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                tomorrow.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = "Amanhã";
        }else if(yesterday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                yesterday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                yesterday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            result = "Ontem";
        }else{
            result = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        }
        return result;
    }

    public static double diferencaEmDias(Date dataInicial, Date dataFinal){
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca /1000) / 60 / 60 /24;
        long horasRestantes = (diferenca /1000) / 60 / 60 %24;
        result = diferencaEmDias + (horasRestantes /24d);
        return result;
    }

    public static Date getLastDayOfMonth(int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        return  calendar.getTime();
    }

    public static String getMonthYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String formattedDate = DataUtil.getMonth(calendar.get(Calendar.MONTH));
        formattedDate += " / ";
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

    public static String getMonth(int month){
        String retorno;
        switch (month) {
            case Calendar.JANUARY:
                retorno = "Janeiro";
                break;
            case Calendar.FEBRUARY:
                retorno = "Fevereiro";
                break;
            case Calendar.MARCH:
                retorno = "Março";
                break;
            case Calendar.APRIL:
                retorno = "Abril";
                break;
            case Calendar.MAY:
                retorno = "Maio";
                break;
            case Calendar.JUNE:
                retorno = "Junho";
                break;
            case Calendar.JULY:
                retorno = "Julho";
                break;
            case Calendar.AUGUST:
                retorno = "Agosto";
                break;
            case Calendar.SEPTEMBER:
                retorno = "Setembro";
                break;
            case Calendar.OCTOBER:
                retorno = "Outubro";
                break;
            case Calendar.NOVEMBER:
                retorno = "Novembro";
                break;
            default:
                retorno = "Dezembro";
                break;
        }
        return retorno;
    }

	public static String getDayOfWeek(int dia){
		String retorno;
		switch (dia) {
		case Calendar.SUNDAY:
			retorno = "Dom";
			break;
		case Calendar.MONDAY:
			retorno = "Seg";
			break;
		case Calendar.TUESDAY:
			retorno = "Ter";
			break;
		case Calendar.WEDNESDAY:
			retorno = "Qua";
			break;
		case Calendar.THURSDAY:
			retorno = "Qui";
			break;
		case Calendar.FRIDAY:
			retorno = "Sex";
			break;
		default:
			retorno = "Sáb";
			break;
		}
		return retorno;
	}


    public static double diferencaEmHoras(Date dataInicial, Date dataFinal){
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        long diferencaEmHoras = (diferenca /1000) / 60 / 60;
        long minutosRestantes = (diferenca / 1000)/60 %60;
        double horasRestantes = minutosRestantes / 60d;
        result = diferencaEmHoras + (horasRestantes);
        return result;
    }

    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal){
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmMinutos = (diferenca /1000) / 60;
        long segundosRestantes = (diferenca / 1000)%60;
        result = diferencaEmMinutos + (segundosRestantes /60d);
        return result;
    }
}