/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.annotation.laview;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.laview.web.annotation.springmvc.ValueConstants;

/**
 * Session 数据注解
 * 
 *   * 如果 Session 不存在，就返回默认值
 *   
 *   * 如果 Session 已经存在，就取此值
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionValue {

	/**
	 * The name of the request header to bind to.
	 */
	String value() default "";

	/**
	 * The default value to use as a fallback. Supplying a default value implicitly
	 * sets {@link #required} to {@code false}.
	 */
	String defaultValue() default ValueConstants.DEFAULT_NONE;	
	
}