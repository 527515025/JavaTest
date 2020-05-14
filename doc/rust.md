# Rust 所有权

计算机程序必须在运行时管理它们所使用的内存资源。

大多数的编程语言都有管理内存的功能：

C/C++ 这样的语言主要通过手动方式管理内存，开发者需要手动的申请和释放内存资源。但为了提高开发效率，只要不影响程序功能的实现，许多开发者没有及时释放内存的习惯。所以手动管理内存的方式常常造成资源浪费。

Java 语言编写的程序在虚拟机（JVM）中运行，JVM 具备自动回收内存资源的功能。但这种方式常常会降低运行时效率，所以 JVM 会尽可能少的回收资源，这样也会使程序占用较大的内存资源。

所有权对大多数开发者而言是一个新颖的概念，它是 Rust 语言为高效使用内存而设计的语法机制。所有权概念是为了让 Rust 在编译阶段更有效地分析内存资源的有用性以实现内存管理而诞生的概念。

### 所有权规则

所有权有以下三条规则：

- Rust 中的每个值都有一个变量，称为其所有者。
- 一次只能有一个所有者。
- 当所有者不在程序运行范围时，该值将被删除。

### 变量范围

我们用下面这段程序描述变量范围的概念：

```
{
    // 在声明以前，变量 s 无效
    let s = "runoob";
    // 这里是变量 s 的可用范围
}
// 变量范围已经结束，变量 s 无效
```

**变量范围是变量的一个属性**，其代表变量的**可行域**，默认从声明变量开始有效直到变量所在域结束。

## 内存和分配

如果我们定义类一个变量并给它赋予一个值，这个变量的值存在于内存中。这种情况很普遍。但如果我们需要储存的数据长度不确定（比如用户输入的一串字符串），我们就无法在定义时明确数据长度，也就无法在编译阶段令程序分配固定长度的内存空间供数据储存使用。（有人说分配尽可能大的空间可以解决问题，但这个方法很不文明）。这就需要提供一种在**程序运行时程序自己申请使用内存的机制——堆**。本章所讲的所有"内存资源"都指的是堆所占用的内存空间。

有分配就有释放，程序不能一直占用某个内存资源。因此决定资源是否浪费的关键因素就是资源有没有及时的释放。
Rust 中没有类似c语言调用 free 函数来释放字资源的方法，Rust 之所以没有明示释放的步骤是因为**在变量范围结束的时候，Rust 编译器自动添加了调用释放资源函数的步骤**。



## 变量与数据交互的方式

变量与数据交互方式主要有移动（Move）和克隆（Clone）两种：

### 移动

多个变量可以在 Rust 中以不同的方式**与相同的数据交互**：

```
let x = 5;
let y = x;
```

这个程序将值 5 绑定到变量 x，然后将 x 的值复制并赋值给变量 y。现在栈中将有两个值 5。此情况中的数据是"基本数据"类型的数据，不需要存储到堆中，**仅在栈中的数据的"移动"方式是直接复制**，这不会花费更长的时间或更多的存储空间。"基本数据"类型有这些：

- 所有整数类型，例如 i32 、 u32 、 i64 等。
- 布尔类型 bool，值为 true 或 false 。
- 所有浮点类型，f32 和 f64。
- 字符类型 char。
- 仅包含以上类型数据的元组（Tuples）。

产生一个 String 对象，值为 "hello"。其中 "hello" 可以认为是类似于长度不确定的数据，需要在堆中存储。

```
let s1 = String::from("hello");
let s2 = s1;
```

两个 String 对象在栈中，每个 String 对象都有一个指针指向堆中的 "hello" 字符串。在给 s2 赋值时，只有栈中的数据被复制了，堆中的字符串依然还是原来的字符串。前面我们说过，当变量超出范围时，Rust 自动调用释放资源函数并清理该变量的堆内存。但是 s1 和 s2 都被释放的话堆区中的 "hello" 被释放两次，这是不被系统允许的。为了确保安全，在给 s2 赋值时 s1 已经无效了。没错，在把 s1 的值赋给 s2 以后 s1 将不可以再被使用。下面这段程序是错的：

```
let s1 = String::from("hello");
let s2 = s1; 
println!("{}, world!", s1); // 错误！s1 已经失效
```

## 克隆

