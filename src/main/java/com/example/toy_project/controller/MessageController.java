package com.example.toy_project.controller;

import com.example.toy_project.dto.SendMessageDto;
import com.example.toy_project.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

  private final SendMessageService sendMessageService;

  @PostMapping("/message")
  public void sendMessage(@RequestBody SendMessageDto sendMessageDto) {
    sendMessageService.sendMessage(sendMessageDto.getMessage());
  }
}
