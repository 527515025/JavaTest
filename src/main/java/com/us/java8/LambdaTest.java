package com.us.java8;

import com.us.Person;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by yangyibo on 16/12/26.
 */
public class LambdaTest {


    public static void main(String[] args) {
        map();
//        forEachT();
//        threadT();
//        sortT();
//        streamsFilterT();
//        streamsCountT();
//        personTComparator();
//        boysAndGirls();
//        stringToIntT();

    }


    public static void map() {
        Map<String,String> result =new HashMap<>();
        result.put("name","abel");
        result.put("address","shanghai");
        result.put("ager","23");
        System.out.println(result.entrySet().stream().map(e->e.getKey()+"=\""+e.getValue()+"\"").collect(Collectors.joining(";")));
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
        List<Person> persons = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Person person = new Person();
            person.setName("abel-" + i);
            person.setAge(20 + i);
            persons.add(person);
        }
        Comparator<Person> byName = Comparator.comparing(Person::getName);
        persons.sort(byName);
        persons.forEach(p -> System.out.println(p.getName()));
    }







    //  使用 Collectors.groupingBy 转换为map   使用Predicate 进行过滤操作。
    public static void boysAndGirls() {
        List<Person> persons = new ArrayList<>();

        for (int i = 1; i <= 40; i++) {
            Person person = new Person();
            person.setName("abel-" + i);
            person.setSex((int) (Math.random() * 2));
            person.setAge(25 + i);
            persons.add(person);
        }
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
        List<String> list = Arrays.asList("1", "3","3", "4", "5", "5", "6", "7","7", "8", "9", "10", "10", "10", "11", "12", "13", "14", "15");
        Comparator<Integer> sort = (Integer e1, Integer e2) -> e2 - e1;
        List<Integer> listInt = list.stream()
                .distinct()
                .map(x -> new Integer(x))
                .filter(x -> x > 4 && x < 11)
                .sorted(sort)
                .collect(Collectors.toList());
        listInt.forEach(e -> System.out.print(e+" , "));

    }




}
