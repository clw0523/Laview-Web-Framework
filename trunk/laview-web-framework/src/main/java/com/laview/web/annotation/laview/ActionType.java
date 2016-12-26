/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.annotation.laview;

/**
 * 操作
 *
 * @author laview_chen
 * @since: v1.0
 */
public enum ActionType {

	Read,
	
	Write,
	
	Both;

	/**
	 * 写操作
	 *
	 * @return
	 */
	public boolean isWrite() {
		return this == Both || this == Write;
	}
}