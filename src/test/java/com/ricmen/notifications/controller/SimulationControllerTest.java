package com.ricmen.notifications.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricmen.notifications.config.SimulationConfig;

@WebMvcTest(SimulationController.class)
class SimulationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SimulationConfig simulationConfig;

  @Test
  void getStatus_shouldReturnEnabledFalse() throws Exception {
    when(simulationConfig.isEnabled()).thenReturn(false);

    mockMvc.perform(get("/api/simulation"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enabled").value(false));
  }

  @Test
  void getStatus_shouldReturnEnabledTrue() throws Exception {
    when(simulationConfig.isEnabled()).thenReturn(true);

    mockMvc.perform(get("/api/simulation"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enabled").value(true));
  }

  @Test
  void setStatus_shouldEnableSimulation() throws Exception {
    when(simulationConfig.isEnabled()).thenReturn(true);

    mockMvc.perform(put("/api/simulation")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"enabled\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enabled").value(true));

    verify(simulationConfig).setEnabled(true);
  }

  @Test
  void setStatus_shouldDisableSimulation() throws Exception {
    when(simulationConfig.isEnabled()).thenReturn(false);

    mockMvc.perform(put("/api/simulation")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"enabled\":false}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enabled").value(false));

    verify(simulationConfig).setEnabled(false);
  }
}
