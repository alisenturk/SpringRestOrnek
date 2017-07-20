package com.alisenturk.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import com.alisenturk.annotations.Loggable;
import com.alisenturk.config.Constants;
import com.alisenturk.dao.base.IProcessLogDAO;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.BaseObject;
import com.alisenturk.model.base.ProcessLog;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.util.Helper;
import com.google.gson.Gson;

@Aspect
@Configuration
public class ProcessLogger {

	@Autowired
	IProcessLogDAO processLogDAO;
	
	@Autowired(required = true)
	private HttpServletRequest  request;
	
	@Around(value="execution(* com.alisenturk.controller..*.*(..)) && @annotation(com.alisenturk.annotations.Loggable)")
    public Object logger(ProceedingJoinPoint joinPoint) { 
		Object result = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		String methodName 	= "";
		String className	= "";
		try{
			result = joinPoint.proceed();
			
			final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        	
    	    Method method = signature.getMethod();
    	    
    	    className 	= joinPoint.getTarget().getClass().getName();
    	    methodName 	= method.getName();
    	    
    	    Loggable loggable 	= method.getAnnotation(Loggable.class);
    	    
        	int 		paramIndx 	= 0;
        	Object 		param 		= null;
        	Gson 		gson 		= new Gson();
        	String 		args 		= Arrays.toString(loggable.arguments()).toString();
        	Kullanici 	kullanici 	= null;
        	String		token		= null;
        	String		enlem		= "-1";
        	String		boylam		= "-1";
        	BaseObject  detail		= null;
        	String		detailInfo	= "";
        	String		statusCode	= "";
        	String		statusMsg	= "";
        	boolean		isViewEveryone = true;
        	StringBuilder strData = new StringBuilder();
            if(joinPoint.getArgs()!=null && joinPoint.getArgs().length>0){
            	for(String name:signature.getParameterNames()){
            		param = joinPoint.getArgs()[paramIndx];
            		if(name.equals("kullanici")){
            			kullanici = (Kullanici)param;
            		}else if(name.equals("token") && param!=null){
            			token = String.valueOf(param);
            		}else if(name.equals("enlem") && param!=null){
            			enlem = String.valueOf(param);
            		}else if(name.equals("boylam") && param!=null){
            			boylam = String.valueOf(param);
            		}else if(args.indexOf(name)>-1 ){
            			strData.append(name + "=" +  gson.toJson(param) + "#");            			
            		}
            		
            		if(loggable.detail()!=null && detailInfo.isEmpty()){
            			for(String detailName:loggable.detail()){
            				if(name.equals(detailName)){
            					detail = (BaseObject)param;
            					detailInfo += detail.getObjectDetail();
            					isViewEveryone = detail.isViewEveryone();
            				}else{
            					detailInfo += String.valueOf(detailName);
            				}
            			}
            		}
            		
            		paramIndx++;
            	}
            	
            	if(signature.getReturnType().getName().indexOf("ResponseData")>-1 && result instanceof ResponseData<?>){
            	
        			ResponseData<?> resp = (ResponseData<?>)result;
        			if(resp!=null){
        				statusCode 	= resp.getStatusCode();
        				statusMsg	= resp.getStatusMessage();
        			}
            	
            	}
            	
            	ProcessLog log = new ProcessLog();
            	log.setProcessDate(new Date());            	
            	log.setByUser(kullanici!=null?kullanici.getKullaniciAdi():"NULL");     
            	log.setPersonelNo(kullanici!=null?kullanici.getPersonelNo():-1);
            	log.setToken(token);
            	log.setClassName(className);
            	log.setMethodName(methodName);
            	log.setDescription(loggable.methodDesc());
            	log.setProcessData(strData.toString());
            	log.setLattitude(enlem);
            	log.setLongitude(boylam);
            	log.setIpAddress(Helper.getClientIpAddress(request));
            	log.setViewTimeline(loggable.viewTimeline());
            	log.setIconClass(loggable.iconClass());            	
        		log.setDetailInfo(Helper.checkNulls(detailInfo,""));
            	log.setGroup(Helper.checkNulls(loggable.groupCode(),"ANY"));
            	log.setStatusCode(statusCode);
            	log.setStatusMessage(statusMsg);
            	log.setViewEveryone(isViewEveryone);
            	Future<Boolean> resp = processLogDAO.insertLogAsync(log);
            	
            }
		}catch(Throwable e){
			Helper.errorLogger(getClass(),new HBRuntimeException(e.getMessage()));
		}finally{
			stopWatch.stop();
        	if(Constants.OPEN_METHOD_PERFORMANS)
        		System.out.println("ProcessLog-" + className + "." + methodName + " calisma suresi..:" + stopWatch.getTotalTimeMillis() + " ms");
		}
		
		return result;
		
	}
}
