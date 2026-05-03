package com.ricmen.notifications.event;

import com.ricmen.notifications.domain.entity.Message;

public record MessageReceivedEvent(Message message) {
}
