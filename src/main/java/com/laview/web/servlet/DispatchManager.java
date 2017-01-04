/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.laview.commons.collections.ArrayUtils;
import com.laview.commons.lang.PropertyUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.commons.web.path.PathPair;
import com.laview.container.context.annotation.Autowired;
import com.laview.container.core.BeanContainerFactory;
import com.laview.web.annotation.springmvc.ResponseBody;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.ActionExecuteProxy;
import com.laview.web.servlet.action.ActionResultProcessor;
import com.laview.web.servlet.action.ActionsFactory;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.ActionConfigsManager;
import com.laview.web.servlet.action.config.UrlMethodInfo;
import com.laview.web.servlet.action.config.UrlMethodMappings;
import com.laview.web.servlet.action.iterceptor.HandlerMethod;
import com.laview.web.servlet.action.iterceptor.RequestInterceptorFactory;
import com.laview.web.servlet.action.support.DefaultActionResultProcessor;
import com.laview.web.servlet.commons.GlobalConfig;
import com.laview.web.servlet.commons.WebResponseConstants;
import com.laview.web.servlet.http.DefaultServletHttpRequestHandler;
import com.laview.web.servlet.http.SimpleUrlHandlerMapping;
import com.laview.web.servlet.http.SimpleUrlHandlerMappingForHtml;
import com.laview.web.servlet.method.ActionMethodParameterHandler;
import com.laview.web.servlet.method.AnnotationMethodHandlerExceptionResolver;
import com.laview.web.servlet.view.result.ActionForward;
import com.laview.web.servlet.webbean.FieldConfig;
import com.laview.web.servlet.webbean.WebBeanConfig;
import com.laview.web.servlet.webbean.WebBeanConfigs;

/**
 * 请求分派器
 *
 * @author laview_chen
 * @since: v1.0
 */
public class DispatchManager {

	private final static Logger logger = Logger.getLogger(DispatchManager.class);
	
	/**
	 * 负责具体的 Action 执行与操作
	 */
	private ActionExecuteProxy executeProxy = new ActionExecuteProxy();
	
	/**
	 * 负责 Action 执行结果的处理
	 */
	private ActionResultProcessor actionResultProcessor = new DefaultActionResultProcessor();
	
	/**
	 * URL 映射
	 */
	private SimpleUrlHandlerMapping urlHandlerMapping = new SimpleUrlHandlerMapping();
	
	/**
	 * URL 映射
	 */
	private SimpleUrlHandlerMappingForHtml urlHandlerMappingForHtml = new SimpleUrlHandlerMappingForHtml();
	
	/**
	 * 默认处理
	 */
	private DefaultServletHttpRequestHandler resourceHandler = new DefaultServletHttpRequestHandler();
	
	
	private final AnnotationMethodHandlerExceptionResolver exceptionResolver = new AnnotationMethodHandlerExceptionResolver();
	
	/**
	 * 系统自己不处理静态资源的分发
	 * 
	 * @param servletContext
	 */
	public DispatchManager() {
	}

	/**
	 * @param servletContext
	 */
	public DispatchManager(ServletContext servletContext) {
		resourceHandler.setServletContext(servletContext);
	}
	
	/**
	 * 请求分派方法
	 *
	 * @param servletData
	 */
	public void dispatch(ServletData servletData) {
		logger.debug("[LWF]==> 请求参数：" + servletData.debug());
		logger.debug("[LWF]==> 映射：" + ActionConfigsManager.debugInfo());

		//看看请求是否为静态资源
		PathPair pp = urlHandlerMapping.findStaticResourceMappingPath(servletData);

		if(pp != null){

			doProcessStaticResource(servletData, pp);
		}else{
			if(interceptorNoStopRequest(servletData)){  //当前版本，只对动态资源进行拦截，不拦截静态资源
				//拦截以后，先看html开关，以及看看是不是html请求，如果是当做静态文件处理
				if(GlobalConfig.isOpenDirectToHtml() && isHtmlStaticMappingRequest(servletData)){
					logger.debug("[LWF]==> Html开关已打开,请求静态文件：path="+servletData.getRequestPath());
					doProcessStaticResource(servletData, null);
				}else{
					//真正的动态请求
					doProcessDynamicRequest(servletData);
				}
			}
		}
	}
	
