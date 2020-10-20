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

### 三级缓存

在Spring的整个容器的生命周期内，有且只存在一个对象，很容易想到这个对象应该存在Cache中，Spring大量运用了Cache的手段，在循环依赖问题的解决过程中甚至使用了“三级缓存”。

singletonObjects指单例对象的cache，singletonFactories指单例对象工厂的cache，earlySingletonObjects指提前曝光的单例对象的cache。以上三个cache构成了三级缓存，Spring就用这三级缓存巧妙的解决了循环依赖问题。

分析getSingleton的整个过程，代码如下 

```java
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object singletonObject = this.singletonObjects.get(beanName);
        if(singletonObject == null && this.isSingletonCurrentlyInCreation(beanName)) {
            Map var4 = this.singletonObjects;
            synchronized(this.singletonObjects) {
                singletonObject = this.earlySingletonObjects.get(beanName);
                if(singletonObject == null && allowEarlyReference) {
                    ObjectFactory singletonFactory = (ObjectFactory)this.singletonFactories.get(beanName);
                    if(singletonFactory != null) {
                        singletonObject = singletonFactory.getObject();
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }

        return singletonObject;
    }
```

Spring首先从singletonObjects（一级缓存）中尝试获取，如果获取不到并且对象在创建中，则尝试从earlySingletonObjects(二级缓存)中获取，如果还是获取不到并且允许从singletonFactories通过getObject获取，则通过singletonFactory.getObject()(三级缓存)获取。如果获取到了则移除对应的singletonFactory,将singletonObject放入到earlySingletonObjects，其实就是将三级缓存提升到二级缓存中！

```java
addSingletonFactory(beanName, new ObjectFactory<Object>() {
   @Override   public Object getObject() throws BeansException {
      return getEarlyBeanReference(beanName, mbd, bean);
   }});
```

解决循环依赖的关键，这段代码发生在**createBeanInstance(创建实例)**之后，也就是说单例对象此时已经被创建出来的。这个对象已经被生产出来了，虽然还不完美（还没有进行初始化的第二步[属性注入]和第三步[方法回调]），但是已经能被人认出来了（根据对象引用能定位到堆中的对象），所以Spring此时将这个对象提前曝光出来让大家认识，让大家使用。

A-B-A

A首先完成了初始化的第一步**createBeanInstance(创建实例)**，并且将自己提前曝光到**singletonFactories**中，此时进行初始化的第二步(**属性注入依赖分析**)，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是**尝试get(A)，尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，由于A通过ObjectFactory将自己提前曝光了**，**所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全**，但是总比没有好呀)，B拿到A对象后顺利完成了初始化阶段1、2、3，**完全初始化之后将自己放入到一级缓存singletonObjects中**。此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，最终A也完成了初始化，长大成人，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象也蜕变完美了

### 详细过程

* 1、先进行初始化a对象的操作，然后发现调用的是**createBean**(String beanName, RootBeanDefinition mbd, Object[] args)方法，而真正起作用的是**doCreateBean**(final String beanName, final RootBeanDefinition mbd, final Object[] args)方法。而在这个方法里面包含了三个重要的方法**createBeanInstance、populateBean、initializeBean**，这三个方法分别代表：创建实例、属性注入、方法回调。

* 2、createBeanInstance和populateBean中间的一段**doCreateBean**的代码，doCreateBean中调用了 获取**getEarlyBeanReference（二级缓存）方法。同时还调用了放置addSingletonFactory方法**。

* 3、addSingletonFactory方法中，会将beanName与singletonFactory形成kv关系put进singletonFactories（三级）里面。并且将earlySingletonObjects里面的key值为beanName的kv进行移除（无论其中有没有值）。
* 4、此时a对象的早期暴露引用已经存在了singletonFactories三级缓存里面

* 5、此时a对象进行populateBean方法进行属性注入，发现需要依赖b对象，紧接着就是去初始化b对象。继续重复上面的步骤到b对象进行属性注入这一步的时候（**此时singletonFactories三级缓存里已经有了a对象的提前暴露引用和b对象的提前暴露引用的工厂对象**），发现需要依赖a对象，此时去获取a对象

* 6、先从singletonObjects一级缓存里取，如果没有取到，则从earlySingletonObjects二级缓存里取，如果还是没取到，则从singletonFactories三级缓存里取，取到以后进行getObject方法返回早期暴露对象引用，**同时放进earlySingletonObjects二级缓存里，并且三级缓存里进行删除该kv**。

