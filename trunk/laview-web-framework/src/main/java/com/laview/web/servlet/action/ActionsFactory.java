/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action;

import java.util.List;

import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;

/**
 * Action 管理与创建工厂
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionsFactory {
	
	/**
	 * 获取 请求路径的映射 Action.
	 * 
	 * 首先在缓存中查找，没找到就创建
	 *
	 * @param path
	 * @return
	 */
	public static List<ActionConfig> getActionInstance(String path) {
		
		return ActionConfigsManager.getActionConfig(path);
	}

}