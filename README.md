# Laview-Web-Framework
This framework similar to spring mvc,It is based on servlet3！


## 1. 本系统建立两套注解

###	1.1 模拟 SpringMVC 的注解
	
###	1.2 第二套注解为本框架自有的注解
	
   * class 上的注解沿用第一套  @Controller 这个，但是 path 里面可以使用正则表达式
		
		*) 方法上的注解：
		
			*)不加任何注解：
			
				*） 方法名为 url 最后一级名称
				
				*）方法支持所有请求操作（GET, POST, PUT, DELETE, OPTIONS)
		 
		 	*)不带参数的注解：
		 	
		 		@Get --- GET 请求操作
		 		
		 		@Post --- POST 请求操作
		 		
		 		@Delete --- Delete 请求操作 
		 		
		 		@Put --- put 请求操作
		 	
		 		*) 方法名为 url 最后一级名称
		 		
		 	*)带参数的注解：
		 	
		 		@Get("/project")
		 	
### 1.3 方法级别上的拦截方法	
	
   * **@Before**  --- 在执行请求方法之前执行的方法

     + 注解中写出要拦截的请求路径。只要路径匹配，那么在执行处理方法之前，执行这个注解所注解的方法

     + 如果路径为空，则表示拦截由此类 Controller 中声明的路径前缀下的所有请求方法

     + @Before 所注解的方法所作的要求或规定如下：

        - 方法的参数只能有三种形式：

           * 不带任何参数

           * 带与所拦截方法一样的参数

           * 只有 Request 和 Response 两个参数

        - 方法的返回值只能有两种情况：

           * 没有返回值

           * 返回 Boolean 类型：如果是 true 表示继续执行请求处理方法，如果是 false 则跳过不执行请求处理方法
          
           * 其他返回值将忽略

     + @Before 的示例
    

            @Controller(path="/user")
            public class UserAction{

                @Get("/list")
                public ModelAndView list(UserQueryBean bean){

                  //...

                }

                @Before("/list")
                public void doBeforeList(){
                  // 在list 之前做一些事
                }

            } 
             
   * **@After**  --- 在完成了请求处理方法执行之后，我们可以执行某些方法进行善后处理
   
   
   
   
   //######################################################################
   * 2016-12-24
	*添加aop代理模块，对@Service注解的类的方法进行拦截，针对@Service类做一些事务的处理
	*使用的时候直接用@Autowired注入到Action中
  
  * 2017-01-02
	*添加@ControllerAdvice注解，Action的统一异常处理。目前只提供全局Action的
	*对应添加@ExceptionHandler注解，处理对应的异常。
	
	*添加redirectHtml结果处理handler，在action中可以返回字符串 "redirectHtml:index.html",表示重定向到webapp/viewroot/index.html
	*添加html结果处理handler(跟redirectHtml同一个)，在action中可以返回字符串 "html:index.html",表示转到到viewroot/index.html

	*GlobalConfig.openDirectToHtml开关,默认是关闭的，可以在ServletInitEvent事件处理中打开，例如：
	@Observes
	public void init(ServletInitEvent event){		
		GlobalConfig.addStaticResourceMapping("/pages", "/pages");
		GlobalConfig.addStaticResourceMapping("/assets", "/assets");
		GlobalConfig.setViewRoot("/");
		GlobalConfig.setOpenDirectToHtml(true);
	}
	openDirectToHtml打开后，动态请求中（非静态资源）先过了拦截器prepareProcess（通常用于是否登录的判断），再判断是否是html文件，如果是html文件，就当做是静态文件处理。不再进入action方法
	@Override
	public boolean prepareProcess(HttpServletRequest request, HttpServletResponse response) {
	}
	*增加了添加静态资源文件或文件夹的配置GlobalConfig.addStaticResourceMapping("/pages", "/pages");
   
   * 2017-01-15 修复bug MapToObject 中processPropertyValue(PropertyInfo propInfo)方法：
		//这是目标对象的属性
		//如果这个属性值还没有，就需要创建一个，这样对能进行后续的，对此对象的属性进行赋值，这里是处理自定义类的属性，要对这样的属性赋值，首先就要创建一个对象
		if(fieldObject == null){
			//fieldObject = field.getDeclaringClass().newInstance();//这里是不是错的？
			fieldObject = field.getType().newInstance();
			ReflectUtils.invokeWriteMethod(valueObject, fieldName, new Object[]{fieldObject});
		}
    
    * 2017-01-16 
	**BaseClassRequestAnnotation
		//如果path不是/开头，就加上
			if(!requestPath.startsWith("/")){
				requestPath = "/"+requestPath;
			}
	**RequestMappingProcessor
		//为了适应spring mvc 方法注解，RequestMapping中添加path
    
    
#############################################################
框架默认扫描的包是：com.laview

可以在web.xml中添加
	<context-param>
    	<param-name>contextBeanLocation</param-name>
    	<param-value>
			com.laview.custom
		</param-value>
  	</context-param>
指定扫描的包。

这句是启动扫描器。
WebApplicationContext.createApplicationContext(new String[]{"com.laview"});
