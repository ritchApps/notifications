package com.ricmen.notifications.dto.response;

import java.time.LocalDateTime;

import com.ricmen.notifications.domain.enums.Category;

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