Rust会尽可能地降低程序的运行成本，所以默认情况下，长度较大的数据存放在堆中。且采用移动的方式进行数据交互。但如果需要将数据单纯的复制一份以供他用，可以使用数据的第二种交互方式——克隆。

```
fn main() {
  let s1 = String::from("hello");
  let s2 = s1.clone();
  println!("s1 = {}, s2 = {}", s1, s2);
}
```



运行结果：

```
s1 = hello, s2 = hello
```

这里是真的**将堆中的 "hello" 复制了一份**，所以 s1 和 s2 都分别绑定了一个值，释放的时候也会被当作两个资源。当然，克隆仅在需要复制的情况下使用，毕竟复制数据会花费更多的时间。

## 涉及函数的所有权机制

```
fn main() {
    let s = String::from("hello");
    // s 被声明有效

    takes_ownership(s);
    // s 的值被当作参数传入函数
    // 所以可以当作 s 已经被移动，从这里开始已经无效

    let x = 5;
    // x 被声明有效

    makes_copy(x);
    // x 的值被当作参数传入函数
    // 但 x 是基本类型，依然有效
    // 在这里依然可以使用 x 却不能使用 s

} // 函数结束, x 无效, 然后是 s. 但 s 已被移动, 所以不用被释放


fn takes_ownership(some_string: String) {
    // 一个 String 参数 some_string 传入，有效
    println!("{}", some_string);
} // 函数结束, 参数 some_string 在这里释放

fn makes_copy(some_integer: i32) {
    // 一个 i32 参数 some_integer 传入，有效
    println!("{}", some_integer);
} // 函数结束, 参数 some_string 是基本类型, 无需释放
```

如果将变量当作参数传入函数，那么它和移动的效果是一样的。

### 函数返回值的所有权机制

```
fn main() {
    let s1 = gives_ownership();
    // gives_ownership 移动它的返回值到 s1

    let s2 = String::from("hello");
    // s2 被声明有效

    let s3 = takes_and_gives_back(s2);
    // s2 被当作参数移动, s3 获得返回值所有权
} // s3 无效被释放, s2 被移动, s1 无效被释放.

fn gives_ownership() -> String {
    let some_string = String::from("hello");
    // some_string 被声明有效

    return some_string;
    // some_string 被当作返回值移动出函数
}

fn takes_and_gives_back(a_string: String) -> String { 
    // a_string 被声明有效

    a_string  // a_string 被当作返回值移出函数
}
```

被当作函数返回值的变量所有权将会被移动出函数并返回到调用函数的地方，而不会直接被无效释放。

## 引用与租借

引用（Reference）是 C++ 开发者较为熟悉的概念。

如果你熟悉指针的概念，你可以把它看作一种指针。

实质上"引用"是变量的间接访问方式。

```
fn main() {
    let s1 = String::from("hello");
    let s2 = &s1; //引用，& 运算符可以取变量的"引用"。
    println!("s1 is {}, s2 is {}", s1, s2);
}
//运行结果：
s1 is hello, s2 is hello
```

当一个**变量的值被引用时，变量本身不会被认定无效**。因为"引用"并没有在栈中复制变量的值：函数参数传递的道理一样：

```
fn main() {
    let s1 = String::from("hello");
		//引用s1传递，此时s1仍然是有效的
    let len = calculate_length(&s1);

    println!("The length of '{}' is {}.", s1, len);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}
```

引用不会获得值的所有权。引用只能租借（Borrow）值的所有权。**引用本身也是一个类型并具有一个值**，**这个值记录的是别的值所在的位置，但引用不具有所指值的所有权**：

```
fn main() {
    let s1 = String::from("hello");
    let mut s2 = &s1;
    let s3 = s1; 
    //println!("{}", s2); //此时s2是无效的因为s2，因为 s2 租借的 s1 已经将所有权移动到 s3，所以 s2 将无法继续租借使用 s1 的所有权
    s2 = &s3; // 重新从 s3 租借所有权
    println!("{}", s2);
}
```

既然引用不具有所有权，即使它**租借了所有权，它也只享有使用权**（这跟租房子是一个道理）。

如果尝试利**用租借来的权利来修改数据**会被阻止：

