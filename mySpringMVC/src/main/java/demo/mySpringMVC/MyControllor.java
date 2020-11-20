package demo.mySpringMVC;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ioc.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ioc.interfaces.*;
import ioc.support.AnnotationApplicationContext;
import mvc.rest.RequestMethod;
import mvc.rest.RestRequestMapping;



@Scope(MyBeanDefinition.SINGLETON)
@Controller("MyController")
@RequestMapping("/web")
public class MyControllor {

    @Autowired("MyService")
    private MyService service;
    

    
    @RequestMapping("/test.do")
    @RestRequestMapping(method = RequestMethod.GET)
    public void testGet(HttpServletRequest req,HttpServletResponse resp,@RequestParam("word") String word) throws ServletException{
    	{
    		
    		try {
    			System.out.println("这是GET");
    			req.getRequestDispatcher("/WEB-INF/test2.jsp").forward(req, resp);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
        
        
    }
    
    
    @RequestMapping("/test.do")
    @RestRequestMapping(method = RequestMethod.PUT)
    public void testPut(HttpServletRequest req,HttpServletResponse resp,@RequestParam("word") String word) throws ServletException{
    	{
    		
    		try {
    			System.out.println("这是狗");
    			req.getRequestDispatcher("/WEB-INF/test2.jsp").forward(req, resp);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
        
        
    }
    
    
    @RequestMapping("/test.do")
    @RestRequestMapping(method = RequestMethod.DELETE)
    public void testDelete(HttpServletRequest req,HttpServletResponse resp,@RequestParam("word") String word) throws ServletException{
    	{
    		
    		try {
    			System.out.println("这是DELETE");
    			req.getRequestDispatcher("/WEB-INF/test2.jsp").forward(req, resp);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
        
        
    }
    
    
    @RequestMapping("/test.do")
    @RestRequestMapping(method = RequestMethod.POST)
    public void testPost(HttpServletRequest req,HttpServletResponse resp,@RequestParam("word") String word) throws ServletException{
    	{
    		
    		try {
    			
    			
    			System.out.println("这是你爹的posy");
    			req.getRequestDispatcher("/WEB-INF/test2.jsp").forward(req, resp);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
        
        
    }
    
    
    
    @RequestMapping("/view.do")
    public void view(HttpServletRequest req, HttpServletResponse res,@RequestParam("word") String word) throws ServletException, IOException{
    	//req.getRequestDispatcher(req.getContextPath()+"/test.jsp").forward(req, res);
    	ArrayList<String[]> list=new ArrayList<String[]>();
    	ApplicationContext context=new AnnotationApplicationContext("applicationContext.properties");
    	for(int i=0;i<10;i++) {
    		//MyStudent myStudent=(MyStudent) context.getBean("MyStudent",MyStudent.class);
    		MyStudent myStudent=new MyStudent();
    		myStudent.setClassString("计算机"+(i+1));
    		myStudent.setAge((i+5));
    		myStudent.setIdString(i+"");
    		myStudent.setNameString("张"+i);
    	     list.add(new String[] {myStudent.getIdString(),myStudent.getNameString(),myStudent.getAge()+"",myStudent.getClassString()});
    	}
    	req.setAttribute("list", list);
    	
    	
    	req.getRequestDispatcher("/WEB-INF/student.jsp").forward(req, res);
    	
    	//"/WEB-INF/jsp/view.jsp"
        
    }
    
    @RequestMapping("/file.do")
    public void fileTest(HttpServletRequest request, HttpServletResponse response,@RequestParam("word") String word) throws ServletException, IOException{
    	 try{
             response.setContentType("text/html;charset=utf-8");
//             创建DiskFileItemFactory工厂对象
             DiskFileItemFactory factory=new DiskFileItemFactory();
//             设置文件缓存目录，如果该文件夹不存在则创建一个
             File f=new File("C:\\TempFolder");
             if (!f.exists()){
                 f.mkdirs();
             }
             factory.setRepository(f);
//             创建ServletFileUpload对象
             ServletFileUpload fileUpload=new ServletFileUpload(factory);
//             设置字符编码
             fileUpload.setHeaderEncoding("utf-8");
//             解析request，将form表单的各个字段封装为FileItem对象
            List<FileItem> fileItems = fileUpload.parseRequest(request);
//             获取字符流
             PrintWriter writer=response.getWriter();
//             遍历List集合
             for (FileItem fileItem:fileItems) {
//             判断是否为普通字段
                 if (fileItem.isFormField()){
//                     获取字段名称
                     String name = fileItem.getFieldName();
                     if(name.equals("name")){
//                         如果字段值不为空
                         if (!fileItem.getString().equals("")){
                             String value=fileItem.getString("utf-8");
                             writer.print("上传者："+value+"<br />");
                         }
                     }
                 }
                 else {
                     //获取上传的文件名
                	
                     String filename=fileItem.getName();
//                     处理上传文件
                     if(filename!=null&&filename!=""){
                         writer.print("上传的文件名称是："+filename+"<br />");
//                         保持文件名唯一
                         filename=filename.replaceAll("\\\\", "_").replaceAll(":", "");
                       //  filename= UUID.randomUUID().toString()+"_"+filename;
                        
                         
                        String parentPathString="C:/Users/syr/Desktop/服务器测试文件上传/";
                        
                         String filepath=parentPathString+filename;
                         System.out.println(filepath+"这是目录");
                         //创建File对象
                         
                         File file=new File(filepath);
                         //创建文件夹
                         file.getParentFile().mkdirs();
                         //创建文件
                         file.createNewFile();
                         //获取上传文件流
                        
                         InputStream in= fileItem.getInputStream();
                        // InputStream in = (fileItem).getInputStream();
//                         使用 FileOutputStream打开服务器端的上传文件
                         FileOutputStream out=new FileOutputStream(file);
//                         流的对拷
                         byte[] bytes=new byte[1024];//每次读取一个字节
                         int len;
//                         开始读取上传文件的字节，并将其输出到服务器端的上传文件输出流中
                         while ((len=in.read(bytes))>0)
                         {
                        	 out.write(bytes,0,len);
                         }
                             
                         
                         out.flush();
                         in.close();
                         out.close();
                         fileItem.delete();
                         writer.print("文件上传成功！");
                     }
                 }
  
  
             }
         }catch (Exception e){
             throw new RuntimeException(e);
         }
     }
 
    }
   

