package com.alisenturk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable{
	
	public String	groupCode() default "ANY";
	public String 	methodDesc();
	public String[] detail() default "";
	public String[] arguments() default "ALL";
	public String 	iconClass() default "glyphicon glyphicon-info-sign";
	public boolean 	viewTimeline() default false;
	
}
