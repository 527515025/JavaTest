# 操作



##GC

新生代由于其对象存活时间短，且需要经常gc，因此采用效率较高的复制算法，其将内存区分为一个eden区和两个suvivor区，eden区和survivor区的比例是8:1，分配内存时先分配eden区，当eden区满时，使用**复制算法**进行gc，将存活对象复制到一个survivor区，当一个survivor区满时，将其存活对象复制到另一个区中，当对象存活时间大于某一阈值时，将其放入老年代。 
老年代和永久代因为其存活对象时间长，因此使用**标记清除或标记整理算法**

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

# jvm 的堆、栈、方法区

https://www.cnblogs.com/andy-zhou/p/5327288.html
https://www.cnblogs.com/woshimrf/p/jvm-garbage.html
http://hoyouly.top/2018/04/08/jvm/
https://blog.csdn.net/qq_31337311/article/details/78799262
https://blog.csdn.net/u012998254/article/details/81428621

###0、栈是运行时单位，而堆是存储的单位。

栈解决程序的运行问题，即程序如何执行，或者说如何处理数据；堆解决的是数据存储的问题，即数据怎么放、放在哪儿。

在Java中一个线程就会相应有一个线程栈与之对应，这点很容易理解，因为不同的线程执行逻辑有所不同，因此需要一个独立的线程栈。而堆则是所有线程共享的。栈因为是运行单位，因此里面存储的信息都是跟当前线程（或程序）相关信息的。包括局部变量、程序运行状态、方法返回值等等；而堆只负责存储对象信息。

### 1、java中的栈（stack）和堆（heap）是java在内存（ram）中存放数据的地方

* （1）当程序运行时，首先通过类装载器加载字节码文件，经过解析后装入方法区！在方法区中存了类的各种信息，包括类变量、常量及方法。对于同一个方法的调用，同一个类的不同实例调用的都是存在方法区的同一个方法。类变量的生命周期从程序开始运行时创建，到程序终止运行时结束！ 
* （2）当程序中new一个对象时，这个对象存在堆中，对象的变量存在栈中，指向堆中的引用！对象的成员变量都存在堆中，当对象被回收时，对象的成员变量随之消失！ 
* （3）当方法调用时，JVM会在栈中分配一个栈桢，存储方法的局部变量。当方法调用结束时，局部变量消失！

###2、堆中存什么？栈中存什么？

堆中存的是对象。栈中存的是基本数据类型和堆中对象的引用。一个对象的大小是不可估计的，或者说是可以动态变化的，但是在栈中，一个对象只对应了一个4btye的引用（堆栈分离的好处：））。

为什么不把基本类型放堆中呢？因为其占用的空间一般是1~8个字节——需要空间比较少，而且因为是基本类型，所以不会出现动态增长的情况——长度固定，因此栈中存储就够了，如果把他存在堆中是没有什么意义的（还会浪费空间，后面说明）。可以这么说，基本类型和对象的引用都是存放在栈中，而且都是几个字节的一个数，因此在程序运行时，他们的处理方式是统一的。但是基本类型、对象引用和对象本身就有所区别了，因为一个是栈中的数据一个是堆中的数据。最常见的一个问题就是，Java中参数传递时的问题。

对象的属性其实就是数据，存放在堆中；而对象的行为（方法），就是运行逻辑，放在栈中。

### 3、堆区  对象、成员变量

* 存储的全部是对象，每个对象都包含一个与之对应的class的信息.（class的目的是得到操作指令）；
* jvm只有一个heap区，被所有**线程共享**，不存放基本类型和对象引用，只存放对象本身。
* **堆的优劣势**：堆的优势是可以动态的分配内存大小，生存期也不必事先告诉编译器，java的垃圾收集器会自动收取这些不在使用的数据，但缺点是，由于要在运行时动态分配内存，存取速度慢。

### 3、栈区 局部变量 

* 每一个线程包含一个stack区，只保存**基本数据类型的对象和自定义对象的引用（不是对象**），对象都存放在共享heap中；每个栈中的数据（基本数据类型和对象引用）都是私有的，其他栈不能访问；栈分为3部分：基**本类型变量区**、**执行环境上下文**、操作指令区（存放操作指令）
* 栈的优势劣势：**存取速度比堆要快**，仅次于直接位于CPU的寄存器，但**必须确定的是存在stack中的数据大小与生存期**必须是确定的，**缺乏灵活性**。
* 单个stack的数据可以共享。在java中，所有基本类型和引用类型都在stack中储存，栈中数据的生存空间一般在当前scopes内

