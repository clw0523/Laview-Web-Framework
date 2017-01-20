/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.config.processor;

import org.apache.log4j.Logger;

import com.laview.web.servlet.action.ScanControllers;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;

/**
 * 定义在 Class 上的请求路径处理类的基类。
 * 
 * 这是为了减少重复而建立的基类，其子类为  ControllerProcessor 和 RequestMappingProcessor
 *
 * @author laview_chen
 * @since: v1.0
 */
public class BaseClassRequestAnnotation {

	private final static Logger logger = Logger.getLogger(BaseClassRequestAnnotation.class);
	
	private final ScanControllers scanControllers;
	
	public BaseClassRequestAnnotation(ScanControllers scanControllers){
		this.scanControllers = scanControllers;
	}
	
	protected void addActionConfig(String requestPath, Class<?> clazz){
//		ActionConfig config = ActionConfigsManager.getActionConfig(requestPath);
//		
//		if(config != null){
//			logger.error("[Laview-Web-Framework]==> URL[" + requestPath +"] 已经存在，将忽略这个映射");
//		}else{
			
			//如果path不是/开头，就加上
			if(!requestPath.startsWith("/")){
				requestPath = "/"+requestPath;
			}
		
			ActionConfig config = new ActionConfig(requestPath);
			config.setType(clazz.getCanonicalName());
			config.setActionId(clazz.getCanonicalName());
			config.setBeanInfo(scanControllers.getTheBeanInfo());
			
			ActionConfigsManager.addActionConfig(config);
			
			logger.debug("[Laview-Web-Framework]==> " + String.format("URL [%s] <------> %s", requestPath, clazz.getName()));
//		}		
	}
	

}