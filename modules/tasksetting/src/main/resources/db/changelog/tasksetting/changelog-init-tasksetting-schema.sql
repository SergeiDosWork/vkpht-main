--liquibase formatted sql
--changeset s_podrezov:init-tasksetting-schema

--canban_level
CREATE TABLE canban_level
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    index              BIGINT UNIQUE                       NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN canban_level.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN canban_level.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN canban_level.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN canban_level.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN canban_level.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN canban_level.update_date IS 'Дата обновления записи';

--status
CREATE TABLE status
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP    DEFAULT NOW()              NOT NULL,
    date_to               TIMESTAMP,
    name                  VARCHAR(256)                            NOT NULL,
    canban_level_id       BIGINT,
    description           VARCHAR(1024),
    index                 BIGINT,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    is_system             BOOLEAN      DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN      DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT status_canban_level_id_fkey FOREIGN KEY (canban_level_id) REFERENCES canban_level (id)
);
COMMENT ON TABLE status IS 'Реестр статусов';
COMMENT ON COLUMN status.id IS 'Уникальный идентификационный номер статуса';
COMMENT ON COLUMN status.date_from IS 'Дата и время создания статуса';
COMMENT ON COLUMN status.date_to IS 'Дата и время удаления статуса. Если статус активен, то значение null';
COMMENT ON COLUMN status.name IS 'Название статуса';
COMMENT ON COLUMN status.canban_level_id IS '---';
COMMENT ON COLUMN status.description IS 'Описание статуса.';
COMMENT ON COLUMN status.index IS 'Порядковый номер статуса для конкретного таска.';
COMMENT ON COLUMN status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN status.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN status.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN status.unit_code IS 'Код юнита';
CREATE INDEX status_canban_level_id_idx ON status (canban_level_id);

--duration
CREATE TABLE duration
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    name                  VARCHAR(256)                        NOT NULL,
    estimated_length_days BIGINT                              NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL
);
COMMENT ON COLUMN duration.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN duration.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN duration.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN duration.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN duration.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN duration.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--module
CREATE TABLE module
(
    code                  VARCHAR(128) PRIMARY KEY                NOT NULL,
    name                  VARCHAR(256)                            NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    is_adminable          smallint      DEFAULT 0                 NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL
);
COMMENT ON TABLE module IS 'Данная таблица содержит каталог бизнес-процессов.';
COMMENT ON COLUMN module.code IS 'Уникальный код модуля';
COMMENT ON COLUMN module.name IS 'Наименование бизнес-процесса';
COMMENT ON COLUMN module.description IS 'Описание бизнес-процесса';
COMMENT ON COLUMN module.is_adminable IS 'Признак администрирования.';
COMMENT ON COLUMN module.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN module.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN module.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN module.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN module.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN module.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN module.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN module.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--process_type
CREATE TABLE process_type
(
    code                            VARCHAR(128) PRIMARY KEY                NOT NULL,
    name                            VARCHAR(256)                            NOT NULL,
    description                     VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    member_root_task_component_id   BIGINT,
    external_id                     VARCHAR(128) UNIQUE,
    date_from                       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                         TIMESTAMP,
    author_employee_id              BIGINT                                  NOT NULL,
    update_employee_id              BIGINT                                  NOT NULL,
    update_date                     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_duration_required            BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_task_type_required           BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_system                       BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system           BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_integral_evaluation_required BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_constant_required BOOLEAN DEFAULT FALSE NOT NULL
);
COMMENT ON TABLE process_type IS 'Данная таблица содержит каталог типов процессов.';
COMMENT ON COLUMN process_type.code IS 'Код типа процесса';
COMMENT ON COLUMN process_type.name IS 'Наименование типа процесса.';
COMMENT ON COLUMN process_type.description IS 'Описание типа процесса.';
COMMENT ON COLUMN process_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN process_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN process_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN process_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN process_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN process_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN process_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN process_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN process_type.is_constant_required IS 'Признак, указывающий, что процесс данного типа должен быть постоянным';
CREATE INDEX process_type_member_root_task_component_id_idx ON process_type (member_root_task_component_id);

--component
CREATE TABLE component
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP     DEFAULT NOW()              NOT NULL,
    date_to               TIMESTAMP,
    module_code           VARCHAR(128)                             NOT NULL,
    name                  VARCHAR(256)                             NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    code                  VARCHAR(128)                             NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE,
    process_type_code     VARCHAR(128),
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                   NOT NULL,
    update_employee_id    BIGINT                                   NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT component_module_code_fkey FOREIGN KEY (module_code) REFERENCES module (code),
    CONSTRAINT component_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code)
);
COMMENT ON TABLE component IS 'Компоненты';
COMMENT ON COLUMN component.id IS 'Уникальный идентификационный номер части бизнес-процесса.';
COMMENT ON COLUMN component.date_from IS 'Дата и время создания.';
COMMENT ON COLUMN component.date_to IS 'Дата и время удаления.';
COMMENT ON COLUMN component.module_code IS 'Идентификатор бизнес-процесса';
COMMENT ON COLUMN component.name IS 'Наименование части бизнес-процесса.';
COMMENT ON COLUMN component.description IS 'Описание части бизнес-процесса.';
COMMENT ON COLUMN component.code IS 'Уникальный код компонента';
COMMENT ON COLUMN component.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN component.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN component.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN component.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN component.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN component.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN component.unit_code IS 'Код юнита';
CREATE INDEX component_module_code_idx ON component (module_code);
CREATE INDEX component_process_type_code_idx ON component (process_type_code);

ALTER TABLE process_type
    ADD CONSTRAINT process_type_member_root_task_component_id_fkey FOREIGN KEY (member_root_task_component_id) REFERENCES component (id);

--access_type
CREATE TABLE access_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                  VARCHAR(256)                        NOT NULL,
    description           VARCHAR(256),
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL
);
COMMENT ON TABLE access_type IS 'Типы доступа';
COMMENT ON COLUMN access_type.id IS 'Уникальный идентификационный номер параметра.';
COMMENT ON COLUMN access_type.name IS 'Наименование параметра.';
COMMENT ON COLUMN access_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN access_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN access_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN access_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN access_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN access_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN access_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN access_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--user_type
CREATE TABLE user_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                  VARCHAR(256)                        NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    code                  VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE user_type IS 'Типы владельцев бизнес-объектов';
COMMENT ON COLUMN user_type.id IS 'Уникальный идентификационный номер типа владельца таска.';
COMMENT ON COLUMN user_type.name IS 'Наименование типа владельца таска.';
COMMENT ON COLUMN user_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN user_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN user_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN user_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN user_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN user_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN user_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN user_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN user_type.code IS 'Уникальный код типа владельца таска';
CREATE UNIQUE INDEX user_type_code_key ON user_type (code) WHERE date_to IS NULL;

--process_type_status_group
CREATE TABLE process_type_status_group
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP    DEFAULT NOW()              NOT NULL,
    date_to               TIMESTAMP,
    process_type_code     VARCHAR(128)                            NOT NULL,
    name                  VARCHAR(256)                            NOT NULL,
    description           VARCHAR(2048)                           NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    is_system             BOOLEAN      DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN      DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT process_type_status_group_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code)
);
COMMENT ON COLUMN process_type_status_group.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN process_type_status_group.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN process_type_status_group.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN process_type_status_group.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN process_type_status_group.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN process_type_status_group.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN process_type_status_group.unit_code IS 'Код юнита';
CREATE INDEX process_type_status_group_process_type_code_idx ON process_type_status_group (process_type_code);

--task_type
CREATE TABLE task_type
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                         VARCHAR(128)                        NOT NULL,
    access_type_id               BIGINT    DEFAULT '1'::BIGINT       NOT NULL,
    user_type_id                 BIGINT    DEFAULT '1'::BIGINT       NOT NULL,
    description                  VARCHAR(1024),
    date_from                    TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                      TIMESTAMP,
    component_id                 BIGINT                              NOT NULL,
    duration_id                  BIGINT,
    process_type_code            VARCHAR(128),
    process_type_status_group_id BIGINT,
    external_id                  VARCHAR(128) UNIQUE,
    author_employee_id           BIGINT                              NOT NULL,
    update_employee_id           BIGINT                              NOT NULL,
    update_date                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system                    BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system        BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_access_type_id_fkey FOREIGN KEY (access_type_id) REFERENCES access_type (id),
    CONSTRAINT task_type_component_id_fkey FOREIGN KEY (component_id) REFERENCES component (id),
    CONSTRAINT task_type_duration_id_fkey FOREIGN KEY (duration_id) REFERENCES duration (id),
    CONSTRAINT task_type_user_type_id_fkey FOREIGN KEY (user_type_id) REFERENCES user_type (id),
    CONSTRAINT task_type_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code),
    CONSTRAINT task_type_process_type_status_group_id_fkey FOREIGN KEY (process_type_status_group_id) REFERENCES process_type_status_group (id)
);
COMMENT ON TABLE task_type IS 'Типы бизнес-объектов';
COMMENT ON COLUMN task_type.id IS 'Уникальный идентификационный номер типа таска';
COMMENT ON COLUMN task_type.name IS 'Название типа таска';
COMMENT ON COLUMN task_type.access_type_id IS 'Тип доступа к таску. Возможны два значения – 1 (частный таск, то есть открыть таск смогут только работник и руководитель) и 2 (открытый)';
COMMENT ON COLUMN task_type.user_type_id IS 'Ссылка на идентификационный номер таблицы user_type, типа владельца таска. Возможны значения – 1 (идентификационный номер работника division_team_assigment_id), 2 (подразделение division_id) или 3 (юр.лицо legal_entity_id)';
COMMENT ON COLUMN task_type.description IS 'Описание типа таска';
COMMENT ON COLUMN task_type.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type.date_to IS 'Дата и время удаления записи';
COMMENT ON COLUMN task_type.component_id IS 'Связь таска с частью бизнес-процесса';
COMMENT ON COLUMN task_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN task_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_access_type_id_idx ON task_type (access_type_id);
CREATE INDEX task_type_access_component_id_idx ON task_type (component_id);
CREATE INDEX task_type_access_duration_id_idx ON task_type (duration_id);
CREATE INDEX task_type_access_user_type_id_idx ON task_type (user_type_id);
CREATE INDEX task_type_access_process_type_code_idx ON task_type (process_type_code);
CREATE INDEX task_type_access_process_type_status_group_id_idx ON task_type (process_type_status_group_id);

--task
CREATE TABLE task
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    user_id            integer                             NOT NULL,
    parent_id          BIGINT,
    status_id          BIGINT                              NOT NULL,
    type_id            BIGINT,
    root_id            BIGINT,
    author_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT task_root_id_fkey FOREIGN KEY (root_id) REFERENCES task (id),
    CONSTRAINT task_status_id_fkey FOREIGN KEY (status_id) REFERENCES status (id),
    CONSTRAINT task_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES task (id),
    CONSTRAINT task_type_id_fkey FOREIGN KEY (type_id) REFERENCES task_type (id)
);
COMMENT ON TABLE task IS 'У Task с разными типами в качестве user_id могут выступать различные сущности.\nДля карточек и целей - это назначения. Для ПЭБД - это подразделение.';
COMMENT ON COLUMN task.id IS 'Уникальный идентификационный номер таска';
COMMENT ON COLUMN task.date_from IS 'Дата и время создания таска';
COMMENT ON COLUMN task.date_to IS 'Дата и время удаления таска. Если таск активен, то значение null';
COMMENT ON COLUMN task.user_id IS 'Ссылка на идентификационный номер работника (division_team_assigment_id), подразделению (division_id) или юр.лицу (legal_entity_id)';
COMMENT ON COLUMN task.parent_id IS 'Ссылка на идентификационный номер той же таблицы Task, родительского таска';
COMMENT ON COLUMN task.status_id IS 'Ссылка на идентификационный номер таблицы status, определяющей статус таска';
COMMENT ON COLUMN task.type_id IS 'Ссылка на идентификационный номер таблицы Task_type, определяющей тип таска';
COMMENT ON COLUMN task.root_id IS 'Ссылка на идентификационный номер той же таблицы Task, верхреуровнего таска (таска, у которого parent_id равно null)';
COMMENT ON COLUMN task.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task.update_date IS 'Дата обновления записи';
CREATE INDEX task_parent_id_idx ON task (parent_id);
CREATE INDEX task_status_id_idx ON task (status_id);
CREATE INDEX task_type_id_idx ON task (type_id);
CREATE INDEX task_root_id_idx ON task (root_id);

--task_type_field_group
CREATE TABLE task_type_field_group
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP     DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    name                  VARCHAR(256)                            NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    index                 BIGINT        DEFAULT 0                 NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL,
    code                  VARCHAR(128),
    unit_code             VARCHAR(128)                            NOT NULL,
    CONSTRAINT task_type_field_group_unit_code_code_key UNIQUE (unit_code, code)
);
COMMENT ON TABLE task_type_field_group IS 'Группы полей бизнес-объектов';
COMMENT ON COLUMN task_type_field_group.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN task_type_field_group.date_from IS 'Дата и время создания группы.';
COMMENT ON COLUMN task_type_field_group.date_to IS 'Дата и время удаления группы.';
COMMENT ON COLUMN task_type_field_group.name IS 'Наименование группы.';
COMMENT ON COLUMN task_type_field_group.description IS 'Описание группы.';
COMMENT ON COLUMN task_type_field_group.index IS 'Порядок строк.';
COMMENT ON COLUMN task_type_field_group.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_type_field_group.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_type_field_group.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_type_field_group.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN task_type_field_group.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_group.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN task_type_field_group.code IS 'Уникальный код группы полей бизнес-объектов';
COMMENT ON COLUMN task_type_field_group.unit_code IS 'Код юнита';

