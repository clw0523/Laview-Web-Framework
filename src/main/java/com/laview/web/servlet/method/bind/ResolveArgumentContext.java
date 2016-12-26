/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.config.UrlMethodMapping;
import com.laview.web.servlet.action.config.UrlMethodMappings;

/**
 * 参数装填上下文。一个参数
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ResolveArgumentContext {
	
	private ServletData servletData;
	
	private UrlMethodMappings methodMapping;

	/**
	 * 参数的类型
	 */
	private Class<?> parameterType;
	
	private String parameterName;
	
	/**
	 * 与方法对应的请求路径
	 */
	private String requestMethodPath;
	
	/**
	 * PathVar Or Request Header Var Value
	 */
	private Object sourceValue;
	
	/**
	 *
	 * @param servletData
	 * @param methodMapping
	 * @param parameterType
	 * @param parameterName
	 * @return
	 */
	public static ResolveArgumentContext createBy(ServletData servletData, 
						UrlMethodMappings methodMapping, Class<?> parameterType, String parameterName) {
		
		ResolveArgumentContext result = new ResolveArgumentContext();
		result.servletData = servletData;
		result.methodMapping = methodMapping;
		result.parameterType = parameterType;
		result.parameterName = parameterName;
		
		return result;
	}

	/**
	 * 客户所提交的值
	 *
	 * @return
	 */
	public Object getSubmitValue(){
		if(sourceValue != null)
			return sourceValue;
		return servletData.getRequestParameter(parameterName);
	}
	
	/**
	 * 这个参数是不是，使用 URL 参数注入
	 *
	 * @return
	 */
	public boolean isPathVariable(){
		return methodMapping.checkIsPathVariable(parameterType, parameterName);
	}
	
	/**
	 * @return the servletData
	 */
	public ServletData getServletData() {
		return servletData;
	}

	/**
	 * @return the methodMapping
	 */
	public UrlMethodMappings getMethodMapping() {
		return methodMapping;
	}

	/**
	 * @return the parameterType
	 */
	public Class<?> getParameterType() {
		return parameterType;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 *
	 * @param requestMethodPath
	 */
	public void setRequestMethodPath(String requestMethodPath) {
		this.requestMethodPath = requestMethodPath;
	}

	/**
	 * @return the requestMethodPath
	 */
	public String getRequestMethodPath() {
		return requestMethodPath;
	}

	/**
	 *
	 * @param value
	 */
	public void setSourceValue(Object value) {
		this.sourceValue = value;
	}

	/**
	 * @return the pathVarValue
	 */
	public Object getSourceValue() {
		return sourceValue;
	}

	
}