####方法区（Method Area）

方法区（Method Area）：与Java堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap（非堆），目的应该是与Java堆区分开来。很多人都更愿意把方法区称为“永久代”（Permanent Generation）。从jdk1.7已经开始准备“去永久代”的规划，jdk1.7的HotSpot中，已经把**原本放在方法区中的静态变量、字符串常量池等移到堆内存**中。

**在jdk1.8中，永久代已经不存在，存储的类信息、编译后的代码数据等已经移动到了元空间**（MetaSpace）中，元空间并没有处于堆内存上，而是直接占用的本地内存（NativeMemory）

元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代之间最大的区别在于：元空间并不在虚拟机中，而是使用本地内存。因此，默认情况下，元空间的大小仅受本地内存限制，但可以通过以下参数来指定元空间的大小

### 4、方法区 类信息、类变量（静态变量和常量）、方法

* 1、方法区又叫静态区，跟堆一样，被所有的线程共享。方法区包含所有的class和static变量；
* 2、方法区中包含的都是在程序中永远的唯一的元素

* 常量池 (放在方法区)
  * Java中的常量池，实际上分为两种形态：静态常量池和运行时常量池。
  * 所谓静态常量池，即*.class文件中的常量池，class文件中的常量池不仅仅包含字符串(数字)字面量，还包含类、方法的信息，占用class文件绝大部分空间。
  * 而运行时常量池，则是jvm虚拟机在完成类装载操作后，将class文件中的常量池载入到内存中，并保存在方法区中，我们常说的常量池，就是指方法区中的运行时常量池
  * 方法区（Method Area）与Java堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap（非堆），目的应该是与Java堆区分开来。
  * 栈：在Java中，JVM中的栈记录了线程的方法调用。每个线程拥有一个栈。在某个线程的运行过程中，如果有新的方法调用，那么该线程对应的栈就会增加一个存储单元，即帧(frame)。在frame中，保存有该方法调用的参数、局部变量和返回地址。
  * 堆：是JVM中一块可自由分配给对象的区域。当我们谈论垃圾回收(garbage collection)时，我们主要回收堆(heap)的空间。
    Java的普通对象存活在堆中。与栈不同，堆的空间不会随着方法调用结束而清空。因此，在某个方法中创建的对象，可以在方法调用结束之后，继续存在于堆中。这带来的一个问题是，如果我们不断的创建新的对象，内存空间将最终消耗殆尽。

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

## 按照基本回收策略分

* 引用计数（Reference Counting）:

比较古老的回收算法。原理是此对象有一个引用，即增加一个计数，删除一个引用则减少一个计数。垃圾回收时，只用收集计数为0的对象。**此算法最致命的是无法处理循环引用的问题**。

* 标记-清除（Mark-Sweep）:
  此算法执行分两阶段。第一阶段从引用根节点开始标记所有被引用的对象，第二阶段遍历整个堆，把未标记的对象清除。**此算法需要暂停整个应用，同时，会产生内存碎片**。

* 复制（Copying）:

此算法把内存空间划为两个相等的区域，每次只使用其中一个区域。垃圾回收时，**遍历当前使用区域，把正在使用中的对象复制到另外一个区域中。**次算法每次只处理正在使用中的对象，因此复制成本比较小，同时复制过去以后还能进行相应的内存整理，不会出现“碎片”问题。当然，此**算法的缺点也是很明显的，就是需要两倍内存空间**。

**商业虚拟机用这个回收算法来回收新生代**

* 标记-整理（Mark-Compact）:

此算法结合了“标记-清除”和“复制”两个算法的优点。也是分两阶段，第一阶段从根节点开始标记所有被引用对象，第二阶段遍历整个堆，把清除未标记对象并且把存活对象“压缩”到堆的其中一块，按顺序排放。此算法避免了“标记-清除”的碎片问题，同时也避免了“复制”算法的空间问题。

**老年代标记-清理“和”标记-整理“算法来进行回收。**

##Java中可以作为GC Root的对象有

* 虚拟机栈中引用的对象（本地变量表）
* 方法区中静态属性的引用对象
* 方法区中常量引用对象
* 本地方法栈中引用对象（Native 对象）
* 主要执行在上下文中和全局性的引用

#分区对待的方式分

