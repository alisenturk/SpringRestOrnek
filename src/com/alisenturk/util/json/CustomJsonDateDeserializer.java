package com.alisenturk.util.json;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alisenturk.annotations.MyDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

public class CustomJsonDateDeserializer extends JsonDeserializer<Date> implements ContextualDeserializer {
	private String dateFormat = "";
	public CustomJsonDateDeserializer() {
		super();
	}

	
	public CustomJsonDateDeserializer(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}
	@Override
	public Date deserialize(JsonParser jsonparser,DeserializationContext deserializationcontext) throws IOException {
		
		String date = jsonparser.getText();
		
		if(dateFormat!=null && dateFormat.length()>3){			
			return dateParser(date,dateFormat); 
		}else{
			if(date.indexOf("-")==2 && date.length()==10){
				return dateParser(date,"dd-MM-yyyy");
			}else if(date.indexOf("-")>=4 && date.length()==10){
				return dateParser(date,"yyyy-MM-dd");
			}else if(date.indexOf("-")>-1 && date.indexOf("T")==-1 && date.length()>=16){
				return dateParser(date,"yyyy-MM-dd HH:mm:ss");
			}else if(date.indexOf("-")>-1 && date.length()==10){
				return dateParser(date,"yyyy-MM-dd");
			}else if(date.indexOf("/")>-1 && date.length()==10){
				return dateParser(date,"dd/MM/yyyy");
			}else if(date.indexOf("/")>-1 && date.length()>=16){
				return dateParser(date,"dd/MM/yyyy HH:mm:ss");
			}else if(date.indexOf("-")>-1 && date.indexOf("T")>-1 ){
				return dateParser(date,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
			}
			
			return null;
			
		}

	}
	
	private Date dateParser(String date,String dateFormat){		
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext serializationConfig,BeanProperty beanProperty) throws JsonMappingException {
		 final AnnotatedElement annotated = beanProperty.getMember().getAnnotated();
		 MyDateFormat dateFormat = beanProperty.getMember().getAnnotation(MyDateFormat.class);
		 String format = this.dateFormat;
		 if(dateFormat!=null && dateFormat.value()!=null){
			 format = dateFormat.value();
		 }
		return new CustomJsonDateDeserializer(format);
	}

	
	
}
