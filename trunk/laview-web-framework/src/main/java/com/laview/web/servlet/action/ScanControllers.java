/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action;

import com.laview.commons.annotation.process.AnnotationProcessorManager;
import com.laview.container.config.bean.ScanBean;
import com.laview.container.core.BeanInfo;
import com.laview.web.annotation.laview.After;
import com.laview.web.annotation.laview.Before;
import com.laview.web.annotation.laview.Delete;
import com.laview.web.annotation.laview.Get;
import com.laview.web.annotation.laview.Post;
import com.laview.web.annotation.laview.Put;
import com.laview.web.annotation.laview.Request;
import com.laview.web.annotation.laview.WebBean;
import com.laview.web.annotation.springmvc.Controller;
import com.laview.web.annotation.springmvc.RequestMapping;
import com.laview.web.servlet.action.config.processor.AfterAnnotationProcessor;
import com.laview.web.servlet.action.config.processor.BeforeAnnotationProcessor;
import com.laview.web.servlet.action.config.processor.ControllerProcessor;
import com.laview.web.servlet.action.config.processor.DeleteProcessor;
import com.laview.web.servlet.action.config.processor.GetProcessor;
import com.laview.web.servlet.action.config.processor.PostProcessor;
import com.laview.web.servlet.action.config.processor.PutProcessor;
import com.laview.web.servlet.action.config.processor.RequestMappingProcessor;
import com.laview.web.servlet.action.config.processor.RequestProcessor;
import com.laview.web.servlet.webbean.processor.WebBeanProcessor;

/**
 * 实现 laview.container 的 ScanBean，用来扫描 @Controller 和 @ServletInit 注解
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ScanControllers implements ScanBean{

	private BeanInfo theBeanInfo;
	
	/**
	 * 注解处理器，通过添加处理器方式来 处理注解，分散处理责任
	 */
	private final AnnotationProcessorManager processorManager = new AnnotationProcessorManager();
	
	public ScanControllers(){
		processorManager.addProcessor(Controller.class, new ControllerProcessor(this));
		processorManager.addProcessor(RequestMapping.class, new RequestMappingProcessor(this));
		
		processorManager.addProcessor(Get.class, new GetProcessor());
		processorManager.addProcessor(Post.class, new PostProcessor());
		processorManager.addProcessor(Put.class, new PutProcessor());
		processorManager.addProcessor(Delete.class, new DeleteProcessor());
		processorManager.addProcessor(Request.class, new RequestProcessor());
		processorManager.addProcessor(Before.class, new BeforeAnnotationProcessor());
		processorManager.addProcessor(After.class, new AfterAnnotationProcessor());
		
		//WebBean 注解
		processorManager.addProcessor(WebBean.class, new WebBeanProcessor());
	}
	
	
	/* (non-Javadoc)
	 * @see com.laview.container.config.bean.ScanBean#doWith(com.laview.container.core.BeanInfo)
	 */
	@Override
	public void doWith(BeanInfo bean) {
		//if(bean.beanIsAnnotationPresent(Controller.class)){
			theBeanInfo = bean;
			processorManager.forClass(bean.getBeanClass()).processAll();
			theBeanInfo = null;
		//}
	}

	public BeanInfo getTheBeanInfo(){
		return this.theBeanInfo;
	}
}