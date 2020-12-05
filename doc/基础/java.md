# 操作

# maven 跳过测试

mvn install -Dmaven.test.skip=true

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

# 概念

#java



##java 字符串判断

判断某字符串是否为空或长度为0或由空白符(whitespace)构成
下面是示例：

```
StringUtils.isBlank(null) = true
StringUtils.isBlank("") = true
StringUtils.isBlank(" ") = true
StringUtils.isBlank(" ") = true
StringUtils.isBlank("\t \n \f \r") = true //对于制表符、换行符、换页符和回车符StringUtils.isBlank()均识为空白符
StringUtils.isBlank("\b") = false //"\b"为单词边界符
StringUtils.isBlank("bob") = false
StringUtils.isBlank(" bob ") = false
```

2.public static boolean isEmpty(String str)
判断某字符串是否为空，为空的标准是str==null或str.length()==0
下面是StringUtils判断是否为空的示例：

```
StringUtils.isEmpty(null) = true
StringUtils.isEmpty("") = true
StringUtils.isEmpty(" ") = false //注意在StringUtils中空格作非空处理
StringUtils.isEmpty(" ") = false
StringUtils.isEmpty("bob") = false
StringUtils.isEmpty(" bob ") = false
```

##Java反射

关于获取类的字段有两种方式：getFields()和getDeclaredFields()。我们先来看看这两者的区别吧：

getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。 
getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。

##java 对象的序列化和反序列化

Java序列化是指把**Java对象转换为字节序列**的过程，Java反序列化是指把**字节序列恢复为Java对象**的过程。反序列化：客户端重文件，或者**网络中获取到文件以后，在内存中重构对象**。序列化：对象序列化的最重要的作用是传递和保存对象的时候，保证对象的**完整性和可传递性**。方便**字节可以在网络上传输**以及保存在本地文件。

 对象序列化可以将一个对象保存到一个文件，可以将通过流的方式在网络上传输，可以将文件的内容读取转化为一个对象。所谓对象流也就是将对象的内容流化，可以对流化后的对象进行读写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对象流进行读写操作时引发的问题。

   序列化的实现：将需要被序列化的类实现serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流（如FileOutputStream)来构造一个ObjectOutputStream（对象流）对象，接着使用ObjectOutputStream对象的writeObject（Object obj）方法就可以将参数obj的对象写出，要恢复的话则用输入流。

## **为什么需要序列化和反序列化**

### **实现分布式**

核心在于**RMI**，可以利用**对象序列化**运行远程主机上的服务，实现运行的时候，就像在本地上运行Java对象一样。


### **实现递归保存对象**

进行序列化的时候，单单并不是保存一个对象，而是递归的保存一整个对象序列，即递归保存，通过反序列化，可以递归的得到一整个对象序列。

### **序列信息可以永久保存**

用于序列化的信息，可以永久保存为文件，或者保存在数据库中，在使用的时候，再次随时恢复到内存中，**实现内存中的类的信息可以永久的保存。**

### **数据格式统一**

比照**Linux的一切皆文件**的思想，同时Java也是这样的思想，让数据格式尽可能的统一，**让对象，文件，数据，等等许许多多不同的格式，都让其统一，以及保存。实现数据可以完整的传输和保存**。然后进行**反序列化还原**，即，对象还是对象，文件还是文件。

Externalization和Serialization接口的区别，其中前者接口会被恢复成为默认值，后者接口不会恢复默认值。

如果Externalization 需要恢复属性值，这里就需要重写两个抽象方法，分别是writeExternal与readExternal两个抽象方法。

Transient关键字，加上以后，可以阻止该变量被序列化到文件中，反序列化以后，变量的值设定为初始值。

##jvm内存

* JDK1.7后，常量池被放入到堆空间中。方法，变量是放在栈里面的。
* 从栈内存里面去数据要快一些。
* JVM有两类存储区：常量缓冲池和方法区
* 常量缓冲池用于存储类名称、方法和字段名称以及字符串常量。
  方法区则用于存储Java方法的字节码。

##值传递和引用传递

* 基本类型(byte--short--int--long--float--double--boolean--char)的变量总是按值传递
* 就对象而言，不是将对象本身传递给方法，而是将对象的的引用或者说对象的首地址传递给方法；引用本身是按值传递的，通过对象的引用，方法可以直接操作该对象
* 如果传递的是数组的引用，则对数组元素的后续修改可以在原始数组中反映出来（因为数组本身就是个对象，int[] a = new int[2];，这里面的int是数组元素的类型，而数组元素的修改是操作对象）

##Java中的参数传递时传值呢？还是传引用？

要说明这个问题，先要明确两点：

1. 不要试图与C进行类比，Java中没有指针的概念
2. 程序运行永远都是在**栈**中进行的，因而参数传递时，只存在**传递基本类型和对象引用**的问题。不会直接传对象本身。
   明确以上两点后。Java在方法调用传递参数时，因为没有指针，所以它都是进行传值调用（这点可以参考C的传值调用）。因此，很多书里面都说Java是进行传值调用，这点没有问题，而且也简化的C中复杂性。

但是传引用的错觉是如何造成的呢？在运行栈中，基本类型和引用的处理是一样的，都是传值，所以，如果是传引用的方法调用，也同时可以理解为“传引用值”的传值调用，即引用的处理跟基本类型是完全一样的。但是当进入被调用方法时，被传递的这个引用的值，被程序解释（或者查找）到堆中的对象，这个时候才对应到真正的对象。如果此时进行修改，修改的是引用对应的对象，而不是引用本身，即：修改的是堆中的数据。所以这个修改是可以保持的了。

对象，从某种意义上说，是由基本类型组成的。可以把一个对象看作为一棵树，对象的属性如果还是对象，则还是一颗树（即非叶子节点），基本类型则为树的叶子节点。程序参数传递时，被传递的值本身都是不能进行修改的，但是，如果这个值是一个非叶子节点（即一个对象引用），则可以修改这个节点下面的所有内容。

堆和栈中，栈是程序运行最根本的东西。程序运行可以没有堆，但是不能没有栈。而堆是为栈进行数据存储服务，说白了堆就是一块共享的内存。不过，正是因为堆和栈的分离的思想，才使得Java的垃圾回收成为可能。

