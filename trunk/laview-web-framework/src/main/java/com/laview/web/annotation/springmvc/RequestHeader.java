/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.annotation.springmvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入请求头变量的值.
 * 
 * 来自 SpringMVC
 *
 * @author Juergen Hoeller
 * @since: v1.5.2
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {

	/**
	 * The name of the request header to bind to.
	 */
	String value() default "";

	/**
	 * Whether the header is required.
	 * <p>Default is {@code true}, leading to an exception thrown in case
	 * of the header missing in the request. Switch this to {@code false}
	 * if you prefer a {@code null} in case of the header missing.
	 * <p>Alternatively, provide a {@link #defaultValue}, which implicitly sets
	 * this flag to {@code false}.
	 */
	boolean required() default true;

	/**
	 * The default value to use as a fallback. Supplying a default value implicitly
	 * sets {@link #required} to {@code false}.
	 */
	String defaultValue() default ValueConstants.DEFAULT_NONE;	
}