package com.alisenturk.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;


import com.alisenturk.config.Constants;
import com.alisenturk.config.ProdStage;
import com.alisenturk.exceptions.HBRuntimeException;
import com.google.common.io.Files;
import com.google.gson.Gson;

import crypto.TripleDes;
import sun.net.www.http.HttpClient;

public class Helper {
			
	private static final String PROJECT_CONFIG_BUNDLE_NAME 	= "com.alisenturk.resources.ProjectConfig";
	private static final String PROJECT_MESSAGE_BUNDLE_NAME 	= "com.alisenturk.resources.Imece";
	
	private Helper(){
	}
	
	public static String getAppMessage(String messageKey) {
		String msgValue = messageKey;
        try {
            
            String key = PROJECT_CONFIG_BUNDLE_NAME + Constants.PROD_STAGE.getValue();            
            ResourceBundle message = ResourceBundle.getBundle(key,new Locale("tr","TR"));
            msgValue = Helper.checkNulls(message.getString(messageKey),"").trim();
            
            return msgValue;
        }catch(HBRuntimeException e) {
        	Helper.errorLogger(Helper.class, e);
        }finally{
        	msgValue = Helper.checkNulls(msgValue, messageKey);
        }
        return msgValue;
    }
	
	public static String getMessage(String lang,String messageKey) {
		String msgValue = messageKey;
        try {
            
            String key = PROJECT_MESSAGE_BUNDLE_NAME;             
            ResourceBundle message = ResourceBundle.getBundle(key,new Locale(lang.toLowerCase(),lang.toUpperCase()));
            msgValue = Helper.checkNulls(message.getString(messageKey),"").trim();
            
            return msgValue;
        }catch(HBRuntimeException e) {
        	Helper.errorLogger(Helper.class, e);
        }finally{
        	msgValue = Helper.checkNulls(msgValue, messageKey);
        }
        return msgValue;
    }
	
	public static String request2String(HttpServletRequest request,String param){
		String val = "";
		if(request!=null && request.getParameter(param)!=null){
			val = request.getParameter(param);
		}else if(request!=null && request.getAttribute(param)!=null){
			val = (String)request.getAttribute(param);
		}
		return val;
	}
	public static int request2Int(HttpServletRequest request,String param){
		int val = 0;
		try{
			if(request!=null && request.getParameter(param)!=null){
				val = Integer.parseInt(request.getParameter(param));
			}else if(request!=null && request.getAttribute(param)!=null){
				val = ((Integer)request.getAttribute(param)).intValue();
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:" + param);
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e,"[param]:" + param);
		}
		return val;
	}
	public static double request2Double(HttpServletRequest request,String param){
		double val = 0;
		try{
			if(request!=null && request.getParameter(param)!=null){
				val = Double.valueOf(request.getParameter(param)).doubleValue();
			}else if(request!=null && request.getAttribute(param)!=null){
				val = ((Double)request.getAttribute(param)).doubleValue();
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:" + param);
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e,"[param]:" + param);
		}
		return val;
	}
	public static float request2Float(HttpServletRequest request,String param){
		float val = 0;
		try{
			if(request!=null && request.getParameter(param)!=null){
				val = Float.valueOf(request.getParameter(param)).floatValue();
			}else if(request!=null && request.getAttribute(param)!=null){
				val = ((Float)request.getAttribute(param)).floatValue();
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:" + param);
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e,"[param]:" + param);
		}
		return val;
	}
	
