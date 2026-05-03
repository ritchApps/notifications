package com.ricmen.notifications.service;

import com.ricmen.notifications.mapper.CategoryMapper;
import com.ricmen.notifications.dto.response.CategoryResponseDto;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.dto.response.MessageResponseDto;
import com.ricmen.notifications.event.MessageReceivedEvent;
import com.ricmen.notifications.mapper.MessageMapper;
import com.ricmen.notifications.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final MessageMapper mapper;
  private final CategoryMapper categoryMapper;

  @Transactional
  public MessageResponseDto send(Category category, String body) {
    Message message = new Message();
    message.setCategory(category);
    message.setBody(body);

    Message saved = messageRepository.save(message);
    log.info("Mesage saved with id: {} for category: {}", saved.getId(), saved.getCategory());

    eventPublisher.publishEvent(new MessageReceivedEvent(saved));
    return mapper.toDto(saved);
  }

  public List<CategoryResponseDto> getAllCategories() {
    return Arrays.stream(Category.values())
        .map(categoryMapper::toDto).toList();
  }
}
