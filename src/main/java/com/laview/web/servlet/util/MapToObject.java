/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.laview.commons.convert.ConversionService;
import com.laview.commons.convert.ConversionServiceFactory;
import com.laview.commons.convert.GenericConverter;
import com.laview.commons.lang.ClassUtils;
import com.laview.commons.lang.NumberUtils;
import com.laview.commons.lang.PropertyUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.commons.lang.StringUtils;
import com.laview.web.upload.FormFile;

/**
 * 将 Map 的数据设置到 Object 的属性中去
 *
 * @author laview_chen
 * @since: v1.0
 */
public class MapToObject {

	private static final Logger log = Logger.getLogger(MapToObject.class);
	
	/**
	 * 属性设置黑名单：表示名单中的属性是禁止赋值
	 */
	private static final Set<String> blacklist = new HashSet<String>();
	
	static{
		blacklist.add("class");
	}
	
	/**
	 * 数据转换服务类
	 */
	private static final ConversionService conversionService = ConversionServiceFactory.createDefaultConversionService();
	
	private Map<String, Object> datas;
	
	private Object target;
	
	public MapToObject(Map<String, Object> datas, Object target){
		this.datas = datas;
		this.target = target;
	}
	
	/**
	 * 提供一个静态方法进行数据填充
	 *
	 * @param values
	 * @param argObject
	 */
	public static void filledWith(Map<String, Object> values, Object argObject) throws Exception{
		new MapToObject(values, argObject).convert();
	}
	
	/**
	 * 得到一个完成设置 的 Object
	 *
	 * @return
	 */
	public Object convert() throws Exception{
		
		//获取要注入对象的属性和值的迭代数组
		Set<Map.Entry<String, Object>> mapDatas = datas.entrySet();
		
		//下面对这个对象的属性进行逐一赋值
		for(Map.Entry<String, Object> entry: mapDatas){
			
			//从迭代数组中取属性名称
			String propertyName = entry.getKey();
			
			if(StringUtils.stringIsEmpty(propertyName))
				continue;
			
			//处理中括号的级联，laview 2017-01-15 如果是classItems[textbooks]则改为classItems.textbooks
			if(propertyName.contains("[") && propertyName.contains("]")){
				propertyName = propertyName.replace("]", "").replace("[", ".");
			}
			
			//取这个属性对应的值
			PropertyInfo propInfo = new PropertyInfo();
			propInfo.propertyName = propertyName;       //属性名
			propInfo.sourceValue  = entry.getValue();   // Request 中的值
			propInfo.targetType   = PropertyUtils.getPropertyType(target, propertyName); //属性类型
			propInfo.argObject = target; //要设置属性值的参数（参数对象）
							
			//将这个属性与值设置到对象，如果 属性类型 与值 的类型不一致，就要将值进行转换
			processPropertyValue(propInfo);
			
		}
		
		return target;
	}
	
	/**
	 * 对属性进行处理（联级性质的属性，例如 book.author.name），然后将值最终赋给终端的属性
	 *
	 * @param argObject
	 * @param propertyName
	 * @param argValues
	 */
	private void processPropertyValue(PropertyInfo propInfo) throws Exception{
		if(log.isDebugEnabled()){
			log.info(" * Process Property: " + propInfo.propertyName + ", value=" + propInfo.sourceValue);
		}
		
		//如果这个属性是一个对象（即联级属性表示方式，例如  book.author.name  中的 author.name, author 就是一个对象）
		int pos = propInfo.propertyName.indexOf(".");
		
		Object valueObject = propInfo.argObject;
		if(pos > 0){ //可能是联级属性，例如 book.author.name 之类
			
			//将这个属性再一次分拆
			String fieldName = propInfo.propertyName.substring(0, pos);
			String fieldProperty = propInfo.propertyName.substring(pos + 1);
			
			//从 要赋属性值的对象中，取这个属性的类型
			Field field = ReflectUtils.findField(valueObject.getClass(), fieldName, null);
			if(field != null){
				//尝试从对象中读取这个属性值 
				Object fieldObject = ReflectUtils.invokeReadMethod(valueObject, fieldName);
				//PropertyUtils.getPropertyValue(field, property, defaultValue)
				
				//这是目标对象的属性
				//如果这个属性值还没有，就需要创建一个，这样对能进行后续的，对此对象的属性进行赋值，这里是处理自定义类的属性，要对这样的属性赋值，首先就要创建一个对象
				if(fieldObject == null){
					//fieldObject = field.getDeclaringClass().newInstance();//这里是不是错的？
					fieldObject = field.getType().newInstance();
					ReflectUtils.invokeWriteMethod(valueObject, fieldName, new Object[]{fieldObject});
				}
				
				PropertyInfo info = new PropertyInfo();
				info.argObject = fieldObject;
				info.propertyName = fieldProperty;
				info.sourceValue = propInfo.sourceValue;
				info.targetType  = PropertyUtils.getPropertyType(fieldObject, fieldProperty);
				
				//对于联级属性，则要进入递归，将属性值赋给这个类 (使用递归方式赋值。。-_-! ..期望这个层次不要太深)
				processPropertyValue(info);
			}else {
				//没有这个属性，这是一个错误
				throw new NoSuchFieldException(" Can not find property [" + fieldName + "]...");
			}
		}else {
			//表示这只是一个单纯的属性，并没有联级属性 (如果没这个域，就不处理)
			//下面是真正的属性赋值
			//如果这是类属性，并且不在黑名单上，则就进行赋值
			if(!blacklist.contains(propInfo.propertyName.toUpperCase()) && ClassUtils.existsProperty(valueObject, propInfo.propertyName))
				setPropertyValue(propInfo);
		}
	}
	
