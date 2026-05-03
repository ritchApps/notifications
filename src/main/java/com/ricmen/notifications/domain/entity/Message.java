package com.ricmen.notifications.domain.entity;

import com.ricmen.notifications.domain.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String body;

  @Column(name = "crated_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

}
