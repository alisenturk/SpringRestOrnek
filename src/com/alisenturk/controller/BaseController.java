package com.alisenturk.controller;

import javax.servlet.http.HttpServletRequest;

import com.alisenturk.config.Constants;
import com.alisenturk.model.kullanici.Kullanici;

public abstract class BaseController {

	public Kullanici getKullaniciFromRequest(HttpServletRequest req){
		if(req!=null && req.getAttribute(Constants.REQ_USER_PARAM_NAME)!=null){
			return (Kullanici)req.getAttribute(Constants.REQ_USER_PARAM_NAME);
		}		
		return null;
	}

}
