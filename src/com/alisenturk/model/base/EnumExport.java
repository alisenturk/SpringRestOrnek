package com.alisenturk.model.base;

import java.io.Serializable;

public class EnumExport implements Serializable {
	
	private static final long serialVersionUID = -3476833151038199800L;
	private String 	code;
	private String	label;
		
	public EnumExport() {
		super();
	}
	
	public EnumExport(String code, String label) {
		super();
		this.code = code;
		this.label = label;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
