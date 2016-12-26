/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.support;

import java.util.List;

import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping;

/**
 * do @After Method
 *
 * @author laview_chen
 * @since: v1.0
 */
public class AfterMethodExecuteHelper extends BaseMethodExecuteHelper{

	/**
	 *
	 * @param mapping
	 * @return
	 */
	@Override
	protected boolean checkExecute(UrlInterceptorMethodMapping mapping) {
		return mapping != null && mapping.isAfterMethod();
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.support.BaseMethodExecuteHelper#getExecuteMethodMappings(com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	protected List<UrlInterceptorMethodMapping> getExecuteMethodMappings(ActionExecuteContext actionContext) {
		
		return actionContext.getAfterInterceptMethod();
	}
	
}