```
fn main() {
    let s1 = String::from("run");
    let s2 = &s1;
    println!("{}", s2);
    s2.push_str("oob"); // 错误，禁止修改租借的值
    println!("{}", s2);
}
```

当然，也存在一种可变的租借方式，就像你租一个房子，如果物业规定房主可以修改房子结构，房主在租借时也在**合同中声明赋予你这种权利，你是可以重新装修房子的：**

```
fn main() {
    let mut s1 = String::from("run");// s1 是可变的
    let s2 = &mut s1;// s2 是可变的引用, 用 &mut 修饰可变的引用类型。
    s2.push_str("oob");
    println!("{}", s2);
}
```

可变引用与不可变引用相比除了权限不同以外，**可变引用不允许多重引用，但不可变引用可以**：

```
let mut s = String::from("hello");
let r1 = &mut s;
let r2 = &mut s; //这段地方报错，因为多重可变引用了 s
println!("{}, {}", r1, r2);
```

Rust 对可变引用的这种设计主要出于对**并发状态下发生数据访问碰撞**的考虑，在编译阶段就避免了这种事情的发生。由于**发生数据访问碰撞的必要条件之一是数据被至少一个使用者写且同时被至少一个其他使用者读或写，所以在一个值被可变引用时不允许再次被任何引用**。

### 垂悬引用（Dangling References）

这是一个换了个名字的概念，如果放在有指针概念的编程语言里它就指的是那种**没有实际指向一个真正能访问的数据的指针**（注意，**不一定是空指针**，还有**可能是已经释放的资源**）。它们就像失去悬挂物体的绳子，所以叫"垂悬引用"。
**"垂悬引用"在 Rust 语言里不允许出现**，如果有，编译器会发现它。下面是一个垂悬的典型案例：
```
fn main() {
    let reference_to_nothing = dangle();
}

fn dangle() -> &String {
    let s = String::from("hello");

    &s
}
```

伴随着 dangle 函数的结束，其**局部变量的值本身没有被当作返回值，被释放了**。**但它的引用却被返回****，这个引用所指向的值已经不能确定的存在**，故不允许其出现。

# Rust Slice（切片）类型

切片（Slice）是对数据值的部分引用。

### 字符串切片

最简单、最常用的数据切片类型是字符串切片（String Slice）。
```
fn main() {
  let s = String::from("broadcast");

  let part1 = &s[0..5];
  let part2 = &s[5..9];

  println!("{}={}+{}", s, part1, part2);
}
//运行结果：
//broadcast=broad+cast
```

![rust_slice](/Users/yangyibo/GitWork/JavaTest/doc/img/rust_slice.png)

上图解释了字符串切片的原理（注：Rust 中的字符串类型实质上记录了字符在内存中的起始位置和其长度，我们暂时了解到这一点）。

使用 **..** 表示范围的语法在循环章节中出现过。**x..y** 表示 **[x, y)** 的数学含义。**..** 两边可以没有运算数：

```
..y 等价于 0..y
x.. 等价于位置 x 到数据结束
.. 等价于位置 0 到结束
```

```
fn main() {
    let mut s = String::from("runoob");
    let slice = &s[0..3];
    s.push_str("yes!"); // 错误
    println!("slice = {}", slice);
}
```

**s 被部分引用，禁止更改其值**
在 Rust 中有两种常用的字符串类型：str 和 String。str 是 Rust 核心语言类型，就是本章一直在讲的字符串切片（String Slice），常常以引用的形式出现（&str）。凡是**用双引号包括的字符串常量整体的类型性质都是** **&str**：

```
let s = "hello";
```

这里的 s 就是一个 &str 类型的变量。

String 类型是 Rust 标准公共库提供的一种数据类型，它的功能更完善——它支持字符串的追加、清空等实用的操作。String 和 str 除了同样拥有一个字符**开始位置**属性和一个字符串**长度**属性以外还有一个**容量**（capacity）属性。
**String 和 str 都支持切片，切片的结果是 &str 类型的数据**。
**注意：切片结果必须是引用类型，但开发者必须自己明示这一点:**

### 非字符串切片

除了字符串以外，其他一些线性数据结构也支持切片操作，例如数组

```
fn main() {
    let arr = [1, 3, 5, 7, 9];
    let part = &arr[0..3];
    for i in part.iter() {
        println!("{}", i);
    }
}
```

