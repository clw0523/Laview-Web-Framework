/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import java.lang.reflect.Method;

import com.laview.commons.web.RequestMethod;

/**
 * 映射方法的配置数据，这是一个读接口，方便外部读取配置信息
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface UrlMethodInfo {

	/**
	 * Method 所对应的 请求路径
	 *
	 * @return
	 */
	public String getPath();
	
	/**
	 * 注解的 Get/Post ..等
	 *
	 * @return
	 */
	public RequestMethod[] getRequestMethods();
	
	/**
	 * 返回第几个参数的名称
	 *
	 * @param index
	 * @return
	 */
	public String getParameterNameBy(int index);
	
	/**
	 * 方法对象
	 *
	 * @return
	 */
	public Method getMethod();
	
	/**
	 * 返回值是否添加了 @ResponseBody 注解
	 *
	 * @return
	 */
	public boolean isResponseBody();
}