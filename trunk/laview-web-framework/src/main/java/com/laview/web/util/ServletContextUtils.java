/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 通过本类来取得 当前请求的 Request 和 Response 内容
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ServletContextUtils {

	/**
	 * 线程变量，存放请求上下文
	 */
	private static ThreadLocal<LocalContext> threadLocal = new ThreadLocal<LocalContext>(); 
	
	/**
	 * 设置
	 *
	 * @param request
	 * @param response
	 */
	public static void setServletContext(HttpServletRequest request, HttpServletResponse response) {
		threadLocal.remove();
		threadLocal.set(new LocalContext(request, response));
	}

	/**
	 * 移除
	 *
	 * @param request
	 * @param response
	 */
	public static void removeServletContext() {
		LocalContext context = threadLocal.get();
		if(context != null){
			context.release();
			threadLocal.remove();
		}
	}

	/**
	 * 当前请求的 Request
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		LocalContext context = threadLocal.get();
		if(context != null){
			return context.request;
		}
		return null;
	}
	
	/**
	 * 取 session
	 *
	 * @return
	 */
	public static HttpSession getSession(){
		LocalContext context = threadLocal.get();
		if(context != null){
			return context.request.getSession();
		}
		return null;
	}
	
	/**
	 * 当前请求的  Response
	 *
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		LocalContext context = threadLocal.get();
		if(context != null){
			return context.response;
		}
		return null;
	}
	
	private static class LocalContext{
		
		HttpServletRequest request;
		
		HttpServletResponse response;
		
		LocalContext(HttpServletRequest request, HttpServletResponse response){
			this.request = request;
			this.response = response;
		}

		/**
		 *
		 */
		public void release() {
			this.request = null;
			this.response = null;
		}
	}


}