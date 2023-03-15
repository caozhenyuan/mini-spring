# mini-spring

## IOC容器

**IOC容器的组成部分：**

- 一个部件来对应 Bean 内存的映像。
- XML reader 负责从外部 XML 文件获取 Bean 的配置。
- 反射部件负责加载 Bean Class 并且创建这个实例。
- 一个 Map 来保存 Bean 的实例。
- getBean() 方法供外部使用。

**问题：**

IoC 的字面含义是“控制反转”，那么它究竟“反转”了什么？又是怎么体现在代码中的？



**注入：**

Spring 中有三种属性注入的方式，分别是 Field 注入、Setter 注入和构造器（Constructor）注入。

**问题：**

你认为构造器注入和 Setter 注入有什么异同？它们各自的优缺点是什么？
