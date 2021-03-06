package ioc.support;

import java.lang.reflect.Field;
import java.util.Map;

import ioc.annotation.Autowired;
import ioc.annotation.Scope;

/*
 * bean之间的依赖注入
 */
public class Populator {
	
  public Populator() {
	  
	
}
  //依赖注入
  public void populate(Map<String, Object> instanceMap) {
	  
	  //首先判断ioc容器中有没有东西
	  if(instanceMap.isEmpty())
		  return;
	  
	  //循环遍历每一个容器中的对象
	  for(Map.Entry<String, Object> entry:instanceMap.entrySet()) {
		  
		  //获取对象的字段 getDeclaredFields()返回Class中所有的字段，包括私有字段。
		  Field[] fields=entry.getValue().getClass().getDeclaredFields();
		  for(Field field:fields) {
			  if(!field.isAnnotationPresent(Autowired.class))
				  continue;
			  
			  Autowired autowire=field.getAnnotation(Autowired.class);
			  //后去字段要注入的id value  为空则按类名  接口名自动注入
			  String idString=autowire.value();
			  if(idString.equals("")) {
				  idString=field.getType().getName();
			  }
			  //f.setAccessible(true);得作用就是让我们在用反射时访问私有变量
			  field.setAccessible(true);
			  try {
				  //反射注入
				  
				  System.out.println("autowire 依赖注入"+"  "+idString+"   "+entry.getValue()+"   "+(instanceMap.get(idString)));
				  
				  //判断注入方式
				  
				  Class fieldClass=instanceMap.get(idString).getClass();
				  boolean flag=true;//默认是单例模式
				  if(fieldClass.isAnnotationPresent(Scope.class)) {
					  Scope scope=(Scope) fieldClass.getAnnotation(Scope.class);
					  if(scope.value().equals("prototype")) {
						  flag=false;
					  }
				  }
				  if(flag) {
					  field.set(entry.getValue(), instanceMap.get(idString));
				  }else {
					  field.set(entry.getValue(), ObjectUtil.objToObj(instanceMap.get(idString), instanceMap.get(idString).getClass()));
				  }
				  
				  
				  
			  }catch (Exception e) {
				// TODO: handle exception
				  e.printStackTrace();
				  System.out.println("Populator.java报错");
			}
		  }
	  }
	
}
}
