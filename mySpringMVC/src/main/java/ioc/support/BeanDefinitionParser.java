package ioc.support;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import ioc.interfaces.BeanRegister;

public class BeanDefinitionParser {
   //配置的扫描包的key
	public static final String SCAN_PACKAGE="scanPackage";
	
	//容器注册对象
	private BeanRegister register;
	
	public BeanDefinitionParser(BeanRegister register) {
		this.register=register;
	}
	
	//解析bean的信息
	public void parse(Properties properties) {
		//获取要扫描的包
		String packageName=properties.getProperty(SCAN_PACKAGE);
		
		//执行注册
		doRegister(packageName);
	}
	
	//注册BeanDefinition
	public void doRegister(String packageName) {
		
		System.out.println(packageName);
		//获取此包名下的绝对路径
		//C:\Users\syr\git\repository2\mySpringMVC\demo
		URL url=getClass().getClassLoader().getResource("./"+packageName.replaceAll("\\.","/"));
		System.out.println(url.toString());
		//URL url=getClass().getClassLoader().getResource("C:\\Users\\syr\\git\\repository2\\mySpringMVC\\demo)");
		File dirFile=new File(url.getFile());
		
		//循环遍历 递归找到所有的java文件
		for(File file:dirFile.listFiles()) {
			if(file.isDirectory()) {
				//若是文件夹 递归继续执行
				doRegister(packageName+"."+file.getName());
			}else {
				//处理文件名来获取类名 运行时获取的是class文件
				String className=packageName+"."+file.getName().replaceAll(".class", "").trim();
				//1.类带有容器要处理的注解，解析id生成BeanDefinition集合返回
				//2.不带有需要处理的注解   直接返回null
				
				
                List<BeanDefinition> definitions = BeanDefinitionGenerator.generate(className);
				
				if(definitions == null)
					continue;
                //调用容器的注册方法来完成bean信息的注册
				
				//将所有bean都加入该definitions
                this.register.registerBeanDefinition(definitions);
			}
		}
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             