# mini-spring

## 原作者地址：https://github.com/YaleGuo/Minis

## IOC容器

**IOC容器的组成部分：**

- 一个部件来对应 Bean 内存的映像。
- XML reader 负责从外部 XML 文件获取 Bean 的配置。
- 反射部件负责加载 Bean Class 并且创建这个实例。
- 一个 Map 来保存 Bean 的实例。
- getBean() 方法供外部使用。

**问题：**

IoC 的字面含义是“控制反转”，那么它究竟“反转”了什么？又是怎么体现在代码中的？

> 一个“正常”的控制过程是由调用者直接创建 Bean，但是 IoC 的过程正好相反，是由框架来创建 Bean，然后注入给调用者，这与“正常”的过程是反的，控制反转就是这个意思。 
>



**IOC容器的注入：**

Spring 中有三种属性注入的方式，分别是 Field 注入、Setter 注入和构造器（Constructor）注入。

**问题：**

你认为构造器注入和 Setter 注入有什么异同？它们各自的优缺点是什么？

> 



**IOC容器的工作的基础：**

反射技术是 IoC 容器赖以工作的基础。



**循环依赖问题**

请你想象一个场景，Spring 扫描到了 ABean，在解析它并设置内部属性时，发现某个属性是另一个 BBean，而此时 Spring 内部还不存在 BBean 的实例。这就要求 Spring 在创建 ABean 的过程中，能够再去创建一个 BBean，继续推衍下去，BBean 可能又会依赖第三个 CBean。事情还可能进一步复杂化，如果 CBean 又反过来依赖 ABean，就会形成循环依赖。

创建 Bean 的过程：

1. Bean 的定义配置生成了 BeanDefinition
2. 然后根据定义加载 Bean 类，再进行实例化
3. 最后在 Bean 中注入属性。

从这个过程中可以看出，在注入属性之前，其实这个 Bean 的实例已经生成出来了，只不过此时的实例还不是一个完整的实例，它还有很多属性没有值，可以说是一个早期的毛胚实例。而我们现在讨论的 Bean 之间的依赖是在属性注入这一阶段，因此我们可以在实例化与属性注入这两个阶段之间增加一个环节，确保给 Bean 注入属性的时候，Spring 内部已经准备好了 Bean 的实例。

Spring 的做法是在 BeanFactory 中引入一个结构：**earlySingletonObjects**，这里面存放的就是早期的毛胚实例。创建 Bean 实例的时候，不用等到所有步骤完成，而是可以在属性还没有注入之前，就把早期的毛胚实例先保存起来，供属性注入时使用。

> 第一步，先实例化 ABean，此时它是早期的不完整毛胚实例，好多属性还没被赋值，将实例放置到 earlySingletonObjects 中备用。然后给 ABean 注入属性，这个时候发现它还要依赖 BBean。
>
> 第二步，实例化 BBean，它也是早期的不完整毛胚实例，我们也将实例放到 earlySingletonObjects 中备用。然后再给 BBean 注入属性，又发现它依赖 CBean。
>
> 第三步，实例化 CBean，此时它仍然是早期的不完整的实例，同样将实例放置到 earlySingletonObjects 中备用，然后再给 CBean 属性赋值，这个时候又发现它反过来还要依赖 ABean。
>
> 第四步，我们从 earlySingletonObjects 结构中找到 ABean 的早期毛胚实例，取出来给 CBean 注入属性，这意味着这时 CBean 所用的 ABean 实例是那个早期的毛胚实例。这样就先创建好了 CBean。
>
> 第五步，程序控制流回到第二步，完成 BBean 的属性注入。
>
> 第六步，程序控制流回到第一步，完成 ABean 的属性注入。至此，所有的 Bean 就都创建完了。

通过上述过程可以知道，这一系列的 Bean 是纠缠在一起创建的，我们不能简单地先后独立创建它们，而是要作为一个整体来创建。



1. 首先要判断有没有已经创建好的 bean，有的话直接取出来，如果没有就检查 earlySingletonObjects 中有没有相应的毛胚 Bean，有的话直接取出来，没有的话就去创建，并且会根据 Bean 之间的依赖关系把相关的 Bean 全部创建好。
2. createBean() 方法中调用了一个 doCreateBean(bd) 方法，专门负责创建早期的毛胚实例（construct）。
3. 毛胚实例创建好后会放在 earlySingletonObjects 结构中，然后 createBean() 方法再调用 handleProperties() 补齐这些 property 的值。在 getBean() 方法中。
4. 很多资料把这个过程叫做 bean 的“**三级缓存**”。



为了减少它内部的复杂性，Spring 对外提供了一个很重要的包装方法：**refresh()**。具体的包装方法也很简单，就是对所有的 Bean 调用了一次 getBean()，利用 getBean() 方法中的 createBean() 创建 Bean 实例，就可以只用一个方法把容器中所有的 Bean 的实例创建出来了。

**问题：**

你认为能不能在一个 Bean 的构造器中注入另一个 Bean？

> Spring支持一个Bean构造器注入另一个Bean，工作中也都是尽量通过构造器注入。
>
> 有很多优点 通过**属性注入的方式**能解决循环依赖的问题，原理是通过缓存的方式解决的，这里的关键点是属性注入是在bean创建后注入的 而构造器注入不能解决循环依赖问题 因为需要在创建bean时就需要将依赖的bean传入到构造函数中，如果依赖的bean尚未创建完成，就不能传入到构造函数中，循环依赖就不能解决。

**问题：**

