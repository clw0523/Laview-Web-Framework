package com.laview.web.annotation.springmvc;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerAdvice {

	//@AliasFor("basePackages")
	String[] value() default {};//暂时还没用到

	Class<?>[] basePackageClasses() default {};//暂时还没用到

	Class<? extends Annotation>[] annotations() default {};//暂时还没用到

}