# Rust 结构体

Rust 中的**结构体（Struct）与元组（Tuple）都可以将若干个类型不一定相同的数据捆绑在一起形成整体**，但结构体的**每个成员和其本身都有一个名字**，这样访问它成员的时候就不用记住下标了。**元组常用于非定义的多值传递，而结构体用于规范常用的数据结构**。结构体的每个成员叫做"**字段**"。

### 结构体定义

这是一个结构体定义：

```
struct Site {
    domain: String,
    name: String,
    nation: String,
    found: u32
}
```

注意：如果你常用 C/C++，请记住在 Rust 里 **struct 语句用来定义，不能声明实例，结尾不需要 ; 符号**，而且每个字段定义之后用 **,** 分隔。

### 结构体实例

```
let runoob = Site {
    domain: String::from("www.runoob.com"),
    name: String::from("RUNOOB"),
    nation: String::from("China"),
    found: 2013
};


//格式
结构体类名 {
    字段名 : 字段值,
    ...
}
```

**不需要按照定义的顺序来输入成员的值**。**如果正在实例化的结构体有字段名称和现存变量名称一样的，可以简化书写：**

```
let domain = String::from("www.runoob.com");
let name = String::from("RUNOOB");
let runoob = Site {
    domain,  // 等同于 domain : domain,
    name,    // 等同于 name : name,
    nation: String::from("China"),
    traffic: 2013
};
```

### 更新结构体

如果你想要新建一个结构体的实例，其中大部分属性需要被设置成与现存的一个结构体属性一样，仅需更改其中的一两个字段的值，可以使用结构体更新语法：

```
let site = Site {
    domain: String::from("www.example.com"),
    name: String::from("EXAMPLE"),
    ..runoob
};
```

注意：**..runoob** 后面**不可以有逗号**。这种语法不允许一成不变的复制另一个结构体实例，意思就是说至少重新设定一个字段的值才能**引用其他实例**的值。

### 元组结构体

有一种更简单的定义和使用结构体的方式：**元组结构体**。

元组结构体是一种**形式是元组的结构体**。

与元组的区别是它有名字和固定的类型格式。它存在的意义是为了处理那些需要定义类型（经常使用）又不想太复杂的简单数据：

```
fn main() {
  struct Color(u8, u8, u8);
  struct Point(f64, f64);
  
  //"颜色"和"点坐标"是常用的两种数据类型，但如果实例化时写个大括号再写上两个名字就为了可读性牺牲了便捷  性，Rust 不会遗留这个问题。元组结构体对象的使用方式和元组一样
  let black = Color(0, 0, 0);
  let origin = Point(0.0, 0.0);
  
   //通过 . 和下标来进行访问：
   println!("black = ({}, {}, {})", black.0, black.1, black.2);
   println!("origin = ({}, {})", origin.0, origin.1);
 }
 
 //运行结果：
 //black = (0, 0, 0)
 //origin = (0, 0)
```

## 结构体所有权

**结构体必须掌握字段值所有权，因为结构体失效的时候会释放所有字段**。这就是为什么本章的案例中使用了 String 类型而不使用 &str 的原因。
但这不意味着结构体中不定义引用型字段，这需要通过**"生命周**期"机制来实现。

### 输出结构体

调试中,Rust 提供了一个方便地输出一整个结构体的方法

```
//如第一行所示：一定要导入调试库 #[derive(Debug)] ，之后在 println 和 print 宏中就可以用 {:?} 占位符输出一整个结构体，如果属性较多的话可以使用另一个占位符 {:#?}，{:?} 一行显示，{:#?} 会分行显示 。
#[derive(Debug)]


struct Rectangle {
    width: u32,
    height: u32,
}

fn main() {
    let rect1 = Rectangle { width: 30, height: 50 };

    println!("rect1 is {:?}", rect1);
}


//输出结果
//rect1 is Rectangle { width: 30, height: 50 }

```

### 结构体方法

方法（Method）和函数（Function）类似，只不过它是用来操作结构体实例的。

Rust 语言不是面向对象，但是面向对象的珍贵思想可以在 Rust 实现。**结构体方法的第一个参数必须是 &self**，不需声明类型，因为 **self不是一种风格而是关键字**。

## 结构体关联函数

