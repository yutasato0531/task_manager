CREATE SEQUENCE IF NOT EXISTS tasks_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS test_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE tasks
(
    task_id BIGINT NOT NULL,
    user_id BIGINT,
    task    VARCHAR(255),
    year    INTEGER,
    month   INTEGER,
    date    INTEGER,
    hour    INTEGER,
    minute  INTEGER,
    CONSTRAINT pk_tasks PRIMARY KEY (task_id)
);

CREATE TABLE test
(
    id   BIGINT NOT NULL,
    text VARCHAR(255),
    CONSTRAINT pk_test PRIMARY KEY (id)
);

CREATE TABLE users
(
    user_id    BIGINT NOT NULL,
    user_name  VARCHAR(255),
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    salt       VARCHAR(255),
    hash       VARCHAR(255),
    version    INTEGER,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);