/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.RequestMethod;
import com.laview.web.annotation.laview.Request;

/**
 * @Get 注解
 *
 * @author laview_chen
 * @since: v1.0
 */
public class RequestProcessor extends RequestMethodProcessor implements AnnotationProcessor<Method>{
	
	/* (non-Javadoc)
	 * @see com.laview.web.ioc.processor.RequestMethodProcessor#getRequestPath()
	 */
	@Override
	protected String getRequestPath(Method method, Annotation annotation) {
		Request request = (Request)annotation;
		return (StringUtils.stringIsEmpty(request.value())? method.getName() : request.value());
	}

	/* (non-Javadoc)
	 * @see com.laview.web.ioc.processor.RequestMethodProcessor#getRequestMethods()
	 */
	@Override
	protected RequestMethod[] getRequestMethods() {
		return new RequestMethod[]{RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT};
	}

}