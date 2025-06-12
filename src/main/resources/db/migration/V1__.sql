CREATE SEQUENCE IF NOT EXISTS tasks_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS test_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE tasks
(
    task_id BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT,
    task    VARCHAR(255),
    year    INTEGER,
    month   INTEGER,
    date    INTEGER,
    hour    INTEGER,
    minute  INTEGER,
    CONSTRAINT pk_tasks PRIMARY KEY (task_id)
);

INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'皿洗い' ,2025, 6, 10, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'掃除' ,2025, 6, 10, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'料理' ,2025, 6, 10, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'洗濯' ,2025, 6, 11, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'風呂掃除' ,2025, 6, 11, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'床掃除' ,2025, 6, 11, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'窓拭き' ,2025, 6, 12, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'ゴミ捨て' ,2025, 6, 12, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'お迎え' ,2025, 6, 12, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'買い物' ,2025, 6, 13, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'休憩' ,2025, 6, 13, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'庭掃除' ,2025, 6, 13, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'着替え' ,2025, 6, 14, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'歯磨き' ,2025, 6, 14, 12, 0);
INSERT INTO tasks(user_id, task, year, month, date, hour, minute) VALUES (1,'洗顔' ,2025, 6, 14, 12, 0);



CREATE TABLE test
(
    id   BIGINT NOT NULL,
    text VARCHAR(255),
    CONSTRAINT pk_test PRIMARY KEY (id)
);

CREATE TABLE users
(
    user_id    BIGINT GENERATED ALWAYS AS IDENTITY,
    user_name  VARCHAR(255),
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    salt       VARCHAR(255),
    hash       VARCHAR(255),
    version    INTEGER,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

INSERT INTO users(user_name, first_name, last_name, salt, hash) VALUES ('yuta', 'yuta', 'sato', 'KSjCSUyOce', 'acf604ab02f19d1e15ed9ba51ddd82ddf0093a399ba3674a3647f3cc7ac1594c');

CREATE TABLE sessions
(
    user_id     BIGINT NOT NULL,
    session_id  NCHAR(16) NOT NULL,
    user_name   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (user_id)
);