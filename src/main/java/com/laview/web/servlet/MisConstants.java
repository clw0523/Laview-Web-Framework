/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet;

/**
 * 存放一些全局性的常量
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface MisConstants {

	/**
	 * ServletContextInterceptor 使用的常量
	 * 
	 * 这个常量，表示 web 系统的客户端请求路径的根路径。我们可以在 jsp 中用来替代 <c:url> 标签
	 */
	public final static String BASE_CONTEXT_PATH = "BaseContextPath";

	/**
	 *  ServletContextInterceptor 使用的常量
	 *  
	 *  表示 客户端的请求 IP，不过这个 ip 是从 request 中得到，只是一个参考值，不是准确值
	 *  
	 *  由于是放到 Request 中，因此，我们可以在 WebAction 的方法中使用
	 */
	public final static String CIENT_IP_ADDRESS = "_client_IpAddr_";
	
}