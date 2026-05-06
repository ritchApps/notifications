package com.ricmen.notifications.config;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

@Component
public class SimulationConfig {

  private final AtomicBoolean enabled = new AtomicBoolean(false);

  public boolean isEnabled() {
    return enabled.get();
  }

  public void setEnabled(boolean value) {
    enabled.set(value);
  }
}
