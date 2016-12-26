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
 * Attribute 的属性值
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebAttribute {

	/**
	 * 属性值来自于何处
	 *
	 * @return
	 */
	FromType from() default FromType.Request;
	
	/**
	 * 对应 Web 客户端提交的名称，默认情况下，取 属性名，也可以属性名不同，通过这里来绑定客户端的名称
	 *
	 * @return
	 */
	String name() default "";
	
	/**
	 * 读取数据，还是读写
	 *
	 * @return
	 */
	ActionType action() default ActionType.Read;
	
	/**
	 * 一个默认值
	 *
	 * @return
	 */
	String defaultValue() default "";
}