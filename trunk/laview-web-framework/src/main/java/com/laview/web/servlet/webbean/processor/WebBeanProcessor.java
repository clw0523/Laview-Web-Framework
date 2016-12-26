/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.webbean.processor;

import java.lang.annotation.Annotation;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.web.annotation.laview.WebBean;
import com.laview.web.servlet.webbean.WebBeanConfigFactory;

/**
 * @WebBean 注解解析器
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebBeanProcessor implements AnnotationProcessor<Class<?>>{

	/* (non-Javadoc)
	 * @see com.laview.commons.annotation.process.AnnotationProcessor#process(java.lang.Object, java.lang.annotation.Annotation)
	 */
	@Override
	public void process(Class<?> t, Annotation annotation) {
		WebBean webBean = (WebBean)annotation;
		
		WebBeanConfigFactory.addWebBeanConfig(t, webBean);
	}

}