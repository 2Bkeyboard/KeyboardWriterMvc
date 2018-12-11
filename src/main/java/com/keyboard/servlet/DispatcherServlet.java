package com.keyboard.servlet;

import com.keyboard.annotation.*;
import com.keyboard.controller.TomController;
import com.keyboard.util.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:48
 * @Description servlet核心类
 */
public class DispatcherServlet extends HttpServlet {

    //log
    private static final Logger log = new Logger(DispatcherServlet.class);

    //储存.class文件name
    private List<String> classObjectNames = new ArrayList<String>();

    //实例化后的bean
    private Map<String,Object> beans = new HashMap<String, Object>();

    //映射路径
    private Map<String,Object> handers = new HashMap<String, Object>();


    /** 为什么要配置要在web.xml:load-on-startup == 0
     *
     * 因为我们web项目都会加载到tomcat启动,因为我们配置了0,所以就会
     * 首先执行这个方法
     * @param config
     */
    @Override
    public void init(ServletConfig config){
        //1.扫描获取全类路径名:com.keyboard.controller.JerryController
        //扫描com.keyboard下所有的包,以及类
        doScan("com.keyboard");
        //实例化对象,将对象放入ioc容器:com.keyboard.controller.JerryController.class
        doInstance();
        //自动装配
        doAutowired();
        //映射url路径和方法建立关系
        doMapping();
    }

    /**
     * 建立url和方法的关系
     */
    public void doMapping(){
        for(Map.Entry<String,Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> classObject = instance.getClass();
            //判断是不是controller层
            if(classObject.isAnnotationPresent(JerryController.class)) {
                JerryRequestMapping jerryRequestMapping = classObject.getAnnotation(JerryRequestMapping.class);
                //拿到路径
                String classPath = jerryRequestMapping.value();
                //拿到方法
                Method[] methods = classObject.getMethods();
                //遍历当前方法是否被JerryRequestMapping注解
                for (Method method : methods){
                    if(method.isAnnotationPresent(JerryRequestMapping.class)){
                        JerryRequestMapping jerryRequestMapping1 = classObject.getAnnotation(JerryRequestMapping.class);
                        String methodPath = jerryRequestMapping1.value();
                        //  /jerry/query/ ---> query Method
                        //map的key是路径,value是方法
                        handers.put(classPath+methodPath,method);
                    }
                }
            }
        }
    }

    /**
     * 自动装配
     */
    public void doAutowired(){
        //遍历bean
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            Object instance = entry.getValue();
            Class<?> classObject = instance.getClass();
            //判断是不是controller层
            if(classObject.isAnnotationPresent(JerryController.class)){
                //判断哪些成员变量存在JerryAutowired注解
                Field[] fields = classObject.getDeclaredFields();
                //遍历fields是否用到JerryAutowired注解
                for(Field field : fields){
                    //判断当前成员变量上是否存在JerryAutowired注解
                    if(field.isAnnotationPresent(JerryAutowired.class)){
                        //根据key去map中拿到已实例化的对象
                        JerryAutowired jerryAutowired = field.getAnnotation(JerryAutowired.class);
                        String key = jerryAutowired.value();
                        //获取对象
                        Object value = beans.get(key);
                        //解除private等权限
                        field.setAccessible(true);
                        //设置值
                        try {
                            field.set(instance,value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        continue;
                    }
                }
            }
        }

    }

    /**
     * 实例化对象
     */
    public void doInstance(){
        //遍历类路径,并加载到map
        for(String className:classObjectNames){
            //com.keyboard.controller.JerryController.class
            //去除上面字符串最后的.class
            String cn = className.replace(".class","");
            //加载类对象
            try {
                Class<?> classObject = Class.forName(cn);
                //注解扫描
                if(classObject.isAnnotationPresent(JerryController.class)){//判断是不是Controller类
                    log.info("扫描到Controller类======>"+classObject.getName());
                    //map.put(key,instance); 容器放置对象,key适用@JerryRequestMapping中的值
                    //获取注解对象 模拟IOC容器
                    JerryRequestMapping jerryRequestMapping = classObject.getAnnotation(JerryRequestMapping.class);
                    String key = jerryRequestMapping.value();
                    //实例化对象
                    Object value = classObject.newInstance();
                    //放入"ioc"容器中
                    beans.put(key,value);
                } else if(classObject.isAnnotationPresent(JerryService.class)){//判断是不是Service类
                    log.info("扫描到Service类======>"+classObject.getName());
                    //map.put(key,instance); 容器放置对象,key适用@JerryService中的值
                    //获取注解对象 模拟IOC容器
                    JerryService jerryService = classObject.getAnnotation(JerryService.class);
                    String key = jerryService.value();
                    //实例化对象
                    Object value = classObject.newInstance();
                    //放入"ioc"容器中
                    beans.put(key,value);
                }else{
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描包
     * @param basePackage
     */
    public void doScan(String basePackage){
        //扫描编译好的所有类路径
        URL url = this.getClass().getClassLoader().
                getResource("/"+basePackage.replaceAll("\\.","/"));
        //url.getFile == com.keyboard
        log.info(url.getFile());
        //获取文件对象
        String fileStr = url.getFile();
        File file = new File(fileStr);
        //拿到com.keyboard下的所有.class
        String[] filesStr = file.list();
        //判断是文件还是文件夹
        for(String path : filesStr){
            File filePath = new File(fileStr+path);
            //判断是不是路径,不是路径的话一定是.class
            if(filePath.isDirectory()){
                //递归
                doScan(basePackage+"."+path);
            }else{
                //找到.class文件就记录下来
                //add("com./.../*.class")
                classObjectNames.add(basePackage+"."+filePath.getName());
            }
        }
    }

    private static Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method){
        //拿到当前执行的方法有哪些参数
        Class<?>[] paramClazzs = method.getParameterTypes();
        //根据参数个数,new一个参数的数组,将方法里所有的参数赋值到args里面去
        Object[] args = new Object[paramClazzs.length];
        int args_i = 0;
        int index = 0;
        for(Class<?> paramClass : paramClazzs){
            if(ServletRequest.class.isAssignableFrom(paramClass)){
                args[args_i++] = request;
            }
            if(ServletResponse.class.isAssignableFrom(paramClass)){
                args[args_i++] = response;
            }
            //从0-3判断有没有JerryRequestParam注解,当paramClass为0和1时,不是
            //当为2和3时@jerryRequestParam需要解析
            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if(paramAns.length > 0){
                for(Annotation paramAn : paramAns){
                    if(JerryRequestParam.class.isAssignableFrom(paramAn.getClass())){
                        JerryRequestParam jerryRequestParam = (JerryRequestParam) paramAn;
                        //找到注解中的name和age
                        args[args_i] = request.getParameter(jerryRequestParam.value());
                    }
                }
            }
            index++;
        }
        return args;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取到请求路径,包括 /jerry/query/
        String uri = req.getRequestURI();//获取到的请求路径 /KeyboardWriterMvc/jerry/query
        String context = req.getContextPath(); //获取到项目名 /KeyboardWriterMvc/
        String path = uri.replace(context,"");//拿到请求路径
        //路径对应的方法
        Method method = (Method) handers.get(path);
        //拿到对象
        TomController instance = (TomController) beans.get("/"+path.split("/")[1]);
        //执行
        Object args[] = hand(req,resp,method);
        try {
            method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
