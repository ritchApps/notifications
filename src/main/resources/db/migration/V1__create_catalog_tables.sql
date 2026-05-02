CREATE TABLE categories (
    id SMALLINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
);

CREATE TABLE channel_types (
  id SMALLINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_channel_types PRIMARY KEY(id),
  CONSTRAINT uq_channel_types_name UNIQUE (name)
);
