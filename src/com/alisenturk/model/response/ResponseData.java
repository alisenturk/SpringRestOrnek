package com.alisenturk.model.response;

import java.io.Serializable;
import java.util.List;

public class ResponseData<T> implements Serializable{ 
	private static final long serialVersionUID = 8805336031799121824L;
	private String	statusCode;
	private String	statusMessage;
	private String	token;
	private T		data;
	private int		dataCount=-1;
	/**
	 * @return Yapılan isteğin durumuyla ilgili bilgi veren kod
	 */
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * @return Status kodun açıklaması
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	/**
	 * @return Her istekte gönderilmesi gereken ve kullanıcının login olduğunu gösteren kod
	 */
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * @return Başarılı işlem sonucunda geriye dönen data
	 */
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getDataCount() {
		if(dataCount==-1 && getData()!=null){
			if(getData() instanceof List){
				dataCount = ((List)getData()).size();
			}else{
				dataCount=1;
			}
		}
		
		return dataCount;
	}
	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	
}
