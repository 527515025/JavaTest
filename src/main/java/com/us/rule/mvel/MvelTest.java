package com.us.rule.mvel;

import com.us.rule.BaseExprEngine;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yyb
 * @time 2020/8/12
 */
public class MvelTest {

    public static void main(String[] args) {
//        ageTest();
//        containsTest();
//        startsWithTest();
//        emptyTest();
//        returnTest();
//        multipleTest();
        execExprTest();


    }


    /**
     * 布尔表达式：
     * 大于小于 等于 以及 优先级
     */
    public static void ageTest() {
        String expr = "(age > 10 && age <= 20) || age == 11 ";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("age", 12);
        if (engine.execCompiled(obj, params)) {
            //满足表达式
            System.out.println("少年");
        } else {
            System.out.println("不满足条件");
        }
    }


    /**
     * 判断包含
     */
    public static void containsTest() {
        String expr = "name contains(\"张\")";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("name", "张三");
        params.put("age", 11);
        if (engine.execCompiled(obj, params)) {
            //满足表达式
            System.out.println("包含张");
        } else {
            System.out.println("不满足条件");
        }
    }

    /**
     * 判断以xx开头
     */
    public static void startsWithTest() {
        String expr = "mobile.startsWith(136)";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("mobile", "13643489802");
        params.put("age", 11);
        if (engine.execCompiled(obj, params)) {
            //满足表达式
            System.out.println("包含136开头手机号");
        } else {
            System.out.println("不满足条件");
        }
    }

    /**
     * 判空，多个表达式时，只有最后一个表达式的结果为返回
     */
    public static void emptyTest() {
        String expr = "foo == null ; foo1 == nil; foo2 == empty";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("foo", 1);
        params.put("foo1", 1);
        params.put("foo2", " ");
        if (engine.execCompiled(obj, params)) {
            //满足表达式
            System.out.println("true");
        } else {
            System.out.println("不满足条件");
        }
    }


    /**
     * 多个表达式时，只有最后一个表达式的结果为返回
     * 您可以编写具有任意数量语句的脚本，使用分号表示语句的终止。这在所有情况下都是必需的，除非只有一条语句，或者脚本中的最后一条语句。
     */
    public static void multipleTest() {
        String expr = "age > 20; sex == 1; name contains(\"张2\")";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("age", 10);
        params.put("sex", 1);
        params.put("name", "张三");
        if (engine.execCompiled(obj, params)) {
            //满足表达式
            System.out.println("true");
        } else {
            System.out.println("不满足条件");
        }
    }

    /**
     * 有返回值的表达式
     */
    public static void returnTest() {
        String expr = "a = 10;b = (a = a * 2) + 10; a;";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Object obj = engine.compileExpr(expr);
        Map<String, Object> params = new HashedMap();
        params.put("a", 1);
        Object r = engine.execReturnCompiled(obj, params);
        if (null != r) {
            //满足表达式
            System.out.println(r);
        } else {
            System.out.println("不满足条件");
        }
    }

    /**
     * 不用编辑即可执行
     */
    public static void execExprTest() {
        String expr = "x * y";
        BaseExprEngine engine = BaseExprEngine.defaultEngine();
        Map vars = new HashMap();
        vars.put("x", new Integer(5));
        vars.put("y", new Integer(10));
        Object r = engine.exec(expr, vars);
        if (r != null) {
            //满足表达式
            System.out.println(r);
        } else {
            System.out.println("不满足条件");
        }
    }

}
