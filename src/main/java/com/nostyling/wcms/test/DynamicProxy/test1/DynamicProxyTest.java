package com.nostyling.wcms.test.DynamicProxy.test1;

/**
 * @author shiliang
 * @Classname DynamicProxyDemonstration
 * @Date 2021/5/14 15:17
 * @Description 动态代理演示
 */
public class DynamicProxyTest {
    public static void main(String[] args) {
        // jdk动态代理测试
        // InvocationHandlerImpl<Subject> invocationHandler = new InvocationHandlerImpl<Subject>(new RealSubject());
        // Subject proxy = invocationHandler.getProxy();
        // proxy.SayGoodBye();
        Subject proxy = InvocationHandlerImpl.getProxy(Subject.class, new RealSubject());
        proxy.SayGoodBye();
        proxy.SayHello("test1");
    }

}