	public static Date request2Date(HttpServletRequest request,String param){		
		return request2Date(request, param,"dd/MM/yyyy");
	}
	public static Date request2Date(HttpServletRequest request,String param,String pformat){
		Date val = null;
		String format = pformat;
		try{
			if(format==null || format.length()<4){
				format = "dd/MM/yyyy";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if(request!=null && request.getParameter(param)!=null){
				String strDate = request.getParameter(param);
				val = sdf.parse(strDate);
			}else if(request!=null && request.getAttribute(param)!=null){
				String strDate = (String)request.getAttribute(param);
				val = sdf.parse(strDate);
			}
		}catch (RuntimeException | ParseException e) {
			Helper.errorLogger(Helper.class, e,"[param]:" + param + "[format]:"+format);
		}
		return val;
	}
	
	public static String date2String(Date date) {
		return date2String(date,"dd/MM/yyyy");
	}
	public static String date2String(long date,String format) {
		return date2String(new Date(date), format);
	}
	public static String date2String(Date date,String format) {
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	public static String getServerIPAddress(){
		String ipAddress = Helper.getAppMessage("serverIP");
		try{
			InetAddress ip;
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		
		return ipAddress;
	}
	public static void errorLogger(Class className,Exception e){
		ErrorHandler eh = new ErrorHandler(className,e);
		eh.setLogtype("error");
		eh.setServerName(getServerIPAddress());
		eh.logwrite();		
	}
	public static void errorLogger(Class className,Exception e,String extraInfo){
		ErrorHandler eh = new ErrorHandler(className,e);
		eh.setLogtype("error");
		eh.setServerName(getServerIPAddress());
		eh.setExtraInfo(extraInfo);
		eh.logwrite();
	}
	public static Locale getLocale(String planguage){
		String firstLang 	= "tr";
		String secondLang 	= "TR";
		String language		= planguage;
		
		if(language==null || "".equals(language)){ 
			language	= Constants.DEFAULT_LANGUAGE;
		}
		if("UK".equalsIgnoreCase(language)){
			language = "UA";
		}
		language = language.toUpperCase(Locale.ENGLISH);
		
		if (language!=null && "EN".equals(language)){
			firstLang 	= "en";
			secondLang 	= "US";								
		}else if(language!=null && "IR".equals(language)){
			language 	= "IR";
			firstLang 	= "ir";
			secondLang 	= "IR";		
		}else if(language!=null && "UA".equals(language)){
			language 	= "UA";
			firstLang 	= "uk";
			secondLang 	= "UA";		
		}else if(language!=null && "RU".equals(language)){
			language 	= "RU";
			firstLang 	= "ru";
			secondLang 	= "RU";		
		}else{
			language 	= "TR"; 
			firstLang 	= "tr";
			secondLang 	= "TR";
		}
		return new Locale(firstLang,secondLang);
	}
		
	public static String checkNulls(Object value, String newVal) {
		return checkNulls(value, newVal,true);
	}
	public static String checkNulls(Object value, String newVal,boolean isTrim) {
		try {
			if (value == null || "null".equals(value))
				return newVal;
			else {
				String str = String.valueOf(value);
				if(isTrim)
					str = str.trim();
				
				if(str.length()<1)
					return newVal;
				else
					return str;	
			}
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return "";
	}
	public static Date dateAddYear(Date date, int year) {
		Date newdate = date;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, year);
			newdate = cal.getTime();

		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return newdate;
	}
	public static Date dateAddMonth(Date date, int month) {
		Date newdate = date;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, month);
			newdate = cal.getTime();

		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return newdate;
	}

	public static Date dateAdd(Date date, int day) {
		Date newdate = date;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, day);
			newdate = cal.getTime();

		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return newdate;
	}
	
	public static Date string2Date(String strDate) {
		return string2Date(strDate,"dd/MM/yyyy");
	}
	public static Date string2Date(String strDate,String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(strDate);
		} catch (Exception e) {
			if(format.indexOf("-")>=3){
				return string2Date(strDate,"yyyy-MM-ddd");
			}else{
				Helper.errorLogger(Helper.class, e, " strDate..:" + strDate + " format..:" + format);
			}
		}
		return null;
	}
	
