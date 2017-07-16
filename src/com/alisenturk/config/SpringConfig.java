package com.alisenturk.config;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alisenturk.model.base.ActiveTokenList;
import com.alisenturk.util.Helper;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "tr.com.halkbank")
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy
@EnableScheduling
public class SpringConfig extends WebMvcConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean(name="activeTokenList")
	public ActiveTokenList activeTokenList(){
		ActiveTokenList atl = new ActiveTokenList();
		HashSet<String> hashSet = new HashSet<>();
		Set<String> set = Collections.synchronizedSet(hashSet);
		atl.setTokenList(set);
		return atl;
	}
	
	
	
	@Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setExposeContextBeansAsAttributes(true);
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
        		.addResourceLocations("/public/", "/")
        		.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)
        		.cachePublic());
        
    }
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			    .allowedOrigins(Helper.getAppMessage("allowedOrigins").split(";")) 
				.allowedMethods("*")
				.allowedHeaders("*")		
				.allowCredentials(false).maxAge(3600);
		
	}

	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(EhCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean EhCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("tr/com/halkbank/resources/ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(50000000);
		commonsMultipartResolver.setMaxInMemorySize(50000000);
		return commonsMultipartResolver;
	}

	
	
	/*
	
	@Bean(name="dataSource")
	  public DataSource getDataSource() {
		
        
		DriverManagerDataSource dataSource = null;
	    String driverClassName 	= "com.ibm.db2.jcc.DB2Driver";
	    String url 				=  Helper.getAppMessage("db2.url");	     
	    String username 		= Helper.getAppMessage("db2.userId");	     
	    String password 		= Helper.getAppMessage("db2.password");
	    
	    Properties prop = new Properties();	   
	    prop.put("initialSize",20);
	    prop.put("removeAbandoned",true);
	    prop.put("maxActive", 100);
	    prop.put("minIdle", 1);
	    prop.put("maxIdle", 5);
	    prop.put("validationQuery","select 1 from sysibm.sysdummy1");
	    prop.put("testOnBorrow", true);
	    
	    dataSource = new DriverManagerDataSource(url,prop);
	    dataSource.setDriverClassName(driverClassName);
	    dataSource.setUrl(url);
	    dataSource.setUsername(username);
	    dataSource.setPassword(password);
	    System.out.println("dataSource...:" + dataSource.toString() + " -> " + new Date());
	    return dataSource;
	  } */

	/*
	@Bean
	public DataSource dataSource(){
		@Bean
    @Profile("javaee")
    public JndiObjectFactoryBean dataSource() throws IllegalArgumentException {
        JndiObjectFactoryBean dataSource = new JndiObjectFactoryBean();
        dataSource.setExpectedType(DataSource.class);
        dataSource.setJndiName(env.getProperty("jdbc.jndiDataSource"));
        return dataSource;
    }
   	
	}*/
}
