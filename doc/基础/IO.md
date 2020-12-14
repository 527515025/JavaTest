# 什么是DMA

**DMA(Direct Memory Access)是系统中的一个特殊设备**，它可以协调完成**内存到设备间的数据传输**，中间过程**不需要CPU介入**。

以文件写入为例：

- 进程p1发出数据写入磁盘文件的请求
- CPU处理写入请求，通过编程告诉DMA引擎数据在内存的位置，要写入数据的大小以及目标设备等信息
- CPU处理其他进程p2的请求，DMA负责将内存数据写入到设备中
- DMA完成数据传输，中断CPU
- CPU从p2上下文切换到p1,继续执行p1

![image-20200924001638896](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200924001638896.png)

什么是IO

Java NIO(new/inputstream outputstream)使用通道、缓冲来操作流，所以要深刻理解这些概念，尤其是，缓冲中的数据结构（当前位置(position)、限制(limit)、容量(capacity)）,这些知识点要通过写程序慢慢体会。


## NIO vs  传统IO

NIO是面向缓冲、通道的；传统IO面向流

通道是双向的既可以写、也可以读；传统IO只能是单向的

NIO可以设置为异步；传统IO只能是阻塞，同步的

* C10k 问题：服务器连接越来越多 ， 资源，性能

  

### **IO 的方式通常分为几种：**

- 同步阻塞的 BIO
- 同步非阻塞的 NIO
- 异步非阻塞的 AIO 在使用同步 I/O 的网络应用中，如果要同时处理多个客户端请求，或是在客户端要同时和多个服务器进行通讯，就必须使用多线程来处理。

**NIO 基于 Reactor**，当 socket 有流可读或可写入 socket 时，操作系统会相应的通知应用程序进行处理，应用再将流读取到缓冲区或写入操作系统。也就是说，这个时候，已经不是一个连接就要对应一个处理线程了，而是有效的请求，对应一个线程，当连接没有数据时，是没有工作线程来处理的。

AIO与 NIO 不同，当进行读写操作时，只须直接调用 API 的 read 或 write 方法即可。这两种方法均为异步的，对于读操作而言，当有流可读取时，操作系统会将可读的流传入 read 方法的缓冲区，并通知应用程序；对于写操作而言，当操作系统将 write 方法传递的流写入完毕时，操作系统主动通知应用程序。即可以理解为，**read/write 方法都是异步的，完成后会主动调用回调函数**。





strace -ff  -o -out

BIO 每次请求占用一个线程。 

## 缓冲区结构图

NIO是面向缓冲区的，缓冲区可以理解为一块内存，有大小。缓冲区有位置、界限、容量几个概念。

capacity：容量，缓冲区的大小

limit：限制，表示最大的可读写的数量

position：当前位置，每当读写，当前位置都会加一

flip和clear方法，内部就操作这三个变量。

 

## 缓冲区常用方法

clear：将当前位置设置为0，限制设置为容量，目的是尽最大可能让字节，由通道读取到缓冲中

flip：当前位置置为限制，然后将当前位置置为0，目的是将有数据部分的字节，由缓冲写入到通道中。通常用在读与写之间。

# NIO

https://mp.weixin.qq.com/s/GfV9w2B0mbT7PmeBS45xLw?spm=a2c6h.12873639.0.0.6dd14a61A3b526

# Java 中的网络 IO 模型

Java 中的网络 IO 模型有三种：**BIO、NIO、AIO。**

1）BIO：**同步的、阻塞式 IO**。在这种模型中，服务器上一个线程处理一次连接，即客户端每发起一个请求，服务端都要开启一个线程专门处理该请求。这种模型对线程量的耗费极大，且线程利用率低，难以承受请求的高并发。BIO 虽然可以使用线程池+等待队列进行优化，避免使用过多的线程，但是依然无法解决线程利用率低的问题。

使用 **BIO 构建 C/S 系统的 Java 编程组件是 ServerSocket 和 Socket**。

![image-20201214001449516](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214001449516.png)

2）NIO：**同步的、非阻塞式 IO**。在这种模型中，服务器上**一个线程处理多个连接**，即多个客户端请求都会被注册到**多路复用器（ Selector**）上，**多路复用器会轮训这些连接，轮训到连接上有 IO 活动就进行处理**。NIO 降低了线程的需求量，提高了线程的利用率。Netty 就是基于 NIO 的（这里有一个问题：前文大力宣扬 Netty 是一个异步高性能网络应用框架，为何这里又说 Netty 是基于同步的 NIO 的？寻答案）。