我们的容器以单例模式管理所有的 Bean，那么怎么应对多线程环境？

> 在单例模式的情况下，每个 Bean 是同一个实例，如果多个线程同时访问该 Bean，可能会引发线程安全问题。 
>
> 为了应对多线程环境，我们可以使用线程安全的 Bean。
>
> 在 Spring 中，可以通过以下两种方式实现 Bean 的线程安全：
>
> 1. 使用原型模式，每次获取 Bean 时都返回一个新实例，确保 Bean 在每个线程中都是独立的。 
> 2. 在单例 Bean 内部增加锁机制，避免多个线程同时访问同一个共享资源。通常，为了提高性能，单例模式是最常用的模式，因此在实现单例模式的 Bean 时，如果可能会受到多线程并发访问的情况，我们需要额外考虑线程安全问题。如果某个 Bean 的状态不受共享资源的影响，比如一些静态变量、常量等，那么可以不必考虑线程安全问题。

## Spring MVC

**Spring MVC基本流程**

> 前端发送请求到控制器，控制器寻找对应模型，找到模型后返回结果，渲染视图返回给前端生成页面。这是标准的前端请求数据的模型。

**web.xml标签解析**

| 标签              | 含义                                                         |
| ----------------- | :----------------------------------------------------------- |
| <servlet-class>   | 指定Servlet对应类，也是Web程序的核心代码                     |
| <param-value>     | 初始化配置文件地址，表示所有的配置参数都由这里引入           |
| <load-on-startup> | 当值大于等于0时，容器启动时加载该Servlet，且值越小启动优先级越高。如果为负数，则容器启动时不会加载该Servlet |
| <url-pattern>     | 标签为“/”,表示拦截所有的URL                                  |
| <servlet-name>    | Servlet自定义名称，且<servlet-mapping>与<servlet>标签中的<servlet-name>配置一样，表示用<servlet>标签中配置的Servlet进行URL请求拦截与映射匹配 |

**MVC 的基本思路**

> 屏蔽 Servlet 的概念，让程序员主要写业务逻辑代码

**Web 规范的内容**

> web.xml 文件是 Java 的 Servlet 规范中规定的，它里面声明了一个 Web 应用全部的配置信息。按照规定，每个 Java Web 应用都必须包含一个 web.xml 文件，且必须放在 WEB-INF 路径下。它的顶层根是 web-app，指定命名空间和 schema 规定。通常，我们会在 web.xml 中配置 context-param、Listener、Filter 和 Servlet 等元素。

~~~xml
<display-name></display-name>  
声明WEB应用的名字    
<description></description>   
 声明WEB应用的描述信息    
<context-param></context-param> 
声明应用全局的初始化参数。  
<listener></listener>
声明监听器，它在建立、修改和删除会话或servlet环境时得到事件通知。
<filter></filter> 
声明一个实现javax.servlet.Filter接口的类。    
<filter-mapping></filter-mapping>
声明过滤器的拦截路径。 
<servlet></servlet> 
声明servlet类。    
<servlet-mapping></servlet-mapping> 
声明servlet的访问路径，试一个方便访问的URL。    
<session-config></session-config> 
session有关的配置，超时值。
<error-page></error-page> 
在返回特定HTTP状态代码时，或者特定类型的异常被抛出时，能够制定将要显示的页面。   
~~~

**Servlet 服务器如 Tomcat 启动的时候，要遵守下面的时序：**

> 1. 在启动 Web 项目时，Tomcat 会读取 web.xml 中的 comtext-param 节点，获取这个 Web 应用的全局参数。
> 2. Tomcat 创建一个 ServletContext 实例，是全局有效的。
> 3. 将 context-param 的参数转换为键值对，存储在 ServletContext 里。
> 4. 创建 listener 中定义的监听类的实例，按照规定 Listener 要继承自 ServletContextListener。监听器初始化方法是 contextInitialized(ServletContextEvent event)。初始化方法中可以通过 event.getServletContext().getInitParameter(“name”) 方法获得上下文环境中的键值对。
> 5. 当 Tomcat 完成启动，也就是 contextInitialized 方法完成后，再对 Filter 过滤器进行初始化。
> 6. servlet 初始化：有一个参数 load-on-startup，它为正数的值越小优先级越高，会自动启动，如果为负数或未指定这个参数，会在 servlet 被调用时再进行初始化。init-param 是一个 servlet 整个范围之内有效的参数，在 servlet 类的 init() 方法中通过 this.getInitParameter(″param1″) 方法获得。

总结：

> 当 Sevlet 服务器启动时，Listener 会优先启动，读配置文件路径，启动过程中初始化上下文，然后启动 IoC 容器，这个容器通过 refresh() 方法加载所管理的 Bean 对象。这样就实现了 Tomcat 启动的时候同时启动 IoC 容器。

**什么是WAC?**

> 在服务器启动的过程中，会注册Web应用上下文，也就是WAC。

**从 Dispatcher 内可访问 WebApplicationContext 里面管理的 Bean，那通过 WebApplicationContext 可以访问 Dispatcher 内管理的 Bean 吗？**

> 通过 `WebApplicationContext` 可以访问 `DispatcherServlet` 内管理的 Bean。在 `DispatcherServlet` 初始化时，会创建一个 `WebApplicationContext`，并将其注册到 `DispatcherServlet` 中。可以从 `HttpRequest` 对象中获取 `WebApplicationContext` 实例，然后可以通过 `getBean` 方法获取 `DispatcherServlet` 中的 Bean。



## JDBC