* 7、到此，**a对象的早期暴露引用已经被b对象获取到了，并且在singletonFactories三级缓存里已经没有a对象的早期暴露引用的工厂对象了，a对象的早期暴露引用存在了二级缓存earlySingletonObjects里面，当然singletonFactories三级缓存依然有b对象的早期暴露引用的工厂对象。**

* 8、继续：b对象拿到了a对象的早期暴露引用，进行完属性注入以后，则返回一个b对象了同时调用方法getSingleton(String beanName, ObjectFactory<?> singletonFactory)

* 9、此时singletonObjects一级缓存将要存入b对象，而二级缓存earlySingletonObjects和三级缓存singletonFactories则把相关缓存的对象移除。至此b对象则只存在一级缓存singletonObjects里面了。
   当b对象完成了初始化以后，a对象则进行相关属性的注入引入b的对象。完成实例化的同时a对象也会调用一次addSingleton方法，那么a对象完成以后，也就只有一级缓存singletonObjects里面才有a对象。

至此，属性的循环依赖问题则完美的得到解决

##AOP



AOP（Aspect-OrientedProgramming，面向方面编程思想），可以说是OOP（Object-Oriented Programing，面向对象编程）的补充和完善。OOP引入**封装、继承、多态**等概念来建立一种对象层次结构，用于模拟公共行为的一个集合。不过OOP允许开发者定义纵向的关系，但并不适合定义横向的关系，

AOP利用一种"横切"的技术，剖解开封装的对象内部，并将那些影响了多个类的公共行为封装到一个可重用模块，并将其命名为"Aspect"，即切面。所谓"切面"，简单说就是那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和可维护性。

使用"横切"技术，AOP 把软件系统分为两个部分：**核心关注点**和**横切关注点**。**业务处理的主要流程是核心关注点**，与之关**系不大的部分是横切关注点**。横切关注点的一个特点是，他们经常发生在核心关注点的多处，而各处基本相似，比如权限认证、日志、事物。**AOP 的作用在于分离系统中的各种关注点，将核心关注点和横切关注点分离开来**

我们传统的编程方式是垂直化的编程，即A–>B–>C–>D这么下去，一个逻辑完毕之后执行另外一段逻辑。但是AOP提供了另外一种思路，它的作用是在业务逻辑不知情（即业务逻辑不需要做任何的改动）的情况下对业务代码的功能进行增强，这种编程思想的使用场景有很多，例如事务提交、方法执行之前的权限检测、日志打印、方法调用事件等等

面向切面编程
（erp的参数校验）

###  主要应用场景
1. Authentication 权限
2. Caching 缓存
3. Context passing 内容传递
4. Error handling 错误处理
5. Lazy loading 懒加载
6. Debugging 调试
7. logging, tracing, profiling and monitoring 记录跟踪 优化 校准
8. Performance optimization 性能优化
9. Persistence 持久化
10. Resource pooling 资源池
11. Synchronization 同步
12. Transactions 事务

### 核心概念

1、切面（aspect）：类是对物体特征的抽象，切面就是对横切关注点的抽象

2、横切关注点：对哪些方法进行拦截，拦截后怎么处理，这些关注点称之为横切关注点。 

3、连接点（joinpoint）：被拦截到的点，因为 Spring 只支持方法类型的连接点，所以在 Spring

中连接点指的就是被拦截到的方法，实际上连接点还可以是字段或者构造器。 

4、切入点（pointcut）：对连接点进行拦截的定义

5、通知（advice）：所谓通知指的就是指拦截到连接点之后要执行的代码，通知分为前置、后置、

异常、最终、环绕通知五类。 

6、目标对象：代理的目标对象

7、织入（weave）：将切面应用到目标对象并导致代理对象创建的过程

8、引入（introduction）：在不修改代码的前提下，引入可以在运行期为类动态地添加一些方法

或字段。

### AOP、拦截器、过滤器、区别

#### 过滤器
**过滤器拦截的是URL**

Spring中自定义过滤器（Filter）一般只有一个方法，返回值是void，当请求到达web容器时，会探测当前请求地址是否配置有过滤器，有则调用该过滤器的方法（可能会有多个过滤器），然后才调用真实的业务逻辑，至此过滤器任务完成。过滤器并没有定义业务逻辑执行前、后等，仅仅是请求到达就执行。

