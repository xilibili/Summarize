/*
 * Copyright (c) 2021, www.jd.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 */
package com.github.summary.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Description: 启动类
 *
 * @author wangzhiqiang39
 * @version 1.0.0
 * @date 2021/7/23
 */
@Slf4j
@SpringBootApplication
@ComponentScan(value = "com.github.summary")
public class SummaryApplication {

    /**
     * 程序入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            // 文档地址： http://127.0.0.1:8080/doc.html
            SpringApplication.run(SummaryApplication.class, args);
            log.info("——系统 SummaryApplication 启动成功！！！！");
        } catch (Exception e) {
            log.error("程序启动错误信息", e);
        }
    }
}
