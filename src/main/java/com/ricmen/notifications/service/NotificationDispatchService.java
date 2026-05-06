package com.ricmen.notifications.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import com.ricmen.notifications.channel.NotificationChannel;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;
import com.ricmen.notifications.repository.NotificationLogRepository;
import com.ricmen.notifications.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationDispatchService {

  private final UserRepository userRepository;
  private final NotificationLogRepository notificationLogRepository;
  private final Map<ChannelType, NotificationChannel> channels;

  public NotificationDispatchService(
      UserRepository userRepository,
      NotificationLogRepository notificationLogRepository,
      List<NotificationChannel> channelList) {
    this.userRepository = userRepository;
    this.notificationLogRepository = notificationLogRepository;
    this.channels = channelList.stream()
        .collect(Collectors.toMap(
            NotificationChannel::supportedChannel,
            Function.identity()));
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void dispatch(Message message) {
    List<User> subscribedUsers = userRepository.findAllSubscribedToCategory(message.getCategory());

    if (subscribedUsers.isEmpty()) {
      log.warn("No subscribers found for category: {}", message.getCategory());
      return;
    }

    for (User user : subscribedUsers) {
      for (ChannelType channelType : user.getChannels()) {
        try {
          NotificationChannel channel = channels.get(channelType);
          channel.send(user, message);
          saveLog(user, message, channelType, NotificationStatus.DELIVERED, null);
        } catch (Exception e) {
          log.error("Failed to send {} notification to {}: {}",
              channelType, user.getName(), e.getMessage());
          saveLog(user, message, channelType, NotificationStatus.FAILED, e.getMessage());
        }
      }
    }
  }

  private void saveLog(User user, Message message, ChannelType channelType, NotificationStatus status,
      String errorMessage) {
    NotificationLog notificationLog = new NotificationLog();
    notificationLog.setUser(user);
    notificationLog.setMessage(message);
    notificationLog.setChannelType(channelType);
    notificationLog.setStatus(status);
    notificationLog.setErrorMessage(errorMessage);
    notificationLogRepository.save(notificationLog);
  }

}
