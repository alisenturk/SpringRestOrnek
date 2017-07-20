package com.alisenturk.model.parametre;

import java.io.Serializable;

public class Departman implements Serializable {

	private static final long serialVersionUID = -3375587114285836484L;
	private String	departmanKodu;
	private String	departmanAdi;
	
	
	public Departman() {
		super();
	}
	
	public Departman(String departmanKodu, String departmanAdi) {
		super();
		this.departmanKodu = departmanKodu;
		this.departmanAdi = departmanAdi;
	}
	
	public String getDepartmanKodu() {
		return departmanKodu;
	}
	public void setDepartmanKodu(String departmanKodu) {
		this.departmanKodu = departmanKodu;
	}
	public String getDepartmanAdi() {
		return departmanAdi;
	}
	public void setDepartmanAdi(String departmanAdi) {
		this.departmanAdi = departmanAdi;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((departmanKodu == null) ? 0 : departmanKodu.hashCode());
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
		Departman other = (Departman) obj;
		if (departmanKodu == null) {
			if (other.departmanKodu != null)
				return false;
		} else if (!departmanKodu.equals(other.departmanKodu))
			return false;
		return true;
	}
	
	
}
