package com.alisenturk.dao.base;

import java.util.List;

import com.alisenturk.model.response.ResponseData;

public interface ICacheManager {

	public ResponseData<List<String[]>> cacheList();
	public ResponseData<String> 		clearCache(String password,String cacheName);
	
}
