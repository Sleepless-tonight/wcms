package com.nostyling.wcms.test.DynamicProxy.test1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author shiliang
 * @Classname InvocationHandlerImpl
 * @Date 2021/5/14 15:14
 * @Description 调用处理器实现类
 * * 每次生成动态代理类对象时都需要指定一个实现了该接口的调用处理器对象
 */
public class InvocationHandlerImpl implements InvocationHandler {

    /**
     * 这个就是我们要代理的真实对象
     */
    private Object subject;
    /**
     * 获取被代理接口实例对象
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), this);
    }
    /**
     * 构造方法，给我们要代理的真实对象赋初值
     *
     * @param subject
     */
    public InvocationHandlerImpl(Object subject) {
        this.subject = subject;
    }

    /**
     * 该方法负责集中处理动态代理类上的所有方法调用。
     * 调用处理器根据这三个参数进行预处理或分派到委托类实例上反射执行
     *
     * @param proxy  代理类实例
     * @param method 被调用的方法对象
     * @param args   调用参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Do something before");
        Object result = method.invoke(subject, args);
        System.out.println("Do something after");
        return result;
    }

}