Java中，栈的大小通过-Xss来设置，当栈中存储数据比较多时，需要适当调大这个值，否则会出现java.lang.StackOverflowError异常。常见的出现这个异常的是无法返回的递归，因为此时栈中保存的信息都是方法返回的记录点。

## Jvm性能监控

###  jconsole 

直接在jdk/bin目录下点击jconsole.exe即可启动，或者 执行jconsole 命令 

在弹出的框中可以选择本机的监控本机的java应用，也可以选择远程的java服务来监控，如果监控远程服务需要在tomcat启动脚本中添加如下代码：

```
 -Dcom.sun.management.jmxremote.port=6969  
 -Dcom.sun.management.jmxremote.ssl=false  
 -Dcom.sun.management.jmxremote.authenticate=false
```

连接进去之后，就可以看到jconsole概览图和主要的功能：概述、内存、线程、类、VM、MBeans

**CPU Profiler简介**

社区实现的JVM Profiler很多，比如已经商用且功能强大的[JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html)，也有免费开源的产品，如[JVM-Profiler](https://github.com/uber-common/jvm-profiler)，功能各有所长。我们日常使用的Intellij IDEA最新版内部也集成了一个简单好用的Profiler，详细的介绍参见[官方Blog](https://blog.jetbrains.com/idea/2018/09/intellij-idea-2018-3-eap-git-submodules-jvm-profiler-macos-and-linux-and-more/)。

在用IDEA打开需要诊断的Java项目后，在“Preferences -> Build, Execution, Deployment -> Java Profiler”界面添加一个“CPU Profiler”，然后回到项目，单击右上角的“Run with Profiler”启动项目并开始CPU Profiling过程。一定时间后（推荐5min），在Profiler界面点击“Stop Profiling and Show Results”，即可看到Profiling的结果，包含火焰图和调用树，如下图所示：

这里要说明一下，因为我们没有在项目中引入任何依赖，仅仅是“Run with Profiler”，Profiler就能获取我们程序运行时的信息。这个功能其实是通过JVM Agent实现的，为了更好地帮助大家系统性的了解它，我们在这里先对JVM Agent做个简单的介绍。

因为字节码文件由十六进制值组成，而JVM以两个十六进制值为一组。即以字节为单位进行读取。在Java中一般是用javac命令编译源代码为字节码文件















##intern()

* intern()方法设计的初衷，就是重用String对象，以节省内存消耗。
* 使用intern() 方法创建字符串，当常量池中已经包含一个等于此 String 对象的字符串（用 equals(Object) 方法确定），则返回池中的字符串。否则，将此 String 对象添加到池中，并返回此 String 对象的引用。 

##replace和replaceAll的区别

* 1)replace的参数是char和CharSequence，即可以支持字符的替换，也支持字符串的替换(CharSequence即字符串序列的意思,说白了也就是字符串)；
* 2)replaceAll的参数是regex，即基于规则表达式的替换，比如，可以通过replaceAll("\\d", "*")把一个字符串所有的数字字符都换成星号;
* 相同点：都是全部替换，即把源字符串中的某一字符或字符串全部换成指定的字符或字符串，如果只想替换第一次出现的，可以使用replaceFirst()，这个方法也是基于规则表达式的替换，但与replaceAll()不同的是，只替换第一次出现的字符串；
* 另外，如果replaceAll()和replaceFirst()所用的参数据不是基于规则表达式的，则与replace()替换字符串的效果是一样的，即这两者也支持字符串的操作；
* 还有一点注意:：执行了替换操作后,源字符串的内容是没有发生改变的。

##StringBuilder & StringBuffer

* StringBuilder 类在 Java 5 中被提出，它和 StringBuffer 之间的最大不同在于 StringBuilder 的方法不是线程安全的（不能同步访问）。由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。
* String类是不可变类，任何对String的改变都 会引发新的String对象的生成(除了intern())；StringBuffer则是可变类，任何对它所指代的字符串的改变都不会产生新的对象。

#static

## static关键字的用途

一句话描述就是：方便在没有创建对象的情况下进行调用(方法/变量)。

### 线程不安全

静态变量：使用static关键字定义的变量。static可以修饰变量和方法，也有static静态代码块。被static修饰的成员变量和成员方法独立于该类的任何对象。也就是说，它不依赖类特定的实例，被**类的所有实例共享**。

显然，被static关键字修饰的方法或者变量不需要依赖于对象来进行访问，只要类被加载了，就可以通过类名去进行访问。因此，static对象可以在它的任何对象创建之前访问，无需引用任何对象。

### 1.static方法

static方法也成为静态方法，由于静态方法不依赖于任何对象就可以直接访问，因此对于静态方法来说，是没有this的，因为不依附于任何对象，既然都没有对象，就谈不上this了，并且由于此特性，**在静态方法中不能访问类的非静态成员变量和非静态方法**，因为非静态成员变量和非静态方法都必须依赖于具体的对象才能被调用

**2.static变量**

static变量也称为静态变量，静态变量和非静态变量的区别：

- **静态变量被所有对象共享，在内存中只有一个副本，在类初次加载的时候才会初始化**
- 非静态变量是对象所拥有的，在创建对象的时候被初始化，存在多个副本，各个对象拥有的副本互不影响

### 3.static块

构造方法用于对象的初始化。静态初始化块，用于类的初始化操作。可以提升程序性能。

在静态初始化块中不能直接访问非staic成员。

static关键字不能改变变量和方法的访问权限

static是不允许用来修饰局部变量。不要问为什么，这是Java语法的规定

\4. **特别要注意一个问题：**

​    对于被static和final修饰过的实例常量，实例本身不能再改变了，但对于一些容器类型（比如，ArrayList、HashMap）的实例变量，不可以改变容器变量本身，但可以修改容器中存放的对象，这一点在编程中用到很多。

https://blog.csdn.net/kuangay/article/details/81485324

#final

final可以修饰类、变量、方法，

- 修饰类表示该类不能被继承、
- 修饰方法表示该方法不能被重写、
- 修饰变量表示该变量是一个常量不能被重新赋值。

##java 基本数据类型

又称为原始数据类型byte,short,char,int,long,float,double,boolean，他们之间的比较应该使用（==），比较的是他们的值。

##java  复合数据类型

