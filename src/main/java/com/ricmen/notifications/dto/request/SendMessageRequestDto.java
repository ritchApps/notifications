package com.ricmen.notifications.dto.request;

import com.ricmen.notifications.domain.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequestDto {
  @NotNull(message = "Category is required")
  private Category category;

  @NotBlank(message = "Message body cannot be empty")
  private String body;
}
