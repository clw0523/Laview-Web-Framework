/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.webbean;

import com.laview.commons.lang.StringUtils;
import com.laview.web.annotation.laview.ActionType;
import com.laview.web.annotation.laview.FromType;
import com.laview.web.annotation.laview.WebAttribute;
import com.laview.web.annotation.springmvc.RequestHeader;

/**
 * WebBean 上每个 Field 的注解情况
 *
 * @author laview_chen
 * @since: v1.0
 */
public class FieldConfig {

	/**
	 * WebBean 注解类属性名
	 */
	private final String fieldName;
	
	/**
	 * 这是对应的 Web Attribute 名，如果不显式给出，就取 fieldName 为 attributeName
	 */
	private String attributeName;
	
	/**
	 * 值 来自于何方
	 */
	private FromType valueFrom;
	
	private String defaultValue;
	
	private ActionType actionType;
	
	/**
	 * @param name
	 */
	public FieldConfig(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 添加注解内容
	 *
	 * @param field
	 * @param annotation
	 */
	public void setWebAttribute(WebAttribute annotation) {
		if(valueFrom != null)
			throw new RuntimeException("[LWF WebAttribute FieldConfig]==> WebBean 属性注解，只能用 WebAttribute 与 RequestHeader 之一，不能同时使用两者。");
		valueFrom = annotation.from();
		attributeName = StringUtils.stringNotEmpty(annotation.name())?annotation.name():fieldName;
		defaultValue = StringUtils.stringNotEmpty(annotation.defaultValue())?annotation.defaultValue(): null;
		actionType = annotation.action();
	}

	/**
	 *
	 * @param annotation
	 */
	public void setWebAttribute(RequestHeader annotation) {
		
		if(valueFrom != null)
			throw new RuntimeException("[LWF WebAttribute FieldConfig]==> WebBean 属性注解，只能用 WebAttribute 与 RequestHeader 之一，不能同时使用两者。");
		
		valueFrom = FromType.Header;
		attributeName = StringUtils.stringNotEmpty(annotation.value())?annotation.value(): fieldName;
		defaultValue = StringUtils.stringNotEmpty(annotation.defaultValue())?annotation.defaultValue(): null;
		actionType = ActionType.Read;
	}
	
	/**
	 * 不添加任何注解时，默认为 request
	 *
	 * @param request
	 */
	public void setWebAttribute(FromType request) {
		valueFrom = request;
		attributeName = fieldName;
		defaultValue = null;
		actionType = ActionType.Read;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @return the valueFrom
	 */
	public FromType getValueFrom() {
		return valueFrom;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the actionType
	 */
	public ActionType getActionType() {
		return actionType;
	}

}