特别注意：过滤器方法的入参有request，response，FilterChain，其中FilterChain是过滤器链，使用比较简单，而request，response则关联到请求流程，因此可以对请求参数做过滤和修改，同时FilterChain过滤链执行完，并且完成业务流程后，会返回到过滤器，此时也可以对请求的返回数据做处理。

#### 拦截器
**拦截器拦截的是URL**

拦截器有三个方法，相对于过滤器更加细致，有被拦截逻辑执行前、后等。Spring中拦截器有三个方法：preHandle，postHandle，afterCompletion。分别表示如下

* preHandle 表示被拦截的URL对应的方法执行前的自定义处理

* postHandle 表示此时还未将modelAndView进行渲染，被拦截的URL对应的方法执行后的自定义处理，。

* afterCompletion：表示此时modelAndView已被渲染，执行拦截器的自定义处理。

#### AOP（面向切面）
**面向切面拦截的是类的元数据（包、类、方法名、参数等）**

相对于拦截器更加细致，而且非常灵活，拦截器只能针对URL做拦截，而AOP针对具体的代码，能够实现更加复杂的业务逻辑。具体类型参照其他博客。

三者使用场景
三者功能类似，但各有优势，从过滤器--》拦截器--》切面，拦截规则越来越细致，执行顺序依次是过滤器、拦截器、切面。一般情况下数据被过滤的时机越早对服务的性能影响越小，因此我们在编写相对比较公用的代码时，优先考虑过滤器，然后是拦截器，最后是aop。比如权限校验，一般情况下，所有的请求都需要做登陆校验，此时就应该使用过滤器在最顶层做校验；日志记录，一般日志只会针对部分逻辑做日志记录，而且牵扯到业务逻辑完成前后的日志记录，因此使用过滤器不能细致地划分模块，此时应该考虑拦截器，然而拦截器也是依据URL做规则匹配，因此相对来说不够细致，因此我们会考虑到使用AOP实现，AOP可以针对代码的方法级别做拦截，很适合日志功能。



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

# 事务

 声明式事务 和 编程式事务
## 编程式事务

基于底层的API，如PlatformTransactionManager、TransactionDefinition 和 TransactionTemplate 等核心接口，开发者完全可以通过编程的方式来进行事务管理。

**编程式事务方式需要是开发者在代码中手动的管理事务的开启、提交、回滚等操作。**开发者可以通过API自己控制事务。

```java
public void test() {
      TransactionDefinition def = new DefaultTransactionDefinition();
      TransactionStatus status = transactionManager.getTransaction(def);
       try {
         // 事务操作
         // 事务提交
         transactionManager.commit(status);
      } catch (DataAccessException e) {
         // 事务提交
         transactionManager.rollback(status);
         throw e;
      }
}
```

### 声明式事务

**声明式事务管理方法允许开发者配置的帮助下来管理事务，而不需要依赖底层API进行硬编码。开发者可以只使用注解或基于配置的 XML 来管理事务。**如下使用@Transactional即可给test方法增加事务控制。

```java
@Transactional
public void test() {
     // 事务操作  
}
```

### 声明式事务优点

声明式事务帮助我们节省了很多代码，他会自动帮我们进行事务的开启、提交以及回滚等操作，把程序员从事务管理中解放出来。

**声明式事务管理使用了 AOP 实现的，本质就是在目标方法执行前后进行拦截。** 在目标方法执行前加入或创建一个事务，在执行方法执行后，根据实际情况选择提交或是回滚事务。

**使用这种方式，对代码没有侵入性，方法内只需要写业务逻辑就可以了。**

### **声明式事务问题**

1. **粒度问题**： **声明式事务有一个局限，那就是他的最小粒度要作用在方法上。**也就是说，如果想要给一部分代码块增加事务的话，那就需要把这个部分代码块单独独立出来作为一个方法。但是，正是因为这个粒度问题，Hollis并不建议过度的使用声明式事务。

2. **易忽略**：因为声明式事务是通过注解的，有些时候还可以通过配置实现，这就会导致一个问题，那就是这个事务有可能被开发者忽略。

忽略可能带来的问题：

