package ioc.support;

import java.util.List;

import ioc.interfaces.BeanRegister;


/*
 * 创建bean并且添加到容器工厂
 */
public class BeanCreater {
	
   private BeanRegister register;
   
   public BeanCreater(BeanRegister register) {
	   this.register=register;
   }
   
   //遍历BeanDefinition列表 调用生成函数
   public void create(List<BeanDefinition> bds) {
	   for (BeanDefinition bd:bds) {
		   doCreate(bd);
	   }
   }
   
   //生成bean
   private void doCreate(BeanDefinition bd) {
	   Object instanceObject=bd.getInstance();
	   this.register.registerInstancwMapping(bd.getId(), instanceObject);
   }
}
