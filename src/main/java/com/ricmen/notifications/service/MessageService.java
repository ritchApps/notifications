package com.ricmen.notifications.service;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.event.MessageReceivedEvent;
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

  @Transactional
  public Message send(Category category, String body) {
    Message message = new Message();
    message.setCategory(category);
    message.setBody(body);

    Message saved = messageRepository.save(message);
    log.info("Mesage saved with id: {} for category: {}", saved.getId(), saved.getCategory());

    eventPublisher.publishEvent(new MessageReceivedEvent(saved));
    return saved;
  }

  public List<Category> getAllCategories() {
    return Arrays.asList(Category.values());
  }
}
