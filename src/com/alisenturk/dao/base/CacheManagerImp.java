package com.alisenturk.dao.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;

import net.sf.ehcache.Ehcache;

@Repository
public class CacheManagerImp implements ICacheManager {

	@Autowired
	CacheManager cacheManager;
	
	@Override
	public ResponseData<List<String[]>> cacheList() {
		ResponseData<List<String[]>> response = new ResponseData<>();
		String 	cacheName;
		Object 	nativeCache;
		Ehcache ehCache;
		String[] data;
		try{
			response.setStatusCode("OK");
			response.setStatusMessage("OK");
			response.setData(new ArrayList<String[]>());
			
			Collection<String> cacheNames = cacheManager.getCacheNames();
			Iterator it = cacheNames.iterator();
			while(it.hasNext()){
				data = new String[2];
				cacheName = (String) it.next();
				nativeCache = cacheManager.getCache(cacheName).getNativeCache();
				if (nativeCache instanceof Ehcache) {
				    ehCache = (Ehcache) nativeCache;
				    data[0] = cacheName;
				    data[1] = String.valueOf(ehCache.getStatistics().getSize());
				}
				response.getData().add(data); 
			}
			
			
		}catch(HBRuntimeException e){
			response.setStatusCode(ResponseStatus.NOK.getCode());
			response.setStatusMessage(e.getMessage());
		}
		return response;
	}

	@Override
	public ResponseData<String> clearCache(String password, String cacheName) {
		ResponseData<String> response = new ResponseData<>();
		Object 	nativeCache	= null;
		String	strCacheName= null; 
		Ehcache ehCache		= null;
		int		clearCount	= 0;
		try{
			Collection<String> cacheNames = cacheManager.getCacheNames();
			Iterator it = cacheNames.iterator();
			while(it.hasNext()){
				strCacheName = (String) it.next();
				if("all".equalsIgnoreCase(cacheName) || cacheName.equalsIgnoreCase(strCacheName)){
					nativeCache = cacheManager.getCache(strCacheName).getNativeCache();
					if(nativeCache instanceof Ehcache) {
						ehCache = (net.sf.ehcache.Ehcache) nativeCache;
						clearCount +=ehCache.getStatistics().getSize();
					}
					cacheManager.getCache(strCacheName).clear();
				}
			}
			
			response.setStatusCode("OK");
			response.setStatusMessage("Temizlenen cache adedi..:" + clearCount);
		}catch(HBRuntimeException e){
			response.setStatusCode(ResponseStatus.NOK.getCode());
			response.setStatusMessage(e.getMessage());
		}
		
		return response;
	}

}
