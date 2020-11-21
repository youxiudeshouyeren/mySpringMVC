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
import java.util.Map;
import java.util.UUID;
import ioc.interfaces.*;
import ioc.support.AnnotationApplicationContext;
import mvc.rest.RequestMethod;
import mvc.rest.RestRequestMapping;



@Scope(MyBeanDefinition.SINGLETON)
@Controller("MyController")
@RequestMapping("/web")
public class MyControllor {

    
    @Autowired("MyDB")
    private StudentDB db;
    
    
    //首页 
    @RequestMapping("/test.do")
    public void testMVC(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	req.getRequestDispatcher("/WEB-INF/test.jsp").forward(req, res);
    	
    }
    
   
    
    //编辑分发 分为新增与更改
    @RequestMapping("/edit.do")
    public void editStudent(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	if(req.getParameter("id")==null) {
    		
    		
    		
    		req.getRequestDispatcher("/WEB-INF/addStu.jsp").forward(req, res);
    	}else {
    		
    	    MyStudent student=db.getStudent((String)req.getParameter("id"));
    	    req.setAttribute("student", student);
    		req.getRequestDispatcher("/WEB-INF/updateStu.jsp").forward(req, res);
    	}
    }
    
    
    //返回所有信息
    @RequestMapping("/view.do")
    @RestRequestMapping(method = RequestMethod.GET)
    public void viewALL(HttpServletRequest req, HttpServletResponse res,@RequestParam("word") String word) throws ServletException, IOException{
    	
    	ArrayList<MyStudent> students=db.qureyAll();
    	req.setAttribute("list", students);
    	
    	
    	req.getRequestDispatcher("/WEB-INF/student.jsp").forward(req, res);
    	
    	
        
    }
    
    @RequestMapping("/view.do")
    @RestRequestMapping(method = RequestMethod.POST)
    public void addStu(HttpServletRequest req, HttpServletResponse res,@RequestParam("word") String word) throws ServletException, IOException{
    	
    	MyStudent student=new MyStudent();
    	System.out.println((String)req.getParameter("classString"));
    	student.setClassString((String)req.getParameter("classString"));
    	student.setAge(Integer.parseInt((String)req.getParameter("age")));
    	student.setNameString((String)req.getParameter("nameString"));
    	student.setIdString((String)req.getParameter("uid"));
    	
    	db.addStudent(student);//添加
    	
    	ArrayList<MyStudent> students=db.qureyAll();
    	req.setAttribute("list", students);
    	
    	
    	req.getRequestDispatcher("/WEB-INF/student.jsp").forward(req, res);
    	
    	
        
    }
    
    @RequestMapping("/view.do")
    @RestRequestMapping(method = RequestMethod.DELETE)
    public void deleteStu(HttpServletRequest req, HttpServletResponse res,@RequestParam("word") String word) throws ServletException, IOException{
    	
    	db.deleteStudent((String)req.getParameter("id"));
    	
    	
    	
    	ArrayList<MyStudent> students=db.qureyAll();
    	req.setAttribute("list", students);
    	
    	
    	req.getRequestDispatcher("/WEB-INF/student.jsp").forward(req, res);
    	
    	
    	
        
    }
    
    @RequestMapping("/view.do")
    @RestRequestMapping(method = RequestMethod.PUT)
    public void updateStu(HttpServletRequest req, HttpServletResponse res,@RequestParam("word") String word) throws ServletException, IOException{
    	
    	
    	MyStudent student=db.getStudent((String)req.getParameter("id"));
    	student.setClassString((String)req.getParameter("classString"));
    	student.setAge(Integer.parseInt((String)req.getParameter("age")));
    	student.setNameString((String)req.getParameter("nameString"));
    	student.setIdString((String)req.getParameter("uid"));
    	
    	ArrayList<MyStudent> students=db.qureyAll();
    	req.setAttribute("list", students);
    	
    	
    	req.getRequestDispatcher("/WEB-INF/student.jsp").forward(req, res);
    	
    	
        
    }
    
    @RequestMapping("/file.do")
    public void fileTest(HttpServletRequest request, HttpServletResponse response,@RequestParam("word") String word) throws ServletException, IOException{
    	
    	if(request.getMethod().equalsIgnoreCase("get")) {
    		request.getRequestDispatcher("/WEB-INF/testFile.jsp").forward(request, response);
    	}else {
    		
    	
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
    }
   

