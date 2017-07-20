package com.alisenturk.model.scheduler;

import java.io.Serializable;
import java.util.Date;

public class SchedulerLog implements Serializable {
	
	
	private long		id;
	private String		schedulerName;
	private Date		lastRunTime;
	private String		statusCode;
	private String		statusMessage;
	private String		serverIP;
	private String		schedulerTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public Date getLastRunTime() {
		return lastRunTime;
	}
	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getSchedulerTime() {
		return schedulerTime;
	}
	public void setSchedulerTime(String schedulerTime) {
		this.schedulerTime = schedulerTime;
	}
	
	
}
