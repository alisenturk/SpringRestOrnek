package com.alisenturk.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alisenturk.annotations.TokenControl;
import com.alisenturk.config.Constants;
import com.alisenturk.controller.BaseController;
import com.alisenturk.dao.kullanici.IKullaniciDAO;
import com.alisenturk.dao.token.ITokenDAO;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.ActiveTokenList;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.Helper;


@Aspect
@Configuration
public class TokenController {
	
	@Autowired
	ITokenDAO tokenDAO;
	

	@Autowired
	IKullaniciDAO kullaniciDAO;
	
	@Autowired(required=true)
	@Qualifier(value="activeTokenList")
	ActiveTokenList activeTokenList; 
	
	@Around(value="execution(* tr.com.halkbank.controller..*.*(..)) && @annotation(tr.com.halkbank.annotations.TokenControl)")
    public Object tokenKontrol(ProceedingJoinPoint joinPoint) { 
        Object result = null;
        StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		String 	methodName 		= "";
		int[] 	tokenGecerli 	= new int[2];
		boolean invalidToken 	= false;
		
        try{ 
        	final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        	
    	    Method method = signature.getMethod();
    	    methodName = joinPoint.getTarget().getClass().getName() + "." + method.getName();
    	    
    	    TokenControl tokenCtrl = method.getAnnotation(TokenControl.class);
    	    int tokenIndex = -1;
        	
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        	
            if(joinPoint.getArgs()!=null && joinPoint.getArgs().length>0){
            	
            	int indx = -1;		
            	for(String name:signature.getParameterNames()){
            		indx++;
            		if("token".equals(name)){
            			tokenIndex = indx;            			            			
            		}
            	}
            	
            	
            	if(joinPoint.getArgs()[tokenIndex]==null){
            		invalidToken = true;
            	}
            	String token =  String.valueOf(joinPoint.getArgs()[tokenIndex]);       
            	
            	if(token!=null && token.length()<20){
            		invalidToken = true;
            	}
            	
            	if(!invalidToken){
            		tokenGecerli = tokenDAO.tokenGecerliMiWithTime(token);
            	}
            	
    			if (invalidToken || tokenGecerli[0]==0){
    				result = "Invalid token!"; 
    				if(signature.getReturnType().getName().indexOf("ResponseData")>-1){
	    				ResponseData<String> response = new ResponseData<>();
	    				response.setStatusCode(ResponseStatus.INVALID_TOKEN.getCode());
	    				response.setStatusMessage(ResponseStatus.INVALID_TOKEN.getMessage());
	    				result = response;
    				}
    			}else{
    				/*** Token geçerlilik süresinin uzatılması için kuyruğa atılıyor 
    				 * Token'ının geçerliliğinin dolmasına 10dk az süre kaldıysa güncelleme yapılması için kuyruğa atılır 
    				 ***/
    				if(tokenGecerli[1]>0 && tokenGecerli[1]<(60*10)){ 
    					activeTokenList.getTokenList().add(token);
    				}
    				
    				Object target = joinPoint.getTarget();
                	if(tokenCtrl.kullaniciDogrulamasi() && target instanceof BaseController){
                		BaseController bc = (BaseController)target;
                		Kullanici kullanici = kullaniciDAO.araKullaniciToken(token);
            			if(kullanici==null){
            				result = "Kullanıcı bulunamadı![token]:" + token; 
            				if(signature.getReturnType().getName().indexOf("ResponseData")>-1){
        	    				ResponseData<String> response = new ResponseData<>();
        	    				response.setStatusCode(ResponseStatus.NORECORD.getCode());
        	    				response.setStatusMessage("Kullanıcı bulunamadı![token]:" + token);
        	    				result = response;
            				}
            			}else{
            				if(request!=null && kullanici!=null){
            					request.setAttribute(Constants.REQ_USER_PARAM_NAME,kullanici);
            				}
            				
							result = joinPoint.proceed();
            			}	                		
                	}else{
						result = joinPoint.proceed();
                	}
    				 
    			}
               
            }
        }catch(Throwable e){
            Helper.errorLogger(getClass(),new HBRuntimeException(e.getMessage()));
        }finally{
        	stopWatch.stop();
        	if(Constants.OPEN_METHOD_PERFORMANS)
        		System.out.println(methodName + " calisma suresi..:" + stopWatch.getTotalTimeMillis() + " ms");
        }

        return (result==null)?"Basarisiz":result;
    }
}