* 增量收集（Incremental Collecting）:
  实时垃圾回收算法，即：在应用进行的同时进行垃圾回收。不知道什么原因JDK5.0中的收集器没有使用这种算法的。

* 分代收集（Generational Collecting）:
  基于对对象生命周期分析后得出的垃圾回收算法。把对象分为年青代、年老代、持久代，对不同生命周期的对象使用不同的算法（上述方式中的一个）进行回收。现在的垃圾回收器（从J2SE1.2开始）都是使用此算法的。

#为什么要分代

分代的垃圾回收策略，是基于这样一个事实：不同的对象的生命周期是不一样的。因此，不同生命周期的对象可以采取不同的收集方式，以便提高回收效率。

#什么情况下触发垃圾回收

由于对象进行了分代处理，因此垃圾回收区域、时间也不一样。GC有两种类型：Scavenge GC和Full GC。jdk1.8 后分为 Minor GC、Major GC和Full GC，清理Eden区和 Survivor区叫Minor GC。清理Old区叫Major GC。清理整个堆空间—包括年轻代和老年代叫Full GC。

##Scavenge GC

一般情况下，当新对象生成，并且在Eden申请空间失败时，就会触发Scavenge GC，对Eden区域进行GC，清除非存活对象，并且把尚且存活的对象移动到Survivor区。然后整理Survivor的两个区。这种方式的GC是对年轻代的Eden区进行，不会影响到年老代。因为大部分对象都是从Eden区开始的，同时Eden区不会分配的很大，所以Eden区的GC会频繁进行。因而，一般在这里需要使用速度快、效率高的算法，使Eden去能尽快空闲出来。

##Full GC

对整个堆进行整理，包括Young、Tenured和Perm。Full GC因为需要对整个对进行回收，所以比Scavenge GC要慢，因此应该尽可能减少Full GC的次数。在对JVM调优的过程中，很大一部分工作就是对于FullGC的调节。有如下原因可能导致Full GC：

* 年老代（Tenured）被写满
* 持久代（Perm）被写满 
* System.gc()被显示调用 
* 上一次GC之后Heap的各域分配策略动态变化

##GC

新生代由于其对象存活时间短，且需要经常gc，因此采用效率较高的复制算法，其将内存区分为一个eden区和两个suvivor区，eden区和survivor区的比例是8:1，分配内存时先分配eden区，当eden区满时，使用**复制算法**进行gc，将存活对象复制到一个survivor区，当一个survivor区满时，将其存活对象复制到另一个区中，当对象存活时间大于某一阈值时，将其放入老年代。 
老年代和永久代因为其存活对象时间长，因此使用**标记清除或标记整理算法**


#垃圾回收的瓶颈

传统分代垃圾回收方式，已经在一定程度上把垃圾回收给应用带来的负担降到了最小，把应用的吞吐量推到了一个极限。但是他无法解决的一个问题，就是Full GC所带来的应用暂停。在一些对实时性要求很高的应用场景下，GC暂停所带来的请求堆积和请求失败是无法接受的。这类应用可能要求请求的返回时间在几百甚至几十毫秒以内，如果分代垃圾回收方式要达到这个指标，只能把最大堆的设置限制在一个相对较小范围内，但是这样有限制了应用本身的处理能力，同样也是不可接收的。

分代垃圾回收方式确实也考虑了实时性要求而提供了并发回收器，支持最大暂停时间的设置，但是受限于分代垃圾回收的内存划分模型，其效果也不是很理想。

为了达到实时性的要求（其实Java语言最初的设计也是在嵌入式系统上的），一种新垃圾回收方式呼之欲出，它既支持短的暂停时间，又支持大的内存空间分配。可以很好的解决传统分代方式带来的问题。

##

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
取余：向0 靠近，fix（2.3）=3
取模: 向负无穷靠近 floor（2.3）= 3
**如果是负数**则 取余是向0 靠近，取模是向负无穷靠近 例如
7/（-3）=-2.3，在这个运算中，x为7，y为-3，
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


##GC

新生代由于其对象存活时间短，且需要经常gc，因此采用效率较高的复制算法，其将内存区分为一个eden区和两个suvivor区，eden区和survivor区的比例是8:1，分配内存时先分配eden区，当eden区满时，使用**复制算法**进行gc，将存活对象复制到一个survivor区，当一个survivor区满时，将其存活对象复制到另一个区中，当对象存活时间大于某一阈值时，将其放入老年代。 
老年代和永久代因为其存活对象时间长，因此使用**标记清除或标记整理算法**

