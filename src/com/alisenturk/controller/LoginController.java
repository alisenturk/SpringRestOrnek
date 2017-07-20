package com.alisenturk.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alisenturk.config.Constants;
import com.alisenturk.config.ProdStage;
import com.alisenturk.dao.base.ICacheManager;
import com.alisenturk.dao.base.IProcessLogDAO;
import com.alisenturk.dao.kullanici.IKullaniciDAO;
import com.alisenturk.dao.token.ITokenDAO;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.LoginData;
import com.alisenturk.model.base.ProcessLog;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.model.response.login.LoginResponseData;
import com.alisenturk.util.Helper;



@RestController
public class LoginController{

	@Autowired
	IKullaniciDAO kullaniciDAO;

	@Autowired
	ITokenDAO tokenDAO;
	
	@Autowired
	IProcessLogDAO processLogDAO;

	@Autowired
	ICacheManager cacheManager;
	
	@Autowired(required = false)
	private HttpServletRequest  request;


	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json","application/xml"})
	public @ResponseBody ResponseData<Kullanici> login(@RequestBody LoginData data) {
		return loginProcess(data);
	}
	
		
	private ResponseData<Kullanici> loginProcess(LoginData data){
		return loginProcess(data,"-1","-1");
	}
		
	private ResponseData<Kullanici> loginProcess(LoginData data,String enlem,String boylam){

		LoginResponseData response = new LoginResponseData();
		if(data.isEmpty()){
			response.setStatusCode(ResponseStatus.NORECORD.getCode());
			response.setStatusMessage("Lütfen geçerli bir kullanıcı kodu ve şifre giriniz.");
			return response;
		}
		if(!isWorkingTime()){
			String message  = Helper.getMessage("tr","working.time");
			String start 	= Helper.getAppMessage("working.time.start");
			String finish 	= Helper.getAppMessage("working.time.finish");
			
			message = message.replace("#start#",start);
			message = message.replace("#finish#",finish);
			
			response.setStatusCode(ResponseStatus.BLOCK.getCode());
			response.setStatusMessage(message);
			return response;
		}
		
		
		int errorCount = processLogDAO.getLoginErrorCount(data.getUsername());
		
		ResponseStatus loginStatus = null;
		
		if(errorCount>=3){
			loginStatus = ResponseStatus.BLOCK;
			response.setStatusCode(loginStatus.getCode());
			response.setStatusMessage(loginStatus.getMessage());
		}else{
			loginStatus =  doLogin(data.getUsername(), data.getPassword());
		}
		
		loginStatus = ResponseStatus.OK;
		
		if (loginStatus.equals(ResponseStatus.OK)) {
			Kullanici kullanici = kullaniciDAO.araKullanici(data.getUsername());
			
			if (kullanici == null) {
				response.setStatusCode(ResponseStatus.NORECORD.getCode());
				response.setStatusMessage("Lütfen geçerli bir kullanıcı kodu ve şifre giriniz.");
			}else if(kullanici.getRoles().size()==0){
				response.setStatusCode(ResponseStatus.UNAUTHORIZED.getCode());
				response.setStatusMessage("Giriş için yetkiniz bulunmamaktadır!");
			} else {
				
				if(enlem==null || enlem.equals("-1")){
					enlem = data.getLatitude();
				}
				if(boylam==null || boylam.equals("-1")){
					boylam = data.getLongitude();
				}
				
				/** Login işlemi loglanıyor */
				ProcessLog log = new ProcessLog();
            	log.setProcessDate(new Date());            	
            	log.setByUser(kullanici.getKullaniciAdi());     
            	log.setToken("");
            	log.setClassName("LoginController.java");
            	log.setMethodName("loginProcess");
            	log.setDescription("Login olundu");
            	log.setProcessData("");
            	log.setLattitude(enlem);
            	log.setLongitude(boylam);
            	log.setIpAddress(Helper.getClientIpAddress(request));
            	log.setViewTimeline(true);
            	log.setGroup("KULLANICI");
            	log.setIconClass("glyphicon glyphicon-map-marker");
            	processLogDAO.insertLog(log);
				
            	kullanici.setLoggedIn(true);
            	kullanici.setLoginZamani(Helper.date2String(new Date(),"dd/MM/yyyy HH:mm:ss"));
            	
				response.setStatusCode(ResponseStatus.OK.getCode());
				response.setStatusMessage("");
				
				
				response.setToken(tokenDAO.tokenYarat(kullanici.getPersonelNo(),
													 kullanici.getKullaniciAdi(),data.getLatitude(),data.getLongitude()));
				
				response.setData(kullanici);
				
				if(response.getToken()==null){
					response.setStatusCode(ResponseStatus.NOK.getCode());
					response.setStatusMessage("Token oluşturmada bağlantı hatası![666]");
					response.setToken("");
					response.setData(null);
				}
			}
			
		} else if(!loginStatus.equals(ResponseStatus.BLOCK)){
			
			ProcessLog log = new ProcessLog();
        	log.setProcessDate(new Date());            	
        	log.setByUser(data.getUsername());     
        	log.setClassName("LoginController.java");
        	log.setMethodName("ERRORLOGIN");
        	log.setDescription("Hatalı giriş denemesi");
        	log.setProcessData(loginStatus.getMessage() + " [" + loginStatus.getCode() +"] ");
        	log.setLattitude(enlem);
        	log.setLongitude(boylam);
        	log.setIpAddress(Helper.getClientIpAddress(request));
        	log.setViewTimeline(false);
        	log.setGroup("KULLANICI");
        	log.setIconClass("glyphicon glyphicon-map-marker");
        	processLogDAO.insertLog(log);
        	
        	log = new ProcessLog();
        	log.setProcessDate(new Date());            	
        	log.setByUser(data.getUsername());     
        	log.setClassName("LoginController.java");
        	log.setMethodName("loginProcess");
        	log.setDescription("Hatalı giriş denemesi");
        	log.setProcessData(loginStatus.getMessage() + " [" + loginStatus.getCode() +"] ");
        	log.setLattitude(enlem);
        	log.setLongitude(boylam);
        	log.setIpAddress(Helper.getClientIpAddress(request));
        	log.setViewTimeline(true);
        	log.setGroup("KULLANICI");
        	log.setIconClass("glyphicon glyphicon-map-marker");
        	processLogDAO.insertLog(log);
        	
			response.setStatusCode(loginStatus.getCode());
			response.setStatusMessage(loginStatus.getMessage());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/tokenCheck", method = RequestMethod.POST, produces = "application/json",headers="Accept=*/*")
	public @ResponseBody ResponseData<String> tokenController(@RequestParam String token) {
		ResponseData<String> result = new ResponseData<>();
		boolean tokenGecerli = tokenDAO.tokenGecerliMi(token);
		
		if (!tokenGecerli){
			tokenDAO.tokenSil(token);
			result.setStatusCode(ResponseStatus.INVALID_TOKEN.getCode());
			result.setStatusMessage(ResponseStatus.INVALID_TOKEN.getMessage());
			result.setData("NOK");
		}else{
			tokenDAO.extendTokenValidity(token);
			result.setStatusCode(ResponseStatus.OK.getCode());
			result.setStatusMessage("");
			result.setData("OK");
		}		
		return result;
	}
	
	
	@RequestMapping(value = "/makePassiveToToken", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseData<String> makePassiveToToken( @RequestHeader(name="hbtoken",defaultValue="bos") String token) {
		ResponseData<String> response = new ResponseData<>();
		try {
			response.setData("");
			if(token.equalsIgnoreCase("bos")){
				throw new HBRuntimeException("Token alinamadi !");
			}
			response = this.kullaniciDAO.makePassiveToToken(token);
		} catch (Exception e) {
			response.setData("");
			response.setStatusCode(ResponseStatus.OK.getCode());
			response.setStatusMessage("");
			Helper.errorLogger(getClass(), e);
		}
		return response;
	}

	private ResponseStatus doLogin(String username, String password) {
		ResponseStatus status = ResponseStatus.WRONGPASSWORD;
		
		try {
			
			Hashtable<String, String> environment = new Hashtable<>();
			String ldapUser = "racfid=" + username	+ ",profiletype=user,cn=racf,o=ibm,c=us";

			environment.put("java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");
			environment.put("java.naming.provider.url",	Helper.getAppMessage("LdapLoadProp.ldapHost"));
			environment.put("java.naming.security.principal", ldapUser);
			environment.put("java.naming.security.credentials", password);
			environment.put("java.naming.security.authentication", "simple");
			
			if(!Constants.PROD_STAGE.equals(ProdStage.DEVELOPMENT)){ /** Geliştirme ortamında LDAP sunucusuna erişilemediği için **/
				InitialDirContext ctx = new InitialDirContext(environment);
				ctx.close();
				status = ResponseStatus.OK;
			}else{
				status = ResponseStatus.OK;
			}
		} catch (Exception e) {
			Helper.errorLogger(getClass(), e);
			if (e.toString().contains("revoked"))
				status = ResponseStatus.REVOKED;
			else if (e.toString().contains("expired"))
				status = ResponseStatus.EXPIRED;
			else
				status = ResponseStatus.WRONGPASSWORD;
		}		
		
		return status;

	}
	
	
	
	private boolean isWorkingTime(){
		boolean workingTime = false;
		int start = Integer.parseInt(Helper.getAppMessage("working.time.start"));
		int finish = Integer.parseInt(Helper.getAppMessage("working.time.finish"));
		
		Calendar calendar = Calendar.getInstance();
		int hour 	= calendar.get(Calendar.HOUR_OF_DAY);
		if(hour>=start && hour<finish){
			workingTime = true;
		}
				
		return workingTime;
	}
	
}
