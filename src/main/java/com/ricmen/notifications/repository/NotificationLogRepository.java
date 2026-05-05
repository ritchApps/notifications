package com.ricmen.notifications.repository;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
  @Query("""
      SELECT nl FROM NotificationLog nl
      JOIN FETCH nl.user
      JOIN FETCH nl.message
      ORDER BY nl.sentAt DESC
      """)
  List<NotificationLog> findAllByOrderBySentAtDesc();

  @Query("""
      SELECT nl FROM NotificationLog nl
      JOIN FETCH nl.user
      JOIN FETCH nl.message
      WHERE (:status IS NULL OR nl.status = :status)
      AND (:channelType IS NULL OR nl.channelType = :channelType)
      AND (:category IS NULL OR nl.message.category = :category)
      ORDER BY nl.sentAt DESC
      """)
  List<NotificationLog> findAllWithFilters(
      @Param("status") NotificationStatus status,
      @Param("channelType") ChannelType channelType,
      @Param("category") Category category);
}
