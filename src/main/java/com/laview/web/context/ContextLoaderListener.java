/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.context;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.laview.commons.collections.Lists;
import com.laview.commons.lang.StringUtils;
import com.laview.container.beans.ApplicationContext;

/**
 * 使用 Web 的 Servlet Listener 来初始化 和启动 laview-container 
 *
 * @author laview_chen
 * @since: v1.0
 */
@WebListener("LWF Context Loader")
public class ContextLoaderListener implements ServletContextListener{

	private final static String DEFAULT_BEAN_LOCATION = "DefaultBeanLocation";
	
	private final static String CUSTOM_BEAN_LOCATION = "contextBeanLocation";
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String defaultName = event.getServletContext().getInitParameter(DEFAULT_BEAN_LOCATION);
		String name = event.getServletContext().getInitParameter(CUSTOM_BEAN_LOCATION);

		List<String> packages = Lists.newArrayList(defaultName.split(","));
		if(StringUtils.stringNotEmpty(name)){
			packages.addAll(Lists.newArrayList(name.split(",")));
		}
  
	    System.setProperty("ROOT", event.getServletContext().getRealPath("/"));  
		
		WebApplicationContext.createApplicationContext(packages.toArray(new String[packages.size()]));
	}

}

