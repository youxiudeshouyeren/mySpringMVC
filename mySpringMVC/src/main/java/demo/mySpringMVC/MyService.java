package demo.mySpringMVC;
import ioc.support.*;
import java.lang.reflect.Field;

import ioc.annotation.Autowired;
import ioc.annotation.Component;
import ioc.annotation.Scope;
import ioc.interfaces.ApplicationContext;


@Component("MyService")
@Scope("prototype")
public class MyService {
	  @Autowired("demo.mySpringMVC.MyStudent")
	private MyStudent student1;
	  
	  @Autowired("demo.mySpringMVC.MyStudent")
	private MyStudent student2;
	  
	  @Autowired("MyController")
	  private MyControllor testController;
	  
    public  void say(String s){
    	for(Field field:this.getClass().getDeclaredFields()) {
    		System.out.println(field.toString());
    	}
        System.out.println(s);
    }
    
    
    
    public static void main(String[] args) {
		ApplicationContext context=new AnnotationApplicationContext("applicationContext.properties");
    	MyService myService=context.getBean("MyService",MyService.class);
    	
	}
}
