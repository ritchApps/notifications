package com.ricmen.notifications.service;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricmen.notifications.channel.NotificationChannel;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.NotificationLog;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;
import com.ricmen.notifications.repository.NotificationLogRepository;
import com.ricmen.notifications.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDispatchServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private NotificationLogRepository notificationLogRepository;

  @Mock
  private NotificationChannel smsChannel;

  @Mock
  private NotificationChannel emailChannel;

  @Mock
  private NotificationChannel pushChannel;

  private NotificationDispatchService dispatchService;

  private Message sportsMessage;

  @BeforeEach
  void setUp() {
    when(smsChannel.supportedChannel()).thenReturn(ChannelType.SMS);
    when(emailChannel.supportedChannel()).thenReturn(ChannelType.EMAIL);
    when(pushChannel.supportedChannel()).thenReturn(ChannelType.PUSH_NOTIFICATION);

    dispatchService = new NotificationDispatchService(userRepository, notificationLogRepository,
        List.of(smsChannel, emailChannel, pushChannel));

    sportsMessage = new Message();
    sportsMessage.setCategory(Category.SPORTS);
    sportsMessage.setBody("Sports update");
  }

  @Test
  void dispatch_shouldSendToAllSubscribedUsersAndChannels() {
    User user = buildUser(1, Set.of(Category.SPORTS),
        Set.of(ChannelType.SMS, ChannelType.EMAIL, ChannelType.PUSH_NOTIFICATION));
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS)).thenReturn(List.of(user));

    dispatchService.dispatch(sportsMessage);

    verify(smsChannel).send(user, sportsMessage);
    verify(emailChannel).send(user, sportsMessage);
    verify(pushChannel).send(user, sportsMessage);
    verify(notificationLogRepository, times(3)).save(any(NotificationLog.class));

  }

  @Test
  void dispatch_shouldLogDeliveredStatusOnSuccess() {
    User user = buildUser(1, Set.of(Category.SPORTS), Set.of(ChannelType.SMS));
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS)).thenReturn(List.of(user));

    dispatchService.dispatch(sportsMessage);

    ArgumentCaptor<NotificationLog> captor = ArgumentCaptor.forClass(NotificationLog.class);
    verify(notificationLogRepository).save(captor.capture());
    assertThat(captor.getValue().getStatus()).isEqualTo(NotificationStatus.DELIVERED);
    assertThat(captor.getValue().getErrorMessage()).isNull();
  }

  @Test
  void dispatch_shouldLogFailedStatusWhenChannelThrowsException() {
    User user = buildUser(1, Set.of(Category.SPORTS), Set.of(ChannelType.SMS));
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS)).thenReturn(List.of(user));
    doThrow(new RuntimeException("SMS service unavailable")).when(smsChannel).send(user, sportsMessage);

    dispatchService.dispatch(sportsMessage);

    ArgumentCaptor<NotificationLog> captor = ArgumentCaptor.forClass(NotificationLog.class);
    verify(notificationLogRepository).save(captor.capture());
    assertThat(captor.getValue().getStatus()).isEqualTo(NotificationStatus.FAILED);
    assertThat(captor.getValue().getErrorMessage()).isEqualTo("SMS service unavailable");
  }

  @Test
  void dispatch_shouldContinueWithOtherChannelsWhenOneChannelFails() {
    User user = buildUser(1, Set.of(Category.SPORTS),
        Set.of(ChannelType.SMS, ChannelType.EMAIL, ChannelType.PUSH_NOTIFICATION));
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS)).thenReturn(List.of(user));
    doThrow(new RuntimeException("SMS failed")).when(smsChannel).send(user, sportsMessage);

    dispatchService.dispatch(sportsMessage);

    verify(emailChannel).send(user, sportsMessage);
    verify(notificationLogRepository, times(3)).save(any(NotificationLog.class));

  }

  @Test
  void dispatch_shouldDoNothingWhenNoSusbcribersFound() {
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS)).thenReturn(List.of());

    dispatchService.dispatch(sportsMessage);

    verify(notificationLogRepository, never()).save(any());
    verify(smsChannel, never()).send(any(), any());
    verify(emailChannel, never()).send(any(), any());
  }

  @Test
  void dispatch_shouldSendToMultipleSubscribers() {
    User user1 = buildUser(1, Set.of(Category.SPORTS), Set.of(ChannelType.SMS));
    User user2 = buildUser(2, Set.of(Category.SPORTS), Set.of(ChannelType.EMAIL));
    User user3 = buildUser(3, Set.of(Category.SPORTS), Set.of(ChannelType.PUSH_NOTIFICATION));
    when(userRepository.findAllSubscribedToCategory(Category.SPORTS))
        .thenReturn(List.of(user1, user2, user3));

    dispatchService.dispatch(sportsMessage);

    verify(smsChannel).send(user1, sportsMessage);
    verify(emailChannel).send(user2, sportsMessage);
    verify(pushChannel).send(user3, sportsMessage);
    verify(notificationLogRepository, times(3)).save(any(NotificationLog.class));

  }

  private User buildUser(int id, Set<Category> categories, Set<ChannelType> channels) {
    User user = new User();
    user.setId(id);
    user.setName("User " + id);
    user.setEmail("user" + id + "@example.com");
    user.setPhone("+1-555-000" + id);
    user.setCategories(categories);
    user.setChannels(channels);
    return user;
  }

}
