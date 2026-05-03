package com.ricmen.notifications.domain.entity;

import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.domain.enums.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 150, unique = true)
  private String email;

  @Column(nullable = false, length = 20)
  private String phone;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_category_subscriptions", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "category_id")
  @Enumerated(EnumType.ORDINAL)
  private Set<Category> categories = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_channels", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "channel_type_id")
  @Enumerated(EnumType.ORDINAL)
  private Set<ChannelType> channels = new HashSet<>();

}
