/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.commons.web.RequestContentType;
import com.laview.commons.web.RequestMethod;

/**
 * 对 请求数据 进行封装
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface ServletData {

	/**
	 * 获取当前请求的请求路径  pathInfo
	 *
	 * @return
	 */
	String getPathInfo();

	/**
	 *
	 * @return
	 */
	String getServletPath();

	/**
	 * getPathInfo() 与  getServletPath() 合并而成
	 * 
	 * @return
	 */
	String getRequestPath();

	/**
	 * 获取当前请求的请求方法，如  Get, Post 等
	 *
	 * @return
	 */
	RequestMethod getRequestMethod();

	/**
	 * 使用 名称 来获取请求参数 (request)值
	 *
	 * @param argName
	 * @return
	 */
	String getRequestParameter(String name);

	/**
	 * 使用名称 来获取 请求参数 值，这是数组形式的返回值
	 *
	 * @param argName
	 * @return
	 */
	String[] getRequestParameters(String argName);
	
	/**
	 * 设置 Request Attribute 数据
	 *
	 * @param name
	 * @param value
	 */
	void setRequestAttribute(String name, Object value);
	
	/**
	 *
	 */
	String debug();

	/**
	 *
	 * @return
	 */
	HttpServletRequest getRequest();

	/**
	 *
	 * @return
	 */
	HttpServletResponse getResponse();

	/**
	 *
	 * @return
	 */
	RequestContentType getContentType();

	/**
	 * 写数据到 Response
	 *
	 * @param actionResultAsString
	 */
	void writeResultToResponse(String content) throws Exception;

	/**
	 * 前转到一个 view;
	 *
	 * @param view
	 */
	void forward(String view) throws Exception;

	/**
	 *
	 * @param view
	 */
	void sendRedirect(String view) throws Exception;

	/**
	 * 请求是否为 MultipartRequest 请求，例如 文件上传的请求提交
	 *
	 * @return
	 */
	boolean isMultipartRequest();

	/**
	 * 获取 RequestHeader 的 Value
	 *
	 * @param name
	 */
	String getRequestHeader(String name);

	/**
	 * 向 RequestHeader 中设置数据
	 *
	 * @param name
	 * @param string
	 */
	void setRequestHeader(String name, String string);
	
	/**
	 * 从 session 中获取值
	 *
	 * @param attributeName
	 * @return
	 */
	Object getSessionValue(String attributeName);

	/**
	 * 向 session 中设置数据
	 *
	 * @param name
	 * @param value
	 */
	void setSessionAttribute(String name, Object value);

}