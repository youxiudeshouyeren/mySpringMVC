package ioc.support;

import java.util.ArrayList;
import java.util.List;

import ioc.annotation.Component;
import ioc.annotation.Controller;

public class BeanDefinitionGenerator {
 
	public static List<BeanDefinition> generate(String className) {
           try {
        	   Class clazz=Class.forName(className);
        	   String[] ids=generateIds(clazz);
        	   if(ids==null)
        		   return null;
        	   
        	   List<BeanDefinition> list=new ArrayList<BeanDefinition>();
        	   for(String id:ids) {
        		   list.add(new BeanDefinition(id,clazz));
        	   }
        	   return list;
           }catch(ClassNotFoundException e) {
        	   System.out.println("BeanDefinationGenerator 出错");
        	   e.printStackTrace();
           }
           return null;
	}
	
	 /**
     * 生成id数组
     * 1.带有@Controller 注解但是注解value没给值,@Controller一般没有
     * 接口定义,用类的全名作为id返回ids长度为1 
     * 2.@Component 没有value  获取所有的实现的接口,接口名为id,返货ids数组
     * 长度是实现的接口个数
     * 3.@Component 有value 返回id=value
     * 4.不带容器要实例化的注解  null
     */
	
		private static String[] generateIds(Class clazz) {
			String[] idStrings=null;
			//Controller类型的注解是否在clazz类上。


			if(clazz.isAnnotationPresent(Controller.class)) {
				Controller controller=(Controller) clazz.getAnnotation(Controller.class);
				if(controller.value().equals(""))
				{
					idStrings=new String[] {clazz.getName()};
				}
				else {
					idStrings=new String[] {controller.value()};
				}
			}
			else if(clazz.isAnnotationPresent(Component.class)) {
				//以对象的方式返回该注解
				Component component=(Component)clazz.getAnnotation(Component.class);
				String valueString=component.value();
				if(!valueString.equals("")) {
					idStrings=new String[] {valueString};
				}
				else {
					Class<?>[] interfacesClasses=clazz.getInterfaces();
					idStrings=new String[interfacesClasses.length];
					//如果这个类实现了接口，就用接口的类型作为id
					for(int i=0;i<interfacesClasses.length;i++) {
						idStrings[i]=interfacesClasses[i].getName();
					}
					
				}
				return idStrings;
				
			}
			return idStrings;
			
		}
	
	
}
