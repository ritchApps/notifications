package com.ricmen.notifications.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricmen.notifications.dto.response.NotificationLogResponseDto;
import com.ricmen.notifications.service.NotificationLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class NotificationLogController {

  private final NotificationLogService notificationLogService;

  @GetMapping
  public ResponseEntity<List<NotificationLogResponseDto>> getAllLogs() {
    return ResponseEntity.ok(notificationLogService.getAllLogs());
  }

}
