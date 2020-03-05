##spring

##spring4 时间

```
//使用瞬时时间 + 时区  
Instant instant = Instant.now();  
LocalDateTime d3 = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());  
System.out.println(d3);  
```

###IOC

学 java 的人应该都知道spring，学spring的人呢都应该知道 IOC和 AOP 对吧。今天就简单的说一下spring 的IOC。

控制反转IoC(Inversion of Control)，是一种设计思想，DI(依赖注入)是实现IoC的一种方法。
其实spring就是一个容器，一个bean容器。主要是完成了完成对象的创建和依赖的管理注入。

###什么是控制反转呢？

所谓控制反转，就是把原先我们代码里面需要实现的对象创建、依赖的代码，反转给ioc容器来帮忙实现，也就是 ioc 容器帮我们做了原本应该我门自己实现的对象创建和依赖的内容。
好了那么ioc 容器是怎么帮我门创建 对象 和注入依赖呢？
1。读取xml
2。反射




ioc 控制反转 也称为 DI （dependenty injection 依赖注入）
优点：
spring 帮我们管理和创建对象，降低了耦合性。
通过xml 文件配置方便修改 灵活。

反转： 反转到容器控制，本来是要由我控制的现在交给容器帮忙控制，容器里面各种对象，相互之间各种依赖，随意装配 。 

spring ioc 帮我们创建和管理了我们需要创建的对象，并像搭乐高积木一样，来管理这个对象间的关系。

Spring Bean的创建是典型的工厂模式，bean 的生命周期默认是单例模式

##AOP

面向切面编程
AOP（Aspect-OrientedProgramming，面向方面编程），可以说是OOP（Object-Oriented Programing，面向对象编程）的补充和完善。
动态代理就是生成一个代理对象，对代理需要被代理的对象。 代理对象 是为了 代理被代理对象创建的对象。

面线切面编程就像是横着切了一刀。 使用场景 权限、日志、事务（start ，commit）、异常处理、

http://blog.csdn.net/moreevan/article/details/11977115/
http://www.cnblogs.com/hongwz/p/5764917.html
https://www.zhihu.com/question/24863332

如果你的类没有实现接口，spring 也能给你生成动态代理，spring直接生成二进制码，用继承。 

