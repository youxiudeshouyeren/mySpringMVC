package ioc.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import ioc.annotation.Scope;
import ioc.interfaces.*;
import ioc.support.BeanDefinition;


public class AnnotationApplicationContext implements ApplicationContext,BeanRegister{
	//并发HashMap
	private Map<String, Object> instanceMappingMap=new ConcurrentHashMap<String, Object>();
    
	//保存所有bean的信息，包括bean的类型与id
	private List<BeanDefinition> beanDefinitions=new ArrayList<BeanDefinition>();
	
	//配置值文件的config，使用properties文件
	private Properties config=new Properties();
	
	public AnnotationApplicationContext(String location) {
		InputStream iStream=null;
		try {
			/*
			 * class是指当前类的class对象，getClassLoader()是获取当前的类加载器，什么是类加载器？
			 * 简单点说，就是用来加载java类的,类加载器负责把class文件加载进内存中，并创建一个java.lang.Class类的一个实例，
			 * 也就是class对象，并且每个类的类加载器都不相同。getResourceAsStream(path)是用来获取资源的，
			 * 而类加载器默认是从classPath下获取资源的，因为这下面有class文件吗，
			 * 所以这段代码总的意思是通过类加载器在classPath目录下获取资源.并且是以流的形式。
			 */
			//1.定位
			iStream=this.getClass().getClassLoader().getResourceAsStream(location);
			
			//2.载入
			config.load(iStream);
			System.out.println(config.toString());
			
			//3.注册
			register();
			
			//4.实例化
			createBean();
			
			//5.注入
			populate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				iStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//关闭流
		}
	}
	
	/**
     * 调用具体委派的注入类进行注入
     */
    private void populate() {
        Populator populator = new Populator();
        populator.populate(instanceMappingMap);
    }
    
    /**
     * 调用具体的创建对象创建bean
     */
    private void createBean() {
        BeanCreater creater = new BeanCreater(this);
        creater.create(beanDefinitions);
    }
    
    /**
     * 调用具体的注册对象注册bean信息
     */
    private void register() {
        BeanDefinitionParser parser = new BeanDefinitionParser(this);
        parser.parse(config);
    }
	

	public Properties getConfig() {
        return this.config;
    }

	public <T> T getBean(String id, Class<T> clazz) {
		
		
		Class thisClass=instanceMappingMap.get(id).getClass();
		boolean flag=true;//默认为单例模式
		
		if(thisClass.isAnnotationPresent(Scope.class)) {
			Scope scope=(Scope) thisClass.getAnnotation(Scope.class);
			if(scope.value().equals("prototype")) {
				flag=false;
			}
		}
		
		if(flag) {
			return (T) instanceMappingMap.get(id);//单例模式直接返回
		}else {
			try {
				return (T) ObjectUtil.objToObj(instanceMappingMap.get(id), instanceMappingMap.get(id).getClass());//返回深度拷贝的对象
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (T) instanceMappingMap.get(id); 
		
	}
	
	
	
	//根据传入的类寻找bean
	public <T> T getBean(Class<T> clazz) {
		T t=null;//要返回的bean
		if(clazz.getAnnotation(Scope.class).value().equals(MyBeanDefinition.SINGLETON)||clazz.getAnnotation(Scope.class)==null)
		{
			for(BeanDefinition beanDefinition:beanDefinitions) {
			if(beanDefinitions.getClass().getName().equals(clazz.getName())) {
				t=(T) instanceMappingMap.get(beanDefinition.getId()); //返回单例缓存中的bean
			}
		  }
		}
//		else {
//			
//			for(BeanDefinition beanDefinition:beanDefinitions) {
//				if(beanDefinitions.getClass().getName().equals(clazz.getName())) {
//					t=((T ? extends clo) instanceMappingMap.get(beanDefinition.getId())); //返回单例缓存中的bean的拷贝
//				}
//			  }
//		}
		return t;
	}

	public Map<String, Object> getBeans() {
		
		return instanceMappingMap;
	}

	public void registerBeanDefinition(List<BeanDefinition> bds) {
		
		beanDefinitions.addAll(bds);
	}

	public void registerInstancwMapping(String id, Object instance) {
		
		 instanceMappingMap.put(id,instance);
	}

	@Override
	public Object getBean(String id) {
		
		Class thisClass=instanceMappingMap.get(id).getClass();
		boolean flag=true;//默认为单例模式
		
		if(thisClass.isAnnotationPresent(Scope.class)) {
			Scope scope=(Scope) thisClass.getAnnotation(Scope.class);
			if(scope.value().equals("prototype")) {
				flag=false;
			}
		}
		
		if(flag) {
			return instanceMappingMap.get(id);//单例模式直接返回
		}else {
			try {
				return ObjectUtil.objToObj(instanceMappingMap.get(id), instanceMappingMap.get(id).getClass());//返回深度拷贝的对象
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instanceMappingMap.get(id); 
		
	}

}
