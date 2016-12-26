/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.support;

import java.util.List;

import com.laview.commons.collections.Lists;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping;
import com.laview.web.util.WebFrameWorkLogger;

/**
 *
 *
 * @author laview_chen
 * @since: v1.0
 */
public class BeforeMethodExecuteHelper extends BaseMethodExecuteHelper{

	/**
	 *
	 * @param mapping
	 * @return
	 */
	@Override
	protected boolean checkExecute(UrlInterceptorMethodMapping mapping) {
		return mapping != null && mapping.isBeforeMethod();
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.support.BaseMethodExecuteHelper#getExecuteMethodMappings()
	 */
	@Override
	protected List<UrlInterceptorMethodMapping> getExecuteMethodMappings(ActionExecuteContext actionContext) {
		return actionContext.getBeforeInterceptMethod();	
	}

}