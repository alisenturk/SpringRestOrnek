package com.alisenturk.config;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alisenturk.util.Helper;

@Repository
//@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConnectionPool implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init(){
		//System.out.println("ConnectionPool.init..:" + this);
	}
	
	@PreDestroy
	public void destroy(){		
		//System.out.println("ConnectionPool.destroy..:" + this);
	}
	
	private Connection loadWebSphereDataSource() throws NamingException, SQLException{
		String dataSourceName;
    	DataSource ds;   
	   		 	
    	Connection connection = null;
    	/*** WebSphere Config	*/	    	 
    	InitialContext context;
    	dataSourceName = Helper.getAppMessage("dataSourceName"); //"Production -> jdbc/appl  Test-> jdbc/appltest"; //app dbd 
    	context = new InitialContext();
    	ds = (DataSource) context.lookup(dataSourceName);
    	connection = ds.getConnection();
    	
    	return connection;
	}
	private Connection loadTomcatDataSource(){
		/** Tomcat Config **/
    	Context contextTom;
    	Connection connection = null;
		try {
			contextTom = (Context) new InitialContext().lookup("java:comp/env");
			connection = ((DataSource) contextTom.lookup(Helper.getAppMessage("dataSourceName"))).getConnection();
		} catch (NamingException | SQLException e1) {
		}
		return connection;
	}
	private Connection initConnection(){
		Connection connection = null;
   		if(Constants.PROD_STAGE.equals(ProdStage.DEVELOPMENT)){
	   		String 	userId           = Helper.getAppMessage("db2.userId");
	   		String 	password         = Helper.getAppMessage("db2.password");
	   		String 	url              = Helper.getAppMessage("db2.url");
	   		
	   		//Date begin = new Date();	   		
	   		try{
	   			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
	   			connection = DriverManager.getConnection( url, userId, password );
	   		}catch(Exception e){
	   			Helper.errorLogger(getClass(), e);
	   		}finally{
	   			//System.out.println("Connection Suresi..:" + Helper.dateDifferent(begin, new Date(),Calendar.MILLISECOND));
	   			userId 		= null;
	   			password 	= null;
	   			url			= null;
	   		}
   		
   		}else{
   			try{
   				connection = loadWebSphereDataSource();
   			}catch(NamingException ne){
   				connection = loadTomcatDataSource();
   			}catch(Exception e){
   				connection = loadTomcatDataSource();				
   				Helper.errorLogger(getClass(), e);
   			}
   		}
   		//System.out.println("conn.init.:" + connection.toString() + " -> " + new Date());
   		return connection;
   
	}

	public void closeConnection(Connection connection){
		try{
			if(connection!=null && !connection.isClosed()){
				//System.out.println("conn.closed..:" + connection.toString() + " -> " + new Date());
				connection.close();
			}
			connection = null;
		}catch(RuntimeException re){
			Helper.errorLogger(getClass(), re);
		}catch(Exception e){
			Helper.errorLogger(getClass(), e);
		}
	}

	public Connection getConnection() {		
		return initConnection();
	}
	
	public void closeStatement(Statement st){
		try{
			if(st!=null)st.close();
		}catch(RuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e);
		}
	}
	
	public void closeStatement(PreparedStatement st){
		try{
			if(st!=null)st.close();
		}catch(RuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e);
		}
	}
	
	public void closeResultSet(ResultSet rs){
		try{
			if(rs!=null)rs.close();
		}catch(RuntimeException | SQLException e){
			Helper.errorLogger(getClass(), e);
		}
	}
	
	public void rollBack(Connection con){
		try {
			con.rollback();
		} catch (RuntimeException | SQLException e) {
			Helper.errorLogger(getClass(), e);
		}
	}
	
}
