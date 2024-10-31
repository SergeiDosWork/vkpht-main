--liquibase formatted sql

--changeset s_podrezov:init-common-schema

-- TODO example
CREATE TABLE indicator
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY       NOT NULL PRIMARY KEY,
    date_from                   timestamp without time zone DEFAULT NOW() NOT NULL,
    date_to                     timestamp without time zone,
    description                 character varying(2048)                   NOT NULL,
    author_employee_id          bigint                                    NOT NULL,
    name                        character varying(256)                    NOT NULL,
    competence_id               bigint,
    weight                      numeric(6, 2)               DEFAULT 0     NOT NULL,
    indicator_scale_template_id bigint,
    is_short_view               boolean                     DEFAULT FALSE NOT NULL
);