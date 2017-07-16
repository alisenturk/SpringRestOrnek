package com.alisenturk.util;

public enum DateFormats {
	
	YEAR_MONTH_DAY("yyyy-MM-dd"),
	YEAR_MONTH_DAY_HOUR_MINUTE("yyyy-MM-dd HH:mm"),
	YEAR_MONTH_DAY_HOUR_MINUTE_SECOND("yyyy-MM-dd HH:mm:ss"),
	YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_MILLISECOND("yyyy-MM-dd HH:mm:ss:SSS"),
	DAY_MONTH_YEAR("dd-MM-yyyy"),
	DAY_MONTH_YEAR_SLASH("dd/MM/yyyy"),
	DAY_MONTH_YEAR_HOUR_MINUTE("dd-MM-yyyy HH:mm"),
	DAY_MONTH_YEAR_HOUR_MINUTE_SECOND("dd-MM-yyyy HH:mm:ss"),
	DAY_MONTH_YEAR_HOUR_MINUTE_SECOND_MILLISECOND("dd-MM-yyyy HH:mm:ss:SSS");
	
	DateFormats(String value){
		this.value = value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}
	
	static public boolean isValid(String format) {
		DateFormats[] tarihFormatlari = DateFormats.values();
	       for (DateFormats t : tarihFormatlari)
	           if (t.getValue().equals(format))
	               return true;
	       	return false;
	}
	
}
