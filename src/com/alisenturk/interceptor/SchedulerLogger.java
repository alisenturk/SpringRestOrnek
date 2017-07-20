package com.alisenturk.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import com.alisenturk.annotations.ShcedulerLogger;
import com.alisenturk.config.Constants;
import com.alisenturk.dao.scheduler.ISchedulerDAO;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.scheduler.SchedulerLog;
import com.alisenturk.util.Helper;
import com.alisenturk.util.ProjectUtil;


@Aspect
@Configuration
public class SchedulerLogger {

	@Autowired
	ISchedulerDAO schedulerDAO;
	
	@Autowired(required = true)
	private HttpServletRequest  request;
	
	@After(value="execution(* com.alisenturk.scheduled..*.*(..)) && @annotation(com.alisenturk.annotations.ShcedulerLogger)")
    public Object logger(JoinPoint joinPoint) { 
		Object result = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		String methodName 	= "";
		String className	= "";
		try{
			
			final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        	
    	    Method method = signature.getMethod();
    	    
    	    className 	= joinPoint.getTarget().getClass().getName();
    	    methodName 	= method.getName();
    	    String serverIp = Helper.getServerIPAddress();
    	    ShcedulerLogger shcedulerLogger 	= method.getAnnotation(ShcedulerLogger.class);
    	    
    	    if(ProjectUtil.havePermissionRunForTask(serverIp)){
    	    	
	    	    SchedulerLog log = new SchedulerLog();
	        	log.setSchedulerName(shcedulerLogger.schedulerName());            	
	        	log.setServerIP(serverIp);     
	        	log.setStatusCode("OK");
	        	log.setStatusMessage("OK");        	
	        	log.setSchedulerTime(shcedulerLogger.schedulerTime());
	        	schedulerDAO.insertSchedulerLog(log);
    	    }
        	
        	
		}catch(Throwable e){
			Helper.errorLogger(getClass(),new HBRuntimeException(e.getMessage()));
		}finally{
			stopWatch.stop();
        	if(Constants.OPEN_METHOD_PERFORMANS)
        		System.out.println(Helper.getAppMessage("project.Name")+"-SchedulerLogger-" + className + "." + methodName + " calisma suresi-"+Helper.date2String(new Date(),"yyyyMMddHHmmss")+"..:" + stopWatch.getTotalTimeMillis() + " ms");
		}
		
		return result;
		
	}
}
