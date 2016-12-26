/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.web.UriUtils;
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
public class BeforeAnnotationProcessor implements AnnotationProcessor<Method>{
	
	/* (non-Javadoc)
	 * @see com.laview.commons.annotation.process.AnnotationProcessor#process(java.lang.Object, java.lang.annotation.Annotation)
	 */
	@Override
	public void process(Method method, Annotation annotation) {
		ActionConfig config = ActionConfigsManager.getActionConfigByActionId(method.getDeclaringClass().getCanonicalName());
		if(config != null){
			Before before = (Before)annotation;
			config.addInterceptorMethod(method, before.value(), InterceptorMethodType.Before);
			WebFrameWorkLogger.debug( String.format("[添加 Before 方法拦截器]URL [%s] <------> %s.%s", UriUtils.addFirstSeparator(before.value()), config.getType(), method.getName() ));
		}
	}

}