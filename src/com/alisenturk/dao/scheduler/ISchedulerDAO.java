package com.alisenturk.dao.scheduler;

import java.io.Serializable;

import com.alisenturk.model.scheduler.SchedulerLog;


public interface ISchedulerDAO extends Serializable {
	
	public void insertSchedulerLog(SchedulerLog log);
}
