package com.nostyling.wcms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shiliang
 * @Classname wcms
 * @Date 2021/2/25 10:37
 * @Description Web Content Management System
 */
@SpringBootApplication
public class WMSCApplication {
    private static Logger logger = LoggerFactory.getLogger(WMSCApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(WMSCApplication.class, args);
        logger.info("成功启动！");
    }
}
