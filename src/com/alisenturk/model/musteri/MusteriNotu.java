package com.alisenturk.model.musteri;

import java.util.Date;

import com.alisenturk.model.base.BaseObject;
import com.alisenturk.model.base.Status;
import com.alisenturk.util.HashGenerator;


public class MusteriNotu implements BaseObject {
	
	public static final String NOT_TIP_ZIYARET = "ZİYARET NOTU";
	
	private static final long serialVersionUID = -8298934022338061675L;
	private static final String[] HASH_FIELDS = {"musteriNo","id"};
	
	private long	id;
	private long	musteriNo;
	private Date	notTarihi;
	private String	saat;
	private String	counter;
	private String	operatorId;
	private String	notTipKod;
	private String	iliskiTarihAciklamasi;
	private String	notAciklamasi;
	private String	notOperatorId;
	private Date	gecerlilikTarihi;
	private Status  status;
	private boolean updatable=false;
	private String			valueHash;
	
	public Date getNotTarihi() {
		return notTarihi;
	}
	public void setNotTarihi(Date notTarihi) {
		this.notTarihi = notTarihi;
	}
	public String getSaat() {
		return saat;
	}
	public void setSaat(String saat) {
		this.saat = saat;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getNotTipKod() {
		return notTipKod;
	}
	public void setNotTipKod(String notTipKod) {
		this.notTipKod = notTipKod;
	}
	public String getIliskiTarihAciklamasi() {
		return iliskiTarihAciklamasi;
	}
	public void setIliskiTarihAciklamasi(String iliskiTarihAciklamasi) {
		this.iliskiTarihAciklamasi = iliskiTarihAciklamasi;
	}
	public String getNotAciklamasi() {
		return notAciklamasi;
	}
	public void setNotAciklamasi(String notAciklamasi) {
		this.notAciklamasi = notAciklamasi;
	}
	public String getNotOperatorId() {
		return notOperatorId;
	}
	public void setNotOperatorId(String notOperatorId) {
		this.notOperatorId = notOperatorId;
	}
	public Date getGecerlilikTarihi() {
		return gecerlilikTarihi;
	}
	public void setGecerlilikTarihi(Date gecerlilikTarihi) {
		this.gecerlilikTarihi = gecerlilikTarihi;
	}
	@Override
	public String toString() {
		return "MusteriNotu [notTarihi=" + notTarihi + ", saat=" + saat
				+ ", counter=" + counter + ", operatorId=" + operatorId
				+ ", notTipKod=" + notTipKod + ", iliskiTarihAciklamasi="
				+ iliskiTarihAciklamasi + ", notAciklamasi=" + notAciklamasi
				+ ", notOperatorId=" + notOperatorId + ", gecerlilikTarihi="
				+ gecerlilikTarihi + "]";
	}
	public long getMusteriNo() {
		return musteriNo;
	}
	public void setMusteriNo(long musteriNo) {
		this.musteriNo = musteriNo;
	}
	/**
	 * @return the updatable
	 */
	public boolean isUpdatable() {
		return updatable;
	}
	/**
	 * @param updatable the updatable to set
	 */
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	public String getValueHash() {
		return valueHash;
	}
	public void setValueHash(String valueHash) {
		this.valueHash = valueHash;
	}
	public static String[] getHashFields() {
		return HASH_FIELDS;
	}
	@Override
	public String getObjectDetail() {
		StringBuilder info = new StringBuilder();
		if(musteriNo>0){
			info.append(musteriNo + " nolu müşteri için ");
		}
		return info.toString();
	}
	@Override
	public String getObjectKey() {
		HashGenerator<MusteriNotu> hashGenerator = new HashGenerator<>();
		return hashGenerator.generateTokenForData(this, HASH_FIELDS);
	}
	
	@Override
	public boolean isViewEveryone() {
		return true;
	}
	
	
}
