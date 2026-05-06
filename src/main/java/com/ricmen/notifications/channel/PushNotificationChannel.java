package com.ricmen.notifications.channel;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushNotificationChannel implements NotificationChannel {

  @Override
  public void send(User user, Message message) {
    log.info("Sending Push Notification to {} | category: {} | message: {}",
        user.getName(),
        message.getCategory(),
        message.getBody());
  }

  @Override
  public ChannelType supportedChannel() {
    return ChannelType.PUSH_NOTIFICATION;
  }
}