--basic_data_type
CREATE TABLE basic_data_type
(
    code                  VARCHAR(128) PRIMARY KEY            NOT NULL,
    name                  VARCHAR(256) UNIQUE                 NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL
);
COMMENT ON TABLE basic_data_type IS 'Базовые типы данных';
COMMENT ON COLUMN basic_data_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN basic_data_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN basic_data_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN basic_data_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN basic_data_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN basic_data_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN basic_data_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN basic_data_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--field_type
CREATE TABLE field_type
(
    code                  VARCHAR(128) PRIMARY KEY                NOT NULL,
    name                  VARCHAR(256) UNIQUE                     NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    basic_data_type_code  VARCHAR(128),
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL,
    CONSTRAINT field_type_basic_data_type_code_fkey FOREIGN KEY (basic_data_type_code) REFERENCES basic_data_type (code)
);
COMMENT ON TABLE field_type IS 'Типы полей';
COMMENT ON COLUMN field_type.code IS 'Уникальный код типа поля';
COMMENT ON COLUMN field_type.name IS 'Наименование';
COMMENT ON COLUMN field_type.description IS 'Описание';
COMMENT ON COLUMN field_type.basic_data_type_code IS 'Возможность заложить базовый тип данных для установки ограничений выбора формата, типа и т.д. Может быть NULL, если тип данных заранее не определен.';
COMMENT ON COLUMN field_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN field_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN field_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN field_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN field_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN field_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN field_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN field_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX field_type_basic_data_type_code_idx ON field_type (basic_data_type_code);

--component_field
CREATE TABLE component_field
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    component_id          BIGINT                                  NOT NULL,
    field_type_code       VARCHAR(128)                            NOT NULL,
    code                  VARCHAR(128)                            NOT NULL,
    name                  VARCHAR(256)                            NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    is_required           BOOLEAN       DEFAULT FALSE             NOT NULL,
    date_from             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL,
    CONSTRAINT component_field_component_id_fkey FOREIGN KEY (component_id) REFERENCES component (id),
    CONSTRAINT component_field_field_type_code_fkey FOREIGN KEY (field_type_code) REFERENCES field_type (code)
);
COMMENT ON TABLE component_field IS 'Поля компонентов';
COMMENT ON COLUMN component_field.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN component_field.component_id IS 'Уникальный идентификационный номер части бизнес-процесса.';
COMMENT ON COLUMN component_field.field_type_code IS 'Уникальный код типа поля';
COMMENT ON COLUMN component_field.code IS 'Уникальный код поля компонента';
COMMENT ON COLUMN component_field.name IS 'Наименование поля компонента';
COMMENT ON COLUMN component_field.description IS 'Описание';
COMMENT ON COLUMN component_field.is_required IS 'Признак обязательности';
COMMENT ON COLUMN component_field.date_from IS 'Дата создания записи';
COMMENT ON COLUMN component_field.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN component_field.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN component_field.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN component_field.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN component_field.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN component_field.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN component_field.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE UNIQUE INDEX component_field_component_id_code_key ON component_field (component_id, code) WHERE (date_to IS NULL);
CREATE INDEX component_field_component_id_idx ON component_field (component_id);
CREATE INDEX component_field_field_type_code_idx ON component_field (field_type_code);

--task_type_field
CREATE TABLE task_type_field
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP     DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    is_required           smallint                                NOT NULL,
    name                  VARCHAR(256)                            NOT NULL,
    params                VARCHAR(4096) DEFAULT NULL::VARCHAR,
    task_type_id          BIGINT,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    group_id              BIGINT,
    index                 BIGINT        DEFAULT 0                 NOT NULL,
    is_hidden             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_final              BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    component_field_id    BIGINT,
    field_type_code       VARCHAR(128)  DEFAULT 'string'::VARCHAR NOT NULL,
    is_system_visible     BOOLEAN       DEFAULT FALSE             NOT NULL,
    default_value         VARCHAR(1024),
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                                  NOT NULL,
    update_employee_id    BIGINT                                  NOT NULL,
    update_date           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_system_type        BOOLEAN       DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_field_task_type_id_fkey FOREIGN KEY (task_type_id) REFERENCES task_type (id),
    CONSTRAINT task_type_field_group_id_fkey FOREIGN KEY (group_id) REFERENCES task_type_field_group (id),
    CONSTRAINT task_type_field_field_type_code_fkey FOREIGN KEY (field_type_code) REFERENCES field_type (code),
    CONSTRAINT task_type_field_component_field_id_fkey FOREIGN KEY (component_field_id) REFERENCES component_field (id)
);
COMMENT ON TABLE task_type_field IS 'Типы полей бизнес-объектов';
COMMENT ON COLUMN task_type_field.id IS 'Уникальный идентификационный номер типа поля таска';
COMMENT ON COLUMN task_type_field.date_from IS 'Дата и время создания типа поля таска';
COMMENT ON COLUMN task_type_field.date_to IS 'Дата и время удаления типа поля таска. Если тип поля таска активен, то значение null';
COMMENT ON COLUMN task_type_field.is_required IS 'Флаг требуемости. Если 1, то поле обязательно для заполнение при создании таска';
COMMENT ON COLUMN task_type_field.name IS 'Названия типа поля';
COMMENT ON COLUMN task_type_field.params IS 'Параметры поля таска. На текущий момент заполняется только тип, основные форматы:';
COMMENT ON COLUMN task_type_field.task_type_id IS 'Тип таска к которой привязан конкретный тип поля, ссылается на таблицу task_type.';
COMMENT ON COLUMN task_type_field.description IS 'Описание типа поля.';
COMMENT ON COLUMN task_type_field.group_id IS 'Признак, объединяющий поля в группы (используется в tasksetting2).';
COMMENT ON COLUMN task_type_field.index IS 'Порядок следования полей типа таска в рамках типа таска.';
COMMENT ON COLUMN task_type_field.is_hidden IS 'Поле скрыто. Не отображается в интерфейсах.';
COMMENT ON COLUMN task_type_field.is_final IS 'Поле не может быть изменено. Можно только задать значение при создании.';
COMMENT ON COLUMN task_type_field.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field.component_field_id IS 'Уникальный идентификационный номер поля компонента';
COMMENT ON COLUMN task_type_field.field_type_code IS 'Код типа значения поля';
COMMENT ON COLUMN task_type_field.is_system_visible IS 'Признак видимого системного поля';
COMMENT ON COLUMN task_type_field.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_type_field.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_type_field.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_type_field.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN task_type_field.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN task_type_field.is_system_type IS 'Признак что поле является системным';
CREATE INDEX task_type_field_task_type_id_idx ON task_type_field (task_type_id);
CREATE INDEX task_type_field_group_id_idx ON task_type_field (group_id);
CREATE INDEX task_type_field_component_field_id_idx ON task_type_field (component_field_id);
CREATE INDEX task_type_field_field_type_code_idx ON task_type_field (field_type_code);

--task_field
CREATE TABLE task_field
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    value              VARCHAR(1024)                       NOT NULL,
    task_id            BIGINT,
    task_type_field_id BIGINT,
    author_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT task_field_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT task_field_task_type_field_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
COMMENT ON TABLE task_field IS '{"tegs":["Таски"], description:"данная таблица хранит информацию о полях объектов в системе"}';
COMMENT ON COLUMN task_field.id IS 'Уникальный идентификационный номер поля таска';
COMMENT ON COLUMN task_field.date_from IS 'Дата и время создания поля таска';
COMMENT ON COLUMN task_field.date_to IS 'Дата и время удаления поля таска. Если поле таска активен, то значение null';
COMMENT ON COLUMN task_field.value IS 'Значение поля таска';
COMMENT ON COLUMN task_field.task_id IS 'Ссылка на идентификационный номер таблицы Task';
COMMENT ON COLUMN task_field.task_type_field_id IS 'Ссылка на идентификационный номер таблицы task_type_field, определяющей тип поля';
COMMENT ON COLUMN task_field.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_field.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_field.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_field.update_date IS 'Дата обновления записи';
CREATE INDEX task_field_task_id_idx ON task_field (task_id);
CREATE INDEX task_field_task_type_field_id_idx ON task_field (task_type_field_id);

--comparison_sign
CREATE TABLE comparison_sign
(
    code VARCHAR(128) PRIMARY KEY NOT NULL,
    name VARCHAR(256) UNIQUE      NOT NULL
);
COMMENT ON TABLE comparison_sign IS 'Данная таблица содержит каталог знаков сравнения.';
COMMENT ON COLUMN comparison_sign.code IS 'Уникальный код знака';
COMMENT ON COLUMN comparison_sign.name IS 'Наименование знака';

--component_field_inheritance
CREATE TABLE component_field_inheritance
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    component_field_id_from BIGINT                              NOT NULL,
    component_field_id_to   BIGINT                              NOT NULL,
    is_system               BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system   BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT component_field_inheritance_component_field_id_from_fkey FOREIGN KEY (component_field_id_from) REFERENCES component_field (id),
    CONSTRAINT component_field_inheritance_component_field_id_to_fkey FOREIGN KEY (component_field_id_to) REFERENCES component_field (id)
);
COMMENT ON TABLE component_field_inheritance IS 'Наследование полей компонентов';
COMMENT ON COLUMN component_field_inheritance.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN component_field_inheritance.component_field_id_from IS 'Уникальный идентификационный номер поля компонента';
COMMENT ON COLUMN component_field_inheritance.component_field_id_to IS 'Уникальный идентификационный номер поля компонента';
COMMENT ON COLUMN component_field_inheritance.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN component_field_inheritance.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX component_field_inheritance_component_field_id_from_idx ON component_field_inheritance (component_field_id_from);
CREATE INDEX component_field_inheritance_component_field_id_to_idx ON component_field_inheritance (component_field_id_to);

--condition_group
CREATE TABLE condition_group
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    date_to               TIMESTAMP,
    name                  VARCHAR(256)                             NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL
);
COMMENT ON TABLE condition_group IS 'Группы проверок на условия';
COMMENT ON COLUMN condition_group.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN condition_group.date_from IS 'Дата открытия записи';
COMMENT ON COLUMN condition_group.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN condition_group.name IS 'Наименование';
COMMENT ON COLUMN condition_group.description IS 'Описание';
COMMENT ON COLUMN condition_group.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition_group.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN condition_group.unit_code IS 'Код юнита';

--condition_kind
CREATE TABLE condition_kind
(
    code                  VARCHAR(128) PRIMARY KEY          NOT NULL,
    name                  VARCHAR(256)                      NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE       NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE       NOT NULL
);
COMMENT ON TABLE condition_kind IS 'Типы проверок на условия';
COMMENT ON COLUMN condition_kind.code IS 'Уникальный код вида условия';
COMMENT ON COLUMN condition_kind.name IS 'Наименование';
COMMENT ON COLUMN condition_kind.description IS 'Описание';
COMMENT ON COLUMN condition_kind.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition_kind.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--condition
CREATE TABLE condition
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    group_id              BIGINT                              NOT NULL,
    name                  VARCHAR(256)                        NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    kind_code             VARCHAR(128)                        NOT NULL,
    comparison_sign_code  VARCHAR(128)                        NOT NULL,
    value                 VARCHAR(512)                        NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT condition_group_id_fkey FOREIGN KEY (group_id) REFERENCES condition_group (id),
    CONSTRAINT condition_kind_code_fkey FOREIGN KEY (kind_code) REFERENCES condition_kind (code),
    CONSTRAINT condition_comparison_sign_code_fkey FOREIGN KEY (comparison_sign_code) REFERENCES comparison_sign (code)
);
COMMENT ON TABLE condition IS 'Проверки на условия';
COMMENT ON COLUMN condition.id IS 'Уникальный идентификационный номер условия.';
COMMENT ON COLUMN condition.group_id IS 'Уникальный идентификационный номер группы условий';
COMMENT ON COLUMN condition.name IS 'Наименование условия';
COMMENT ON COLUMN condition.description IS 'Описание условия';
COMMENT ON COLUMN condition.kind_code IS 'Код условия';
COMMENT ON COLUMN condition.comparison_sign_code IS 'Знак сравнения';
COMMENT ON COLUMN condition.value IS 'Значение';
COMMENT ON COLUMN condition.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX condition_group_id_idx ON condition (group_id);
CREATE INDEX condition_kind_code_idx ON condition (kind_code);
CREATE INDEX condition_comparison_sign_code_idx ON condition (comparison_sign_code);

--type_service
CREATE TABLE type_service
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    name                  VARCHAR(256)                             NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    is_using_auth_token   BOOLEAN       DEFAULT FALSE              NOT NULL,
    date_from             TIMESTAMP     DEFAULT NOW()              NOT NULL,
    date_to               TIMESTAMP,
    base_uri              VARCHAR(256)                             NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE              NOT NULL,
    connection_timeout    integer       DEFAULT 60                 NOT NULL,
    read_timeout          integer       DEFAULT 60                 NOT NULL,
    follow_redirects      BOOLEAN       DEFAULT TRUE               NOT NULL,
    unit_code             VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL
);
COMMENT ON TABLE type_service IS 'Тип сервисов для справочников с сервисами';
COMMENT ON COLUMN type_service.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN type_service.name IS 'Наименование';
COMMENT ON COLUMN type_service.description IS 'Описание';
COMMENT ON COLUMN type_service.is_using_auth_token IS 'Признак использования токена авторизации';
COMMENT ON COLUMN type_service.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN type_service.date_to IS 'Дата и время удаления записи. Если запись активна - значение null.';
COMMENT ON COLUMN type_service.base_uri IS 'Код базового URL для формирования запроса';
COMMENT ON COLUMN type_service.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN type_service.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN type_service.unit_code IS 'Код юнита';

--type_calculation
CREATE TABLE type_calculation
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                 VARCHAR(256)                        NOT NULL,
    description          VARCHAR(2048)                       NOT NULL,
    type_service_id      BIGINT                              NOT NULL,
    service_endpoint_uri VARCHAR(512)                        NOT NULL,
    date_from            TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to              TIMESTAMP,
    CONSTRAINT type_calculation_type_service_id_fkey FOREIGN KEY (type_service_id) REFERENCES type_service (id)
);
CREATE INDEX type_calculation_type_service_id_idx ON type_calculation (type_service_id);

