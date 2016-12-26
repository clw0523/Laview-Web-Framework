/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.annotation.springmvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.laview.commons.web.RequestMethod;

/**
 * 请求映射，这个注解只能用于类和类的方法。
 * 
 * 但是，要注意的是，当用于类时，将替代类中的  @Controller(path="/") 中的 path 部分
 *
 * 并且，RequestMethod[] method() 将不起作用，这一点需要注意。
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

	String value() default "";
	
	/**
	 * 用来方法来区分同一个请求
	 *
	 * @return
	 */
	RequestMethod[] method() default {};
}