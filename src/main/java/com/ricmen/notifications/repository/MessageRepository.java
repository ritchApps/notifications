package com.ricmen.notifications.repository;

import com.ricmen.notifications.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
