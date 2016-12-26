/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;


/**
 * 专用于  @Before 和 @After 方法的信息记录
 *
 * @author laview_chen
 * @since: v1.0
 */
public class UrlInterceptorMethodMapping extends UrlMethodMapping{

	private InterceptorMethodType methodType;

	private ActionConfig actionConfig;
	
	/**
	 *
	 * @return
	 */
	public boolean isBeforeMethod() {
		return methodType == InterceptorMethodType.Before;
	}
	
	public boolean isAfterMethod(){
		return methodType == InterceptorMethodType.After;
	}
	
	/**
	 * @return the metodType
	 */
	public InterceptorMethodType getMetodType() {
		return methodType;
	}

	/**
	 * @param metodType the metodType to set
	 */
	public void setMetodType(InterceptorMethodType metodType) {
		this.methodType = metodType;
	}

	/**
	 * @return the actionConfig
	 */
	public ActionConfig getActionConfig() {
		return actionConfig;
	}

	/**
	 * @param actionConfig the actionConfig to set
	 */
	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UrlInterceptorMethodMappings [metodType=");
		builder.append(methodType);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}


	public static enum InterceptorMethodType{
		
		Before,
		
		After;
		
	}

}