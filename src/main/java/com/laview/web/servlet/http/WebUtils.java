/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.http;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebUtils {

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>If forwarded to via a RequestDispatcher, the current resource will see its
	 * own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
	public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
	public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
	public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

	
	public static void exposeForwardRequestAttributes(HttpServletRequest request) {
		exposeRequestAttributeIfNotPresent(request, FORWARD_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
		exposeRequestAttributeIfNotPresent(request, FORWARD_CONTEXT_PATH_ATTRIBUTE, request.getContextPath());
		exposeRequestAttributeIfNotPresent(request, FORWARD_SERVLET_PATH_ATTRIBUTE, request.getServletPath());
		exposeRequestAttributeIfNotPresent(request, FORWARD_PATH_INFO_ATTRIBUTE, request.getPathInfo());
		exposeRequestAttributeIfNotPresent(request, FORWARD_QUERY_STRING_ATTRIBUTE, request.getQueryString());
	}
	
	/**
	 * Expose the specified request attribute if not already present.
	 * @param request current servlet request
	 * @param name the name of the attribute
	 * @param value the suggested value of the attribute
	 */
	private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
		if (request.getAttribute(name) == null) {
			request.setAttribute(name, value);
		}
	}
	
}