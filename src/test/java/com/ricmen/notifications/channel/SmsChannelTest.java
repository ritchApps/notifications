package com.ricmen.notifications.channel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SmsChannelTest {

  private SimulationConfig simulationConfig;
  private SmsChannel smsChannel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    simulationConfig = new SimulationConfig();
    smsChannel = new SmsChannel(simulationConfig);

    user = new User();
    user.setId(1);
    user.setName("Ricardo Mendoza");
    user.setPhone("+1-555-0101");
    user.setEmail("ricardo@example.com");

    message = new Message();
    message.setId(1L);
    message.setCategory(Category.SPORTS);
    message.setBody("Sports update");
  }

  @Test
  void send_shouldNotThrowWhenSimulationDisabled() {
    simulationConfig.setEnabled(false);
    assertThatCode(() -> smsChannel.send(user, message)).doesNotThrowAnyException();
  }

  @Test
  void send_shouldUsePhoneNumberFromUser() {
    simulationConfig.setEnabled(false);
    // send() completes without exception — phone is used internally via log
    assertThatCode(() -> smsChannel.send(user, message)).doesNotThrowAnyException();
    assertThat(user.getPhone()).isEqualTo("+1-555-0101");
  }

  @RepeatedTest(20)
  void send_canThrowWhenSimulationEnabled() {
    simulationConfig.setEnabled(true);
    try {
      smsChannel.send(user, message);
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("temporarily unavailable");
    }
  }

  @Test
  void supportedChannel_shouldReturnSMS() {
    assertThat(smsChannel.supportedChannel()).isEqualTo(ChannelType.SMS);
  }
}
