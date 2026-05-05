package com.ricmen.notifications.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;
import com.ricmen.notifications.dto.response.NotificationLogResponseDto;
import com.ricmen.notifications.mapper.NotificationLogMapper;
import com.ricmen.notifications.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationLogService {
  private final NotificationLogRepository notificationLogRepository;
  private final NotificationLogMapper notificationLogMapper;

  public List<NotificationLogResponseDto> getAllLogs() {
    List<NotificationLog> logs = notificationLogRepository.findAllByOrderBySentAtDesc();
    return logs.stream().map(notificationLogMapper::toDto).toList();
  }

  public List<NotificationLogResponseDto> getFilteredLogs(
      NotificationStatus status,
      ChannelType channelType,
      Category category) {
    return notificationLogRepository
        .findAllWithFilters(status, channelType, category)
        .stream()
        .map(notificationLogMapper::toDto)
        .toList();
  }

}
