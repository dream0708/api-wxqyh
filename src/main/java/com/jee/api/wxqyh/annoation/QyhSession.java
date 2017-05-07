package com.jee.api.wxqyh.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  
@Retention(RetentionPolicy.RUNTIME) 
public @interface QyhSession {

	boolean detail() default false ;
	
	boolean hash() default false ;
}
