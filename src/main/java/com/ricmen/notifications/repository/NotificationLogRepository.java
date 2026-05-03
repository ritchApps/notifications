package com.ricmen.notifications.repository;

import com.ricmen.notifications.domain.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
  List<NotificationLog> findAllByOrderBySentAtDesc();
}
