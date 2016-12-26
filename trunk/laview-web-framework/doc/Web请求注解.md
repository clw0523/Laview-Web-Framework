
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






