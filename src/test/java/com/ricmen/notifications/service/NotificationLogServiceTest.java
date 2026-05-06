package com.ricmen.notifications.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.enums.Category;
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

  };

  @Test
  void getFilteredLogs_shouldReturnOnlyMatchingStatus() {
    NotificationLog delivered = buildLog(1L, NotificationStatus.DELIVERED);
    NotificationLogResponseDto dto = buildDto(1L, NotificationStatus.DELIVERED);

    when(notificationLogRepository.findAllWithFilters(NotificationStatus.DELIVERED, null, null))
        .thenReturn(List.of(delivered));
    when(notificationLogMapper.toDto(delivered)).thenReturn(dto);

    List<NotificationLogResponseDto> result =
        notificationLogService.getFilteredLogs(NotificationStatus.DELIVERED, null, null);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStatus()).isEqualTo(NotificationStatus.DELIVERED);
  }

  @Test
  void getFilteredLogs_shouldReturnOnlyMatchingChannel() {
    NotificationLog log = buildLog(1L, NotificationStatus.DELIVERED);
    NotificationLogResponseDto dto = buildDto(1L, NotificationStatus.DELIVERED);
    dto = NotificationLogResponseDto.builder()
        .id(1L).status(NotificationStatus.DELIVERED)
        .channelType(ChannelType.EMAIL)
        .userEmail("user@example.com").messageBody("msg").category("SPORTS")
        .sentAt(LocalDateTime.now()).build();

    when(notificationLogRepository.findAllWithFilters(null, ChannelType.EMAIL, null))
        .thenReturn(List.of(log));
    when(notificationLogMapper.toDto(log)).thenReturn(dto);

    List<NotificationLogResponseDto> result =
        notificationLogService.getFilteredLogs(null, ChannelType.EMAIL, null);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getChannelType()).isEqualTo(ChannelType.EMAIL);
  }

  @Test
  void getFilteredLogs_shouldReturnOnlyMatchingCategory() {
    NotificationLog log = buildLog(1L, NotificationStatus.DELIVERED);
    NotificationLogResponseDto dto = NotificationLogResponseDto.builder()
        .id(1L).status(NotificationStatus.DELIVERED)
        .channelType(ChannelType.SMS)
        .userEmail("user@example.com").messageBody("msg").category("SPORTS")
        .sentAt(LocalDateTime.now()).build();

    when(notificationLogRepository.findAllWithFilters(null, null, Category.SPORTS))
        .thenReturn(List.of(log));
    when(notificationLogMapper.toDto(log)).thenReturn(dto);

    List<NotificationLogResponseDto> result =
        notificationLogService.getFilteredLogs(null, null, Category.SPORTS);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCategory()).isEqualTo("SPORTS");
  }

  @Test
  void getFilteredLogs_shouldReturnEmptyWhenNoMatch() {
    when(notificationLogRepository.findAllWithFilters(NotificationStatus.FAILED, ChannelType.SMS, Category.MOVIES))
        .thenReturn(List.of());

    List<NotificationLogResponseDto> result =
        notificationLogService.getFilteredLogs(NotificationStatus.FAILED, ChannelType.SMS, Category.MOVIES);

    assertThat(result).isEmpty();
  }

  @Test
  void getFilteredLogs_shouldPassAllNullsWhenNoFiltersApplied() {
    when(notificationLogRepository.findAllWithFilters(null, null, null)).thenReturn(List.of());

    notificationLogService.getFilteredLogs(null, null, null);

    verify(notificationLogRepository).findAllWithFilters(null, null, null);
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
