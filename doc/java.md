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

Java NIO(new/inputstream outputstream)使用通道、缓冲来操作流，所以要深刻理解这些概念，尤其是，缓冲中的数据结构（当前位置(position)、限制(limit)、容量(capacity)）,这些知识点要通过写程序慢慢体会。


## NIO vs  传统IO

NIO是面向缓冲、通道的；传统IO面向流

通道是双向的既可以写、也可以读；传统IO只能是单向的

NIO可以设置为异步；传统IO只能是阻塞，同步的

 

## 缓冲区结构图

NIO是面向缓冲区的，缓冲区可以理解为一块内存，有大小。缓冲区有位置、界限、容量几个概念。

capacity：容量，缓冲区的大小

limit：限制，表示最大的可读写的数量

position：当前位置，每当读写，当前位置都会加一

flip和clear方法，内部就操作这三个变量。

 

## 缓冲区常用方法

clear：将当前位置设置为0，限制设置为容量，目的是尽最大可能让字节，由通道读取到缓冲中

flip：当前位置置为限制，然后将当前位置置为0，目的是将有数据部分的字节，由缓冲写入到通道中。通常用在读与写之间。



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

 对象序列化可以将一个对象保存到一个文件，可以将通过流的方式在网络上传输，可以将文件的内容读取转化为一个对象。所谓对象流也就是将对象的内容流化，可以对流化后的对象进行读写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对象流进行读写操作时引发的问题。

   序列化的实现：将需要被序列化的类实现serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流（如FileOutputStream)来构造一个ObjectOutputStream（对象流）对象，接着使用ObjectOutputStream对象的writeObject（Object obj）方法就可以将参数obj的对象写出，要恢复的话则用输入流。


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

#final



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



