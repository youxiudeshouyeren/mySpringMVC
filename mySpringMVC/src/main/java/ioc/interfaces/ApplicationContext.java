package ioc.interfaces;

import java.util.Map;

public interface ApplicationContext {
 /*
  * 根据id返回bean
  */
	Object getBean(String id);
	
	
	/*
	 * 根据id获取特定类型的bean，强制类型转换
	 */
	<T>T getBean(String id,Class<T> clazz);
	
	/*
	 * 获取工厂内的所有bean集合
	 */
	Map<String,Object> getBeans();
}
