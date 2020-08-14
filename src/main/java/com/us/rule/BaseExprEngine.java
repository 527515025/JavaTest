package com.us.rule;

import com.alibaba.fastjson.JSON;
import com.us.rule.mvel.MvelEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author yyb
 * @time 2020/8/12
 */
public abstract class BaseExprEngine {
    private static final Logger logger = LoggerFactory.getLogger(BaseExprEngine.class);

    /**
     * 用参数验证表达式。
     *
     * @param rawExpr
     * @param data
     * @return
     * @throws
     */
    public Object exec(String rawExpr, Map<String, Object> data) {
        try {
            if (StringUtils.isEmpty(rawExpr)) {
                return false;
            }
            return execInner(rawExpr, data);
        } catch (Exception e) {
            logger.error("表达式解析错误 expr:{} data:{} ex:{}", rawExpr, JSON.toJSONString(data),
                    ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 编译表达式
     *
     * @param expr
     * @return
     */
    public abstract Object compileExpr(String expr);

    /**
     * 执行编译后的表达式
     *
     * @param expr
     * @param data
     * @return
     */
    public abstract boolean execCompiled(Object expr, Map<String, Object> data);


    /**
     * 执行表达式
     *
     * @param expr
     * @param data
     * @return
     */
    protected abstract Object execInner(String expr, Map<String, Object> data);

    /**
     * 执行有返回值的表达式
     *
     * @param expr
     * @param data
     * @return
     */
    public abstract Object execReturnCompiled(Object expr, Map<String, Object> data);


    private static BaseExprEngine engine;

    /**
     * 创建基于MVEL的表达式引擎。
     *
     * @return
     */
    public synchronized static BaseExprEngine defaultEngine() {
        if (engine == null) {
            engine = new MvelEngine();
        }
        return engine;
    }
}
