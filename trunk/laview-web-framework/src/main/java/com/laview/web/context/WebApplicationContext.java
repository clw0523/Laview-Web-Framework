/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.context;

import com.laview.container.beans.ApplicationContext;
import com.laview.container.core.BeanInfo;
import com.laview.web.servlet.action.config.ActionConfig;

/**
 * 继承容器的 ApplicationContext
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebApplicationContext extends ApplicationContext{
	
	private static WebApplicationContext instance;

	/**
	 * @param scanPackagePrefixs
	 */
	public WebApplicationContext(String[] scanPackagePrefixs) {
		super(scanPackagePrefixs);
	}

	/**
	 *
	 * @param array
	 */
	public static void createApplicationContext(String[] beanLocations) {
		instance = new WebApplicationContext(beanLocations);
	}

	public static WebApplicationContext getInstance(){
		return instance;
	}

	/**
	 * 使用  ActionConfig 来获取 Action 实现。这里会对 action 进行注入操作
	 *
	 * @param actionConfig
	 * @return
	 */
	public static Object getActionInstance(ActionConfig actionConfig) throws Exception{
		BeanInfo actionBean = actionConfig.getBeanInfo();
		
		return instance.getBeanBy(actionBean);
	}

}