/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind.argument;

/**
 * 将字符串转成不同基本类型的“视图”接口。
 * 
 * 将给 ViewBoundHelper 视图类使用。
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface BaseTypeValueBound {

	public Object valueOf(String s);
	
}