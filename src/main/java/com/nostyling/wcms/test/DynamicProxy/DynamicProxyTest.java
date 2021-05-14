package com.nostyling.wcms.test.DynamicProxy;

/**
 * @author shiliang
 * @Classname DynamicProxyDemonstration
 * @Date 2021/5/14 15:17
 * @Description 动态代理演示
 */
public class DynamicProxyTest {
    public static void main(String[] args) {
        // jdk动态代理测试
        Subject proxy = new InvocationHandlerImpl(new RealSubject()).getProxy();
        proxy.SayGoodBye();
    }

}
