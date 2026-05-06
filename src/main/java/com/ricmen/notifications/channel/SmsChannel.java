package com.ricmen.notifications.channel;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsChannel implements NotificationChannel {

  private final SimulationConfig simulationConfig;

  @Override
  public void send(User user, Message message) {
    simulateSend(user, message, simulationConfig.isEnabled());
    log.info("Sending SMS to {} at {} | category: {} | message: {}",
        user.getName(),
        user.getPhone(),
        message.getCategory(),
        message.getBody());
  }

  @Override
  public ChannelType supportedChannel() {
    return ChannelType.SMS;
  }
}
