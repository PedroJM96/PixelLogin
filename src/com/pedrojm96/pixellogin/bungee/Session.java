package com.pedrojm96.pixellogin.bungee;

import java.util.concurrent.TimeUnit;

public class Session {
	
	private long time;
	private TimeUnit type;
	private int sessiontime;
	private String ip;
	
	public Session(long time, int sessiontime,String type,String ip) {
		this.time = time;
		this.sessiontime = sessiontime;
		this.ip = ip;
		
		if(type.equalsIgnoreCase("SECONDS")) {
			this.type = TimeUnit.SECONDS;
		} else if(type.equalsIgnoreCase("MINUTES")) {
			this.type = TimeUnit.MINUTES;
		}else if(type.equalsIgnoreCase("DAYS")) {
			this.type = TimeUnit.DAYS;
		}else {
			this.type = TimeUnit.MINUTES;
		}
	}
	
	public boolean isActive(String ip) {
		if(!this.ip.equals(ip)) {
			//System.out.print("La ip no es igual..................................");
			return false;
		}
		long stime = System.currentTimeMillis();
		
		long transcurrido = stime - time;
		
		if(transcurrido<=type.toMillis(sessiontime)) {
			//System.out.print("Esta activa la seccion..................................");
			return true;
		}else {
			return false;
		}
	}
	
	

}
