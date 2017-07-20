package com.alisenturk.dao.musteri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.alisenturk.config.ConnectionPool;
import com.alisenturk.config.Constants;
import com.alisenturk.config.ProdStage;
import com.alisenturk.controller.musteri.MusteriAramaKriterleri;
import com.alisenturk.dao.base.BaseDAO;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.musteri.Musteri;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.Helper;



@Repository
@Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MusteriDAO extends BaseDAO implements IMusteriDAO  {

	private static final long serialVersionUID = 1486299502063212135L;

	@Autowired
	ConnectionPool datasource;

	
	
	@Override	
	@Cacheable(value = "musteriFindListCache", key = "{#kriter,#kullanici}",unless="#result.data.size()==0")	
	public ResponseData<List<Musteri>> musteriAraList(Kullanici kullanici,MusteriAramaKriterleri kriter) {
		
		ResponseData<List<Musteri>> responseData  = new ResponseData<List<Musteri>>();
		try {
			List<Musteri> list = null;
			responseData.setData(list);
			String mureCommArea = "";
			
			if(responseData.getStatusCode().equals(ResponseStatus.OK.getCode())){
				list = new ArrayList<>();
				
				Musteri musteri = new Musteri();
				musteri.setMusteriNo(1);
				musteri.setAd("Ali");
				musteri.setSoyad("Şentürk");
				musteri.setTcKimlikNo("123213213213");
				
				list.add(musteri);
				
				if(list.isEmpty() && !responseData.getData().isEmpty()){
					responseData.setData(Collections.<Musteri>emptyList());
					responseData.setStatusCode(ResponseStatus.NORECORD.getCode());
					responseData.setStatusMessage("Aradığınız müşteri sizin bölgenize kayıtlı değildir!");
				}
				
				if(!list.isEmpty()){
					responseData.setData(list);
				}else{
					responseData.setData(Collections.<Musteri>emptyList());
				}
			}else{				
				responseData.setData(Collections.<Musteri>emptyList());
				throw new HBRuntimeException(responseData.getStatusCode() + "-" + responseData.getStatusMessage());
			}
			
		} catch (Exception e) {
			responseData.setStatusCode(ResponseStatus.NOK.getCode());
			responseData.setStatusMessage(ResponseStatus.NOK.getMessage());
			responseData.setData(new ArrayList<Musteri>());
			Helper.errorLogger(getClass(), e, "[Kriter]:" + kriter.toString());
		}
		return responseData;
	}

	
	
}
