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
public class DefaultJspForwardResult implements ActionForward{

	private final static Logger logger = Logger.getLogger(DefaultJspForwardResult.class);
	
	private final static String JSP_EXT = ".jsp";
	
	/**
	 * HTTP 协议前缀
	 */
	private final static String HTTP_PREFIX = "http://";
	
	private final String jspFile;
	
	private boolean redirect = false;
	
	DefaultJspForwardResult(final String jspFile){
		this(jspFile, false);
	}
	
	DefaultJspForwardResult(final String jspFile, final boolean redirect){
		this.jspFile = jspFile;
		this.redirect = redirect;
	}
	
	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionForwardResult#getView()
	 */
	@Override
	public String getView() {
		return view(jspFile);
	}

	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionForwardResult#isRedirect()
	 */
	@Override
	public boolean isRedirect() {
		return this.redirect;
	}

	private String view(String viewName){
		if(logger.isDebugEnabled())
			logger.info("Request JSP File=" + GlobalConfig.getViewRoot() + viewName + ".jsp");
		
		//如果是 redirect 到外部网站，就直接返回
		if(this.redirect && StringUtils.stringNotEmpty(viewName) && viewName.startsWith(HTTP_PREFIX))
			return viewName;
		
		if(!viewName.toLowerCase().endsWith(JSP_EXT)){
			viewName = viewName + ".jsp";
		}
		
		return GlobalConfig.getViewRoot() + viewName;
	}

}