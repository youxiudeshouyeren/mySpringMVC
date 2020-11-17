package interfaces;

import java.util.List;

import support.BeanDefinition;

public interface BeanRegister {
	/*
	 * 向工厂内注册BeanDefinition
	 */
   void registerBeanDefinition(List<BeanDefinition> bds);
   
   /*/
    * 向工厂内注册bean实例对象
    */
   void registerInstancwMapping(String id,Object instance);
}
