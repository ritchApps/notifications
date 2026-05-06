package com.ricmen.notifications.channel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

class EmailChannelTest {

  private SimulationConfig simulationConfig;
  private EmailChannel emailChannel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    simulationConfig = new SimulationConfig();
    emailChannel = new EmailChannel(simulationConfig);

    user = new User();
    user.setId(2);
    user.setName("Ricardo Mendoza");
    user.setEmail("ricardo.mendoza@example.com");
    user.setPhone("+1-555-0202");

    message = new Message();
    message.setId(2L);
    message.setCategory(Category.FINANCE);
    message.setBody("Finance update");
  }

  @Test
  void send_shouldNotThrowWhenSimulationDisabled() {
    simulationConfig.setEnabled(false);
    assertThatCode(() -> emailChannel.send(user, message)).doesNotThrowAnyException();
  }

  @Test
  void send_shouldHandleAllCategoriesWithSimulationDisabled() {
    simulationConfig.setEnabled(false);
    for (Category category : Category.values()) {
      message.setCategory(category);
      assertThatCode(() -> emailChannel.send(user, message)).doesNotThrowAnyException();
    }
  }

  @RepeatedTest(20)
  void send_canThrowWhenSimulationEnabled() {
    simulationConfig.setEnabled(true);
    // With simulation on, send() may or may not throw — both outcomes are valid
    try {
      emailChannel.send(user, message);
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).contains("temporarily unavailable");
    }
  }

  @Test
  void supportedChannel_shouldReturnEmail() {
    assertThat(emailChannel.supportedChannel()).isEqualTo(ChannelType.EMAIL);
  }
}
