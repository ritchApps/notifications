package com.ricmen.notifications.repository;

import com.ricmen.notifications.domain.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
  @Query("""
      SELECT nl FROM NotificationLog nl
      JOIN FETCH nl.user
      JOIN FETCH nl.message
      ORDER BY nl.sentAt DESC
      """)
  List<NotificationLog> findAllByOrderBySentAtDesc();
}
