CREATE INDEX idx_ucs_category_id
    ON user_category_subscriptions (category_id);

CREATE INDEX idx_uc_user_id
    ON user_channels (user_id);

CREATE INDEX idx_messages_category_id
    ON messages (category_id);

CREATE INDEX idx_nl_sent_at
    ON notification_logs (sent_at DESC);

CREATE INDEX idx_nl_message_id
    ON notification_logs (message_id);