--condition_calculation_configuration
CREATE TABLE condition_calculation_configuration
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    condition_id        BIGINT                              NOT NULL,
    type_calculation_id BIGINT                              NOT NULL,
    parameter_name      VARCHAR(256)                        NOT NULL,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    CONSTRAINT role_calculation_configuration_role_id_fkey FOREIGN KEY (condition_id) REFERENCES condition (id),
    CONSTRAINT role_calculation_configuration_type_calculation_id_fkey FOREIGN KEY (type_calculation_id) REFERENCES type_calculation (id)
);
CREATE INDEX condition_calculation_configuration_condition_id_idx ON condition_calculation_configuration (condition_id);
CREATE INDEX condition_calculation_configuration_type_calc_id_idx ON condition_calculation_configuration (type_calculation_id);

--condition_modifier_kind
CREATE TABLE condition_modifier_kind
(
    code                  VARCHAR(128) PRIMARY KEY          NOT NULL,
    name                  VARCHAR(256)                      NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE       NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE       NOT NULL
);
COMMENT ON TABLE condition_modifier_kind IS 'Типы модификаторов проверок на условия';
COMMENT ON COLUMN condition_modifier_kind.code IS 'Уникальный код вида модификатора условий';
COMMENT ON COLUMN condition_modifier_kind.name IS 'Наименование';
COMMENT ON COLUMN condition_modifier_kind.description IS 'Описание';
COMMENT ON COLUMN condition_modifier_kind.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition_modifier_kind.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--condition_modifier
CREATE TABLE condition_modifier
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    condition_id          BIGINT                              NOT NULL,
    name                  VARCHAR(256)                        NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    kind_code             VARCHAR(128)                        NOT NULL,
    comparison_sign_code  VARCHAR(128)                        NOT NULL,
    value                 VARCHAR(512)                        NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT condition_modifier_comparison_sign_code_fkey FOREIGN KEY (comparison_sign_code) REFERENCES comparison_sign (code),
    CONSTRAINT condition_modifier_condition_id_fkey FOREIGN KEY (condition_id) REFERENCES condition (id),
    CONSTRAINT condition_modifier_kind_code_fkey FOREIGN KEY (kind_code) REFERENCES condition_modifier_kind (code)
);
COMMENT ON TABLE condition_modifier IS 'Модификаторы проверок на условия';
COMMENT ON COLUMN condition_modifier.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN condition_modifier.condition_id IS 'Уникальный идентификационный номер каталога условий';
COMMENT ON COLUMN condition_modifier.name IS 'Наименование';
COMMENT ON COLUMN condition_modifier.description IS 'Описание';
COMMENT ON COLUMN condition_modifier.kind_code IS 'Код вида условия';
COMMENT ON COLUMN condition_modifier.comparison_sign_code IS 'Код знака сравнения';
COMMENT ON COLUMN condition_modifier.value IS 'Значение';
COMMENT ON COLUMN condition_modifier.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition_modifier.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX condition_modifier_comparison_sign_code_idx ON condition_modifier (comparison_sign_code);
CREATE INDEX condition_modifier_condition_id_idx ON condition_modifier (condition_id);
CREATE INDEX condition_modifier_kind_code_idx ON condition_modifier (kind_code);

--condition_modifier_kind_condition_kind
CREATE TABLE condition_modifier_kind_condition_kind
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    condition_modifier_kind_code VARCHAR(128)                        NOT NULL,
    condition_kind_code          VARCHAR(128)                        NOT NULL,
    is_system                    BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system        BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT idx_condition_modifier_kind_condition_kind_key UNIQUE (condition_modifier_kind_code, condition_kind_code),
    CONSTRAINT condition_modifier_kind_condition_kind_ckc_fkey FOREIGN KEY (condition_kind_code) REFERENCES condition_kind (code),
    CONSTRAINT condition_modifier_kind_condition_kind_cmkc_fkey FOREIGN KEY (condition_modifier_kind_code) REFERENCES condition_modifier_kind (code)
);
COMMENT ON TABLE condition_modifier_kind_condition_kind IS 'Связь модификаторов и типов услови';
COMMENT ON COLUMN condition_modifier_kind_condition_kind.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN condition_modifier_kind_condition_kind.condition_modifier_kind_code IS 'Уникальный код вида модификатора условий';
COMMENT ON COLUMN condition_modifier_kind_condition_kind.condition_kind_code IS 'Уникальный код вида условия';
COMMENT ON COLUMN condition_modifier_kind_condition_kind.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN condition_modifier_kind_condition_kind.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX condition_modifier_kind_condition_kind_ckc_idx ON condition_modifier_kind_condition_kind (condition_kind_code);
CREATE INDEX condition_modifier_kind_condition_kind_cmkc_idx ON condition_modifier_kind_condition_kind (condition_modifier_kind_code);

--custom_dictionary_table
CREATE TABLE custom_dictionary_table
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    name                  VARCHAR(256)                             NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT custom_dictionary_table_name_unit_code_key UNIQUE (name, unit_code)

);
COMMENT ON TABLE custom_dictionary_table IS 'Данная таблица содержит каталог кастомных справочников';
COMMENT ON COLUMN custom_dictionary_table.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN custom_dictionary_table.name IS 'Наименование';
COMMENT ON COLUMN custom_dictionary_table.description IS 'Описание';
COMMENT ON COLUMN custom_dictionary_table.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN custom_dictionary_table.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN custom_dictionary_table.unit_code IS 'Код юнита';

--custom_dictionary_table_column
CREATE TABLE custom_dictionary_table_column
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    table_id              BIGINT                              NOT NULL,
    index                 BIGINT        DEFAULT 0             NOT NULL,
    name                  VARCHAR(256)                        NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    basic_data_type_code  VARCHAR(128)                        NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT custom_dictionary_table_column_table_id_index_key UNIQUE (table_id, index),
    CONSTRAINT custom_dictionary_table_column_basic_data_type_code_fkey FOREIGN KEY (basic_data_type_code) REFERENCES basic_data_type (code),
    CONSTRAINT custom_dictionary_table_column_table_id_fkey FOREIGN KEY (table_id) REFERENCES custom_dictionary_table (id)
);
COMMENT ON TABLE custom_dictionary_table_column IS 'Данная таблица содержит перечень столбцов для кастомных справочников';
COMMENT ON COLUMN custom_dictionary_table_column.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN custom_dictionary_table_column.table_id IS 'Уникальный идентификационный номер каталога';
COMMENT ON COLUMN custom_dictionary_table_column.index IS 'Индекс';
COMMENT ON COLUMN custom_dictionary_table_column.name IS 'Наименование';
COMMENT ON COLUMN custom_dictionary_table_column.description IS 'Описание';
COMMENT ON COLUMN custom_dictionary_table_column.basic_data_type_code IS 'Код типа данных';
COMMENT ON COLUMN custom_dictionary_table_column.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN custom_dictionary_table_column.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX custom_dictionary_table_column_basic_data_type_code_idx ON custom_dictionary_table_column (basic_data_type_code);
CREATE INDEX custom_dictionary_table_column_table_id_idx ON custom_dictionary_table_column (table_id);

--custom_dictionary_table_column_row
CREATE TABLE custom_dictionary_table_column_row
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    column_id             BIGINT                              NOT NULL,
    index                 BIGINT  DEFAULT 0                   NOT NULL,
    value                 VARCHAR(512),
    is_system             BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT custom_dictionary_table_column_row_column_id_index_key UNIQUE (column_id, index),
    CONSTRAINT custom_dictionary_table_column_row_column_id_fkey FOREIGN KEY (column_id) REFERENCES custom_dictionary_table_column (id)
);
COMMENT ON TABLE custom_dictionary_table_column_row IS 'Данная таблица содержит значения в строках столбца кастомных справочников';
COMMENT ON COLUMN custom_dictionary_table_column_row.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN custom_dictionary_table_column_row.column_id IS 'Уникальный идентификационный номер столбца';
COMMENT ON COLUMN custom_dictionary_table_column_row.index IS 'Связующий индекс';
COMMENT ON COLUMN custom_dictionary_table_column_row.value IS 'Значений';
COMMENT ON COLUMN custom_dictionary_table_column_row.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN custom_dictionary_table_column_row.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX custom_dictionary_table_column_row_column_id_idx ON custom_dictionary_table_column_row (column_id);

--cycle
CREATE TABLE cycle
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from                     TIMESTAMP    DEFAULT NOW()              NOT NULL,
    date_to                       TIMESTAMP,
    name                          VARCHAR(256)                            NOT NULL,
    description                   VARCHAR(2048)                           NOT NULL,
    is_startable                  BOOLEAN      DEFAULT FALSE              NOT NULL,
    date_start                    TIMESTAMP,
    date_end                      TIMESTAMP,
    date_block                    TIMESTAMP,
    member_root_task_task_type_id BIGINT,
    date_start_confirm            TIMESTAMP,
    date_end_confirm              TIMESTAMP,
    member_group_id               BIGINT,
    unit_code                     VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    is_constant                   BOOLEAN      DEFAULT FALSE              NOT NULL,
    CONSTRAINT cycle_member_root_task_task_type_id_fkey FOREIGN KEY (member_root_task_task_type_id) REFERENCES task_type (id)
);
COMMENT ON COLUMN cycle.unit_code IS 'Код юнита';
COMMENT ON COLUMN cycle.is_constant IS 'Признак, указывающий является ли цикл постоянным';
CREATE INDEX cycle_member_root_task_task_type_id_idx ON cycle (member_root_task_task_type_id);

--cycle_task
CREATE TABLE cycle_task
(
    cycle_id BIGINT        NOT NULL,
    task_id  BIGINT UNIQUE NOT NULL,
    CONSTRAINT cycle_task_ps PRIMARY KEY (cycle_id, task_id),
    CONSTRAINT cycle_task_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT cycle_task_cycle_id_fkey FOREIGN KEY (cycle_id) REFERENCES cycle (id)
);
CREATE INDEX cycle_task_cycle_id_idx ON cycle_task (cycle_id);

--duration_label
CREATE TABLE duration_label
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    duration_id           BIGINT                              NOT NULL,
    index                 integer                             NOT NULL,
    name                  VARCHAR(256)                        NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT duration_label_duration_id_fkey FOREIGN KEY (duration_id) REFERENCES duration (id)
);
COMMENT ON COLUMN duration_label.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN duration_label.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN duration_label.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN duration_label.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN duration_label.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN duration_label.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX duration_label_duration_id_idx ON duration_label (duration_id);

--formula
CREATE TABLE formula
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    formula               VARCHAR(256)                            NOT NULL,
    is_system             BOOLEAN      DEFAULT FALSE              NOT NULL,
    is_editable_if_system BOOLEAN      DEFAULT FALSE              NOT NULL,
    unit_code             VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL
);
COMMENT ON TABLE formula IS 'Формула расчета оценки';
COMMENT ON COLUMN formula.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN formula.formula IS 'Текстовая формулировка формулы расчета оценки выполнения цели';
COMMENT ON COLUMN formula.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN formula.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN formula.unit_code IS 'Код юнита';

--scale
CREATE TABLE scale
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to   TIMESTAMP,
    unit_code VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE scale IS 'Данная таблица содержит каталог шкал оценки';
COMMENT ON COLUMN scale.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN scale.date_from IS 'Дата создания записи';
COMMENT ON COLUMN scale.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN scale.unit_code IS 'Код юнита';

--evaluation_scale
CREATE TABLE evaluation_scale
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    formula_id            BIGINT,
    lower_bound           numeric(15, 2),
    upper_bound           numeric(15, 2),
    value                 numeric(15, 2),
    scale_id              BIGINT                              NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT evaluation_scale_formula_id_fkey FOREIGN KEY (formula_id) REFERENCES formula (id),
    CONSTRAINT evaluation_scale_scale_id_fkey FOREIGN KEY (scale_id) REFERENCES scale (id)
);
COMMENT ON TABLE evaluation_scale IS 'Связь шкалы оценки с диапазонами значений';
COMMENT ON COLUMN evaluation_scale.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN evaluation_scale.date_from IS 'Дата создания записи';
COMMENT ON COLUMN evaluation_scale.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN evaluation_scale.formula_id IS 'Уникальный идентификационный номер формулы расчета оценки выполнения цели';
COMMENT ON COLUMN evaluation_scale.lower_bound IS 'Нижняя граница';
COMMENT ON COLUMN evaluation_scale.upper_bound IS 'Верхняя граница';
COMMENT ON COLUMN evaluation_scale.value IS 'Значение';
COMMENT ON COLUMN evaluation_scale.scale_id IS 'Уникальный идентификационный номер шкалы оценки';
COMMENT ON COLUMN evaluation_scale.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN evaluation_scale.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX evaluation_scale_formula_id_idx ON evaluation_scale (formula_id);
CREATE INDEX evaluation_scale_scale_id_idx ON evaluation_scale (scale_id);

--formula_trigger
CREATE TABLE formula_trigger
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                    TIMESTAMP,
    task_type_field_master     BIGINT                              NOT NULL,
    task_type_field_slave      BIGINT                              NOT NULL,
    index                      integer                             NOT NULL,
    is_automatic_recalculation BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT formula_trigger_task_type_field_master_fkey FOREIGN KEY (task_type_field_master) REFERENCES task_type_field (id),
    CONSTRAINT formula_trigger_task_type_field_slave_fkey FOREIGN KEY (task_type_field_slave) REFERENCES task_type_field (id)
);
COMMENT ON TABLE formula_trigger IS 'Информация о триггерах для вычисления значения поля';
COMMENT ON COLUMN formula_trigger.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN formula_trigger.date_from IS 'Дата появления записи';
COMMENT ON COLUMN formula_trigger.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN formula_trigger.task_type_field_master IS 'Id главного зависимого типа поля';
COMMENT ON COLUMN formula_trigger.task_type_field_slave IS 'Id подчиненного зависимого типа поля';
COMMENT ON COLUMN formula_trigger.index IS 'Индекс';
COMMENT ON COLUMN formula_trigger.is_automatic_recalculation IS 'Флаг указывающий на то, нужно ли пересчитывать значения зависимых полей';
CREATE INDEX formula_trigger_task_type_field_master_idx ON formula_trigger (task_type_field_master);
CREATE INDEX formula_trigger_task_type_field_slave_idx ON formula_trigger (task_type_field_slave);

