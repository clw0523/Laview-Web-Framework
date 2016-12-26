/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import com.laview.commons.reflect.MethodArgumentDescriptor;
import com.laview.web.annotation.laview.SessionValue;
import com.laview.web.annotation.springmvc.CookieValue;
import com.laview.web.annotation.springmvc.RequestHeader;

/**
 * 请求处理方法参数描述类。
 *
 * @author laview_chen
 * @since: v1.0
 */
public class HandlerMethodArgumentDescriptor extends MethodArgumentDescriptor{

	/**
	 * URL 参数变量
	 */
	private boolean isPathVariable;
	
	/**
	 * Request Header 参数
	 */
	private RequestHeader requestHeader;
	
	private CookieValue cookieValue;
	
	/**
	 * session 参数
	 */
	private SessionValue sessionValue;
	
	/**
	 * @return the isPathVariable
	 */
	public boolean isPathVariable() {
		return isPathVariable;
	}

	/**
	 *
	 * @return
	 */
	public boolean isCookieValue() {
		return cookieValue != null;
	}

	/**
	 *
	 * @return
	 */
	public boolean isSessionValue() {
		return sessionValue != null;
	}
	
	/**
	 * @param isPathVariable the isPathVariable to set
	 */
	public void setPathVariable(boolean isPathVariable) {
		this.isPathVariable = isPathVariable;
	}

	/**
	 * @return the requestHeader
	 */
	public boolean isRequestHeader() {
		return requestHeader != null;
	}

	public RequestHeader getRequestHeader(){
		return this.requestHeader;
	}
	
	/**
	 * @param requestHeader the requestHeader to set
	 */
	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

	/**
	 * @return the cookieValue
	 */
	public CookieValue getCookieValue() {
		return cookieValue;
	}

	/**
	 * @param cookieValue the cookieValue to set
	 */
	public void setCookieValue(CookieValue cookieValue) {
		this.cookieValue = cookieValue;
	}

	/**
	 * @return the sessionValue
	 */
	public SessionValue getSessionValue() {
		return sessionValue;
	}

	/**
	 * @param sessionValue the sessionValue to set
	 */
	public void setSessionValue(SessionValue sessionValue) {
		this.sessionValue = sessionValue;
	}

}