/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.web.UriUtils;
import com.laview.web.annotation.laview.After;
import com.laview.web.annotation.laview.Before;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping.InterceptorMethodType;
import com.laview.web.util.WebFrameWorkLogger;

/**
 * @Before 注解的扫描处理器
 *
 * @author laview_chen
 * @since: v1.0
 */
public class AfterAnnotationProcessor implements AnnotationProcessor<Method>{
	
	/* (non-Javadoc)
	 * @see com.laview.commons.annotation.process.AnnotationProcessor#process(java.lang.Object, java.lang.annotation.Annotation)
	 */
	@Override
	public void process(Method method, Annotation annotation) {
		ActionConfig config = ActionConfigsManager.getActionConfigByActionId(method.getDeclaringClass().getCanonicalName());
		if(config != null){
			After after = (After)annotation;
			config.addInterceptorMethod(method, after.value(), InterceptorMethodType.After);
			WebFrameWorkLogger.debug( String.format("[添加 After 方法拦截器]URL [%s] <------> %s.%s", UriUtils.addFirstSeparator(after.value()), config.getType(), method.getName() ));
		}
	}

}