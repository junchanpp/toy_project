package com.example.toy_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

  @RabbitListener(queues = "${rabbitmq.queue.name}")
  public void receiveMessage(String message) {
    log.info("message received: {}", message);
  }
}
