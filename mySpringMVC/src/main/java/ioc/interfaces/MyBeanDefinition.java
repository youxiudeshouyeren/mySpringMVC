package ioc.interfaces;

public interface MyBeanDefinition {

	 String SINGLETON="singleton";//单例模式
	 
	 String PROTOTYPE="prototype";//原型模式
	 
	 Class getClazz();//返回类型
	 
	 String getScope();//返回创建模式
	 
	 boolean isSingleton();//返回是否是单例模式
	 
	 boolean isPrototype();//返回是否是原型模式
	 
	 
}