结构体关联函数，结构体方法"不叫"结构体函数"是因为"函数"这个名字留给了这种函数,它在 impl 块中却没有 &self 参数。这种函数不依赖实例，但是使用它需要声明是在哪个 impl 块中的


```
struct Rectangle {
    width: u32,
    height: u32,
}

impl Rectangle {
    //结构体方法 ，第一个参数必须是 &self，不需声明类型，因为 self 不是一种风格而是关键字
    fn area(&self) -> u32 {
        self.width * self.height
    }

    fn wider(&self, rect: &Rectangle) -> bool {
        self.width > rect.width
    }
     //结构体关联函数，它在 impl 块中却没有 &self 参数。这种函数不依赖实例，但是使用它需要声明是在哪个 impl 块中的
    fn create(width: u32, height: u32) -> Rectangle {
        Rectangle { width, height }
    }
}

fn main() {
    let rect1 = Rectangle { width: 30, height: 50 };
    let rect2 = Rectangle { width: 40, height: 20 };
    //请注意，在调用结构体方法的时候不需要填写 self ，这是出于对使用方便性的考虑。
    println!("rect1's area is {}", rect1.area());
    //多参数
    println!("{}", rect1.wider(&rect2));
      //结构体关联函数
    let rect3 = Rectangle :: create(20,30);
    println!("rect3 is {:#?}", rect3);
}
```

### 单元结构体

结构体可以值作为一种象征而无需任何成员，我们称这种没有身体的结构体为单元结构体（Unit Struct）。

```
struct UnitStruct;
```

# Rust 组织管理

对于一个工程来讲，组织代码是十分重要的。Rust 中有三和重要的组织概念：**箱、包、模块**

### 箱（Crate）

"箱"是**二进制程序文件或者库文件**，**存在于"包"中**。

**"箱"是树状结构的**，它的树根是编译器开始运行时编译的源文件所编译的程序。

注意："二进制程序文件"不一定是"二进制可执行文件"，只能确定是是包含目标机器语言的文件，文件格式随编译环境的不同而不同。

### 包（Package）

当我们使用 **Cargo 执行 new 命令创建 Rust 工程时，工程目录下会建立一个 Cargo.toml 文件。工程的实质就是一个包，包必须由一个 Cargo.toml 文件来管理**，该文件描述了包的基本信息以及依赖项。

**一个包最多包含一个库"箱"，可以包含任意数量的二进制"箱"，但是至少包含一个"箱"（不管是库还是二进制"箱"）。**

当使用 cargo new 命令创建完包之后**，src 目录下会生成一个 main.rs 源文件**，Cargo 默认这个文件为**二进制箱的根**，编译之后的**二进制箱将与包名相同**。

### 模块（Module）

对于一个软件工程来说，我们往往按照所使用的编程语言的组织规范来进行组织，组织模块的主要结构往往是树。Java 组织功能模块的主要单位是类，而 JavaScript 组织模块的主要方式是 function。

这些先进的语言的组织单位可以层层包含，就像文件系统的目录结构一样。**Rust 中的组织单位是模块（Module）**

```
mod nation {
   pub mod government {
       pub fn govern() {}
    }
   pub mod congress {
       pub fn legislate() {}
    }
   pub mod court {
       pub fn judicial() {}
    }
}
```

这是一段描述法治国家的程序：国家（nation）包括政府（government）、议会（congress）和法院（court），分别有行政、立法和司法的功能。我们可以把它转换成树状结构：

```
nation
 ├── government
 │ └── govern
 ├── congress
 │ └── legislate
 └── court
   └── judicial
```

在文件系统中，目录结构往往以斜杠在路径字符串中表示对象的位置，**Rust 中的路径分隔符是 ::** 。

路径分为绝对路径和相对路径。**绝对路径从 crate 关键字开始描述。相对路径从 self 或 super 关键字或一个标识符开始描述**。例如：

```
//是描述 govern 函数的绝对路径，
crate::nation::government::govern();
//相对路径可以表示为：
nation::government::govern();
```

## 访问权限

Rust 中有两种简单的访问权：**公共（public）和私有（private）**。默认情况下，如果不加修饰符，模块中的成员访问权将是私有的。如果想使用公共权限，需要使用 pub 关键字。**对于私有的模块，只有在与其平级的位置或下级的位置才能访问，不能从其外部访问**。

