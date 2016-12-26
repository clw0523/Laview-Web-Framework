/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

/**
 * 经过 ActionResultHandler 处理之后，最终返回的结果。 处理的返回结果
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface ActionForward {

	/**
	 * 返回 View 内容，例如返回 jsp 文件名称或所在路径等
	 *
	 * @return
	 */
	public String getView();

	/**
	 * redirect 设置
	 * 
	 * @return
	 */
	public boolean isRedirect();
}