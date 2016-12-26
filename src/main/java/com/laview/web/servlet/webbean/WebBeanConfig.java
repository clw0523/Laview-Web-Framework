/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.webbean;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.laview.commons.collections.Lists;
import com.laview.web.annotation.laview.ActionType;
import com.laview.web.annotation.laview.WebBean;

/**
 * 记录添加  @WebBean 注解的类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebBeanConfig {

	private final Class<?> beanClass;
	
	private ActionType actionType;
	
	/**
	 * 类的属性与注解信息映射表  <class_fieldname, fieldConfig>
	 */
	private final ConcurrentMap<String, FieldConfig> fieldConfigs = 
							new ConcurrentHashMap<String, FieldConfig>();
	
	/**
	 *
	 * @param field
	 * @param fieldConfig
	 */
	public void addFieldConfig(FieldConfig fieldConfig) {
		fieldConfigs.put(fieldConfig.getFieldName(), fieldConfig);
	}

	/**
	 * @param webBean 
	 * @param beanClass
	 */
	public WebBeanConfig(Class<?> beanClass, WebBean webBean) {
		this.beanClass = beanClass;
		this.actionType = webBean.action();
	}

	/**
	 *
	 * @return
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}

	/**
	 * 字段配置
	 * @return
	 */
	public List<FieldConfig> getFieldConfigs() {
		return Lists.newSimpleList(fieldConfigs.values());
	}

	/**
	 * @return the actionType
	 */
	public ActionType getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

}