#java多线程

##产生线程不安全的原因

在同一程序中运行多个线程本身不会导致问题，问题在于多个线程访问了相同的资源。如，同一内存区（变量，数组，或对象）、系统（数据库，web services等）或文件。实际上，这些问题只有在一或多个线程向这些资源做了写操作时才有可能发生，只要资源没有发生变化,多个线程读取相同的资源就是安全的。

##竞态条件 & 临界区

当两个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件。导致竞态条件发生的代码区称作**临界区**。上例中add()方法就是一个临界区,它会产生**竞态条件**。在临界区中使用适当的同步就可以避免竞态条件。

```
public class Counter {
    protected long count = 0;

    public void add(long value){
        this.count = this.count + value;  
    }
}

```

两个线程交替执行结果如下

```
this.count = 0;
   A:   读取 this.count 到一个寄存器 (0)
   B:   读取 this.count 到一个寄存器 (0)
   B:   将寄存器的值加2
   B:   回写寄存器值(2)到内存. this.count 现在等于 2
   A:   将寄存器的值加3
   A:   回写寄存器值(3)到内存. this.count 现在等于 3
```

##共享资源

允许被多个线程同时执行的代码称作线程安全的代码叫做共享资源。**线程安全的代码不包含竞态条件**。当多个线程同时更新共享资源时会引发竞态条件。

##局部变量

局部变量存储在线程自己的栈中。也就是说，局部变量永远也不会被多个线程共享。所以，基础类型的局部变量是线程安全的。

```
public void someMethod(){
  long threadSafeInt = 0;//局部变量
  threadSafeInt++;
}
```

##局部的对象引用

上面提到的局部变量是一个基本类型，如果局部变量是一个**对象类型**呢？对象的局部引用和基础类型的局部变量不太一样。尽管引用本身没有被共享，但引用所指的对象并没有存储在线程的栈内，**所有的对象都存在共享堆**中，所以对于局部对象的引用，有可能是线程安全的，也有可能是线程不安全的。

如果在某个方法中创建的对象不会被其他方法或全局变量获得，或者说方法中创建的对象没有逃出此方法的范围，那么它就是**线程安全**的。实际上，哪怕将这个对象作为参数传给其它方法，只要**别的线程获取不到这个对象**，那它仍是线程安全的。

```
public void someMethod(){
  LocalObject localObject = new LocalObject();
  localObject.callMethod();
  method2(localObject);
}

public void method2(LocalObject localObject){
  localObject.setValue("value");
}
```

上面样例中 LocalObject 对象没有被方法返回，也没有被传递给someMethod()方法外的对象，始终在someMethod()方法内部。**每个执行someMethod()的线程都会创建自己的LocalObject对象**，并赋值给localObject引用。因此，这里的LocalObject是线程安全的。事实上，整个someMethod()都是线程安全的。即使将LocalObject作为参数传给同一个类的其它方法或其它类的方法时，它仍然是**线程安全**的。当然，如果LocalObject通过某些方法被传给了别的线程，那它就不再是线程安全的了。
	

```
如果一个资源的创建，使用，销毁都在同一个线程内完成，且永
远不会脱离该线程的控制，则该资源的使用就是线程安全的。
```

##Java中实现线程安全的方法

* 最简单的方式，使用Synchronization关键字:
  http://blog.csdn.net/suifeng3051/article/details/48711405
* 使用java.util.concurrent.atomic 包中的原子类，例如 AtomicInteger
* 使用java.util.concurrent.locks 包中的锁
* 使用线程安全的集合ConcurrentHashMap
* 使用volatile关键字，保证变量可见性（直接从内存读，而不是从线程cache读）

http://blog.csdn.net/suifeng3051/article/details/52164267

##synchronized 

* synchronized修饰非静态方法、同步代码块的synchronized (this)用法和synchronized (非this对象)的用法锁的是对象，线程想要执行对应同步代码，需要获得对象锁。     
* synchronized修饰非静态方法以及同步代码块的synchronized (类.class)用法锁的是类，线程想要执行对应同步代码，需要获得类锁。
* 在java中，同步加锁的是一个对象或者一个类，而不是代码

##synchronized 使用

synchronized关键字可标记四种代码块：

1. 实例方法
2. 静态方法
3. 实例方法中的代码块
4. 静态方法中的代码块

##synchronized的缺陷