使用 NIO 构建 **C/S 系统的 Java 编程组件是 Channel、Buffer、Selector**。

![image-20201214001603302](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214001603302.png)

**NIO 是面向缓冲区编程**的，从缓冲区读取数据的时候游标在缓冲区中是可以前后移动的，这就增加了数据处理的灵活性。这和面向流的 BIO 只能顺序读取流中数据有很大的不同。

Java NIO 的非阻塞模式，使得一个线程从某个通道读取数据的时候，**若当前有可用数据，则该线程进行处理，若当前无可用数据，则该线程不会保持阻塞等待状态，而是可以去处理其他工作**（比如处理其他通道的读写）；同样，一个线程向某个通道写入数据的时候，一旦开始写入，该线程无需等待写完即可去处理其他工作（比如处理其他通道的读写）。这种特性使得一个线程能够处理多个客户端请求，而不是像 BIO 那样，一个线程只能处理一个请求。

3）**AIO**：**异步非阻塞式 IO**。在这种模型中，由**操作系统完成与客户端之间的 read/write，之后再由操作系统主动通知服务器线程去处理后面的工作，在这个过程中服务器线程不必同步等待 read/write 完成**。由于不同的操作系统对 AIO 的支持程度不同，AIO 目前未得到广泛应用。

## NIO/AIO对比

使用 Java NIO 构建的 IO 程序，它的工作模式是：**主动轮训 IO 事件**，IO 事件发生后程序的线程主动处理 IO 工作，这种模式也叫做 Reactor 模式。

使用 Java AIO 构建的 IO 程序，它的工作模式是：将 IO 事件的处理托管给操作系统，操作系统完成 IO 工作之后会通知程序的线程去处理后面的工作，这种模式也叫做 Proactor 模式。

讨论一下网路 IO 中阻塞、非阻塞、异步、同步这几个术语的含义和关系：

- **阻塞**：如果线程调用 read/write 过程，但 read/write 过程没有就绪或没有完成，则调用 read/write 过程的线程会一直等待，这个过程叫做阻塞式读写。
- **非阻塞**：如果线程调用 read/write 过程，但 read/write 过程没有就绪或没有完成，调用 read/write 过程的线程并不会一直等待，而是去处理其他工作，**等到 read/write 过程就绪或完成后再回来处理**，这个过程叫做非阻塞式读写。
- AIO异步：**read/write 过程托管给操作系**统来完成，完成后操作系统会通知（通过回调或者事件）应用网络 IO 程序（其中的线程）来进行后续的处理。
- 同步：read/write 过程由网络 IO 程序（其中的线程）来完成。

基于以上含义，可以看出：异步 IO 一定是非阻塞 IO；**同步 IO 既可以是阻塞 IO、也可以是非阻塞 IO**。

##**Java NIO API 简单回顾**

BIO 以流 stream 的方式处理数据，而 **NIO 以 buffer 缓冲区**（也被叫做块）的方式处理数据，**块 IO 效率比流 IO 效率高很多**。BIO 基于字符流或者字节流进行操作，而 **NIO 基于 Channel 和 Buffer 进行操作**，数据总是**从通道读取到缓冲区或者从缓冲区写入到通道。Selector 用于监听多个通道上的事件**（比如收到连接请求、数据达到等等），**因此使用单个线程就可以监听多个客户端通道**。如下图所示：

![image-20201214003527854](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214003527854.png)

- 一个 **Selector 对应一个处理线程**
- 一个 **Selector 上可以注册多个 Channel**
- 每个 Channel 都会对应一个 Buffer（有时候一个 Channel 可以使用多个 Buffer，这时候程序要进行多个 Buffer 的分散和聚集操作），Buffer 的本质是一个内存块，**底层是一个数组**
- Selector 会**根据不同的事件在各个 Channel 上切换**
- **Buffer 是双向的，既可以读也可以写**，切换读写方向要**调用 Buffer 的 flip()**方法
- 同样，**Channel 也是双向的，数据既可以流入也可以流出**

### 缓冲区（Buffer）

