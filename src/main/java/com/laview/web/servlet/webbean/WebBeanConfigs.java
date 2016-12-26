/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.webbean;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * WebBean 配置
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebBeanConfigs {

	private final static ConcurrentMap<Class<?>, WebBeanConfig> configs =
								new ConcurrentHashMap<Class<?>, WebBeanConfig>();
	
	/**
	 * 判断 bean 类型是否添加 @WebBean 注解
	 *
	 * @param argType
	 * @return
	 */
	public static boolean beanIsWebBean(Class<?> argType) {
		return configs.containsKey(argType);
	}

	/**
	 *
	 * @param config
	 */
	public static void addConfig(WebBeanConfig config) {
		configs.put(config.getBeanClass(), config);
	}

	/**
	 * 得取 WebBean 配置
	 *
	 * @param argType
	 * @return
	 */
	public static WebBeanConfig getWebBeanConfig(Class<?> argType) {
		return configs.get(argType);
	}
}