　　synchronized是java中的一个关键字，也就是说是Java语言内置的特性。那么为什么会出现Lock呢？

　如果一个代码块被synchronized修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况：

　　1）获取锁的线程执行完了该代码块，然后线程释放对锁的占有；

　　2）线程执行发生异常，此时JVM会让线程自动释放锁。

　　那么如果这个获取锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，其他线程便只能干巴巴地等待，影响程序执行效率。

##volatile 与 synchronized 的比较

* volatile是变量修饰符，其修饰的变量具有可见性。
* volatile主要用在多个线程感知实例变量被更改了场合，从而使得各个线程获得最新的值。它强制线程每次从主内存中取到变量，而不是从线程的私有内存中读取变量，从而保证了数据的可见性。
* ①volatile轻量级，只能修饰变量。synchronized重量级，还可修饰方法
* ②volatile只能保证数据的可见性，不能用来同步，因为多个线程并发访问volatile修饰的变量不会阻塞。
* synchronized不仅保证可见性，而且还保证原子性，因为，只有获得了锁的线程才能进入临界区，从而保证临界区中的所有语句都全部执行。多个线程争抢synchronized锁对象时，会出现阻塞。

##Lock 接口

lock：需要显示指定起始位置和终止位置。一般使用ReentrantLock类做为锁，多个线程中必须要使用一个ReentrantLock类做为对象才能保证锁的生效。且在加锁和解锁处需要通过lock()和unlock()显示指出。所以一般会在finally块中写unlock()以防死锁。

synchronized是托管给JVM执行的，而lock是java写的控制锁的代码。

Lock接口中每个方法的使用

* lock()用来获取锁。如果锁已被其他线程获取，则进行等待。
* tryLock()如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待，则返回true。
* tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
* lockInterruptibly()是用来获取锁的。如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。
* unLock()方法是用来释放锁的。

###synchronized和lock用途区别

synchronized原语和ReentrantLock在一般情况下没有什么区别，但是在非常复杂的同步应用中，请考虑使用ReentrantLock，特别是遇到下面2种需求的时候。

1.某个线程在等待一个锁的控制权的这段时间需要中断
2.需要分开处理一些wait-notify，ReentrantLock里面的Condition应用，能够控制notify哪个线程
3.具有公平锁功能，每个到来的线程都将排队等候

lock 接口的最大优势是它为读和写提供两个单独的锁，可以让你构建高性能数据结构，比如 ConcurrentHashMap 和条件阻塞。

##Java的锁分为对象锁和类锁。

  　　1. 当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内针对该对象的操作只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。

  　　2. 然而，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。

  　　3. 尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对该object中所有其它synchronized(this)同步代码块的访问将被阻塞。

  　　4. 同步加锁的是对象，而不是代码。因此，如果你的类中有一个同步方法，这个方法可以被两个不同的线程同时执行，只要每个线程自己创建一个的该类的实例即可。

  　　5. 不同的对象实例的synchronized方法是不相干扰的。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法。

  　　6. synchronized关键字是不能继承的，也就是说，基类的方法synchronized f(){} 在继承类中并不自动是synchronized f(){}，而是变成了f(){}。继承类需要你显式的指定它的某个方法为synchronized方法。

　　7.对一个全局对象或者类加锁时，对该类的所有对象都起作用
　　

##java 线程死锁

死锁的四个条件：

* 1、互斥使用，即当资源被一个线程使用(占有)时，别的线程不能使用
* 2、不可抢占，资源请求者不能强制从资源占有者手中夺取资源，资源只能由资源占有者主动释放。
* 3、请求和保持，即当资源请求者在请求其他的资源的同时保持对原有资源的占有。
* 4、循环等待，即存在一个等待队列：P1占有P2的资源，P2占有P3的资源，P3占有P1的资源。这样就形成了一个等待环路。

#java 线程生命周期

*  新建（new Thread）
   当创建Thread类的一个实例（对象）时，此线程进入新建状态（未被启动）。
   例如：Thread  t1=new Thread();

*  就绪（runnable）
   线程已经被启动，正在等待被分配给CPU时间片，也就是说此时线程正在就绪队列中排队等候得到CPU资源。例如：t1.start();

*  运行（running）
   线程获得CPU资源正在执行任务（run()方法），此时除非此线程自动放弃CPU资源或者有优先级更高的线程进入，线程将一直运行到结束。

