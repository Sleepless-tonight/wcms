package com.nostyling.wcms.test.DynamicProxy.test3;

/**
 * @author shiliang
 * @Classname CalculatorImpl
 * @Date 2021/5/31 15:13
 * @Description 目标对象实现类，实现Calculator接口
 */
public class CalculatorImpl implements Calculator {

    //加
    public int add(int a, int b) {
        int result = a + b;
        return result;
    }

    //减
    public int subtract(int a, int b) {
        int result = a - b;
        return result;
    }

    //乘法、除法...
}
