package com.example.toy_project.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AppController {
  @GetMapping("/")
  public ResponseEntity<String> index() {
    return ResponseEntity.ok("Hello World!");
  }
}
