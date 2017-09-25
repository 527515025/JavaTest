package com.us.python;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * Created by yangyibo on 17/6/19.
 * java 调用python 脚本
 */
public class FirstPythonScript {
    public static void main(String[] args) {
        getPythonScript();
    }

    public static void getPythonMethod() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("/Users/yangyibo/Idea/python/support.py");
        PyFunction func = (PyFunction) interpreter.get("sum", PyFunction.class);

        int a = 2010, b = 2;
        PyObject pyobj = func.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println("anwser = " + pyobj.toString());
        interpreter.close();
    }

    public static void getPythonScript() {

        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("/Users/yangyibo/Idea/python/list.py");
        interpreter.close();
    }

}
