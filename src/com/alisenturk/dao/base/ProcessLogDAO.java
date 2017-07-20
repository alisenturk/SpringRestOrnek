package com.alisenturk.dao.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;

import com.alisenturk.config.ConnectionPool;
import com.alisenturk.config.Constants;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.ProcessLog;
import com.alisenturk.util.Helper;

@Repository
@Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProcessLogDAO implements IProcessLogDAO{

	@Autowired
	ConnectionPool dataSource;

	@Override
	public void insertLog(ProcessLog log) {
		StringBuilder 		sql = new StringBuilder();
		PreparedStatement 	ps = null;
		Connection conn = null;
		try{
			if(log.getToken()!=null && log.getToken().length()>5 && (log.getByUser()==null || log.getByUser().equals("NULL"))){
				log.setByUser(findUsernameByToken(log.getToken()));
			}
			if(log.getByUser()==null || log.getByUser().equalsIgnoreCase("NULL")){
				throw new HBRuntimeException("Kullanici bilgisi tanımsız!");
			}
			if(!Helper.checkValidCoordinate(log.getLattitude())){
				log.setLattitude("-1");
			}
			if(!Helper.checkValidCoordinate(log.getLongitude())){
				log.setLongitude("-1");
			}
			
			sql.append("INSERT INTO PROCESSLOG ");
			sql.append("(	BYUSER, DESCRIPTION, " );
			sql.append("	CLASSNAME, METHODNAME, PROCESSDATA, ");
			sql.append("	lattitude,Longitude,ipAddress, ");
			sql.append(" 	personelNo, ");
			sql.append("	iconClass, viewTimeline,detailInfo, ");
			sql.append("	groupCode,statusCode,statusMessage, ");
			sql.append("	viewEveryone ");
			sql.append(" ) ");
			sql.append(" VALUES (?, ?, ");
			sql.append(" ?,?,?, ");
			sql.append(" ?,?,?, ");
			sql.append(" DB2ADMIN.FIND_PERSONEL_ID('"+log.getByUser()+"') , ");
			sql.append(" ?,?,?, ");
			sql.append(" ?,?,?, ");
			sql.append(" ? ");
			sql.append(") ");
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1,Helper.checkNulls(log.getByUser(),"NULL"));
			ps.setString(2,Helper.checkNulls(log.getDescription(),""));
			ps.setString(3,log.getClassName());
			ps.setString(4,log.getMethodName());
			ps.setString(5,log.getProcessData());
			ps.setString(6,Helper.checkNulls(log.getLattitude(),"-1"));
			ps.setString(7,Helper.checkNulls(log.getLongitude(),"-1"));
			ps.setString(8,Helper.checkNulls(log.getIpAddress(),"-1"));
			ps.setString(9,log.getIconClass());
			ps.setInt(10,log.isViewTimeline()?1:0);
			ps.setString(11,Helper.checkNulls(log.getDetailInfo(),""));
			ps.setString(12,Helper.checkNulls(log.getGroup(),"ANY"));
			ps.setString(13,Helper.checkNulls(log.getStatusCode(),""));
			ps.setString(14,Helper.checkNulls(log.getStatusMessage(),""));
			ps.setInt(15,(log.isViewEveryone()?1:0));
			
			ps.executeUpdate();
			
		}catch(HBRuntimeException | SQLException e){
			e.printStackTrace();
			Helper.errorLogger(getClass(), e,log.toString());
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
			}
			dataSource.closeConnection(conn);
		}
		
	}
	
	@Override
	@Cacheable(value="kullaniciAdiCache",key="#token",unless="#result==null")
	public String findUsernameByToken(String token){
		String 				username 	= null;
		StringBuilder 		sql			= new StringBuilder(); 
		PreparedStatement 	pst 		= null;
		ResultSet 			rs			= null;
		Connection			conn		= null;
		try{
			sql.append("SELECT ");			
			sql.append("	it.kullanici_adi ");						
			sql.append("FROM  ");
			sql.append("	token_tbl it  ");
			sql.append("WHERE  ");
			sql.append("	it.TOKEN = ? ");
			sql.append(Constants.DB2_READ_ONLY_QUERY);
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql.toString());
			pst.setString(1,token);			
			rs = pst.executeQuery();
			
			while(rs.next()){
				username =  rs.getString("kullanici_adi");				
			}
			rs.close();
			pst.close();			
			
		}catch(RuntimeException | SQLException e){			
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeResultSet(rs);
			dataSource.closeStatement(pst);
		}
		return username;
	}
	@Override
	public void clearLog() {
		StringBuilder 		sql = new StringBuilder();
		PreparedStatement 	ps = null;
		Connection conn = null;
		try{
			sql.append("DELETE FROM PROCESSLOG ");
			sql.append("WHERE ");
			sql.append(" PROCESSDATE < TO_DATE('"+Helper.date2String(new Date())+"','dd/MM/yyyy') ");
						
			conn = dataSource.getConnection();
			ps = dataSource.getConnection().prepareStatement(sql.toString());
			
			ps.executeUpdate();
			
		}catch(HBRuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e,"[SQL]:" + sql.toString());
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
			}
			dataSource.closeConnection(conn);
		}
		
	}

	@Override
	public int getLoginErrorCount(String username){
		Connection 			conn	= null;
		PreparedStatement	ps		= null;		
		ResultSet  			rs		= null;
		StringBuilder		sql		= new StringBuilder();
		int errorCount = 0;
		try{
			sql.append("SELECT ");
			sql.append("  count(1) errorCount ");
			sql.append("FROM ");
			sql.append("	processlog pl ");
			sql.append("WHERE ");			 
			sql.append("	pl.username = ? ");
			sql.append("	AND pl.methodname = 'ERRORLOGIN' ");
			
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1,username);
			rs = ps.executeQuery();
			
			while(rs.next()){
				errorCount = rs.getInt("errorCount");
			}
			
		}catch(HBRuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e);
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeResultSet(rs);
			dataSource.closeStatement(ps);
		}
		
		return errorCount;
		
	}
	
	public void deleteLoginError(){
		Connection 			conn	= null;
		PreparedStatement	ps		= null;		
		StringBuilder		sql		= new StringBuilder();
		int errorCount = 0;
		try{
			sql.append("DELETE ");
			sql.append("FROM ");
			sql.append("	processlog pl ");
			sql.append("WHERE ");
			sql.append("	pl.methodname = 'ERRORLOGIN' ");
			sql.append("	and pl.processdate <( CURRENT TIMESTAMP - 15 MINUTES ) "); 
			
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			
		}catch(HBRuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e);
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeStatement(ps);
		}
	}

	@Override
	@Async
	public Future<Boolean>  insertLogAsync(ProcessLog log) {
		insertLog(log);
		return new AsyncResult<Boolean>(true);
		
	}

}
