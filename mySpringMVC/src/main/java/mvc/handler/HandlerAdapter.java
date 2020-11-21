package mvc.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapter {
	//保存对应的RequestParam value 参数的位置
	
	private Map<String, Integer> paramType;
	
	
	public HandlerAdapter(Map<String,Integer> paramType) {
		this.paramType=paramType;
	}
	
	/*
	 * 具体调用的方法
	 */
	
	public void handle(HttpServletRequest req,HttpServletResponse resp,Handler handler) throws Exception {
		//获取要调用方法的全部参数类型
		Class<?>[] parametersType=handler.getMethod().getParameterTypes();
		
		//创建一个反射调用需要的参数值的数组，数组长度等于参数长度
		Object[] paramValues=new Object[parametersType.length];
		
		/**
         * 如果参数类型-->index  map里面有HttpServletRequest
         * 就在这个index下的数组赋值req
         */
		if(paramType.containsKey(HttpServletRequest.class.getName())) {
			paramValues[paramType.get(HttpServletRequest.class.getName())] = req;
		       
		}
		if (paramType.containsKey(HttpServletResponse.class.getName())) {
            paramValues[paramType.get(HttpServletResponse.class.getName())] = resp;
        }
		
		/**
         * 循环遍历RequestParam  value==>index
         * 如果拿到的value在请求参数里面有
         * 那么就从req中取出来赋值给数组
         * 
         */
		for(Map.Entry<String, Integer> entry:paramType.entrySet()) {
			String paramName=entry.getKey();
			Integer indexInteger=entry.getValue();
			//拿到请求对应的value
			
			String[] valueStrings=req.getParameterValues(paramName);
			if(valueStrings!=null&&valueStrings.length!=0) {
				
				String valueString=Arrays.toString(valueStrings).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
				//赋值给参数数组，并且把取出来的string类型转换为需要的参数的类型
				paramValues[indexInteger]=castValueType(valueString,parametersType[indexInteger]);
				
			}
		}

            handler.getMethod().invoke(handler.getController(), paramValues);
        }	
	
	
	
	
	
	private Object castValueType(String value, Class<?> clazz) {
        if(clazz == String.class){
            return value;
        }else if(clazz == Integer.class){
            return Integer.valueOf(value);
        }else if(clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else{
            return null;
        }
    }
	

	
	
	public Map<String, Integer> getParamType(){
		return paramType;
	}
	
	public void setParamType(Map<String, Integer> paramType) {
		this.paramType=paramType;
	}

}
