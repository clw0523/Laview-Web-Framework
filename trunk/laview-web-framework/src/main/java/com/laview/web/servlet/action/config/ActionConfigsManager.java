/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.laview.commons.collections.Lists;

/**
 * Actions 管理器
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionConfigsManager {

	private static final Logger logger = Logger.getLogger(ActionConfigsManager.class);
	
	/**
	 * 请求路径与处理类的映射
	 */
	private final static ConcurrentMap<String, List<ActionConfig>> actionConfigs = new ConcurrentHashMap<>();
	
	/**
	 * 请求<ActoinClassName, Action>的映射
	 */
	private final static ConcurrentMap<String, ActionConfig> actionIdMaps = 
							new ConcurrentHashMap<String, ActionConfig>();
	
	/**
	 *
	 * @param path
	 * @return
	 */
	public static List<ActionConfig> getActionConfig(String path) {
		logger.info("[LWF]==> 用来查找 ActionConfig 的Path[" + path + "]");
		return actionConfigs.get(path);
	}

	/**
	 *
	 * @param canonicalName
	 */
	public static ActionConfig getActionConfigByActionId(String actionId) {
		return actionIdMaps.get(actionId);
	}

	/**
	 *
	 * @param config
	 */
	public static void addActionConfig(ActionConfig config) {
		List<ActionConfig> configs = actionConfigs.get(config.getRequestPath());
		if(configs == null){
			configs = Lists.newArrayList();
			actionConfigs.put(config.getRequestPath(), configs);
		}
		configs.add(config);
		actionIdMaps.put(config.getActionId(), config);
	}

	public static String debugInfo(){
		return actionConfigs.toString();
	}
}