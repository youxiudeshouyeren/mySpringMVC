package support;

import java.util.List;

import interfaces.BeanRegister;


/*
 * 创建bean并且添加到容器工厂
 */
public class BeanCreater {
	
   private BeanRegister register;
   
   public BeanCreater(BeanRegister register) {
	   this.register=register;
   }
   
   public void create(List<BeanDefinition> bds) {
	   for (BeanDefinition bd:bds) {
		   doCreate(bd);
	   }
   }
   
   private void doCreate(BeanDefinition bd) {
	   Object instanceObject=bd.getInstance();
	   this.register.registerInstancwMapping(bd.getId(), instanceObject);
   }
}
