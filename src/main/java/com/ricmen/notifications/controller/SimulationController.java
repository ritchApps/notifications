package com.ricmen.notifications.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricmen.notifications.config.SimulationConfig;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

  private final SimulationConfig simulationConfig;

  @GetMapping
  public ResponseEntity<Map<String, Boolean>> getStatus() {
    return ResponseEntity.ok(Map.of("enabled", simulationConfig.isEnabled()));
  }

  @PutMapping
  public ResponseEntity<Map<String, Boolean>> setStatus(@RequestBody Map<String, Boolean> body) {
    simulationConfig.setEnabled(Boolean.TRUE.equals(body.get("enabled")));
    return ResponseEntity.ok(Map.of("enabled", simulationConfig.isEnabled()));
  }
}
