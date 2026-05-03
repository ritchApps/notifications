package com.ricmen.notifications.mapper;

import org.mapstruct.Mapper;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.dto.response.MessageResponseDto;

@Mapper(componentModel = "spring")
public interface MessageMapper {
  MessageResponseDto toDto(Message message);
}
