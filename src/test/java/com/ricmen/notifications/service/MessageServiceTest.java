package com.ricmen.notifications.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.dto.response.CategoryResponseDto;
import com.ricmen.notifications.dto.response.MessageResponseDto;
import com.ricmen.notifications.event.MessageReceivedEvent;
import com.ricmen.notifications.mapper.CategoryMapper;
import com.ricmen.notifications.mapper.MessageMapper;
import com.ricmen.notifications.repository.MessageRepository;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Mock
  private com.ricmen.notifications.mapper.MessageMapper MessageMapper;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private MessageService messageService;

  @Test
  void send_shouldPersistMessageWithCorrectCategoryAndBody() {
    Message saved = buildMessage(1L, Category.SPORTS, "Sports udpate");

    when(messageRepository.save(any(Message.class))).thenReturn(saved);
    when(MessageMapper.toDto(saved)).thenReturn(MessageResponseDto.builder()
        .id(1L)
        .category(Category.SPORTS)
        .body("Sports update")
        .build());

    MessageResponseDto result = messageService.send(Category.SPORTS, "Sports update");

    ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
    verify(messageRepository).save(captor.capture());
    assertThat(captor.getValue().getCategory()).isEqualTo(Category.SPORTS);
    assertThat(captor.getValue().getBody()).isEqualTo("Sports update");
    assertThat(result.getId()).isEqualTo(1L);
  }

  @Test
  void send_shouldPublishEventAfterPersisting() {
    Message saved = buildMessage(1L, Category.FINANCE, "Finance update");
    when(messageRepository.save(any(Message.class))).thenReturn(saved);
    when(MessageMapper.toDto(saved)).thenReturn(
        MessageResponseDto.builder().build());

    messageService.send(Category.FINANCE, "Finance update");

    ArgumentCaptor<MessageReceivedEvent> captor = ArgumentCaptor.forClass(MessageReceivedEvent.class);
    verify(eventPublisher).publishEvent(captor.capture());
    assertThat(captor.getValue().message()).isEqualTo(saved);

  }

  @Test
  void send_shouldReturnMappedDto() {
    Message saved = buildMessage(1L, Category.MOVIES, "New Movie");

    MessageResponseDto expectedDto = MessageResponseDto.builder()
        .id(1L)
        .category(Category.MOVIES)
        .body("New movie")
        .build();

    when(messageRepository.save(any(Message.class))).thenReturn(saved);
    when(MessageMapper.toDto(saved)).thenReturn(expectedDto);

    MessageResponseDto result = messageService.send(Category.MOVIES, "New movie");

    assertThat(result).isEqualTo(expectedDto);

  }

  @Test
  void getAllCategories_shouldReturnAllThreeCategories() {
    when(categoryMapper.toDto(Category.SPORTS)).thenReturn(new CategoryResponseDto("SPORTS", "Sports"));
    when(categoryMapper.toDto(Category.FINANCE)).thenReturn(new CategoryResponseDto("FINANCE", "Finance"));
    when(categoryMapper.toDto(Category.MOVIES)).thenReturn(new CategoryResponseDto("MOVIES", "Movies"));

    List<CategoryResponseDto> result = messageService.getAllCategories();

    assertThat(result).extracting(CategoryResponseDto::displayName).containsExactlyInAnyOrder("Sports", "Finance",
        "Movies");
  }

  private Message buildMessage(Long id, Category category, String body) {
    Message saved = new Message();
    saved.setId(id);
    saved.setCategory(category);
    saved.setBody(body);

    return saved;
  }

}
