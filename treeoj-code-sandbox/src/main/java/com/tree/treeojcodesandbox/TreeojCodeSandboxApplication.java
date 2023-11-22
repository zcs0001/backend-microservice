package com.tree.treeojcodesandbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.tree")
@EnableDiscoveryClient
@SpringBootApplication
public class TreeojCodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeojCodeSandboxApplication.class, args);
    }

}
