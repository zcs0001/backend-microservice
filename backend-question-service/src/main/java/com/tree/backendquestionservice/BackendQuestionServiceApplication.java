package com.tree.backendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.tree.backendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.tree")
@EnableDiscoveryClient
@EnableFeignClients(basePackages ={"com.tree.backendserviceclient.service"})
@SpringBootApplication
public class BackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendQuestionServiceApplication.class, args);
    }

}
