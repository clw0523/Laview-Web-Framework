/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet;

/**
 * Cookies 操作类
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface Cookies {

	/**
	 * 添加一个 Cookies
	 *
	 * @param name     --- cookie 名称
	 * @param value    --- 值
	 * @param maxAge   --- 生存时间
	 */
	public void addCookie(String name, String value, int maxAge);
	
	/**
	 * 添加一个 Cookie，生命周期为无限
	 *
	 * @param name
	 * @param value
	 */
	public void addCookie(String name, String value);
	
	/**
	 * 删除 Cookie
	 *
	 * @param name
	 */
	public void deleteCookie(String name);
	
	/**
	 * 使用名称来获取值
	 *
	 * @param name
	 * @return
	 */
	public String getCookie(String name);
	
}