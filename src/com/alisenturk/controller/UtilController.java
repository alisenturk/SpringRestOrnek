package com.alisenturk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alisenturk.dao.base.ICacheManager;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.Helper;


@RestController
public class UtilController {

	@Autowired
	ICacheManager cacheManager;
	
	@RequestMapping(value = "/cacheList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseData<List<String[]>> cacheList(@RequestParam String password) {
		
		ResponseData<List<String[]>> response = new ResponseData<>();
		try{
			if(!Helper.checkNulls(password, "").equals("1234"))
				throw new HBRuntimeException("Hatalı şifre!");
			
			response = cacheManager.cacheList();
			
		}catch(HBRuntimeException e){
			response.setStatusCode(ResponseStatus.NOK.getCode());
			response.setStatusMessage(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/clearCache", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseData<String> clearCache(@RequestParam String password,@RequestParam(defaultValue="all") String cacheName) {
		
		ResponseData<String> response = new ResponseData<>();
		try{
			if(!Helper.checkNulls(password, "").equals("1234"))
				throw new HBRuntimeException("Hatalı şifre!");
			
			response = cacheManager.clearCache(password, cacheName);
		}catch(HBRuntimeException e){
			response.setStatusCode(ResponseStatus.NOK.getCode());
			response.setStatusMessage(e.getMessage());
		}
		
		return response;
	}
	
}
