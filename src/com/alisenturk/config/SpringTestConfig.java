package com.alisenturk.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alisenturk.model.base.ActiveTokenList;

@Configuration
@ComponentScan(basePackages = { "tr.com.halkbank" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy
public class SpringTestConfig {
	
	
	@Bean(name="activeTokenList")
	public ActiveTokenList activeTokenList(){
		ActiveTokenList atl = new ActiveTokenList();
		HashSet<String> hashSet = new HashSet<>();
		Set<String> set = Collections.synchronizedSet(hashSet);
		atl.setTokenList(set);
		return atl;
	}
	
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(EhCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean EhCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("tr/com/halkbank/test/resources/ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}
}
