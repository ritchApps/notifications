package com.ricmen.notifications.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.dto.response.NotificationLogResponseDto;

@Mapper(componentModel = "spring")
public interface NotificationLogMapper {
  @Mapping(source = "user.name", target = "userName")
  @Mapping(source = "user.email", target = "userEmail")
  @Mapping(source = "message.body", target = "messageBody")
  @Mapping(source = "message.category", target = "category")
  NotificationLogResponseDto toDto(NotificationLog notificationLog);
}