当复合数据类型用（==）进行比较，比较的是他们在内存中的存放地址。
当复合数据类型之间进行equals比较时，这个方法的初始行为是比较对象在堆内存中的地址，但在一些诸如String,Integer,Date类中把Object中的这个方法覆盖了，作用被覆盖为比较内容是否相同。

##TreeMap和HashMap

* HashMap：适用于在Map中插入、删除和定位元素。 
* Treemap：适用于按自然顺序或自定义顺序遍历键(key)。 
* HashMap通常比TreeMap快一点(树和哈希表的数据结构使然)，建议多使用HashMap，在需要排序的Map时候才用TreeMap。 

###Treemap

Treemap的实现是红黑树算法的实现，红黑树是一颗自平衡的排序二叉树；对红黑二叉树而言它主要包括三大基本操作：左旋、右旋、着色。
一棵有效的红黑树二叉树而言我们必须增加如下规则：
       * 1、每个节点都只能是红色或者黑色
       * 2、根节点是黑色
       * 3、每个叶节点（NIL节点，空节点）是黑色的。
       * 4、如果一个结点是红的，则它两个子节点都是黑的。也就是说在一条路径上不能出现相邻的两个红色结点。
       * 5、从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点。

##HashMap

当我们给put()方法传递键和值时，我们先对key调用hashCode()方法，返回hashCode**取模**来决定key会被放到数组里的位置。找到bucket位置来储存Entry对象。”这里关键点在于指出，HashMap是在bucket中储存键对象和值对象，作为Map.Entry

hashMap的数据结构是 数组＋单项链表（bucket）结构.

###取模运算

取余：rem(x,y)，遵循尽可能让商向0靠近的原则
取模：mod(x,y)，遵循尽可能让商向负无穷靠近的原则
**符号相同时**，两者不会冲突。如果都是正数，取余取模值一样。
7/3=2.3  
取余：向0 靠近，fix（2.3）=2
取模: 向负无穷靠近 floor（2.3）=2
**如果是负数**则 取余是向0 靠近，取模是向负无穷靠近 例如
7/（-3）=-2.3，在这个运算中
取余：向0 靠近，fix（-2.3）=-2
取模: 向负无穷靠近 floor（-2.3）= -3

####当两个对象的hashcode相同会发生什么？

因为hashcode相同，所以它们的bucket位置相同，‘碰撞’会发生。因为HashMap使用链表存储对象，这个Entry(包含有键值对的Map.Entry对象)会存储在链表中。

####如果两个键的hashcode相同，你如何获取值对象？

当我们调用get()方法，HashMap会使用键对象的hashcode找到bucket位置，然后获取值对象，如果有两个值对象储存在同一个bucket，找到bucket位置之后，将会遍历链表直到找到值对象，会调用keys.equals()方法去找到链表中正确的节点，最终找到要找的值对象。

####如何减少碰撞的发生

使用不可变的、声明作final的对象，并且采用合适的equals()和hashCode()方法的话，将会减少碰撞的发生，提高效率。不可变性使得能够缓存不同键的hashcode，这将提高整个获取对象的速度，使用String，Interger这样的wrapper类作为键是非常好的选择。

####如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？

默认的负载因子大小为0.75，也就是说，当一个map填满了75%的bucket时候，和其它集合类(如ArrayList等)一样，将会创建原来HashMap大小的两倍的bucket数组，来重新调整map的大小，并将原来的对象放入新的bucket数组中。这个过程叫作rehashing，因为它调用hash方法找到新的bucket位置。

####重新调整HashMap大小存在什么问题吗？

当重新调整HashMap大小的时候，**存在条件竞争**，因为如果两个线程都发现HashMap需要重新调整大小了（多线程建议使用ConcurrentHashMap），它们会同时试着调整大小。在调整大小的过程中，存储在链表中的元素的次序会反过来，因为移动到新的bucket位置的时候，HashMap并不会将元素放在链表的尾部，而是放在头部，这是为了避免尾部遍历(tail traversing)。如果条件竞争发生了，那么就死循环了。

####为什么String, Interger这样的wrapper类适合作为键？

而且String最为常用。因为String是不可变的，也是final的，而且已经重写了equals()和hashCode()方法了。其他的wrapper类也有这个特点。不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。不可变性还有其他的优点如线程安全。

####我们可以使用自定义的对象作为键吗？

当然你可能使用任何对象作为键，只要它遵守了equals()和hashCode()方法的定义规则，并且当对象插入到Map中之后将不会再改变了。如果这个自定义对象时不可变的，那么它已经满足了作为键的条件，因为当它创建之后就已经不能改变了。

####我们可以使用CocurrentHashMap来代替Hashtable吗

ConcurrentHashMap越来越多人用了。我们知道Hashtable是synchronized的，但是ConcurrentHashMap同步性能更好，因为它仅仅根据同步级别对map的一部分进行上锁。ConcurrentHashMap当然可以代替HashTable，但是HashTable提供更强的线程安全性

##HashMap在Java1.7与1.8中的区别

https://www.cnblogs.com/stevenczp/p/7028071.html

####JDK1.7中

使用一个Entry数组来存储数据，用key的hashcode**取模**来决定key会被放到数组里的位置，如果hashcode相同，或者hashcode取模后的结果相同（hash collision），那么这些key会被定位到Entry数组的同一个格子里，这些key会形成一个链表。

在hashcode特别差的情况下，比方说所有key的hashcode都相同，这个链表可能会很长，那么put/get操作都可能需要遍历这个链表也就是说时间复杂度在最差情况下会退化到O(n)

####JDK1.8中

使用一个Node数组来存储数据，但这个Node可能是链表结构，也可能是红黑树结构
如果插入的key的hashcode相同，那么这些key也会被定位到Node数组的同一个格子里。
如果同一个格子里的key不超过8个，使用链表结构存储。
如果超过了8个，那么会调用treeifyBin函数，将链表转换为红黑树。
那么即使hashcode完全相同，由于红黑树的特点，查找某个特定元素，也只需要O(log n)的开销。也就是说put/get的操作的时间复杂度最差只有O(log n)

####限制

听起来挺不错，但是真正想要利用JDK1.8的好处，有一个限制：
key的对象，必须正确的实现了Compare接口。如果没有实现Compare接口，或者实现得不正确（比方说所有Compare方法都返回0）那JDK1.8的HashMap其实还是慢于JDK1.7的

