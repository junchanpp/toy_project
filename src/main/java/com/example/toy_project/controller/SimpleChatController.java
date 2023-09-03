package com.example.toy_project.controller;

import com.example.toy_project.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SimpleChatController {

  @MessageMapping("/send")
  @SendTo("/topic/listen")
  public MessageDTO greeting(MessageDTO messageDTO) throws InterruptedException {
    Thread.sleep(1000); // simulated delay
    return MessageDTO.builder()
        .from("Server")
        .message("Hello " + messageDTO.getFrom() + ", you said \"" + messageDTO.getMessage() + "\"")
        .build();
  }
}
