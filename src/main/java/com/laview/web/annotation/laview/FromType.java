/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.annotation.laview;

/**
 * Web Attribute 类型
 *
 * @author laview_chen
 * @since: v1.0
 */
public enum FromType {

	Request, /* 属性值来自 Request */
	
	Session, /* 属性值来自 Session */
	
	Header,  /* 值来自于 RequestHeader */
	
	Cookies, /* 值来自于 Cookies*/
	
	Both;  /* 所有地方都有可能，找到一个算一个 */
}