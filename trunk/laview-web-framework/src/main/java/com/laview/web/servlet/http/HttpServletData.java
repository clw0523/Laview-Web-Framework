/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.http;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.laview.commons.collections.ArrayUtils;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.RequestContentType;
import com.laview.commons.web.RequestMethod;
import com.laview.commons.web.ServletUtils;
import com.laview.web.servlet.Cookies;
import com.laview.web.servlet.ServletData;
import com.laview.web.upload.MultipartRequestWrapper;

/**
 * 对 HTTP 请求数据进行封装
 *
 * @author laview_chen
 * @since: v1.0
 */
public class HttpServletData implements ServletData, Cookies {
	
	private static final Logger logger = Logger.getLogger(HttpServletData.class);

	private final static String REQ_HEADER_ACCEPT = "Accept";

	private HttpServletRequest request;

	private HttpServletResponse response;

	private RequestMethod methodType;
	
	private boolean multipartRequest = false;

	/**
	 * @param req
	 * @param resp
	 * @param methodType
	 */
	public HttpServletData(HttpServletRequest req, HttpServletResponse resp, RequestMethod methodType) {
		this.request = req;
		this.response = resp;
		this.methodType = methodType;
		
		//如果 request MultipartReqest，例如文件上传，则对 Request 进行技术处理
		processMultipart();
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getPathInfo()
	 */
	@Override
	public String getPathInfo() {
		return request.getPathInfo();
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getServletPath()
	 */
	@Override
	public String getServletPath() {
		return request.getServletPath();
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequestPath()
	 */
	@Override
	public String getRequestPath() {
		String path = getPathInfo();
		if(path == null)
			path = this.getServletPath();

		return path;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequestMethod()
	 */
	@Override
	public RequestMethod getRequestMethod() {
		return this.methodType;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequestParameter(java.lang.String)
	 */
	@Override
	public String getRequestParameter(String name) {
		return request.getParameter(name);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequestParameters(java.lang.String)
	 */
	@Override
	public String[] getRequestParameters(String name) {
		return request.getParameterValues(name);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequest()
	 */
	@Override
	public HttpServletRequest getRequest() {
		return this.request;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getResponse()
	 */
	@Override
	public HttpServletResponse getResponse() {
		return this.response;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getContentType()
	 */
	@Override
	public RequestContentType getContentType() {
		String contentType = request.getContentType();
		if(StringUtils.stringIsEmpty(contentType)){
			contentType = request.getHeader(REQ_HEADER_ACCEPT);
		}
		return RequestContentType.getBy(contentType);
	}

	/**
	 * 默认使用 utf-8
	 */
	public void setDefaultResponseEncoding() {
		String type = response.getContentType();
		if(type == null){
			response.setContentType(getContentType().getContentType() + "; charset=utf-8");
		}else{
			if(type.indexOf("charset=") < 0)
				response.setContentType(type + "; charset=utf-8");
		}
		response.setCharacterEncoding("utf-8"); 
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#writeResultToResponse(java.lang.String)
	 */
	@Override
	public void writeResultToResponse(String content) throws Exception{
		setDefaultResponseEncoding();
		response.getWriter().write(content);
		response.getWriter().flush();

	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#setRequestAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setRequestAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#forward(java.lang.String)
	 */
	@Override
	public void forward(String view) throws Exception{
		RequestDispatcher rd = request.getRequestDispatcher(view);
		if(rd != null)
			rd.forward(request, response);
		else
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "view");
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String view) throws Exception {
		response.sendRedirect(view);
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#isMultipartRequest()
	 */
	@Override
	public boolean isMultipartRequest() {
		return this.multipartRequest;
	}
	
	/**
	 * 对 MultipartRequest 进行技术处理
	 *
	 * @return
	 */
	private void processMultipart(){
		if(request != null && methodType == RequestMethod.POST){
			String contentType = request.getContentType();
	        if ((contentType != null)
	                && contentType.startsWith("multipart/form-data")) {
	        	request = new MultipartRequestWrapper(request);
	        	this.multipartRequest = true;
	        }
		}
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getRequestHeader(java.lang.String)
	 */
	@Override
	public String getRequestHeader(String name) {
		return request.getHeader(name);
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#getSessionValue(java.lang.String)
	 */
	@Override
	public Object getSessionValue(String attributeName) {
		HttpSession session = this.getRequest().getSession();
		if(session != null)
			return session.getAttribute(attributeName);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#setRequestHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void setRequestHeader(String name, String string) {
		response.setHeader(name, string);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#setSessionAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setSessionAttribute(String name, Object value) {
		request.getSession().setAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.ServletData#debug()
	 */
	@Override
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("* PathInfo:[" + request.getPathInfo() + "]\n");
		sb.append("* ServletPath:[" + request.getServletPath() + "]\n");
		sb.append("* Method:[" + request.getMethod() + "]\n");
		sb.append("* RequestURI:[" + request.getRequestURI() + "]\n");

		sb.append("* ServerName:[" + request.getServerName() + "]\n");
		sb.append("* ServerPort:[" + request.getServerPort() + "]\n");

		sb.append("* LocalName:[" + request.getLocalName() + "]\n");
		sb.append("* LocalAddr:[" + request.getLocalAddr() + "]\n");
		sb.append("* LocalPort:[" + request.getLocalPort() + "]\n");

		sb.append("* AuthType:[" + request.getAuthType() + "]\n");
		sb.append("* Protocol:[" + request.getProtocol() + "]\n");
		sb.append("* Scheme:[" + request.getScheme() + "]\n");

		sb.append("* RemoteUser:[" + request.getRemoteUser() + "]\n");
		sb.append("* RemoteAddr:[" + request.getRemoteAddr() + "]\n");
		sb.append("* RemoteHost:[" + request.getRemoteHost() + "]\n");
		sb.append("* RemotePort:[" + request.getRemotePort() + "]\n");
		
		sb.append("************************ DUMP Reuqest ******************\n");
		sb.append(dump(request));
		
		return sb.toString();
	}

	private String dump(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		
		for (Enumeration attrs = request.getAttributeNames(); attrs.hasMoreElements();) {
			String name = (String) attrs.nextElement();
			Object attr = request.getAttribute(name);
			sb.append(name).append(":[").append(attr).append("]").append("\n");
		}

		sb.append("Character encoding:[").append(request.getCharacterEncoding()).append("]\n");
		
		sb.append("Context length:[").append(String.valueOf(request.getContentLength())).append("]\n");
		sb.append("Content type:[").append(request.getContentType()).append("]\n");
		sb.append("Local address:[").append(request.getLocalAddr()).append("]\n");
		sb.append("Local port:[").append(String.valueOf(request.getLocalPort())).append("]\n");

		sb.append("Remote address:[").append(request.getRemoteAddr()).append("]\n");
		sb.append("Remote host:[").append(request.getRemoteHost()).append("]\n");
		sb.append("Remote port:[").append(String.valueOf(request.getRemotePort())).append("]\n");

		sb.append("Server name:[").append(request.getServerName()).append("]\n");
		sb.append("Server port:[").append(String.valueOf(request.getServerPort())).append("]\n");

		sb.append("Locale:[").append(request.getLocale()).append("]\n");

		{
			StringBuffer sb1 = new StringBuffer();
			for (Enumeration locales = request.getLocales(); locales.hasMoreElements();) {
				sb1.append(locales.nextElement());
				sb1.append(" ");
			}
			sb.append("Locales:[").append(sb1.toString().trim()).append("]\n");
		}
		
		Map<String, Object> paramters = ServletUtils.getParameters(request);
		
		sb.append("Parameter->").append(paramters).append("\n");
		
		sb.append("Protocol:[").append(request.getProtocol()).append("]\n");
		sb.append("Scheme:[").append(request.getScheme()).append("]\n");
		sb.append("Is secure?:[").append(String.valueOf(request.isSecure())).append("]\n");

		// HTTP

		sb.append("Method:[").append(request.getMethod()).append("]\n");
		sb.append("Auth Type:[").append(request.getAuthType()).append("]\n");

		for (Enumeration names = request.getHeaderNames(); names.hasMoreElements();) {
			String name = (String) names.nextElement();
			String value = request.getHeader(name);
			sb.append("Header->" + name + ":[").append(value).append("]\n");
		}

		sb.append("Path Info:[").append(request.getPathInfo()).append("]\n");
		sb.append("Path Translated:[").append(request.getPathTranslated()).append("]\n");
		sb.append("Query String:[").append(request.getQueryString()).append("]\n");
		sb.append("Remote User:[").append(request.getRemoteUser()).append("]\n");
		sb.append("Requested Session ID:[").append(request.getRequestedSessionId()).append("]\n");
		sb.append("Request URI:[").append(request.getRequestURI()).append("]\n");
		sb.append("Request URL:[").append(request.getRequestURL()).append("]\n");
		sb.append("Servlet Path:[").append(request.getServletPath()).append("]\n");
		sb.append("User Principal:[").append(request.getUserPrincipal()).append("]\n");

		return sb.toString();
	}

	//------------ Cookies 操作 -----------
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.Cookies#addCookie(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void addCookie(String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		if(maxAge > 0){
			cookie.setMaxAge(maxAge);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.Cookies#addCookie(java.lang.String, java.lang.String)
	 */
	@Override
	public void addCookie(String name, String value) {
		addCookie(name, value, Integer.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.Cookies#deleteCookie(java.lang.String)
	 */
	@Override
	public void deleteCookie(String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.Cookies#getCookie(java.lang.String)
	 */
	@Override
	public String getCookie(String name) {
		if(StringUtils.stringNotEmpty(name)){
			Cookie[] cookies = request.getCookies();
			if(ArrayUtils.notNullAndEmpty(cookies)){
				for(Cookie cookie: cookies){
					if(name.equals(cookie.getName())){
						return cookie.getValue();
					}
				}
			}
		}
		return null;
	}

}