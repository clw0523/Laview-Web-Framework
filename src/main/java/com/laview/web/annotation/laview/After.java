/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.annotation.laview;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示在某个请求的请求处理方法执行之前执行的方法.
 * 
 *    doBefore(..)
 *    
 *    doAction(..)
 *    
 *    doAfter(..)
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface After {

	/**
	 * 可以有一个请求路径，也可以没有。 当存在请求路径时，系统在执行这些请求路径的响应方法之后，将执行本方法
	 * 
	 * @return
	 */
	String value();

}