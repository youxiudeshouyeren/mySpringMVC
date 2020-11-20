package mvc.handler;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import mvc.rest.RequestMethod;
import mvc.rest.RestRequestMapping;

public class Handler {
  //调用对应的Controll对象
	private Object controller;
	
	private RequestMethod restMethod;//rest风格的标识
	
	public RequestMethod getRestMethod() {
		return restMethod;
	}

	public void setRestMethod(RequestMethod restMethod) {
		this.restMethod = restMethod;
	}

		//URL匹配规则
		private Method method;
		
		//对应RequestMapping的url正则
		private Pattern pattern;
		
		  public Handler(Object controller, Method method, Pattern pattern) {
		        this.controller = controller;
		        this.method = method;
		        this.pattern = pattern;
		    }
	
	@Override
		public String toString() {
			return "Handler [controller=" + controller + ", restMethod=" + restMethod + ", method=" + method
					+ ", pattern=" + pattern + "]";
		}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	

	
}
