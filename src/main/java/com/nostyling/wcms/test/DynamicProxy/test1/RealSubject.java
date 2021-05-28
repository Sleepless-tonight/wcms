package com.nostyling.wcms.test.DynamicProxy.test1;

/**
 * @author shiliang
 * @Classname RealSubject
 * @Date 2021/5/14 15:12
 * @Description 需要代理的实际对象
 */
public class RealSubject implements Subject {

    /**
     * 你好
     *
     * @param name
     * @return
     */
    @Override
    public String SayHello(String name) {
        System.out.println("SayHello");
        return "hello " + name;
    }

    /**
     * 再见
     *
     * @return
     */
    @Override
    public String SayGoodBye() {
        System.out.println("SayGoodBye");

        return " good bye ";
    }
}
