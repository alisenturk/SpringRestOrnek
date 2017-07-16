package com.alisenturk.config;

public enum ProdStage {
	PRODUCTION("Prod"),
	TEST("Test"),
	DEVELOPMENT("Dev");
	
	private ProdStage(String value) {
		this.value 		= value;
	}

	private String 		value;
	
	public String getValue() {
		return value;
	}

	

}
