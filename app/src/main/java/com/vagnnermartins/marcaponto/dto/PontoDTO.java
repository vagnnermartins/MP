package com.vagnnermartins.marcaponto.dto;

import android.annotation.SuppressLint;

import com.vagnnermartins.marcaponto.util.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("DefaultLocale")
@SuppressWarnings("serial")
public class PontoDTO implements Serializable{

	private Calendar dia;
	private Calendar horaEntrada;
	private Calendar horaSaidaPausa;
	private Calendar horaVolta;
	private Calendar horaSaida;
	private String diaFormatado;
	public static PontoDTO fromJsonToObject(String json) throws JSONException{
		PontoDTO ponto = null;
		JSONObject obj = (JSONObject) new JSONObject(json);
		ponto = new PontoDTO();
		ponto.setDia(longToCalendar(obj.getLong("dia")));
		ponto.setHoraEntrada(longToCalendar(obj.getLong("horaEntrada")));
		ponto.setHoraSaidaPausa(longToCalendar(obj.getLong("horaSaidaPausa")));
		ponto.setHoraVolta(longToCalendar(obj.getLong("horaVolta")));
		ponto.setHoraSaida(longToCalendar(obj.getLong("horaSaida")));
		return ponto;
	}
	public String toJson(){
		JSONObject json = new JSONObject();
		try {
			json.put("dia", dia.getTime().getTime());
			json.put("horaEntrada", horaEntrada.getTime().getTime());
			json.put("horaSaidaPausa", horaSaidaPausa.getTime().getTime());
			json.put("horaVolta", horaVolta.getTime().getTime());
			json.put("horaSaida", horaSaida.getTime().getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	private static Calendar longToCalendar(long time){
		Date d = new Date(time);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}
	public void setDiaFormatado(String diaFormatado){
		this.diaFormatado = diaFormatado;
	}
	public String getDiaFormatado(){
		if(diaFormatado == null || diaFormatado.equals("")){
			diaFormatado = dia.get(Calendar.DAY_OF_MONTH) + "/" + 
					dia.get(Calendar.MONTH) + "/" + 
					dia.get(Calendar.YEAR);
		}
		return diaFormatado;
	}
	public Calendar getDia() {
		return dia;
	}
	public void setDia(Calendar dia) {
		this.dia = dia;
	}
	public Calendar getHoraEntrada() {
		if(horaEntrada ==  null){
			horaEntrada = Calendar.getInstance();
			horaEntrada.set(Calendar.HOUR_OF_DAY, 00);
			horaEntrada.set(Calendar.MINUTE, 00);
			horaEntrada.set(Calendar.SECOND, 00);
		}
		return horaEntrada;
	}
	public void setHoraEntrada(Calendar horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Calendar getHoraSaidaPausa() {
		if(horaSaidaPausa ==  null){
			horaSaidaPausa = Calendar.getInstance();
			horaSaidaPausa.set(Calendar.HOUR_OF_DAY, 00);
			horaSaidaPausa.set(Calendar.MINUTE, 00);
			horaSaidaPausa.set(Calendar.SECOND, 00);
		}
		return horaSaidaPausa;
	}
	public void setHoraSaidaPausa(Calendar horaSaidaPausa) {
		this.horaSaidaPausa = horaSaidaPausa;
	}
	public Calendar getHoraVolta() {
		if(horaVolta ==  null || horaVolta.equals("")){
			horaVolta = Calendar.getInstance();
			horaVolta.set(Calendar.HOUR_OF_DAY, 00);
			horaVolta.set(Calendar.MINUTE, 00);
			horaVolta.set(Calendar.SECOND, 00);
		}
		return horaVolta;
	}
	public void setHoraVolta(Calendar horaVolta) {
		this.horaVolta = horaVolta;
	}
	public Calendar getHoraSaida() {
		if(horaSaida ==  null || horaSaida.equals("")){
			horaSaida = Calendar.getInstance();
			horaSaida.set(Calendar.HOUR_OF_DAY, 00);
			horaSaida.set(Calendar.MINUTE, 00);
			horaSaida.set(Calendar.SECOND, 00);
		}
		return horaSaida;
	}
	public void setHoraSaida(Calendar horaSaida) {
		this.horaSaida = horaSaida;
	}
	@SuppressLint("DefaultLocale")
	public String getHorasTrabalhadas() {
		String retorno = null;
		try {
			int segundo = 0; 
			int minutos; 
			int minuto = 0; 
			int hora = 0; 
			long segundosPrimeiroPeriodo = diferencaEntreDuasDatasEmSegundos(horaSaidaPausa.getTime(), horaEntrada.getTime());
			long segundosSegundoPeriodo = diferencaEntreDuasDatasEmSegundos(horaSaida.getTime(), horaVolta.getTime());
			int segundosTrabalhados = (int) (segundosPrimeiroPeriodo + segundosSegundoPeriodo);
			segundo = segundosTrabalhados % 60; 
			minutos = segundosTrabalhados / 60; 
			minuto = minutos % 60; 
			hora = minutos / 60; 
			retorno = DataUtil.obterHoraMinutoComDoisDigitos(hora) + ":" +
					DataUtil.obterHoraMinutoComDoisDigitos(minuto) + ":" +
					DataUtil.obterHoraMinutoComDoisDigitos(segundo);
		} catch (Exception e) {
		}
       return retorno;
   }
	
	private long diferencaEntreDuasDatasEmSegundos(Date dateMaior, Date dateMenor){
		long diferenciaEmSegundos = (dateMaior.getTime() - dateMenor.getTime()) / 1000;
		return diferenciaEmSegundos;
	}
	public String getHoraEntradaFormatada() {
		String horaEntradaFormatada = DataUtil.obterHoraMinutoComDoisDigitos(getHoraEntrada().get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraEntrada().get(Calendar.MINUTE))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraEntrada().get(Calendar.SECOND));
		if(horaEntradaFormatada.equals("00:00:00")){
			horaEntradaFormatada = "--:--";
		}
		return horaEntradaFormatada;
	}
	public String getHoraSaidaPausaFormatada() {
		String horaSaidaPausaFormatada = DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaidaPausa().get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaidaPausa().get(Calendar.MINUTE))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaidaPausa().get(Calendar.SECOND));
		if(horaSaidaPausaFormatada.equals("00:00:00")){
            horaSaidaPausaFormatada = "--:--";
		}
		return horaSaidaPausaFormatada;
	}
	public String getHoraVoltaFormatada() {
		String horaVoltaFormatada = DataUtil.obterHoraMinutoComDoisDigitos(getHoraVolta().get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraVolta().get(Calendar.MINUTE))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraVolta().get(Calendar.SECOND));
		if(horaVoltaFormatada.equals("00:00:00")){
            horaVoltaFormatada = "--:--";
		}
		return horaVoltaFormatada;
	}
	public String getHoraSaidaFormatada() {
		String horaSaidaFormatada = DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaida().get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaida().get(Calendar.MINUTE))
				+ ":"
				+ DataUtil.obterHoraMinutoComDoisDigitos(getHoraSaida().get(Calendar.SECOND));
		if(horaSaidaFormatada.equals("00:00:00")){
            horaSaidaFormatada = "--:--";
		}
		return horaSaidaFormatada;
	}
}