####为什么要这么操作呢？

我认为应该是为了避免Hash Collision DoS攻击
Java中String的hashcode函数的强度很弱，有心人可以很容易的构造出大量hashcode相同的String对象。如果向服务器一次提交数万个hashcode相同的字符串参数，那么可以很容易的卡死JDK1.7版本的服务器。
但是String正确的实现了Compare接口，因此在JDK1.8版本的服务器上，Hash Collision DoS不会造成不可承受的开销。

##HashMap和Hashtable

* HashMap几乎可以等价于Hashtable，除了HashMap是非synchronized的，并可以接受null(HashMap可以接受为null的键值(key)和值(value)，而Hashtable则不行)
* HashMap是非synchronized，而Hashtable是synchronized，这意味着Hashtable是线程安全的，多个线程可以共享一个Hashtable；而如果没有正确的同步的话，多个线程是不能共享HashMap的。Java 5提供了ConcurrentHashMap，它是HashTable的替代，比HashTable的扩展性更好。

##ConcurrentHashMap

数据结构 ConcurrentHashMap的目标是实现支持高并发、高吞吐量的线程安全的HashMap

一个ConcurrentHashMap由多个segment组成，每一个segment都包含了一个HashEntry数组的hashtable， 每一个segment包含了对自己的hashtable的操作，比如get，put，replace等操作，这些操作发生的时候，对自己的hashtable进行锁定。由于每一个segment写操作只锁定自己的hashtable，所以可能存在多个线程同时写的情况，性能无疑好于只有一个hashtable锁定的情况。

ConcurrentHashMap具体是线程安全的.它引入了一个“分段锁”的概念，具体可以理解为把一个大的Map拆分成N个小的HashTable，根据key.hashCode()来决定把key放到哪个HashTable中。
在ConcurrentHashMap中，就是把Map分成了N个Segment，put和get的时候，都是现根据key.hashCode()算出放到哪个Segment中


##什么是HashSet

HashSet实现了Set接口，它不允许集合中有重复的值，当我们提到HashSet时，第一件事情就是在将对象存储在HashSet之前，要先确保对象重写equals()和hashCode()方法，这样才能比较对象的值是否相等，以确保set中没有储存相等的对象。如果我们没有重写这两个方法，将会使用这个方法的默认实现。

HashSet使用**成员对象**来计算hashcode值，对于两个对象来说hashcode可能相同，所以equals()方法用来判断对象的相等性，如果两个对象不同的话，那么返回false

public boolean add(Object o)方法用来在Set中添加元素，当元素值重复时则会立即返回false，如果成功添加的话会返回true。




##Map.Entry

Map是java中的接口，Map.Entry是Map的一个内部接口。

Map提供了一些常用方法，如keySet()、entrySet()等方法，keySet()方法返回值是Map中key值的集合；entrySet()的返回值也是返回一个Set集合，此集合的类型为Map.Entry。

Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry<K,V>。它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。

###Map.Entry使用

你是否已经对每次从Map中取得关键字然后再取得相应的值感觉厌倦？使用Map.Entry类，你可以得到在同一时间得到所有的信息。

```
 //第三种：推荐，尤其是容量大时
      System.out.println("通过Map.entrySet遍历key和value");
      for (Map.Entry<String, String> entry : map.entrySet()) {
       System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
      }
```

##java范型中E，T，？的区别？

自定义泛型类时，类持有者名称可以使用T(Type，一般指类) 超类

如果是容器的元素可以使用E(Element，一般指方法)若键值匹配可以用K(Key)和V(Value)等， 

若是<?>（一般指参数），则是默认是允许Object及其下的子类，也就是java的所有对象了。 

```
Set<T> 表示 集合里 是   T类的实例 
List<E> 表示  集合里 是  E类的实例 
List<?> 表示 集合里的对象类型不确定，未指定 
List 同 List<?> 是一样的。 
```

也就是说可以随便写咯

##Integer.valueof(String s)和Integer.parseInt(String s)的具体区别

Integer.valueof(String s)是将一个包装类,是一个实际值为数字的变量先转成string型再将它转成Integer型的包装类对象(相当于转成了int的对象)这样转完的对象就具有方法和属性了。
而Integer.parseInt(String s)只是将是数字的字符串转成数字，注意他返回的是int型变量不具备方法和属性。

设有下面两个赋值语句：
a=Integer.parseInt(“123”);
b=Integer.valueOf(“123”).intValue();
下述说法正确的是（d）。
A、a是整数类型变量，b是整数类对象。
B、a是整数类对象，b是整数类型变量。
C、a和b都是整数类对象并且值相等。
D、a和b都是整数类型变量并且值相等。

###解释：

parseInt(Strings)方法是类Integer的静态方法，它的作用就是将形参s转化为整数，比如：

```
Interger.parseInt("1")=1;
Integer.parseInt("20")=20;
Integer.parseInt("324")=324;
```

当然，s表示的整数必须合法，不然是会抛异常的。
valueOf(Strings)也是Integer类的静态方法，它的作用是将形参s转化为Integer对象，
什么是Integer对象，Integer就是基本数据类型int型包装类，就是将int包装成一个类，这样在很多场合下是必须的。如果理解不了，你就认为int是Integer的mini版，好用了很多，但也丢失了一些功能，好了，看代码：
Interger.valueOf("123")=Integer(123)
这时候Integer（123）就是整数123的对象表示形式，它再调用intValue()方法，就是将123的对象表示形式转化为基本数据123
所以，选择D

##Long和long区别

Long类型与long类型的区别导致的。Long类型变量表示的是一个对象，其值是对象的一个属性。相当于一个Long变量中包含了一个long类型的值。所以当进行两个Long类型变量比较时如果用的是==来比较，那么实际比较的是两个Long型变量指向的对象的地址，显然两个不同Long对象地址是不同的。

通过Long类型的equals方法来比较对象的值。equals方法是获取Long类型存储的值和其他Long对象或者值进行比较。如果equals的参数是Long类型，那么会取得参数的值与调用的对象的值进行比较，进而得出符合我们预期的结果



# java8

##java8 interface 的默认方法与abstract class的非抽象方法的区别？

