package com.us.rule.mvel;

import com.us.rule.BaseExprEngine;

import java.io.Serializable;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * 脚本语言性能测试结果
 * https://www.iteye.com/topic/361794
 * https://www.iteye.com/blog/caoyaojun1988-163-com-2089726
 *
 * @author yyb
 * @time 2020/8/12
 */
public class MvelEngine extends BaseExprEngine {


    @Override
    protected Object execInner(String expr, Map<String, Object> data) {
        try {
            Object result = MVEL.eval(expr, data);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            System.out.println("表达式执行错误");
            return false;
        }
        return null;

    }

    @Override
    public Serializable compileExpr(String expr) {
        try {
            Serializable compiled = MVEL.compileExpression(expr);
            return compiled;
        } catch (Exception e) {
            System.out.println("表达式编译错误：" + expr);
            return null;
        }
    }

    @Override
    public boolean execCompiled(Object expr, Map<String, Object> data) {
        try {
            Object result = MVEL.executeExpression(expr, data);
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("表达式执行错误");
            return false;
        }
    }

    @Override
    public Object execReturnCompiled(Object expr, Map<String, Object> data) {
        try {
            Object result = MVEL.executeExpression(expr, data);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            System.out.println("表达式执行错误");
            return false;
        }
        return null;
    }

}