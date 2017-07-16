package com.alisenturk.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.security.util.FieldUtils;

public class HashGenerator<T> {
	
	public String generateHash(T t,String[] fields){
		return generateHash(t, fields, null);
	}
	public String generateHash(T t,String[] fields,String extraValue){
		StringBuilder hashVal 	= new StringBuilder();
		
		List<String> listFields = Arrays.asList(fields);
		Class<? extends Object> clzz = t.getClass();
		boolean todayAdded=false;
		for(Field field:clzz.getDeclaredFields()){			
			if(listFields.contains(field.getName())){
				hashVal.append(readFieldValue(t, field.getName()) + "#");				
			}else if(!todayAdded && listFields.contains("today")){
				todayAdded=true;
				hashVal.append(Helper.date2String(new Date()) + "#");
			}
		}
		if(extraValue!=null){
			hashVal.append(extraValue+"#");
		}
		
		String strHash = "";
		if(hashVal.toString().length()>1){		
			strHash = Helper.generateSha256Hash(hashVal.toString());
		}
		
		return strHash;
	}
	
	
	private static String readFieldValue(Object obj,String fieldName){
		try{
			Object pd =null;
			if(fieldName.equalsIgnoreCase("serialVersionUID")){
				pd = FieldUtils.getFieldValue(obj,fieldName);			
			}else{
				pd = new PropertyDescriptor(fieldName,obj.getClass()).getReadMethod().invoke(obj);
			}
			if(pd!=null){
				return String.valueOf(pd).trim();
			}else{
				return "";
			}
		}catch(RuntimeException e){
			Helper.errorLogger(HashGenerator.class, e);
		}catch(Exception e){
			Helper.errorLogger(HashGenerator.class, e);
		}
		return "";
	}
	public String generateTokenForData(T t,String[] fields){
		StringBuilder hashVal 	= new StringBuilder();
		hashVal.append("{");
		List<String> listFields = Arrays.asList(fields);
		Class<? extends Object> clzz = t.getClass();
		boolean todayAdded = false;
		for(Field field:clzz.getDeclaredFields()){
			
			if(listFields.contains(field.getName())){
				hashVal.append("\""+field.getName()+"\":\""+ readFieldValue(t, field.getName()) + "\",");
			}else if(!todayAdded && listFields.contains("today")){
				todayAdded=true;
				hashVal.append("\"today\":\""+ Helper.date2String(new Date()) + "\",");				
			}
		}
				
		String strHash = "";
		if(hashVal.toString().length()>3){			
			strHash = hashVal.toString().substring(0,hashVal.toString().length()-1) + "}";
		}		
		strHash = Helper.generateKeyForData(strHash,false);
		return strHash;
	}
}