1.语法层面上的区别

　　1）抽象类可以提供成员方法的实现细节，而接口中只能存在public abstract 方法；

　　2）抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是public static final类型的；

　　3）接口中不能含有静态代码块以及静态方法，而抽象类可以有静态代码块和静态方法；

　　4）一个类只能继承一个抽象类，而一个类却可以实现多个接口。

2.设计层面上的区别

　　1）抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。举个简单的例子，飞机和鸟是不同类的事物，但是它们都有一个共性，就是都会飞。那么在设计的时候，可以将飞机设计为一个类Airplane，将鸟设计为一个类Bird，但是不能将 飞行 这个特性也设计为类，因此它只是一个行为特性，并不是对一类事物的抽象描述。此时可以将 飞行 设计为一个接口Fly，包含方法fly( )，然后Airplane和Bird分别根据自己的需要实现Fly这个接口。然后至于有不同种类的飞机，比如战斗机、民用飞机等直接继承Airplane即可，对于鸟也是类似的，不同种类的鸟直接继承Bird类即可。从这里可以看出，继承是一个 "是不是"的关系，而 接口 实现则是 "有没有"的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，而接口实现则是有没有、具备不具备的关系，比如鸟是否能飞（或者是否具备飞行这个特点），能飞行则可以实现这个接口，不能飞行就不实现这个接口。

　　2）设计层面不同，抽象类作为很多子类的父类，它是一种模板式设计。而接口是一种行为规范，它是一种辐射式设计。什么是模板式设计？最简单例子，大家都用过ppt里面的模板，如果用模板A设计了ppt B和ppt C，ppt B和ppt C公共的部分就是模板A了，如果它们的公共部分需要改动，则只需要改动模板A就可以了，不需要重新对ppt B和ppt C进行改动。而辐射式设计，比如某个电梯都装了某种报警器，一旦要更新报警器，就必须全部更新。也就是说对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。
　　

## Java 8 Optional 

* 静态方法 `empty()` 创建一个空的 Optional 对象 Optional<String> empty = Optional.empty();

* 静态方法 `of()` 创建一个非空的 Optional 对象  传递给 `of()` 方法的参数必须是非空的，也就是说不能为 null，否则仍然会抛出 NullPointerException。Optional<String> opt = Optional.of("沉默王二");

* 静态方法 `ofNullable()` 创建一个即可空又可非空的 Optional 对象 Optional<String> optOrNull = Optional.ofNullable(name);

* 静态方法`ofNullable()` 方法内部有一个三元表达式，如果为参数为 null，则返回私有常量 EMPTY；否则使用 new 关键字创建了一个新的 Optional 对象——不会再抛出 NPE 异常了。

* `isPresent()` 判断一个 Optional 对象是否存在，如果存在，该方法返回 true

* `ifPresent()`，允许我们使用函数式编程的方式执行一些代码，因此，我把它称为非空表达式。

  ```
  Optional<String> opt = Optional.of("沉默王二");
  opt.ifPresent(str -> System.out.println(str.length()));
  ```

  


## 分析Java Swap 内存运行情况

1. 使用 Linux top 命令查看各进程使用内存情况  **RES** 为 **实际使用的物理内存**
2. 在项目中添加**`-XX:NativeMemoryTracking=detail`** JVM参数重启项目，使用命令 **`jcmd pid VM.native_memory detail`** 查看到的内存分布
3. 使用命令`gdp -pid pid`进入GDB之后，然后使用命令`dump memory mem.bin startAddress endAddress`dump内存，其中startAddress和endAddress可以从/proc/pid/smaps中查找。然后使用`strings mem.bin`查看dump的内容
4. 项目启动使用strace追踪系统调用，使用该mmap申请的地址空间在pmap对应
5. 因为strace命令中已经显示申请内存的线程ID。直接使用命令`jstack pid`去查看线程栈，找到对应的线程栈（注意10进制和16进制转换）







HashMap是数组+链表+红黑树（JDK1.8增加了红黑树部分）实现的



(1) 从源码可知，HashMap类中有一个非常重要的字段，就是 Node[] table，即哈希桶数组，明显它是一个Node的数组。**Node是HashMap的一个内部类，实现了Map.Entry接口，本质是就是一个映射(键值对)。**上图中的每个黑色圆点就是一个Node对象



(2) HashMap就是使用哈希表来存储的。哈希表为解决冲突，可以采用开放地址法和链地址法等来解决问题，Java中HashMap采用了链地址法。链地址法，简单来说，就是数组加链表的结合。在每个数组元素上都一个链表结构，当数据被Hash后，得到数组下标，把数据放在对应下标元素的链表上。



**1. get方法**

取key的hashCode值、高位运算、取模运算



首先想到的就是把hash值对数组长度取模运算。**取模**运算的消耗还是比较大的，在HashMap中是这样做的

