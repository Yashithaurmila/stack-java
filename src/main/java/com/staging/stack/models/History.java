package com.staging.stack.models;

import com.staging.stack.StackApplication;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

@Entity


public class History {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long engineerId;
	private String engineerName;
	private String description;
	private String time;
	private String stopTime;
	private long lifeTime;
	private long instanceId;

	public static String getCurrentTimeUsingCalendar() {
		Calendar cal = Calendar.getInstance();
		Date date=cal.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate=dateFormat.format(date);
		return formattedDate;
	}
	public static Date stringToDate(String d) throws ParseException {
		SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter6.parse(d);
		return date;
	}

	public static long calculateLifeTime(String s1, String s2)throws Exception{
	    Date d1 = stringToDate(s1);
	    Date d2 = stringToDate(s2);
	    long diff = d2.getTime() - d1.getTime();
	    return diff;
	}

	public History() {
		super();
		// TODO Auto-generated constructor stub
	}

	public History (long id, long engineerId, String engineerName, String description, String time, Long instanceId) throws Exception {
		super();
		this.id = id;
		this.engineerId = engineerId;
		this.engineerName = engineerName;
		this.description = description;
		this.time = getCurrentTimeUsingCalendar();
		this.instanceId = instanceId;
		this.stopTime = getCurrentTimeUsingCalendar();
        this.lifeTime = calculateLifeTime(time, stopTime);


	}

	public long getInstanceId() {


		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public String getDesc() {
		return this.description;
	}

	public void setDesc(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime() {
		this.time = getCurrentTimeUsingCalendar();
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime() {
		this.stopTime = getCurrentTimeUsingCalendar();
	}

	public long getEngineerId() {
		return engineerId;
	}

	public void setEngineerId(long engineerId) {
		this.engineerId = engineerId;
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime() throws Exception{
		this.lifeTime = calculateLifeTime(this.getTime(), this.getStopTime());
	}
}
