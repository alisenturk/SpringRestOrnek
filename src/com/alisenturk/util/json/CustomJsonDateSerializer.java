package com.alisenturk.util.json;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Date;

import com.alisenturk.annotations.MyDateFormat;
import com.alisenturk.util.Helper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class CustomJsonDateSerializer extends JsonSerializer<Date> implements ContextualSerializer {

	private String dateFormat = "dd/MM/yyyy HH:mm:ss";
	
	public CustomJsonDateSerializer() {
		super();
	}

	
	public CustomJsonDateSerializer(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		
		generator.writeString(timeStampFormatter(date,this.dateFormat));
	}
	
	public static synchronized String timeStampFormatter(Date date,String format){
		return Helper.date2String(date,format);		
	}
		
	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializationConfig, BeanProperty beanProperty) throws JsonMappingException {
		 final AnnotatedElement annotated = beanProperty.getMember().getAnnotated();
		 MyDateFormat dateFormat = beanProperty.getMember().getAnnotation(MyDateFormat.class);
		 String format = this.dateFormat;
		 if(dateFormat!=null && dateFormat.value()!=null){
			 format = dateFormat.value();
		 }
		return new CustomJsonDateSerializer(format);
	}
}
