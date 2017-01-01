package com.laview.web.servlet;

public class ControllerAdviceManager {

	/**
	 * 异常处理类，目前只接受一个
	 */
	private static Object controllerAdviceInstance = null;

	public static Object getControlleradviceinstance() {
		return controllerAdviceInstance;
	}
	
	public static void setControlleradviceinstance(Object instance) {
		if(controllerAdviceInstance == null){
			controllerAdviceInstance = instance;
		}
	
	}
}
