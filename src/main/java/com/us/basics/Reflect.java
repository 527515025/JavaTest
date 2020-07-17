package com.us.basics;

import com.us.bean.Person;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyibo on 17/6/12.
 */
public class Reflect {


    public static String getString(Object o, Class<?> c) {
        String result = c.getSimpleName() + ":";

        // 获取父类，判断是否为实体类
        if (c.getSuperclass().getName().indexOf("entity") >= 0) {
            result += "\n<" + getString(o, c.getSuperclass()) + ">,\n";
        }

        // 获取类中的所有定义字段
        Field[] fields = c.getDeclaredFields();

        // 循环遍历字段，获取字段对应的属性值
        for (Field field : fields) {
            // 如果不为空，设置可见性，然后返回
            field.setAccessible(true);

            try {
                // 设置字段可见，即可用get方法获取属性值。
                result += field.getName() + "=" + field.get(o) + ",\n";
            } catch (Exception e) {
                // System.out.println("error--------"+methodName+".Reason is:"+e.getMessage());
            }
        }
        if (result.indexOf(",") >= 0) result = result.substring(0, result.length() - 2);
        return result;
    }


    public static String getClassAttribute(List<?> o, Class<?> c, String field) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNoneBlank(field)) {
            Field[] fields = c.getDeclaredFields();
            int pos;
            for (pos = 0; pos < fields.length; pos++) {
                if (field.equals(fields[pos].getName())) {
                    break;
                }
            }
            for (Object o1 : o) {
                try {
                    fields[pos].setAccessible(true);
                    result.append(fields[pos].get(o1) + ",");
                } catch (Exception e) {
                    System.out.println("error--------" + ".Reason is:" + e.getMessage());
                }
            }
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }

    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        person.setName("abel");
        person.setAge(16);

        Person person2 = new Person();
        person2.setName("abel2");
        person2.setAge(17);

        personList.add(person);
        personList.add(person2);

        System.out.println(getClassAttribute(personList, Person.class, "name"));
    }
}
