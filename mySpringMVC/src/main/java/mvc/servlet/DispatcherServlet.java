package mvc.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ioc.annotation.Controller;
import ioc.annotation.RequestMapping;
import ioc.annotation.RequestParam;
import ioc.support.AnnotationApplicationContext;
import mvc.handler.Handler;
import mvc.handler.HandlerAdapter;
import mvc.rest.RequestMethod;
import mvc.rest.RestRequestMapping;



public class DispatcherServlet extends HttpServlet{

	
	public static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
	
	private List<Handler> handlerMapping=new ArrayList<Handler>();
	
	private Map<Handler, HandlerAdapter> adapterMapping=new ConcurrentHashMap<Handler, HandlerAdapter>();
	
	@Override
	public void init() {
		
		//取出web.xml中配置的param参数
		
		String locationString=getInitParameter(CONTEXT_CONFIG_LOCATION);
		
		//创建ApplicationContext上下文，启动bean的解析 创建 注入等过程
		AnnotationApplicationContext context=new AnnotationApplicationContext(locationString);
		
	

        //解析url和Method的关联关系
        initHandlerMappings(context);
        //适配器（匹配的过程）
        initHandlerAdapters(context);

        
    

        System.out.println("GPSpring MVC is init.");
        
    }


    private void initHandlerAdapters(AnnotationApplicationContext context) {
    	
    	 System.out.println(handlerMapping.size());
        if (handlerMapping.isEmpty()) 
        	return;
        //遍历所有的handlerMapping
        
        System.out.println(handlerMapping.size());
        
        for (Handler handler : handlerMapping) {
            Method method = handler.getMethod();
            //创建一个保存RequestParam 注解的value(即参数名)==>index(参数位置索引)
            Map<String, Integer> paramType = new HashMap<String, Integer>();
            //获取所有的参数类型数组
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                //如果有HttpServletRequest类型就往map中保存 类型名==>index
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramType.put(type.getName(),i);
                }
            }

            //获取所有的参数注解,之所以返回二维数组,是因为每个参数可能有
            //多个注解修饰
            Annotation[][] pas = method.getParameterAnnotations();
            for (int i = 0; i < pas.length; i++) {
                //获取第i个参数的修饰注解数组
                Annotation[] pa = pas[i];
                //遍历每个参数的修饰注解
                for (Annotation a:pa){
                    if(a instanceof RequestParam){
                        String paramName = ((RequestParam) a).value();
                        if(!"".equals(paramName)){
                            //如果注解属于@RequestParam
                            //把注解参数 name==>index保存map
                            paramType.put(paramName,i);
                        }
                    }
                }
            }
            adapterMapping.put(handler,new HandlerAdapter(paramType));
        }

    }

    /**
     * 初始化handlerMappings
     * @param context
     */
    /**
     * 初始化handlerMappings
     * @param context
     */
    private void initHandlerMappings(AnnotationApplicationContext context) {
        //获取context中所有的bean(instancesMapping)实例对象数组
        Map<String, Object> beans = context.getBeans();
        if (beans.isEmpty()) 
        	return;
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            //对controller修饰的类做解析
            if (!entry.getValue().getClass().isAnnotationPresent(Controller.class)) 
            	continue;
            //获取类层面的url
            String url = "";
            Class<?> clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                url = clazz.getAnnotation(RequestMapping.class).value();
            }

            //再取对应方法上的url
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                if (!m.isAnnotationPresent(RequestMapping.class)) 
                	continue;
                String subUrl = m.getAnnotation(RequestMapping.class).value();
                String regex = (url + subUrl).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                //添加到handlerMapping中去
                Handler hadHandler=new Handler(entry.getValue(), m, pattern);
                
                if(m.isAnnotationPresent(RestRequestMapping.class)) {
                	hadHandler.setRestMethod(m.getAnnotation(RestRequestMapping.class).method());
                }//设置RequestMethod方法
                
                handlerMapping.add(hadHandler);
            }
        }
    }



    //servlet调用
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
        	req.setCharacterEncoding("UTF-8");
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //doPost
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	
    	
        //取出匹配的handler，通过之前维护的handleMapping获取handler
        Handler handler = getHandler(req);

        //根据handler取出HandlerAdapter
        HandlerAdapter ha = getHandlerAdapter(handler);

        //调用handle方法处理请求
        ha.handle(req,resp,handler);
    }

    private HandlerAdapter getHandlerAdapter(Handler handler) {
    	
    	System.out.println("上面是请求的handler");
        if(adapterMapping.isEmpty())
        	return null;
        
        for(Map.Entry<Handler, HandlerAdapter> e:adapterMapping.entrySet()) {
        	System.out.println(e.getKey().toString());
        }
        return adapterMapping.get(handler);
    }

    private Handler getHandler(HttpServletRequest req) {
    	
        if(handlerMapping.isEmpty())
        	return null;
        
        String contextPath = req.getContextPath();
        String url = req.getRequestURI();
        
        //获取请求的url  除去contextPath剩余的
        url = url.replace(contextPath,"").replaceAll("/+","/");
        
        System.out.println("url是"+url);
        
        //遍历handlermapping，找到url匹配的handler
        for (Handler handler:handlerMapping){
            if(handler.getPattern().matcher(url).matches()){
                //匹配到就把handler返回 
            	//将post请求转换为rest风格
            	if(req.getParameter("_method")!=null&&!req.getParameter("_method").equals("")&&req.getMethod().equalsIgnoreCase("POST")) {
            		String restmethodString=(String) req.getParameter("_method");
            		System.out.println(restmethodString+"获得的method");
            		RequestMethod remMethod = null;
            		switch (restmethodString) {
				
					case "put":
					{
						remMethod=RequestMethod.PUT;
						System.out.println("捕捉到PUT");
						
						break;
					}
					case "delete":
					{
						remMethod=RequestMethod.DELETE;
						System.out.println("捕捉到DELETE");
						
						break;
					}
					case "post":
					{
						remMethod=RequestMethod.POST;
						System.out.println("捕捉到POST");
						
						break;
					}
					default:
						remMethod=RequestMethod.GET;//没有匹配则为GET
					}
            		
            		
            		if(handler.getRestMethod()==remMethod) {
            			return handler;
            		}
            	}
            	
            	
            	
                
            }
        }
        
        for (Handler handler:handlerMapping){
            if(handler.getPattern().matcher(url).matches()){
            	if(req.getMethod().equalsIgnoreCase("GET"))
            	{
            		if(handler.getRestMethod()==RequestMethod.GET)
            			return handler;
            	}
            	
            }
            }
        
        for (Handler handler:handlerMapping){
            if(handler.getPattern().matcher(url).matches()){
            	
            	return handler;//如果上面没有匹配成功则从这个返回
            }
            }
        return null;
    }
}