package com.ricmen.notifications.repository;

import com.ricmen.notifications.domain.entity.User;
import com.ricmen.notifications.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM User u WHERE :category MEMBER OF u.categories")
  List<User> findAllSubscribedToCategory(@Param("category") Category category);

}
