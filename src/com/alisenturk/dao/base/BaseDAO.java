package com.alisenturk.dao.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alisenturk.util.Helper;

public abstract class BaseDAO implements Serializable {
		
	public long getLastId(Connection conn,String tableName){
		long lastId = 0l;
		Statement 	st = null;
		ResultSet	rs = null;
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT DISTINCT IDENTITY_VAL_LOCAL() AS VAL FROM " + tableName);
			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while(rs.next()){
				lastId = rs.getLong("VAL");
			}
			rs.close();
			st.close();
		}catch(SQLException e){
			Helper.errorLogger(getClass(), e);
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
		}
		return lastId;
	}
}