--integral_evaluation_formula
CREATE TABLE integral_evaluation_formula
(
    code        VARCHAR(128) PRIMARY KEY NOT NULL,
    description VARCHAR(2048)
);

--integral_evaluation_formula_variable
CREATE TABLE integral_evaluation_formula_variable
(
    code              VARCHAR(128) PRIMARY KEY NOT NULL,
    description       VARCHAR(2048),
    process_type_code VARCHAR(128),
    CONSTRAINT integral_evaluation_formula_variable_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code)
);
CREATE INDEX integral_evaluation_formula_variable_process_type_code_idx ON integral_evaluation_formula_variable (process_type_code);

--member_group_type
CREATE TABLE member_group_type
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    type VARCHAR(128),
    name VARCHAR(128)
);
COMMENT ON COLUMN member_group_type.id IS 'Идентификатор группы';
COMMENT ON COLUMN member_group_type.type IS 'Тип группы';
COMMENT ON COLUMN member_group_type.name IS 'Название типа группы';

--member_group
CREATE TABLE member_group
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY    NOT NULL PRIMARY KEY,
    date_from            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to              TIMESTAMP,
    name                 VARCHAR(256)                           NOT NULL,
    description          VARCHAR(2048),
    member_group_type_id BIGINT,
    update_date          TIMESTAMP,
    author_employee_id   BIGINT,
    update_employee_id   BIGINT,
    unit_code            VARCHAR(128) DEFAULT NULL::VARCHAR     NOT NULL,
    CONSTRAINT member_group_member_group_type_id_fkey FOREIGN KEY (member_group_type_id) REFERENCES member_group_type (id)
);
COMMENT ON TABLE member_group IS 'Данная таблица содержит каталог групп участников процесса';
COMMENT ON COLUMN member_group.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN member_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN member_group.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN member_group.name IS 'Наименование';
COMMENT ON COLUMN member_group.description IS 'Описание';
COMMENT ON COLUMN member_group.member_group_type_id IS 'Идентификатор группы';
COMMENT ON COLUMN member_group.update_date IS 'Дата обновления группы';
COMMENT ON COLUMN member_group.author_employee_id IS 'Идентификатор сотрудника, создавшего группу';
COMMENT ON COLUMN member_group.update_employee_id IS 'Идентификатор сотрудника, обновившего группу';
COMMENT ON COLUMN member_group.unit_code IS 'Код юнита';
CREATE INDEX member_group_member_group_idx ON member_group (member_group_type_id);

--member_type
CREATE TABLE member_type
(
    code                  VARCHAR(128) PRIMARY KEY NOT NULL,
    name                  VARCHAR(256) UNIQUE      NOT NULL,
    is_system             BOOLEAN DEFAULT FALSE    NOT NULL,
    is_editable_if_system BOOLEAN DEFAULT FALSE    NOT NULL
);
COMMENT ON TABLE member_type IS 'Типы участников процесса';
COMMENT ON COLUMN member_type.code IS 'Уникальный код участника';
COMMENT ON COLUMN member_type.name IS 'Наименование';
COMMENT ON COLUMN member_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN member_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--member
CREATE TABLE member
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    member_group_id    BIGINT                              NOT NULL,
    member_type_code   VARCHAR(128)                        NOT NULL,
    value_id           BIGINT,
    is_exclude         BOOLEAN   DEFAULT FALSE             NOT NULL,
    update_date        TIMESTAMP,
    author_employee_id BIGINT,
    update_employee_id BIGINT,
    CONSTRAINT member_member_group_id_fkey FOREIGN KEY (member_group_id) REFERENCES member_group (id),
    CONSTRAINT member_member_type_code_fkey FOREIGN KEY (member_type_code) REFERENCES member_type (code)
);
COMMENT ON TABLE member IS 'Данная таблица содержит каталог участников процесса';
COMMENT ON COLUMN member.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN member.date_from IS 'Дата создания записи';
COMMENT ON COLUMN member.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN member.member_group_id IS 'Уникальный идентификационный номер группы участников';
COMMENT ON COLUMN member.member_type_code IS 'Уникальный код типа участника';
COMMENT ON COLUMN member.value_id IS 'Уникальный идентификационный номер участника';
COMMENT ON COLUMN member.is_exclude IS 'Признак исключения из процесса';
COMMENT ON COLUMN member.update_date IS 'Дата обновления группы';
COMMENT ON COLUMN member.author_employee_id IS 'Идентификатор сотрудника, обновившего группу';
CREATE INDEX member_member_group_id_idx ON member (member_group_id);
CREATE INDEX member_member_type_code_idx ON member (member_type_code);

--process_template
CREATE TABLE process_template
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                     TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to                       TIMESTAMP,
    date_block                    TIMESTAMP,
    process_type_code             VARCHAR(128)                        NOT NULL,
    name                          VARCHAR(256)                        NOT NULL,
    duration_id                   BIGINT,
    params                        json          DEFAULT '{}'::json    NOT NULL,
    member_root_task_task_type_id BIGINT,
    is_startable                  BOOLEAN       DEFAULT FALSE         NOT NULL,
    description                   VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    integral_evaluation_formula   VARCHAR(256),
    unit_code                     VARCHAR(128)                        NOT NULL,
    CONSTRAINT process_template_member_root_task_task_type_id_fkey FOREIGN KEY (member_root_task_task_type_id) REFERENCES task_type (id),
    CONSTRAINT process_template_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code),
    CONSTRAINT process_template_duration_id_fkey FOREIGN KEY (duration_id) REFERENCES duration (id)
);
COMMENT ON COLUMN process_template.unit_code IS 'Код юнита';
CREATE INDEX process_template_process_type_code_idx ON process_template (process_type_code);
CREATE INDEX process_template_duration_id_idx ON process_template (duration_id);
CREATE INDEX process_template_member_root_task_task_type_id_idx ON process_template (member_root_task_task_type_id);

--process
CREATE TABLE process
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_block                    TIMESTAMP,
    date_from                     TIMESTAMP,
    date_start_confirm            TIMESTAMP,
    date_to                       TIMESTAMP,
    date_end_confirm              TIMESTAMP,
    is_startable                  smallint                            NOT NULL,
    name                          VARCHAR(128)                        NOT NULL,
    params                        json          DEFAULT '{}'::json    NOT NULL,
    type_code                     VARCHAR(128),
    date_start                    TIMESTAMP,
    date_end                      TIMESTAMP,
    external_id                   VARCHAR(128) UNIQUE,
    template_id                   BIGINT,
    cycle_id                      BIGINT,
    member_group_id               BIGINT,
    duration_id                   BIGINT,
    member_root_task_task_type_id BIGINT,
    description                   VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    is_constant                   BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT process_type_code_fkey FOREIGN KEY (type_code) REFERENCES process_type (code),
    CONSTRAINT process_template_template_id_fkey FOREIGN KEY (template_id) REFERENCES process_template (id),
    CONSTRAINT process_cycle_id_fkey FOREIGN KEY (cycle_id) REFERENCES cycle (id),
    CONSTRAINT process_member_root_task_task_type_id_fkey FOREIGN KEY (member_root_task_task_type_id) REFERENCES task_type (id),
    CONSTRAINT process_duration_id_fkey FOREIGN KEY (duration_id) REFERENCES duration (id),
    CONSTRAINT process_member_group_id_fkey FOREIGN KEY (member_group_id) REFERENCES member_group (id)
);
COMMENT ON TABLE process IS 'Данная таблица содержит список процессов.';
COMMENT ON COLUMN process.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN process.date_block IS 'Дата и время блокировки процесса.';
COMMENT ON COLUMN process.date_from IS 'Дата и время создания процесса.';
COMMENT ON COLUMN process.date_start_confirm IS 'Отложенная дата старта.';
COMMENT ON COLUMN process.date_to IS 'Дата и время удаления процесса.';
COMMENT ON COLUMN process.date_end_confirm IS 'Отложенная дата окончания.';
COMMENT ON COLUMN process.is_startable IS 'Флаг активности процесса. Если 1, то процесс активен.';
COMMENT ON COLUMN process.name IS 'Наименование процесса.';
COMMENT ON COLUMN process.params IS 'В params хранится информация о таргетировании процесса в формате json. Структура описана отдельно. Это могут быть: должности, подразделения, юридические лица и т.д.';
COMMENT ON COLUMN process.type_code IS 'Код типа процесса';
COMMENT ON COLUMN process.date_start IS 'Дата и время начала действия процесса.';
COMMENT ON COLUMN process.date_end IS 'Дата и время окончания действия процесса.';
COMMENT ON COLUMN process.external_id IS 'Внешний идентификатор процесса.';
COMMENT ON COLUMN process.is_constant IS 'Признак, указывающий является ли процесс постоянным';
CREATE INDEX process_type_code_idx ON process (type_code);
CREATE INDEX process_template_id_idx ON process (template_id);
CREATE INDEX process_cycle_id_idx ON process (cycle_id);
CREATE INDEX process_member_group_id_idx ON process (member_group_id);
CREATE INDEX process_duration_id_idx ON process (duration_id);
CREATE INDEX process_member_root_task_task_type_id_idx ON process (member_root_task_task_type_id);

--process_member_root_component
CREATE TABLE process_member_root_component
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    process_id         BIGINT                              NOT NULL,
    child_component_id BIGINT                              NOT NULL,
    CONSTRAINT process_member_root_component_child_component_id_fkey FOREIGN KEY (child_component_id) REFERENCES component (id),
    CONSTRAINT process_member_root_component_process_id_fkey FOREIGN KEY (process_id) REFERENCES process (id)
);
CREATE INDEX process_member_root_component_process_id_idx ON process_member_root_component (process_id);
CREATE INDEX process_member_root_component_child_component_id_idx ON process_member_root_component (child_component_id);

--role
CREATE TABLE role
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                  VARCHAR(64) UNIQUE                  NOT NULL,
    is_assignable         BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_technical          BOOLEAN   DEFAULT FALSE             NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    code                  VARCHAR(128) UNIQUE                 NOT NULL
);
COMMENT ON TABLE role IS 'Роли';
COMMENT ON COLUMN role.id IS 'Уникальный идентификационный номер роли.';
COMMENT ON COLUMN role.name IS 'Наименование роли.';
COMMENT ON COLUMN role.is_assignable IS 'Признак назначаемой роли.';
COMMENT ON COLUMN role.is_technical IS 'Признак технической роли';
COMMENT ON COLUMN role.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN role.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN role.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN role.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN role.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN role.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN role.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN role.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--process_role
CREATE TABLE process_role
(
    employee_id BIGINT NOT NULL,
    process_id  BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    CONSTRAINT process_role_pkey PRIMARY KEY (employee_id, process_id, role_id),
    CONSTRAINT process_role_process_id_fkey FOREIGN KEY (process_id) REFERENCES process (id),
    CONSTRAINT process_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES role (id)
);
CREATE INDEX process_role_process_id_idx ON process_role (process_id);
CREATE INDEX process_role_role_id_idx ON process_role (role_id);

--process_stage_group
CREATE TABLE process_stage_group
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    name        VARCHAR(256)                             NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    unit_code   VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL
);
COMMENT ON TABLE process_stage_group IS 'Данная таблица содержит каталог групп этапов процесса';
COMMENT ON COLUMN process_stage_group.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN process_stage_group.name IS 'Наименование';
COMMENT ON COLUMN process_stage_group.description IS 'Описание';
COMMENT ON COLUMN process_stage_group.unit_code IS 'Код юнита';

--stage_type
CREATE TABLE stage_type
(
    code                  VARCHAR(128) PRIMARY KEY          NOT NULL,
    name                  VARCHAR(128)                      NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE       NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE       NOT NULL
);
COMMENT ON TABLE stage_type IS 'Типы этапов процесса';
COMMENT ON COLUMN stage_type.code IS 'Код этапа.';
COMMENT ON COLUMN stage_type.name IS 'Наименование типа этапа.';
COMMENT ON COLUMN stage_type.description IS 'Описание типа этапа.';
COMMENT ON COLUMN stage_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN stage_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--process_stage
CREATE TABLE process_stage
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_start_confirm TIMESTAMP,
    date_to            TIMESTAMP,
    date_end_confirm   TIMESTAMP,
    process_id         BIGINT,
    params             json         DEFAULT '{}'::json     NOT NULL,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    external_id        VARCHAR(128) UNIQUE,
    group_id           BIGINT,
    member_group_id    BIGINT,
    stage_type_code    VARCHAR(128) DEFAULT NULL::VARCHAR,
    CONSTRAINT process_stage_group_id_fkey FOREIGN KEY (group_id) REFERENCES process_stage_group (id),
    CONSTRAINT process_stage_member_group_id_fkey FOREIGN KEY (member_group_id) REFERENCES member_group (id),
    CONSTRAINT process_stage_process_id_fkey FOREIGN KEY (process_id) REFERENCES process (id),
    CONSTRAINT process_stage_stage_type_code_fkey FOREIGN KEY (stage_type_code) REFERENCES stage_type (code)
);
COMMENT ON TABLE process_stage IS 'Данная таблица содержит список этапов, соответствующих процессу.';
COMMENT ON COLUMN process_stage.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN process_stage.date_from IS 'Дата и время создания этапа.';
COMMENT ON COLUMN process_stage.date_start_confirm IS 'Отложенная дата старта.';
COMMENT ON COLUMN process_stage.date_to IS 'Дата и время удаления этапа.';
COMMENT ON COLUMN process_stage.date_end_confirm IS 'Отложенная дата окончания.';
COMMENT ON COLUMN process_stage.process_id IS 'Ссылка на идентификационный номер таблицы process, процесса к которому относится этап.';
COMMENT ON COLUMN process_stage.params IS 'Параметры этапа.';
COMMENT ON COLUMN process_stage.date_start IS 'Дата и время начала действия этапа.';
COMMENT ON COLUMN process_stage.date_end IS 'Дата и время окончания действия этапа.';
COMMENT ON COLUMN process_stage.external_id IS 'Внешний уникальный идентификационный номер';
COMMENT ON COLUMN process_stage.group_id IS 'Уникальный идентификационный номер группы этапов';
COMMENT ON COLUMN process_stage.member_group_id IS 'Уникальный идентификационный номер группы участников процесса';
COMMENT ON COLUMN process_stage.stage_type_code IS 'Ссылка на идентификационный номер таблицы stage_type, типа этапа процесса.';
CREATE INDEX process_stage_process_id_idx ON process_stage (process_id);
CREATE INDEX process_stage_group_id_idx ON process_stage (group_id);
CREATE INDEX process_stage_member_group_id_idx ON process_stage (member_group_id);
CREATE INDEX process_stage_stage_type_code_idx ON process_stage (stage_type_code);

