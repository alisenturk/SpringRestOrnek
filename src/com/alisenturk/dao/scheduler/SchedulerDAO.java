package com.alisenturk.dao.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alisenturk.config.ConnectionPool;
import com.alisenturk.model.scheduler.SchedulerLog;
import com.alisenturk.util.Helper;


@Repository
public class SchedulerDAO implements ISchedulerDAO {

	@Autowired
	ConnectionPool datasource;

	@Override
	public void insertSchedulerLog(SchedulerLog log) {
		StringBuilder 		sql 	= null;
		Connection			conn	= null;
		PreparedStatement	ps		= null;
		
		try{
			sql = new StringBuilder();
			sql.append("INSERT INTO SCHEDULER_LOG ");
			sql.append(" ( ");
			sql.append(" SCHEDULERNAME,SERVERIP,STATUSCODE,STATUSMESSAGE, ");
			sql.append(" schedulerTime ");
			sql.append(" ) VALUES ");
			sql.append(" (?,?, ");
			sql.append(" ?,?, ");
			sql.append(" ? ");
			sql.append(" ) ");
			
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1,log.getSchedulerName());
			ps.setString(2,log.getServerIP());
			ps.setString(3,log.getStatusCode());
			ps.setString(4,log.getStatusMessage());
			ps.setString(5,log.getSchedulerTime());
			ps.executeUpdate();
			
		}catch(Exception e){
			Helper.errorLogger(getClass(), e);
		}finally{
			sql = null;
			datasource.closeConnection(conn);
			datasource.closeStatement(ps);
		}
		
	}
	
	

	
	
	
}
