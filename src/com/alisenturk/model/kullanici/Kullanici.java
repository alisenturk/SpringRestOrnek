package com.alisenturk.model.kullanici;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alisenturk.model.base.HashFields;
import com.alisenturk.model.parametre.Departman;
import com.alisenturk.util.HashGenerator;
import com.alisenturk.util.Helper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class Kullanici implements Serializable {

	private static final long serialVersionUID = 4313648554965359458L;

	private int personelNo;
	private String miskod;
	private String ad;
	private String soyad;
	private String email;
	private Departman departman;
	private String kullaniciAdi;
	private List<Role> roles = new ArrayList<Role>();
	private String recType;
	private String valueHash;
	private String enlem;
	private String boylam;
	private String loginZamani;
	private boolean loggedIn;
	public Kullanici() {
		super();
	}

	public Kullanici(int personelNo, String ad, String soyad) {
		super();
		this.personelNo = personelNo;
		this.ad = ad;
		this.soyad = soyad;
		generateHashValue();
	}
	public int getPersonelNo() {
		return personelNo;
	}

	public void setPersonelNo(int personelNo) {
		this.personelNo = personelNo;
	}

	public String getMiskod() {
		return miskod;
	}

	public void setMiskod(String miskod) {
		this.miskod = miskod;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Departman getDepartman() {
		return departman;
	}

	public void setDepartman(Departman departman) {
		this.departman = departman;
	}

	public String getKullaniciAdi() {
		return Helper.checkNulls(kullaniciAdi,"").toUpperCase();
	}

	public void setKullaniciAdi(String kullaniciAdi) {
		this.kullaniciAdi = kullaniciAdi;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + personelNo;
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
		Kullanici other = (Kullanici) obj;
		if (personelNo != other.personelNo)
			return false;
		return true;
	}


	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	@JsonIgnore
	public String getRolesAsString() {
		StringBuilder userRoles = new StringBuilder();
		for (Role role : getRoles()) {
			userRoles.append("'" + role.getCode() + "',");
		}
		userRoles.append("'AGENT' ");
		return userRoles.toString();
	}

	public String getValueHash() {
		return valueHash;
	}

	public void setValueHash(String valueHash) {
		this.valueHash = valueHash;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public void generateHashValue() {
		HashGenerator<Kullanici> hashGenerator = new HashGenerator<>();
		valueHash = hashGenerator.generateHash(this, HashFields.KULLANICI.getHashFields());
	}

	public String getEnlem() {
		return enlem;
	}

	public void setEnlem(String enlem) {
		this.enlem = enlem;
	}

	public String getBoylam() {
		return boylam;
	}

	public void setBoylam(String boylam) {
		this.boylam = boylam;
	}

	public String getLoginZamani() {
		return loginZamani;
	}

	public void setLoginZamani(String loginZamani) {
		this.loginZamani = loginZamani;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}


}
