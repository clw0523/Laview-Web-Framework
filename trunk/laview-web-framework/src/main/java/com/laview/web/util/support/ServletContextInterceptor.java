/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.util.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.commons.web.ServletUtils;
import com.laview.web.servlet.MisConstants;
import com.laview.web.servlet.action.iterceptor.AbstractInterceptor;
import com.laview.web.util.ServletContextUtils;

/**
 * 获取请求的上下文，并定义了请求的根目录变量：BaseContextPath。
 * 
 * 我们可以使用 ${BaseContextPath}/admin 来替代  <c:url value="/admin"/>
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ServletContextInterceptor extends AbstractInterceptor{

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.iterceptor.AbstractInterceptor#prepareProcess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean prepareProcess(HttpServletRequest request, HttpServletResponse response) {
		
		ServletContextUtils.setServletContext(request, response);
		request.setAttribute(MisConstants.BASE_CONTEXT_PATH, request.getContextPath());
		request.setAttribute(MisConstants.CIENT_IP_ADDRESS, ServletUtils.getIpAddr(request));
		
		return super.prepareProcess(request, response);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.iterceptor.AbstractInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object action, Exception exception) {
		super.afterCompletion(request, response, action, exception);
		
		ServletContextUtils.removeServletContext();
	}
	
}