如果模块中定义了结构体，结构体除了其本身是私有的以外，其字段也默认是私有的。所以如果想使用模块中的结构体以及其字段，需要 pub 声明：

```
mod back_of_house {
    pub struct Breakfast {
        pub toast: String,
        seasonal_fruit: String,
    }

    impl Breakfast {
        pub fn summer(toast: &str) -> Breakfast {
            Breakfast {
                toast: String::from(toast),
                seasonal_fruit: String::from("peaches"),
            }
        }
    }
}
pub fn eat_at_restaurant() {
    let mut meal = back_of_house::Breakfast::summer("Rye");
    meal.toast = String::from("Wheat");
    println!("I'd like {} toast please", meal.toast);
}
fn main() {
    eat_at_restaurant()
}
```

枚举类枚举项可以内含字段，但不具备类似权限的性质

## 难以发现的模块

在 Rust 中，模块就像是 Java 中的类包装，但是文件一开头就可以写一个主函数，这该如何解释呢？每一个 Rust 文件的内容都是一个"难以发现"的模块。

让我们用两个文件来揭示这一点：

```
// main.rs
mod second_module;

fn main() {
    println!("This is the main module.");
    println!("{}", second_module::message());
}
```

```
// second_module.rs
pub fn message() -> String {
    String::from("This is the 2nd module.")
}
```

## use 关键字

use 关键字能够将模块标识符引入当前作用域：

```
// second_module.rs

pub fn message() -> String {
    String::from("This is the 2nd module.")
}

pub mod nation {
    pub mod government {
        pub fn govern() {
            println!("wo shi yi ge yin yong")
        }
    }
}
```

```
// main.rs
use second_module::nation::government::govern;
fn main() {
       govern();
}
//执行结果
//wo shi yi ge yin yong
```

# 异常

## kind 方法

到此为止，Rust 似乎没有像 try 块一样可以令任何位置发生的同类异常都直接得到相同的解决的语法，但这样并不意味着 Rust 实现不了：我们完全可以把 try 块在独立的函数中实现，将所有的异常都传递出去解决。实际上这才是一个分化良好的程序应当遵循的编程方法：应该注重独立功能的完整性。

# 范型&特性

## 特性

特性（trait）概念接近于 Java 中的接口（Interface），但两者不完全相同。特性与接口相同的地方在于它们都是一种行为规范，可以用于标识哪些类有哪些方法。特性在 Rust 中用 trait 表示：

```
trait Descriptive {
    fn describe(&self) -> String;
}
```

Descriptive 规定了实现者必须有是 **describe(&self) -> String** 方法。

格式是： impl <特性名> for <所实现的类型名>  我们用它实现一个结构体：

```
struct Person {
    name: String,
    age: u8
}

impl Descriptive for Person {
    fn describe(&self) -> String {
        format!("{} {}", self.name, self.age)
    }
}
```

### 默认特性

这是特性与接口的不同点：接口只能规范方法而不能定义方法，但特性可以定义方法作为默认方法，因为是"默认"，所以对象既可以重新定义方法，也可以不重新定义方法使用默认的方法：



### 特性做参数

很多情况下我们需要传递一个函数做参数，例如回调函数、设置按钮事件等, 在 Java 中函数必须以接口实现的类实例来传递. 

任何实现了 Descriptive 特性的对象都可以作为这个函数的参数，这个函数没必要了解传入对象有没有其他属性或方法，只需要了解它一定有 Descriptive 特性规范的方法就可以了。当然，此函数内也无法使用其他的属性与方法。

```
fn output(object: impl Descriptive) {
    println!("{}", object.describe());
}
//或用这种等效语法实现
fn output<T: Descriptive>(object: T) {
    println!("{}", object.describe());
}
```

特性作类型表示时如果涉及多个特性，可以用 **+** 符号表示，例如

```
fn some_function<T: Display + Clone, U: Clone + Debug>(t: T, u: U)
//可以简化成：
fn some_function<T, U>(t: T, u: U) -> i32
    where T: Display + Clone,
          U: Clone + Debug


```

### 特性做返回值

**特性做返回值只接受实现了该特性的对象做返回值**，且在同一个函数中**所有可能的返回值类型必须完全一样**。

