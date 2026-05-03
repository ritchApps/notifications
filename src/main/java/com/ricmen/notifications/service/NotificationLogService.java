package com.ricmen.notifications.service;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.repository.NotificationLogRepository;
import com.sun.nio.sctp.Notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationLogService {
  private final NotificationLogRepository notificationLogRepository;

  public List<NotificationLog> getAllLogs() {
    return notificationLogRepository.findAllByOrderBySentAtDesc();
  }
}
