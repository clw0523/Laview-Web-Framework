/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.util;

/**
 * 要设置的属性信息
 *
 * @author laview_chen
 * @since: v1.0
 */
class PropertyInfo {

	//Request 中的数据值
	Object sourceValue;
	
	// Pojo 属性的类型
	Class<?> targetType;
	
	// Pojo 的属性名称
	String propertyName;
	
	// Pojo，这是 Action 参数类型创建的对象，也就是等待装填的对象，然后作为传入值 传入到方法中
	Object argObject;
	
}