死磕 synchronized 和 lock 系

# 1. synchronized

- synchronized修饰非静态方法以及同步代码块的synchronized (this)用法和synchronized (非this对象)的用法锁的都是**对象**，线程想要执行对应同步代码，需要获得**对象锁**。   
- synchronized修饰静态方法以及同步代码块的**synchronized (类.class)**用法锁的**是类**，线程想要执行对应同步代码，需要获得**类锁。**
- 在java中，**同步加锁的是一个对象或者一个类，而不是代码**。
- **不同的对象实例的synchronized方法是不相干扰的**。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法。
- **对一个全局对象或者类加锁时，对该类的所有对象都起作用**。(因为加的是类锁)
- 一个对象中的同步方法一次只能被一个线程访问，如果有多个同步方法，一个线程一次也只能访问其中的一个同步方法，但是**非同步方法不受任何影响**
- 同步是通过加锁的形式来控制的，让一个线程访问一个同步方法时会获得这个对象的锁，只有退出同步方法时才会释放这个锁，**其它线程才可访问**
- **synchronized关键字是不能继承的**，也就是说，基类的方法```synchronized f(){} ```在继承类中并不自动是```synchronized f(){}```，而是变成了f(){}。继承类需要你显式的指定它的某个方法为synchronized方法。

### synchronized关键字可标记四种代码块：

1. 实例方法  锁对象
2. 静态方法 锁类
3. 实例方法中的代码块  锁对象
4. 静态方法中的代码块  锁类

### synchronized 的实现

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

## 偏向锁实现

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

## 轻量级锁实现

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

## 重量级锁实现

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



##有了 synchronized **那么为什么会出现Lock呢？**

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



# AQS 、 lock 

## 抽象类 AbstractQueuedSynchronizer 继承 AbstractOwnableSynchronizer
## 接口 lock
## 实现类 ReentrantLock、NonReentrantLock
## 内部类 FairSync  NonfairSync 

ReentrantLock implements Lock 内部类 NonfairSync extends Sync  extends AbstractQueuedSynchronizer

NonReentrantLock extends AbstractQueuedSynchronizer implements Lock 

画 类图 贴代码。结合公平锁，非公平锁，可重入非可重入，共享锁，非共享锁。

https://tech.meituan.com/2018/11/15/java-lock.html

### 公平锁 VS 非公平锁

### 可重入锁 VS 非可重入锁

### 独享锁 VS 共享锁


首先重入锁ReentrantLock和非可重入锁NonReentrantLock都继承父类AQS，其父类AQS中维护了一个同步状态status来计数重入次数，status初始值为0。

释义：

加锁： 当线程尝试获取锁时，可重入锁先尝试获取并更新status值，如果status == 0表示没有其他线程在执行同步代码，则把status置为1，当前线程开始执行。如果status != 0，则判断当前线程是否是获取到这个锁的线程，如果是的话执行status+1，且当前线程可以再次获取锁。

释放锁时，可重入锁同样先获取当前status的值，在当前线程是持有锁的线程的前提下。如果status-1 == 0，则表示当前线程所有重复获取锁的操作都已经执行完毕，然后该线程才会真正释放锁。

当线程尝试获取锁时，可重入锁先尝试获取并更新status值，如果status == 0表示没有其他线程在执行同步代码，则把status置为1，当前线程开始执行。如果status != 0，则判断当前线程是否是获取到这个锁的线程，如果是的话执行status+1，且当前线程可以再次获取锁。而非可重入锁是直接去获取并尝试更新当前status的值，如果status != 0的话会导致其获取锁失败，当前线程阻塞。

释放锁时，可重入锁同样先获取当前status的值，在当前线程是持有锁的线程的前提下。如果status-1 == 0，则表示当前线程所有重复获取锁的操作都已经执行完毕，然后该线程才会真正释放锁。而非可重入锁则是在确定当前线程是持有锁的线程之后，直接将status置为0，将锁释放。

## synchronized和lock用途区别

synchronized原语和ReentrantLock在一般情况下没有什么区别，但是在非常复杂的同步应用中，请考虑使用ReentrantLock，特别是遇到下面2种需求的时候。

1.某个线程在等待一个锁的控制权的这段时间需要中断
2.需要分开处理一些wait-notify，ReentrantLock里面的Condition应用，能够控制notify哪个线程
3.具有公平锁功能，每个到来的线程都将排队等候

lock 接口的最大优势是它为读和写提供两个单独的锁，可以让你构建高性能数据结构，比如 ConcurrentHashMap 和条件阻塞。



https://mp.weixin.qq.com/s/zaazbtl3_PCwaRqK2gy_Lw

https://tech.meituan.com/2018/11/15/java-lock.html

http://blog.csdn.net/suifeng3051/article/details/52164267