*  死亡（dead）
   当线程执行完毕或被其它线程杀死，线程就进入死亡状态，这时线程不可能再进入就绪状态等待执行。
  *  自然终止：正常运行run()方法后终止
  *  异常终止：调用stop()方法让一个线程终止运行
*  堵塞（blocked）
   由于某种原因导致正在运行的线程让出CPU并暂停自己的执行，即进入堵塞状态。

   * 正在睡眠：用sleep(long t) 方法可使线程进入睡眠方式。一个睡眠着的线程在指定的时间过去可进入就绪状态。
   * 正在等待：调用wait()方法。（调用motify()方法回到就绪状态）
   * 被另一个线程所阻塞：调用suspend()方法。（调用resume()方法恢复）

## 2.常用方法

* void run()   创建该类的子类时必须实现的方法
* void start() 开启线程的方法
* static void sleep(long t) 释放CPU的执行权，不释放锁
* static void sleep(long millis,int nanos)
* final void wait()释放CPU的执行权，释放锁
* final void notify()
* static void yied()可以对当前线程进行临时暂停（让线程将资源释放出来）

##3 wait() 与notify()/notifyAll()/sleep

这三个方法都是Object的方法，并不是线程的方法！

* wait():释放占有的对象锁，线程进入等待池，释放cpu,而其他正在等待的线程即可抢占此锁，获得锁的线程即可运行程序。而sleep()不同的是，线程调用此方法后，会休眠一段时间，休眠期间，会暂时释放cpu，但并不释放对象锁。也就是说，在休眠期间，其他线程依然无法进入此代码内部。休眠结束，线程重新获得cpu,执行代码。wait()和sleep()最大的不同在于wait()会释放对象锁，而sleep()不会！ 
* notify(): 该方法会唤醒因为调用对象的wait()而等待的线程，其实就是对对象锁的唤醒，从而使得wait()的线程可以有机会获取对象锁。调用notify()后，并不会立即释放锁，而是继续执行当前代码，直到synchronized中的代码全部执行完毕，才会释放对象锁。JVM则会在等待的线程中调度一个线程去获得对象锁，执行代码。需要注意的是，wait()和notify()必须在synchronized代码块中调用。
* notifyAll()则是唤醒所有等待的线程。
* sleep方法在等待时不会释放任何锁或监视器

##java线程池

###在什么情况下使用线程池？ 

* 1.单个任务处理的时间比较短 
* 2.需处理的任务的数量大 

###使用线程池的好处:

* 1.减少在创建和销毁线程上所花的时间以及系统资源的开销 
* 2.如不使用线程池，有可能造成系统创建大量线程而导致消耗完系统内存

###线程池包括以下四个基本组成部分：

* 1、线程池管理器（ThreadPool）：用于创建并管理线程池，包括 创建线程池，销毁线程池，添加新任务；
* 2、工作线程（PoolWorker）：线程池中线程，在没有任务时处于等待状态，可以循环的执行任务；
* 3、任务接口（Task）：每个任务必须实现的接口，以供工作线程调度任务的执行，它主要规定了任务的入口，任务执行完后的收尾工作，任务的执行状态等；
* 4、任务队列（taskQueue）：用于存放没有处理的任务。提供一种缓冲机制。

###线程池的核心参数

ThreadPoolExecutor 有四个构造方法，前三个都是调用最后一个(最后一个参数最全)

```
 public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }

   
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             threadFactory, defaultHandler);
    }

  
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), handler);
    }

    // 都调用它
    public ThreadPoolExecutor(// 核心线程数
    int corePoolSize, 
                              // 最大线程数
                              int maximumPoolSize,  
                              // 闲置线程存活时间
                              long keepAliveTime,  
                              // 时间单位
                              TimeUnit unit, 
                              // 线程队列
                              BlockingQueue<Runnable> workQueue,  
                              // 线程工厂  
                              ThreadFactory threadFactory,                
                              // 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略              
                              RejectedExecutionHandler handler   ) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

```

* corePoolSize：核心线程数
  *  核心线程会一直存活，即使没有任务需要执行
  *  当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理
  *  设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
* maxPoolSize：最大线程数
  * 当线程数>=corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
  * 当线程数=maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
* keepAliveTime：线程空闲时间
  * 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
  * 如果allowCoreThreadTimeout=true，则会直到线程数量=0
* workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
  * ArrayBlockingQueue;
  * LinkedBlockingQueue;
  * SynchronousQueue;
  * ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。
