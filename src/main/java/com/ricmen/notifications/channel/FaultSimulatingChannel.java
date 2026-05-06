package com.ricmen.notifications.channel;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FaultSimulatingChannel implements NotificationChannel {

  private final NotificationChannel delegate;
  private final SimulationConfig simulationConfig;

  @Override
  public void send(User user, Message message) {
    if (simulationConfig.isEnabled() && Math.random() < 0.2) {
      throw new RuntimeException(delegate.supportedChannel() + " service temporarily unavailable");
    }
    delegate.send(user, message);
  }

  @Override
  public ChannelType supportedChannel() {
    return delegate.supportedChannel();
  }
}
