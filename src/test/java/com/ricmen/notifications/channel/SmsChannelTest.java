package com.ricmen.notifications.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

class SmsChannelTest {

  private SmsChannel smsChannel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    smsChannel = new SmsChannel();

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
  void send_shouldNotThrow() {
    assertThatCode(() -> smsChannel.send(user, message)).doesNotThrowAnyException();
  }

  @Test
  void send_shouldHandleAllCategories() {
    for (Category category : Category.values()) {
      message.setCategory(category);
      assertThatCode(() -> smsChannel.send(user, message)).doesNotThrowAnyException();
    }
  }

  @Test
  void supportedChannel_shouldReturnSMS() {
    assertThat(smsChannel.supportedChannel()).isEqualTo(ChannelType.SMS);
  }
}
