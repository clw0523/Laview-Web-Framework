/**
 * @Copyright:Copyright (c) 1991 - 2011
 * @Company: Laview
 */
package com.laview.web.annotation.springmvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Action 的注解。每一个有 Controller 注解的 Pojo 类都是 请求响应 类。
 *
 * 本注解只作用在 Class 一级
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
	
	/**
	 * 对应请求名称，例如“/test.do”
	 *
	 * @return
	 */
	String path() default "";
}