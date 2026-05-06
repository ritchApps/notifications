package com.ricmen.notifications.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;
import com.ricmen.notifications.domain.enums.NotificationStatus;
import com.ricmen.notifications.dto.response.NotificationLogResponseDto;
import com.ricmen.notifications.service.NotificationLogService;

@WebMvcTest(NotificationLogController.class)
class NotificationLogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private NotificationLogService notificationLogService;

  @Test
  void getAllLogs_shouldReturn200_withLogs() throws Exception {
    when(notificationLogService.getAllLogs()).thenReturn(List.of(
        NotificationLogResponseDto.builder()
            .id(1L).userName("Alice").status(NotificationStatus.DELIVERED).build(),
        NotificationLogResponseDto.builder()
            .id(2L).userName("Bob").status(NotificationStatus.FAILED).build()));

    mockMvc.perform(get("/api/logs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].userName").value("Alice"));
  }

  @Test
  void getAllLogs_shouldReturn200_whenEmpty() throws Exception {
    when(notificationLogService.getAllLogs()).thenReturn(List.of());

    mockMvc.perform(get("/api/logs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getFilteredLogs_shouldReturn200_withAllParams() throws Exception {
    when(notificationLogService.getFilteredLogs(
        NotificationStatus.DELIVERED, ChannelType.SMS, Category.SPORTS))
        .thenReturn(List.of(NotificationLogResponseDto.builder()
            .id(1L).status(NotificationStatus.DELIVERED).build()));

    mockMvc.perform(get("/api/logs/filter")
            .param("status", "DELIVERED")
            .param("channelType", "SMS")
            .param("category", "SPORTS"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void getFilteredLogs_shouldReturn200_withNoParams() throws Exception {
    when(notificationLogService.getFilteredLogs(null, null, null)).thenReturn(List.of());

    mockMvc.perform(get("/api/logs/filter"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }
}