```
fn person() -> impl Descriptive {
    Person {
        name: String::from("Cali"),
        age: 24
    }
}
```

### 有条件实现方法

impl 功能十分强大，我们可以用它实现类的方法。但对于泛型类来说，有时我们需要区分一下它所属的泛型已经实现的方法来决定它接下来该实现的方法：

```
//这段代码声明了 A<T> 类型必须在 T 已经实现 B 和 C 特性的前提下才能有效实现此 impl 块。

struct A<T> {}

impl<T: B + C> A<T> {
    fn d(&self) {}
}
```

# Rust 生命周期

**Rust 生命周期机制是与所有权机制同等重要的资源管理机制**。之所以引入这个概念主要是应对复杂类型系统中资源管理的问题。**引用是对待复杂类型时必不可少的机制**，毕竟复杂类型的数据不能被处理器轻易地复制和计算。但引用往往导致极其复杂的资源管理问题

### 生命周期注释

生命周期注释是描述引用生命周期的办法。虽然不能够改变引用的生命周期，但可以在合适的地方生命两个引用的生命周期一致。

生命收起注释用单引号开头，跟着一个小写字母单词：

```
&i32        // 常规引用
&'a i32     // 含有生命周期注释的引用
&'a mut i32 // 可变型含有生命周期注释的引用
```

### 静态生命周期

生命周期注释有一个特别的：'static 。所有用双引号包括的字符串常量所代表的精确数据类型都是 &'static str ，'static 所表示的生命周期从程序运行开始到程序运行结束。

### 泛型、特性与生命周期协同作战

```
use std::fmt::Display;

fn longest_with_an_announcement<'a, T>(x: &'a str, y: &'a str, ann: T) -> &'a str
    where T: Display
{
    println!("Announcement! {}", ann);
    if x.len() > y.len() {
        x
    } else {
        y
    }
}
```

# Rust 面向对象

面向对象的编程语言通常实现了数据的封装与继承并能基于数据调用方法。
Rust 不是面向对象的编程语言，但这些功能都得以实现。

### 封装

**封装就是对外显示的策略**，在 Rust 中可以通过**模块的机制来实现最外层的封装**，并且每一个 Rust 文件都可以看作一个模块，模块内的元素可以通过 **pub 关键字**对外明示。这一点在"组织管理"章节详细叙述过。

**"类"往往是面向对象的编程语言中常用到的概念**。**"类"封装的是数据**，是对同一类数据实体以及其处理方法的抽象。在 Rust 中，**我们可以使用结构体或枚举类来实现类的功能**：

```
// second.rs 类
pub struct ClassName {
    field: i32,
}

impl ClassName {
    pub fn new(value: i32) -> ClassName {
        ClassName {
            field: value
        }
    }

    pub fn public_method(&self) {
        println!("from public method");
        self.private_method();
    }

    fn private_method(&self) {
        println!("from private method");
    }
}
//main.rs文件中调用使用 
mod second;
use second::ClassName;

fn main() {
    let object = ClassName::new(1024);
    object.public_method();
}
```

### 继承

几乎其他的面向对象的编程语言都可以实现"继承"，并用"extend"词语来描述这个动作。

**继承是多态（Polymorphism）思想的实现，多态指的是编程语言可以处理多种类型数据的代码。**在 Rust 中，**通过特性（trait）实现多态**。有关特性的细节已在"特性"章节给出。但是特性无法实现属性的继承，只能实现类似于"接口"的功能，所以想继承一个类的方法最好在"子类"中定义"父类"的实例。

总结地说，Rust 没有提供跟继承有关的语法糖，也没有官方的继承手段（完全等同于 Java 中的类的继承），**但灵活的语法依然可以实现相关的功能。**

# Rust 并发编程

安全高效的处理并发是 Rust 诞生的目的之一，主要解决的是服务器高负载承受能力。
* **并发（concurrent）的概念是指程序不同的部分独立执行，**
* **这与并行（parallel）的概念容易混淆，并行强调的是"同时执行"。**

并发往往会造成并行。

### 线程
**线程（thread）是一个程序中独立运行的一个部分**。
**线程不同于进程（process）的地方是线程是程序以内的概念**，**程序往往是在一个进程中执行的**。
在有操作系统的环境中**进程往往被交替地调度得以执行**，**线程则在进程以内由程序进行调度**。

