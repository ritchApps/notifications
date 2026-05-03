package com.ricmen.notifications.channel;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailChannel implements NotificationChannel {

  @Override
  public void send(User user, Message message) {
    log.info("Sending Email to {} at {} | category: {} | message: {}",
        user.getName(),
        user.getEmail(),
        message.getCategory(),
        message.getBody());
  }

  @Override
  public ChannelType supportedChannel() {
    return ChannelType.EMAIL;
  }
}
