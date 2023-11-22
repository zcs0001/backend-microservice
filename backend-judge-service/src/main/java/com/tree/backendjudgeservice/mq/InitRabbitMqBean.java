package com.tree.backendjudgeservice.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

import static com.tree.backendcommon.constant.MqConstant.*;

/**
 * 用于创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
 */
@Slf4j
@Component
public class InitRabbitMqBean {

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = CODE_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, CODE_DIRECT_EXCHANGE);

            // 创建队列，随机分配一个队列名称
            String queueName = CODE_QUEUE;

            Map<String, Object> codeMap = new HashMap<>();
            // code队列绑定死信交换机
            codeMap.put("x-dead-letter-exchange", CODE_DLX_EXCHANGE);
            codeMap.put("x-dead-letter-routing-key", CODE_DLX_ROUTING_KEY);
            channel.queueDeclare(queueName, true, false, false, codeMap);
            channel.queueBind(queueName, EXCHANGE_NAME, CODE_ROUTING_KEY);

            // 创建死信队列和死信交换机
            // 创建死信队列
            channel.queueDeclare(CODE_DLX_QUEUE, true, false, false, null);
            // 创建死信交换机
            channel.exchangeDeclare(CODE_DLX_EXCHANGE, CODE_DIRECT_EXCHANGE);
            channel.queueBind(CODE_DLX_QUEUE, CODE_DLX_EXCHANGE, CODE_DLX_ROUTING_KEY);

            log.info("消息队列启动成功");
        } catch (Exception e) {
            log.error("消息队列启动失败", e);
        }
    }
}
