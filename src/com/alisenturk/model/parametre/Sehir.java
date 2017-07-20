package com.alisenturk.model.parametre;

import java.io.Serializable;

public class Sehir implements Serializable{

	private int		id;
	private String	ad;
	private int		sira;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAd() {
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	public int getSira() {
		return sira;
	}
	public void setSira(int sira) {
		this.sira = sira;
	}
	
	
	
}