--process_stage_task
CREATE TABLE process_stage_task
(
    process_stage_id BIGINT NOT NULL,
    task_id          BIGINT NOT NULL,
    CONSTRAINT process_stage_task_pkey PRIMARY KEY (process_stage_id, task_id),
    CONSTRAINT process_stage_task_process_stage_id_fkey FOREIGN KEY (process_stage_id) REFERENCES process_stage (id),
    CONSTRAINT process_stage_task_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id)
);
COMMENT ON TABLE process_stage_task IS 'Данная таблица содержит связь между этапом процесса и таском';
COMMENT ON COLUMN process_stage_task.process_stage_id IS 'Уникальный идентификационный номер этапа процесса';
COMMENT ON COLUMN process_stage_task.task_id IS 'Уникальный идентификационный номер таска';
CREATE INDEX process_stage_task_task_id_idx ON process_stage_task (task_id);

--process_task
CREATE TABLE process_task
(
    process_id BIGINT NOT NULL,
    task_id    BIGINT NOT NULL,
    CONSTRAINT process_task_pkey PRIMARY KEY (process_id, task_id),
    CONSTRAINT process_task_process_id_fkey FOREIGN KEY (process_id) REFERENCES process (id),
    CONSTRAINT process_task_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id)
);
COMMENT ON TABLE process_task IS 'Данная таблица содержит привязку рутовых тасков к процессам.';
COMMENT ON COLUMN process_task.process_id IS 'Ссылка на идентификационный номер таблицы process, процесса привязанного к рутовой карточке';
COMMENT ON COLUMN process_task.task_id IS 'Ссылка на идентификационный номер таблицы task, рутовой таска привязанной к процессу.';
CREATE INDEX process_task_process_id_idx ON process_task (process_id);
CREATE INDEX process_task_task_id_idx ON process_task (task_id);

--process_template_event
CREATE TABLE process_template_event
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    process_template_id BIGINT                              NOT NULL,
    event_task_type_id  BIGINT                              NOT NULL,
    CONSTRAINT process_template_event_event_task_type_id_fkey FOREIGN KEY (event_task_type_id) REFERENCES task_type (id),
    CONSTRAINT process_template_event_process_template_id_fkey FOREIGN KEY (process_template_id) REFERENCES process_template (id)
);
CREATE INDEX process_template_event_process_template_id_idx ON process_template_event (process_template_id);
CREATE INDEX process_template_event_event_task_type_id_idx ON process_template_event (event_task_type_id);

--process_template_member_root_component
CREATE TABLE process_template_member_root_component
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    process_template_id BIGINT                              NOT NULL,
    child_component_id  BIGINT                              NOT NULL,
    CONSTRAINT process_template_member_root_component_cc_id_fkey FOREIGN KEY (child_component_id) REFERENCES component (id),
    CONSTRAINT process_template_member_root_component_pt_id_fkey FOREIGN KEY (process_template_id) REFERENCES process_template (id)
);
CREATE INDEX process_template_member_root_component_pt_id_idx ON process_template_member_root_component (process_template_id);
CREATE INDEX process_template_member_root_component_cc_id_idx ON process_template_member_root_component (child_component_id);

--process_type_stage_type
CREATE TABLE process_type_stage_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    process_type_code     VARCHAR(128)                        NOT NULL,
    stage_type_code       VARCHAR(128)                        NOT NULL,
    index                 BIGINT                              NOT NULL,
    is_system             BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT process_type_stage_type_process_type_code_index_key UNIQUE (process_type_code, index),
    CONSTRAINT process_type_stage_type_process_type_code_fkey FOREIGN KEY (process_type_code) REFERENCES process_type (code),
    CONSTRAINT process_type_stage_type_stage_type_code_fkey FOREIGN KEY (stage_type_code) REFERENCES stage_type (code)
);
COMMENT ON TABLE process_type_stage_type IS 'В этой таблице хранятся шаблонные значения этапов процесса, которые подставляются как значения по умолчанию при создании процесса.';
COMMENT ON COLUMN process_type_stage_type.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN process_type_stage_type.process_type_code IS 'Код типа процесса';
COMMENT ON COLUMN process_type_stage_type.stage_type_code IS 'Код этапа процесса';
COMMENT ON COLUMN process_type_stage_type.index IS 'Индекс';
COMMENT ON COLUMN process_type_stage_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN process_type_stage_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX process_type_stage_type_process_type_code_idx ON process_type_stage_type (process_type_code);
CREATE INDEX process_type_stage_type_stage_type_code_idx ON process_type_stage_type (stage_type_code);

--process_type_status_group_status
CREATE TABLE process_type_status_group_status
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                    TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                      TIMESTAMP,
    process_type_status_group_id BIGINT                              NOT NULL,
    status_id                    BIGINT                              NOT NULL,
    external_id                  VARCHAR(128) UNIQUE,
    author_employee_id           BIGINT                              NOT NULL,
    update_employee_id           BIGINT                              NOT NULL,
    update_date                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system                    BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system        BOOLEAN   DEFAULT FALSE             NOT NULL,
    index                        integer,
    CONSTRAINT process_type_status_group_status_ptsg_id_fkey FOREIGN KEY (process_type_status_group_id) REFERENCES process_type_status_group (id),
    CONSTRAINT process_type_status_group_status_status_id_fkey FOREIGN KEY (status_id) REFERENCES status (id)
);
COMMENT ON COLUMN process_type_status_group_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN process_type_status_group_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN process_type_status_group_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN process_type_status_group_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN process_type_status_group_status.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN process_type_status_group_status.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX process_type_status_group_status_ptsg_id_idx ON process_type_status_group_status (process_type_status_group_id);
CREATE INDEX process_type_status_group_status_status_id_idx ON process_type_status_group_status (status_id);

--role_calculation_configuration
CREATE TABLE role_calculation_configuration
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    role_id             BIGINT                              NOT NULL,
    type_calculation_id BIGINT                              NOT NULL,
    parameter_name      VARCHAR(256)                        NOT NULL,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    CONSTRAINT role_calculation_configuration_type_calculation_id_fkey FOREIGN KEY (type_calculation_id) REFERENCES type_calculation (id),
    CONSTRAINT role_calculation_configuration_role_id_fkey FOREIGN KEY (role_id) REFERENCES role (id)
);
CREATE INDEX role_calculation_configuration_role_id_idx ON role_calculation_configuration (role_id);
CREATE INDEX role_calculation_configuration_type_calculation_id_idx ON role_calculation_configuration (type_calculation_id);

--task_type_role
CREATE TABLE task_type_role
(
    id                        BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    task_type_id              BIGINT                                  NOT NULL,
    role_id                   BIGINT                                  NOT NULL,
    name                      VARCHAR(256)                            NOT NULL,
    description               VARCHAR(1024) DEFAULT ''::VARCHAR,
    index                     BIGINT        DEFAULT 0                 NOT NULL,
    count_min_suggested       BIGINT        DEFAULT 0,
    count_max_suggested       BIGINT        DEFAULT 0,
    count_recommend_suggested BIGINT        DEFAULT 0,
    is_auto_assign            BOOLEAN       DEFAULT FALSE             NOT NULL,
    external_id               VARCHAR(128) UNIQUE,
    date_from                 TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                   TIMESTAMP,
    author_employee_id        BIGINT                                  NOT NULL,
    update_employee_id        BIGINT                                  NOT NULL,
    update_date               TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system                 BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system     BOOLEAN       DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT task_type_role_task_type_id_fkey FOREIGN KEY (task_type_id) REFERENCES task_type (id)
);
COMMENT ON TABLE task_type_role IS 'Типовые роли бизнес-объектов';
COMMENT ON COLUMN task_type_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN task_type_role.task_type_id IS 'Уникальный идентификатор типа таска';
COMMENT ON COLUMN task_type_role.role_id IS 'Уникальный идентификатор роли';
COMMENT ON COLUMN task_type_role.name IS 'Наименование типовой роли';
COMMENT ON COLUMN task_type_role.description IS 'Описание типовой роли';
COMMENT ON COLUMN task_type_role.index IS 'Порядок следования типовых ролей';
COMMENT ON COLUMN task_type_role.count_min_suggested IS 'Предлагаемое минимальное кол-во пользователей с типовой ролью';
COMMENT ON COLUMN task_type_role.count_max_suggested IS 'Предлагаемое максимальное кол-во пользователей с типовой ролью';
COMMENT ON COLUMN task_type_role.count_recommend_suggested IS 'Предлагаемое рекомендованное кол-во пользователей с типовой ролью';
COMMENT ON COLUMN task_type_role.is_auto_assign IS 'Признак автоматического назначения типовой роли';
COMMENT ON COLUMN task_type_role.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_type_role.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_role.date_to IS 'Дата и время удаления записи';
COMMENT ON COLUMN task_type_role.author_employee_id IS 'Уникальный идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_type_role.update_employee_id IS 'Уникальный идентификатор пользователя, который последним обновил запись';
COMMENT ON COLUMN task_type_role.update_date IS 'Дата и время последнего обновления записи';
COMMENT ON COLUMN task_type_role.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_role.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_role_role_id_idx ON task_type_role (role_id);
CREATE INDEX task_type_role_task_type_id_idx ON task_type_role (task_type_id);
CREATE UNIQUE INDEX task_type_role_key ON task_type_role (task_type_id, index) WHERE (date_to IS NULL);

--role_priority
CREATE TABLE role_priority
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code                  VARCHAR(16)                         NOT NULL,
    task_type_role_id     BIGINT                              NOT NULL,
    index                 BIGINT  DEFAULT 1                   NOT NULL,
    is_system             BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT role_priority_code_task_type_role_id_index_key UNIQUE (code, task_type_role_id, index),
    CONSTRAINT role_priority_task_type_role_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id)
);
COMMENT ON TABLE role_priority IS 'Приоритет ролей';
COMMENT ON COLUMN role_priority.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN role_priority.code IS 'Код модуля';
COMMENT ON COLUMN role_priority.task_type_role_id IS 'Тип роли, разрешающей работу с типом таска';
COMMENT ON COLUMN role_priority.index IS 'Индекс приоритета';
COMMENT ON COLUMN role_priority.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN role_priority.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX role_priority_task_type_role_id_idx ON role_priority (task_type_role_id);

--status_change_rule
CREATE TABLE status_change_rule
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY    NOT NULL PRIMARY KEY,
    task_type_role_id     BIGINT                                 NOT NULL,
    status_id_from        BIGINT,
    status_id_to          BIGINT                                 NOT NULL,
    is_commentable        BOOLEAN      DEFAULT FALSE             NOT NULL,
    is_comment_required   BOOLEAN      DEFAULT FALSE             NOT NULL,
    description           VARCHAR(512) DEFAULT NULL::VARCHAR,
    name                  VARCHAR(256),
    date_execute          TIMESTAMP,
    date_from             TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    is_system             BOOLEAN      DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN      DEFAULT FALSE             NOT NULL,
    CONSTRAINT status_change_rule_status_id_from_fkey FOREIGN KEY (status_id_from) REFERENCES status (id),
    CONSTRAINT status_change_rule_status_id_to_fkey FOREIGN KEY (status_id_to) REFERENCES status (id),
    CONSTRAINT status_change_rule_task_type_role_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id)
);
COMMENT ON TABLE status_change_rule IS 'Правила смены статусов';
COMMENT ON COLUMN status_change_rule.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN status_change_rule.task_type_role_id IS 'Тип роли разрешающий работу с типом таска';
COMMENT ON COLUMN status_change_rule.status_id_from IS 'Текущий статус';
COMMENT ON COLUMN status_change_rule.status_id_to IS 'Следующий статус';
COMMENT ON COLUMN status_change_rule.is_commentable IS 'Признак возможности комментирования перехода';
COMMENT ON COLUMN status_change_rule.is_comment_required IS 'Признак обязательности комментария.';
COMMENT ON COLUMN status_change_rule.description IS 'Описание';
COMMENT ON COLUMN status_change_rule.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN status_change_rule.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN status_change_rule.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN status_change_rule.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE UNIQUE INDEX status_change_rule_key ON status_change_rule (task_type_role_id, status_id_from, status_id_to) WHERE date_to IS NULL;
CREATE INDEX status_change_rule_task_type_role_id_idx ON status_change_rule (task_type_role_id);
CREATE INDEX status_change_rule_status_id_from_idx ON status_change_rule (status_id_from);
CREATE INDEX status_change_rule_status_id_to_idx ON status_change_rule (status_id_to);

