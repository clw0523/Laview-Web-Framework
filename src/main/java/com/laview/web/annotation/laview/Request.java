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

/**
 * 这是一个综合注解，定义了本注解的方法，表示不区分 请求方法 （即 Get, Post, Delete, Put 等行）
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Request {

	/**
	 * 可以有一个请求值，也可以没有
	 *
	 * @return
	 */
	String value() default "";
	
}