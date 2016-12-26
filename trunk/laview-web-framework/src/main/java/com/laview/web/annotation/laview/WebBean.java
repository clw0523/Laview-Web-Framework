/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.annotation.laview;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来声明请求处理方法参数中的 Web Bean 参数，也就是相当于 ActionForm 的参数
 *
 * 声明的 Web Bean 参数，可以用作输入或者输出参数，也就是输出到 jsp 的参数
 * 
 * 注解了  WebBean 的 bean 类，可以使用下述注解，来注解这个类的数据来源：
 * 
 * 	  //@RequestHeader  --- 表明这个数据域的数据来自于 Request Header   -- 暂时不实现，使用 WebAttribute 来完成
 * 
 *    @WebAttribute --- 表明这个数据域的数据来自于 session 或  request
 *
 * @author laview_chen
 * @since: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebBean {
	/**
	 * 在不指定的情况下，将参数名称作为 bean 的名称，如果指定则使用这个名称作为 bean 的名称
	 *
	 * @return
	 */
	String value() default "";
	
	/**
	 * 读或写操作
	 *
	 * @return
	 */
	ActionType action() default ActionType.Read;
}