首先，如果开发者没有注意到一个方法是被事务嵌套的，那么就可能会再方法中加入一些如**RPC远程调用、消息发送、缓存更新、文件写入等操作**。

这些操作如果被包在事务中，有两个问题：

1. 这些**操作自身是无法回滚**的，这就会**导致数据的不一致**。可能RPC调用成功了，但是本地事务回滚了，可是PRC调用无法回滚了。

2. 如果在事务中有远程调用，就会**拉长整个事务**。那么久会导致本事务的数据库连接一直被占用，那么如果类似操作过多，就会导致**数据库连接池耗尽**。

有些时候，即使没有在事务中进行远程操作，但是有些人还是可能会不经意的进行一些内存操作，如运算。或者如果遇到**分库分表的情况，有可能不经意间进行跨库**操作。

但是如果是编程式事务的话，业务代码中就会清清楚楚看到什么地方开启事务，什么地方提交，什么时候回滚。这样有人改这段代码的时候，就会强制他考虑要加的代码是否应该方法事务内。

**声明式事务失效情况**

1、@Transactional 应用在**非 public 修饰**的方法上

2、@Transactional **注解属性 propagation** 设置错误

3、@Transactional **注解属性 rollbackFor** 设置错误

4、同**一个类中方法调用，导致@Transactiona**l失效

5、异常**被catch捕获**导致@Transactional失效

6、**数据库引擎不支持事务**

因为**Spring的声明式事务是基于AOP**实现的，但是在代码中，有时候我们会有很多切面，不同的切面可能会来处理不同的事情，多个切面之间可能会有相互影响。在之前的一个项目中，我就发现我们的Service层的事务全都失效了，一个SQL执行失败后并没有回滚，排查下来才发现，是因为一位**同事新增了一个切面，这个切面里面做个异常的统一捕获**，导致事务的切面没有捕获到异常，导致事务无法回滚。

结论：**我们确实无法保证所有人的能力都很高**，也无法要求所有开发者都能不出错。我们能做的就是，尽量可以通过**机制或者规范**，来避免或者降低这些问题发生的概率。

## 传播行为

| 传播行为	      | 意义 |
| :-----------: | ------------------------------------------------------------ |
| PROPAGATION_REQUIRED | 表示当前方法必须在一个事务中运行。如果一个现有事务正在进行中，该方法将在那个事务中运行，否则就要开始一个新事务。 |
| PROPAGATION_SUPPORTS | 表示当前方法不需要事务性上下文，但是如果有一个事务已经在运行的话，它也可以在这个事务里运行。 |
| PROPAGATION_MANDATORY | 表示该方法必须运行在一个事务中。如果当前没有事务正在发生，将抛出一个异常 |
| PROPAGATION_REQUIRES_NEW | 表示当前方法必须在它自己的事务里运行。一个新的事务将被启动，而且如果有一个现有事务在运行的话，则将在这个方法运行期间被挂起。 |
| PROPAGATION_NOT_SUPPORTED | 表示该方法不应该在一个事务中运行。如果一个现有事务正在进行中，它将在该方法的运行期间被挂起。 |
| PROPAGATION_NEVER | 表示当前的方法不应该在一个事务中运行。如果一个事务正在进行，则会抛出一个异常。 |
| PROPAGATION_NESTED | 表示如果当前正有一个事务在进行中，则该方法应当运行在一个嵌套式事务中。被嵌套的事务可以独立于封装事务进行提交或回滚。如果封装事务不存在，行为就像PROPAGATION_REQUIRES一样。他会和父事务一起commit，当它回滚时，父事务有条件的选择是否跟随回滚，或者继续执行 |




| 数据隔离级别	      | Dirty reads（脏读） |non-repeatable reads （不可重复读）| phantom reads（幻读）|
| :-----------: | ------------------------------------------------------------ |
|Serializable          |           不会            |       不会                      |     不会|
|REPEATABLE READ     |      不会          |         不会             |               会|
|READ COMMITTED      |      不会        |            会                |             会|
|Read Uncommitted      |      会             |        会                   |          会|

最安全的，是Serializable，但是伴随而来也是高昂的性能开销。 
另外，事务常用的两个属性：readonly和timeout

* 一个是设置事务为只读以提升性能。
* 另一个是设置事务的超时时间，一般用于防止大事务的发生。还是那句话，事务要尽可能的小！




# xspring 注入接口

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