缓冲区（Buffer）本质上是一个**可读可写的内存块**，可以理解成一个容器对象，**Channel 读写文件或者网络都要经由 Buffer**。在 Java NIO 中，Buffer 是一个顶层抽象类，它的常用子类有（前缀表示该 Buffer 可以存储哪种类型的数据）：

- ByteBuffer
- CharBuffer
- ShortBuffer
- IntBuffer
- LongBuffer
- DoubleBuffer
- FloatBuffer

涵盖了 Java 中除 boolean 之外的**所有的基本数据类型**。其中 ByteBuffer 支持类型化的数据存取，即可以往 ByteBuffer 中放 byte 类型数据、也可以放 char、int、long、double 等类型的数据，但读取的时候要做好类型匹配处理，否则会抛出 BufferUnderflowException。

另外，Buffer 体系中还有一个重要的 MappedByteBuffer（ByteBuffer 的子类），可以**让文件内容直接在堆外内存中被修改**，而如何同步到文件由 NIO 来完成。

### 通道（Channel）

通道（Channel）是双向的，可读可写。在 Java NIO 中，**Buffer 是一个顶层接口**，它的常用子类有：

- FileChannel：用于文件读写
- DatagramChannel：用于 UDP 数据包收发
- ServerSocketChannel：用于服务端 TCP 数据包收发
- SocketChannel：用于客户端 TCP 数据包收发

###  选择器（Selector）

**选择器（Selector）是实现 IO 多路复用的关键**，多个 Channel 注册到某个 Selector 上，当 Channel 上有事件发生时，Selector 就会取得事件然后调用线程去处理事件。也就是说只有当连接上真正有读写等事件发生时，线程才会去进行读写等操作，这就不必为每个连接都创建一个线程，一个线程可以应对多个连接。这就是 IO 多路复用的要义。

Netty 的 IO 线程 NioEventLoop 聚合了 Selector，可以同时并发处理成百上千的客户端连接，后文会展开描述。

在 Java NIO 中，**Selector 是一个抽象类**，它的常用方法有：

```java
public abstract class Selector implements Closeable {
    ......
    
    /**
     * 得到一个选择器对象
     */
    public static Selector open() throws IOException {
        return SelectorProvider.provider().openSelector();
    }
    ......

    /**
     * 返回所有发生事件的 Channel 对应的 SelectionKey 的集合，通过
     * SelectionKey 可以找到对应的 Channel
     */
    public abstract Set<SelectionKey> selectedKeys();
    ......
    
    /**
     * 返回所有 Channel 对应的 SelectionKey 的集合，通过 SelectionKey
     * 可以找到对应的 Channel
     */
    public abstract Set<SelectionKey> keys();
    ......
    
    /**
     * 监控所有注册的 Channel，当其中的 Channel 有 IO 操作可以进行时，
     * 将这些 Channel 对应的 SelectionKey 找到。参数用于设置超时时间
     */
    public abstract int select(long timeout) throws IOException;
    
    /**
    * 无超时时间的 select 过程，一直等待，直到发现有 Channel 可以进行
    * IO 操作
    */
    public abstract int select() throws IOException;
    
    /**
    * 立即返回的 select 过程
    */
    public abstract int selectNow() throws IOException;
    ......
    
    /**
    * 唤醒 Selector，对无超时时间的 select 过程起作用，终止其等待
    */
    public abstract Selector wakeup();
    ......
}
```

服务端的工作流程为：

![image-20201214004044770](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214004044770.png)

服务端的工作流程为：

1）当客户端发起连接时，会通过 ServerSocketChannel 创建对应的 SocketChannel。

2）调用 **SocketChannel 的注册方法将 SocketChannel 注册到 Selector 上**，注册方法返回一个 SelectionKey，该 SelectionKey 会被放入 Selector 内部的 SelectionKey 集合中。该 SelectionKey 和 Selector 关联（即通过 SelectionKey 可以找到对应的 Selector），也和 SocketChannel 关联（即通过 SelectionKey 可以找到对应的 SocketChannel）。

4）Selector 会调用 select()/select(timeout)/selectNow()方法对内部的 SelectionKey 集合关联的 SocketChannel 集合进行监听，找到有事件发生的 SocketChannel 对应的 SelectionKey。

5）通过 SelectionKey 找到有事件发生的 SocketChannel，完成数据处理。

## **零拷贝技术** 

kafka中**partition leader到follower的消息同步**和**consumer拉取partition中的消息都使用到zero cory**。Cousumer从broker获取数据时直接使用了FileChannel.transferTo()，直接在内核态进行的channel到channel的数据传输。

