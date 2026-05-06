package com.ricmen.notifications.channel;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;

public interface NotificationChannel {
  void send(User user, Message message);

  ChannelType supportedChannel();
}
