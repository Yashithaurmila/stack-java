package com.staging.stack.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity

public class History {
	
	@Id
	
	private long id;
	private String engineerName;
	private String description;
	private String time;
	private long instanceId;
	
	public static String getCurrentTimeUsingCalendar() {
	    Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String formattedDate=dateFormat.format(date);
	    return formattedDate;
	}

	public History() {
		super();
		// TODO Auto-generated constructor stub
	}

	public History(int id, String engineerName, String description, String time, Long instanceId) {
		super();
		this.id = id;
		this.engineerName = engineerName;
		this.description = description;
		this.time = getCurrentTimeUsingCalendar();
		this.instanceId = instanceId;
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

	@Override
	public String toString() {
		return "History [id=" + id + ", engineerName=" + engineerName + ", desc=" + description + ", time=" + time + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, engineerName, id, instanceId, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		History other = (History) obj;
		return Objects.equals(description, other.description) && Objects.equals(engineerName, other.engineerName)
				&& id == other.id && instanceId == other.instanceId && Objects.equals(time, other.time);
	}
	
	
	
	
}
