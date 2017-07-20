package com.alisenturk.dao.token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.alisenturk.config.ConnectionPool;
import com.alisenturk.config.Constants;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.Helper;

@Repository
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenDAO implements ITokenDAO {

	
	@Autowired
	ConnectionPool dataSource;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	private String generateToken(int personelNo,String kullaniciAdi){
		UUID uuid = UUID.randomUUID();
		String key = uuid.toString()+"#"+personelNo; 
		String token = passwordEncoder.encode(key);
		
		return token;
	}
	public String tokenExists(int personelNo){
		StringBuilder 		sql = new StringBuilder();
		PreparedStatement 	st 	= null;
		ResultSet			rs 	= null;
		Connection			conn=null;
		String token = null;
		try {
			sql.append("SELECT token FROM token_tbl ");
			sql.append("WHERE ");
			sql.append("	sicil = ? ");
			sql.append("	AND durum = 'AKTIF' ");
			sql.append("	AND tarih >=(current timestamp -"+(Constants.TOKEN_LIFE)+" MINUTES ) ");
			sql.append(" FETCH FIRST 1 ROWS ONLY ");
			sql.append(Constants.DB2_READ_ONLY_QUERY);
			
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			st.setInt(1,personelNo);
			rs  = st.executeQuery();
			while(rs.next()){
				token = rs.getString("token");
			}
			rs.close();		
			st.close();
		} catch (SQLException e) {
			
			Helper.errorLogger(getClass(), e, "[SQL]..:" + sql.toString());
		} finally {
			dataSource.closeConnection(conn);
			dataSource.closeStatement(st);
			dataSource.closeResultSet(rs);
		}

		return token;
	}
	@Override
	public String tokenYarat(int personelNo,String kullaniciAdi,String latitude,String longitude) {
		
		String enlem 	= latitude;
		String boylam	= longitude;
		
		if(!Helper.checkValidCoordinate(enlem)){
			enlem = "-1";
		}
		if(!Helper.checkValidCoordinate(boylam)){
			boylam = "-1";
		}
		
		
		String token = tokenExists(personelNo); /** aynı kullanıcı için geçerli token var mı bakılıyor. **/
		
		if(token!=null && token.length()>10){
			return token;
		}
		
		token = generateToken(personelNo, kullaniciAdi);
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement st = null;
		Connection			conn=null;
		try {
			sql.append("INSERT INTO token_tbl (");
			sql.append("	sicil, ");
			sql.append("	token,  ");
			sql.append("	kullanici_adi , ");
			sql.append("	enlem, ");
			sql.append("	boylam ");
			sql.append(" ) ");
			sql.append("VALUES (?,?,?, ");
			sql.append("	?,?,? ");
			sql.append(" ) ");
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			st.setInt(1,personelNo);
			st.setString(2,token.trim());
			st.setString(3,kullaniciAdi.trim().toUpperCase());
			st.setString(4,Helper.checkNulls(enlem,"-1"));
			st.setString(5,Helper.checkNulls(boylam,"-1"));
			int affected = st.executeUpdate();

			if (affected < 1)
				return null;

			st.close();
		} catch (SQLException e) {
			token = null;
			Helper.errorLogger(getClass(), e, "[SQL]..:" + sql.toString());
		} finally {
			dataSource.closeConnection(conn);
			dataSource.closeStatement(st);
		}
		
		return token;
	}
	
	@Override
	public boolean tokenGecerliMi(String token) {
		
		StringBuilder sql = new StringBuilder();
		boolean gecerliMi = false;
		int result[] = tokenGecerliMiWithTime(token);
		gecerliMi 	 = result[0]==1;
		return gecerliMi;
	}
	
	@Override
	public void extendTokenValidity(String token){
		List<String> tokens = new ArrayList<>();
		tokens.add(token);
		extendTokenValidity(tokens);
	}
	@Override
	public boolean tokenSil(String token) {
		boolean sonuc = false;
		StringBuilder sql = new StringBuilder();
		PreparedStatement st = null;
		Connection			conn=null;
		try {
			sql.append("DELETE FROM token_tbl ");
			sql.append(" WHERE token = ? ");
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			st.setString(1,token.trim());
			
			int affected = st.executeUpdate();

			if (affected > 0)
				sonuc = true;

			st.close();
		} catch (SQLException e) {
			Helper.errorLogger(getClass(), e, "[SQL]..:" + sql.toString());

		} finally {
			dataSource.closeConnection(conn);
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
		}
		return sonuc;
	}
	@Override
	@Cacheable(value="wsToken",key="#token")
	public int[] tokenGecerliMiWithTime(String token) {
		int[] result = {0,0};
		
		StringBuilder 		sql 		= new StringBuilder();		
		PreparedStatement 	st 			= null;
		ResultSet 			rs 			= null;	
		Date				expireTime	= null;
		Connection			conn=null;
		try {
			sql.append("SELECT ");
			sql.append(" 	it.id , ");
			sql.append(" 	it.tarih ");
			sql.append("FROM token_tbl it ");
			sql.append("WHERE ");
			sql.append(" 	it.token = ? ");
			sql.append("	AND it.DURUM='AKTIF' ");
			sql.append(Constants.DB2_READ_ONLY_QUERY);
			
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			st.setString(1,token.trim());
			
			rs = st.executeQuery();			
			if (rs.next()){
				expireTime 	= new Date(rs.getTimestamp("tarih").getTime());
				expireTime  = Helper.dateAddMinute(expireTime,Constants.TOKEN_LIFE);
				result[0]	= 1;
				result[1]	= (int)Helper.dateDifferent(new Date(),expireTime,Calendar.SECOND);				
			}else{
				result[0]	= 0;
				result[1]	= 0;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			Helper.errorLogger(getClass(), e, "[SQL]..:" + sql.toString() + "[Token]..:" + token);
		} finally {
			dataSource.closeConnection(conn);
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
			
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
			
			
		}
		return result;
	}
	@Override
	public void extendTokenValidity(List<String> tokens) {
		StringBuilder sql = new StringBuilder();
		PreparedStatement 	st = null;
		Connection			conn=null;
		try{ 
			sql.append("UPDATE token_tbl SET tarih = CURRENT TIMESTAMP, durum='AKTIF' WHERE token=? ");
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			for(String token:tokens){
				st.setString(1,token);
				st.addBatch();
			}
			int sayi[] = st.executeBatch();
			st.close();
		}catch(SQLException e){
			Helper.errorLogger(getClass(), e,"[SQL]:" + sql.toString());
		}finally{
			dataSource.closeConnection(conn);
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					Helper.errorLogger(getClass(), e);
				}
		}
		
	}

}
