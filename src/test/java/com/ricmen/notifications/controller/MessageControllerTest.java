package com.ricmen.notifications.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.dto.response.CategoryResponseDto;
import com.ricmen.notifications.dto.response.MessageResponseDto;
import com.ricmen.notifications.service.MessageService;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @Test
  void sendMessage_shouldReturn201_whenRequestIsValid() throws Exception {
    MessageResponseDto response = MessageResponseDto.builder()
        .id(1L).category(Category.SPORTS).body("Sports update").build();

    when(messageService.send(Category.SPORTS, "Sports update")).thenReturn(response);

    mockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"category\":\"SPORTS\",\"body\":\"Sports update\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.body").value("Sports update"));
  }

  @Test
  void sendMessage_shouldReturn400_whenBodyIsBlank() throws Exception {
    mockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"category\":\"SPORTS\",\"body\":\"\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void sendMessage_shouldReturn400_whenCategoryIsNull() throws Exception {
    mockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"body\":\"Sports update\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void sendMessage_shouldReturn500_whenServiceThrows() throws Exception {
    when(messageService.send(Category.SPORTS, "Sports update"))
        .thenThrow(new RuntimeException("Unexpected failure"));

    mockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"category\":\"SPORTS\",\"body\":\"Sports update\"}"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("An unexpected error ocurred"));
  }

  @Test
  void getCategories_shouldReturn200_withAllCategories() throws Exception {
    when(messageService.getAllCategories()).thenReturn(List.of(
        new CategoryResponseDto("SPORTS", "Sports"),
        new CategoryResponseDto("FINANCE", "Finance"),
        new CategoryResponseDto("MOVIES", "Movies")));

    mockMvc.perform(get("/api/messages/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
  }
}