--task_link_type
CREATE TABLE task_link_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                  VARCHAR(64) UNIQUE                  NOT NULL,
    code                  VARCHAR UNIQUE                      NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL
);
COMMENT ON TABLE task_link_type IS 'Типы связей бизнес-объектов';
COMMENT ON COLUMN task_link_type.id IS 'Уникальный идентификационный номер типа связи.';
COMMENT ON COLUMN task_link_type.name IS 'Наименование типа связи.';
COMMENT ON COLUMN task_link_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_link_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN task_link_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN task_link_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_link_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_link_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN task_link_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_link_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--status_change_rule_cascade
CREATE TABLE status_change_rule_cascade
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_link_type_code           VARCHAR(128)                        NOT NULL,
    status_change_rule_id_initial BIGINT                              NOT NULL,
    status_change_rule_id_cascade BIGINT                              NOT NULL,
    is_mandatory                  BOOLEAN   DEFAULT FALSE             NOT NULL,
    date_from                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                       TIMESTAMP,
    author_employee_id            BIGINT,
    update_employee_id            BIGINT,
    update_date                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system                     BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system         BOOLEAN   DEFAULT FALSE             NOT NULL,
    external_id                   VARCHAR(128) UNIQUE,
    is_initial_task_id_from       BOOLEAN   DEFAULT TRUE              NOT NULL,
    CONSTRAINT status_change_rule_casca_status_change_rule_id_cascad_fkey FOREIGN KEY (status_change_rule_id_cascade) REFERENCES status_change_rule (id),
    CONSTRAINT status_change_rule_casca_status_change_rule_id_initia_fkey FOREIGN KEY (status_change_rule_id_initial) REFERENCES status_change_rule (id),
    CONSTRAINT status_change_rule_cascade_task_link_type_code_fkey FOREIGN KEY (task_link_type_code) REFERENCES task_link_type (code)
);
COMMENT ON TABLE status_change_rule_cascade IS 'Таблица для каскадного обновления статусов';
COMMENT ON COLUMN status_change_rule_cascade.id IS 'Уникальный идентификационный номер связи.';
COMMENT ON COLUMN status_change_rule_cascade.task_link_type_code IS 'Код связи между тасками, требующими каскадного обновления.';
COMMENT ON COLUMN status_change_rule_cascade.status_change_rule_id_initial IS 'Идентификатор первичного изменения статуса, за которым следует каскад.';
COMMENT ON COLUMN status_change_rule_cascade.status_change_rule_id_cascade IS 'Идентификатор статусного перехода, который необходимо совершить у связанных тасков.';
COMMENT ON COLUMN status_change_rule_cascade.is_mandatory IS 'Флаг обязательности выполнения всей транзакции статусных переходов.';
COMMENT ON COLUMN status_change_rule_cascade.date_from IS 'Дата и время создания связи.';
COMMENT ON COLUMN status_change_rule_cascade.date_to IS 'Дата и время удаления связи.';
COMMENT ON COLUMN status_change_rule_cascade.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN status_change_rule_cascade.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN status_change_rule_cascade.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN status_change_rule_cascade.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN status_change_rule_cascade.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN status_change_rule_cascade.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN status_change_rule_cascade.is_initial_task_id_from IS 'Флаг, отвечающий за направление каскадного обновления статусов тасков (true = от task_id_from к task_id_to; false = от task_id_to к task_id_from).';
CREATE INDEX status_change_rule_cascade_task_link_type_code_idx ON status_change_rule_cascade (task_link_type_code);
CREATE INDEX status_change_rule_casca_status_change_rule_id_initial_idx ON status_change_rule_cascade (status_change_rule_id_initial);
CREATE INDEX status_change_rule_casca_status_change_rule_id_cascade_idx ON status_change_rule_cascade (status_change_rule_id_cascade);

--status_change_rule_condition_group
CREATE TABLE status_change_rule_condition_group
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    status_change_rule_id BIGINT                                  NOT NULL,
    condition_group_id    BIGINT                                  NOT NULL,
    name                  VARCHAR(256)                            NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    is_mandatory          BOOLEAN       DEFAULT FALSE             NOT NULL,
    index                 BIGINT        DEFAULT 0                 NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE             NOT NULL,
    CONSTRAINT status_change_rule_condition_group_cg_id_fkey FOREIGN KEY (condition_group_id) REFERENCES condition_group (id),
    CONSTRAINT status_change_rule_condition_group_scr_id_fkey FOREIGN KEY (status_change_rule_id) REFERENCES status_change_rule (id)
);
COMMENT ON COLUMN status_change_rule_condition_group.index IS 'Порядковый номер группы. По отображению и по порядку следованию проверок.';
COMMENT ON COLUMN status_change_rule_condition_group.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN status_change_rule_condition_group.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX status_change_rule_condition_group_scr_id_idx ON status_change_rule_condition_group (status_change_rule_id);
CREATE INDEX status_change_rule_condition_group_cg_id_idx ON status_change_rule_condition_group (condition_group_id);

--status_change_rule_field_update
CREATE TABLE status_change_rule_field_update
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    status_change_rule_id BIGINT                              NOT NULL,
    task_type_field_id    BIGINT                              NOT NULL,
    value                 VARCHAR(1024)                       NOT NULL,
    CONSTRAINT status_change_rule_field_update_scr_id_fkey FOREIGN KEY (status_change_rule_id) REFERENCES status_change_rule (id),
    CONSTRAINT status_change_rule_field_update_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
COMMENT ON TABLE status_change_rule_field_update IS 'Правила создания task_field при переводе статуса task';
COMMENT ON COLUMN status_change_rule_field_update.id IS 'Синтетический ID';
COMMENT ON COLUMN status_change_rule_field_update.date_from IS 'Системная дата начала действия записи';
COMMENT ON COLUMN status_change_rule_field_update.date_to IS 'Системная дата окончания действия записи';
COMMENT ON COLUMN status_change_rule_field_update.status_change_rule_id IS 'ID правила смены статуса';
COMMENT ON COLUMN status_change_rule_field_update.task_type_field_id IS 'ID типа поля бизнес-объекта';
COMMENT ON COLUMN status_change_rule_field_update.value IS 'Устанавливаемое значение поля';
CREATE INDEX status_change_rule_field_update_scr_id_idx ON status_change_rule_field_update (status_change_rule_id);
CREATE INDEX status_change_rule_field_update_ttf_id_idx ON status_change_rule_field_update (task_type_field_id);

--task_history
CREATE TABLE task_history
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    comment            VARCHAR(512),
    date               TIMESTAMP DEFAULT NOW()             NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    status_id          BIGINT,
    task_id            BIGINT,
    parent_id          BIGINT,
    task_type_role_id  BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT task_history_status_id_fkey FOREIGN KEY (status_id) REFERENCES status (id),
    CONSTRAINT task_history_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT task_history_task_type_role_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id)
);
COMMENT ON TABLE task_history IS 'Данная таблица содержит каталог истории статусных переходов тасков.';
COMMENT ON COLUMN task_history.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN task_history.comment IS 'Комментарий, указанный при переходе таска в какой-либо статус.';
COMMENT ON COLUMN task_history.date IS 'Дата и время перехода таска в какой-либо статус.';
COMMENT ON COLUMN task_history.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_history.status_id IS 'Статус, в который была переведена таск. Ссылается на таблицу status.';
COMMENT ON COLUMN task_history.task_id IS 'Уникальный идентификационный номер таска. Ссылается на таблицу task.';
COMMENT ON COLUMN task_history.parent_id IS 'Родитель таска.';
COMMENT ON COLUMN task_history.task_type_role_id IS 'Уникальный идентификационный номер типа роли доступа к таскам';
COMMENT ON COLUMN task_history.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_history.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN task_history.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN task_history.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_history.update_date IS 'Дата обновления записи';
CREATE INDEX task_history_status_id_idx ON task_history (status_id);
CREATE INDEX task_history_task_id_idx ON task_history (task_id);
CREATE INDEX task_history_parent_id_idx ON task_history (parent_id);
CREATE INDEX task_history_task_type_role_id_idx_idx ON task_history (task_type_role_id);

--task_link
CREATE TABLE task_link
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    task_id_from       BIGINT,
    task_link_type_id  BIGINT,
    task_id_to         BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT task_link_task_id_from_fkey FOREIGN KEY (task_id_from) REFERENCES task (id),
    CONSTRAINT task_link_task_id_to_fkey FOREIGN KEY (task_id_to) REFERENCES task (id),
    CONSTRAINT task_link_task_link_type_id_fkey FOREIGN KEY (task_link_type_id) REFERENCES task_link_type (id)
);
COMMENT ON TABLE task_link IS 'Данная таблица содержит связи тасков через тип связи.';
COMMENT ON COLUMN task_link.id IS 'Уникальный идентификационный номер связи.';
COMMENT ON COLUMN task_link.date_from IS 'Дата и время создания связи.';
COMMENT ON COLUMN task_link.date_to IS 'Дата и время удаления связи.';
COMMENT ON COLUMN task_link.task_id_from IS 'Ссылка на идентификационный номер таблицы task, таска от которой создаётся связь.';
COMMENT ON COLUMN task_link.task_link_type_id IS 'Тип связи. Ссылается на таблицу task_link_type.';
COMMENT ON COLUMN task_link.task_id_to IS 'Ссылка на идентификационный номер таблицы task, таска к которой создаётся связь.';
COMMENT ON COLUMN task_link.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN task_link.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_link.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN task_link.update_date IS 'Дата обновления записи';
CREATE INDEX task_link_task_id_from_idx ON task_link (task_id_from);
CREATE INDEX task_link_task_id_to_idx ON task_link (task_id_to);
CREATE INDEX task_link_task_link_type_id_idx ON task_link (task_link_type_id);

--task_role_assignment
CREATE TABLE task_role_assignment
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from         TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to           TIMESTAMP,
    employee_id       BIGINT                              NOT NULL,
    task_type_role_id BIGINT                              NOT NULL,
    task_id           BIGINT                              NOT NULL,
    CONSTRAINT task_role_assignment_task_id_fkey FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT task_role_assignment_task_type_role_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id)
);
COMMENT ON TABLE task_role_assignment IS 'Данная таблица для фиксирования назначений ролей сотрудников для конкретных тасков.';
COMMENT ON COLUMN task_role_assignment.id IS 'Уникальный идентификационный номер.';
COMMENT ON COLUMN task_role_assignment.date_from IS 'Дата и время создания.';
COMMENT ON COLUMN task_role_assignment.date_to IS 'Дата и время удаления.';
COMMENT ON COLUMN task_role_assignment.employee_id IS 'Уникальный идентификационный номер сотрудника (из Orgstructure)';
COMMENT ON COLUMN task_role_assignment.task_type_role_id IS 'Тип роли, разрешающей-- работу с типом таска';
COMMENT ON COLUMN task_role_assignment.task_id IS 'Уникальный идентификационный номер таска';
CREATE INDEX task_role_assignment_task_type_role_id_idx ON task_role_assignment (task_type_role_id);
CREATE INDEX task_role_assignment_task_id_idx ON task_role_assignment (task_id);

--type_dictionary_entity
CREATE TABLE type_dictionary_entity
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    name                            VARCHAR(256)                             NOT NULL,
    description                     VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    type_service_id                 BIGINT,
    dictionary_service_endpoint_uri VARCHAR(512)                             NOT NULL,
    key_basic_data_type_code        VARCHAR(128)                             NOT NULL,
    key_field_name                  VARCHAR(256)                             NOT NULL,
    value_basic_data_type_code      VARCHAR(128)                             NOT NULL,
    value_field_name                VARCHAR(256)                             NOT NULL,
    is_system                       BOOLEAN       DEFAULT FALSE              NOT NULL,
    is_editable_if_system           BOOLEAN       DEFAULT FALSE              NOT NULL,
    ignore_value_field              BOOLEAN       DEFAULT FALSE              NOT NULL,
    unit_code                       VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT type_dictionary_entity_key_basic_data_type_code_fkey FOREIGN KEY (key_basic_data_type_code) REFERENCES basic_data_type (code),
    CONSTRAINT type_dictionary_entity_type_service_id_fkey FOREIGN KEY (type_service_id) REFERENCES type_service (id),
    CONSTRAINT type_dictionary_entity_value_basic_data_type_code_fkey FOREIGN KEY (value_basic_data_type_code) REFERENCES basic_data_type (code)
);
COMMENT ON TABLE type_dictionary_entity IS 'Каталог справочников';
COMMENT ON COLUMN type_dictionary_entity.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN type_dictionary_entity.name IS 'Наименование';
COMMENT ON COLUMN type_dictionary_entity.description IS 'Описание';
COMMENT ON COLUMN type_dictionary_entity.type_service_id IS 'Если NULL - локальный справочник.
Локальный справочник - это сервис с обращением к собственному АПИ, который по наименованию справочника вернет его содержимое.
Наполнение и настройка локального справочника выполняется в отдельном интерфейсе.';
COMMENT ON COLUMN type_dictionary_entity.dictionary_service_endpoint_uri IS 'Эндпоинт для вызова метода';
COMMENT ON COLUMN type_dictionary_entity.key_basic_data_type_code IS 'Код типа поля-ключа';
COMMENT ON COLUMN type_dictionary_entity.key_field_name IS 'Наименования поля-ключа';
COMMENT ON COLUMN type_dictionary_entity.value_basic_data_type_code IS 'Код типа значения';
COMMENT ON COLUMN type_dictionary_entity.value_field_name IS 'Наименование поля-значения';
COMMENT ON COLUMN type_dictionary_entity.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN type_dictionary_entity.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN type_dictionary_entity.ignore_value_field IS 'Не включать в ответ поле, указанное в колонке value_field_name';
COMMENT ON COLUMN type_dictionary_entity.unit_code IS 'Код юнита';
CREATE INDEX type_dictionary_entity_type_service_id_idx ON type_dictionary_entity (type_service_id);
CREATE INDEX type_dictionary_entity_key_basic_data_type_code_idx ON type_dictionary_entity (key_basic_data_type_code);
CREATE INDEX type_dictionary_entity_value_basic_data_type_code_idx ON type_dictionary_entity (value_basic_data_type_code);

