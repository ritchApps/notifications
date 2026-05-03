package com.ricmen.notifications.mapper;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.dto.response.MessageResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
  MessageResponseDto toDto(Message message);
}
