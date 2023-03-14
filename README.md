# mini-spring

## IOC容器

IOC容器的组成部分：

- 一个部件来对应 Bean 内存的映像。
- XML reader 负责从外部 XML 文件获取 Bean 的配置。
- 反射部件负责加载 Bean Class 并且创建这个实例。
- 一个 Map 来保存 Bean 的实例。
- getBean() 方法供外部使用。
