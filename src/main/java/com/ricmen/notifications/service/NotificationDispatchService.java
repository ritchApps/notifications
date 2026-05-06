package com.ricmen.notifications.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    record Delivery(ChannelType channelType, User user) {}

    List<Delivery> deliveries = subscribedUsers.stream()
        .flatMap(user -> user.getChannels().stream()
            .map(channelType -> new Delivery(channelType, user)))
        .sorted(Comparator.comparing(Delivery::channelType))
        .toList();

    List<NotificationLog> logs = new ArrayList<>();

    for (Delivery delivery : deliveries) {
      NotificationStatus status;
      String errorMessage = null;
      try {
        channels.get(delivery.channelType()).send(delivery.user(), message);
        status = NotificationStatus.DELIVERED;
      } catch (Exception e) {
        log.error("Failed to send {} notification to {}: {}",
            delivery.channelType(), delivery.user().getName(), e.getMessage());
        status = NotificationStatus.FAILED;
        errorMessage = e.getMessage();
      }
      logs.add(buildLog(delivery.user(), message, delivery.channelType(), status, errorMessage));
    }

    notificationLogRepository.saveAll(logs);
  }

  private NotificationLog buildLog(User user, Message message, ChannelType channelType,
      NotificationStatus status, String errorMessage) {
    NotificationLog entry = new NotificationLog();
    entry.setUser(user);
    entry.setMessage(message);
    entry.setChannelType(channelType);
    entry.setStatus(status);
    entry.setErrorMessage(errorMessage);
    return entry;
  }

}
