--liquibase formatted sql

--changeset s_podrezov:init-tasksetting-schema

-- TODO example
CREATE TABLE task
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY                   NOT NULL PRIMARY KEY,
    date_from          timestamp without time zone DEFAULT NOW()             NOT NULL,
    date_to            timestamp without time zone,
    user_id            integer                                               NOT NULL,
    parent_id          bigint,
    status_id          bigint                                                NOT NULL,
    type_id            bigint,
    root_id            bigint,
    author_employee_id bigint                                                NOT NULL,
    external_id        character varying(128),
    update_employee_id bigint                                                NOT NULL,
    update_date        timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);