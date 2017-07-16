package com.alisenturk.model.base;

import java.io.Serializable;

public class LoginData implements Serializable {
	
	private static final long serialVersionUID = 5426629155933675236L;
	
	private String 	username;
	private String 	password;
	private String	latitude;
	private String	longitude;
	
	public LoginData() {
		super();
	}
	public LoginData(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public boolean isEmpty(){
		if(null==username || null==password || 
		   username.length()<3 || password.length()<3){
			return true;
		}else{
			return false;
		}
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	
}
