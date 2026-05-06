package com.ricmen.notifications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ricmen.notifications.channel.EmailChannel;
import com.ricmen.notifications.channel.FaultSimulatingChannel;
import com.ricmen.notifications.channel.NotificationChannel;
import com.ricmen.notifications.channel.PushNotificationChannel;
import com.ricmen.notifications.channel.SmsChannel;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ChannelConfig {

  private final SimulationConfig simulationConfig;

  @Bean
  public NotificationChannel smsChannel() {
    return new FaultSimulatingChannel(new SmsChannel(), simulationConfig);
  }

  @Bean
  public NotificationChannel emailChannel() {
    return new FaultSimulatingChannel(new EmailChannel(), simulationConfig);
  }

  @Bean
  public NotificationChannel pushNotificationChannel() {
    return new FaultSimulatingChannel(new PushNotificationChannel(), simulationConfig);
  }
}