\```

hash=(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

//n = tab.length

first = tab[(n - 1) & hash]) != null

\```

来计算该对象应该保存在table数组的哪个索引处。



而HashMap底层数组的长度总是2的n次方，这是HashMap在速度上的优化。当length总是2的n次方时，`tab[(n - 1) & hash]`运算等价于对length取模，也就是 **hash%length，**但是&比%具有更高的效率。此处关于& 运算不清楚的小伙伴 请移步

[与或非运算](https://blog.csdn.net/u012373815/article/details/78777047)



在JDK1.8的实现中，**优化了高位运算的算法，通过hashCode()的高16位 异或 低16位实现的：(h = k.hashCode()) ^ (h >>> 16)**，主要是从速度、功效、质量来考虑的，**这么做可以在数组table的length比较小的时候，也能保证考虑到高低Bit都参与到Hash的计算中**，同时不会有太大的开销。





**2. put 方法**

当我们给put()方法传递键和值时，我们先对key调用hashCode()方法，返回hashCode 进行低16位 异或 高16位  然后**取模**来决定key会被放到数组里的位 置。之后进行判断如下 步骤：

// Node<K,V>[] tab;

①.判断键值对数组table[i]是否为空或为null，否则执行resize()进行扩容；



②.根据键值key计算hash值得到插入的数组索引i，如果table[i]==null，直接新建节点添加，转向⑥，如果table[i]不为空，转向③；



③.判断table[i]的首个元素是否和key一样，如果相同直接覆盖value，否则转向④，这里的相同指的是hashCode以及equals；



④.判断table[i] 是否为treeNode，即table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对，否则转向⑤；



⑤.遍历table[i]，判断链表长度是否大于8，大于8的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作；遍历过程中若发现key已经存在直接覆盖value即可；



⑥.插入成功后，判断实际存在的键值对数量size是否超多了最大容量threshold，如果超过，进行扩容。





### 3. 取模运算

取余：rem(x,y)，遵循尽可能让**商**向0靠近的原则

取模：mod(x,y)，遵循尽可能让**商**向负无穷靠近的原则



**符号相同时**，两者不会冲突。如果都是正数，取余取模值一样。

7/3=2.3  

取余：向0 靠近，fix（2.3）=2

取模: 向负无穷靠近 floor（2.3）= 2



**如果是负数**则 取余是向0 靠近，取模是向负无穷靠近 例如

7/（-3）=-2.3，在这个运算中



取余：向0 靠近，fix（-2.3）=-2

取模: 向负无穷靠近 floor（-2.3）= -3



#### **4. 当两个对象的hashcode相同会发生什么？**



因为hashcode相同，所以它们的bucket位置相同，‘碰撞’会发生。因为HashMap使用链表存储对象，这个Entry(包含有键值对的Map.Entry对象)会存储在链表中。

####  

#### 5.如果两个键的hashcode相同，你如何获取值对象？

当我们调用get()方法，HashMap会使用键对象的hashcode找到bucket位置，然后获取值对象，如果有两个值对象储存在同一个bucket，找到bucket位置之后，将会遍历链表对比key的值直到找到值对象，会调用keys.equals()方法去找到链表中正确的节点，最终找到要找的值对象。



#### 5.如何减少碰撞的发生

**使用不可变的、声明作final的对象**，并且采用合适的equals()和hashCode()方法的话，将会减少碰撞的发生，提高效率。不可变性使得能够缓存不同键的hashcode，这将提高整个获取对象的速度，使用String，Interger这样的wrapper类（Java封装类）作为键是非常好的选择。

####  

#### 6. 如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？



默认的负载因子大小为0.75（符合泊松分布），也就是说，当一个map填满了75%的bucket时候，和其它集合类(如ArrayList等)一样，将会创建原来HashMap大小的**2 倍**的bucket数组，来重新调整map的大小，并将原来的对象放入新的bucket数组中。这个过程叫作rehashing，因为它调用hash方法找到新的bucket位置。





#### 7.重新调整HashMap大小存在什么问题吗？



当重新调整HashMap大小的时候，**存在条件竞争**，因为如果两个线程都发现HashMap需要重新调整大小了**（多线程建议使用ConcurrentHashMap）**，它们会同时试着调整大小。在调整大小的过程中，**存储在链表中的元素的次序会反过来，因为移动到新的bucket位置的时候，HashMap并不会将元素放在链表的尾部，而是放在头部，这是为了避免尾部遍历(tail traversing)**。如果条件竞争发生了，**那么就死循环了**。



https://blog.csdn.net/xuefeng0707/article/details/40797085





#### 8.为什么String, Interger这样的wrapper类适合作为键？



而且String最为常用。因为String是不可变的，也是final的，而且已经重写了equals()和hashCode()方法了。其他的wrapper类也有这个特点。不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。不可变性还有其他的优点如线程安全。



#### 9.我们可以使用自定义的对象作为键吗？

当然你可能使用任何对象作为键，只要它遵守了equals()和hashCode()方法的定义规则，并且当对象插入到Map中之后将不会再改变了。如果这个自定义对象时不可变的，那么它已经满足了作为键的条件，因为当它创建之后就已经不能改变了。







美团技术团队，hashmap

https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651745258&idx=1&sn=df5ffe0fd505a290d49095b3d794ae7a&scene=21#wechat_redirect











# 集合

# Queue接口

　　Queue用于模拟了**队列**这种数据结构，队列通常是指“先进先出”(FIFO)的容器。队列的头部保存在队列中时间最长的元素，队列的尾部保存在队列中时间最短的元素。新元素**插入(offer)**到队列的尾部，**访问元素(poll)**操作会返回队列头部的元素。通常，**队列不容许随机访问队列中的元素。**

#### **常用方法**

```java
1. 入队

**void add(Object o):** 指定元素加入队列尾部

boolean offer(Object o)：同上，在有限容量队列中，此方法更好

2. 出队

**Object poll()：**获取头部元素，并从队列中删除；如果队列为空，则返回null

Object remove()：获取头部元素，并从队列中删除；

3. 出队不删除

**Object peek()：**获取头部元素，不删除；如果队列为空，则返回null

