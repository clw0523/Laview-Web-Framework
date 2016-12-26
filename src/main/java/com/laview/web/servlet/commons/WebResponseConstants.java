/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.commons;

import javax.servlet.http.HttpServletResponse;

/**
 * 请求常量
 *
 * @author laview_chen
 * @since: v1.0
 */
public final class WebResponseConstants {
	
	public final static String SC_STATUS = "status:";

	/**
	 * HTTP 403 
	 */
	public final static String SC_FORBIDDEN = SC_STATUS + HttpServletResponse.SC_FORBIDDEN;
	
	/**
	 * HTTP 404
	 */
	public final static String SC_NOT_FOUND = SC_STATUS + HttpServletResponse.SC_NOT_FOUND;
	
}