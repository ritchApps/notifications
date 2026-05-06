package com.ricmen.notifications.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

class PushNotificationChannelTest {

  private SimulationConfig simulationConfig;
  private PushNotificationChannel pushChannel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    simulationConfig = new SimulationConfig();
    pushChannel = new PushNotificationChannel(simulationConfig);

    user = new User();
    user.setId(3);
    user.setName("Ricardo Mendoza");
    user.setEmail("ricardo.mendoza@example.com");
    user.setPhone("+1-555-03030");

    message = new Message();
    message.setBody("New movie released");
    message.setId(3L);
    message.setCategory(Category.MOVIES);
  }

  @Test
  void send_shouldNotThrowWhenSimulationDisabled() {
    simulationConfig.setEnabled(false);
    assertThatCode(() -> pushChannel.send(user, message)).doesNotThrowAnyException();
  }

  @Test
  void send_shouldHandleAllCategoriesWithSimulationDisabled() {
    simulationConfig.setEnabled(false);
    for (Category category : Category.values()) {
      message.setCategory(category);
      assertThatCode(() -> pushChannel.send(user, message)).doesNotThrowAnyException();
    }
  }

  @RepeatedTest(20)
  void send_canThrowWhenSimulationEnabled() {
    simulationConfig.setEnabled(true);
    try {
      pushChannel.send(user, message);
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("temporarily unavailable");
    }
  }

  @Test
  void supportedChannel_shouldReturnPushNotification() {
    assertThat(pushChannel.supportedChannel()).isEqualTo(ChannelType.PUSH_NOTIFICATION);
  }
}
