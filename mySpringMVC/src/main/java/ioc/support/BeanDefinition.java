package ioc.support;

import java.util.Objects;

import ioc.interfaces.MyBeanDefinition;

public class BeanDefinition implements MyBeanDefinition{
	
	private String id;//bean的id
	
	private Class clazz;//bean的类
	
	private String scope=BeanDefinition.SINGLETON;//默认为单例模式
	
	public BeanDefinition (String id,Class clazz) {
		this.id=id;
		this.clazz=clazz;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public void setScope(String scope) {
		this.scope=scope;
	}
	
	public Object getInstance() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public String getScope() {
		
		return this.scope;
	}

	@Override
	public boolean isSingleton() {
		
		return Objects.equals(scope, MyBeanDefinition.SINGLETON);
	}

	@Override
	public boolean isPrototype() {
		
		return Objects.equals(scope, MyBeanDefinition.PROTOTYPE);
	}

}
