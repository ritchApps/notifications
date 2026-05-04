package com.ricmen.notifications.dto.response;

import java.time.LocalDateTime;

import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationLogResponseDto {

  private Long id;
  private String userName;
  private String userEmail;
  private String messageBody;
  private String category;
  private ChannelType channelType;
  private NotificationStatus status;
  private LocalDateTime sentAt;
  private String errorMessage;
}
