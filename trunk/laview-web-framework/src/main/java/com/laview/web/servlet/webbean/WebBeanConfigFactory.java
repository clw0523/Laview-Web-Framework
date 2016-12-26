/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.webbean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.laview.commons.lang.PropertyUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.web.annotation.laview.WebAttribute;
import com.laview.web.annotation.laview.FromType;
import com.laview.web.annotation.laview.WebBean;
import com.laview.web.annotation.springmvc.RequestHeader;

/**
 * 工厂类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebBeanConfigFactory {

	/**
	 *
	 * @param t
	 * @param webBean
	 */
	public static void addWebBeanConfig(Class<?> t, WebBean webBean) {
		WebBeanConfig config = new WebBeanConfig(t, webBean);		
		WebBeanConfigs.addConfig(config);
		
		scanWebBean(config);
	}

	/**
	 * 处理 WebBean 中的注解信息
	 *
	 * @param config
	 */
	private static void scanWebBean(WebBeanConfig config) {
		
		Class<?> beanClass = config.getBeanClass();
		Field[] fields = ReflectUtils.getAllCanModifiedProperty(beanClass);
		
		for(Field field: fields){
			
			//获得 Field 上的所有注解 （包括注解到  get 方法）
			Annotation[] annotations = PropertyUtils.getPropertyAnnotations(beanClass, field);
			FieldConfig fieldConfig = new FieldConfig(field.getName());
			if(annotations != null && annotations.length > 0){
				
				for(Annotation annotation: annotations){
					if(annotation.annotationType().equals(WebAttribute.class)){
						fieldConfig.setWebAttribute((WebAttribute)annotation);
					}else if(annotation.annotationType().equals(RequestHeader.class)){
						fieldConfig.setWebAttribute((RequestHeader)annotation);
					}
				}			
			}else{
				fieldConfig.setWebAttribute(FromType.Request);
			}
			
			config.addFieldConfig(fieldConfig);
		}		
	}

}