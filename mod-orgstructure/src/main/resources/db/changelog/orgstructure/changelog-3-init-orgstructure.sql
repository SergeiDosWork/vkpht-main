--liquibase formatted sql

--changeset s_podrezov:orgstructure-database-init

CREATE TABLE rt_core.org_position_assignment
(
    id                   bigint                                                NOT NULL,
    abbreviation         character varying(128)                                NOT NULL,
    date_from            timestamp WITHOUT TIME ZONE DEFAULT NOW()             NOT NULL,
    date_to              timestamp WITHOUT TIME ZONE,
    full_name            character varying(512)                                NOT NULL,
    probation_date_to    timestamp WITHOUT TIME ZONE DEFAULT NOW()             NOT NULL,
    short_name           character varying(256)                                NOT NULL,
    stake                real                                                  NOT NULL,
    category_id          bigint,
    employee_id          bigint                                                NOT NULL,
    placement_id         bigint,
    position_id          bigint                                                NOT NULL,
    precursor_id         bigint,
    status_id            smallint,
    substitution_type_id smallint,
    type_id              smallint,
    external_id          character varying(128),
    author_employee_id   bigint                                                NOT NULL,
    update_employee_id   bigint                                                NOT NULL,
    update_date          timestamp WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    job_title_id         bigint
);