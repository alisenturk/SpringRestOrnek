package com.alisenturk.config;

import java.util.List;

import com.alisenturk.elasticloggerv2.service.ElasticService;
import com.alisenturk.elasticloggerv2.service.ElasticSetting;
import com.alisenturk.util.Helper;

public class ElasticLogger<T> {

	private ElasticSetting 		settings; 
	private ElasticService<T>	elasticService;
	private List<T>				data; 		
	private String				indiceName;
	private String				mappingName;
	
	public ElasticLogger(String mappingName) {
		super();
		this.mappingName = mappingName;
		indiceName = Helper.getAppMessage("elasticlog.indices");
		init();
	}
	
	public ElasticLogger(String mappingName,String indiceName) {
		super();
		this.mappingName = mappingName;
		this.indiceName  = indiceName;
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init(){
		settings = new ElasticSetting();
		settings.setHostAddress(Helper.getAppMessage("elasticlog.host"));
		settings.setPortNumber(Helper.getAppMessage("elasticlog.port"));
		settings.setIndexName(indiceName);
		settings.setMappingName(mappingName);
		
		elasticService = ElasticService.createElasticService(settings);
	}
	
	public void write(){
		if(Constants.ELASTIC_WRITE_UNSYNC){ 
			Runnable runA = new Runnable() {
				public void run() {
		    	  long lastId = elasticService.getDocumentCount();
		    	  elasticService.addDocument(data, lastId);
				}
			};
			
			do{
			}while(Constants.THREAD_GROUP.activeCount()>4);
			
			Thread ta = new Thread(Constants.THREAD_GROUP,runA);
			ta.start();
		}else{
			long lastId = elasticService.getDocumentCount();
	    	 elasticService.addDocument(data, lastId);
		}
		
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	
}