aspectj 是一个专门用来生成代理的框架
joinpoint 切入点 ；语法 ：execution(public void com.abel.dao.impl.UserDAOImpl.save(com.abel.model.User )
pointcut 连接点的集合 pointcut 是 joinpoint 的集合
语法 ：execution(* com.abel.dao.impl.*.*(..)
com.abel.dao.impl 路径下的所有类的（任何返回值）
所有方法

Aspect 就是切面类
advice @Befor 
target 代理对象
weave  织入


<aop:after> 后通知
<aop:after-returning> 返回后通知
<aop:after-throwing> 抛出后通知
<aop:around> 周围通知
<aop:aspect>定义一个切面
<aop:before>前通知


##Spring如何解决循环依赖

###构造器循环依赖：

表示通过构造器注入构成的循环依赖，此依赖是无法解决的，只能抛出BeanCurrentlyInCreationException异常表示循环依赖。

如在创建CircleA类时，构造器需要CircleB类，那将去创建CircleB，在创建CircleB类时又发现需要CircleC类，则又去创建CircleC，最终在创建CircleC时发现又需要CircleA；从而形成一个环，没办法创建。

Spring容器将每一个正在创建的Bean 标识符放在一个“当前创建Bean池”中，Bean标识符在创建过程中将一直保持在这个池中，因此如果在创建Bean过程中发现自己已经在“当前创建Bean池”里时将抛出BeanCurrentlyInCreationException异常表示循环依赖；而对于创建完毕的Bean将从“当前创建Bean池”中清除掉。
setter循环依赖：表示通过setter注入方式构成的循环依赖。

###setter 注入造成的依赖 

setter循环依赖：表示通过setter注入方式构成的循环依赖。
对于setter注入造成的依赖是通过Spring容器提前暴露刚完成构造器注入但未完成其他步骤（如setter注入）的Bean来完成的，而且只能解决单例作用域的Bean循环依赖。

###为什么用set方式就不报错了呢 ？

    Spring先是用构造实例化Bean对象 ，此时Spring会将这个实例化结束的对象放到一个Map中，并且Spring提供了获取这个未设置属性的实例化对象引用的方法。   结合我们的实例来看，，当Spring实例化了StudentA、StudentB、StudentC后，紧接着会去设置对象的属性，此时StudentA依赖StudentB，就会去Map中取出存在里面的单例StudentB对象，以此类推，不会出来循环的问题喽、

# spring 注入接口

当接口中有范型的时候想当于多个 接口，此时注入接口报错
当接口多个实现类时，此时注入接口，报错。

## @Primary

当一个接口有2个不同实现时,使用@Autowired注解时会报
@Primary可以理解为默认优先选择,同时不可以同时设置多个,
内部实质是设置BeanDefinition的primary属性

## @NotEmpty,@NotNull和@NotBlank的区别

* @NotEmpty :不能为null，且Size>0

* @NotNull:不能为null，但可以为empty,没有Size的约束

* @NotBlank:只用于String,不能为null且trim()之后size>0

## @Order

@Order 标记只支持AspectJ的切面排序。spring 4.0对@Order做了增强，它开始支持对装载在诸如Lists和Arrays容器中的自动包装（auto-wired）组件的排序。

#@Async介绍

Spring中，基于@Async标注的方法，称之为异步方法；这些方法将在执行的时候，将会在独立的线程中被执行，调用者无需等待它的完成，即可继续其他的操作

##@Mapper注解的componentModel属性

componentModel属性用于指定自动生成的接口实现类的组件类型。这个属性支持四个值：
default: 这是默认的情况，mapstruct不使用任何组件类型, 可以通过Mappers.getMapper(Class)方式获取自动生成的实例对象。

cdi: the generated mapper is an application-scoped CDI bean and can be retrieved via @Inject

spring: 生成的实现类上面会自动添加一个@Component注解，可以通过Spring的 @Autowired方式进行注入

jsr330: 生成的实现类上会添加@javax.inject.Named 和@Singleton注解，可以通过 @Inject注解获取。





# 框架

##MapStruct

   MapStruct是一个代码生成器，简化了不同的Java Bean之间映射的处理，所以映射指的就是从一个实体变化成一个实体。例如我们在实际开发中，DAO层的实体和一些数据传输对象(DTO)，大部分属性都是相同的，只有少部分的不同，通过mapStruct，可以让不同实体之间的转换变的简单。我们只需要按照约定的方式进行配置即可。

##gradle

Gradle和Maven都是项目自动构建工具，编译源代码只是整个过程的一个方面.虽然两者都是项目工具，但是maven现在已经是行业标准，Gradle是后起之秀，Gradle抛弃了Maven的基于XML的繁琐配置，众所周知XML的阅读体验比较差，对于机器来说虽然容易识别，但毕竟是由人去维护的。取而代之的是Gradle采用了领域特定语言Groovy的配置，大大简化了构建代码的行数.

Gradle给我最大的有点是两点。其一是简洁，基于Groovy的紧凑脚本实在让人爱不释手，在表述意图方面也没有什么不清晰的地方。其二是灵活，各种在Maven中难以下手的事情，在Gradle就是小菜一碟，比如修改现有的构建生命周期，几行配置就完成了，同样的事情，在Maven中你必须编写一个插件，

范例 用 gradle 引入依赖

```
dependencies {
    compile('org.springframework:spring-core:2.5.6')
    compile('org.springframework:spring-beans:2.5.6')
    compile('org.springframework:spring-context:2.5.6')
    compile('com.google.code.kaptcha:kaptcha:2.3:jdk15')
    testCompile('junit:junit:4.7')
}
```

#