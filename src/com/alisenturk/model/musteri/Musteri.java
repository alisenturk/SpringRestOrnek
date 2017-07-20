package com.alisenturk.model.musteri;

import com.alisenturk.model.base.BaseObject;
import com.alisenturk.model.base.HashFields;
import com.alisenturk.util.HashGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Musteri  implements BaseObject {
	
	private static final long serialVersionUID = 7880427403604890862L;

	private long			musteriNo;
	private String			ad;
	private String			soyad;
	private String			tcKimlikNo;
	


	@Override
	public String getObjectKey() {
		HashGenerator<Musteri> hashGenerator = new HashGenerator<>();
		return hashGenerator.generateTokenForData(this,HashFields.MUSTERI.getHashFields());
	}


	@Override
	public boolean isViewEveryone() {
		return true;
	}


	@Override
	public String getObjectDetail() {
		return "";
	}


	public long getMusteriNo() {
		return musteriNo;
	}


	public void setMusteriNo(long musteriNo) {
		this.musteriNo = musteriNo;
	}


	public String getAd() {
		return ad;
	}


	public void setAd(String ad) {
		this.ad = ad;
	}


	public String getSoyad() {
		return soyad;
	}


	public void setSoyad(String soyad) {
		this.soyad = soyad;
	}


	public String getTcKimlikNo() {
		return tcKimlikNo;
	}


	public void setTcKimlikNo(String tcKimlikNo) {
		this.tcKimlikNo = tcKimlikNo;
	}

	

}
