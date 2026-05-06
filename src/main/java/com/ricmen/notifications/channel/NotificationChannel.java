package com.ricmen.notifications.channel;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;

public interface NotificationChannel {
  void send(User user, Message message);

  ChannelType supportedChannel();

  default void simulateSend(User user, Message message, boolean simulationEnabled) {
    if (simulationEnabled && Math.random() < 0.2) {
      throw new RuntimeException(
          supportedChannel() + " service temporarily unavailable");
    }
  }
}
