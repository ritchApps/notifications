package com.ricmen.notifications.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void home_shouldRedirectToIndexHtml() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/index.html"));
  }
}
