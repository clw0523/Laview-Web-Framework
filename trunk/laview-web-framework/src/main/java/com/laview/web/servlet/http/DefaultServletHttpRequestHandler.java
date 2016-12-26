/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.http;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.laview.commons.lang.Assert;
import com.laview.commons.lang.StringUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.view.result.ActionForward;
import com.laview.web.servlet.view.result.ActionResultHandler;

/**
 * 这是用来将资源文件转发给 省缺 Servlet 处理的过程。
 * 
 * 当我们在  web.xml 定义了  "*" 或 "/*" 这样的 Servlet URL Mapping 时，资源文件如  *.js, *.html
 * 
 * *.gif 等等，都会被定义的 Servlet 进行拦截处理，此时就要用本类将这些资源转给省缺的 Servlet 进行处理。
 *
 * @author laview_chen
 * @since: v1.0
 */
public class DefaultServletHttpRequestHandler implements ActionResultHandler{

	/** Default Servlet name used by Tomcat, Jetty, JBoss, and GlassFish */
	private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

	/** Default Servlet name used by Google App Engine */
	private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";

	/** Default Servlet name used by Resin */
	private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";

	/** Default Servlet name used by WebLogic */
	private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";

	/** Default Servlet name used by WebSphere */
	private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";


	private String defaultServletName;

	private ServletContext servletContext;


	/**
	 * Set the name of the default Servlet to be forwarded to for static resource requests.
	 */
	public void setDefaultServletName(String defaultServletName) {
		this.defaultServletName = defaultServletName;
	}
	
	/**
	 * If the {@code defaultServletName} property has not been explicitly set,
	 * attempts to locate the default Servlet using the known common
	 * container-specific names.
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		if (!Assert.hasText(this.defaultServletName)) {
			if (this.servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
				this.defaultServletName = COMMON_DEFAULT_SERVLET_NAME;
			}
			else if (this.servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null) {
				this.defaultServletName = GAE_DEFAULT_SERVLET_NAME;
			}
			else if (this.servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME) != null) {
				this.defaultServletName = RESIN_DEFAULT_SERVLET_NAME;
			}
			else if (this.servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null) {
				this.defaultServletName = WEBLOGIC_DEFAULT_SERVLET_NAME;
			}
			else if (this.servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null) {
				this.defaultServletName = WEBSPHERE_DEFAULT_SERVLET_NAME;
			}
			else {
				throw new IllegalStateException("Unable to locate the default servlet for serving static content. " +
						"Please set the 'defaultServletName' property explicitly.");
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#processActionResult(com.laview.web.servlet.ServletData, com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		RequestDispatcher rd = this.servletContext.getNamedDispatcher(this.defaultServletName);
		
		if (rd == null) {
			throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" +
					this.defaultServletName +"'");
		}
		try {
			rd.forward(servletData.getRequest(), servletData.getResponse());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#accept(java.lang.Object)
	 */
	@Override
	public boolean accept(Object actionResult) {
		return false;
	}

}