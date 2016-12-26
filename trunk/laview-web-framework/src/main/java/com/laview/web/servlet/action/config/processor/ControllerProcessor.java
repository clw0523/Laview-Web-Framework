/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.lang.StringUtils;
import com.laview.web.annotation.springmvc.Controller;
import com.laview.web.servlet.action.ScanControllers;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;


/**
 * 处理 @Controller 注解
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ControllerProcessor extends BaseClassRequestAnnotation implements AnnotationProcessor<Class<?>>{
	
	/**
	 * @param scanControllers
	 */
	public ControllerProcessor(ScanControllers scanControllers) {
		super(scanControllers);
	}

	/* (non-Javadoc)
	 * @see com.laview.commons.annotation.process.AnnotationProcessor#process(java.lang.Object, java.lang.annotation.Annotation)
	 */
	@Override
	public void process(Class<?> clazz, Annotation annotation) {
		Controller controller = (Controller)annotation;
		
		if(StringUtils.stringIsEmpty(controller.path())) //如果 @Controller 没有定义 Path ，就跳过
			return;
		
		addActionConfig(controller.path(), clazz);
	}

}