	public static double roundDecimal(double price) {
		DecimalFormat twoDigits = new DecimalFormat("0.0000",DecimalFormatSymbols.getInstance(new Locale("EN")));
		return BigDecimal.valueOf(Double.valueOf(twoDigits.format(price))).doubleValue();
	}
	public static double roundDouble(double input){
		return Math.round(input * Math.pow(10, (double) 2.0)) / Math.pow(10, (double) 2.0);	
    }
	public static double dateDifferent(Date pfrom, Date pto, int differentType) {
		
		Date from	= pfrom; 
		Date to		= pto;
		
		if (from == null) {
			from = new Date();
		}
		if (to == null) {
			to = new Date();
		}

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(from);
		calendar2.setTime(to);
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		double diff = milliseconds2 - milliseconds1;
		 
		double left = 0;
		if (differentType == Calendar.SECOND) {
			left = (diff / 1000L);
		} else if (differentType == Calendar.MINUTE) {
			left = (diff / (60L * 1000L));
		} else if (differentType == Calendar.HOUR) {
			left = (diff / (60L * 60L * 1000L));
		} else if (differentType == Calendar.DATE) {
			left = Helper.roundDecimal(diff / (24d * 60d * 60d * 1000d));
		}else if (differentType == Calendar.MILLISECOND) {
			left = diff;
		}else if (differentType == Calendar.YEAR) {
			left = Helper.roundDouble(diff / (365d * 24d * 60d * 60d * 1000d));
		}
		
		
		return left;
	}
	
	public static boolean isEmpty(String str){
		if(str==null || str.trim().length()==0)
			return true;
		else
			return false;	
	}
	
	public static String generateMD5(String value){
		StringBuilder sb = new StringBuilder();
    	try{ 
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(value.getBytes("UTF-8"));
	 
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format method 1	        
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
    	}catch (RuntimeException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
    		Helper.errorLogger(Helper.class, e);
		}
        return sb.toString();
    }
	