--task_type_field_assignment_configuration
CREATE TABLE task_type_field_assignment_configuration
(
    task_type_field_id        BIGINT PRIMARY KEY    NOT NULL,
    task_type_role_id         BIGINT                NOT NULL,
    dictionary_entity_id_list BIGINT,
    dictionary_entity_id_info BIGINT,
    is_multiselect            BOOLEAN DEFAULT FALSE NOT NULL,
    is_readonly               BOOLEAN DEFAULT FALSE NOT NULL,
    is_system                 BOOLEAN DEFAULT FALSE NOT NULL,
    is_editable_if_system     BOOLEAN DEFAULT FALSE NOT NULL,
    sort_field_name           VARCHAR(256),
    sort_order                VARCHAR(16),
    CONSTRAINT task_type_field_assignment_configuration_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id),
    CONSTRAINT task_type_field_assignment_configuration_ttr_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id),
    CONSTRAINT task_type_field_assignment_configuration_de_id_list_fkey FOREIGN KEY (dictionary_entity_id_list) REFERENCES type_dictionary_entity (id),
    CONSTRAINT task_type_field_assignment_configuration_de_id_info_fkey FOREIGN KEY (dictionary_entity_id_info) REFERENCES type_dictionary_entity (id)
);
COMMENT ON COLUMN task_type_field_assignment_configuration.sort_field_name IS 'Наименование поля справочника для сортировки';
COMMENT ON COLUMN task_type_field_assignment_configuration.sort_order IS 'Порядок сортировки';
CREATE INDEX task_type_field_assignment_configuration_ttr_id_idx ON task_type_field_assignment_configuration (task_type_role_id);
CREATE INDEX task_type_field_assignment_configuration_del_idx ON task_type_field_assignment_configuration (dictionary_entity_id_list);
CREATE INDEX task_type_field_assignment_configuration_dei_idx ON task_type_field_assignment_configuration (dictionary_entity_id_info);

--type_parameter_value_type
CREATE TABLE type_parameter_value_type
(
    code        VARCHAR(128) PRIMARY KEY          NOT NULL,
    "name"      VARCHAR(256) UNIQUE               NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL
);

--task_type_field_calculation_configuration
CREATE TABLE task_type_field_calculation_configuration
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_type_field_id             BIGINT                              NOT NULL,
    type_calculation_id            BIGINT                              NOT NULL,
    parameter_name                 VARCHAR(256)                        NOT NULL,
    type_parameter_value_type_code VARCHAR(128)                        NOT NULL,
    date_from                      TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                        TIMESTAMP,
    CONSTRAINT task_type_field_calculation_configuration_dfp_code_fkey FOREIGN KEY (type_parameter_value_type_code) REFERENCES type_parameter_value_type (code),
    CONSTRAINT task_type_field_calculation_configuration_tc_id_fkey FOREIGN KEY (type_calculation_id) REFERENCES type_calculation (id),
    CONSTRAINT task_type_field_calculation_configuration_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
CREATE INDEX task_type_field_calculation_configuration_tpvt_code_idx ON task_type_field_calculation_configuration (type_parameter_value_type_code);
CREATE INDEX task_type_field_calculation_configuration_ttf_id_idx ON task_type_field_calculation_configuration (task_type_field_id);
CREATE INDEX task_type_field_calculation_configuration_tc_id_idx ON task_type_field_calculation_configuration (type_calculation_id);

--type_date_behavior_kind
CREATE TABLE type_date_behavior_kind
(
    code        VARCHAR(128) PRIMARY KEY          NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL
);
COMMENT ON TABLE type_date_behavior_kind IS 'Вид даты, определяющий ее поведение.';

--type_date_display_kind
CREATE TABLE type_date_display_kind
(
    code        VARCHAR(128) PRIMARY KEY          NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL
);
COMMENT ON TABLE type_date_display_kind IS 'Вид даты, определяющий ее отображение.';

--task_type_field_date_configuration
CREATE TABLE task_type_field_date_configuration
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY                           NOT NULL PRIMARY KEY, -- TODO update entity
    date_from               TIMESTAMP   DEFAULT CURRENT_TIMESTAMP                         NOT NULL,
    date_to                 TIMESTAMP,
    task_type_field_id      BIGINT                                                        NOT NULL,
    date_display_kind_code  VARCHAR(128)                                                  NOT NULL,
    date_behavior_kind_code VARCHAR(128)                                                  NOT NULL,
    basic_date_format       VARCHAR(64) DEFAULT 'yyyy-MM-dd''T''HH:mm:ss.SSSXXX'::VARCHAR NOT NULL,
    CONSTRAINT task_type_field_date_configuration_dbk_code_fkey FOREIGN KEY (date_behavior_kind_code) REFERENCES type_date_behavior_kind (code),
    CONSTRAINT task_type_field_date_config_date_display_kind_code_fkey FOREIGN KEY (date_display_kind_code) REFERENCES type_date_display_kind (code),
    CONSTRAINT task_type_field_date_configuration_task_type_field_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
CREATE INDEX task_type_field_date_config_date_behavior_kind_code_idx ON task_type_field_date_configuration (date_behavior_kind_code);
CREATE INDEX task_type_field_date_config_date_display_kind_code_idx ON task_type_field_date_configuration (date_display_kind_code);
CREATE INDEX task_type_field_date_configuration_ttf_id_idx ON task_type_field_date_configuration (task_type_field_id);

--type_dictionary_field_position
CREATE TABLE type_dictionary_field_position
(
    code                  VARCHAR(128) PRIMARY KEY          NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE       NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE       NOT NULL
);
COMMENT ON TABLE type_dictionary_field_position IS 'Типы расположения значений справочника';
COMMENT ON COLUMN type_dictionary_field_position.code IS 'Уникальный код';
COMMENT ON COLUMN type_dictionary_field_position.description IS 'Описание';
COMMENT ON COLUMN type_dictionary_field_position.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN type_dictionary_field_position.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--task_type_field_dictionary_column
CREATE TABLE task_type_field_dictionary_column
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_type_field_id             BIGINT                              NOT NULL,
    dictionary_field_title         VARCHAR(256)                        NOT NULL,
    dictionary_field_name          VARCHAR(256)                        NOT NULL,
    dictionary_field_position_code VARCHAR(128)                        NOT NULL,
    is_enabled                     BOOLEAN   DEFAULT TRUE              NOT NULL,
    index                          BIGINT    DEFAULT 0                 NOT NULL,
    date_from                      TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                        TIMESTAMP,
    is_system                      BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system          BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_field_dictionary_column_dfp_code_fkey FOREIGN KEY (dictionary_field_position_code) REFERENCES type_dictionary_field_position (code),
    CONSTRAINT task_type_field_dictionary_column_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
COMMENT ON TABLE task_type_field_dictionary_column IS 'Данная таблица содержит информацию о столбцах данных кастомных справочников.';
COMMENT ON COLUMN task_type_field_dictionary_column.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN task_type_field_dictionary_column.task_type_field_id IS 'Уникальный идентификационный номер типа поля';
COMMENT ON COLUMN task_type_field_dictionary_column.dictionary_field_title IS 'Наименование поля справочника';
COMMENT ON COLUMN task_type_field_dictionary_column.dictionary_field_name IS 'Наименование возвращаемого параметра';
COMMENT ON COLUMN task_type_field_dictionary_column.dictionary_field_position_code IS 'Код позиции поля';
COMMENT ON COLUMN task_type_field_dictionary_column.is_enabled IS 'Признак включения';
COMMENT ON COLUMN task_type_field_dictionary_column.index IS 'Индекс';
COMMENT ON COLUMN task_type_field_dictionary_column.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_field_dictionary_column.date_to IS 'Дата и время удаления записи. Если запись активна - значение null.';
COMMENT ON COLUMN task_type_field_dictionary_column.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_dictionary_column.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_field_dictionary_column_dfp_code_idx ON task_type_field_dictionary_column (dictionary_field_position_code);
CREATE INDEX task_type_field_dictionary_column_ttf_id_idx ON task_type_field_dictionary_column (task_type_field_id);

--task_type_field_dictionary_configuration
CREATE TABLE task_type_field_dictionary_configuration
(
    task_type_field_id        BIGINT PRIMARY KEY    NOT NULL,
    dictionary_entity_id_list BIGINT                NOT NULL,
    dictionary_entity_id_info BIGINT                NOT NULL,
    is_multiselect            BOOLEAN DEFAULT FALSE NOT NULL,
    is_readonly               BOOLEAN DEFAULT FALSE NOT NULL,
    is_system                 BOOLEAN DEFAULT FALSE NOT NULL,
    is_editable_if_system     BOOLEAN DEFAULT FALSE NOT NULL,
    sort_field_name           VARCHAR(256),
    sort_order                VARCHAR(16),
    CONSTRAINT task_type_field_dictionary_configuration_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id),
    CONSTRAINT task_type_field_dictionary_configuration_de_id_info_fkey FOREIGN KEY (dictionary_entity_id_info) REFERENCES type_dictionary_entity (id),
    CONSTRAINT task_type_field_dictionary_configuration_de_id_list_fkey FOREIGN KEY (dictionary_entity_id_list) REFERENCES type_dictionary_entity (id)
);
COMMENT ON TABLE task_type_field_dictionary_configuration IS 'Конфигурация справочников и полей бизнес-объектов';
COMMENT ON COLUMN task_type_field_dictionary_configuration.task_type_field_id IS 'Уникальный идентификационный номер типа поля';
COMMENT ON COLUMN task_type_field_dictionary_configuration.dictionary_entity_id_list IS 'Уникальный идентификационный номер сущности справочника для вывода списка значений';
COMMENT ON COLUMN task_type_field_dictionary_configuration.dictionary_entity_id_info IS 'Уникальный идентификационный номер сущности справочника для вывода значения';
COMMENT ON COLUMN task_type_field_dictionary_configuration.is_multiselect IS 'Признак множественного выбора';
COMMENT ON COLUMN task_type_field_dictionary_configuration.is_readonly IS 'Признак справочника только для чтения';
COMMENT ON COLUMN task_type_field_dictionary_configuration.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_dictionary_configuration.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN task_type_field_dictionary_configuration.sort_field_name IS 'Наименование поля справочника для сортировки';
COMMENT ON COLUMN task_type_field_dictionary_configuration.sort_order IS 'Порядок сортировки';
CREATE INDEX task_type_field_dictionary_configuration_de_id_info_idx ON task_type_field_dictionary_configuration (dictionary_entity_id_info);
CREATE INDEX task_type_field_dictionary_configuration_de_id_list_idx ON task_type_field_dictionary_configuration (dictionary_entity_id_list);

--task_type_field_dictionary_list_parameter
CREATE TABLE task_type_field_dictionary_list_parameter
(
    id                                       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_type_field_id                       BIGINT                              NOT NULL,
    query_parameter_name                     VARCHAR(128)                        NOT NULL,
    query_parameter_value_task_type_field_id BIGINT                              NOT NULL,
    is_skippable_on_null                     BOOLEAN   DEFAULT TRUE              NOT NULL,
    date_from                                TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                                  TIMESTAMP,
    query_parameter_value_task_type_id       BIGINT,
    is_system                                BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system                    BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_field_dictionary_list_parameter_qpttf_ttf_id_fkey FOREIGN KEY (query_parameter_value_task_type_field_id) REFERENCES task_type_field (id),
    CONSTRAINT task_type_field_dictionary_list_parameter_ttfdp_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field_dictionary_configuration (task_type_field_id),
    CONSTRAINT ttf_dictionary_list_parameter_task_type_id_fkey FOREIGN KEY (query_parameter_value_task_type_id) REFERENCES task_type (id)
);
COMMENT ON TABLE task_type_field_dictionary_list_parameter IS 'Связь справочников и полей';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.task_type_field_id IS 'Уникальный идентификационный номер типа поля';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.query_parameter_name IS 'Наименование параметра для фильтрации ответа справочника';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.query_parameter_value_task_type_field_id IS 'Уникальный идентификационный номер типа поля, в котором хранится значение для фильтрации';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.is_skippable_on_null IS 'Не отправлять параметр, если значение в указанном поле null. В противном случае, отправлять в качестве значения null.';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.date_to IS 'Дата и время удаления записи. Если запись активна - значение null.';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_dictionary_list_parameter.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_field_dictionary_list_parameter_ttfdp_ttf_id_idx ON task_type_field_dictionary_list_parameter (task_type_field_id);
CREATE INDEX task_type_field_dictionary_list_parameter_qpttf_ttf_id_idx ON task_type_field_dictionary_list_parameter (query_parameter_value_task_type_field_id);
CREATE INDEX task_type_field_dictionary_list_parameter_qpvttid_idx ON task_type_field_dictionary_list_parameter (query_parameter_value_task_type_id);

--task_type_field_float_type
CREATE TABLE task_type_field_float_type
(
    code                  VARCHAR(128) PRIMARY KEY          NOT NULL,
    name                  VARCHAR(256) UNIQUE               NOT NULL,
    description           VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL,
    is_system             BOOLEAN       DEFAULT FALSE       NOT NULL,
    is_editable_if_system BOOLEAN       DEFAULT FALSE       NOT NULL
);
COMMENT ON TABLE task_type_field_float_type IS 'Тип дробных значений';
COMMENT ON COLUMN task_type_field_float_type.code IS 'Уникальный код типа значения поля';
COMMENT ON COLUMN task_type_field_float_type.name IS 'Наименование';
COMMENT ON COLUMN task_type_field_float_type.description IS 'Описание';
COMMENT ON COLUMN task_type_field_float_type.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_float_type.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';

--task_type_field_float_configuration
CREATE TABLE task_type_field_float_configuration
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    task_type_field_id              BIGINT                                   NOT NULL,
    task_type_field_float_type_code VARCHAR(128)                             NOT NULL,
    value_min                       numeric(32, 4) DEFAULT NULL::numeric,
    value_max                       numeric(32, 4) DEFAULT NULL::numeric,
    is_unsigned                     BOOLEAN        DEFAULT FALSE             NOT NULL,
    date_from                       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                         TIMESTAMP,
    is_system                       BOOLEAN        DEFAULT FALSE             NOT NULL,
    is_editable_if_system           BOOLEAN        DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_field_float_configuration_ttf_float_type_code_fkey FOREIGN KEY (task_type_field_float_type_code) REFERENCES task_type_field_float_type (code),
    CONSTRAINT task_type_field_float_configuration_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
