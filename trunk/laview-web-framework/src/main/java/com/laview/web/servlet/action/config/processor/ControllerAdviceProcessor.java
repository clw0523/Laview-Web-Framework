package com.laview.web.servlet.action.config.processor;

import java.lang.annotation.Annotation;

import com.laview.commons.annotation.process.AnnotationProcessor;
import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ControllerAdviceManager;
import com.laview.web.util.WebFrameWorkLogger;

public class ControllerAdviceProcessor implements AnnotationProcessor<Class<?>> {

	@Override
	public void process(Class<?> t, Annotation annotation) {
		try {
			if(ReflectUtils.canNewInstance(t)){
				ControllerAdviceManager.setControlleradviceinstance(t.newInstance());
			}
		}catch (Exception e) {
			WebFrameWorkLogger.error("[LWF]===> 设置异常处理实例失败,异常：", e);
		}
	}

}
