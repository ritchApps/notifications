package com.ricmen.notifications.channel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ricmen.notifications.domain.entity.Message;
import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

class EmailChannelTest {

  private EmailChannel emailChannel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    emailChannel = new EmailChannel();

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
  void send_shouldNotThrowException() {
    assertThatCode(() -> emailChannel.send(user, message))
        .doesNotThrowAnyException();
  };

  @Test
  void send_shouldHandleAllCategories() {
    for (Category category : Category.values()) {
      message.setCategory(category);
      assertThatCode(() -> emailChannel.send(user, message))
          .doesNotThrowAnyException();
    }
  }

  @Test
  void supportedChannel_shouldReturnEmail() {
    assertThat(emailChannel.supportedChannel()).isEqualTo(ChannelType.EMAIL);
  }

}
