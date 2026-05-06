package com.ricmen.notifications.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.service.NotificationDispatchService;

@ExtendWith(MockitoExtension.class)
class MessageReceivedEventListenerTest {

  @Mock
  private NotificationDispatchService notificationDispatchService;

  @InjectMocks
  private MessageReceivedEventListener listener;

  @Test
  void onMessageReceived_shouldDispatchMessage() {
    Message message = new Message();
    message.setCategory(Category.SPORTS);

    listener.onMessageReceived(new MessageReceivedEvent(message));

    verify(notificationDispatchService).dispatch(message);
  }
}
