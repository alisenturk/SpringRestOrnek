package com.alisenturk.config;

public class Constants {
	 
	public static final ProdStage		PROD_STAGE 				= ProdStage.DEVELOPMENT;  //Sunuculara atarken değiştirilmeli
	public static final String 			DEFAULT_LANGUAGE 		= "TR"; 
	public static final String			DB2_READ_ONLY_QUERY 	= " FOR READ ONLY WITH UR ";
	public static final int 			TOKEN_LIFE				= 20; //token in gecerlilik omru, dk cinsinden
	public static final String 			TEMP_FILE_PATH			= "D:/temp/";
	public static final	boolean			OPEN_METHOD_PERFORMANS	= true;
	public static final boolean			ELASTIC_WRITE_UNSYNC	= true; 
	public static final String			APP_NAME				= "SpringRest";
	public static final ThreadGroup		THREAD_GROUP			= new ThreadGroup(APP_NAME + "_THREAD");
	public static final String			REQ_USER_PARAM_NAME		= "kullanici";
	
	
}
