package com.tree.backendjudgeservice;

import com.tree.backendjudgeservice.mq.CodeMqInitMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.tree")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.tree.backendserviceclient.service"})
@SpringBootApplication
public class BackendJudgeServiceApplication {

    public static void main(String[] args) {
        // 初始化消息队列，先注释掉，改用 Bean 的方式初始化消息队列（InitRabbitMqBean.java）
//        CodeMqInitMain.doInitCodeMq();
        SpringApplication.run(BackendJudgeServiceApplication.class, args);
    }

}