由于线程**并发很有可能出现并行**的情况，所以在**并行中可能遇到的死锁**、**延宕错误**常出现于含有并发机制的程序。
为了解决这些问题，很多其它语言（如 Java、C#）采用特殊的**运行时（runtime）**软件来协调资源，但这样无疑极大地**降低了程序的执行效率**。

C/C++ 语言在操作系统的最底层也支持多线程，且语言本身以及其编译器不具备侦察和避免并行错误的能力，这对于开发者来说压力很大，开发者需要花费大量的精力避免发生错误。

**Rust 不依靠运行时环境**，这一点像 C/C++ 一样。**但 Rust 在语言本身就设计了包括所有权机制在内的手段来尽可能地把最常见的错误消灭在编译阶段**，**这一点其他语言不具备**。
但这不意味着我们编程的时候可以不小心，迄今为止由于并发造成的问题还没有在公共范围内得到完全解决，仍有可能出现错误，并发编程时要尽量小心！

Rust 中通过 **std::thread::spawn 函数创建新线程**：

```
use std::thread;
use std::time::Duration;

fn spawn_function() {
    for i in 0..5 {
        println!("spawned thread print {}", i);
        thread::sleep(Duration::from_millis(1));
    }
}

pub fn thread_test() {
    //std::thread::spawn 函数的参数是一个无参函数，不是推荐的写法
    //thread::spawn(spawn_function);

    //使用闭包（closures）来传递函数作为参数
    thread::spawn(|| {
        for i in 0..5 {
            println!("spawned thread print {}", i);
            thread::sleep(Duration::from_millis(1));
        }
    });

    for i in 0..3 {
        println!("main thread print {}", i);
        thread::sleep(Duration::from_millis(1));
    }
}

//执行结果
main thread print 0
spawned thread print 0
main thread print 1
spawned thread print 1
main thread print 2
spawned thread print 2
spawned thread print 3

```

此程序有一个子线程，目的是打印 5 行文字，主线程打印三行文字，但很显然**随着主线程的结束，spawn 线程也随之结束了，并没有完成所有打印**。

闭包是可以保存进变量或作为参数传递给其他函数的匿名函数。闭包相当于 Rust 中的 Lambda 表达式，

```
|参数1, 参数2, ...| -> 返回值类型 {
    // 函数体
}
//示例
pub fn closures_test() {
   // let inc = |num: i32| -> i32 {
    //闭包可以省略类型声明使用 Rust 自动类型判断机制：
    let inc = |num: i32| -> i32 {

    num + 1
    };
    println!("inc(5) = {}", inc(5));
}
```

### join 方法

join 方法可以使子线程运行结束后再停止运行程序。

```
use std::thread;
use std::time::Duration;

fn main() {
    let handle = thread::spawn(|| {
        for i in 0..5 {
            println!("spawned thread print {}", i);
            thread::sleep(Duration::from_millis(1));
        }
    });

    for i in 0..3 {
        println!("main thread print {}", i);
        thread::sleep(Duration::from_millis(1));
    }

    handle.join().unwrap();
}
```

### move 强制所有权迁移

在子线程中尝试使用当前函数的资源，这一定是错误的！因为所有权机制禁止这种危险情况的产生，它将破坏所有权机制销毁资源的一定性。我们可以使用闭包的 move 关键字来处理：

```
use std::thread;

fn main() {
    let s = "hello";
   
    let handle = thread::spawn(move || {
        println!("{}", s);
    });

    handle.join().unwrap();
}
```

### 消息传递 

Rust 中一个实现消息传递并发的主要工具是通道（channel），通道有两部分组成，一个发送者（transmitter）和一个接收者（receiver）。

std::sync::mpsc 包含了消息传递的方法：

```
use std::thread;
use std::sync::mpsc;
//子线程获得了主线程的发送者 tx，并调用了它的 send 方法发送了一个字符串，然后主线程就通过对应的接收者 //rx 接收到了。
fn main() {
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        let val = String::from("hi");
        tx.send(val).unwrap();
    });

    let received = rx.recv().unwrap();
    println!("Got: {}", received);
}
//运行结果
Got: hi
```

