/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import org.apache.log4j.Logger;

import com.laview.commons.lang.StringUtils;
import com.laview.web.servlet.commons.GlobalConfig;



/**
 * 返回一个 JSP 文件的简单的  ActionForwardResult 实现
 *
 * @author laview_chen
 * @since: v1.0
 */
public class DefaultHtmlForwardResult implements ActionForward{

	private final static Logger logger = Logger.getLogger(DefaultHtmlForwardResult.class);
	
	private final static String HTML_EXT = ".html";
	
	/**
	 * HTTP 协议前缀
	 */
	private final static String HTTP_PREFIX = "http://";
	
	private final String htmlFile;
	
	private boolean redirect = false;
	
	public DefaultHtmlForwardResult(final String jspFile){
		this(jspFile, false);
	}
	
	public DefaultHtmlForwardResult(final String htmlFile, final boolean redirect){
		this.htmlFile = htmlFile;
		this.redirect = redirect;
	}
	
	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionForwardResult#getView()
	 */
	@Override
	public String getView() {
		return view(htmlFile);
	}

	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionForwardResult#isRedirect()
	 */
	@Override
	public boolean isRedirect() {
		return this.redirect;
	}

	private String view(String viewName){
		
		//如果是 redirect 到外部网站，就直接返回
		if(this.redirect && StringUtils.stringNotEmpty(viewName) && viewName.startsWith(HTTP_PREFIX)){
			if(logger.isDebugEnabled())
				logger.info("Request HTML File=" + viewName);
			return viewName;
		}
		if(!viewName.toLowerCase().endsWith(HTML_EXT)){
			viewName = viewName + ".html";
		}
		if(logger.isDebugEnabled())
			logger.info("Request HTML File=" + GlobalConfig.getViewRoot() + viewName);
		
		return GlobalConfig.getViewRoot() + viewName;
	}

}