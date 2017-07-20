package com.alisenturk.controller.musteri;

import com.alisenturk.model.base.BaseObject;
import com.alisenturk.model.kullanici.Kullanici;


public class MusteriAramaKriterleri implements BaseObject {
	
	private static final long serialVersionUID = -7037052927674504400L;
	
	private long		musteriNo;
	private String		tckimlikNo;
	private String		cepTelefonu;
	private String		dovizTuru = "TL";
	private int			detayGetir = 0;
	private int			sadeceAktif = 0;
	private int			sonKayit 	= 0; 
	private int			subeKodu  	= 0;
	private int 		gun			= 0;
	private String		objectKey	= "";
	private Kullanici	kullanici;
	
	public Kullanici getKullanici() {
		return kullanici;
	}
	public void setKullanici(Kullanici kullanici) {
		this.kullanici = kullanici;
	}
	public long getMusteriNo() {
		return musteriNo;
	}
	public void setMusteriNo(long musteriNo) {
		this.musteriNo = musteriNo;
	}
	public String getTckimlikNo() {
		return tckimlikNo;
	}
	public void setTckimlikNo(String tckimlikNo) {
		this.tckimlikNo = tckimlikNo;
	}
	public String getCepTelefonu() {
		return cepTelefonu;
	}
	public void setCepTelefonu(String cepTelefonu) {
		this.cepTelefonu = cepTelefonu;
	}
	
	public String getDovizTuru() {
		return dovizTuru;
	}
	public void setDovizTuru(String dovizTuru) {
		this.dovizTuru = dovizTuru;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cepTelefonu == null) ? 0 : cepTelefonu.hashCode());
		result = prime * result + detayGetir;
		result = prime * result + ((dovizTuru == null) ? 0 : dovizTuru.hashCode());
		result = prime * result + gun;
		result = prime * result + (int) (musteriNo ^ (musteriNo >>> 32));
		result = prime * result + sonKayit;
		result = prime * result + subeKodu;
		result = prime * result + ((tckimlikNo == null) ? 0 : tckimlikNo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusteriAramaKriterleri other = (MusteriAramaKriterleri) obj;
		if (cepTelefonu == null) {
			if (other.cepTelefonu != null)
				return false;
		} else if (!cepTelefonu.equals(other.cepTelefonu))
			return false;
		if (detayGetir != other.detayGetir)
			return false;
		if (dovizTuru == null) {
			if (other.dovizTuru != null)
				return false;
		} else if (!dovizTuru.equals(other.dovizTuru))
			return false;
		if (gun != other.gun)
			return false;
		if (musteriNo != other.musteriNo)
			return false;
		if (sonKayit != other.sonKayit)
			return false;
		if (subeKodu != other.subeKodu)
			return false;
		if (tckimlikNo == null) {
			if (other.tckimlikNo != null)
				return false;
		} else if (!tckimlikNo.equals(other.tckimlikNo))
			return false;
		return true;
	}
	public int getDetayGetir() {
		return detayGetir;
	}
	public void setDetayGetir(int detayGetir) {
		this.detayGetir = detayGetir;
	}
	@Override
	public String getObjectDetail() {
		StringBuilder info = new StringBuilder();
		if(musteriNo>0){
			info.append(musteriNo + " nolu");
		}
		if(tckimlikNo!=null && tckimlikNo.length()==11){
			info.append(tckimlikNo + " kimlik nolu ");
		}
		if(cepTelefonu!=null && cepTelefonu.length()>5){
			info.append(cepTelefonu + " cep telefon nolu");
		}
		if(detayGetir==0){
			 info.append(" müşteri sorgulanmıştır.");			 
		}else{
			 info.append(" müşteri detayı görüntülenmiştir.");
		}
		
		return info.toString();
	}
	
	@Override
	public boolean isViewEveryone() {
		return true;
	}
	public int getSadeceAktif() {
		return sadeceAktif;
	}
	public void setSadeceAktif(int sadeceAktif) {
		this.sadeceAktif = sadeceAktif;
	}
	/**
	 * Gösterilecek kayitlarin sayisini veya tarih bazlı ne kadar önceki kayıtların gösterileceğini belirtir.
	 * Örneğin son 90 gündeki kayıtlar gibi
	 * @return
	 */
	public int getSonKayit() {
		return sonKayit;
	}
	public void setSonKayit(int sonKayit) {
		this.sonKayit = sonKayit;
	}
	public int getSubeKodu() {
		return subeKodu;
	}
	public void setSubeKodu(int subeKodu) {
		this.subeKodu = subeKodu;
	}
	public int getGun() {
		return gun;
	}
	public void setGun(int gun) {
		this.gun = gun;
	}
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	
	
}
