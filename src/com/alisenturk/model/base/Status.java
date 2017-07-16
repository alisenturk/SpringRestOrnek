package com.alisenturk.model.base;

public enum Status {

	AKTIF("AKTIF","Aktif"),
	PASIF("PASIF","Pasif"),
	SILINMIS("SILINMIS","Silinmiş");
	
	Status(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	private String	value;
	private String	label;
	
	public String getValue() {
		return value;
	}
	public String getLabel() {
		return label;
	}
	
	
	
}
