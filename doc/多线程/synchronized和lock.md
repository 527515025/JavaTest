

# 死磕 synchronized 和 AQS 系

先将ReentrantLock跟常用的Synchronized进行比较，其特性如下

|          |ReentrantLock                  |Synchronized      |
| -------- | ------------------------------ | ----------------- |
| 锁实现   | 依赖AQS                        | 监视器Monitor模式 |
| 灵活性   | 支持响应中断、超时、尝试获取锁 | 不灵活            |
| 释放形式 | 必须显示的调用unlock()释放锁   | 自动释放监视器    |
| 锁类型   | 公平锁&非公平锁                | 非公平锁          |
| 条件队列 | 可关联多个队列                 | 关联一个条件队列  |
| 可重入性 | 可重入                         | 可重入            |

# 1. synchronized

synchronized关键字是Java中解决并发问题的一种常用方法，也是最简单的一种方法，其作用有三个:（1）互斥性：确保线程互斥的访问同步代码（2）可见性：保证共享变量的修改能够及时可见（3）有序性：有效解决重排序问题,其用法也有三个:

1. 修饰实例方法 锁对象
2. 修饰静态方法 以及 静态方法中的代码块  锁类
3. 修饰实例方法代码块 锁对象

- synchronized修饰非静态方法以及同步代码块的synchronized (this)用法和synchronized (非this对象)的用法锁的都是**对象**，线程想要执行对应同步代码，需要获得**对象锁**。   
- synchronized修饰静态方法以及同步代码块的**synchronized (类.class)**用法锁的**是类**，线程想要执行对应同步代码，需要获得**类锁。**
- 在java中，**同步加锁的是一个对象或者一个类，而不是代码**。
- **不同的对象实例的synchronized方法是不相干扰的**。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法。
- **对一个全局对象或者类加锁时，对该类的所有对象都起作用**。(因为加的是类锁)
- 一个对象中的同步方法一次只能被一个线程访问，如果有多个同步方法，一个线程一次也只能访问其中的一个同步方法，但是**非同步方法不受任何影响**
- 同步是通过加锁的形式来控制的，让一个线程访问一个同步方法时会获得这个对象的锁，只有退出同步方法时才会释放这个锁，**其它线程才可访问**
- **synchronized关键字是不能继承的**，也就是说，基类的方法```synchronized f(){} ```在继承类中并不自动是```synchronized f(){}```，而是变成了f(){}。继承类需要你显式的指定它的某个方法为synchronized方法。


##1.1  synchronized 的实现

对于synchronized关键字而言，javac在编译时，会生成对应的**monitorenter和monitorexit指令**分别对应synchronized同步块的**进入和退出**，有**两个monitorexit指令的原因是：为了保证抛异常的情况下也能释放锁**，所以javac为同步代码块添加了一个隐式的try-finally，在**finally中会调用monitorexit命令释放锁**。在JVM底层，对于这两种synchronized语义的实现大致相同。对于synchronized方法而言，javac为其生成了一个**ACCSYNCHRONIZED**关键字，在JVM进行方法调用时，发现调用的方法被**ACCSYNCHRONIZED**修饰，则会先尝试获得**锁**

传统的锁（也就是下文要说的**重量级锁**）依赖于**系统的同步函数**，在linux上使用**mutex互斥锁**，最底**层实现依赖于futex**，这**些同步函数都涉及到用户态和内核态的切换**、**进程的上下文切换，成本较高**。对于加了synchronized关键字但运行时并没有多线程竞争，或**两个线程接近于交替执行的情况，使用传统锁机制无疑效率是会比较低的**。

依赖于**操作系统Mutex Lock所实现的锁我们称之为“重量级锁”**，JDK 6中为了减少获得锁和释放锁带来的性能消耗，对synchronized 进行了优化，引入了“偏向锁”和“轻量级锁”。所以目前锁一共有4种状态，级别从低到高依次是：无锁、偏向锁、轻量级锁和重量级锁。锁状态只能升级不能降级。

这四种锁是指锁的状态，专门针对synchronized的。是对synchronized 的优化。Synchronize通过在对象头设置标记，达到了获取锁和释放锁的目的 。

![image-20201118221542956](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201118221542956.png)

要了解四种锁的实现过程要首先要熟悉**对象头和 Monitor** 的概念，关于Java 对象头的概念，此处可以参考 ：https://abelyang.blog.csdn.net/article/details/109436998 的 4.3.1

###  对象头

在32位系统下，对象头8字节，64位则是16个字节【未开启压缩指针，开启后12字节】。对象头中存储了对象的hashcode、分代年龄、锁状态、锁标志位对于普通对象而言，其对象头中有两类信息：**mark word和类型指针**。另外对于数组而言还会有一份记录数组长度的数据。

![image-20201118220025917](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201118220025917.png)

- 在操作同步资源之前需要给同步资源先加锁，这把锁就是存在Java对象头里的，以Hotspot虚拟机为例，Hotspot的对象头主要包括两部分数据：**Mark Word（标记字段）、Klass Pointer（类型指针）。**

  - **Mark Word（标记字段）：默认存储对象的HashCode**，GC分代年龄和**锁标志位信息**。这些信息都是与对象自身定义无关的数据，所以Mark Word被设计成一个非固定的数据结构以便在极小的空间内存存储尽量多的数据。它会根据对象的状态复用自己的存储空间，也就是说**在运行期间Mark Word里存储的数据会随着锁标志位的变化而变化。\**在32位系统上mark word长度为\**32字节，64位系统上长度为64字节**。
  - **Klass Pointer（类型指针）：对象指向它的类元数据的指针，**虚拟机通过这个指针来确定这个对象是哪个类的实例。

  ### Monitor

  Monitor可以理解为一个同步工具或一种同步机制，通常被描述为一个对象。**每一个Java对象就有一把看不见的锁，称为内部锁或者Monitor锁**。

  Monitor是线程私有的数据结构，每一个线程都有一个可用monitor record列表，同时还有一个全局的可用列表。每一个被锁住的对象都会和一个monitor关联，同时monitor中有一个Owner字段存放拥有该锁的线程的唯一标识，表示该锁被这个线程占用。

  现在话题回到synchronized，synchronized通过Monitor来实现线程同步，Monitor是依赖于底层的操作系统的Mutex Lock（互斥锁）来实现的线程同步。

