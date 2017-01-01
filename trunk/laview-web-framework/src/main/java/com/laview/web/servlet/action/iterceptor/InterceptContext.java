/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.iterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.config.UrlMethodInfo;

/**
 * 拦截器执行的上、下文类
 * 
 * 主要是为拦截器，提供当前拦截点的上、下文所需要数据
 *
 * @author laview_chen
 * @since: v1.0
 */
public class InterceptContext {

	//上文数据
	private ServletData servletData;
	
	/**
	 * 请求所映射的类的实例
	 */
	private Object action;
	
	/**
	 * 请求所映射的方法
	 */
	private UrlMethodInfo handleMethodInfo;
	
	/**
	 * 方法的参数值，（按顺序）
	 */
	private Object[] methodArgs;
	
	/**
	 * 执行 Action 的返回结果
	 */
	private Object actionResult;
	
	private Exception actionException;

	/**
	 * @param action
	 * @param methodInfo
	 * @param args
	 */
	public InterceptContext(ServletData servletData, Object action, UrlMethodInfo methodInfo, Object[] args) {
		this(servletData, action, methodInfo, args, null);
	}

	/**
	 * @param action
	 * @param methodInfo
	 * @param args
	 */
	public InterceptContext(ServletData servletData, Object action, UrlMethodInfo methodInfo, Object[] args, Object result) {
		this.action = action;
		this.handleMethodInfo = methodInfo;
		this.methodArgs = args;
		this.actionResult = result;
		this.servletData = servletData;
	}
	
	public InterceptContext(ServletData servletData, Object action, UrlMethodInfo methodInfo, Object[] args, Object result,Exception e) {
		this.action = action;
		this.handleMethodInfo = methodInfo;
		this.methodArgs = args;
		this.actionResult = result;
		this.servletData = servletData;
		this.actionException = e;
	}

	/**
	 * @return the action
	 */
	public Object getAction() {
		return action;
	}

	/**
	 * @return the methodArgs
	 */
	public Object[] getMethodArgs() {
		return methodArgs;
	}

	/**
	 * @return the handleMethodInfo
	 */
	public UrlMethodInfo getHandleMethodInfo() {
		return handleMethodInfo;
	}

	/**
	 * @return the actionResult
	 */
	public Object getActionResult() {
		return actionResult;
	}

	public HttpServletRequest getHttpRequest(){
		return this.servletData.getRequest();
	}
	
	public HttpServletResponse getHttpResponse(){
		return this.servletData.getResponse();
	}

	public Exception getActionException() {
		return actionException;
	}

	public void setActionException(Exception actionException) {
		this.actionException = actionException;
	}
}