* threadFactory：线程工厂，主要用来创建线程；
* rejectedExecutionHandler：任务拒绝处理器，两种情况会拒绝处理任务：
  * 当线程数已经达到maxPoolSize，切队列已满，会拒绝新任务
  * 当线程池被调用shutdown()后，会等待线程池里的任务执行完毕，再shutdown。如果在调用shutdown()和线程池真正shutdown之间提交任务，会拒绝新任务
* 当拒绝处理任务时线程池会调用rejectedExecutionHandler来处理这个任务。如果没有设置默认是AbortPolicy，会抛出异常。ThreadPoolExecutor类有几个内部实现类来处理这类情况：
  * AbortPolicy 丢弃任务，抛运行时异常
  * CallerRunsPolicy 执行任务
  * DiscardPolicy 忽视，什么都不会发生
  * DiscardOldestPolicy 从队列中踢出最先进入队列（最后一个执行）的任务
  * 实现RejectedExecutionHandler接口，可自定义处理器

###Java线程池 ExecutorService 的创建

* Executors.newCachedThreadPool 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
* Executors.newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
* Executors.newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
* Executors.newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

备注：Executors只是一个工厂类，它所有的方法返回的都是ThreadPoolExecutor、ScheduledThreadPoolExecutor这两个类的实例。

###  springBoot 的使用配置

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据收集配置，主要作用在于Spring启动时自动加载一个ExecutorService对象.
 * @author Bruce
 * @date 2017/2/22
 * 
 * update by Cliff at 2027/11/03
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService getThreadPool(){
        return Executors.newCachedThreadPool();
    }


}

```

###ExecutorService有如下几个执行方法

- executorService.execute(Runnable);这个方法接收一个Runnable实例，并且异步的执行
- executorService.submit(Runnable)
- executorService.submit(Callable)
- executorService.invokeAny(...)
- executorService.invokeAll(...)

###execute(Runnable)

这个方法接收一个Runnable实例，并且异步的执行

```
executorService.execute(new Runnable() {
public void run() {
    System.out.println("Asynchronous task");
}
});

executorService.shutdown();
```

###submit(Runnable)

submit(Runnable)和execute(Runnable)区别是前者可以返回一个Future对象，通过返回的Future对象，我们可以检查提交的任务是否执行完毕，请看下面执行的例子：

```
Future future = executorService.submit(new Runnable() {
public void run() {
    System.out.println("Asynchronous task");
}
});

future.get();  //returns null if the task has finished correctly.

```

###submit(Callable)

submit(Callable)和submit(Runnable)类似，也会返回一个Future对象，但是除此之外，submit(Callable)接收的是一个Callable的实现，Callable接口中的call()方法有一个返回值，可以返回任务的执行结果，而Runnable接口中的run()方法是void的，没有返回值。请看下面实例：

```
Future future = executorService.submit(new Callable(){
public Object call() throws Exception {
    System.out.println("Asynchronous Callable");
    return "Callable Result";
}
});

System.out.println("future.get() = " + future.get());
```

如果任务执行完成，future.get()方法会返回Callable任务的执行结果。注意，future.get()方法会产生阻塞。

###invokeAny(…)

invokeAny(...)方法接收的是一个Callable的集合，执行这个方法不会返回Future，但是会返回所有Callable任务中其中一个任务的执行结果。这个方法也无法保证返回的是哪个任务的执行结果，反正是其中的某一个。

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

Set<Callable<String>> callables = new HashSet<Callable<String>>();

callables.add(new Callable<String>() {
public String call() throws Exception {
    return "Task 1";
}
});
callables.add(new Callable<String>() {
public String call() throws Exception {
    return "Task 2";
}
});
callables.add(new Callable<String>() {
    public String call() throws Exception {
    return "Task 3";
}
});

String result = executorService.invokeAny(callables);
System.out.println("result = " + result);
executorService.shutdown();
```

###invokeAll(…)

invokeAll(...)与 invokeAny(...)类似也是接收一个Callable集合，但是前者执行之后会返回一个Future的List，其中对应着每个Callable任务执行后的Future对象。

```
List<Future<String>> futures = executorService.invokeAll(callables);

for(Future<String> future : futures){
System.out.println("future.get = " + future.get());
}

executorService.shutdown();
```

https://www.cnblogs.com/dolphin0520/p/3932921.html
https://www.cnblogs.com/waytobestcoder/p/5323130.html

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