## 1.2 偏向锁实现

偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁，降低获取锁的代价。

#### 偏向锁加锁过程

case 1：当该对象第一次被线程获得锁的时候，发现是**匿名偏向状态**（mark word中的thread id为0，表示未偏向任何线程，也叫做匿名偏向(anonymously biased)。），则会用CAS指令，将mark word中的thread id由0改成当前线程Id。如果成功，则代表获得了偏向锁，继续执行同步块中的代码。否则，将偏向锁撤销，升级为轻量级锁。

case 2：当被偏向的线程再次进入同步块时，发现锁对象偏向的就是当前线程，在通过一些额外的检查后，会往**当前线程的栈**中添加一条**Displaced Mark Word**为空的**Lock Record（在轻量级锁中会讲）**中，然后继续执行同步块的代码，因为操纵的是线程私有的栈，因此不需要用到CAS指令；由此可见**在偏向锁模式下，当被偏向的线程再次尝试获得锁时，仅仅进行几个简单的操作就可以了**，在这种情况下，synchronized关键字带来的性能开销基本可以忽略。

case 3.当其他线程进入同步块时，发现已经有偏向的线程了，则会进入到**撤销偏向锁的逻辑里**，**一般来说，会在safepoint（安全点，见文章：[一文明白JVM-万字长文，遇人随便问][https://abelyang.blog.csdn.net/article/details/109436998] 的 5.2.3章节）中去查看偏向的线程是否还存活，如果存活且还在同步块中则将锁升级为轻量级锁，原偏向的线程继续拥有锁，当前线程则走入到锁升级的逻辑里；如果偏向的线程已经不存活或者不在同步块中，则将对象头的mark word改为无锁状态（unlocked）**，之后再升级为轻量级锁。

由此可见，偏向锁升级的时机为：当锁已经发生偏向后，**只要有另一个线程尝试获得偏向锁，则该偏向锁就会升级成轻量级锁。当然这个说法不绝对，因为还有批量重偏向这一机制**

#### 偏向锁解锁过程

偏向锁不会自行解锁

**当有其他线程尝试获得锁时**，是根据对象的 mark word 中记录的 threadId 遍历偏向线程的lock record来确定该线程**是否还在执行同步块中的代码**。因此偏向锁的解锁很简单，仅仅将栈中的最近一条lock record的obj字段设置为null。需要注意的是，偏向锁的解锁步骤中并不会修改对象头中的thread id。

**一般来说，会在safepoint（安全点）中去查看偏向的线程是否还存活，如果存活且还在同步块中则将锁升级为轻量级锁，原偏向的线程继续拥有锁，当前线程则走入到锁升级的逻辑里；如果偏向的线程已经不存活或者不在同步块中，则将对象头的mark word改为无锁状态（unlocked）**

#### 批量重偏向与撤销（防止偏向锁过多）

从偏向锁的加锁解锁过程中可以看出，当只有一个线程反复进入同步块时，偏向锁带来的性能开销基本可以忽略，但是当有其他线程尝试获得锁时，就需要等到safe point时将偏向锁撤销为无锁状态或升级为轻量级/重量级锁。（safe point 见文章：[一文明白JVM-万字长文，遇人随便问][https://abelyang.blog.csdn.net/article/details/109436998] 的 5.2.3 章节）。总之，偏向锁的撤销是有一定成本的，如果说运行时的场景本身存在多线程竞争的，那偏向锁的存在不仅不能提高性能，而且会导致性能下降。因此，JVM中增加了一种**批量重偏向/撤销**的机制。

1. 一个线程创建了大量对象并执行了初始的同步操作，之后在另一个线程中将这些对象的锁进行之后的操作。这种case下，会导致大量的偏向锁撤销操作。

2. 存在明显多线程竞争的场景下使用偏向锁是不合适的，例如生产者/消费者队列。

**批量重偏向**（bulk rebias）机制是为了解决第一种场景。**批量撤销**（bulk revoke）则是为了解决第二种场景。

其做法是：以class为单位，为每个class维护一个**偏向锁撤销计数器**，每一次**该class的对象发生偏向撤销操作**时，该计数器+1，当这个**值达到重偏向阈值（默认20）**时，JVM就认为该class的偏向锁有问题，因此会进行**批量重偏向**。每个class对象会有一个对应的**epoch**字段，每个处于偏向锁状态**对象的mark word**中也有该字段，**其初始值为创建该对象时，class中的epoch的值**。每次发生**批量重偏向时，就将该值+1**，同时遍历JVM中所有线程的栈，找到**该class**所有正处于加锁状态的偏向锁，**将其epoch字段改为新值**（批量重偏向）。下次获得锁时，**发现当前对象的epoch值和class的epoch不相等，那就算当前已经偏向了其他线程**，也不会执行撤销操作，而是直接**通过CAS操作将其mark word的Thread Id 改成当前线程Id**。

当达到重偏向阈值后，**假设该class计数器继续增长**，当其达到**批量撤销的阈值后（默认40**），JVM就认为该class的使用场景存在多线程竞争，会标记该class为不可偏向，之后，**对于该class的锁，直接走轻量级锁的逻辑**。

## 1.3 轻量级锁实现

**多个**线程**交替**进入临界区（偏向锁的时候，被另外的线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不进行阻塞，只是交替执行，提高性能）

当有另一个线程与该线程同时竞争时，锁会升级为重量级锁。为了防止继续自旋，**一旦升级，将无法降级**。

### 过程

线程在执行同步块之前，JVM会先在当前的线程的栈帧中创建一个**Lock Record**，是线程私有的(TLS) 其包括一个用于存储对象头中的 m**ark word（官方称之为Displaced Mark Word）以及一个指向对象的指针**。下图右边的部分就是一个Lock Record。

![image-20201119220639215](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201119220639215.png)

### 加锁过程

#### **加锁过程：**

1. 当锁是偏向锁的时候，被另外的线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，从而提高性能。
2. 在代码进入同步块的时候，如果同步对象锁状态为**无锁状态**（锁标志位为“01”状态，是否为偏向锁为“1”），虚拟机首先将在当前线程的**栈帧**中建立一个名为锁记录（**Lock Record**）的空间，用于存储锁对象目前的**Mark Word的拷贝**，然后拷贝对象头中的Mark Word复制到锁记录中。
3. 拷贝成功后，虚拟机将使用CAS操作尝试将对象的**Mark Word更新为指向Lock Record的指针，并将Lock Record里的owner （object referce）指针指向对象的Mark Word。**
4. 如果这个更新动作成功了，**那么这个线程就拥有了该对象的锁，并且对象Mark Word的锁标志位设置为“00”**，表示此对象处于轻量级锁定状态。
5. 如果轻量级锁的更新操作失败了，虚拟机首先会检查对象的Mark Word是否指向当前线程的栈帧，如果是就说明当**前线程已经拥有了这个对象的锁，那就可以直接进入同步块继续执行**，**否则说明多个线程竞争锁**。
6. 若当前只有一个等待线程，则该线程通过自旋进行等待。但是**当自旋超过一定的次数，或者一个线程在持有锁，一个在自旋，又有第三个来访时，轻量级锁升级为重量级锁。**

### 解锁过程

1. 线程使用完对象用完之后，会遍历当前线程栈，找到对象的 markword 中记录的lock record ，且**检查 lock record 的 obj 指针 指向的 对象地址等于 当前需解锁对象**。使用原子的CAS将 lock record 的 Displaced MarkWord **替换回对象头**，如果成功，则表示没有竞争发生，如果替换失败则升级为重量级锁。
2. 如果Lock Record的Displaced Mark Word为 null，代表这是一次重入，将obj设置为null后continue。
3. 如果Lock Record的Displaced Mark Word不为null，则利用CAS指令将Displaced MarkWord替换回对象头。如果成功，则continue，否则 则说明对象锁已经膨胀为重量级锁了。

注意：

轻量级锁是第二个线程进来时获取的，那么在释放时优先考虑**并没有第三个线程参与竞争，没有重量级锁**，但是为了确保这个问题，释放时要检查**lock record指针和Displaced Mark Word是否与对象MarkWord相同**。

## 1.4 重量级锁实现

重量级锁：多个线程同时进入临界区。其他线程试图获取锁时，都会被阻塞，只有持有锁的线程释放锁之后才会唤醒这些线程，进行竞争。

此时就到了关键时刻了Monitor 就要出场了，`Synchronize`的实现原理，无论是同步方法还是同步代码块，无论是`ACC_SYNCHRONIZED`还是`monitorenter`、`monitorexit`都是基于`Monitor`实现的。

Monitor其实是一种同步工具，也可以说是一种同步机制。**每个对象都拥有自己的监视锁Monitor**

> 对象的所有方法都被“互斥”的执行。好比一个Monitor只有一个运行“许可”，任一个线程进入任何一个方法都需要获得这个“许可”，离开时把许可归还。
> 通常提供singal机制：允许正持有“许可”的线程暂时放弃“许可”，等待某个谓词成真（条件变量），而条件成立后，当前进程可以“通知”正在等待这个条件变量的线程，让他可以重新去获得运行许可。

在Java虚拟机(HotSpot)中，Monitor是基于C++实现的，由ObjectMonitor实现的，ObjectMonitor中有几个关键属性：

* _owner：指向持有ObjectMonitor对象的线程
* _WaitSet：存放处于wait状态的线程队列
* _EntryList：存放处于等待锁block状态的线程队列
* _recursions：锁的重入次数
* _count：用来记录该线程获取锁的次数
* _cxq 

其中cxq ，EntryList ，WaitSet都是 ObjectWaiter 对象的**链表结构**。**owner指向持有锁的线程**

当多个线程同时访问一段同步代码时，首先会**进入`_EntryList`队列**中，当某个线程**获取到对象的 monitor 后**进**入`_Owner`区域**并**把monitor中的`_owner`变量设置为当前线程**，同时**monitor中的计数器`_count`加1**。即获得对象锁。

若持有monitor的线程调**用`wait()`方**法，将**释放当前持有的monitor，`_owner`变量恢复为`null`，`_count`自减1**，同时该**线程进入`_WaitSet`集合中**等待被唤醒。若当前线程**执行完毕也将释放monitor(锁)并复位count变量**的值，以便其他线程进入获取monitor(锁)。如下图所示

![monitor](/Users/yangyibo/Desktop/monitor.png)

过程：

当一个线程尝试获得锁时，如果该锁已经被占用，则会将**该线程封装成一个ObjectWaiter对象**插入到cxq的**队列尾部**，然后**暂停当前线程（等待锁）**。当持有锁的线程释放锁前，会将cxq中的所有元素移动到**EntryList**中去，并唤醒EntryList的**队首线程**。

如果一个线程在同步块中调用了Object wait方法，会将该线程对应的ObjectWaiter从EntryList移除并加入到WaitSet中，然后释放锁。**当wait的线程被notify之后，会将对应的ObjectWaiter从WaitSet移动到EntryList中。**

# 2. Lock 接口

![image-20201122191912127](/Users/yangyibo/Library/Application Support/typora-user-images/image-20201122191912127.png)



* lock的存储结构：一个int类型状态值（用于锁的状态变更），一个双向链表（用于存储等待中的线程）

* lock获取锁的过程：本质上是通过CAS来获取状态值修改，如果当场没获取到，会将该线程放在线程等待链表中。

* lock释放锁的过程：修改状态值，调整等待链表。

lock：需要显示指定起始位置和终止位置。一般使用ReentrantLock类做为锁，多个线程中必须要使用一个ReentrantLock类做为对象才能保证锁的生效。且在加锁和解锁处需要通过lock()和unlock()显示指出。所以一般会在finally块中写unlock()以防死锁。

synchronized是托管给JVM执行的，而lock是java写的控制锁的代码。

## 2.1   Lock接口中每个方法的使用

* **lock()** 用来获取锁。如果锁已被其他线程获取，则进行等待。
* **tryLock()** 如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待，则返回true。
* **tryLock(long time, TimeUnit unit)** 方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
* **lockInterruptibly()**  是用来获取锁的。如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。
* **unLock()** 方法是用来释放锁的。

#3. AQS（AbstractQueuedSynchronizer） 

https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

Java中的大部分同步类（**Lock、Semaphore、ReentrantLock等**）都是基于AbstractQueuedSynchronizer（简称为AQS）实现的。AQS是一种提供了**原子式管理同步状态**、**阻塞和唤醒线程功能以及队列模型的简单框架**。

AQS核心思想是，如果被请求的共享资源空闲，那么就将当前请求资源的线程设置为有效的工作线程，将共享资源设置为锁定状态；如果共享资源被占用，就需要一定的**阻塞等待唤醒机制来保证锁分配**。这个机制主要用的是**CLH队列的变体**实现的，将暂时获取不到锁的线程加入到队列中。

AQS 的主要代码结构如下：

```java
abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    static final class Node {
  	//当前节点在队列中的状态 
    volatile int waitStatus;
    //前驱指针  
    volatile Node prev;
     //后继指针
    volatile Node next;
    //表示处于该节点的线程  
    volatile Thread thread
    //指向下一个处于Condition状态的节点
    Node nextWaiter;  
    private transient volatile Node head;
    private transient volatile Node tail;
    /**
     * The synchronization state. 用于可重入
     */  
    private volatile int state;
    
   /**
    * 内部类实现 Condition 接口
    */
    class ConditionObject implements Condition, java.io.Serializable{
    
    }
  }
}
```

| waitStatus 枚举 | 含义                                           |
| --------------- | ---------------------------------------------- |
| 0               | 当一个Node被初始化的时候的默认值               |
| CANCELLED ：1   | 为1，表示线程获取锁的请求已经取消了            |
| CONDITION：-2   | 为-2，表示节点在等待队列中，节点线程等待唤醒   |
| PROPAGATE：-3   | 为-3，当前线程处在SHARED情况下，该字段才会使用 |
| SIGNAL：-1      | 为-1，表示线程已经准备好了，就等资源释放了     |

## 3.1 CLH

 CLH(Craig, Landin, and Hagersten locks): 队列，是单向链表，**Java中 AQS的队列是CLH变体的虚拟 双向 队列**（FIFO）注意：（双向链表中，**第一个节点为虚节点，其实并不存储任何信息，只是占位**。真正的第一个有数据的节点，是在第二个节点开始的）。AQS是通过将**每条请求共享资源的线程封装成一个节点**来实现锁的分配。

AQS使用一个Volatile的int类型的成员变量**state**来表示同步状态，通过内置的FIFO队列来完成资源获取的排队工作，通过**Unsafe的 CAS**完成对State值的修改。

AbstractQueuedSynchronizer会把所有的**请求线程构成一个CLH队列**，**当一个线程执行完毕（lock.unlock()）时会激活自己的后继节点**，但**正在执行的线程并不在队列中**，而那些等待执行的线程全 部处于阻塞状态，

**线程到队列头部的时候，尝试CAS更新锁状态，如果更新成功表示该等待线程获取锁成功。从头部移除。**

 与synchronized相同的是，这也是一个**虚拟队列**，不存在队列实例，**仅存在节点之间的前后关系**（Node prev、 Node next;）。原生的CLH队列是用于自旋锁，

当有线程竞争锁时，该线程会首先尝试获得锁，这对于那些已经在队列中排队的线程来说显得不公平（非公平），与synchronized实现类似，这样会极大提高吞吐量。 如果已经存在Running线程，则新的竞争线程会被追加到队尾，具体是采用基于CAS的Lock-Free算法，因为线程并发对Tail调用CAS可能会 导致其他线程CAS失败，解决办法是循环CAS直至成功。

## 3.2 AQS数据结构

AQS中最基本的数据结构——**Node**，Node即为上面CLH变体队列中的节点

![AQSNode](/Users/yangyibo/Desktop/AQSNode.png)



## 3.3  同步状态State

AQS中维护了一个名为state的int型字段，意为同步状态，是由Volatile修饰的，用于展示当前临界资源的获锁情况，该字段用来描述有多少线程获持有锁。**state等于0表示没有线程持有锁**

| 方法名                                                       | 描述                 |
| ------------------------------------------------------------ | -------------------- |
| protected final int getState()                               | 获取State的值        |
| protected final void setState(int newState)                  | 设置State的值        |
| protected final boolean compareAndSetState(int expect, int update) | 使用CAS方式更新State |

这几个方法都是**Final修饰的**，说明子类中无法重写它们。我们可以通过修改State字段表示的同步状态来实现多线程的**独占模式和共享模式**（加锁过程）。

**独占模式加锁流程**

![AQS独占锁](/Users/yangyibo/Desktop/AQS独占锁.png)

**共享模式加锁流程**

![AQS共享锁](/Users/yangyibo/Desktop/AQS共享锁.png)

###  3.3.1 通过 state 了解 ReentrantReadWriteLock  独占模式和共享模式

```java
public class ReentrantReadWriteLock
    implements ReadWriteLock, java.io.Serializable {
    /** Inner class providing readlock */
    private final ReentrantReadWriteLock.ReadLock readerLock;
    /** Inner class providing writelock */
    private final ReentrantReadWriteLock.WriteLock writerLock;
   }
```

ReentrantReadWriteLock有两把锁：ReadLock和WriteLock，由词知意，一个读锁一个写锁，合称“读写锁”。再进一步观察可以发现ReadLock和WriteLock是靠内部类Sync实现的锁。**Sync是AQS的一个子类，这种结构在CountDownLatch、ReentrantLock、Semaphore里面也都存在。**

读锁和写锁的锁主体都是Sync，但读锁和写锁的加锁方式不一样。**读锁是共享锁**，**写锁是独享锁**。读锁的共享锁可保证并发读非常高效，而读写、写读、写写的过程互斥，因为读锁和写锁是分离的。所以ReentrantReadWriteLock的并发性相比一般的互斥锁有了很大提升。

AQS的时候我们也提到了**state字段（int类型，32位），该字段用来描述有多少线程获持有锁**。

在独享锁中这个值通常是0或者1（如果是重入锁的话state值就是重入的次数），在共享锁中state就是持有锁的数量。但是在ReentrantReadWriteLock中有读、写两把锁，所以需要在一个整型变量state上分别描述读锁和写锁的数量（或者也可以叫状态）。于是将state变量“按位切割”切分成了两个部分，**高16位表示读锁状态（读锁个数）**，**低16位表示写锁状态（写锁个数）**。如下图所示：

![AQS32位读写锁](/Users/yangyibo/Desktop/AQS32位读写锁.png)

### 3.3.2 WriteLock写锁（独享锁）

```java
public static class WriteLock implements Lock, java.io.Serializable {
   private final Sync sync;
   protected WriteLock(ReentrantReadWriteLock lock) {
            sync = lock.sync;
        }
  //加锁代码
  protected final boolean tryAcquire(int acquires) {
	Thread current = Thread.currentThread();
	int c = getState(); // 取到当前锁的个数
	int w = exclusiveCount(c); // 取写锁的个数w
	if (c != 0) { // 如果已经有线程持有了锁(c!=0)
		if (w == 0 || current != getExclusiveOwnerThread()) // 如果写线程数（w）为0（换言之存在读锁） 或者持有锁的线程不是当前线程就返回失败
			return false;
		if (w + exclusiveCount(acquires) > MAX_COUNT)    // 如果写入锁的数量大于最大数（65535，2的16次方-1）就抛出一个Error。
      throw new Error("Maximum lock count exceeded");
		// Reentrant acquire
    setState(c + acquires);
    return true;
  }
  if (writerShouldBlock() || !compareAndSetState(c, c + acquires)) // 如果当且写线程数为0，并且当前线程需要阻塞那么就返回失败；或者如果通过CAS增加写线程数失败也返回失败。
		return false;
	setExclusiveOwnerThread(current); // 如果c=0，w=0或者c>0，w>0（重入），则设置当前线程或锁的拥有者
	return true;
 }
}
```

- 这首先取到**当前锁的个数c**，然后再通过c来获取写锁的个数。因为**写锁是低16位**，所以取低16位的最大值与当前的c做与运算（ int w = exclusiveCount©; ），高16位和0与运算后是0，剩下的就是低位运算的值，同时也是持有写锁的线程数目。
- 在取到写锁线程的数目后，首先**判断是否已经有线程持有了锁。如果已经有线程持有了锁(c!=0)**，则查看当前写锁线程的数目，如果写线程数为0（即此时存在读锁）或者持有锁的线程不是当前线程就返回失败，则进入队列（涉及到公平锁和非公平锁的实现）。
- 如果写入锁的数量大于最大数（65535，2的16次方-1）就抛出一个Error。
- 如果当且写线程数为0（那么读线程也应该为0，因为上面已经处理c!=0的情况），并且当前线程需要阻塞那么就返回失败；如果通过CAS增加写线程数失败也返回失败。
- 如果c=0,w=0或者c>0,w>0（重入），则设置当前线程或锁的拥有者，返回成功！

tryAcquire() 除了重入条件（当前线程为获取了写锁的线程）之外，增加了一个读锁是否存在的判断。如果存在读锁，则写锁不能被获取，原因在于：必须确保写锁的操作对读锁可见，如果允许读锁在已被获取的情况下对写锁的获取，那么正在运行的其他读线程就无法感知到当前写线程的操作。

因此，只有等待其他读线程都释放了读锁，写锁才能被当前线程获取，而写锁一旦被获取，则其他读写线程的后续访问均被阻塞。写锁的释放与ReentrantLock的释放过程基本类似，每次释放均减少写状态，当写状态为0时表示写锁已被释放，然后等待的读写线程才能够继续访问读写锁，同时前次写线程的修改对后续的读写线程可见。

### 3.3.3 ReadLock （共享锁）

```java
  public static class ReadLock implements Lock, java.io.Serializable {
    private final Sync sync;
    //读锁
    protected ReadLock(ReentrantReadWriteLock lock) {
            sync = lock.sync;
        }
    
    //获取读锁
    protected final int tryAcquireShared(int unused) {
    Thread current = Thread.currentThread();
    int c = getState();
    if (exclusiveCount(c) != 0 &&
        getExclusiveOwnerThread() != current)
        return -1;   // 如果其他线程已经获取了写锁，则当前线程获取读锁失败，进入等待状态
    int r = sharedCount(c);
    if (!readerShouldBlock() &&
        r < MAX_COUNT &&
        compareAndSetState(c, c + SHARED_UNIT)) {
        if (r == 0) {
            firstReader = current;
            firstReaderHoldCount = 1;
        } else if (firstReader == current) {
            firstReaderHoldCount++;
        } else {
            HoldCounter rh = cachedHoldCounter;
            if (rh == null || rh.tid != getThreadId(current))
                cachedHoldCounter = rh = readHolds.get();
            else if (rh.count == 0)
                readHolds.set(rh);
            rh.count++;
        }
        return 1;
    }
    return fullTryAcquireShared(current);
 }
}
```

可以看到在tryAcquireShared(int unused)方法中，如果其他线程已经获取了写锁，则当前线程获取读锁失败，进入等待状态。如**果当前线程获取了写锁或者写锁未被获取，则当前线程（线程安全，依靠CAS保证）增加读状态，成功获取读锁。**读锁的每次释放（线程安全的，可能有多个读线程同时释放读锁）均减少读状态，减少的值是“1<<16”。所以读写锁才能实现读读的过程共享，而读写、写读、写写的过程互斥。

## 3.4 AQS 小结

| 同步工具               | 同步工具与AQS关联                                            |
| ---------------------- | ------------------------------------------------------------ |
| ReentrantLock          | 使用AQS保存锁重复持有的次数。当一个线程获取锁时，ReentrantLock记录当前获得锁的线程标识，用于检测是否重复获取，以及错误线程试图解锁操作时异常情况的处理。 |
| Semaphore              | 使用AQS同步状态来保存信号量的当前计数。tryRelease会增加计数，acquireShared会减少计数。 |
| CountDownLatch         | 使用AQS同步状态来表示计数。计数为0时，所有的Acquire操作（CountDownLatch的await方法）才可以通过。 |
| ReentrantReadWriteLock | 使用AQS同步状态中的16位保存写锁持有的次数，剩下的16位用于保存读锁的持有次数。 |
| ThreadPoolExecutor     | Worker利用AQS同步状态实现对独占线程变量的设置（tryAcquire和tryRelease）。 |
| NonReentrantLock       | 继承AQS，实现tryAcquire 和 tryRelease 实现非重入锁           |

# 4. ReentrantLock

ReentrantLock 实现了Lock接口，是Lock 的实现类。 通过内部抽象类 Sync 实现了**公平锁和非公平锁**。Sync继承 AbstractQueuedSynchronizer 实现了**锁的可重入**，并且是**独享锁模式**。ReentrantLock意思为**可重入**锁，指的是一个线程能够对一个临界资源重复加锁。ReentrantLock 其部分代码结构如下：

```java
class ReentrantLock implements Lock, java.io.Serializable {
   
    private final Sync sync;
    
    abstract static class Sync extends AbstractQueuedSynchronizer {}
    
    /**
     * Sync object for fair locks
     */
    static final class FairSync extends Sync{}
    /**
     * Sync object for non-fair locks
     */
    static final class NonfairSync extends Sync{}
   
}
```
ReenTrantLock的实现是一种**自旋锁**，通过循环调用CAS操作来实现加锁。它的性能比较好也是因为**避免了使线程进入内核态的阻塞状态。**

锁类型：ReentrantLock可以指定构造函数的boolean类型来创建公平锁和**非公平锁（默认）**

可重复：﻿**每次加锁都是对state加1，可重入则是对state叠加，每次unlock都会对state减1，当减到0时表示线程释放锁**。

## 4. 1 FairSync (公平锁)

**当有线程请求获取锁时，公平锁则会验证阻塞队列是否有阻塞线程，有队列会加到阻塞队列中**

```
static final class FairSync extends Sync {
  ...  
	final void lock() {
	//Acquire方法是FairSync和UnfairSync的父类AQS中的核心方法。
		acquire(1);
	}
  ...
}
```

结合公平锁和非公平锁的加锁流程，虽然流程上有一定的不同，但是都调用了Acquire方法，而Acquire方法是FairSync和UnfairSync的父类AQS中的核心方法。

## 4.2 NonfairSync （非公平锁 , 默认，提高吞吐量）

当有线程竞争锁时，当前线程会首先尝试获得锁而不是在队列中进行排队等候，这对于那些已经在队列中排队的线程来说显得不公平，这也是非公平锁

```java
// 非公平锁
static final class NonfairSync extends Sync {
	...
	final void lock() {
		if (compareAndSetState(0, 1))
			setExclusiveOwnerThread(Thread.currentThread());
		else
      //Acquire方法是FairSync和UnfairSync的父类AQS中的核心方法。
			acquire(1);
		}
  ...
}
```

- 若通过CAS设置变量State（同步状态）成功，也就是**获取锁成功**，则将当前线程设置为独占线程。
- 若通过CAS设置变量State（同步状态）失败，也就是**获取锁失败**，则**进入Acquire方法进行后续处理**。

某个线程获取锁失败的后续流程有以下两种可能：

* 将当**前线程获锁结果设置为失败，获取锁流程结束**。这种设计会**极大降低系统的并发度**，并不满足我们实际的需求。所以就需要一种流程，也就是**AQS框架的处理**流程。**存在某种排队等候机制，线程继续等待，仍然保留获取锁的可能**，获取锁流程仍在继续。

**非公平锁提高吞吐量**

假如一个线程刚好释放锁后唤醒后一个等待的线程A准备去获取锁时，一个新线程B进来尝试获取就会直接成功。如果在待唤醒的线程A获取锁之前，这个新进来的线程B持有锁然后再释放锁就不会影响待唤醒线程A的获取锁。这样就提高了程序的吞吐量。即使线程A在获取锁时因为B还没释放而获取失败，也不过是重新获取，不论怎样都提高了线程B的响应，提高了整体索取锁的吞吐量

## 4.3 通过公平锁非公平锁看ReentrantLock实现

### 4.3.1 **加锁：**

- 通过ReentrantLock的加锁方法Lock进行加锁操作。
- 会调用到内部类Sync的Lock方法，由于Sync#lock是抽象方法，根据ReentrantLock初始化选择的公平锁和非公平锁，执行相关内部类的Lock方法，**本质上都会执行AQS的Acquire方法**。
- AQS的Acquire方法会执行tryAcquire方法，但是由于tryAcquire需要自定义同步器实现，因此执行了ReentrantLock中的tryAcquire方法，由于**ReentrantLock是通过公平锁和非公平锁内部类实现的tryAcquire方法，因此会根据锁类型不同，执行不同的tryAcquire**（此处公平锁会直接加入CLH队列，非公平锁会尝试获取锁，若失败则加入CLH队列）。
- tryAcquire是获取锁逻辑，获取失败后，会执行框架AQS的后续逻辑，跟ReentrantLock自定义同步器无关。

### 4.3.2 **解锁：**

- 通过ReentrantLock的解锁方法Unlock进行解锁。
- ReentrantLock里面的公平锁和非公平锁的父类Sync定义了可重入锁的释放锁机制。Unlock会调用内部类Sync的**Release方法，该方法继承于AQS。**
- Release中会调用tryRelease方法，tryRelease需要自定义同步器实现**，tryRelease只在ReentrantLock中的Sync实现，因此可以看出，释放锁的过程，并不区分是否为公平锁。**
- 释放成功后，所有处理由AQS框架完成，与自定义同步器无关。

### 4.3.3 小结

由此可见ReentrantLock中公平锁和非公平锁在**底层是相同**的，这里以非公平锁为例进行分析。

（1）当调用 lock 方法 进行 compareAndSetState(0, 1)获取锁时，如果成功了则 setExclusiveOwnerThread(Thread.currentThread()); 设置当前线程为独占线程 否则执行 Acquire(1)。

（2）当执行Acquire(1)时，会通过tryAcquire获取锁。在这种情况下，如果获取锁失败，就会调用addWaiter加入到等待队列中去。获取锁失败后，会执行addWaiter(Node.EXCLUSIVE)加入等待队列

（3）addWaiter方法，这个方法其实就是把对应的线程以Node的数据结构形式加入到双端队列里，返回的是一个包含该线程的Node。而这个Node会作为参数，进入到acquireQueued方法中。acquireQueued方法可以对排队中的线程进行“获锁”操作。

（4）总的来说，一个线程获取锁失败了，被放入等待队列，acquireQueued会把放**入队列中的线程不断去获取锁，直到获取成功或者不再需要获取（中断）**。

（5）为了防止因节点 自旋未获取到锁， 在死循环导致CPU资源被浪费。我们会判断前置节点的状态来决定是否要将当前线程挂起。

* （1）当前置节点状态 为 -1 时当前线程阻塞；
* （2）当前置节点状态 大于 0 时，前置节点为取消状态，则移除改前驱节点和素有前驱被取消的节点。
* （3）如果前置节点的状态 小于-1时，则cas设置前驱节点状态为-1，此时当前线程不阻塞。会继续循环。

### 4.3.4 **小考试：**

Q：某个线程获取锁失败的后续流程是什么呢？

A：存在某种排队等候机制，线程继续等待，仍然保留获取锁的可能，获取锁流程仍在继续。

Q：既然说到了排队等候机制，那么就一定会有某种队列形成，这样的队列是什么数据结构呢？

A：是CLH变体的FIFO双端队列。

Q：处于排队等候机制中的线程，什么时候可以有机会获取锁呢？

A：当前置节点是头结点，且当前线程获取锁成功”时当前节点会去尝试获取锁跳出循环，否则就挂起当前线程，阻塞调用栈，返回当前线程的中断状态。为了防止因节点 自旋获取锁， 在死循环导致CPU资源被浪费，我们会判断前置节点的状态来决定是否要将当前线程中断挂起。

* （1）当前置节点状态 为 -1 时当前线程阻塞；
* （2）当前置节点状态 大于 0 时，前置节点为取消状态，则移除改前驱节点和素有前驱被取消的节点。
* （3）如果前置节点的状态 小于-1时，则cas设置前驱节点状态为-1，此时当前线程不阻塞。

Q：如果处于排队等候机制中的线程一直无法获取锁，需要一直等待么？还是有别的策略来解决这一问题？

A：线程所在节点的状态会变成取消状态，取消状态的节点会从队列中释放

Q：Lock函数通过Acquire方法进行加锁，但是具体是如何加锁的呢？

A：AQS的Acquire会调用tryAcquire方法，tryAcquire由各个自定义同步器实现，通过tryAcquire完成加锁过程。

## 4.5 ReentrantLock的 可重入

ReentrantLock的可重入性是AQS很好的应用之一，我们很容易得知ReentrantLock实现可重入的方法。在ReentrantLock里面，不管是公平锁还是非公平锁，都有一段逻辑。

```java
//加锁可重入此处以非公平锁加锁代码为例
final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
              //当前线程已经获取锁则重入 C+1
                int nextc = c + acquires;
                if (nextc < 0) 
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

//解锁
 protected final boolean tryRelease(int releases) {
   					//锁状态 - 1
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }
```

从上面代码可以看到，有一个同步状态State来控制整体可重入的情况。State是Volatile修饰的，用于保证一定的可见性和有序性。

1. State初始化的时候为0，表示没有任何线程持有锁。
2. 当有线程持有该锁时，值就会在原来的基础上+1，同一个线程多次获得锁是，就会多次+1，这里就是可重入的概念。
3. 解锁也是对这个字段-1，一直到0，此线程对锁释放。

### 4.5.1 NonReentrantLock非可重入

NonReentrantLock 非可重入锁，加锁时直接设置状态为1，设置失败则返回false。释放锁时直接设置状态为0

```java
class NonReentrantLock extends AbstractQueuedSynchronizer implements Lock{
  
  // 获取锁，直接设置state为1
   protected boolean tryAcquire(int acquires) {
        if(this.compareAndSetState(0, 1)) {
            this.owner = Thread.currentThread();
            return true;
        } else {
            return false;
        }
    }

  //释放锁直接设置State 为0
    protected boolean tryRelease(int releases) {
        if(Thread.currentThread() != this.owner) {
            throw new IllegalMonitorStateException();
        } else {
            this.owner = null;
            this.setState(0);
            return true;
        }
    }

}

```

# 5. synchronized VS Lock

##5.1 有了 synchronized **那么为什么会出现Lock呢？**

　　synchronized是java中的一个关键字，是管程（monitor）的一个实现，也就是说是Java语言内置的特性。**那么为什么会出现Lock呢？**

　如果一个代码块被synchronized修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况：

　　1）获取锁的线程执行完了该代码块，然后线程释放对锁的占有；

　　2）线程执行发生异常，此时JVM会让线程自动释放锁。

　　那么如果这个获取锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，其他线程便只能干巴巴地等待，影响程序执行效率。所以就搞了个Lock

Lock搞了3种方法来破环不可抢占的条件。
1、`void lockInterruptibly() throws InterruptedException;` 这是个支持中断的API。Synchronized进入阻塞之后就没办法唤醒它，所以针对这个问题想了个支持响应中断的方法，让线程阻塞(lock下是等待状态)的时候可以响应中断信号，从而有机会释放已占有的资源来破环不可抢占的条件。
2、`boolean tryLock();`这就是在获取锁的时候，如果获取不到就直接返回，这样也有机会释放已占有的资源来破环不可抢占的条件。
3、`boolean tryLock(long time, TimeUnit unit) throws InterrptedException;`这是个支持超时的API，也就是让在一段时间内获取不到资源的线程直接返回一个错误，不进入阻塞状态，那也有机会释放已占有的资源来破环不可抢占的条件。
然后再来说说Lock的实现类一共有三个，一个是ReentrantLock,另两个是ReentrantReadWriteLock类中的两个静态内部类ReadLock和WriteLock。实现思路大同小异。

## 5.2 synchronized和lock用途区别

synchronized原语和ReentrantLock在一般情况下没有什么区别，但是在非常复杂的同步应用中，请考虑使用ReentrantLock，特别是遇到下面2种需求的时候。

1.某个线程在等待一个锁的控制权的这段时间需要中断
2.需要分开处理一些wait-notify，ReentrantLock里面的Condition应用，能够控制notify哪个线程
3.具有公平锁功能，每个到来的线程都将排队等候

lock 接口的最大优势是它为读和写提供两个单独的锁，可以让你构建高性能数据结构，比如 ConcurrentHashMap 和条件阻塞。

1.首先synchronized是java内置关键字，在jvm层面，Lock是个java类；

2.synchronized无法判断是否获取锁的状态，Lock可以判断是否获取到锁；

3.synchronized会自动释放锁(a 线程执行完同步代码会释放锁 ；b 线程执行过程中发生异常会释放锁)，Lock需在finally中手工释放锁（unlock()方法释放锁），否则容易造成线程死锁；

4.用synchronized关键字的两个线程1和线程2，如果当前线程1获得锁，线程2线程等待。如果线程1阻塞，线程2则会一直等待下去，而Lock锁就不一定会等待下去，如果尝试获取不到锁，线程可以不用一直等待就结束了；（也可已通过lockInterruptibly()方法中断等待）

5.synchronized的锁可重入、不可中断、非公平，而Lock锁可重入、可判断、可公平和非公平（两者皆可）

6.Lock锁适合大量同步的代码的同步问题，synchronized锁适合代码少量的同步问题。



lock大量使用CAS+自旋。因此根据CAS特性，lock建议使用在低锁冲突的情况下。

java1.6以后，官方对synchronized做了大量的锁优化（偏向锁、自旋、轻量级锁）。因此在非必要的情况下，建议使用synchronized做同步操作。

synchronized的底层也是一个基于CAS操作的等待队列，但JVM实现的更精细，把等待队列分为ContentionList和EntryList，目的是为了降低线程的出列速度；当然也实现了偏向锁，从数据结构来说二者设计没有本质区别。但synchronized还实现了自旋锁，并针对不同的系统和硬件体系进行了优化，而Lock则完全依靠系统阻塞挂起等待线程。

本文参考：

[从ReentrantLock的实现看AQS的原理及应用][https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html]

[不可不说的Java“锁”事](https://tech.meituan.com/2018/11/15/java-lock.html)

[死磕synchronized][https://mp.weixin.qq.com/s/zaazbtl3_PCwaRqK2gy_Lw]



## 小扩展自己实现AQS

从架构图中可以得知，AQS提供了大量用于自定义同步器实现的Protected方法。自定义同步器实现的相关方法也只是为了通过修改State字段来实现**多线程的独占模式或者共享模式**。自定义同步器需要实现以下方法（ReentrantLock需要实现的方法如下，并不是全部）：

| 方法名|描述|
| ------------------------------------------- | ------------------------------------------------------------ |
| protected boolean isHeldExclusively()       | 该线程是否正在独占资源。只有用到Condition才需要去实现它。    |
| protected boolean tryAcquire(int arg)       | 独占方式。arg为获取锁的次数，尝试获取资源，成功则返回True，失败则返回False。 |
| protected boolean tryRelease(int arg)       | 独占方式。arg为释放锁的次数，尝试释放资源，成功则返回True，失败则返回False。 |
| protected int tryAcquireShared(int arg)     | 共享方式。arg为获取锁的次数，尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。 |
| protected boolean tryReleaseShared(int arg) | 共享方式。arg为释放锁的次数，尝试释放资源，如果释放后允许唤醒后续等待结点返回True，否则返回False。 |

一般来说，自定义同步器要么是独占方式，要么是共享方式，它们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。ReentrantLock是独占锁，所以实现了tryAcquire-tryRelease。