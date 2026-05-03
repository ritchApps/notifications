package com.ricmen.notifications.dto.response;

import java.time.LocalDateTime;
import java.util.Locale.Category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponseDto {
  private Long id;
  private Category category;
  private String body;
  private LocalDateTime createdAt;
}
