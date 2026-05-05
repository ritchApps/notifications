package com.ricmen.notifications.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;
import com.ricmen.notifications.dto.response.NotificationLogResponseDto;
import com.ricmen.notifications.mapper.NotificationLogMapper;
import com.ricmen.notifications.repository.NotificationLogRepository;

@ExtendWith(MockitoExtension.class)
class NotificationLogServiceTest {

  @Mock
  private NotificationLogRepository notificationLogRepository;

  @Mock
  private NotificationLogMapper notificationLogMapper;

  @InjectMocks
  private NotificationLogService notificationLogService;

  @Test
  void getAllLogs_shouldReturnLogsOrderedByDateDesc() {
    NotificationLog log1 = buildLog(1L, NotificationStatus.DELIVERED);
    NotificationLog log2 = buildLog(2L, NotificationStatus.FAILED);

    NotificationLogResponseDto dto1 = buildDto(1L, NotificationStatus.DELIVERED);
    NotificationLogResponseDto dto2 = buildDto(2L, NotificationStatus.FAILED);

    when(notificationLogRepository.findAllByOrderBySentAtDesc()).thenReturn(List.of(log1, log2));
    when(notificationLogMapper.toDto(log1)).thenReturn(dto1);
    when(notificationLogMapper.toDto(log2)).thenReturn(dto2);

    List<NotificationLogResponseDto> response = notificationLogService.getAllLogs();

    assertThat(response).hasSize(2);
    assertThat(response.get(0).getId()).isEqualTo(dto1.getId());
    assertThat(response.get(1).getId()).isEqualTo(dto2.getId());
  }

  @Test
  void getAllLogs_shouldReturnEmptyListWhenNoLogs() {
    when(notificationLogRepository.findAllByOrderBySentAtDesc()).thenReturn(List.of());

    List<NotificationLogResponseDto> response = notificationLogService.getAllLogs();

    assertThat(response).isEmpty();

  }

  @Test
  void getAllLogs_shouldCallRepositoryOnce() {
    when(notificationLogRepository.findAllByOrderBySentAtDesc())
        .thenReturn(List.of());

    notificationLogService.getAllLogs();

    verify(notificationLogRepository).findAllByOrderBySentAtDesc();
  }

  @Test
  void getAllLogs_shouldReturnDeliveredAndFailedLogs() {
    NotificationLog delivered = buildLog(1L, NotificationStatus.DELIVERED);
    NotificationLog failed = buildLog(2L, NotificationStatus.FAILED);
    NotificationLogResponseDto dtoDelivered = buildDto(1L, NotificationStatus.DELIVERED);
    NotificationLogResponseDto dtoFailed = buildDto(2L, NotificationStatus.FAILED);

    when(notificationLogRepository.findAllByOrderBySentAtDesc())
        .thenReturn(List.of(delivered, failed));

    when(notificationLogMapper.toDto(delivered)).thenReturn(dtoDelivered);
    when(notificationLogMapper.toDto(failed)).thenReturn(dtoFailed);

    List<NotificationLogResponseDto> response = notificationLogService.getAllLogs();

    assertThat(response).extracting(NotificationLogResponseDto::getStatus)
        .containsExactly(NotificationStatus.DELIVERED, NotificationStatus.FAILED);

  }

  private NotificationLogResponseDto buildDto(long id, NotificationStatus status) {
    return NotificationLogResponseDto.builder()
        .id(id).status(status)
        .userEmail("Ricardo Mendoza")
        .messageBody("Test message")
        .category("SPORTS")
        .channelType(ChannelType.SMS)
        .sentAt(LocalDateTime.now())
        .build();
  }

  private NotificationLog buildLog(long id, NotificationStatus notificationStatus) {
    NotificationLog notificationLog = new NotificationLog();
    notificationLog.setId(id);
    notificationLog.setStatus(notificationStatus);

    return notificationLog;
  }

}
