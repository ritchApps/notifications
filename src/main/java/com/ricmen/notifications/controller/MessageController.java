package com.ricmen.notifications.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricmen.notifications.dto.request.SendMessageRequestDto;
import com.ricmen.notifications.dto.response.CategoryResponseDto;
import com.ricmen.notifications.dto.response.MessageResponseDto;
import com.ricmen.notifications.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<MessageResponseDto> sendMessage(
      @Valid @RequestBody SendMessageRequestDto request) {
    MessageResponseDto messageDto = messageService.send(request.getCategory(), request.getBody());

    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryResponseDto>> getCategories() {
    return ResponseEntity.ok(messageService.getAllCategories());
  }

}