	private boolean isHtmlStaticMappingRequest(ServletData servletData) {
		//看看请求是否为静态资源For Html
		PathPair pp2 = urlHandlerMappingForHtml.findStaticResourceMappingPath(servletData);
		if(pp2 != null){
			return true;
		}
		return false;
	}

	/**
	 * 触发拦截器，检查请求是否被拦截 
	 *
	 * @param servletData
	 * @return
	 */
	private boolean interceptorNoStopRequest(ServletData servletData) {
		return RequestInterceptorFactory.triggerPrepareProcess(servletData.getRequest(), servletData.getResponse());
	}

	/**
	 * 处理  .js, .gif ...的资源内容请求
	 *
	 * @param servletData
	 */
	private void doProcessStaticResource(ServletData servletData, PathPair pair){
		if(pair != null)
			logger.debug("[LWF]==> 静态文件请求：" + pair.getLaterPath() + "，全路径：" + pair.getPath());
		
		//DefaultServletHttpRequestHandler
		//resourceHandler.setServletContext(servletData.getRequest().getServletContext());
		resourceHandler.processActionResult(servletData, null);
	}	
	
	/**
	 * 处理请求
	 *
	 * @param servletData
	 */
	private void doProcessDynamicRequest(ServletData servletData){
		
		ActionExecuteContext actionContext = null;
		Exception exception = null;
		try{
			//获取要执行的 Action，并封装到 执行上、下文
			actionContext = getActionInstance(servletData);
			
			if(actionContext != null){
				
				//执行 Action，并获取返回结果
				executeAction(servletData, actionContext);
				
				if(!actionContext.hasMethodMapping()){
					
					if(GlobalConfig.isOpenDirectToJsp()){ //如果开启直接 jsp 映射，则进入进行判断
						checkActionIsDirectToJsp(servletData, actionContext);
					}
					
					if(!actionContext.hasActionResult()){
						//没有找到 请求的处理类，则交给默认的 处理单元
						logger.debug("[LWF]==> 没有找到请求的处理，RequestURI[" + servletData.getRequest().getRequestURI()+"]，返回: 404");
						
						actionContext.setActionResult(WebResponseConstants.SC_NOT_FOUND);
					}
				}
				
				triggerPostProcess(servletData, actionContext);

				//对 @WebBean 的注解渲染
				renderModelToView(servletData, actionContext);

				//对结果进行处理，并返回内容给客户端
				processActionForward(servletData, actionContext);
				
			}else{
				//没有找到 请求的处理类，则交给默认的 处理单元
				logger.debug("[LWF]==> 没有找到请求的处理，RequestURI[" + servletData.getRequest().getRequestURI()+"]，返回状态码：404");
				
				servletData.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			
		}catch(Exception e){
			//获取或创建 Action 失败
			//执行异常的处理
			processException(servletData, actionContext, e);
			exception = e;
		}
		
		triggerAfterCompletion(servletData, actionContext, exception);
	}

	/**
	 * 从 Action 工厂中获取 请求对应的处理 Action配置 
	 *
	 * @param servletData
	 * @return
	 */
	private ActionExecuteContext getActionInstance(ServletData servletData) {
		ActionExecuteContext result = null;
		
		String path = servletData.getRequestPath();
		
		if(path != null){
			PathPair pp = PathPair.createInstance(path);
			if(!pp.isPathDelimiter())
				pp.deleteLastPathDelimiter().deletePathDotSuffix();
			
			String sb = "/";
			List<ActionConfig> config = ActionsFactory.getActionInstance(pp.getPath());
			while(config == null && pp.hasPathDelimiter()){
				pp.splitLastPath().frontPathReplace();
				if(sb.length() == 1)  //TODO: 暂时用这个笨方法来得到方法的请求路径
					sb = "/" + pp.getLaterPath();
				else
					sb = "/" + pp.getLaterPath() + sb;
				config = ActionsFactory.getActionInstance(pp.getPath());
			}
			if(config != null){
				result = new ActionExecuteContext(config, servletData.getRequestMethod(), sb);
			}else{
				//检查是否为 直接映射到一个 JSP 文件，这种情况下，不存在 Action
				result = checkActionIsDirectToJsp(servletData);
			}
		}
		
		return result;
	}

	/**
	 * 判断 path 是否直接映射到 JSP 文件，对于一些简单的请求，可以不需要 action 而直接映射到  jsp。方便使用
	 * 
	 *  return null 表示不是， 返回一个非空值就是直接映射
	 *
	 * @param path
	 * @return
	 */
	private void checkActionIsDirectToJsp(ServletData servletData, ActionExecuteContext actionContext) {
		String path = servletData.getRequestPath();
		
		//将 request path 分拆成 路径与文件两部分
		PathPair pp = PathPair.createInstance(path).splitLastPath();
		
		String jspFile = String.format("%s%s__%s.jsp", 
							GlobalConfig.getViewAbstractPath(), 
							pp.getFrontPath(), 
							pp.getLaterPath()).replace("\\", "/");
		
		logger.debug("[LWF]==> 可能的 JSP 文件：" + jspFile);
		
		File file = new File(jspFile);
		if(file.exists()){
			actionContext.setActionResult(String.format("%s/__%s.jsp", pp.getFrontPath(), pp.getLaterPath()));
		}		
	}
	
	/**
	 * 判断 path 是否直接映射到 JSP 文件，对于一些简单的请求，可以不需要 action 而直接映射到  jsp。方便使用
	 * 
	 *  return null 表示不是， 返回一个非空值就是直接映射
	 *
	 * @param path
	 * @return
	 */
	private ActionExecuteContext checkActionIsDirectToJsp(ServletData servletData) {
		ActionExecuteContext result = new ActionExecuteContext();
		checkActionIsDirectToJsp(servletData, result);
		
		if(!result.hasActionResult()){
			return null;
		}else{
			return result;
		}
	}

	/**
	 * 执行 action 处理请求
	 *
	 * @param servletData
	 * @param instance
	 * @return
	 */
	private void executeAction(ServletData servletData, ActionExecuteContext actionContext) throws Exception{
		executeProxy.execute(servletData, actionContext);
	}
	
	/**
	 * 触发  拦截器的 postProcess 方法
	 *
	 * @param servletData
	 * @param actionContext
	 */
	private void triggerPostProcess(ServletData servletData, ActionExecuteContext actionContext) {
		RequestInterceptorFactory.triggerPostProcess(servletData.getRequest(), servletData.getResponse(), actionContext);
	}

	/**
	 *
	 * @param servletData
	 * @param actionContext
	 * @param e 
	 */
	private void processException(ServletData servletData, ActionExecuteContext actionContext, Exception e) {
		logger.debug("[LWF]==> 处理 Exception", e);
		//e.printStackTrace();
		
		logger.debug("[LWF]==> 匹配 Exception 交给@ControllerAdvice里面的@ExceptionHandler处理");
		
		try {
			Object controllerAdviceInstance = ControllerAdviceManager.getControlleradviceinstance();
			if(controllerAdviceInstance == null){
				logger.debug("[LWF]==> 没有找到@ControllerAdvice的实例");
				return;
			}
			//找到方法 method
			Method method = exceptionResolver.findBestExceptionHandlerMethod(controllerAdviceInstance, e);
			if(method == null){
				logger.debug("[LWF]==> 没有找到@ControllerAdvice实例的ExceptionHandler方法");
				return;
			}
			logger.debug("[LWF]==> 匹配 Exception 的方法是："+controllerAdviceInstance.getClass()+"."+method.getName());
			
			//获取参数方法 --- 根据方法参数 与 请求数据，将请求数据组装到成方法所需要的参数
			//Object[] args = resolveHandlerArgument(servletData, actionContext);
			ActionMethodParameterHandler methodInvoker = new ActionMethodParameterHandler(servletData);
			Object[] args = methodInvoker.resolveHandlerArgument(actionContext,method);
			
			Field[] fields = controllerAdviceInstance.getClass().getDeclaredFields();
			for(Field field:fields){
				if(field.isAnnotationPresent(Autowired.class)){
					field.setAccessible(true);
					if(field.get(controllerAdviceInstance) == null){
						field.set(controllerAdviceInstance, BeanContainerFactory.getBeanBy(field.getType()) );
					}
				}
			}
			//执行 Action 方法
			logger.debug("[LWF]==>执行ExceptionHandler的方法");
			Object actionResult = method.invoke(controllerAdviceInstance, args);
			
			actionContext.setActionResult(actionResult);
			
			Annotation responseBody = method.getAnnotation(ResponseBody.class);
			if(responseBody != null){
				actionContext.setExceptonHandlerHasResponseBody(true);
			}
			
			//对 @WebBean 的注解渲染
			renderModelToView(servletData, actionContext);
			
			//对结果进行处理，并返回内容给客户端
			processActionForward(servletData, actionContext);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("[LWF]==> ExceptionHandler处理出现异常",e1);
		}
		
	}

	/**
	 * 将返回的数据（要在  JSP 等 View 中渲染的数据）写入到 Request 中去
	 *
	 * @param servletData
	 * @param actionContext
	 */
	private void renderModelToView(ServletData servletData, ActionExecuteContext actionContext) {
		//获取方法的参数列表
		Object[] args = actionContext.getMethodArgs();
		
		if(ArrayUtils.notNullAndEmpty(args)){
			UrlMethodMappings methodMapping = actionContext.getUrlMethodMapping(); //.getMethodConfig(servletData.getRequestMethod());
			for(int i= 0; i < args.length; i++){
				if(args[i] != null){
					WebBeanConfig config = WebBeanConfigs.getWebBeanConfig(args[i].getClass());
					if(config != null){
						//整个 WebBean 是可写的，就写到 Request 里去		
						if(config.getActionType().isWrite()){
							String parameterName = methodMapping.getParameterNameBy(i);
							servletData.getRequest().setAttribute(parameterName, args[i]);
						}else{
							//如果 WebBean 不是整个可写，就要判断内部属性是否有可写的
							renderWebBeanPropertyToView(servletData, args[i], config);
						}
					}
				}
			}
		}
	}

	/**
	 * 将 WebBean 中的可写属性，写到 Request / Header /session 中去
	 *
	 * @param servletData  Request Data
	 * @param arg          WebBean Object
	 * @param config       Config
	 */
	private void renderWebBeanPropertyToView(ServletData servletData, Object arg, WebBeanConfig config) {
		
		List<FieldConfig> fieldConfigs = config.getFieldConfigs();
		for(FieldConfig fieldConfig: fieldConfigs){
			if(fieldConfig.getActionType().isWrite()){
				String name = fieldConfig.getAttributeName();
				Object value = PropertyUtils.getPropertyValue(arg, fieldConfig.getFieldName(), null);
				if(value != null){
					switch(fieldConfig.getValueFrom()){
						case Header:
							servletData.setRequestHeader(name, value.toString());
							break;
						case Request:
							servletData.setRequestAttribute(name, value);
							break;
						case Session:
							servletData.setSessionAttribute(name, value);
							break;
					}
				}
			}
		}
	}

	/**
	 * 委托给 ActionResultProcessor 进行处理
	 *
	 * @param servletData
	 * @param actionContext
	 * @param actionForward
	 */
	private boolean processActionForward(ServletData servletData, 
					ActionExecuteContext actionContext) throws Exception{
		
		boolean result = actionResultProcessor.process(servletData, actionContext);
		
		if(result){
			
			if(actionContext.getActionForward() != null){
				
				ActionForward af = actionContext.getActionForward();
				if(af.isRedirect()){
					servletData.sendRedirect(af.getView());
				}else{
					servletData.forward(af.getView());
				}
			}
			
		}
		return result;
		//else{
			//throw new ServletException("[LWF]==> 找不到请求[" + actionContext.getRequestPath() + "]的处理方法");
		//}
	}


	/**
	 * 整个请求处理完成之后，触发拦截器的完成方法
	 *
	 * @param servletData
	 * @param actionContext
	 * @param exception
	 */
	private void triggerAfterCompletion(ServletData servletData, ActionExecuteContext actionContext, Exception exception) {
		try{
			Object action = actionContext.getActionInstance();
			UrlMethodInfo methodInfo = (UrlMethodInfo)actionContext.getUrlMethodMapping();
			
			RequestInterceptorFactory.triggerAfterCompletion(
					servletData.getRequest(),
					servletData.getResponse(),
					(actionContext != null ? new HandlerMethod(action,methodInfo,actionContext.getMethodArgs(),actionContext.getActionResult()) : null),
					exception
					);
		}catch(Exception e){
			ReflectUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	public SimpleUrlHandlerMapping getUrlHandlerMapping() {
		return urlHandlerMapping;
	}

}