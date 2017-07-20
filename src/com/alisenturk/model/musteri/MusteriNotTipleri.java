package com.alisenturk.model.musteri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusteriNotTipleri implements Serializable{
	private String	notTipKodu;
	private List<MusteriNotu> musteriNotlari = new ArrayList<>();	
	public String getNotTipKodu() {
		return notTipKodu;
	}
	public void setNotTipKodu(String notTipKodu) {
		this.notTipKodu = notTipKodu;
	}
	public List<MusteriNotu> getMusteriNotlari() {
		return musteriNotlari;
	}
	public void setMusteriNotlari(List<MusteriNotu> musteriNotlari) {
		this.musteriNotlari = musteriNotlari;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((notTipKodu == null) ? 0 : notTipKodu.hashCode());
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
		MusteriNotTipleri other = (MusteriNotTipleri) obj;
		if (notTipKodu == null) {
			if (other.notTipKodu != null)
				return false;
		} else if (!notTipKodu.equals(other.notTipKodu))
			return false;
		return true;
	}
	
	
	
}
