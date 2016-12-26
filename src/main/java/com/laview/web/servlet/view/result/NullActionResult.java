/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

/**
 * 这是一个 NULL 值，表示找不到 url 与  method 的映射
 *
 * @author laview_chen
 * @since: v1.0
 */
public class NullActionResult implements ActionForward{

	public final static NullActionResult NULL = new NullActionResult();
	
	private NullActionResult(){
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.struts.action.view.result.ActionForwardResult#getView()
	 */
	@Override
	public String getView() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.struts.action.view.result.ActionForwardResult#isRedirect()
	 */
	@Override
	public boolean isRedirect() {
		return false;
	}

}