Object element()：获取头部元素，不删除；
```

**Queue有两个常用的实现类**：**LinkedList和PriorityQueue**，下面分别介绍这两个实现类。

##1.  **LinkedList**（**双向链表**）

#### 结构

   ```java
       /**
        * Pointer to first node.
        * Invariant: (first == null && last == null) ||
        *            (first.prev == null && first.item != null)
        */
       transient Node<E> first;
   
       /**
        * Pointer to last node.
        * Invariant: (first == null && last == null) ||
        *            (last.next == null && last.item != null)
        */
       transient Node<E> last;
   
       private static class Node<E> {
           E item;
           Node<E> next;
           Node<E> prev;
   
   ```

#### 常用方法

```java
      1. void addFirst(Object e);   //将指定元素插入该双向队列的开头。
      2. void addLast(Object e);　　//将指定元素插入该双向队列的末尾。
      3. Iterator descendingIterator(); //返回以该双向队列对应的迭代器，该迭代器将以逆向顺序来迭代队列中的元素。
      4. Object getFirst();　　　　　　//获取、但不删除双向队列的第一个元素。
      5. Object getLast();　　      //获取、但不删除双向队列的最后一个元素。
      6. boolean offerFirst(Object e);//将指定元素插入该双向队列的开头
      7. boolean offerLast(Object e);//将指定元素插入该双向队列的结尾
      8. Object peekFirst();       //获取、但不删除双向队列的第一个元素；如果此双端队列为空，则返回null。
      9. Object peekLast();       //获取、但不删除该双向队列的最后一个元素；如果此双端队列为空，则返回null。
      10. Object pollFirst();       //获取、并删除双向队列的第一个元素；如果此双端队列为空，则返回null。
      11. Object pollLast();       //获取、并删除双向队列的最后一个元素，如果此双端队列为空，则返回null。
      12. Object pop();          //pop出该双向队列所表示的栈中第一个元素。
      13. void push(Object e);     //将一个元素push进该双向队列所表示的栈中。
      14. Object removeFirst();    //获取、并删除该双向队列的第一个元素。
      15. Object removeFirstOccurrence(Object e);  //删除该双向队列的第一次的出现元素e。
      16. removeLast();　　          //获取、并删除该双向队列的最后一个元素。
      17. removeLastOccurrence(Object e);  //删除该双向队列的最后一次的出现元素e   
```

   LinkedList类实现是**双向链表** 是一个比较奇怪的类**，它**即是List接口的实现类**，这意味着它**是一个List集合**，可以根据索引来随机访问集合中的元素。除此之外，LinkedList**还实现了Deque接口**，Deque接口是Queue接口的子接口，它**代表一个双向队列，Deque接口里定义了一些可以双向操作队列的方法：

   LinkedList不仅**可以当成双向队列**使用，也可以当成“栈”使用**，因为该类还包含了pop(出栈)和push(入栈)两个方法。除此之外，**LinkedList实现了List接口，所以还被当成List使用。LinkedList同时实现了stack、Queue、PriorityQueue的所有功能。

#### 使用方式：

   ```java
   public class TestLinkedList {
    
         public static void main(String[] args) {
             LinkedList<String> ll = new LinkedList<>();
             //入队
             ll.offer("AAA");
             //压栈
             ll.push("BBB");
             //双端的另一端入队
            ll.addFirst("NNN");
            ll.forEach(str -> System.out.println("遍历中:" + str));
            //获取队头
            System.out.println(ll.peekFirst());
            //获取队尾
            System.out.println(ll.peekLast());
            //弹栈
            System.out.println(ll.pop());
            System.out.println(ll);
            //双端的后端出列
            System.out.println(ll.pollLast());
            System.out.println(ll);
        }
    }
   ```

#### 建议：

1. 如果需要遍历List集合元素，**对于ArrayList、Vector集合，则应该使用随机访问方法(get)来遍历集合元素**，这样性能更好，对于LinkedList集合，则应该采用迭代器(Iterater)来遍历集合元素性能会和arrayList差不多，for循环则获取每个元素都会会循环查找链表的前半段。
2. 如果需要经常执行插入（随机插入非首尾插入）、删除操作来改变List集合大小，则应该使用LinkedList集合，而不是ArrayList。使用ArrayList、Vector集合将需要**经常重新分配内存数组**的大小，其时间开销往往是使用LinkedList时时间开销的几十倍，效果很差。
3. 如果有多条线程需要同时访问List集合中的元素，可以考虑使用Vector这个同步实现。
4. 如果你的程序强调对元素的增、删、改、查、遍历等操作就用**LinkedList或者ArrayList;**
   如果是强调对象进入容器和对象从容器出来时的先后关系，那就**用Stack、Queue、PriorityQueue**

* ### 1.1 ArrayList

 ```java
     transient Object[] elementData; // non-private to simplify nested class access
 
     /**
      * The size of the ArrayList (the number of elements it contains).
      *
      * @serial
      */
     private int size;
 ```

 **ArrayList** 是数组 所以 add（0，e） 是在内存中**System.arrayCopy（文末有介绍）** 拷贝一个新的数组，并将后面的所有元素位置+1

#### arrayList 和  LinkedList性能比较总结

新增

* 如果是从集合的头部新增元素，ArrayList 花费的时间应该比 LinkedList 多，因为需要对头部以后的元素进行复制。
* 如果是从集合的中间位置新增元素，ArrayList 花费的时间搞不好要比 LinkedList 少，因为 LinkedList 需要遍历，但是ArrayList要复制后半段数组元素。
* 如果是从集合的尾部新增元素，ArrayList 花费的时间应该比 LinkedList 少（不涉及到ArrayList扩容），因为数组是一段连续的内存空间，也不需要复制数组；而链表需要创建新的对象，前后引用也要重新排列。

删除（基本和新增保持一致）

- 从集合头部删除元素时，ArrayList 花费的时间比 LinkedList 多很多；
- 从集合中间位置删除元素时，ArrayList 花费的时间比 LinkedList 少很多；
- 从集合尾部删除元素时，ArrayList 花费的时间比 LinkedList 少一点。

遍历查找：

* 查找arraylist 速度优于LinkedList。for 循环遍历的时候，ArrayList 花费的时间远小于 LinkedList
* 遍历时对于LinkedList集合，则应该采用迭代器(Iterater)来遍历集合元素性能会和arrayList差不多，for循环则获取每个元素都会会循环查找链表的前半段。

性能比较可参考 ：https://zhuanlan.zhihu.com/p/260424337


## 2. PriorityQueue实现类(优先队列)

#### 结构

  ```
    transient Object[] queue; // non-private to simplify nested class access
  
      /**
       * The number of elements in the priority queue.
       */
      private int size = 0;
  
      /**
       * The comparator, or null if priority queue uses elements'
       * natural ordering.
       */
      private final Comparator<? super E> comparator;
  ```



  PriorityQueue是Queue接口的实现类，但是它并不是一个FIFO的队列实现，**PriorityQueue保存队列元素的顺序并不是按加入队列的顺序，而是按队列元素的大小进行重新排序。**具体表现在：

  1. 保存顺序与FIFO无关，而是按照元素大小进行重排序；因此poll出来的是按照有小到大来取。

  2. 不允许保存null，排序规则有自然排序和定制排序两种，规则与TreeSet一致。

  PriorityQueue增加元素底层也是 System.arraycopy 方法

优先队列，PriorityQueue 线程不安全，多线程使用 PriorityBlockingQueue

##3.  **Deque接口与ArrayDeque实现类 ** 实现了Queue接口（ **循环数组** ）

Deque实现的是一个双端队列，因此它具有“FIFO队列”及“栈”的方法特性，其中ArrayDeque是其典型的实现类。

#### 结构

```java
    transient Object[] elements; // non-private to simplify nested class access

    /**
     * The index of the element at the head of the deque (which is the
     * element that would be removed by remove() or pop()); or an
     * arbitrary number equal to tail if the deque is empty.
     */
    transient int head;

    /**
     * The index at which the next element would be added to the tail
     * of the deque (via addLast(E), add(E), or push(E)).
     */
    transient int tail;
```

通过**数组**实现队列，在队列中存在两个指针，一个指向头部，一个指向尾部。队列的入队，出队通过**System.arraycopy** 方法 拷贝生成新的数组实现。有元素变化则使用 System.arraycopy 进行内存拷贝数组

#### 使用

```java
1. ArrayDeque的栈实现

public class ArrayDequeQueue {
 
      public static void main(String[] args) {
          ArrayDeque<String> queue = new ArrayDeque<>();
          //入队
          queue.offer("AAA");
          queue.offer("BBB");
          queue.offer("CCC");
          System.out.println(queue);
         //获取但不出队
         System.out.println(queue.peek());
         System.out.println(queue);
         //出队
         System.out.println(queue.poll());
         System.out.println(queue);
     }
 
 }
2. ArrayDeque的FIFO队列实现

  public class ArrayDequeQueue {
  
      public static void main(String[] args) {
          ArrayDeque<String> queue = new ArrayDeque<>();
          //入队
          queue.offer("AAA");
          queue.offer("BBB");
          queue.offer("CCC");
          System.out.println(queue);
         //获取但不出队
         System.out.println(queue.peek());
         System.out.println(queue);
         //出队
         System.out.println(queue.poll());
         System.out.println(queue);
     }
 
 }

```

## 对比：

### 接口实现 

> ArrayDeque和LinkedList都实现了Serializable和Cloneable接口，支持**序列化和****克隆操作**
>
> PriorityQueue只实现了Serializable接口，支持**序列化操作**

### 时间复杂度（以队列看）

   1.ArrayDeque：

> **add**时间复杂度：O(n)
>
> **remove**时间复杂度：O(n)
>
> **get**时间复杂度：O(1)

  2.LinkedList：

> **add**时间复杂度：O(1)
>
> **remove**时间复杂度：O(1)
>
> **get**时间复杂度：O(n)

   3.PriorityQueue：

> **get**时间复杂度：O(log(N))
>
> **add/offer**时间复杂度：O(log(N))
>
> **remove/poll**时间复杂度：O(log(N))

### 特点

> ArrayDeque：双端队列，线程不安全，性能高于LinkedList，不允许插入null元素
>
> LinkedList：双端队列，线程不安全，首尾元素操作效率高，低效随机访问
>
> PriorityQueue：线程不安全，不允许插入null元素，动态数组实现最小堆，remove方法一直返回最小元素

# 扩展

### **System.arraycopy**

  **System.arraycopy** 是浅拷贝，System.arraycopy() 函数是 native 函数，即原生态方法，是直接对内存进行复制，减少了寻址时间，自然效率更高。  

  **System.arraycopy**在复制时是值传递，但是在进行复制时，首先检查了字符串常量池中是否存在该字面量，如果存在则返回对应的内存地址，如果不存在则在内存中开辟空间保存对应的对象。

 **System.arraycopy**在复制一维数组时，目标数组修改不会影响原来数据，是值传递，修改副本不会影响原有值。在复制二维数组时，（二维数组的第一维装的是一个一维数组的引用，第二维里是元素数值）对二维数进行复制后，第一维的引用被复制给新数组的地一维，也就是两个数组的第一维都指向相同的那些数组。此时改变期中任何一个元素的值，原数组和新数组的元素值都会变化。

  **System.arraycopy** 是线程不安全的 ，是 **native 函数**

## native 函数

运行时实际运行的是 c++ 所以不会占用堆内存

JVM本身也是一个动态链接库（内部有**class文件的解释器**），它加载类和解释执行的效率不如直接编译的C++高。再有就是，Java设计系统API等底层操作时可能无能为力。一些经常调用的函数，或者和操作系统交互的函数必须用其他语言来完成。

Java中的JNI（Java Native Interface）就是实现native方法的途径。它通过C/C++的编程接口（头文件）来达到和C/C++交互的目的。

我们先来看一下，native方法的执行过程。

* 首先，在类被加载时，需要加载native方法实现的动态链接库，因此这段加载代码必须是静态加载器中的程序段。

* 当JVM执行到native函数时，查找已经加载好的动态链接库，如果找到对应函数的实现，则把执行权转交操作系统，操作系统将进程调度至动态链接库，开始函数执行，Java程序则等待其返回值；如果未找到则报错（属于Error型异常，也就是JVM级别的异常，不可捕获）。

```java
public class Main {

    static {
        System.load("D:\\jni.dll");
    }

    public native static void hello(); // 必须有static，因为静态函数不能直接调用同一个类的非静态函数

    public static void main(String[] args) {
        hello();
    }
}
```

这个hello函数就是我们要实现的native函数，功能是输出字符串“Hello World”。在这个主类中，需要加载`D:\jni.dll`，所以把它写在static初始化器中。

接下来，我们需要编译出class文件，并生成一个C++的头（.h）文件。我们单击idea的一键编译运行即可。这次运行是一定会报错的，提示无法加载动态链接库。但我们不需要运行，只需要那个class文件。

找到 class 文件通过 javah -jni Main 命令生成 Main.h文件。然后打开编辑 Main.h 这个C++ 语言文件，编写函数，然后编译生成 `jni.dll`文件 放到  “D:\\jni.dll” 位置。

再次运行Java程序，可见输出`Hello World!`（c++ 函数的功能为输出 hello world）字样。这样，我们的native函数就圆满成功了





# Arrays.sort 解析

Arrays.sort并不是单一的排序，而是插入排序，快速排序，归并排序三种排序的组合

https://www.jianshu.com/p/d7ba7d919b80 分析

![arraySort](/Users/yangyibo/Documents/技能点/整理知识点图/arraySort.png)

元素少于47这个阀值，就用插入排序

大过INSERTION_SORT_THRESHOLD（47) < 286  的，用一种快速排序的方法：
 1.从数列中挑出五个元素，称为 “基准”（pivot）；
 2.重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
 3.递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。

大于286的，它会进入归并排序（Merge Sort）