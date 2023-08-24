package com.example.toy_project.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMessageService {

  @Value("${rabbitmq.exchange.name}")
  private String exchangeName;

  @Value("${rabbitmq.routing.key}")
  private String routingKey;

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(String message) {
    log.info("message sent: {}", message);
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
  }
}
