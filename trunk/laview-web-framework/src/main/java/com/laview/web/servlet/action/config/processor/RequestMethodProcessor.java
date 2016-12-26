/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.laview.commons.web.RequestMethod;
import com.laview.commons.web.UriUtils;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;

/**
 * @Post @Get @Put @Delete
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class RequestMethodProcessor {

	private final static Logger logger = Logger.getLogger(RequestMethodProcessor.class);

	/**
	 * 处理方法
	 *
	 * @param method
	 * @param annotation
	 */
	public void process(Method method, Annotation annotation) {
		ActionConfig config = ActionConfigsManager.getActionConfigByActionId(method.getDeclaringClass().getCanonicalName());
		if(config != null){
			String path = getRequestPath(method, annotation);
			config.addMethodMapping(UriUtils.addFirstSeparator(path), method, getRequestMethods());
			logger.debug( String.format("URL [%s] <------> %s.%s", UriUtils.addFirstSeparator(path), config.getType(), method.getName() ));
		}
	}

	/**
	 * 请求路径
	 *
	 * @return
	 */
	protected abstract String getRequestPath(Method method, Annotation annotation);
	
	/**
	 * 请求方法
	 *
	 * @return
	 */
	protected abstract RequestMethod[] getRequestMethods();
}