	public static void addCookie(HttpServletResponse response,String name,String value,String path,int day,boolean isSecure){
		try{
			
			addSecureCookie(response, name, value,isSecure,path,day);
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
	}
	public static void addCookie(HttpServletResponse response,String name,String value,String path,int day){
		try{
			
			addSecureCookie(response, name, value,false,path,day);
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
	}
	
	public static void addCookie(HttpServletResponse response,String name,String value){
		addCookie(response, name, value,"/",14);
	}
	public static void addCookie(HttpServletResponse response,String name,String value,boolean isSecure){
		if(isSecure){
			addSecureCookie(response, name, value, isSecure);
		}else{
			addCookie(response, name, value,"/",14);
		}
	}
	public static void addSecureCookie(HttpServletResponse response,String name,String value,boolean isSecure,String path,double day){
		Cookie cookie ;
		try{
			cookie = new Cookie(name,"");
			int expiry = (int)(60*60*24*day);

			cookie = new Cookie(name,value);
			cookie.setComment("Halkbank Portal");
			/** (days * 24 * 60 * 60 * 1000)); **/
			cookie.setMaxAge(expiry);			
			cookie.setSecure(isSecure);
			cookie.setPath(path);
					
			response.addCookie(cookie);
			
		}catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
	}
	public static void addSecureCookie(HttpServletResponse response,String name,String value,boolean isSecure){
		addSecureCookie(response, name, value, isSecure,"/",14);
	}
	public static void eraseCookie(HttpServletRequest req, HttpServletResponse resp,String cookieName) {
	    Cookie[] cookies = req.getCookies();
	    if (cookies != null){
	        for (int i = 0; i < cookies.length; i++) {
	        	
	        	if(cookies[i].getName().trim().equalsIgnoreCase(cookieName)){
		            cookies[i].setValue(null);
		            cookies[i].setPath("/");
		            cookies[i].setMaxAge(0);
		            resp.addCookie(cookies[i]);
	        	}
	        }
	    }
	}
	public static String getCookieValue(HttpServletRequest request,String name){
		String value=null;
	
		Cookie cookie[] = request.getCookies();		
		if (cookie != null && cookie.length > 0) {
			value=null;			
			for (int i = 0; i < cookie.length; i++) {
				
				if(cookie[i].getName().trim().equalsIgnoreCase(name)){
					value = cookie[i].getValue();
				}
				if(value!=null)
					break;
			}
		}
		return value;
	}
	
	public static Object loadObjectFromCookie(HttpServletRequest request,String cookieName, Class<?> target){
    	try{
    		
    		String jsonCookie = Helper.getCookieValue(request,cookieName);    		
    		
    		if(jsonCookie!=null && jsonCookie.trim().length()>10){
    			jsonCookie = jsonCookie.replace("@",",");
    			Gson gson = new Gson();
    			Object obj = gson.fromJson(jsonCookie,target);
    			
    			return obj;
    		}
    		
    	}catch (Exception e) {
    		Helper.errorLogger(Helper.class, e);
		}
    	return null;
    } 
	public static String removeForbiddenChar(String pstr) {
		String str = pstr;
    	str = Helper.checkNulls(str,"");
    	str = str.replace("\\", "");
    	str = str.replace("\"", "");
    	str = str.replace("'", "");
    	str = str.replace("`", "");
    	str = str.replace("]", "");
    	str = str.replace("[", "");
    	str = str.replace("*", "");
    	str = str.replace(",", "");
    	str = str.replace("?", "");
    	str = str.replace("&", "");
    	str = str.replace("!", "");
    	str = str.replace("#", "");
    	str = str.replace("~", "");
    	str = str.replace("/", "");
    	str = str.replace("(", "");
    	str = str.replace(")", "");    	
    	str = str.replace(" ", "");		
		return str.trim();
	}
	@SuppressWarnings("unused")
	public static java.sql.Date convert2SQLDate(Date date){
		if(date!=null)
			return new java.sql.Date(date.getTime());
		else
			return null;
	}
	public static String fileDeleteFromDisk(String path){
		String result = "OK";
		boolean isDeleted = false;
		try{
			File file = new File(path);
			if(file.exists() && file.isFile()){
				isDeleted = file.delete();
			}
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
			result = e.getMessage();
		}
		return result;
	}
	public static String fileWrite2Disk(String path,String fileName,File file){
		String result = "OK";
		BufferedOutputStream stream = null;
		String newFilePath = "";
		boolean created = false;
		try{
			File dir = new File(path);
			if(!dir.exists()){
				created = dir.mkdirs();
			}
			fileName = removeForbiddenChar(fileName).toLowerCase(Locale.ENGLISH); 
			newFilePath = path + "/" + fileName;
			File newFile = new File(newFilePath);
            stream = new BufferedOutputStream(new FileOutputStream(newFile));
            stream.write(Files.toByteArray(file));
            stream.close();

		}catch(RuntimeException | IOException e){
			Helper.errorLogger(Helper.class, e);
			result = e.getMessage();
		}finally{
			if(stream!=null)
				try {
					stream.close();
				} catch (IOException e) {
					Helper.errorLogger(Helper.class, e);
				}
		}
		return result;
	}
	
	public static String readHtmlPage(String urlPath) {
        StringBuilder ticket = new StringBuilder();
        BufferedReader br = null;
        try {
                        
            URL url = new URL(urlPath);            
            HttpURLConnection con = (HttpURLConnection)url.openConnection();            
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));            
            String input;
            while ((input = br.readLine()) != null) {
                ticket.append(input);
            }
            br.close();
        }catch(RuntimeException | IOException e){
			Helper.errorLogger(Helper.class, e);
		}finally{
        	if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					Helper.errorLogger(Helper.class, e);
				}
        }
        return ticket.toString();
    }
	
	public static <T extends Enum<T>> String getEnumFieldValue(T selectedOption,String fieldName) {
		String fieldVal = "";
		if(selectedOption!=null){
			Field field;
			try {
				field = selectedOption.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				fieldVal = (String)field.get(selectedOption);
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				Helper.errorLogger(Helper.class, e);
			}
		}
		return fieldVal;
			
	}
	
	public static double bigDecimal2double(BigDecimal bdVal) {
		double val = 0d;
		if(bdVal!=null){
			val = bdVal.doubleValue();
		}
		return val;
			
	}
	
	public static String request2String(Map<String,Object> reqParam,String param){
		String val = "";
		if(reqParam!=null && reqParam.get(param)!=null){
			val = Helper.checkNulls(reqParam.get(param),"");
		}
		return val;
	}
	public static int request2Int(Map<String,Object> reqParam,String param){
		int val = 0;
		try{
			if(reqParam!=null && reqParam.get(param)!=null){
				val = Integer.parseInt((String)reqParam.get(param));
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:"+param);
		}
		return val;
	}
	public static double request2Double(Map<String,Object> reqParam,String param){
		double val = 0;
		try{
			if(reqParam!=null && reqParam.get(param)!=null){
				val = Double.valueOf((String)reqParam.get(param)).doubleValue();
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:"+param);
		}
		return val;
	}
	public static float request2Float(Map<String,Object> reqParam,String param){
		float val = 0;
		try{
			if(reqParam!=null && reqParam.get(param)!=null){
				val = Float.valueOf((String)reqParam.get(param)).floatValue();
			}
		}catch(NumberFormatException nfe){
			Helper.errorLogger(Helper.class, nfe,"[param]:"+param);
		}
		return val;
	}
	
	public static Date request2Date(Map<String,Object> reqParam,String param){		
		return request2Date(reqParam, param,"dd/MM/yyyy");
	}
	public static Date request2Date(Map<String,Object> reqParam,String param,String format){
		Date val = null;
		try{
			if(format==null || format.length()<4){
				format = "dd/MM/yyyy";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if(reqParam!=null && reqParam.get(param)!=null){
				String strDate = (String)reqParam.get(param);
				val = sdf.parse(strDate);
			}
		}catch (RuntimeException | ParseException e) {
			Helper.errorLogger(Helper.class, e,"[param]:"+param);
		}
		return val;
	}
	
	public static String repeatString(String str,int count){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<count;i++){
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String decryptWith3Des(String encryptData,String key,String iv){
		String decryptData = encryptData;
		try{		
						
			TripleDes tripleDes = new TripleDes();			
			decryptData = tripleDes.decryptWith3Des(encryptData,key,iv);
			
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e,"[decryptData]:"+decryptData);
		}		
		return decryptData;
	}
	
	
	public static long string2Long(String pval){
		String val = pval;
		val = Helper.checkNulls(val, "0").trim();
		try{
			return Long.parseLong(val);
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e,"[pval]:"+pval);
		}		
		return 0;
	}
	
	public static boolean checkNumeric(String value){
		boolean logic = true;
		try {
			Long.parseLong(value.trim());
		} catch (NumberFormatException | NullPointerException e) {
			logic = false;
		}
		return logic;
	}
	
	public static java.sql.Timestamp date2Timestamp(Date pdate){
		Date date = pdate;
		if(date==null)date = new Date();
		
		return new java.sql.Timestamp(date.getTime());
	}
	public static void log2File(String fileName,String content){
		log2File(fileName, content, true);
	}
	public static void log2File(String fileName,String content,boolean isTrim){
		log2File(fileName, content,false, isTrim);
	}	
	public static void log2File(String fileName,String pcontent,boolean isAppend,boolean isTrim){
		String content= pcontent;
		FileWriter fw = null;
		try{
			fw = new FileWriter(new File("/tmp/logfiles/"+fileName),isAppend);			
			if(isTrim){
				content = content.trim();
			}
			fw.write(content);
			fw.close();
		}catch(RuntimeException | IOException e){
			Helper.errorLogger(Helper.class, e);
		}finally{
			if(fw!=null)
				try {
					fw.close();
				} catch (IOException e) {
					Helper.errorLogger(Helper.class, e);
				}
		}
	}
	
	public static Clob string2Clob(Connection conn,String data){
		Clob clob = null;
		try {
			clob = conn.createClob();
			int nocw= clob.setString((long)1,data);
		}catch(Exception e) {
			Helper.errorLogger(Helper.class, e);
		}	        
        
        return clob;

	}
	
	
	public static String validationStringLen(String pstr,int len){
		String str = pstr;
		str = Helper.checkNulls(str,"");
		if(str.length()>len){
			return str.substring(0,len);
		}else{
			return str;
		}
	}
	
	public static String appendStringtoString(String value, String appendedValue, int count){
		StringBuilder sb  = new StringBuilder();
		sb.append(value);
		for (int i = 0; i < count; i++) {
			sb.append(appendedValue);
		}
		return sb.toString();
	}
	public static double string2Double(String val){
		try{
			NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.GERMANY);
			return nf_in.parse(val).doubleValue();
		}catch(RuntimeException | ParseException e){
			return 0;
		}
	}
	
	public static int hex2decimal(String ps) {
		String s = ps;
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase(Locale.ENGLISH);
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }


    // precondition:  d is a nonnegative integer
    public static String decimal2hex(int pd) {
    	int d = pd;
        String digits = "0123456789ABCDEF";
        if (d == 0) return "0";
        String hex = "";
        while (d > 0) {
            int digit = d % 16;                // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / 16;
        }
        return hex;
    }
    
    public static Date dateAddMinute(Date date, int minute) {
		Date newdate = date;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minute);
			newdate = cal.getTime();

		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return newdate;
	}
    
    public static String getEncryptData(String data,String key){
        String strResult = "";
        AESCrypto aes = new AESCrypto(key);
        try {
            strResult = URLEncoder.encode(aes.encrypt(data.trim()),"UTF-8");
        } catch (UnsupportedEncodingException ex) {
           Helper.errorLogger(Helper.class, ex);
        }
        
        return strResult; 
    }
    
    public static String getDecryptData(String data,String key){
        return getDecryptData(data, key, true);
    }
    public static String getDecryptData(String strToDecrypt,String key,boolean urlEncoded){
        String strResult = "";
        AESCrypto aes = new AESCrypto(key);
        try {
            if(urlEncoded){
                strToDecrypt = URLDecoder.decode(strToDecrypt, "UTF-8");
            }
            strResult = aes.decrypt(strToDecrypt.trim());
        } catch (UnsupportedEncodingException ex) {
            Helper.errorLogger(Helper.class, ex);
        }
        
        
        return strResult; 
    }
	
	public static String imageToBase64String(File imageFile){
	
	    String image=null;
	    try{
	    	String fileType = java.nio.file.Files.probeContentType(imageFile.toPath());
	    	if(fileType!=null && !fileType.startsWith("image")){
	    		throw new HBRuntimeException("Hatalı dosya tipi!");
	    	}
	    	if(fileType==null)
	    		return image;
	    	
		    BufferedImage buffImage = ImageIO.read(imageFile);
		
		    if(buffImage != null) {
		    	image = "";
		    	java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
		        ImageIO.write(buffImage, "jpg", os);
		        byte[] data = os.toByteArray();
		        image = HBBase64.encode(data);
		
			
		    }//if
	    }catch(Exception e){
	    	Helper.errorLogger(Helper.class, e);
	    }
		return image;
	}
	
	public static String maskCreditCardNo(String cardNo){
		String mask = Helper.checkNulls(cardNo,"");
		if(cardNo.length()>14){
			mask = cardNo.substring(0,4) + "-" + cardNo.substring(4,6)+"XX-"+"XXXX-" + cardNo.substring(cardNo.length()-4,cardNo.length());
		}
		return mask;
	}
	
	public static int getWeekOfYear(Date date){
		int thisWeek = -1;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			 thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		return thisWeek;
	}
	public static int getMonthOfDate(Date date){
		int thisWeek = -1;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			 thisWeek = calendar.get(Calendar.MONTH)+1;
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		return thisWeek;
	}
	public static int getYearOfDate(Date date){
		int thisWeek = -1;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			 thisWeek = calendar.get(Calendar.YEAR);
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		return thisWeek;
	}
	public static String double2String(double dbl){
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en","US"));
		symbols.setDecimalSeparator('.');
		String pattern = "##0";
		DecimalFormat df = new DecimalFormat(pattern,symbols);
		String number = df.format(dbl);
		return number;
	}
	public static String getLinkShortly(String url){
		return url;
	}
	
	public static int getRandomCode(){
		int rand = ThreadLocalRandom.current().nextInt(100000,999999);
		return rand;
	}
	
	public static boolean checkValidCoordinate(String coordinate){
		boolean isValid = false;
		try{
			if(coordinate!=null){
		        String regex_coords = "([+-]?\\d+\\.?\\d+)\\s*";
		        Pattern compiledPattern2 = Pattern.compile(regex_coords, Pattern.CASE_INSENSITIVE);
		        Matcher matcher2 = compiledPattern2.matcher(coordinate);
		        while (matcher2.find()) {
		            isValid = true;
		        }
			}
			
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		
		return isValid;
	}
	
	public static String generateSha256Hash(String data){
		String newHash = "";
		try{
			 MessageDigest digest = MessageDigest.getInstance("SHA-256");
	         byte[] byteData = digest.digest(data.getBytes("UTF-8"));

	        newHash = DatatypeConverter.printHexBinary(byteData);
			
		}catch(HBRuntimeException | NoSuchAlgorithmException | UnsupportedEncodingException e){
			Helper.errorLogger(Helper.class, e);
		}
		
		return newHash;
	}
	
	public static String date2DB2Timestamp(Date p_date,int addSecond){
		StringBuilder result = new StringBuilder();
		Date date = p_date;
		if(date==null){
			date = new Date();
		}
		try{
			result.append("( TO_DATE('" + Helper.date2String(date, "dd/MM/yyyy HH:mm:ss") + "','DD/MM/YYYY HH24:MI:SS') ") ;
			if(addSecond>0){
				result.append(" + " + addSecond + " SECOND "); 
			}
			result.append(")");
		}catch(Exception e){
			Helper.errorLogger(Helper.class, e);
		}
		return result.toString();
	}
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * hafta başlangıç tarihini döner.
	 * @param format tarih formati
	 * @param date özel tarih
	 * @return
	 */
	public static String getStartOfWeekDate(String format, Date date){
		String startOfWeekDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(date);
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
			startOfWeekDate = new SimpleDateFormat(format).format(calendar.getTime());
			calendar = null;
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return startOfWeekDate;
	}
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * hafta bitiş tarihini döner.
	 * @param format tarih formati
	 * @param date özel tarih
	 * @return
	 */
	public static String getEndOfWeekDate(String format, Date date){
		String endOfWeekDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(date);
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
			calendar.add(Calendar.DATE, 6);
			endOfWeekDate = new SimpleDateFormat(format).format(calendar.getTime());
			calendar = null;
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return endOfWeekDate;
	}
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * ay başlangıç tarihini döner.
	 * @param format tarih formati
	 * @param date özel tarih
	 * @return
	 */
	public static String getStartOfMonthDate(String format, Date date){
		String startOfMonthDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(new Date());
			}
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
		    startOfMonthDate = new SimpleDateFormat(format).format(calendar.getTime());
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return startOfMonthDate;
	}
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * ay bitiş tarihini döner.
	 * @param format tarih formati
	 * @param date özel tarih
	 * @return
	 */
	public static String getEndOfMonthDate(String format, Date date){
		String endOfMonthDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(new Date());
			}
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
		    endOfMonthDate = new SimpleDateFormat(format).format(calendar.getTime());
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return endOfMonthDate;
	}
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * gün baslangic tarihini döner.
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getActualMinumumOfDate(String format, Date date){
		String actualMinumumOfDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(date);
			}
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
		    actualMinumumOfDate = new SimpleDateFormat(format).format(calendar.getTime());
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return actualMinumumOfDate;
	}
	
	
	/**
	 * Anlik yada özel olarak verilen tarihin String tipinde 
	 * gün bitis tarihini döner.
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getActualMaximumOfDate(String format, Date date){
		String actualMaximumOfDate = "";
		try {
			if(!DateFormats.isValid(format)){
				throw new RuntimeException("Format uygun değil !");
			}
			Calendar calendar = Calendar.getInstance();
			if(date!=null){
				calendar.setTime(date);
			}
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
		    actualMaximumOfDate = new SimpleDateFormat(format).format(calendar.getTime());
		} catch (Exception e) {
			Helper.errorLogger(Helper.class, e);
		}
		return actualMaximumOfDate;
	}
	
	private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For",  "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {
		String ip = "127.0.0.1";
	    for (String header : IP_HEADER_CANDIDATES) {
	         ip = request.getHeader(header);	        
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip) && !ip.startsWith("0:")) {
	            return ip;
	        }
	    }
	    if(!request.getRemoteAddr().startsWith("0:")){
	    	ip = request.getRemoteAddr();
	    }
	    
	    return Helper.checkNulls(ip,"127.0.0.1");
	}
	
	public static String stringTypeDate2String(String requestDate, String inputFormat, String outputFormat, int minMax){
		String formattedDate 		= null;
		try {
			if(requestDate==null || !DateFormats.isValid(inputFormat) || !DateFormats.isValid(outputFormat)){
				throw new RuntimeException("Hatali formatlama girisi");
			}
			DateFormat df 			= new SimpleDateFormat(inputFormat);
			DateFormat df2 			= new SimpleDateFormat(outputFormat);
			formattedDate 			=  df2.format(df.parse(requestDate));
			if(minMax == 1){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(df.parse(requestDate));
				calendar.set(Calendar.HOUR_OF_DAY, 23);
			    calendar.set(Calendar.MINUTE, 59);
			    calendar.set(Calendar.SECOND, 59);
			    calendar.set(Calendar.MILLISECOND, 999);
				formattedDate = new SimpleDateFormat(outputFormat).format(calendar.getTime());
				calendar = null;
			}
		} catch (ParseException prEx) {
			Helper.errorLogger(Helper.class, prEx);
			prEx.printStackTrace();
		} catch (RuntimeException rEx) {
			Helper.errorLogger(Helper.class, rEx);
			rEx.printStackTrace();
		}
		return formattedDate;
	}
	public static int getRowCount(ResultSet resultSet) {
	    if (resultSet == null) {
	        return 0;
	    }
	    try {
	        resultSet.last();
	        return resultSet.getRow();
	    } catch (SQLException exp) {
	        exp.printStackTrace();
	    } finally {
	        try {
	            resultSet.beforeFirst();
	        } catch (SQLException exp) {
	            exp.printStackTrace();
	        }
	    }
	    return 0;
	}
	public static boolean isIntByRegex(String str){
	    return str.matches("^-?\\d+$");
	}
	
	public static String generateKeyForData(String data,boolean convertJson){
		try{
			JWTUtil jwt = new JWTUtil();
			return jwt.generateTokenForData(data,convertJson);
		}catch(Exception e){
			e.printStackTrace();
			Helper.errorLogger(Helper.class, e);
		}
		
		return "";
	}
}