	/**
	 * 给对象的属性 propertyName 进行赋值 values
	 *
	 * @param target
	 * @param propertyName
	 * @param values
	 * @throws Exception
	 */
	private void setPropertyValue(PropertyInfo propertyInfo) throws Exception {
		log.debug(" ** 要设置属性名称: " + propertyInfo.propertyName + ", 目标类名:" + propertyInfo.argObject.getClass().getName());
		
		Object resultValue = propertyInfo.sourceValue;
		String propertyName = propertyInfo.propertyName;
		Object target = propertyInfo.argObject;
		
		if(resultValue != null){
			//获取这个属性的类型
			Class<?> propertyType = propertyInfo.targetType;
			
			log.debug(" ** 要设置的属性类型：" + propertyType.getCanonicalName() + ", 传入数据的类型：" + 
						(resultValue == null ? "null":resultValue.getClass().getCanonicalName()) +
						"，要转换的数据值：" + resultValue);
			
			//if(!ReflectUtils.isInheritance(propertyInfo.targetType, FormFile.class)){
				//下面就要因应类型，将这个值转换成目标类型的值，这里需要建立一个转换体系(例如数值类型转换，日期类型转换等等)，不过目前暂时只对原始类型进行转换
				resultValue = converterValue(resultValue, propertyType);
			//}
			
			//显示最终的转换数据信息
			if(log.isDebugEnabled() && resultValue != null){
				log.info("最终转换的类型：[" + resultValue.getClass().getName() + "], 值：" + resultValue.toString());
			}
		}
		
		//未经转换，又或者经过转换之后，值是一个数组
		if(resultValue != null && resultValue.getClass().isArray()){
			
			ReflectUtils.invokeWriteMethod(target, propertyName, resultValue);
		}else{
			ReflectUtils.invokeWriteMethod(target, propertyName, new Object[]{resultValue});
		}
	}
	
	/**
	 * 将 请求值转换成 webBean 对应的属性值
	 *
	 * @param values  传入的请求值
	 * @param target  webBean 的属性类型
	 * @return
	 */
	private Object converterValue(Object values, Class target) throws Exception{
		Object result = values;
		
		if(values != null && target.equals(values.getClass())){
			return values;
		}
		
		//原始数据类型的转换，并且这个要转换的值不能是数组
		if(ClassUtils.isPrimitiveType(target) && !values.getClass().isArray()){
			if(log.isDebugEnabled())
				log.info("这是原始数据类型，使用 NumberUtils.toWrapperType 来转换值");
			
			result = NumberUtils.toWrapperType(result.toString(), target);
			if(result == null)
				result = NumberUtils.toWrapperType("0", target);
		}else if(ClassUtils.isPrimitiveWrapperType(target) && !values.getClass().isArray()){
			result = NumberUtils.toPrimitiveWrapperType(result.toString(), target);
			//如果是空串，得到result为null，就不要转0了，这里是Long Integer Double对象为null
			//if(result == null)
			//	result = NumberUtils.toPrimitiveWrapperType("0", target);
			
		}else if(target.isEnum()) {
			
			String tmp = (String)result;
			if(tmp != null && tmp.length() > 0){
				result = ClassUtils.getEnumBy(target, result.toString());
			}
			else
				result = null;
			
		}else if(target.isArray()){
			
			Class<?> tmpTarget = target.getComponentType();
			
			Object objArray = Array.newInstance(tmpTarget, 1);

			Array.set(objArray, 0, converterValue(result, tmpTarget));

			result = objArray;

		}else if(ReflectUtils.isInheritance(target, FormFile.class)){
			return values;
		}else{
			if(result != null){ //对数据进行转换
				if(log.isDebugEnabled())
					log.info("要使用类型转换器来进行数据转换");
				
				Object value = conversionService.convert(result, target);
				
				if(value != GenericConverter.NOT_CONVERTER){
					result = value;
				}
			}
		}
		return result;
	}

}