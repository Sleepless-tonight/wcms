package com.nostyling.wcms.test.DynamicProxy.test1;

/**
 * @author shiliang
 * @Classname Subject
 * @Date 2021/5/14 15:11
 * @Description 需要动态代理的接口
 */
public interface Subject {

    /**
     * 你好
     *
     * @param name
     * @return
     */
    String SayHello(String name);

    /**
     * 再见
     *
     * @return
     */
    String SayGoodBye();

}