COMMENT ON TABLE task_type_field_float_configuration IS 'Настройка полей с дробным значением';
COMMENT ON COLUMN task_type_field_float_configuration.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN task_type_field_float_configuration.task_type_field_id IS 'Уникальный идентификационный номер типа поля';
COMMENT ON COLUMN task_type_field_float_configuration.task_type_field_float_type_code IS 'Уникальный код типа значения поля';
COMMENT ON COLUMN task_type_field_float_configuration.value_min IS 'Минимально возможное значения';
COMMENT ON COLUMN task_type_field_float_configuration.value_max IS 'Максимально возможное значение';
COMMENT ON COLUMN task_type_field_float_configuration.is_unsigned IS 'Признак положительного значения (беззнаковое)';
COMMENT ON COLUMN task_type_field_float_configuration.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_field_float_configuration.date_to IS 'Дата и время прекращения действия записи';
COMMENT ON COLUMN task_type_field_float_configuration.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_field_float_configuration.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_field_float_configuration_ttfid_idx ON task_type_field_float_configuration (task_type_field_id);
CREATE INDEX task_type_field_float_configuration_ttfft_code_idx ON task_type_field_float_configuration (task_type_field_float_type_code);

--task_type_field_integer_type
CREATE TABLE task_type_field_integer_type
(
    code        VARCHAR(128) PRIMARY KEY          NOT NULL,
    name        VARCHAR(256)                      NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL
);

--task_type_field_integer_configuration
CREATE TABLE task_type_field_integer_configuration
(
    id                                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO: update entity and fk
    date_from                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                           TIMESTAMP,
    task_type_field_id                BIGINT                              NOT NULL,
    task_type_field_integer_type_code VARCHAR(128)                        NOT NULL,
    value_min                         integer,
    value_max                         integer,
    is_unsigned                       BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_field_integer_configuration_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id),
    CONSTRAINT task_type_field_integer_configuration_ttrit_code_fkey FOREIGN KEY (task_type_field_integer_type_code) REFERENCES task_type_field_integer_type (code) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE INDEX task_type_field_integer_configuration_ttf_id_idx ON task_type_field_integer_configuration (task_type_field_id);
CREATE INDEX task_type_field_integer_configuration_ttrit_code_idx ON task_type_field_integer_configuration (task_type_field_integer_type_code);

--task_type_field_string_type
CREATE TABLE task_type_field_string_type
(
    code        VARCHAR(128) PRIMARY KEY          NOT NULL,
    name        VARCHAR(256)                      NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR NOT NULL
);
COMMENT ON TABLE task_type_field_string_type IS 'Тип строковых значений';
COMMENT ON COLUMN task_type_field_string_type.code IS 'Уникальный код типа значения поля';
COMMENT ON COLUMN task_type_field_string_type.name IS 'Наименование';
COMMENT ON COLUMN task_type_field_string_type.description IS 'Описание';

--task_type_field_string_configuration
CREATE TABLE task_type_field_string_configuration
(
    id                               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                          TIMESTAMP,
    task_type_field_id               BIGINT                              NOT NULL,
    task_type_field_string_type_code VARCHAR(128)                        NOT NULL,
    min_length                       integer,
    max_length                       integer,
    CONSTRAINT task_type_field_string_c_task_type_field_string_type_fkey FOREIGN KEY (task_type_field_string_type_code) REFERENCES task_type_field_string_type (code),
    CONSTRAINT task_type_field_string_configurati_task_type_field_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id)
);
COMMENT ON TABLE task_type_field_string_configuration IS 'Настройка полей со строковым значением';
COMMENT ON COLUMN task_type_field_string_configuration.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN task_type_field_string_configuration.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_field_string_configuration.date_to IS 'Дата и время прекращения действия записи';
COMMENT ON COLUMN task_type_field_string_configuration.task_type_field_id IS 'Уникальный идентификатор типа поля';
COMMENT ON COLUMN task_type_field_string_configuration.task_type_field_string_type_code IS 'Уникальный код типа значения поля';
COMMENT ON COLUMN task_type_field_string_configuration.min_length IS 'Минимальная длина строки';
COMMENT ON COLUMN task_type_field_string_configuration.max_length IS 'Максимальная длина строки';
CREATE INDEX task_type_field_string_configuration_ttfstc_idx ON task_type_field_string_configuration (task_type_field_string_type_code);
CREATE INDEX task_type_field_string_configuration_ttfid_idx ON task_type_field_string_configuration (task_type_field_id);

--task_type_parent_permission
CREATE TABLE task_type_parent_permission
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_type_id_parent   BIGINT                              NOT NULL,
    task_type_id_child    BIGINT                              NOT NULL,
    is_system             BOOLEAN DEFAULT FALSE               NOT NULL,
    is_editable_if_system BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT task_type_parent_permission_task_type_id_child_fkey FOREIGN KEY (task_type_id_child) REFERENCES task_type (id),
    CONSTRAINT task_type_parent_permission_task_type_id_parent_fkey FOREIGN KEY (task_type_id_parent) REFERENCES task_type (id)
);
COMMENT ON TABLE task_type_parent_permission IS 'Связь типов бизнес-объектов';
COMMENT ON COLUMN task_type_parent_permission.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN task_type_parent_permission.task_type_id_parent IS 'Уникальный идентификатор родительского типа таска';
COMMENT ON COLUMN task_type_parent_permission.task_type_id_child IS 'Уникальный идентификатор дочернего типа таска';
COMMENT ON COLUMN task_type_parent_permission.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_parent_permission.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_parent_permission_task_type_id_parent_idx ON task_type_parent_permission (task_type_id_parent);
CREATE INDEX task_type_parent_permission_task_type_id_child_idx ON task_type_parent_permission (task_type_id_child);

--task_type_role_permission
CREATE TABLE task_type_role_permission
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    is_readable                BOOLEAN       DEFAULT TRUE          NOT NULL,
    is_writeable               BOOLEAN       DEFAULT TRUE          NOT NULL,
    task_type_role_id          BIGINT                              NOT NULL,
    status_id                  BIGINT,
    comment                    VARCHAR(1024) DEFAULT NULL::VARCHAR,
    target_parent_task_type_id BIGINT,
    target_parent_status_id    BIGINT,
    is_creatable               BOOLEAN       DEFAULT TRUE          NOT NULL,
    is_system                  BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_editable_if_system      BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_deletable               BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT task_type_role_permission_status_id_fkey FOREIGN KEY (status_id) REFERENCES status (id),
    CONSTRAINT task_type_role_permission_target_parent_status_id_fkey FOREIGN KEY (target_parent_status_id) REFERENCES status (id),
    CONSTRAINT task_type_role_permission_target_parent_task_type_id_fkey FOREIGN KEY (target_parent_task_type_id) REFERENCES task_type (id),
    CONSTRAINT task_type_role_permission_task_type_role_id_fkey FOREIGN KEY (task_type_role_id) REFERENCES task_type_role (id)
);
COMMENT ON TABLE task_type_role_permission IS 'Разрешение на редактирование и чтение бизнес-объектов';
COMMENT ON COLUMN task_type_role_permission.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN task_type_role_permission.is_readable IS 'Флаг записи.';
COMMENT ON COLUMN task_type_role_permission.is_writeable IS 'Флаг чтения.';
COMMENT ON COLUMN task_type_role_permission.task_type_role_id IS 'Тип роли, разрешающей работу с типом таска';
COMMENT ON COLUMN task_type_role_permission.status_id IS 'Уникальный идентификатор статуса';
COMMENT ON COLUMN task_type_role_permission.comment IS 'Комментарии';
COMMENT ON COLUMN task_type_role_permission.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_role_permission.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE UNIQUE INDEX idx_ttrp_role_status_parent_type_parent_status_key ON task_type_role_permission (task_type_role_id,
                                                                                                    status_id,
                                                                                                    target_parent_task_type_id,
                                                                                                    target_parent_status_id) NULLS NOT DISTINCT;
CREATE INDEX idx_task_type_role_permission_status_id ON task_type_role_permission (status_id);
CREATE INDEX idx_task_type_role_permission_task_type_role_id ON task_type_role_permission (task_type_role_id);
CREATE INDEX idx_task_type_role_permission_target_parent_task_type_id ON task_type_role_permission (target_parent_task_type_id);
CREATE INDEX idx_task_type_role_permission_target_parent_status_id ON task_type_role_permission (target_parent_status_id);

--task_type_role_permission_field_restriction
CREATE TABLE task_type_role_permission_field_restriction
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    task_type_role_permission_id BIGINT                              NOT NULL,
    task_type_field_id           BIGINT                              NOT NULL,
    is_read_restricted           BOOLEAN       DEFAULT TRUE          NOT NULL,
    is_write_restricted          BOOLEAN       DEFAULT TRUE          NOT NULL,
    comment                      VARCHAR(1024) DEFAULT NULL::VARCHAR,
    is_system                    BOOLEAN       DEFAULT FALSE         NOT NULL,
    is_editable_if_system        BOOLEAN       DEFAULT FALSE         NOT NULL,
    CONSTRAINT task_type_role_permission_field_restriction_ttf_id_fkey FOREIGN KEY (task_type_field_id) REFERENCES task_type_field (id),
    CONSTRAINT task_type_role_permission_field_restriction_ttrp_id_fkey FOREIGN KEY (task_type_role_permission_id) REFERENCES task_type_role_permission (id)
);
COMMENT ON TABLE task_type_role_permission_field_restriction IS 'Запрет на редактирование и чтение полей';
COMMENT ON COLUMN task_type_role_permission_field_restriction.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN task_type_role_permission_field_restriction.task_type_role_permission_id IS 'Уникальный идентификационный номер разрешения на редактирование и чтение для типов тасков';
COMMENT ON COLUMN task_type_role_permission_field_restriction.task_type_field_id IS 'Уникальный идентификационный номер типа поля таска';
COMMENT ON COLUMN task_type_role_permission_field_restriction.is_read_restricted IS 'Признак запрета на чтение';
COMMENT ON COLUMN task_type_role_permission_field_restriction.is_write_restricted IS 'Признак запрета на запись';
COMMENT ON COLUMN task_type_role_permission_field_restriction.comment IS 'Комментарии';
COMMENT ON COLUMN task_type_role_permission_field_restriction.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_role_permission_field_restriction.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_role_permission_field_restriction_ttrp_id_idx ON task_type_role_permission_field_restriction (task_type_role_permission_id);
CREATE INDEX task_type_role_permission_field_restriction_ttf_id_idx ON task_type_role_permission_field_restriction (task_type_field_id);

--task_type_status_dependency
CREATE TABLE task_type_status_dependency
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    task_type_id_master   BIGINT                              NOT NULL,
    task_type_id_slave    BIGINT                              NOT NULL,
    status_id_master      BIGINT                              NOT NULL,
    status_id_slave       BIGINT                              NOT NULL,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    CONSTRAINT task_type_status_dependency_status_id_master_fkey FOREIGN KEY (status_id_master) REFERENCES status (id),
    CONSTRAINT task_type_status_dependency_status_id_slave_fkey FOREIGN KEY (status_id_slave) REFERENCES status (id),
    CONSTRAINT task_type_status_dependency_task_type_id_master_fkey FOREIGN KEY (task_type_id_master) REFERENCES task_type (id),
    CONSTRAINT task_type_status_dependency_task_type_id_slave_fkey FOREIGN KEY (task_type_id_slave) REFERENCES task_type (id)
);
COMMENT ON COLUMN task_type_status_dependency.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN task_type_status_dependency.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
CREATE INDEX task_type_status_dependency_task_type_id_master_idx ON task_type_status_dependency (task_type_id_master);
CREATE INDEX task_type_status_dependency_task_type_id_slave_idx ON task_type_status_dependency (task_type_id_slave);
CREATE INDEX task_type_status_dependency_status_id_master_idx ON task_type_status_dependency (status_id_master);
CREATE INDEX task_type_status_dependency_status_id_slave_idx ON task_type_status_dependency (status_id_slave);

-- task_type_link_permission
CREATE TABLE IF NOT EXISTS task_type_link_permission
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date_from          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_to            TIMESTAMP,
    task_type_id_from  BIGINT    NOT NULL REFERENCES task_type (id) ON UPDATE CASCADE ON DELETE CASCADE,
    task_type_id_to    BIGINT    NOT NULL REFERENCES task_type (id) ON UPDATE CASCADE ON DELETE CASCADE,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT    NOT NULL,
    update_employee_id BIGINT    NOT NULL,
    update_date        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE task_type_link_permission IS 'Cвязи таск тайпов допустимых связей линков между таск тайпами';
COMMENT ON COLUMN task_type_link_permission.id IS 'Уникальный идентификатор записи ';
COMMENT ON COLUMN task_type_link_permission.date_from IS 'Дата и время создания записи';
COMMENT ON COLUMN task_type_link_permission.date_to IS 'Дата и время удаления записи';
COMMENT ON COLUMN task_type_link_permission.task_type_id_from IS 'Идентификатор таск тайпа слева';
COMMENT ON COLUMN task_type_link_permission.task_type_id_to IS 'Идентификатор таск тайпа справа';
COMMENT ON COLUMN task_type_link_permission.external_id IS 'Внешний уникальный идентификатор записи';
COMMENT ON COLUMN task_type_link_permission.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN task_type_link_permission.update_employee_id IS 'Идентификатор пользователя, который последним обновил запись';
COMMENT ON COLUMN task_type_link_permission.update_date IS 'Дата и время последнего обновления записи';

CREATE INDEX task_type_link_permission_task_type_id_from_idx ON task_type_link_permission (task_type_id_from);
CREATE INDEX task_type_link_permission_task_type_id_to_idx ON task_type_link_permission (task_type_id_to);
