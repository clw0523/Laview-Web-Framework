/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.ServletUtils;
import com.laview.web.annotation.springmvc.RequestMapping;
import com.laview.web.servlet.action.ScanControllers;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;


/**
 * 获取 @RequestMapping 注解信息
 *
 * @author laview_chen
 * @since: v1.0
 */
public class RequestMappingProcessor extends BaseClassRequestAnnotation implements AnnotationProcessor<Object>{

	private final static Logger logger = Logger.getLogger(RequestMappingProcessor.class);
	
	/**
	 * @param scanControllers
	 */
	public RequestMappingProcessor(ScanControllers scanControllers) {
		super(scanControllers);
	}

	/* (non-Javadoc)
	 * @see com.laview.commons.annotation.process.AnnotationProcessor#process(java.lang.Object, java.lang.annotation.Annotation)
	 */
	@Override
	public void process(Object object, Annotation annotation) {
		RequestMapping requestMapping = (RequestMapping)annotation;
		//laview 2017-01-16
		String requestMapping_value = requestMapping.value();
		if(StringUtils.isEmpty(requestMapping_value) && StringUtils.notEmpty(requestMapping.path())){
			requestMapping_value = requestMapping.path();
		}
		if(object instanceof Method){
			Method method = (Method)object;
			ActionConfig config = ActionConfigsManager.getActionConfigByActionId(method.getDeclaringClass().getCanonicalName());
			if(config != null){
				config.addMethodMapping(ServletUtils.deletePathDotSuffix(requestMapping_value), method, requestMapping.method());
				logger.debug( String.format("URL [%s] <------> %s.%s", ServletUtils.deletePathDotSuffix(requestMapping_value), config.getType(), method.getName() ));
			}
		}else if(object instanceof Class){
			Class<?> clazz = (Class<?>)object;

			addActionConfig(requestMapping_value, clazz);
		}
		
		/*//原来代码
		 * if(object instanceof Method){
			Method method = (Method)object;
			ActionConfig config = ActionConfigsManager.getActionConfigByActionId(method.getDeclaringClass().getCanonicalName());
			if(config != null){
				config.addMethodMapping(ServletUtils.deletePathDotSuffix(requestMapping.value()), method, requestMapping.method());
				logger.debug( String.format("URL [%s] <------> %s.%s", ServletUtils.deletePathDotSuffix(requestMapping.value()), config.getType(), method.getName() ));
			}
		}else if(object instanceof Class){
			Class<?> clazz = (Class<?>)object;

			addActionConfig(requestMapping.value(), clazz);
		}*/
	}

}