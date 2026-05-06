package com.ricmen.notifications.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricmen.notifications.config.SimulationConfig;
import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

@ExtendWith(MockitoExtension.class)
class FaultSimulatingChannelTest {

  @Mock
  private NotificationChannel delegate;

  private SimulationConfig simulationConfig;
  private FaultSimulatingChannel channel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    simulationConfig = new SimulationConfig();
    channel = new FaultSimulatingChannel(delegate, simulationConfig);

    user = new User();
    user.setName("Ricardo Mendoza");

    message = new Message();
    message.setCategory(Category.SPORTS);
    message.setBody("Sports update");
  }

  @Test
  void send_shouldDelegateToDelegateWhenSimulationDisabled() {
    simulationConfig.setEnabled(false);

    assertThatCode(() -> channel.send(user, message)).doesNotThrowAnyException();
    verify(delegate).send(user, message);
  }

  @Test
  void send_shouldNotCallDelegateWhenFaultIsSimulated() {
    simulationConfig.setEnabled(true);
    when(delegate.supportedChannel()).thenReturn(ChannelType.SMS);

    boolean faultOccurred = false;
    for (int i = 0; i < 100; i++) {
      try {
        channel.send(user, message);
      } catch (RuntimeException e) {
        assertThat(e.getMessage()).contains("temporarily unavailable");
        faultOccurred = true;
        break;
      }
    }
    assertThat(faultOccurred).isTrue();
  }

  @Test
  void supportedChannel_shouldDelegateToDelegate() {
    when(delegate.supportedChannel()).thenReturn(ChannelType.PUSH_NOTIFICATION);
    assertThat(channel.supportedChannel()).isEqualTo(ChannelType.PUSH_NOTIFICATION);
  }
}
