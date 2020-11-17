package support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.ApplicationContext;

import interfaces.BeanRegister;


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
	
	public Object getBeans(String id) {
		// TODO Auto-generated method stub
		return instanceMappingMap.get(id);
	}
	
	public Properties getConfig() {
        return this.config;
    }

	public <T> T getBean(String id, Class<T> clazz) {
		// TODO Auto-generated method stub
		 return (T)instanceMappingMap.get(id);
	}

	public Map<String, Object> getBeans() {
		// TODO Auto-generated method stub
		return instanceMappingMap;
	}

	public void registerBeanDefinition(List<BeanDefinition> bds) {
		// TODO Auto-generated method stub
		beanDefinitions.addAll(bds);
	}

	public void registerInstancwMapping(String id, Object instance) {
		// TODO Auto-generated method stub
		 instanceMappingMap.put(id,instance);
	}

}
