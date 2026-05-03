package com.ricmen.notifications.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.dto.response.NotificationLogResponseDto;
import com.ricmen.notifications.mapper.NotificationLogMapper;
import com.ricmen.notifications.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationLogService {
  private final NotificationLogRepository notificationLogRepository;
  private final NotificationLogMapper mapper;

  public List<NotificationLogResponseDto> getAllLogs() {
    List<NotificationLog> logs = notificationLogRepository.findAllByOrderBySentAtDesc();
    return logs.stream().map(mapper::toDto).toList();
  }
}
