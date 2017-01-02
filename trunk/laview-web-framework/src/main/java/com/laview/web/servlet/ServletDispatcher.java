/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.laview.commons.web.RequestMethod;
import com.laview.container.eventbus.EventBusServiceFactory;
import com.laview.web.servlet.commons.GlobalConfig;
import com.laview.web.servlet.events.ServletDestroyEvent;
import com.laview.web.servlet.events.ServletInitEvent;
import com.laview.web.servlet.http.HttpServletData;

/**
 * 核心 Servlet
 *
 * @author laview_chen
 * @since: v1.0
 */
@WebServlet(name="ActionServlet", urlPatterns={"/"}, loadOnStartup=1)
public class ServletDispatcher extends HttpServlet{

	private final static Logger logger = Logger.getLogger(ServletDispatcher.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 进行请求分派
	 */
	private static DispatchManager dispatchManager;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("[LWF]==> Servlet Init...");
		initGlobalConfig(config);
		dispatchManager = new DispatchManager(config.getServletContext());
		addDispatchManagerToGlobalConfig();
		
		//执行框架使用者的初始化动作
		EventBusServiceFactory.post(new ServletInitEvent());
	}

	/**
	 * 初始化全局参数
	 * @param config
	 */
	private void initGlobalConfig(ServletConfig config) {
		GlobalConfig.initGlobalConfig(config);
	}
	private void addDispatchManagerToGlobalConfig() {
		GlobalConfig.setDispatchManager(dispatchManager);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		//Framework 释放事件
		EventBusServiceFactory.post(new ServletDestroyEvent());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.DELETE);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.GET);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.HEAD);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doOptions(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.OPTIONS);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.POST);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.PUT);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doTrace(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp, RequestMethod.TRACE);
	}

	/**
	 * 请用请求分派器对请求进行分派
	 *
	 * @param req
	 * @param resp
	 * @param methodType
	 */
	private void doRequest(HttpServletRequest req, HttpServletResponse resp, RequestMethod methodType){
		ServletData servletData = new HttpServletData(req, resp, methodType);
		dispatchManager.dispatch(servletData);
	}	
}