CREATE TABLE users (
  id SERIAL NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY(id),
  CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE user_category_subscriptions (
  user_id INTEGER NOT NULL,
  category_id SMALLINT NOT NULL,
  CONSTRAINT pk_user_category_subscriptions
    PRIMARY KEY (user_id, category_id),
  CONSTRAINT fk_ucs_user
    FOREIGN KEY(user_id) REFERENCES users (id)
    ON DELETE CASCADE,
  CONSTRAINT fk_ucs_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON DELETE CASCADE
);

CREATE TABLE user_channels (
    user_id         INTEGER  NOT NULL,
    channel_type_id SMALLINT NOT NULL,
    CONSTRAINT pk_user_channels
        PRIMARY KEY (user_id, channel_type_id),
    CONSTRAINT fk_uc_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_uc_channel_type
        FOREIGN KEY (channel_type_id) REFERENCES channel_types (id)
        ON DELETE CASCADE
);

CREATE TABLE messages (
    id          BIGSERIAL    NOT NULL,
    category_id SMALLINT     NOT NULL,
    body        TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_messages PRIMARY KEY (id),
    CONSTRAINT fk_messages_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE notification_logs (
    id              BIGSERIAL    NOT NULL,
    message_id      BIGINT       NOT NULL,
    user_id         INTEGER      NOT NULL,
    channel_type_id SMALLINT     NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    sent_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    error_message   TEXT,
    CONSTRAINT pk_notification_logs PRIMARY KEY (id),
    CONSTRAINT fk_nl_message
        FOREIGN KEY (message_id) REFERENCES messages (id),
    CONSTRAINT fk_nl_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_nl_channel_type
        FOREIGN KEY (channel_type_id) REFERENCES channel_types (id),
    CONSTRAINT chk_nl_status
        CHECK (status IN ('DELIVERED', 'FAILED'))
);



