**直接 IO 技术**

![image-20201214004345274](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214004345274.png)

内核缓冲区是 Linux 系统的 Page Cahe。为了加快磁盘的 IO，Linux 系统会把磁盘上的数据**以 Page 为单位缓存在操作系统的内存**里，这里的 Page 是 Linux 系统定义的一个逻辑概念，一个 Page 一般为 4K。

可以看出，整个过程有四次数据拷贝，读进来两次，写回去又两次：磁盘-->内核缓冲区-->应用->Socket 缓冲区-->网络。

**内存映射文件技术**

![image-20201214004449693](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214004449693.png)

整个过程有三次数据拷贝，不再经过应用程序内存，直接在内核空间中从内核缓冲区拷贝到 Socket 缓冲区。

 RocketMQ 的文件都利用了**内存映射即 Mmap**，将**程序虚拟页面直接映射到页缓存上**，无需内核态再往用户态的拷贝。所以 RocketMQ 的文件存储都**使用定长结构**来存储，方便一次将整个文件映射至内存中。

**零拷贝技术**

零拷贝中所谓的“零”指的是内存中数据拷贝的次数为 0。

![image-20201214004654827](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214004654827.png)

内核缓冲区到 Socket 缓冲区之间并没有做数据的拷贝，只是一个地址的映射。底层的网卡驱动程序要读取数据并发送到网络上的时候，看似读取的是 Socket 的缓冲区中的数据，其实直接读的是内核缓冲区中的数据。





# Netty

Netty是一款**异步的事件驱动的网络应用框架和工具**，用于快速开发可维护的高性能、高扩展性协议服务器和 客户端。也就是说，Netty是一个**NIO客户端/服务器框架**，支持快速、简单地开发网络应用，如协议服务器和 客户端。它极大简化了网络编程，如TCP和UDP套接字服务。既然是网络编程，Socket就不谈了，为什么不用NIO呢？

Netty 主要用于开发基于 TCP 协议的网络 IO 程序（TCP/IP 是网络通信的基石，当然也是 Netty 的基石，Netty 并没有去改变这些底层的网络基础设施，而是在这之上提供更高层的网络基础设施），例如高性能服务器段/客户端、P2P 程序等。

![image-20201214001400256](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201214001400256.png)

## NIO的主要问题

- NIO的类库和API繁杂，学习成本高，你需要熟练掌握Selector、ServerSocketChannel、SocketChannel、ByteBuffer等。
- 需要熟悉Java多线程编程。这是因为NIO编程涉及到Reactor模式，你必须对多线程和网络编程非常熟悉，才能写出高质量的NIO程序。
- 臭名昭著的epoll bug。它会导致Selector空轮询，最终导致CPU 100%。直到JDK1.7版本依然没得到根本性的解决。

## Netty的优点有很多：

- API使用简单，学习成本低。
- 功能强大，内置了多种解码编码器，支持多种协议。
- 性能高，对比其他主流的NIO框架，Netty的性能最优。
- 社区活跃，发现BUG会及时修复，迭代版本周期短，不断加入新的功能。
- Dubbo、Elasticsearch都采用了Netty，质量得到验证。

## NETTY**的应用场景**

在互联网领域，Netty 作为异步高并发的网络组件，常常用于构建高性能 RPC 框架，以提升分布式服务群之间调用或者数据传输的并发度和速度。例如 Dubbo 的网络层就可以（但并非一定）使用 Netty。 阿里分布式服务框架 Dubbo 的 dubbo协议框架；淘宝的消息中间件 RocketMQ；

一些大数据基础设施，比如 Hadoop，在处理海量数据的时候，数据在多个计算节点之中传输，为了提高传输性能，也采用 Netty 构建性能更高的网络 IO 层。Hadoop 的高性能通信和序列化组件 Avro 的 RPC 框架；

在游戏行业，Netty 被用于构建高性能的游戏交互服务器，Netty 提供了 TCP/UDP、HTTP 协议栈，方便开发者基于 Netty 进行私有协议的开发。

Netty 作为成熟的高性能异步通信框架，无论是应用在互联网分布式应用开发中，还是在大数据基础设施构建中，亦或是用于实现应用层基于公私协议的服务器等等，都有出色的表现，是一个极好的轮子。

