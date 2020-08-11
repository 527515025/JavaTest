package com.us.java8;


import com.us.bean.Person;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangyibo on 16/12/26.
 */
public class LambdaTest {


    public static void main(String[] args) {
//        groupBy();
//        map();
//        forEachT();
//        threadT();
//        sortT();
//        streamsFilterT();
//        streamsCountT();
//        personTComparator();
//        boysAndGirls();
//        stringToIntT();
//        supplierTest();
//        consumerTest();
//        predicateTest();
        FunctionTest();

    }


    public static void map() {
        Map<String, String> result = new HashMap<>();
        result.put("name", "abel");
        result.put("address", "shanghai");
        result.put("ager", "23");
        System.out.println(result.entrySet().stream().map(e -> e.getKey() + "=\"" + e.getValue() + "\"").collect(Collectors.joining(";")));
    }


    //Lambda 的for 循环
    private static void forEachT() {
        List<String> str = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            str.add(i + "");
        }
        str.forEach((String) -> System.out.println(String + ";"));

        str.forEach((String) -> {
            System.out.print(String + ";");
            System.out.println(String + "附加的操作");
        });
        str.clear();
    }


    //匿名类 线程操作
    private static void threadT() {

        System.out.println("current--" + Thread.currentThread().getId());

        //使用 lambda expression  开线程
        new Thread(() -> System.out.println("Hello world Thread !---" + Thread.currentThread().getId())).start();

        //使用匿名内部类   没有开线程
        Runnable race2 = () -> System.out.println("Hello world  Runnable !--" + Thread.currentThread().getId());
        race2.run();
    }


    //Comparator 排序
    private static void sortT() {
        //方式一，通过首字母排序
        List<String> str2 = Arrays.asList("abel", "don", "bruce", "sean");
        str2.sort((e1, e2) -> e1.compareTo(e2));
        str2.forEach((e) -> System.out.println(e));


        System.out.println("--------------分割线--------------------");
        //通过长度排序
        List<String> str3 = Arrays.asList("abel", "don", "bruce", "sean");
        //长度从小到大排序
        str3.sort((e1, e2) -> e1.length() - e2.length());
        str3.forEach((e) -> System.out.println(e));


        System.out.println("--------------分割线--------------------");
        //通过长度排序
        List<String> str4 = Arrays.asList("abel", "don", "bruce", "sean");
        Comparator<String> sortByName = (String e1, String e2) -> e1.length() - e2.length();
        str4.sort(sortByName); //长度从小到大排序
        str4.forEach((e) -> System.out.println(e));
    }


    //过滤
    private static void streamsFilterT() {
        List<String> str3 = Arrays.asList("abel", "don", "bruce", "sean");
        //输出长度大于3的
        str3.stream().filter((String e1) -> (e1.length() > 3)).forEach((e) -> System.out.println(e));

        System.out.println("--------------分割线------------输出包涵 a 字母--------");

        //输出包涵 a 字母
        List<String> str2 = str3.stream().filter(x -> x.contains("a")).collect(Collectors.toList());
        str2.forEach(e -> System.out.println(e));

        System.out.println("--------------分割线------------按首字母排序，输出前三个--------");
        //按首字母排序，输出前三个
        str3.stream().sorted((e1, e2) -> e1.compareTo(e2)).limit(3).forEach((e) -> System.out.println(e));

        System.out.println("--------------分割线--------------------");

        //输出长度最长的
        String max = str3.stream().max((e1, e2) -> (e1.length() - e2.length())).get();
        System.out.println("输出长度最长的: " + max);

        System.out.println("--------------分割线--------------------");

        //输出长度最短的
        String min = str3.stream().min((e1, e2) -> (e1.length() - e2.length())).get();
        System.out.println("输出长度最短的: " + min);
    }


    // reduce 聚合操作   使用summaryStatistics方法获得stream 中元素的各种汇总数据
    private static void streamsCountT() {
        List<String> str3 = Arrays.asList("abel", "don", "bruce", "sean");

        //并行计算
        int sum = str3.stream().parallel().mapToInt(p -> p.length()).sum();
        System.out.println(sum);

        System.out.println("--------------分割线-----------reduce---------");

        //reduce 求和
        int sum2 = str3.stream().parallel().mapToInt(p -> p.length()).reduce(0, (x, y) -> (x + y));
        System.out.println("reduce 求和" + sum2);


        System.out.println("--------------分割线--------------------");

        //使用summaryStatistics方法获得stream 中元素的各种汇总数据
        IntSummaryStatistics sumstatis = str3.stream().mapToInt(p -> p.length()).summaryStatistics();
        System.out.println("求长度平均数" + sumstatis.getAverage()
                + "\n长度最小的" + sumstatis.getMin()
                + "\n长度最大的" + sumstatis.getMax()
                + "\n求总和" + sumstatis.getSum());
    }


    //使用 Comparator 排序
    private static void personTComparator() {
        //将person 对象按照名字首字母顺序排列
        List<Person> persons = getPersionList();
        Comparator<Person> byName = Comparator.comparing(Person::getName);
        persons.sort(byName);
        persons.forEach(p -> System.out.println(p.getName()));
    }


    //  使用 Collectors.groupingBy 转换为map   使用Predicate 进行过滤操作。
    public static void boysAndGirls() {
        List<Person> persons = getPersionList();
        persons.forEach(p -> System.out.println(p.toString()));

        // 统计年龄在25-65岁,且名字中包涵 "-1"或者"-2" 的男女人数、比例   0:为女性,1:为男性
        Map<Integer, Integer> result = persons.parallelStream().filter(p -> p.getAge() <= 65 && p.getAge() >= 25
                && (p.getName().contains("-1") || p.getName().contains("-2"))).
                collect(
                        //把结果收集到一个Map中，用统计到的男女自身作为键，其出现次数作为值。
                        Collectors.groupingBy(p -> p.getSex(), Collectors.summingInt(p -> 1))
                );
        System.out.println("\n" + "boysAndGirls result is " + result);


        System.out.println("--------------分割线--------------------");


        // 统计年龄在25-65岁,且名字中以"a" 开头的男女人数、比例   0:为女性,1:为男性
        Predicate<Person> startsWithA = (n) -> n.getName().startsWith("a");
        Predicate<Person> ageMax = (n) -> n.getAge() <= 65;
        Predicate<Person> ageMin = (n) -> n.getAge() >= 25;
        Map<Integer, Integer> result2 = persons.parallelStream().filter(startsWithA.and(ageMax).and(ageMin)).
                collect(
                        //把结果收集到一个Map中，用统计到的男女自身作为键，其出现次数作为值。
                        Collectors.groupingBy(p -> p.getSex(), Collectors.summingInt(p -> 1))
                );
        System.out.println("boysAndGirls Predicate  result2 is " + result2);
    }


    //使用map 转换对象。使用Collectors 返回集合 使用Comparator排序
    private static void stringToIntT() {
        //将String 集合 转换为 int 取得大于4,小于11的元素,并按从大到小排序,转换为 integer 集合。
        List<String> list = Arrays.asList("1", "3", "3", "4", "5", "5", "6", "7", "7", "8", "9", "10", "10", "10", "11", "12", "13", "14", "15");
        Comparator<Integer> sort = (Integer e1, Integer e2) -> e2 - e1;
        List<Integer> listInt = list.stream()
                .distinct()
                .map(x -> new Integer(x))
                .filter(x -> x > 4 && x < 11)
                .sorted(sort)
                .collect(Collectors.toList());
        listInt.forEach(e -> System.out.print(e + " , "));

    }

    private static List<Person> getPersionList() {
        List<Person> persons = new ArrayList<>();

        for (int i = 1; i <= 40; i++) {
            Random r = new Random();
            Person person = new Person();
            person.setName("abel-" + i);
            person.setSex((int) (Math.random() * 2));
            person.setGroup(String.valueOf(i % 2));
            person.setAge(25 + r.nextInt(50));
            persons.add(person);
        }
        return persons;

    }


    /**
     * 分组
     */
    private static void groupBy() {
        List<Person> persons = getPersionList();
        Map<String, List<Person>> sumCase = persons.stream().collect(Collectors.groupingBy(Person::getGroup));
        //按照年龄升序排列
        List<Person> personsSort = persons.stream().sorted(Comparator.comparingInt(Person::getAge)).collect(Collectors.toList());
        //按照排序后的数据进行分组， 并返回排序后分组的结果
        LinkedHashMap<Integer, List<Person>> age = personsSort.stream().collect(Collectors.groupingBy(Person::getAge, LinkedHashMap::new, Collectors.toList()));


        //将list 排序，并按照排序后的结果进行有序分组
        LinkedHashMap<Integer, List<Person>> ageMap = personsSort.stream().sorted(Comparator.comparingInt(Person::getAge)).collect(Collectors.groupingBy(Person::getAge, LinkedHashMap::new, Collectors.toList()));


        System.out.println(persons.size());
    }


    /*********************************************函数式接口********************************************************************************/


    /**
     * 使用场景：提前定义可能返回的一个指定类型结果，等需要调用的时候再获取结果。
     *
     * @param supplier
     * @return
     */
    public static String getSupplierValue(Supplier<String> supplier) {
        return supplier.get();
    }

    /**
     * 函数，懒加载
     */
    public static void supplierTest() {
        // 示例1
        int num1 = 100;
        int num2 = 200;
        // 提前定义好需要返回的指定类型结果，但不运行
        Supplier<Integer> supplier = () -> num1 + num2;
        //此示例中返回的结果引用的对象num1和num2其实是不能更改的，如果我们在supplier定义后，suppliser.get()调用前将num1或num更改了，则编译会报错！
        // num1 =101;
        // 调取get()方法获取一个结果
        System.out.println(supplier.get());

        // 示例2
        String str = "abcdefghijklmn";
        String s = getSupplierValue(() -> str.substring(1, 5));
        System.out.println(s);
    }


    /**
     * 函数无返回
     */
    public static void consumerTest() {
        // 传入一个加法并打印结果 30
        modify(10, x -> System.out.println(x + 20));

        // 传入一个减法并打印结果 -10
        modify(10, x -> System.out.println(x - 20));


        /**
         * 定义一个消费方法，将李四筛选出来存入 li
         */
        List<Person> li = new ArrayList<>();

        // 定义一个消费方法，li
        Consumer<Person> consumer = x -> {
            if (x.getName().equals("李四")) {
                li.add(x);
            }
        };

        List<Person> list = new ArrayList<>();
        list.add(new Person(21, "张三"));
        list.add(new Person(22, "李四"));
        list.add(new Person(23, "赵六"));
        list.add(new Person(30, "王五"));
        list.add(new Person(52, "李四"));

        // 传入一个消费方法
        list.forEach(consumer);

        // 打印消费方法处理后的li
        System.out.println(li);

    }

    /**
     * 使用场景：处理一些结果或数据，不需要返回的消费型，例如打印、发送通知等操作。
     * 定义一个方法，第二个参数为一个Consumer(对参数进行操作的函数)
     *
     * @param num
     * @param consumer
     */
    public static void modify(int num, Consumer<Integer> consumer) {
        // 执行accept()方法，方法的具体实现不关心，调用的时候才关心
        consumer.accept(num);
    }


    /**
     * 使用场景：对一个数据进行判断，并返回boolean
     * boolean test(T t) 判断指定值是否符合条件
     * Predicate<T> and(Predicate<? super T> other) 与操作
     * Predicate<T> or(Predicate<? super T> other) 或操作
     * static <T> Predicate<T> isEqual(Object targetRef) 静态方法，equals判断第一个test与第二个test方法相同
     */
    public static void predicateTest() {

        //示例1 设置断言x == 10
        Predicate<Integer> predicate = (x) -> x == 10;
        //传入x = 10
        System.out.println("x = 10 ?" + predicate.test(10));
        //断言逻辑取反
        predicate = predicate.negate();
        System.out.println("x != 10 ?" + predicate.test(10));


        //示例2 将list集合里面小于20的数据移除
        List<Integer> list = new ArrayList<>();
        list.add(9);
        list.add(12);
        list.add(21);
        list.add(60);
        // 使用lambda表达式Predicate，判断list里数是否满足条件，并删除,
        // 查看list.removeIf()方法源码，我们发现他实现的方式就是遍历集合并对每个集合元素调用Predicate.test()方法，验证结果并移除元素。
        list.removeIf(x -> x < 20);
        System.out.println(list);

        // 示例3 统计集合中相等的对象的个数
        Person p = new Person(22, "李四");
        // 使用isEqual生成一个断言
        Predicate<Person> predicate3 = Predicate.isEqual(p);
        Long count = Stream.of(
                new Person(21, "张三"),
                new Person(22, "李四"),
                new Person(23, "王五"),
                new Person(24, "王五"),
                new Person(22, "李四"),
                new Person(26, "张三")
        ).filter(predicate3).count();
        System.out.println(count);
    }


    /**
     *  函数有返回
     */
    public static void FunctionTest() {
        //示例1：定义一个 funciton ,实现将String转换为Integer
        Integer a = FunctionTest("100", x -> Integer.parseInt(x));
        // 结果：class java.lang.Integer
        System.out.println(a.getClass());

        //示例2：使用andThen() 实现一个函数 y=10x + 10; 结果30
        Function<Integer, Integer> function2 = x -> 10 * x;
        // andThen 返回首先将此函数应用于的组合函数它的输入
        function2 = function2.andThen(x -> x + 10);
        System.out.println(function2.apply(2));


        //示例3：使用compose() 实现一个函数 y=(10+x)2; 结果 26
        Function<Integer, Integer> function3 = x -> x * 2;
        // compose 将此函数应用于给定参数。
        function3 = function3.compose(x -> x + 10);
        System.out.println(function3.apply(3));


        //示例5：使用使用compose()、andThen()实现一个函数 y=(10+x)2+10; 结果 36
        Function<Integer, Integer> function4 = x -> x * 2;
        function4 = function4.compose(x -> x + 10);
        function4 = function4.andThen(x -> x + 10);
        System.out.println(function4.apply(3));

    }

    /**
     * 使用场景：根据一个数据类型得到另一个数据类型。
     * 方法：
     * <p>
     * R apply(T t); 根据一个数据类型T加工得到一个数据类型R
     * <V> Function<V, R> compose(Function<? super V, ? extends T> before) 组合函数，调用当前function之前调用
     * <V> Function<T, V> andThen(Function<? super R, ? extends V> after) 组合函数，调用当前function之后调用
     * static <T> Function<T, T> identity() 静态方法，返回与原函数参数一致的结果。x=y;
     */
    public static Integer FunctionTest(String a, Function<String, Integer> function) {
        return function.apply(a);
    }
}

