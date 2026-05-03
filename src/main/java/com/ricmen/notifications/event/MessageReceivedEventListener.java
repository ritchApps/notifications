package com.ricmen.notifications.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ricmen.notifications.service.NotificationDispatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceivedEventListener {
  private final NotificationDispatchService notificationDispatchService;

  @EventListener
  public void onMessageReceived(MessageReceivedEvent event) {
    log.info("Event received for category: {}", event.message().getCategory());
    notificationDispatchService.dispatch(event.message());
  }
}
