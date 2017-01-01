package com.laview.web.servlet.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.laview.commons.comparator.ExceptionDepthComparator;
import com.laview.commons.lang.ClassUtils;
import com.laview.commons.lang.ObjectUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.web.annotation.springmvc.ExceptionHandler;

/**
 * 
 * @Description: 在ControllerAdvice中查找最优方法匹配
 * @author laview
 * @date 2017年1月1日 下午3:53:57
 *
 */
public class AnnotationMethodHandlerExceptionResolver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis");

	private final Map<Class<?>, Map<Class<? extends Throwable>, Method>> exceptionHandlerCache =
			new ConcurrentHashMap<Class<?>, Map<Class<? extends Throwable>, Method>>(64);
	/**
	 * Finds the handler method that matches the thrown exception best.
	 * @param handler the handler object
	 * @param thrownException the exception to be handled
	 * @return the best matching method; or {@code null} if none is found
	 */
	public Method findBestExceptionHandlerMethod(Object handler, final Exception thrownException) {
		//final Class<?> handlerType = ClassUtils.getUserClass(handler);
		final Class<?> handlerType = handler.getClass();
		final Class<? extends Throwable> thrownExceptionType = thrownException.getClass();
		
		Method handlerMethod = null;

		Map<Class<? extends Throwable>, Method> handlers = this.exceptionHandlerCache.get(handlerType);
		if (handlers != null) {
			handlerMethod = handlers.get(thrownExceptionType);
			if (handlerMethod != null) {
				return (handlerMethod == NO_METHOD_FOUND ? null : handlerMethod);
			}
		}
		else {
			handlers = new ConcurrentHashMap<Class<? extends Throwable>, Method>(16);
			this.exceptionHandlerCache.put(handlerType, handlers);
		}

		final Map<Class<? extends Throwable>, Method> matchedHandlers = new HashMap<Class<? extends Throwable>, Method>();

		ReflectUtils.doWithMethods(handlerType, new ReflectUtils.MethodCallback() {
			@Override
			public void doWith(Method method) {
				method = ClassUtils.getMostSpecificMethod(method, handlerType);
				List<Class<? extends Throwable>> handledExceptions = getHandledExceptions(method);
				for (Class<? extends Throwable> handledException : handledExceptions) {
					if (handledException.isAssignableFrom(thrownExceptionType)) {
						if (!matchedHandlers.containsKey(handledException)) {
							matchedHandlers.put(handledException, method);
						}
						else {
							Method oldMappedMethod = matchedHandlers.get(handledException);
							if (!oldMappedMethod.equals(method)) {
								throw new IllegalStateException(
										"Ambiguous exception handler mapped for " + handledException + "]: {" +
												oldMappedMethod + ", " + method + "}.");
							}
						}
					}
				}
			}
		});

		handlerMethod = getBestMatchingMethod(matchedHandlers, thrownException);
		handlers.put(thrownExceptionType, (handlerMethod == null ? NO_METHOD_FOUND : handlerMethod));
		return handlerMethod;
	}
	
	/**
	 * Returns all the exception classes handled by the given method.
	 * <p>The default implementation looks for exceptions in the annotation,
	 * or - if that annotation element is empty - any exceptions listed in the method parameters if the method
	 * is annotated with {@code @ExceptionHandler}.
	 * @param method the method
	 * @return the handled exceptions
	 */
	@SuppressWarnings("unchecked")
	protected List<Class<? extends Throwable>> getHandledExceptions(Method method) {
		List<Class<? extends Throwable>> result = new ArrayList<Class<? extends Throwable>>();
		ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
		if (exceptionHandler != null) {
			if (!ObjectUtils.isEmpty(exceptionHandler.value())) {
				result.addAll(Arrays.asList(exceptionHandler.value()));
			}
			else {
				for (Class<?> param : method.getParameterTypes()) {
					if (Throwable.class.isAssignableFrom(param)) {
						result.add((Class<? extends Throwable>) param);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Uses the {@link ExceptionDepthComparator} to find the best matching method.
	 * @return the best matching method, or {@code null} if none found
	 */
	private Method getBestMatchingMethod(
			Map<Class<? extends Throwable>, Method> resolverMethods, Exception thrownException) {

		if (resolverMethods.isEmpty()) {
			return null;
		}
		Class<? extends Throwable> closestMatch =
				ExceptionDepthComparator.findClosestMatch(resolverMethods.keySet(), thrownException);
		Method method = resolverMethods.get(closestMatch);
		return ((method == null) || (NO_METHOD_FOUND == method)) ? null : method;
	}
	
	
}
