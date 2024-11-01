--liquibase formatted sql
--changeset s_podrezov:init-common-schema

-- input_source
CREATE TABLE input_source
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP
);
COMMENT ON TABLE input_source IS 'Источник данных';
COMMENT ON COLUMN input_source.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN input_source.name IS 'Название источника внесения объекта';
COMMENT ON COLUMN input_source.date_from IS 'Дата и время создания источника внесения объекта';
COMMENT ON COLUMN input_source.date_to IS 'Дата и время удаления источника внесения объекта. Если источник активен, то значение null';

-- competence_catalog
CREATE TABLE competence_catalog
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    description            VARCHAR(1024)                       NOT NULL,
    name                   VARCHAR(128)                        NOT NULL,
    params                 VARCHAR(1024)                       NOT NULL,
    parent_id              BIGINT,
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    premoderation_required boolean   DEFAULT FALSE             NOT NULL,
    indicator_required     boolean   DEFAULT FALSE             NOT NULL,
    external_id            VARCHAR(128),
    is_editable_if_system  boolean   DEFAULT FALSE             NOT NULL,
    is_system              boolean   DEFAULT FALSE             NOT NULL,
    unit_code              VARCHAR(128)                        NOT NULL,
    CONSTRAINT "competence_catalog_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES competence_catalog (id)
);
COMMENT ON TABLE competence_catalog IS 'Каталог компетенций';
COMMENT ON COLUMN competence_catalog.id IS 'Уникальный идентификационный номер каталога компетенций';
COMMENT ON COLUMN competence_catalog.description IS 'Текстовое описание каталога компетенций';
COMMENT ON COLUMN competence_catalog.name IS 'Название каталога компетенций';
COMMENT ON COLUMN competence_catalog.params IS 'Параметры компетенций в каталоге. На текущий момент выводится только цвет компетенции. Значения записываются в формате {"color":"#FFFFFF"}';
COMMENT ON COLUMN competence_catalog.parent_id IS 'Ссылка на идентификационный номер той же таблицы Competence_catalog, родительского каталога компетенций';
COMMENT ON COLUMN competence_catalog.date_from IS 'Дата и время создания каталога компетенций';
COMMENT ON COLUMN competence_catalog.date_to IS 'Дата и время удаления каталога компетенций. Если каталог активен, то значение null';
COMMENT ON COLUMN competence_catalog.premoderation_required IS 'Требуется премодерация';
COMMENT ON COLUMN competence_catalog.indicator_required IS 'Требуется индикатор';
COMMENT ON COLUMN competence_catalog.external_id IS 'Внешний код записи';
COMMENT ON COLUMN competence_catalog.is_editable_if_system IS 'Признак изменяемости системной записи (true - запись системная и изменяемая, иначе - false)';
COMMENT ON COLUMN competence_catalog.is_system IS 'Признак указывающий на основание создания блока (true - блок поставляется вместе с системой, false- создан на проекте)';
COMMENT ON COLUMN competence_catalog.unit_code IS 'Код юнита';
CREATE INDEX competence_catalog_parent_id_idx ON competence_catalog (parent_id);

-- competence_type
CREATE TABLE competence_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP
);
COMMENT ON TABLE competence_type IS 'Типы компетенций';
COMMENT ON COLUMN competence_type.id IS 'Уникальный идентификационный номер типа компетенций';
COMMENT ON COLUMN competence_type.name IS 'Название типа компетенций';
COMMENT ON COLUMN competence_type.date_from IS 'Дата и время создания типа компетенций';
COMMENT ON COLUMN competence_type.date_to IS 'Дата и время удаления типа компетенций. Если тип активен, то значение null';

-- competence_algorithm
CREATE TABLE competence_algorithm
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(256) UNIQUE                 NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL
);
COMMENT ON TABLE competence_algorithm IS 'Алгоритм расчета уровня компетенции';
COMMENT ON COLUMN competence_algorithm.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_algorithm.name IS 'Наименование';
COMMENT ON COLUMN competence_algorithm.description IS 'Описание';

-- scale_type
CREATE TABLE scale_type
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from               TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to                 TIMESTAMP,
    level_count             smallint                            NOT NULL,
    name                    VARCHAR(256)                        NOT NULL,
    competence_algorithm_id BIGINT        DEFAULT 1             NOT NULL,
    description             VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    unit_code               VARCHAR(128)                        NOT NULL,
    CONSTRAINT "scale_type_competence_algorithm_id_fk" FOREIGN KEY (competence_algorithm_id) REFERENCES competence_algorithm (id)
);
COMMENT ON TABLE scale_type IS 'Типы шкал оценки';
COMMENT ON COLUMN scale_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN scale_type.date_from IS 'Дата и время создания шкалы';
COMMENT ON COLUMN scale_type.date_to IS 'Дата и время удаления шкалы. Если шкала активна, то значение null';
COMMENT ON COLUMN scale_type.level_count IS 'Количество уровней в шкале';
COMMENT ON COLUMN scale_type.name IS 'Название шкалы';
COMMENT ON COLUMN scale_type.competence_algorithm_id IS 'Ссылка на идентификатор competence_algorithm, алгоритм, используемый при расчете уровня владения компетенцией для данной шкалы';
COMMENT ON COLUMN scale_type.description IS 'Описание';
COMMENT ON COLUMN scale_type.unit_code IS 'Код юнита';
CREATE INDEX scale_type_competence_algorithm_id_idx ON scale_type (competence_algorithm_id);

-- competence
CREATE TABLE competence
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    author_employee_id     BIGINT                              NOT NULL,
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    description            VARCHAR(512)                        NOT NULL,
    name                   VARCHAR(128)                        NOT NULL,
    author_input_source_id BIGINT,
    competence_catalog_id  BIGINT,
    competence_type_id     BIGINT,
    scale_type_id          BIGINT,
    on_moderation          boolean   DEFAULT FALSE             NOT NULL,
    master_id              BIGINT,
    correct_id             BIGINT,
    external_id            VARCHAR(128),
    CONSTRAINT competence_name_competencecatalogid_datefrom_uq UNIQUE (name, competence_catalog_id, date_from),
    CONSTRAINT "competence_competence_type_id_fk" FOREIGN KEY (competence_type_id) REFERENCES competence_type (id),
    CONSTRAINT "competence_competence_catalog_id_fk" FOREIGN KEY (competence_catalog_id) REFERENCES competence_catalog (id),
    CONSTRAINT "competence_correct_id_fk" FOREIGN KEY (correct_id) REFERENCES competence (id),
    CONSTRAINT "competence_master_id_fk" FOREIGN KEY (master_id) REFERENCES competence (id),
    CONSTRAINT "competence_scale_type_id_fk" FOREIGN KEY (scale_type_id) REFERENCES scale_type (id),
    CONSTRAINT "competence_author_input_source_id_fk" FOREIGN KEY (author_input_source_id) REFERENCES input_source (id)
);
COMMENT ON TABLE competence IS 'Список компетенций';
COMMENT ON COLUMN competence.id IS 'Уникальный идентификационный номер компетенции';
COMMENT ON COLUMN competence.author_employee_id IS 'Ссылка на идентификационный номер сотрудника, создавшего компетецию (employee_id)';
COMMENT ON COLUMN competence.date_from IS 'Дата и время создания компетенции';
COMMENT ON COLUMN competence.date_to IS 'Дата и время удаления компетенции. Если компетенция активна, то значение null';
COMMENT ON COLUMN competence.description IS 'Описание компетенции';
COMMENT ON COLUMN competence.name IS 'Название компетенции';
COMMENT ON COLUMN competence.author_input_source_id IS 'Ссылка на идентификационный номер таблицы input_source, источника, создавшего компетенцию';
COMMENT ON COLUMN competence.competence_catalog_id IS 'Ссылка на идентификационный номер записи из таблицы competence_catalog, каталогу(типу) к которому относится компетенция';
COMMENT ON COLUMN competence.competence_type_id IS 'Ссылка на идентификационный номер записи из таблицы competence_type, типу  компетенции. На проектах на текущий момент всегда равен 1';
COMMENT ON COLUMN competence.scale_type_id IS 'Ссылка на scale_type, шкала компетенций';
COMMENT ON COLUMN competence.on_moderation IS 'На модерации';
COMMENT ON COLUMN competence.master_id IS 'Ссылка на competence, основная запись';
COMMENT ON COLUMN competence.correct_id IS 'Ссылка на competence, правильное значение';
COMMENT ON COLUMN competence.external_id IS 'Внешний код записи';
CREATE INDEX competence_competence_type_id_idx ON competence (competence_type_id);
CREATE INDEX competence_competence_catalog_id_idx ON competence (competence_catalog_id);
CREATE INDEX competence_correct_id_idx ON competence (correct_id);
CREATE INDEX competence_master_id_idx ON competence (master_id);
CREATE INDEX competence_scale_type_id_idx ON competence (scale_type_id);
CREATE INDEX competence_author_input_source_id_idx ON competence (author_input_source_id);

-- indicator_scale_template
CREATE TABLE indicator_scale_template
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP     DEFAULT NOW()              NOT NULL,
    date_to     TIMESTAMP,
    name        VARCHAR(256)                             NOT NULL,
    description VARCHAR(2048) DEFAULT ''::VARCHAR        NOT NULL,
    unit_code   VARCHAR(128)  DEFAULT 'default'::VARCHAR NOT NULL
);
COMMENT ON TABLE indicator_scale_template IS 'Шаблоны шкал индикаторов';
COMMENT ON COLUMN indicator_scale_template.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN indicator_scale_template.date_from IS 'Дата создания записи';
COMMENT ON COLUMN indicator_scale_template.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN indicator_scale_template.name IS 'Наименование';
COMMENT ON COLUMN indicator_scale_template.description IS 'Описание';
COMMENT ON COLUMN indicator_scale_template.unit_code IS 'Код юнита';

-- indicator
CREATE TABLE indicator
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                   TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to                     TIMESTAMP,
    description                 VARCHAR(2048)                       NOT NULL,
    author_employee_id          BIGINT                              NOT NULL,
    name                        VARCHAR(256)                        NOT NULL,
    competence_id               BIGINT,
    weight                      numeric(6, 2) DEFAULT 0             NOT NULL,
    indicator_scale_template_id BIGINT,
    is_short_view               boolean       DEFAULT FALSE         NOT NULL,
    CONSTRAINT "indicator_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id),
    CONSTRAINT "indicator_indicator_scale_template_id_fk" FOREIGN KEY (indicator_scale_template_id) REFERENCES indicator_scale_template (id)
);
COMMENT ON TABLE indicator IS 'Индикаторы компетенций';
COMMENT ON COLUMN indicator.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN indicator.date_from IS 'Дата и время создания индикатора';
COMMENT ON COLUMN indicator.date_to IS 'Дата и время удаления индикатора. Если индикатор активен, то значение null';
COMMENT ON COLUMN indicator.description IS 'Текстовое описание индикатора';
COMMENT ON COLUMN indicator.author_employee_id IS 'Ссылка на идентификационный номер работника, добавившего индикатор (employee_id)';
COMMENT ON COLUMN indicator.name IS 'Название индикатора';
COMMENT ON COLUMN indicator.competence_id IS 'Ссылка на идентификационный номер записи из таблицы competence, компетенции, к которой относится индикатор';
COMMENT ON COLUMN indicator.weight IS 'Вес';
COMMENT ON COLUMN indicator.indicator_scale_template_id IS 'Ссылка на идентификатор indicator_scale_template, типовые шкалы индикаторов компетенций';
CREATE INDEX indicator_indicator_competence_id_idx ON indicator (competence_id);
CREATE INDEX indicator_indicator_scale_template_id_idx ON indicator (indicator_scale_template_id);

-- competence_profile_cluster
CREATE TABLE competence_profile_cluster
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(128)                        NOT NULL,
    icon      VARCHAR(256),
    unit_code VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE competence_profile_cluster IS 'Кластеры профилей компетенций';
COMMENT ON COLUMN competence_profile_cluster.id IS 'Уникальный идентификационный номер кластера';
COMMENT ON COLUMN competence_profile_cluster.date_from IS 'Дата и время создания кластера';
COMMENT ON COLUMN competence_profile_cluster.date_to IS 'Дата и время удаления кластера. Если кластера активно, то значение null';
COMMENT ON COLUMN competence_profile_cluster.name IS 'Название кластера';
COMMENT ON COLUMN competence_profile_cluster.icon IS 'Название иконки (mdi-icon) для отображения в системе';
COMMENT ON COLUMN competence_profile_cluster.unit_code IS 'Код юнита';

-- competence_profile_family
CREATE TABLE competence_profile_family
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from TIMESTAMP    DEFAULT NOW()              NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(128)                            NOT NULL,
    parent_id BIGINT,
    unit_code VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT "competence_profile_family_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES competence_profile_family (id)
);
COMMENT ON TABLE competence_profile_family IS 'Семейства профилей компетенций';
COMMENT ON COLUMN competence_profile_family.id IS 'Уникальный идентификационный номер семейства';
COMMENT ON COLUMN competence_profile_family.date_from IS 'Дата и время создания семейства';
COMMENT ON COLUMN competence_profile_family.date_to IS 'Дата и время удаления семейства. Если семейство активно, то значение null';
COMMENT ON COLUMN competence_profile_family.name IS 'Название семейства';
COMMENT ON COLUMN competence_profile_family.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN competence_profile_family.unit_code IS 'Код юнита';
CREATE INDEX competence_profile_family_parent_id_idx ON competence_profile_family (parent_id);

-- competence_profile
CREATE TABLE competence_profile
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                      TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to                        TIMESTAMP,
    name                           VARCHAR(128)                        NOT NULL,
    description                    VARCHAR(512),
    competence_profile_family_id   BIGINT,
    competence_profile_cluster_id  BIGINT,
    commit_date                    TIMESTAMP,
    file                           VARCHAR(2048),
    filename                       VARCHAR(128) DEFAULT NULL::VARCHAR,
    education_level_id             BIGINT,
    work_duration_months_min       BIGINT,
    work_duration_months_recommend BIGINT,
    CONSTRAINT "competence_profile_competence_profile_cluster_id_fk" FOREIGN KEY (competence_profile_cluster_id) REFERENCES competence_profile_cluster (id),
    CONSTRAINT "competence_profile_competence_profile_family_id_fk" FOREIGN KEY (competence_profile_family_id) REFERENCES competence_profile_family (id)
);
COMMENT ON TABLE competence_profile IS 'Профили компетенций';
COMMENT ON COLUMN competence_profile.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile.date_from IS 'Дата и время создания профиля';
COMMENT ON COLUMN competence_profile.date_to IS 'Дата и время удаления профиля. Если профиль активен, то значение null';
COMMENT ON COLUMN competence_profile.name IS 'Название профиля';
COMMENT ON COLUMN competence_profile.description IS 'Текстовое описание профиля';
COMMENT ON COLUMN competence_profile.competence_profile_family_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile_family, семейства, к которому относится профиль';
COMMENT ON COLUMN competence_profile.competence_profile_cluster_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile_cluster, кластеру, к которому относится профиль';
COMMENT ON COLUMN competence_profile.commit_date IS 'Дата и время согласования профиля компетенций';
COMMENT ON COLUMN competence_profile.file IS 'Ссылка на прикрепленный файл к профилю компетенций';
COMMENT ON COLUMN competence_profile.filename IS 'Наименование файла';
COMMENT ON COLUMN competence_profile.education_level_id IS 'Id типа образования в таблице education_level в БД Learning';
COMMENT ON COLUMN competence_profile.work_duration_months_min IS 'Минимальный срок на должности (мес.)';
COMMENT ON COLUMN competence_profile.work_duration_months_recommend IS 'Рекомендованный срок на должности (мес.)';
CREATE INDEX competence_profile_competence_profile_cluster_id_idx ON competence_profile (competence_profile_cluster_id);
CREATE INDEX competence_profile_competence_profile_family_id_idx ON competence_profile (competence_profile_family_id);

-- scale_level
CREATE TABLE scale_level
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from     TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to       TIMESTAMP,
    level_point   numeric(6, 2)                       NOT NULL,
    name          VARCHAR(256)                        NOT NULL,
    scale_type_id BIGINT                              NOT NULL,
    description   VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    index         BIGINT        DEFAULT 0             NOT NULL,
    CONSTRAINT "scale_level_scale_type_id_fk" FOREIGN KEY (scale_type_id) REFERENCES scale_type (id)
);
COMMENT ON TABLE scale_level IS 'Уровни шкал оценки';
COMMENT ON COLUMN scale_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN scale_level.date_from IS 'Дата и время создания уровня';
COMMENT ON COLUMN scale_level.date_to IS 'Дата и время удаления уровня. Если уровень активна, то значение null';
COMMENT ON COLUMN scale_level.level_point IS 'Числовая оценка уровня (используется для расчета)';
COMMENT ON COLUMN scale_level.name IS 'Название уровня';
COMMENT ON COLUMN scale_level.scale_type_id IS 'Ссылка на идентификационный номер записи из таблицы scale_type, определяющая шкалу к которой прикреплен уровень';
COMMENT ON COLUMN scale_level.description IS 'Описание';
COMMENT ON COLUMN scale_level.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
CREATE INDEX scale_level_scale_type_id_idx ON scale_level (scale_type_id);

-- competence_profile_competence
CREATE TABLE competence_profile_competence
(
    competence_profile_id BIGINT                       NOT NULL,
    competence_id         BIGINT                       NOT NULL,
    scale_level_id        BIGINT,
    weight                double precision DEFAULT 1.0 NOT NULL,
    CONSTRAINT "competence_profile_competence_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id),
    CONSTRAINT "competence_profile_competence_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id),
    CONSTRAINT "competence_profile_competence_scale_level_id_fk" FOREIGN KEY (scale_level_id) REFERENCES scale_level (id)
);
COMMENT ON TABLE competence_profile_competence IS 'Связи компетенций с профилем компетенций';
COMMENT ON COLUMN competence_profile_competence.competence_profile_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile, профиля компетенций';
COMMENT ON COLUMN competence_profile_competence.competence_id IS 'Ссылка на идентификационный номер записи из таблицы competence, компетенции, которая привязана к профилю';
COMMENT ON COLUMN competence_profile_competence.scale_level_id IS 'Ссылка на идентификационный номер записи из таблицы scale_level, уровню шкалы, на котором должна находиться компетенция в профиле';
COMMENT ON COLUMN competence_profile_competence.weight IS 'Вес';
CREATE INDEX competence_profile_competence_competence_id_idx ON competence_profile_competence (competence_id);
CREATE INDEX competence_profile_competence_competence_profile_id_idx ON competence_profile_competence (competence_profile_id);
CREATE INDEX competence_profile_competence_scale_level_id_idx ON competence_profile_competence (scale_level_id);

-- competence_profile_position
CREATE TABLE competence_profile_position
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    commit_date           TIMESTAMP,
    competence_profile_id BIGINT                              NOT NULL,
    position_id           BIGINT                              NOT NULL,
    date_inclusion        TIMESTAMP,
    date_exclusion        TIMESTAMP,
    CONSTRAINT "competence_profile_position_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id)
);
COMMENT ON TABLE competence_profile_position IS 'Связи должностей с профилем компетенций';
COMMENT ON COLUMN competence_profile_position.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile_position.date_from IS 'Дата создания записи';
COMMENT ON COLUMN competence_profile_position.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN competence_profile_position.competence_profile_id IS 'Ссылка на идентификатор competence_profile, профиль компетенций';
COMMENT ON COLUMN competence_profile_position.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN competence_profile_position.date_inclusion IS 'Дата включения';
COMMENT ON COLUMN competence_profile_position.date_exclusion IS 'Дата исключения';
CREATE INDEX competence_profile_position_competence_profile_id_idx ON competence_profile_position (competence_profile_id);
CREATE INDEX competence_profile_position_position_id_idx ON competence_profile_position (position_id);

-- indicator_scale_template_level
CREATE TABLE indicator_scale_template_level
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                   TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to                     TIMESTAMP,
    indicator_scale_template_id BIGINT                              NOT NULL,
    name                        VARCHAR(256)                        NOT NULL,
    description                 VARCHAR(2048) DEFAULT ''::VARCHAR   NOT NULL,
    level_point                 numeric(6, 2) DEFAULT 0             NOT NULL,
    index                       BIGINT                              NOT NULL,
    is_exclude_indicator        boolean       DEFAULT FALSE         NOT NULL,
    is_comment_required         boolean       DEFAULT FALSE         NOT NULL,
    CONSTRAINT "indicator_scale_template_level_ist_id_fk" FOREIGN KEY (indicator_scale_template_id) REFERENCES indicator_scale_template (id)
);
COMMENT ON TABLE indicator_scale_template_level IS 'Уровни для шаблонов шкал индикаторов';
COMMENT ON COLUMN indicator_scale_template_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN indicator_scale_template_level.date_from IS 'Дата создания записи';
COMMENT ON COLUMN indicator_scale_template_level.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN indicator_scale_template_level.indicator_scale_template_id IS 'Ссылка на идентификатор indicator_scale_template, типовые шкалы индикаторов компетенций';
COMMENT ON COLUMN indicator_scale_template_level.name IS 'Наименование';
COMMENT ON COLUMN indicator_scale_template_level.description IS 'Описание';
COMMENT ON COLUMN indicator_scale_template_level.level_point IS 'Уровень баллов, которому соответствует индикатор для расчета средней оценки';
COMMENT ON COLUMN indicator_scale_template_level.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
COMMENT ON COLUMN indicator_scale_template_level.is_exclude_indicator IS 'Признак исключения индикатора из расчета средней оценки';
COMMENT ON COLUMN indicator_scale_template_level.is_comment_required IS 'Требуется комментарий (при установленном признаке поле комментарий нельзя оставить пустым, например при согласовании) (boolean)';
CREATE INDEX indicator_scale_template_level_ist_id_idx ON indicator_scale_template_level (indicator_scale_template_id);

-- indicator_level
CREATE TABLE indicator_level
(
    id                                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                         TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to                           TIMESTAMP,
    description                       VARCHAR(256)                        NOT NULL,
    author_employee_id                BIGINT                              NOT NULL,
    name                              VARCHAR(128)                        NOT NULL,
    indicator_id                      BIGINT,
    scale_level_id                    BIGINT,
    indicator_scale_template_level_id BIGINT,
    index                             BIGINT        DEFAULT 0             NOT NULL,
    level_point                       numeric(6, 2) DEFAULT 0             NOT NULL,
    is_comment_required               boolean       DEFAULT FALSE         NOT NULL,
    is_exclude_indicator              boolean       DEFAULT FALSE         NOT NULL,
    CONSTRAINT "indicator_level_indicator_id_fk" FOREIGN KEY (indicator_id) REFERENCES indicator (id),
    CONSTRAINT "indicator_level_scale_level_id_fk" FOREIGN KEY (scale_level_id) REFERENCES scale_level (id),
    CONSTRAINT "indicator_level_indicator_scale_template_level_id_fk" FOREIGN KEY (indicator_scale_template_level_id) REFERENCES indicator_scale_template_level (id)
);
COMMENT ON TABLE indicator_level IS 'Уровни индикаторов компетенций';
COMMENT ON COLUMN indicator_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN indicator_level.date_from IS 'Дата и время создания уровня индикатора';
COMMENT ON COLUMN indicator_level.date_to IS 'Дата и время удаления уровня индикатора. Если уровень активен, то значение null';
COMMENT ON COLUMN indicator_level.description IS 'Текстовое описание уровня индикатора';
COMMENT ON COLUMN indicator_level.author_employee_id IS 'Ссылка на идентификационный номер работника, добавившего уровень индикатора (employee_id)';
COMMENT ON COLUMN indicator_level.name IS 'Название уровня индикатора';
COMMENT ON COLUMN indicator_level.indicator_id IS 'Ссылка на идентификационный номер записи из таблицы indicator, индикатора, к которому относится уровень';
COMMENT ON COLUMN indicator_level.scale_level_id IS 'Ссылка на идентификационный номер записи из таблицы scale_level, уровня шкалы, с которым сопоставляется уровень индикатора';
COMMENT ON COLUMN indicator_level.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
COMMENT ON COLUMN indicator_level.is_comment_required IS 'Требуется комментарий (при установленном признаке поле комментарий нельзя оставить пустым, например при согласовании) (boolean)';
CREATE INDEX indicator_level_indicator_id_idx ON indicator_level (indicator_id);
CREATE INDEX indicator_level_scale_level_id_idx ON indicator_level (scale_level_id);
CREATE INDEX indicator_level_indicator_scale_template_level_id_idx ON indicator_level (indicator_scale_template_level_id);

-- development_plan_duration_monitoring
CREATE TABLE development_plan_duration_monitoring
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name       VARCHAR(256)                        NOT NULL,
    month      BIGINT,
    date_from  TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to    TIMESTAMP,
    is_default boolean   DEFAULT TRUE              NOT NULL,
    unit_code  VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE development_plan_duration_monitoring IS 'Периодичности мониторинга ИПР';
COMMENT ON COLUMN development_plan_duration_monitoring.id IS 'Уникальный идентификационный номер длительности периода';
COMMENT ON COLUMN development_plan_duration_monitoring.name IS 'Название периода длительности периода мониторинга';
COMMENT ON COLUMN development_plan_duration_monitoring.month IS 'Кол-во месяцев, которые длится один период мониторинга';
COMMENT ON COLUMN development_plan_duration_monitoring.date_from IS 'Дата и время создания типа форм развития';
COMMENT ON COLUMN development_plan_duration_monitoring.date_to IS 'Дата и время удаления типа форм развития. Если тип активен, то значение null';
COMMENT ON COLUMN development_plan_duration_monitoring.is_default IS 'Значение по умолчанию';

-- development_plan_inclusion_reason
CREATE TABLE development_plan_inclusion_reason
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(256)                        NOT NULL,
    description VARCHAR(256) DEFAULT NULL::VARCHAR,
    date_from   TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to     TIMESTAMP,
    unit_code   VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE development_plan_inclusion_reason IS 'Основания создания ИПР';
COMMENT ON COLUMN development_plan_inclusion_reason.id IS 'Уникальный идентификационный номер основания';
COMMENT ON COLUMN development_plan_inclusion_reason.name IS 'Название основания';
COMMENT ON COLUMN development_plan_inclusion_reason.description IS 'Описание основания';
COMMENT ON COLUMN development_plan_inclusion_reason.date_from IS 'Дата и время создания источника внесения объекта';
COMMENT ON COLUMN development_plan_inclusion_reason.date_to IS 'Дата и время удаления источника внесения объекта. Если источник активен, то значение null';

-- development_form_catalog_type
CREATE TABLE development_form_catalog_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code      BIGINT UNIQUE                       NOT NULL,
    date_from TIMESTAMP                           NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(128)
);
COMMENT ON TABLE development_form_catalog_type IS 'Типы каталогов форм развития';
COMMENT ON COLUMN development_form_catalog_type.id IS 'Уникальный идентификационный номер типа каталога форм развития';
COMMENT ON COLUMN development_form_catalog_type.code IS 'Внешний код типа каталога форм развития';
COMMENT ON COLUMN development_form_catalog_type.date_from IS 'Дата и время создания типа каталога форм развития';
COMMENT ON COLUMN development_form_catalog_type.date_to IS 'Дата и время удаления типа каталога форм развития. Если тип активен, то значение null';
COMMENT ON COLUMN development_form_catalog_type.name IS 'Название типа каталога форм развития';

-- development_form_catalog
CREATE TABLE development_form_catalog
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                          VARCHAR(128)                        NOT NULL,
    params                        VARCHAR(1024)                       NOT NULL,
    parent_id                     BIGINT,
    date_from                     TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                       TIMESTAMP,
    development_form_catalog_type BIGINT,
    estimation_criteria_id        BIGINT,
    index                         BIGINT,
    unit_code                     VARCHAR(128)                        NOT NULL,
    CONSTRAINT "development_form_catalog_development_form_catalog_type_fk" FOREIGN KEY (development_form_catalog_type) REFERENCES development_form_catalog_type (id),
    CONSTRAINT "development_form_catalog_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES development_form_catalog (id)
);
COMMENT ON TABLE development_form_catalog IS 'Каталог форм развития';
COMMENT ON COLUMN development_form_catalog.id IS 'Уникальный идентификационный номер каталога форм развития';
COMMENT ON COLUMN development_form_catalog.name IS 'Название каталога форм развития';
COMMENT ON COLUMN development_form_catalog.params IS 'Параметры формы развития в каталоге. На текущий момент выводится только цвет формы развития. Значения записываются в формате {"color":"#FFFFFF"}';
COMMENT ON COLUMN development_form_catalog.parent_id IS 'Ссылка на идентификационный номер той же таблицы Development_form_catalog, родительского каталога форм развития';
COMMENT ON COLUMN development_form_catalog.date_from IS 'Дата и время создания каталога форм развития';
COMMENT ON COLUMN development_form_catalog.date_to IS 'Дата и время удаления каталога форм развития. Если каталог активен, то значение null';
COMMENT ON COLUMN development_form_catalog.development_form_catalog_type IS 'Ссылка на идентификационный номер таблицы development_form_catalog_type, типа каталога форм развития';
COMMENT ON COLUMN development_form_catalog.estimation_criteria_id IS 'Ссылка на идентификационный номер таблицы estimation_criteria, направления развития';
COMMENT ON COLUMN development_form_catalog.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
CREATE INDEX development_form_catalog_development_form_catalog_type_idx ON development_form_catalog (development_form_catalog_type);
CREATE INDEX development_form_catalog_parent_id_idx ON development_form_catalog (parent_id);

-- development_form_type
CREATE TABLE development_form_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    unit_code VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE development_form_type IS 'Типы форм развития';
COMMENT ON COLUMN development_form_type.id IS 'Уникальный идентификационный номер типа форм развития';
COMMENT ON COLUMN development_form_type.name IS 'Название типа форм развития';
COMMENT ON COLUMN development_form_type.date_from IS 'Дата и время создания типа форм развития';
COMMENT ON COLUMN development_form_type.date_to IS 'Дата и время удаления типа форм развития. Если тип активен, то значение null';

-- development_form
CREATE TABLE development_form
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    description                 VARCHAR(512),
    name                        VARCHAR(128)                            NOT NULL,
    author_input_source_id      BIGINT                                  NOT NULL,
    development_form_catalog_id BIGINT,
    development_form_type_id    BIGINT                                  NOT NULL,
    date_from                   TIMESTAMP    DEFAULT NOW()              NOT NULL,
    date_to                     TIMESTAMP,
    is_plannable                boolean      DEFAULT TRUE               NOT NULL,
    is_achievable               boolean      DEFAULT TRUE               NOT NULL,
    estimation_criteria_id      BIGINT,
    competence_count_max        BIGINT       DEFAULT 2                  NOT NULL,
    author_employee_id          VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    unit_code                   VARCHAR(128)                            NOT NULL,
    CONSTRAINT "development_form_development_form_catalog_id_fk" FOREIGN KEY (development_form_catalog_id) REFERENCES development_form_catalog (id),
    CONSTRAINT "development_form_development_form_type_id_fk" FOREIGN KEY (development_form_type_id) REFERENCES development_form_type (id),
    CONSTRAINT "development_form_author_input_source_id_fk" FOREIGN KEY (author_input_source_id) REFERENCES input_source (id)
);
COMMENT ON TABLE development_form IS 'Формы развития';
COMMENT ON COLUMN development_form.id IS 'Уникальный идентификационный номер формы развития';
COMMENT ON COLUMN development_form.description IS 'Описание форма развития';
COMMENT ON COLUMN development_form.name IS 'Название формы развития';
COMMENT ON COLUMN development_form.author_input_source_id IS 'Ссылка на идентификационный номер таблицы input_source, источника, создавшего компетенцию';
COMMENT ON COLUMN development_form.development_form_catalog_id IS 'Ссылка на идентификационный номер таблицы development_form_catalog, каталога форм развития';
COMMENT ON COLUMN development_form.development_form_type_id IS 'Ссылка на идентификационный номер таблицы development_form_type, типа форм развития';
COMMENT ON COLUMN development_form.date_from IS 'Дата и время создания формы развития';
COMMENT ON COLUMN development_form.date_to IS 'Дата и время удаления формы развития. Если форма активна, то значение null';
COMMENT ON COLUMN development_form.estimation_criteria_id IS 'Ссылка на идентификационный номер таблицы estimation_criteria, направления развития';
COMMENT ON COLUMN development_form.competence_count_max IS 'Максимальное количество компетенций, которые можно развивать в рамках данной формы развития';
COMMENT ON COLUMN development_form.author_employee_id IS 'Ссылка на идентификационный номер сотрудника, создавшего форму развития (employee_id)';
CREATE INDEX development_form_development_form_catalog_id_idx ON development_form (development_form_catalog_id);
CREATE INDEX development_form_development_form_type_id_idx ON development_form (development_form_type_id);
CREATE INDEX development_form_author_input_source_id_idx ON development_form (author_input_source_id);

-- evaluation_role
CREATE TABLE evaluation_role
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE evaluation_role IS 'Роли оценщиков для мероприятий оценки';
COMMENT ON COLUMN evaluation_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN evaluation_role.date_from IS 'Дата и время создания роли';
COMMENT ON COLUMN evaluation_role.date_to IS 'Дата и время удаления роли. Если роль активна, то значение null';
COMMENT ON COLUMN evaluation_role.name IS 'Название роли оценщика';

-- appraisal_event_status
CREATE TABLE appraisal_event_status
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE appraisal_event_status IS 'Статусы мероприятий аттестации';
COMMENT ON COLUMN appraisal_event_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN appraisal_event_status.date_from IS 'Дата создания записи';
COMMENT ON COLUMN appraisal_event_status.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN appraisal_event_status.name IS 'Наименование';

-- appraisal_commission
CREATE TABLE appraisal_commission
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(512),
    division_id BIGINT,
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP
);
COMMENT ON TABLE appraisal_commission IS 'Аттестационные комиссии';
COMMENT ON COLUMN appraisal_commission.id IS 'Уникальный идентификационный номер комиссии';
COMMENT ON COLUMN appraisal_commission.name IS 'Название комиссии';
COMMENT ON COLUMN appraisal_commission.division_id IS 'Ссылка на идентификационный номер записи из таблицы orgstucture.division, подразделения, к которому прикреплена комиссия';
COMMENT ON COLUMN appraisal_commission.date_from IS 'Дата и время создания комиссии';
COMMENT ON COLUMN appraisal_commission.date_to IS 'Дата и время удаления комиссии. Если комиссия активна, то значение null';

-- appraisal_event
CREATE TABLE appraisal_event
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    is_appraisal_external      boolean      DEFAULT FALSE          NOT NULL,
    division_id                BIGINT,
    location                   VARCHAR(256) DEFAULT NULL::VARCHAR,
    name                       VARCHAR(256) DEFAULT NULL::VARCHAR,
    description                VARCHAR(512) DEFAULT NULL::VARCHAR,
    comment                    VARCHAR(512) DEFAULT NULL::VARCHAR,
    month                      integer,
    year                       integer,
    meeting_date               TIMESTAMP,
    appraisal_commission_id    BIGINT,
    date_from                  TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to                    TIMESTAMP,
    appraisal_event_status_id  BIGINT,
    process_id                 BIGINT,
    is_appraisal_extraordinary boolean      DEFAULT FALSE,
    CONSTRAINT "appraisal_event_appraisal_event_status_id_fk" FOREIGN KEY (appraisal_event_status_id) REFERENCES appraisal_event_status (id),
    CONSTRAINT "appraisal_event_appraisal_commission_id_fk" FOREIGN KEY (appraisal_commission_id) REFERENCES appraisal_commission (id)
);
COMMENT ON TABLE appraisal_event IS 'Список мероприятий аттестации с их атрибутами';
COMMENT ON COLUMN appraisal_event.id IS 'Уникальный идентификационный номер мероприятия';
COMMENT ON COLUMN appraisal_event.is_appraisal_external IS 'Флаг, указывающий является ли мероприятие внешним (true если внешнее, false если внутреннее)';
COMMENT ON COLUMN appraisal_event.division_id IS 'Ссылка на идентификационный номер записи из таблицы orgstucture.division, подразделения, участвующего в мероприятии';
COMMENT ON COLUMN appraisal_event.location IS 'Местоположение проведения заседания аттестации';
COMMENT ON COLUMN appraisal_event.name IS 'Название мероприятия';
COMMENT ON COLUMN appraisal_event.description IS 'Описание мероприятия';
COMMENT ON COLUMN appraisal_event.comment IS 'Комментарий к мероприятию аттестации';
COMMENT ON COLUMN appraisal_event.month IS 'Месяц проведения аттестации';
COMMENT ON COLUMN appraisal_event.year IS 'Год проведения аттестации';
COMMENT ON COLUMN appraisal_event.meeting_date IS 'Дата и время проведения заседания комиссии аттестации';
COMMENT ON COLUMN appraisal_event.appraisal_commission_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_commission, комиссию аттестации, участвующую в заседании';
COMMENT ON COLUMN appraisal_event.date_from IS 'Дата и время создания мероприятия';
COMMENT ON COLUMN appraisal_event.date_to IS 'Дата и время удаления мероприятия. Если мероприятие активно, то значение null';
COMMENT ON COLUMN appraisal_event.process_id IS 'Ссылка на идентификатор process, процесс';
COMMENT ON COLUMN appraisal_event.is_appraisal_extraordinary IS 'Вид аттестации';
CREATE INDEX appraisal_event_appraisal_event_status_id_idx ON appraisal_event (appraisal_event_status_id);
CREATE INDEX appraisal_event_appraisal_commission_id_idx ON appraisal_event (appraisal_commission_id);

-- appraisal_commission_role
CREATE TABLE appraisal_commission_role
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(256)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP
);
COMMENT ON TABLE appraisal_commission_role IS 'Роли участников комиссий аттестации';
COMMENT ON COLUMN appraisal_commission_role.id IS 'Уникальный идентификационный номер роли';
COMMENT ON COLUMN appraisal_commission_role.name IS 'Название роли';
COMMENT ON COLUMN appraisal_commission_role.date_from IS 'Дата и время создания роли';
COMMENT ON COLUMN appraisal_commission_role.date_to IS 'Дата и время удаления роли. Если роль актуальна, то значение null';

-- appraisal_commission_member
CREATE TABLE appraisal_commission_member
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    employee_id                  BIGINT,
    substitute_id                BIGINT,
    appraisal_commission_id      BIGINT,
    appraisal_commission_role_id BIGINT,
    date_from                    TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to                      TIMESTAMP,
    first_name                   VARCHAR(128) DEFAULT ''::VARCHAR    NOT NULL,
    last_name                    VARCHAR(128) DEFAULT ''::VARCHAR    NOT NULL,
    patronymic                   VARCHAR(128) DEFAULT ''::VARCHAR    NOT NULL,
    job_title                    VARCHAR(256) DEFAULT ''::VARCHAR    NOT NULL,
    key_member                   boolean      DEFAULT FALSE          NOT NULL,
    CONSTRAINT "appraisal_commission_member_appraisal_commission_id_fk" FOREIGN KEY (appraisal_commission_id) REFERENCES appraisal_commission (id),
    CONSTRAINT "appraisal_commission_member_appraisal_commission_role_id_fk" FOREIGN KEY (appraisal_commission_role_id) REFERENCES appraisal_commission_role (id),
    CONSTRAINT "appraisal_commission_member_substitute_id_fk" FOREIGN KEY (substitute_id) REFERENCES appraisal_commission_member (id)
);
COMMENT ON TABLE appraisal_commission_member IS 'Участники аттестационной комиссии';
COMMENT ON COLUMN appraisal_commission_member.id IS 'Уникальный идентификационный номер члена комиссии';
COMMENT ON COLUMN appraisal_commission_member.employee_id IS 'Ссылка на идентификационный номер сотрудника, являющегося членом комиссии (employee_id). Если член комиссии внешний, то null';
COMMENT ON COLUMN appraisal_commission_member.substitute_id IS 'Ссылка на идентификационный номер записи из той же таблицы appraisal_commission_member, члена комисии, который замещает текущего члена комисии';
COMMENT ON COLUMN appraisal_commission_member.appraisal_commission_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_commission, комисии аттестации';
COMMENT ON COLUMN appraisal_commission_member.appraisal_commission_role_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_commission_role, роли члена комиссии';
COMMENT ON COLUMN appraisal_commission_member.date_from IS 'Дата и время добавления члена комиссии';
COMMENT ON COLUMN appraisal_commission_member.date_to IS 'Дата и время удаления члена комиссии. Если член комиссии актуален, то значение null';
COMMENT ON COLUMN appraisal_commission_member.first_name IS 'Имя (если участник внешний)';
COMMENT ON COLUMN appraisal_commission_member.last_name IS 'Фамилия (если участник внешний)';
COMMENT ON COLUMN appraisal_commission_member.patronymic IS 'Отчетство (если участник внешний)';
COMMENT ON COLUMN appraisal_commission_member.job_title IS 'Должность (если участник внешний)';
CREATE INDEX appraisal_commission_member_appraisal_commission_id_idx ON appraisal_commission_member (appraisal_commission_id);
CREATE INDEX appraisal_commission_member_appraisal_commission_role_id_idx ON appraisal_commission_member (appraisal_commission_role_id);
CREATE INDEX appraisal_commission_member_substitute_id_idx ON appraisal_commission_member (substitute_id);

-- appraisal_commission_status
CREATE TABLE appraisal_commission_status
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to     TIMESTAMP,
    name        VARCHAR(128)                        NOT NULL,
    description VARCHAR(512) DEFAULT NULL::VARCHAR
);
COMMENT ON TABLE appraisal_commission_status IS 'Статусы комиссий аттестации';
COMMENT ON COLUMN appraisal_commission_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN appraisal_commission_status.date_from IS 'Дата создания записи';
COMMENT ON COLUMN appraisal_commission_status.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN appraisal_commission_status.name IS 'Наименование';
COMMENT ON COLUMN appraisal_commission_status.description IS 'Описание';

-- appraisal_event_commission_member
CREATE TABLE appraisal_event_commission_member
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    appraisal_commission_role_id  BIGINT,
    appraisal_event_id            BIGINT,
    is_attended                   boolean   DEFAULT FALSE             NOT NULL,
    date_from                     TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                       TIMESTAMP,
    appraisal_comission_member_id BIGINT,
    CONSTRAINT "appraisal_event_commission_member_appraisal_event_id_fk" FOREIGN KEY (appraisal_event_id) REFERENCES appraisal_event (id),
    CONSTRAINT "appraisal_event_commission_member_acr_id_fk" FOREIGN KEY (appraisal_commission_role_id) REFERENCES appraisal_commission_role (id)
);
COMMENT ON TABLE appraisal_event_commission_member IS 'Связи мероприятий аттестации и членов комиссий';
COMMENT ON COLUMN appraisal_event_commission_member.id IS 'Уникальный идентификационный номер связи';
COMMENT ON COLUMN appraisal_event_commission_member.appraisal_commission_role_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_commission_role, роли члена комиссии';
COMMENT ON COLUMN appraisal_event_commission_member.appraisal_event_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_event, мероприятия аттестации';
COMMENT ON COLUMN appraisal_event_commission_member.is_attended IS 'лаг присутствия члена комиссии на заседании аттестации';
COMMENT ON COLUMN appraisal_event_commission_member.date_from IS 'Дата и время добавления члена комиссии';
COMMENT ON COLUMN appraisal_event_commission_member.date_to IS 'Дата и время удаления члена комиссии. Если член комиссии актуален, то значение null';
CREATE INDEX appraisal_event_commission_member_appraisal_event_id_idx ON appraisal_event_commission_member (appraisal_event_id);
CREATE INDEX appraisal_event_commission_member_acr_id_idx ON appraisal_event_commission_member (appraisal_commission_role_id);

-- appraisal_recommendation_type
CREATE TABLE appraisal_recommendation_type
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(256),
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP,
    choice_type VARCHAR(128),
    index       integer
);
COMMENT ON TABLE appraisal_recommendation_type IS 'Типы рекомендаций в рамках аттестации';
COMMENT ON COLUMN appraisal_recommendation_type.id IS 'Уникальный идентификационный номер типа рекомендаций';
COMMENT ON COLUMN appraisal_recommendation_type.name IS 'Название типа рекомендации';
COMMENT ON COLUMN appraisal_recommendation_type.date_from IS 'Дата и время добавления типа';
COMMENT ON COLUMN appraisal_recommendation_type.date_to IS 'Дата и время удаления типа. Если тип актуален, то значение null';
COMMENT ON COLUMN appraisal_recommendation_type.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';

-- appraisal_recommendation
CREATE TABLE appraisal_recommendation
(
    id                               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                             VARCHAR(512),
    duration_year                    integer,
    appraisal_recommendation_type_id BIGINT,
    date_from                        TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                          TIMESTAMP,
    index                            integer,
    CONSTRAINT "appraisal_recommendation_appraisal_recommendation_t_id_fk" FOREIGN KEY (appraisal_recommendation_type_id) REFERENCES appraisal_recommendation_type (id)
);
COMMENT ON TABLE appraisal_recommendation IS 'Рекомендации в рамках аттестации';
COMMENT ON COLUMN appraisal_recommendation.id IS 'Уникальный идентификационный номер рекомендации';
COMMENT ON COLUMN appraisal_recommendation.name IS 'Название рекомендации';
COMMENT ON COLUMN appraisal_recommendation.duration_year IS 'Период следующей аттестации (для рекомендаций, содержащих период)';
COMMENT ON COLUMN appraisal_recommendation.appraisal_recommendation_type_id IS 'Ссылка на идентификационный номер записи из таблицы appraisal_recommendation_type, тип рекомендации';
COMMENT ON COLUMN appraisal_recommendation.date_from IS 'Дата и время добавления рекомендации';
COMMENT ON COLUMN appraisal_recommendation.date_to IS 'Дата и время удаления рекомендации. Если рекомендация актуальна, то значение null';
COMMENT ON COLUMN appraisal_recommendation.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
CREATE INDEX appraisal_recommendation_appraisal_recommendation_t_id_idx ON appraisal_recommendation (appraisal_recommendation_type_id);

-- company
CREATE TABLE company
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    name               VARCHAR(256)                        NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128)
);
COMMENT ON TABLE company IS 'Справочник компаний';
COMMENT ON COLUMN company.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN company.date_from IS 'Дата создания записи';
COMMENT ON COLUMN company.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN company.name IS 'Наименование компании';
COMMENT ON COLUMN company.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN company.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN company.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN company.external_id IS 'Внешний идентификатор';

-- competence_cluster
CREATE TABLE competence_cluster
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                     TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                       TIMESTAMP,
    competence_id                 BIGINT                              NOT NULL,
    competence_profile_cluster_id BIGINT                              NOT NULL,
    CONSTRAINT "competence_cluster_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id),
    CONSTRAINT "competence_cluster_competence_profile_cluster_id_fk" FOREIGN KEY (competence_profile_cluster_id) REFERENCES competence_profile_cluster (id)
);
COMMENT ON TABLE competence_cluster IS 'Связи компетенций с кластером профиля компетенций';
COMMENT ON COLUMN competence_cluster.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_cluster.date_from IS 'Дата создания записи';
COMMENT ON COLUMN competence_cluster.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN competence_cluster.competence_id IS 'Ссылка на идентификатор competence, справочник компетенций';
COMMENT ON COLUMN competence_cluster.competence_profile_cluster_id IS 'Ссылка на идентификатор competence_profile_cluste, справочник кластеров профилей компетенций';
CREATE INDEX competence_cluster_competence_id_idx ON competence_cluster (competence_id);
CREATE INDEX competence_cluster_competence_profile_cluster_id_idx ON competence_cluster (competence_profile_cluster_id);

-- competence_detalization
CREATE TABLE competence_detalization
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    parent_id BIGINT,
    child_id  BIGINT,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    CONSTRAINT "competence_detalization_child_id_fk" FOREIGN KEY (child_id) REFERENCES competence (id),
    CONSTRAINT "competence_detalization_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES competence (id)
);
COMMENT ON TABLE competence_detalization IS 'Связи компетенций с детализированными компетенциями';
COMMENT ON COLUMN competence_detalization.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_detalization.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN competence_detalization.child_id IS 'Дочерняя запись';
COMMENT ON COLUMN competence_detalization.date_from IS 'Дата создания записи';
COMMENT ON COLUMN competence_detalization.date_to IS 'Дата окончания записи';
CREATE INDEX competence_detalization_child_id_idx ON competence_detalization (child_id);
CREATE INDEX competence_detalization_parent_id_idx ON competence_detalization (parent_id);

-- evaluation_event_purpose
CREATE TABLE evaluation_event_purpose
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    unit_code VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE evaluation_event_purpose IS 'Цели мероприятий оценки';
COMMENT ON COLUMN evaluation_event_purpose.id IS 'Уникальный идентификационный номер цели проведения';
COMMENT ON COLUMN evaluation_event_purpose.name IS 'Название цели проведения';
COMMENT ON COLUMN evaluation_event_purpose.date_from IS 'Дата и время создания цели';
COMMENT ON COLUMN evaluation_event_purpose.date_to IS 'Дата и время удаления цели. Если цель активна, то значение null';
COMMENT ON COLUMN evaluation_event_purpose.unit_code IS 'Код юнита';

-- evaluation_event_group_type
CREATE TABLE evaluation_event_group_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP
);
COMMENT ON TABLE evaluation_event_group_type IS 'Группы типов мероприятий оценки';
COMMENT ON COLUMN evaluation_event_group_type.id IS 'Уникальный идентификационный номер группы типов мероприятий';
COMMENT ON COLUMN evaluation_event_group_type.name IS 'Название группы типов мероприятий';
COMMENT ON COLUMN evaluation_event_group_type.date_from IS 'Дата и время создания группы';
COMMENT ON COLUMN evaluation_event_group_type.date_to IS 'Дата и время удаления группы. Если группа активна, то значение null';

-- evaluation_event_type
CREATE TABLE evaluation_event_type
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                      TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                        TIMESTAMP,
    name                           VARCHAR(128)                        NOT NULL,
    evaluation_event_group_type_id BIGINT,
    is_evaluated_by_indicator      boolean   DEFAULT FALSE             NOT NULL,
    is_evaluated_by_competence     boolean   DEFAULT FALSE             NOT NULL,
    description                    VARCHAR(1024),
    is_self_evaluation             boolean   DEFAULT FALSE             NOT NULL,
    is_evaluation_result_changable boolean   DEFAULT FALSE             NOT NULL,
    short_name                     VARCHAR(128),
    CONSTRAINT "evaluation_event_type_evaluation_event_group_type_id_fk" FOREIGN KEY (evaluation_event_group_type_id) REFERENCES evaluation_event_group_type (id)
);
COMMENT ON TABLE evaluation_event_type IS 'Типы мероприятий оценки';
COMMENT ON COLUMN evaluation_event_type.id IS 'Уникальный идентификационный номер типа мероприятия';
COMMENT ON COLUMN evaluation_event_type.date_from IS 'Дата и время создания типа мероприятия';
COMMENT ON COLUMN evaluation_event_type.date_to IS 'Дата и время удаления типа мероприятия. Если тип мероприятия  активен, то значение null';
COMMENT ON COLUMN evaluation_event_type.name IS 'Название типа мероприятия';
COMMENT ON COLUMN evaluation_event_type.evaluation_event_group_type_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_event_group_type, группы типов мероприятий';
COMMENT ON COLUMN evaluation_event_type.is_evaluated_by_indicator IS 'Флаг, указывающий проводится ли оценка мероприятий данного типа по индикаторам или по компетенциям (true, если по индикаторам, false, если по компетенциям)';
COMMENT ON COLUMN evaluation_event_type.is_evaluated_by_competence IS 'Оценка по компетенциям';
COMMENT ON COLUMN evaluation_event_type.description IS 'Описание';
COMMENT ON COLUMN evaluation_event_type.is_self_evaluation IS 'Требуется самооценка';
COMMENT ON COLUMN evaluation_event_type.is_evaluation_result_changable IS 'Результаты оценки могут быть изменены';
COMMENT ON COLUMN evaluation_event_type.short_name IS 'Краткое наименование';
CREATE INDEX evaluation_event_type_evaluation_event_group_type_id_idx ON evaluation_event_type (evaluation_event_group_type_id);

-- evaluation_event_catalog
CREATE TABLE evaluation_event_catalog
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL,
    parent_id BIGINT,
    unit_code VARCHAR(128)                        NOT NULL,
    CONSTRAINT "evaluation_event_catalog_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES evaluation_event_catalog (id)
);
COMMENT ON TABLE evaluation_event_catalog IS 'Каталог мероприятий оценки';
COMMENT ON COLUMN evaluation_event_catalog.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN evaluation_event_catalog.date_from IS 'Дата создания записи';
COMMENT ON COLUMN evaluation_event_catalog.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN evaluation_event_catalog.name IS 'Наименование';
COMMENT ON COLUMN evaluation_event_catalog.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN evaluation_event_catalog.unit_code IS 'Код юнита';
CREATE INDEX evaluation_event_catalog_parent_id_idx ON evaluation_event_catalog (parent_id);

-- evaluation_event
CREATE TABLE evaluation_event
(
    id                                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                         TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                           TIMESTAMP,
    evaluation_event_type_id          BIGINT,
    competence_profile_id             BIGINT,
    evaluation_event_purpose_id       BIGINT,
    use_competence_profile_assignment boolean,
    use_competence_profile_successor  boolean,
    name                              VARCHAR(128),
    description                       VARCHAR(512),
    text_for_evaluated                VARCHAR(512),
    text_for_expert                   VARCHAR(512),
    start_date                        TIMESTAMP,
    end_date                          TIMESTAMP,
    comment                           VARCHAR(512),
    event_format                      boolean   DEFAULT FALSE             NOT NULL,
    location                          VARCHAR(512),
    need_evaluated_agreement          boolean   DEFAULT FALSE             NOT NULL,
    use_specific_profile              boolean   DEFAULT FALSE             NOT NULL,
    is_published                      boolean   DEFAULT FALSE             NOT NULL,
    is_agreed                         boolean   DEFAULT FALSE             NOT NULL,
    evaluation_event_catalog_id       BIGINT,
    use_employee_profile              boolean   DEFAULT FALSE             NOT NULL,
    CONSTRAINT "evaluation_event_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id),
    CONSTRAINT "evaluation_event_evaluation_event_purpose_id_fk" FOREIGN KEY (evaluation_event_purpose_id) REFERENCES evaluation_event_purpose (id),
    CONSTRAINT "evaluation_event_evaluation_event_type_id_fk" FOREIGN KEY (evaluation_event_type_id) REFERENCES evaluation_event_type (id),
    CONSTRAINT "evaluation_event_evaluation_event_catalog_id_fk" FOREIGN KEY (evaluation_event_catalog_id) REFERENCES evaluation_event_catalog (id)
);
COMMENT ON TABLE evaluation_event IS 'Мероприятия оценки';
COMMENT ON COLUMN evaluation_event.id IS 'Уникальный идентификационный номер мероприятия оценки';
COMMENT ON COLUMN evaluation_event.date_from IS 'Дата и время создания мероприятия';
COMMENT ON COLUMN evaluation_event.date_to IS 'Дата и время отмены мероприятия. Если мероприятие не отменялось, то значение null';
COMMENT ON COLUMN evaluation_event.evaluation_event_type_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_event_type, типе мероприятия оценки';
COMMENT ON COLUMN evaluation_event.competence_profile_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile, профиля компетенций (если сценарий подразумевает оценку по конкретному профилю)';
COMMENT ON COLUMN evaluation_event.evaluation_event_purpose_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_event_purpose, цели проведения мероприятия оценки';
COMMENT ON COLUMN evaluation_event.use_competence_profile_assignment IS 'Флаг, указывающий использовать ли текущий профиль компетенции работника для оценки';
COMMENT ON COLUMN evaluation_event.use_competence_profile_successor IS 'Флаг, указывающий использовать ли целевой профиль компетенции работника для оценки';
COMMENT ON COLUMN evaluation_event.name IS 'Название мероприятия';
COMMENT ON COLUMN evaluation_event.description IS 'Описание мероприятия';
COMMENT ON COLUMN evaluation_event.text_for_evaluated IS 'Текст сообщения рассылки для оцениваемого работника';
COMMENT ON COLUMN evaluation_event.text_for_expert IS 'Текст сообщения рассылки для оценщика работника';
COMMENT ON COLUMN evaluation_event.start_date IS 'Дата и время начала мероприятия оценки';
COMMENT ON COLUMN evaluation_event.end_date IS 'Дата и время окончания мероприятия оценки';
COMMENT ON COLUMN evaluation_event.comment IS 'Комментарий к мероприятию оценки';
COMMENT ON COLUMN evaluation_event.event_format IS 'Флаг, указывающий формат проведения мероприятия. Если = true, то мероприятие дистанционное, иначе очное';
COMMENT ON COLUMN evaluation_event.location IS 'Место проведения мероприятия оценки';
COMMENT ON COLUMN evaluation_event.need_evaluated_agreement IS 'Флаг, указывающий требуется ли согласие оцениваемого для проведения оценки';
COMMENT ON COLUMN evaluation_event.use_specific_profile IS 'Флаг, указывающий использовать ли конкретный профиль компетенции для оценки';
COMMENT ON COLUMN evaluation_event.is_published IS 'Флаг, указывающий было ли мероприятие опубликовано (запланировано)';
COMMENT ON COLUMN evaluation_event.is_agreed IS 'Флаг, указывающий было ли мероприятие согласовано (согласованы результаты мероприятия)';
COMMENT ON COLUMN evaluation_event.evaluation_event_catalog_id IS 'Ссылка на идентификатор evaluation_event_catalog, справочник каталогов мероприятий оценки';
COMMENT ON COLUMN evaluation_event.use_employee_profile IS 'Флаг, указывающий использовать ли профиль работника для оценки';
CREATE INDEX evaluation_event_competence_profile_id_idx ON evaluation_event (competence_profile_id);
CREATE INDEX evaluation_event_evaluation_event_purpose_id_idx ON evaluation_event (evaluation_event_purpose_id);
CREATE INDEX evaluation_event_evaluation_event_type_id_idx ON evaluation_event (evaluation_event_type_id);
CREATE INDEX evaluation_event_evaluation_event_catalog_id_idx ON evaluation_event (evaluation_event_catalog_id);

-- competence_evaluation_event
CREATE TABLE competence_evaluation_event
(
    evaluation_event_id BIGINT NOT NULL,
    competence_id       BIGINT NOT NULL,
    CONSTRAINT "competence_evaluation_event_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id),
    CONSTRAINT "competence_evaluation_event_evaluation_event_id_fk" FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event (id)
);
COMMENT ON TABLE competence_evaluation_event IS 'Связи компетенций с мероприятием оценки';
COMMENT ON COLUMN competence_evaluation_event.evaluation_event_id IS 'Ссылка на идентификатор evaluation_event, справочник мероприятий оценки';
COMMENT ON COLUMN competence_evaluation_event.competence_id IS 'Ссылка на идентификатор competence, справочник компетенций';
CREATE INDEX competence_evaluation_event_competence_id_idx ON competence_evaluation_event (competence_id);
CREATE INDEX competence_evaluation_event_evaluation_event_id_idx ON competence_evaluation_event (evaluation_event_id);

-- competence_profile_division_team_role
CREATE TABLE competence_profile_division_team_role
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    competence_profile_id BIGINT,
    division_team_role_id BIGINT,
    CONSTRAINT "competence_profile_division_team_role_cp_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id)
);
COMMENT ON TABLE competence_profile_division_team_role IS '{"tegs":["Профили компетенций"], description:"данная таблица содержит информацию о связи профилей компетенций и позиции в фактической оргструктуре в системе"}';
COMMENT ON COLUMN competence_profile_division_team_role.id IS 'Уникальный идентификационный номер связи';
COMMENT ON COLUMN competence_profile_division_team_role.date_from IS 'Дата и время создания связи';
COMMENT ON COLUMN competence_profile_division_team_role.date_to IS 'Дата и время удаления связи. Если связь активна, то значение null';
COMMENT ON COLUMN competence_profile_division_team_role.competence_profile_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile, профиля компетенций';
COMMENT ON COLUMN competence_profile_division_team_role.division_team_role_id IS 'Ссылка на идентификационный номер записи из таблицы orgstructure.division_team_role_id, позиции в фактической оргструкре компании';
CREATE INDEX competence_profile_division_team_role_cp_id_idx ON competence_profile_division_team_role (competence_profile_id);

-- competence_profile_indicator_level
CREATE TABLE competence_profile_indicator_level
(
    competence_profile_id BIGINT NOT NULL,
    indicator_level_id    BIGINT NOT NULL,
    CONSTRAINT "competence_profile_indicator_level_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id),
    CONSTRAINT "competence_profile_indicator_level_indicator_level_id_fk" FOREIGN KEY (indicator_level_id) REFERENCES indicator_level (id)
);
COMMENT ON TABLE competence_profile_indicator_level IS 'Связи уровня индикаторов компетенций с профилем компетенций';
COMMENT ON COLUMN competence_profile_indicator_level.competence_profile_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile, профиля компетенций';
COMMENT ON COLUMN competence_profile_indicator_level.indicator_level_id IS 'Ссылка на идентификационный номер записи из таблицы indicator_level, уровню индикатора, на котором должен находиться индикатор в профиле';
CREATE INDEX competence_profile_indicator_level_competence_profile_id_idx ON competence_profile_indicator_level (competence_profile_id);
CREATE INDEX competence_profile_indicator_level_indicator_level_id_idx ON competence_profile_indicator_level (indicator_level_id);

-- competence_profile_learning_course
CREATE TABLE competence_profile_learning_course
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    competence_profile_id BIGINT                              NOT NULL,
    learning_course_id    BIGINT                              NOT NULL,
    CONSTRAINT "competence_profile_learning_course_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id)
);
COMMENT ON COLUMN competence_profile_learning_course.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile_learning_course.date_from IS 'Дата создания записи';
COMMENT ON COLUMN competence_profile_learning_course.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN competence_profile_learning_course.competence_profile_id IS 'Ссылка на идентификатор competence_profile, профиль компетенций';
COMMENT ON COLUMN competence_profile_learning_course.learning_course_id IS 'Ссылка на идентификатор learning_course, справочник курсов обучения';
CREATE INDEX competence_profile_learning_course_competence_profile_id_idx ON competence_profile_learning_course (competence_profile_id);

-- competence_profile_link_type
CREATE TABLE competence_profile_link_type
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(128)                        NOT NULL,
    description VARCHAR(1024)                       NOT NULL
);
COMMENT ON TABLE competence_profile_link_type IS 'Связи профилей должности';
COMMENT ON COLUMN competence_profile_link_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile_link_type.name IS 'Наименование';
COMMENT ON COLUMN competence_profile_link_type.description IS 'Описание';

-- competence_profile_link
CREATE TABLE competence_profile_link
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                  TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                    TIMESTAMP,
    competence_profile_id_from BIGINT                              NOT NULL,
    competence_profile_id_to   BIGINT                              NOT NULL,
    type_id                    BIGINT                              NOT NULL,
    CONSTRAINT "competence_profile_link_competence_profile_id_from_fk" FOREIGN KEY (competence_profile_id_from) REFERENCES competence_profile (id),
    CONSTRAINT "competence_profile_link_competence_profile_id_to_fk" FOREIGN KEY (competence_profile_id_to) REFERENCES competence_profile (id),
    CONSTRAINT "competence_profile_link_type_id_fk" FOREIGN KEY (type_id) REFERENCES competence_profile_link_type (id)
);
COMMENT ON COLUMN competence_profile_link.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile_link.date_from IS 'Дата создания записи';
COMMENT ON COLUMN competence_profile_link.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN competence_profile_link.competence_profile_id_from IS 'Дата начала действия профиля';
COMMENT ON COLUMN competence_profile_link.competence_profile_id_to IS 'Дата окончания действия профиля';
CREATE INDEX competence_profile_link_competence_profile_id_from_idx ON competence_profile_link (competence_profile_id_from);
CREATE INDEX competence_profile_link_competence_profile_id_to_idx ON competence_profile_link (competence_profile_id_to);
CREATE INDEX competence_profile_link_type_id_idx ON competence_profile_link (type_id);

-- competence_profile_work_experience
CREATE TABLE competence_profile_work_experience
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                         TIMESTAMP,
    competence_profile_id           BIGINT                              NOT NULL,
    work_experience_type_id         BIGINT                              NOT NULL,
    work_experience_duration_months BIGINT                              NOT NULL,
    CONSTRAINT "competence_profile_work_experience_competence_profile_id_fk" FOREIGN KEY (competence_profile_id) REFERENCES competence_profile (id)
);
COMMENT ON TABLE competence_profile_work_experience IS 'Стажи профилей должности';
COMMENT ON COLUMN competence_profile_work_experience.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN competence_profile_work_experience.date_from IS 'Системная дата начала действия записи';
COMMENT ON COLUMN competence_profile_work_experience.date_to IS 'Системная дата окончания действия записи';
COMMENT ON COLUMN competence_profile_work_experience.competence_profile_id IS 'Ссылка на идентификационный номер записи из таблицы competence_profile';
COMMENT ON COLUMN competence_profile_work_experience.work_experience_type_id IS 'Идентификационный номер записи в таблице work_experience_type БД Orgstructure';
COMMENT ON COLUMN competence_profile_work_experience.work_experience_duration_months IS 'Стаж (мес.)';
CREATE INDEX competence_profile_work_experience_competence_profile_id_idx ON competence_profile_work_experience (competence_profile_id);

-- development_goal
CREATE TABLE development_goal
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    description VARCHAR(512),
    name        VARCHAR(128)                        NOT NULL,
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP,
    unit_code   VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE development_goal IS 'Траектории развития в рамках ИПР';
COMMENT ON COLUMN development_goal.id IS 'Уникальный идентификационный номер фокуса развития';
COMMENT ON COLUMN development_goal.description IS 'Текстовое описание фокуса развития';
COMMENT ON COLUMN development_goal.name IS 'Название фокуса развития';
COMMENT ON COLUMN development_goal.date_from IS 'Дата и время добавления фокуса развития';
COMMENT ON COLUMN development_goal.date_to IS 'Дата и время удаления фокуса развития. Если фокус активен, то значение null';

-- development_goal_competence
CREATE TABLE development_goal_competence
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    competence_id       BIGINT                              NOT NULL,
    development_goal_id BIGINT                              NOT NULL,
    author_employee_id  BIGINT                              NOT NULL,
    weight              BIGINT                              NOT NULL,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    CONSTRAINT "development_goal_competence_development_goal_id_fk" FOREIGN KEY (development_goal_id) REFERENCES development_goal (id),
    CONSTRAINT "development_goal_competence_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id)
);
COMMENT ON TABLE development_goal_competence IS 'Связи траекторий развития и компетенций';
COMMENT ON COLUMN development_goal_competence.competence_id IS 'Ссылка на уникальный идентификационный номер компетенции';
COMMENT ON COLUMN development_goal_competence.development_goal_id IS 'Ссылка на уникальный идентификационный номер фокуса развития';
COMMENT ON COLUMN development_goal_competence.author_employee_id IS 'Ссылка на идентификационный номер сотрудника, добавившего компетецию (employee_id)';
COMMENT ON COLUMN development_goal_competence.weight IS 'Вес (порядковый номер) компетенции, определяющий ее значимость для фокуса';
COMMENT ON COLUMN development_goal_competence.date_from IS 'Дата и время добавления компетенции';
COMMENT ON COLUMN development_goal_competence.date_to IS 'Дата и время удаления компетенции. Если каталог активен, то значение null';
COMMENT ON COLUMN development_goal_competence.id IS 'Уникальный идентификатор записи';
CREATE INDEX development_goal_id_competence_id_idx ON development_goal_competence (development_goal_id, competence_id);

-- development_plan_duration
CREATE TABLE development_plan_duration
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name                    VARCHAR(256)                        NOT NULL,
    month                   BIGINT,
    date_from               TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                 TIMESTAMP,
    is_default              boolean   DEFAULT TRUE              NOT NULL,
    date_start_is_available boolean   DEFAULT FALSE             NOT NULL,
    unit_code               VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE development_plan_duration IS 'Типовые длительности ИПР';
COMMENT ON COLUMN development_plan_duration.id IS 'Уникальный идентификационный номер длительности';
COMMENT ON COLUMN development_plan_duration.name IS 'Название периода длительности';
COMMENT ON COLUMN development_plan_duration.month IS 'Кол-во месяцев, которые длится ИПР';
COMMENT ON COLUMN development_plan_duration.date_from IS 'Дата и время создания источника внесения объекта';
COMMENT ON COLUMN development_plan_duration.date_to IS 'Дата и время удаления источника внесения объекта. Если источник активен, то значение null';
COMMENT ON COLUMN development_plan_duration.is_default IS 'Значение по умолчанию';
COMMENT ON COLUMN development_plan_duration.date_start_is_available IS 'Дата начала доступна';

-- division_team_competence
CREATE TABLE division_team_competence
(
    competence_id    BIGINT                  NOT NULL,
    division_team_id BIGINT                  NOT NULL,
    weight           BIGINT                  NOT NULL,
    date_from        TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to          TIMESTAMP,
    CONSTRAINT division_team_competence_competence_id_division_team_id_pk PRIMARY KEY (competence_id, division_team_id),
    CONSTRAINT "division_team_competence_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id)
);
COMMENT ON TABLE division_team_competence IS 'Связи компетенций и подразделений организации';
COMMENT ON COLUMN division_team_competence.competence_id IS 'Уникальный идентификационный номер компетенции';
COMMENT ON COLUMN division_team_competence.division_team_id IS 'Ссылка на идентификационный номер записи в таблице Division_team. Ссылка покажет в какое подразделение или группу подразделений добавляется компетенция.';
COMMENT ON COLUMN division_team_competence.weight IS 'Вес (порядковый номер) компетенции, определяющий ее значимость для подразделения';
COMMENT ON COLUMN division_team_competence.date_from IS 'Дата и время добавления компетенции';
COMMENT ON COLUMN division_team_competence.date_to IS 'Дата и время удаления компетенции. Если каталог активен, то значение null';
CREATE INDEX division_team_competence_competence_id_idx ON division_team_competence (competence_id);

-- project_experience_activity
CREATE TABLE project_experience_activity
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    name               VARCHAR(256)                        NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128),
    unit_code          VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE project_experience_activity IS 'Справочник направлений деятельности';
COMMENT ON COLUMN project_experience_activity.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN project_experience_activity.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_experience_activity.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN project_experience_activity.name IS 'Наименование компании';
COMMENT ON COLUMN project_experience_activity.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN project_experience_activity.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN project_experience_activity.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN project_experience_activity.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN project_experience_activity.unit_code IS 'Код юнита';

-- project_experience_type
CREATE TABLE project_experience_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    code               VARCHAR(128)                        NOT NULL,
    name               VARCHAR(256)                        NOT NULL,
    description        VARCHAR(2048),
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128)
);
COMMENT ON TABLE project_experience_type IS 'Справочник типов проектного опыта';
COMMENT ON COLUMN project_experience_type.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN project_experience_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_experience_type.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN project_experience_type.code IS 'Код типа проектного опыта';
COMMENT ON COLUMN project_experience_type.name IS 'Наименование типа проектного опыта';
COMMENT ON COLUMN project_experience_type.description IS 'Описание типа проектного опыта';
COMMENT ON COLUMN project_experience_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN project_experience_type.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN project_experience_type.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN project_experience_type.external_id IS 'Внешний идентификатор';
CREATE UNIQUE INDEX project_experience_type_code_uq ON project_experience_type (code) WHERE (date_to IS NULL);

-- project_experience
CREATE TABLE project_experience
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    type_id            BIGINT                              NOT NULL,
    name               VARCHAR(256)                        NOT NULL,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128),
    CONSTRAINT "project_experience_type_id_fk" FOREIGN KEY (type_id) REFERENCES project_experience_type (id)
);
COMMENT ON TABLE project_experience IS 'Данные о проектах для проектного опыта';
COMMENT ON COLUMN project_experience.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN project_experience.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_experience.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN project_experience.type_id IS 'Идентификатор типа проектного опыта';
COMMENT ON COLUMN project_experience.name IS 'Наименование проекта для проектного опыта';
COMMENT ON COLUMN project_experience.date_start IS 'Дата начала договора';
COMMENT ON COLUMN project_experience.date_end IS 'Дата окончания договора';
COMMENT ON COLUMN project_experience.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN project_experience.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN project_experience.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN project_experience.external_id IS 'Внешний идентификатор';
CREATE INDEX project_experience_type_id_idx ON project_experience (type_id);

-- employee_project_experience
CREATE TABLE employee_project_experience
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    employee_id           BIGINT                              NOT NULL,
    project_experience_id BIGINT                              NOT NULL,
    company_id            BIGINT                              NOT NULL,
    job_title_id          BIGINT                              NOT NULL,
    activity_id           BIGINT                              NOT NULL,
    fte_percent           integer   DEFAULT 0                 NOT NULL,
    date_start            TIMESTAMP,
    date_end              TIMESTAMP,
    date_confirm_hr       TIMESTAMP,
    date_confirm_ruk      TIMESTAMP,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    external_id           VARCHAR(128),
    CONSTRAINT "employee_project_experience_company_id_fk" FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT "employee_project_experience_activity_id_fk" FOREIGN KEY (activity_id) REFERENCES project_experience_activity (id),
    CONSTRAINT "employee_project_experience_project_experience_id_fk" FOREIGN KEY (project_experience_id) REFERENCES project_experience (id)
);
COMMENT ON TABLE employee_project_experience IS 'Данные о проектном опыте сотрудников';
COMMENT ON COLUMN employee_project_experience.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN employee_project_experience.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_project_experience.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_project_experience.employee_id IS 'Идентификатор сотрудника';
COMMENT ON COLUMN employee_project_experience.project_experience_id IS 'Идентификатор проекта';
COMMENT ON COLUMN employee_project_experience.company_id IS 'Идентификатор компании';
COMMENT ON COLUMN employee_project_experience.job_title_id IS 'Идентификатор должности';
COMMENT ON COLUMN employee_project_experience.activity_id IS 'Идентификатор направления деятельности проектного опыта';
COMMENT ON COLUMN employee_project_experience.fte_percent IS 'Процент участия в проекте';
COMMENT ON COLUMN employee_project_experience.date_start IS 'Дата начала проектного опыта';
COMMENT ON COLUMN employee_project_experience.date_end IS 'Дата окончания проектного опыта';
COMMENT ON COLUMN employee_project_experience.date_confirm_hr IS 'Дата подтверждения проектного опыта HR-ом';
COMMENT ON COLUMN employee_project_experience.date_confirm_ruk IS 'Дата подтверждения проектного опыта Руководителем';
COMMENT ON COLUMN employee_project_experience.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_project_experience.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_project_experience.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_project_experience.external_id IS 'Внешний идентификатор';
CREATE INDEX employee_project_experience_employee_id_idx ON employee_project_experience (employee_id);
CREATE INDEX employee_project_experience_job_title_id_idx ON employee_project_experience (job_title_id);
CREATE INDEX employee_project_experience_project_experience_id_idx ON employee_project_experience (project_experience_id);
CREATE INDEX employee_project_experience_activity_id_idx ON employee_project_experience (activity_id);
CREATE INDEX employee_project_experience_company_id_idx ON employee_project_experience (company_id);

-- employee_project_experience_achievements
CREATE TABLE employee_project_experience_achievements
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                        TIMESTAMP,
    employee_project_experience_id BIGINT                              NOT NULL,
    achievement_name               VARCHAR(256)                        NOT NULL,
    update_date                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id             BIGINT                              NOT NULL,
    update_employee_id             BIGINT                              NOT NULL,
    external_id                    VARCHAR(128),
    CONSTRAINT "employee_project_experience_achievements_epe_id_fk" FOREIGN KEY (employee_project_experience_id) REFERENCES employee_project_experience (id)
);
COMMENT ON TABLE employee_project_experience_achievements IS 'Данные о достижениях сотрудников в проектном опыте';
COMMENT ON COLUMN employee_project_experience_achievements.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN employee_project_experience_achievements.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_project_experience_achievements.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_project_experience_achievements.employee_project_experience_id IS 'Идентификатор проектного опыта';
COMMENT ON COLUMN employee_project_experience_achievements.achievement_name IS 'Наименование достижения';
COMMENT ON COLUMN employee_project_experience_achievements.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_project_experience_achievements.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_project_experience_achievements.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_project_experience_achievements.external_id IS 'Внешний идентификатор';
CREATE INDEX employee_project_experience_achievements_epe_id_idx ON employee_project_experience_achievements (employee_project_experience_id);

-- employee_project_experience_competence
CREATE TABLE employee_project_experience_competence
(
    id                             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                        TIMESTAMP,
    employee_project_experience_id BIGINT                              NOT NULL,
    competence_id                  BIGINT                              NOT NULL,
    update_date                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id             BIGINT                              NOT NULL,
    update_employee_id             BIGINT                              NOT NULL,
    external_id                    VARCHAR(128),
    CONSTRAINT "employee_project_experience_competence_competence_id_fk" FOREIGN KEY (competence_id) REFERENCES competence (id),
    CONSTRAINT "employee_project_experience_competence_epe_id_fk" FOREIGN KEY (employee_project_experience_id) REFERENCES employee_project_experience (id)
);
COMMENT ON TABLE employee_project_experience_competence IS 'Данные о компетенциях сотрудников в проектном опыте';
COMMENT ON COLUMN employee_project_experience_competence.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN employee_project_experience_competence.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_project_experience_competence.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_project_experience_competence.employee_project_experience_id IS 'Идентификатор проектного опыта';
COMMENT ON COLUMN employee_project_experience_competence.competence_id IS 'Идентификатор компетенции';
COMMENT ON COLUMN employee_project_experience_competence.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_project_experience_competence.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_project_experience_competence.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_project_experience_competence.external_id IS 'Внешний идентификатор';
CREATE INDEX employee_project_experience_competence_competence_id_idx ON employee_project_experience_competence (competence_id);
CREATE INDEX employee_project_experience_competence_epe_id_idx ON employee_project_experience_competence (employee_project_experience_id);

-- employee_work_experience
CREATE TABLE employee_work_experience
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from    TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to      TIMESTAMP,
    employee_id  VARCHAR(32)  DEFAULT NULL::VARCHAR,
    company_name VARCHAR(256) DEFAULT NULL::VARCHAR
);
COMMENT ON COLUMN employee_work_experience.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN employee_work_experience.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_work_experience.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN employee_work_experience.employee_id IS 'Ссылка на идентификатор employee, работник';

-- evaluation_border
CREATE TABLE evaluation_border
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to      TIMESTAMP,
    border_group BIGINT                              NOT NULL,
    lower_bound  numeric(15, 2),
    upper_bound  numeric(15, 2),
    value        numeric(15, 2)
);
COMMENT ON TABLE evaluation_border IS 'Связь шкалы оценки с диапазонами значений';
COMMENT ON COLUMN evaluation_border.id IS 'Уникальный идентификационный номер';
COMMENT ON COLUMN evaluation_border.date_from IS 'Дата создания записи';
COMMENT ON COLUMN evaluation_border.date_to IS 'Дата закрытия записи';
COMMENT ON COLUMN evaluation_border.border_group IS 'Указатель группы границ значений показателя';
COMMENT ON COLUMN evaluation_border.lower_bound IS 'Нижняя граница';
COMMENT ON COLUMN evaluation_border.upper_bound IS 'Верхняя граница';
COMMENT ON COLUMN evaluation_border.value IS 'Значение';

-- evaluation_event_competence_profile_type
CREATE TABLE evaluation_event_competence_profile_type
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP,
    name        VARCHAR(256)                        NOT NULL,
    description VARCHAR(2048),
    code        VARCHAR(128)                        NOT NULL,
    is_active   boolean   DEFAULT TRUE              NOT NULL,
    is_editable boolean   DEFAULT TRUE              NOT NULL,
    is_system   boolean   DEFAULT FALSE             NOT NULL,
    unit_code   VARCHAR(128)                        NOT NULL,
    CONSTRAINT evaluation_event_competence_profile_type_code_unit_code_uq UNIQUE (code, unit_code)
);
COMMENT ON TABLE evaluation_event_competence_profile_type IS 'Справочник сценариев проведения оценки';
COMMENT ON COLUMN evaluation_event_competence_profile_type.id IS 'Синтетический ID';
COMMENT ON COLUMN evaluation_event_competence_profile_type.date_from IS 'Системная дата начала действия записи';
COMMENT ON COLUMN evaluation_event_competence_profile_type.date_to IS 'Системная дата окончания действия записи';
COMMENT ON COLUMN evaluation_event_competence_profile_type.name IS 'Наименование сценария оценки';
COMMENT ON COLUMN evaluation_event_competence_profile_type.description IS 'Описание сценария оценки';
COMMENT ON COLUMN evaluation_event_competence_profile_type.code IS 'Код сценария проведения оценки';
COMMENT ON COLUMN evaluation_event_competence_profile_type.unit_code IS 'Код юнита';

-- evaluation_event_format
CREATE TABLE evaluation_event_format
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP,
    name        VARCHAR(256)                        NOT NULL,
    description VARCHAR(2048),
    code        VARCHAR(128)                        NOT NULL,
    is_active   boolean   DEFAULT TRUE              NOT NULL,
    is_editable boolean   DEFAULT TRUE              NOT NULL,
    is_system   boolean   DEFAULT FALSE             NOT NULL,
    unit_code   VARCHAR(128)                        NOT NULL,
    CONSTRAINT evaluation_event_format_code_unit_code_uq UNIQUE (code, unit_code)
);
COMMENT ON TABLE evaluation_event_format IS 'Справочник форматов проведения оценки';
COMMENT ON COLUMN evaluation_event_format.id IS 'Синтетический ID';
COMMENT ON COLUMN evaluation_event_format.date_from IS 'Системная дата начала действия записи';
COMMENT ON COLUMN evaluation_event_format.date_to IS 'Системная дата окончания действия записи';
COMMENT ON COLUMN evaluation_event_format.name IS 'Наименование формата проведения мероприятия оценки';
COMMENT ON COLUMN evaluation_event_format.description IS 'Описание формата проведения оценки';
COMMENT ON COLUMN evaluation_event_format.unit_code IS 'Код юнита';

-- evaluation_event_type_evaluation_role
CREATE TABLE evaluation_event_type_evaluation_role
(
    id                        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    evaluation_event_type_id  BIGINT                              NOT NULL,
    evaluation_role_id        BIGINT                              NOT NULL,
    evaluator_count_min       integer,
    evaluator_count_max       integer,
    evaluator_count_recommend integer,
    is_event_available        boolean,
    is_employee_available     boolean,
    index                     integer,
    CONSTRAINT evaluation_event_type_evaluation_role_eet_id_role_id_uq UNIQUE (evaluation_event_type_id, evaluation_role_id),
    CONSTRAINT "evaluation_event_type_evaluation_role_eet_id_fk" FOREIGN KEY (evaluation_event_type_id) REFERENCES evaluation_event_type (id),
    CONSTRAINT "evaluation_event_type_evaluation_role_er_id_fk" FOREIGN KEY (evaluation_role_id) REFERENCES evaluation_role (id)
);
COMMENT ON TABLE evaluation_event_type_evaluation_role IS '"Связи типов мероприятий оценки и ролей оценщиков, которые могут участвовать в оценке работника в рамках мероприятия"';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.evaluation_event_type_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_event_type, типа мероприятий';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.evaluation_role_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_role, роли оценщика, который может участвовать в оценке работника в рамках мероприятия';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.evaluator_count_min IS 'Кол-во оценщиков, минимальное';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.evaluator_count_max IS 'Кол-во оценщиков, максимальное';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.evaluator_count_recommend IS 'Кол-во оценщиков, рекомендованное';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.is_event_available IS 'Мероприятие доступно';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.is_employee_available IS 'Работник доступен';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
COMMENT ON COLUMN evaluation_event_type_evaluation_role.id IS 'Уникальный идентификатор записи';

-- evaluation_process_type
CREATE TABLE evaluation_process_type
(
    code        VARCHAR(256) PRIMARY KEY NOT NULL,
    name        VARCHAR(256)             NOT NULL,
    description VARCHAR(512)             NOT NULL
);
COMMENT ON TABLE evaluation_process_type IS 'Типы процессов мероприятия оценки';

-- evaluation_process_type_process_stage_group
CREATE TABLE evaluation_process_type_process_stage_group
(
    id                       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    process_type_code        VARCHAR(256)                        NOT NULL,
    process_stage_group_id   BIGINT                              NOT NULL,
    process_stage_group_name VARCHAR(256) DEFAULT ''::VARCHAR    NOT NULL,
    CONSTRAINT evaluation_process_type_process_stage_group_code_fk FOREIGN KEY (process_type_code) REFERENCES evaluation_process_type (code)
);
CREATE INDEX evaluation_process_type_process_stage_group_code_idx ON evaluation_process_type_process_stage_group (process_type_code);

-- responsibility_zone_type
CREATE TABLE responsibility_zone_type
(
    code      VARCHAR(128) PRIMARY KEY            NOT NULL,
    date_from TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL
);

-- evaluation_scale_kpi_responsibility_zone_type
CREATE TABLE evaluation_scale_kpi_responsibility_zone_type
(
    id                            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                       TIMESTAMP,
    kpi_code                      VARCHAR(128),
    responsibility_zone_type_code VARCHAR(128),
    scale_code                    VARCHAR(128),
    CONSTRAINT evaluation_scale_kpi_responsibility_zone_type_type_code_fk FOREIGN KEY (responsibility_zone_type_code) REFERENCES responsibility_zone_type (code)
);
CREATE INDEX evaluation_scale_kpi_responsibility_zone_type_code_idx ON evaluation_scale_kpi_responsibility_zone_type (responsibility_zone_type_code);

-- evaluator_evaluation_event
CREATE TABLE evaluator_evaluation_event
(
    evaluation_event_id BIGINT                  NOT NULL,
    employee_id         BIGINT                  NOT NULL,
    evaluation_role_id  BIGINT,
    date_from           TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to             TIMESTAMP,
    CONSTRAINT "evaluator_evaluation_event_pk" PRIMARY KEY (evaluation_event_id, employee_id),
    CONSTRAINT "evaluator_evaluation_event_evaluation_event_id_fk" FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event (id),
    CONSTRAINT "evaluator_evaluation_event_evaluation_role_id_fk" FOREIGN KEY (evaluation_role_id) REFERENCES evaluation_role (id)
);
COMMENT ON TABLE evaluator_evaluation_event IS 'Связи оценщиков и их ролей в мероприятии оценки';
COMMENT ON COLUMN evaluator_evaluation_event.evaluation_event_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_role, роли оценщика, который может участвовать в оценке работника в рамках мероприятия';
COMMENT ON COLUMN evaluator_evaluation_event.employee_id IS 'Ссылка на идентификационный номер сотрудника, являющегося оценщиком в данном мероприятии (employee_id)';
COMMENT ON COLUMN evaluator_evaluation_event.evaluation_role_id IS 'Ссылка на идентификационный номер записи из таблицы evaluation_role, роли оценщика, в которой он оценивает работника в рамках мероприятия';
COMMENT ON COLUMN evaluator_evaluation_event.date_from IS 'Дата создания записи';
COMMENT ON COLUMN evaluator_evaluation_event.date_to IS 'Дата окончания записи';

-- kpi_calculation_model
CREATE TABLE kpi_calculation_model
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code                    VARCHAR(32)                         NOT NULL,
    date_from               TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to                 TIMESTAMP,
    description             VARCHAR(512) DEFAULT ''::VARCHAR    NOT NULL,
    name                    VARCHAR(128)                        NOT NULL,
    completion_borders_info VARCHAR(128) DEFAULT NULL::VARCHAR,
    evaluation_scale_info   VARCHAR(128) DEFAULT NULL::VARCHAR
);
COMMENT ON TABLE kpi_calculation_model IS 'Алгоритм расчета выполнения КПЭ';
COMMENT ON COLUMN kpi_calculation_model.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_calculation_model.code IS 'Системный код записи';
COMMENT ON COLUMN kpi_calculation_model.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_calculation_model.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN kpi_calculation_model.description IS 'Описание';
COMMENT ON COLUMN kpi_calculation_model.name IS 'Наименование';
COMMENT ON COLUMN kpi_calculation_model.completion_borders_info IS 'Текстовая справка о границах выполнения КПЭ';
COMMENT ON COLUMN kpi_calculation_model.evaluation_scale_info IS 'Шкала выполнения';

-- kpi_type
CREATE TABLE kpi_type
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to     TIMESTAMP,
    description VARCHAR(512) DEFAULT ''::VARCHAR    NOT NULL,
    name        VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE kpi_type IS 'Типы КПЭ';
COMMENT ON COLUMN kpi_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_type.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN kpi_type.description IS 'Описание';
COMMENT ON COLUMN kpi_type.name IS 'Наименование';

-- kpi_group
CREATE TABLE kpi_group
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to     TIMESTAMP,
    description VARCHAR(512) DEFAULT ''::VARCHAR    NOT NULL,
    name        VARCHAR(128)                        NOT NULL,
    kpi_type_id BIGINT,
    CONSTRAINT "kpi_group_kpi_type_id_fk" FOREIGN KEY (kpi_type_id) REFERENCES kpi_type (id)
);
COMMENT ON TABLE kpi_group IS 'Группа КПЭ';
COMMENT ON COLUMN kpi_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_group.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN kpi_group.description IS 'Описание';
COMMENT ON COLUMN kpi_group.name IS 'Наименование';
COMMENT ON COLUMN kpi_group.kpi_type_id IS 'Тип КПЭ';
CREATE INDEX kpi_group_kpi_type_id_idx ON kpi_group (kpi_type_id);

-- kpi_metric
CREATE TABLE kpi_metric
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    description VARCHAR(512) DEFAULT ''::VARCHAR    NOT NULL,
    name        VARCHAR(128)                        NOT NULL,
    date_from   TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to     TIMESTAMP
);
COMMENT ON TABLE kpi_metric IS 'Единицы измерения для КПЭ';
COMMENT ON COLUMN kpi_metric.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_metric.description IS 'Описание';
COMMENT ON COLUMN kpi_metric.name IS 'Наименование';
COMMENT ON COLUMN kpi_metric.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_metric.date_to IS 'Дата окончания записи';

-- kpi
CREATE TABLE kpi
(
    id                       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to                  TIMESTAMP,
    description              VARCHAR(512) DEFAULT ''::VARCHAR    NOT NULL,
    name                     VARCHAR(128)                        NOT NULL,
    kpi_calculation_model_id BIGINT,
    kpi_group_id             BIGINT,
    kpi_metric_id            BIGINT,
    CONSTRAINT "kpi_kpi_calculation_model_id_fk" FOREIGN KEY (kpi_calculation_model_id) REFERENCES kpi_calculation_model (id),
    CONSTRAINT "kpi_kpi_group_id_fk" FOREIGN KEY (kpi_group_id) REFERENCES kpi_group (id),
    CONSTRAINT "kpi_kpi_metric_id_fk" FOREIGN KEY (kpi_metric_id) REFERENCES kpi_metric (id)
);
COMMENT ON COLUMN kpi.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN kpi.description IS 'Описание';
COMMENT ON COLUMN kpi.name IS 'Наименование';
CREATE INDEX kpi_kpi_calculation_model_id_idx ON kpi (kpi_calculation_model_id);
CREATE INDEX kpi_kpi_group_id_idx ON kpi (kpi_group_id);
CREATE INDEX kpi_kpi_metric_id_idx ON kpi (kpi_metric_id);

-- kpi_appraisal_period
CREATE TABLE kpi_appraisal_period
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from         TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to           TIMESTAMP,
    position_id       BIGINT                              NOT NULL,
    period_code       VARCHAR(128)                        NOT NULL,
    process_type_code VARCHAR(256),
    CONSTRAINT "kpi_appraisal_period_process_type_code_fk" FOREIGN KEY (process_type_code) REFERENCES evaluation_process_type (code)
);
COMMENT ON TABLE kpi_appraisal_period IS 'Период мониторинга КПЭ';
COMMENT ON COLUMN kpi_appraisal_period.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_appraisal_period.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_appraisal_period.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN kpi_appraisal_period.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN kpi_appraisal_period.period_code IS 'Номер периода оценки';
COMMENT ON COLUMN kpi_appraisal_period.process_type_code IS 'Тип процесса к которому применим этот тип периода';
CREATE INDEX kpi_appraisal_period_process_type_code_idx ON kpi_appraisal_period (process_type_code);

-- kpi_catalog
CREATE TABLE kpi_catalog
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    date_to            TIMESTAMP,
    code               VARCHAR(128)                            NOT NULL,
    name               VARCHAR(256)                            NOT NULL,
    description        VARCHAR(2048),
    parent_id          BIGINT,
    root_id            BIGINT,
    formula_id         BIGINT,
    author_employee_id BIGINT                                  NOT NULL,
    update_employee_id BIGINT                                  NOT NULL,
    update_date        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    unit_code          VARCHAR(128) DEFAULT 'default'::VARCHAR NOT NULL,
    CONSTRAINT "kpi_catalog_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES kpi_catalog (id),
    CONSTRAINT "kpi_catalog_root_id_fk" FOREIGN KEY (root_id) REFERENCES kpi_catalog (id)
);
COMMENT ON TABLE kpi_catalog IS 'Таблица для ведения списка каталогов KPI с иерархией';
COMMENT ON COLUMN kpi_catalog.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_catalog.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN kpi_catalog.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN kpi_catalog.code IS 'Код';
COMMENT ON COLUMN kpi_catalog.name IS 'Наименование';
COMMENT ON COLUMN kpi_catalog.description IS 'Описание';
COMMENT ON COLUMN kpi_catalog.parent_id IS 'Уникальный идентификатор родительского каталога';
COMMENT ON COLUMN kpi_catalog.root_id IS 'Уникальный идентификатор рутовой группы каталога (если группа является верхней в иерархии, то у нее id = root_id)';
COMMENT ON COLUMN kpi_catalog.formula_id IS 'Уникальный идентификатор формулы';
COMMENT ON COLUMN kpi_catalog.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN kpi_catalog.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN kpi_catalog.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN kpi_catalog.unit_code IS 'Код юнита';
CREATE INDEX kpi_catalog_parent_id_idx ON kpi_catalog (parent_id);
CREATE INDEX kpi_catalog_root_id_idx ON kpi_catalog (root_id);
CREATE UNIQUE INDEX kpi_catalog_unit_code_code_idx ON kpi_catalog (unit_code, code) WHERE (date_to IS NULL);

-- kpi_data
CREATE TABLE kpi_data
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    fact        numeric(20, 2) DEFAULT 0            NOT NULL,
    period_from date                                NOT NULL,
    period_to   date,
    plan        numeric(20, 2) DEFAULT 0            NOT NULL,
    kpi_id      BIGINT,
    date_from   TIMESTAMP      DEFAULT NOW()        NOT NULL,
    date_to     TIMESTAMP,
    CONSTRAINT "kpi_data_kpi_id_fk" FOREIGN KEY (kpi_id) REFERENCES kpi (id)
);
COMMENT ON COLUMN kpi_data.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN kpi_data.date_from IS 'Дата создания записи';
COMMENT ON COLUMN kpi_data.date_to IS 'Дата окончания записи';
CREATE INDEX kpi_data_kpi_id_idx ON kpi_data (kpi_id);

-- onboarding_plan_duration
CREATE TABLE onboarding_plan_duration
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from       TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to         TIMESTAMP,
    duration_months BIGINT    DEFAULT 0                 NOT NULL,
    duration_days   BIGINT    DEFAULT 0                 NOT NULL
);
COMMENT ON TABLE onboarding_plan_duration IS 'Продолжительность планов адаптации (типовых) в днях и месяцах';
COMMENT ON COLUMN onboarding_plan_duration.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_plan_duration.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_plan_duration.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_plan_duration.duration_months IS 'Длительность плана адаптации в месяцах';
COMMENT ON COLUMN onboarding_plan_duration.duration_days IS 'Длительность плана адаптации в днях';

-- onboarding_plan_group
CREATE TABLE onboarding_plan_group
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL,
    parent_id BIGINT,
    CONSTRAINT "onboarding_plan_group_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES onboarding_plan_group (id)
);
COMMENT ON TABLE onboarding_plan_group IS 'Семейства типовых планов адаптации';
COMMENT ON COLUMN onboarding_plan_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_plan_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_plan_group.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_plan_group.name IS 'Наименование';
COMMENT ON COLUMN onboarding_plan_group.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
CREATE INDEX onboarding_plan_group_parent_id_idx ON onboarding_plan_group (parent_id);

-- onboarding_plan
CREATE TABLE onboarding_plan
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from        TIMESTAMP    DEFAULT NOW()          NOT NULL,
    date_to          TIMESTAMP,
    name             VARCHAR(256)                        NOT NULL,
    plan_group_id    BIGINT,
    plan_duration_id BIGINT                              NOT NULL,
    is_startable     boolean      DEFAULT FALSE          NOT NULL,
    condition_code   VARCHAR(128) DEFAULT NULL::VARCHAR,
    condition_name   VARCHAR(128) DEFAULT NULL::VARCHAR,
    CONSTRAINT "onboarding_plan_plan_duration_id_fk" FOREIGN KEY (plan_duration_id) REFERENCES onboarding_plan_duration (id),
    CONSTRAINT "onboarding_plan_plan_group_id_fk" FOREIGN KEY (plan_group_id) REFERENCES onboarding_plan_group (id)
);
COMMENT ON TABLE onboarding_plan IS 'План адаптации';
COMMENT ON COLUMN onboarding_plan.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_plan.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_plan.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_plan.name IS 'Наименование';
CREATE INDEX onboarding_plan_plan_duration_id_idx ON onboarding_plan (plan_duration_id);
CREATE INDEX onboarding_plan_plan_group_id_idx ON onboarding_plan (plan_group_id);

-- onboarding_plan_learning
CREATE TABLE onboarding_plan_learning
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from        TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to          TIMESTAMP,
    plan_id          BIGINT                              NOT NULL,
    learning_plan_id BIGINT                              NOT NULL,
    CONSTRAINT "onboarding_plan_learning_plan_id_fk" FOREIGN KEY (plan_id) REFERENCES onboarding_plan (id)
);
COMMENT ON COLUMN onboarding_plan_learning.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_plan_learning.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_plan_learning.date_to IS 'Дата окончания записи';
CREATE INDEX onboarding_plan_learning_plan_id_idx ON onboarding_plan_learning (plan_id);

-- onboarding_plan_material
CREATE TABLE onboarding_plan_material
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from   TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to     TIMESTAMP,
    name        VARCHAR(256)                        NOT NULL,
    link_url    VARCHAR(512)  DEFAULT NULL::VARCHAR,
    description VARCHAR(2048) DEFAULT NULL::VARCHAR,
    plan_id     BIGINT                              NOT NULL,
    CONSTRAINT "onboarding_plan_material_plan_id_fk" FOREIGN KEY (plan_id) REFERENCES onboarding_plan (id)
);
COMMENT ON COLUMN onboarding_plan_material.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_plan_material.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_plan_material.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_plan_material.name IS 'Наименование';
COMMENT ON COLUMN onboarding_plan_material.description IS 'Описание';
CREATE INDEX onboarding_plan_material_plan_id_idx ON onboarding_plan_material (plan_id);

-- onboarding_result
CREATE TABLE onboarding_result
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL
);
COMMENT ON TABLE onboarding_result IS 'Типы результатов прохождения адаптации';
COMMENT ON COLUMN onboarding_result.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_result.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_result.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_result.name IS 'Наименование';

-- onboarding_task_type
CREATE TABLE onboarding_task_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    name                  VARCHAR(256)                        NOT NULL,
    is_exists_is_required boolean   DEFAULT FALSE             NOT NULL,
    is_exists_description boolean   DEFAULT FALSE             NOT NULL,
    is_exists_days        boolean   DEFAULT FALSE             NOT NULL,
    is_exists_shifts      boolean   DEFAULT FALSE             NOT NULL,
    is_exists_files       boolean   DEFAULT FALSE             NOT NULL,
    is_exists_key_results boolean   DEFAULT FALSE             NOT NULL
);
COMMENT ON TABLE onboarding_task_type IS 'Типы задач адаптации';
COMMENT ON COLUMN onboarding_task_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task_type.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task_type.name IS 'Наименование';
COMMENT ON COLUMN onboarding_task_type.is_exists_is_required IS 'Признак что тип задачи является обязательным';
COMMENT ON COLUMN onboarding_task_type.is_exists_description IS 'Признак что тип задачи должен содержать текстовое описание';
COMMENT ON COLUMN onboarding_task_type.is_exists_days IS 'Признак что тип задачи имеет срок в днях';
COMMENT ON COLUMN onboarding_task_type.is_exists_shifts IS 'Признак того что тип задачи содержит поле для ввода числа смен. (это поле есть в задачах стажировки).';
COMMENT ON COLUMN onboarding_task_type.is_exists_files IS 'Признак что тип задачи может содержать файл';
COMMENT ON COLUMN onboarding_task_type.is_exists_key_results IS 'Признак что тип задачи должен содержать ключевые результаты';

-- onboarding_task_group
CREATE TABLE onboarding_task_group
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from    TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to      TIMESTAMP,
    name         VARCHAR(256)                        NOT NULL,
    task_type_id BIGINT                              NOT NULL,
    is_required  boolean   DEFAULT TRUE              NOT NULL,
    is_checklist boolean   DEFAULT FALSE             NOT NULL,
    index        BIGINT UNIQUE                       NOT NULL,
    CONSTRAINT "onboarding_task_group_task_type_id_fk" FOREIGN KEY (task_type_id) REFERENCES onboarding_task_type (id)
);
COMMENT ON TABLE onboarding_task_group IS 'Блоки адаптации для группировки задач';
COMMENT ON COLUMN onboarding_task_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task_group.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task_group.name IS 'Наименование';
COMMENT ON COLUMN onboarding_task_group.task_type_id IS 'Ссылка на идентификатор task_type, тип таска';
COMMENT ON COLUMN onboarding_task_group.is_required IS 'Признак обязательного поля';
COMMENT ON COLUMN onboarding_task_group.is_checklist IS 'Признак того что поле должно быть чекбоксом';
COMMENT ON COLUMN onboarding_task_group.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
CREATE INDEX onboarding_task_group_task_type_id_idx ON onboarding_task_group (task_type_id);

-- onboarding_task_subgroup
CREATE TABLE onboarding_task_subgroup
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from     TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to       TIMESTAMP,
    name          VARCHAR(256)                        NOT NULL,
    index         BIGINT                              NOT NULL,
    task_group_id BIGINT                              NOT NULL,
    plan_id       BIGINT,
    CONSTRAINT onboarding_task_subgroup_plan_id_index_uq UNIQUE (plan_id, index),
    CONSTRAINT "onboarding_task_subgroup_task_group_id_fk" FOREIGN KEY (task_group_id) REFERENCES onboarding_task_group (id),
    CONSTRAINT "onboarding_task_subgroup_plan_id_fk" FOREIGN KEY (plan_id) REFERENCES onboarding_plan (id)
);
COMMENT ON TABLE onboarding_task_subgroup IS 'Подгруппы для блоков адаптации';
COMMENT ON COLUMN onboarding_task_subgroup.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task_subgroup.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task_subgroup.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task_subgroup.name IS 'Наименование';
COMMENT ON COLUMN onboarding_task_subgroup.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму (integer)';
COMMENT ON COLUMN onboarding_task_subgroup.task_group_id IS 'Идентификатор группы задач адаптации (блока задач в плане адаптации)';
COMMENT ON COLUMN onboarding_task_subgroup.plan_id IS 'Идентификатор типового плана адаптации к которому относится таск';
CREATE INDEX onboarding_task_subgroup_plan_id_idx ON onboarding_task_subgroup (plan_id);
CREATE INDEX onboarding_task_subgroup_task_group_id_idx ON onboarding_task_subgroup (task_group_id);

-- onboarding_task
CREATE TABLE onboarding_task
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from        TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to          TIMESTAMP,
    name             VARCHAR(256)                        NOT NULL,
    description      VARCHAR(2048) DEFAULT NULL::VARCHAR,
    task_subgroup_id BIGINT                              NOT NULL,
    duration_days    BIGINT,
    duration_shifts  BIGINT,
    CONSTRAINT "onboarding_task_task_subgroup_id_fk" FOREIGN KEY (task_subgroup_id) REFERENCES onboarding_task_subgroup (id)
);
COMMENT ON COLUMN onboarding_task.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task.name IS 'Наименование';
COMMENT ON COLUMN onboarding_task.description IS 'Описание';
CREATE INDEX onboarding_task_task_subgroup_id_idx ON onboarding_task (task_subgroup_id);

-- onboarding_task_file
CREATE TABLE onboarding_task_file
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    task_id   BIGINT                              NOT NULL,
    bytes     oid                                 NOT NULL,
    file_name VARCHAR(256)                        NOT NULL,
    file_type VARCHAR(256)                        NOT NULL,
    CONSTRAINT "onboarding_task_file_task_id_fk" FOREIGN KEY (task_id) REFERENCES onboarding_task (id)
);
COMMENT ON COLUMN onboarding_task_file.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task_file.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task_file.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task_file.task_id IS 'Ссылка на идентификатор task, таски';
CREATE INDEX onboarding_task_file_task_id_idx ON onboarding_task_file (task_id);

-- onboarding_task_keyresult
CREATE TABLE onboarding_task_keyresult
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL,
    task_id   BIGINT                              NOT NULL,
    CONSTRAINT "onboarding_task_keyresult_task_id_fk" FOREIGN KEY (task_id) REFERENCES onboarding_task (id)
);
COMMENT ON COLUMN onboarding_task_keyresult.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN onboarding_task_keyresult.date_from IS 'Дата создания записи';
COMMENT ON COLUMN onboarding_task_keyresult.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN onboarding_task_keyresult.name IS 'Наименование';
COMMENT ON COLUMN onboarding_task_keyresult.task_id IS 'Ссылка на идентификатор task, таски';
CREATE INDEX onboarding_task_keyresult_task_id_idx ON onboarding_task_keyresult (task_id);

-- orgstructure_function_style
CREATE TABLE orgstructure_function_style
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    function_id        BIGINT                              NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    params             json      DEFAULT '{}'::json        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP,
    update_employee_id BIGINT
);

-- portal_news_type
CREATE TABLE portal_news_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(128)                        NOT NULL,
    unit_code VARCHAR(128) DEFAULT NULL::VARCHAR  NOT NULL
);
COMMENT ON TABLE portal_news_type IS 'Таблица для типа новостей';
COMMENT ON COLUMN portal_news_type.id IS 'Уникальный идентификационный номер типа новости.';
COMMENT ON COLUMN portal_news_type.name IS 'Название типа новости.';
COMMENT ON COLUMN portal_news_type.unit_code IS 'Код юнита';

-- portal_news
CREATE TABLE portal_news
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY    NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    name               VARCHAR(128)                           NOT NULL,
    description        VARCHAR(2048),
    type               BIGINT                                 NOT NULL,
    image_url          VARCHAR(256),
    is_viewing         boolean,
    update_date        TIMESTAMP                              NOT NULL,
    author_employee_id BIGINT                                 NOT NULL,
    update_employee_id BIGINT                                 NOT NULL,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    text               text,
    unit_code          VARCHAR(128) DEFAULT NULL::VARCHAR     NOT NULL,
    CONSTRAINT "portal_news_type_fk" FOREIGN KEY (type) REFERENCES portal_news_type (id)
);
COMMENT ON TABLE portal_news IS 'Таблица для новостей';
COMMENT ON COLUMN portal_news.id IS 'Уникальный идентификационный номер новости.';
COMMENT ON COLUMN portal_news.date_from IS 'Дата и время создания новости.';
COMMENT ON COLUMN portal_news.date_to IS 'Дата и время удаления новости.';
COMMENT ON COLUMN portal_news.name IS 'Название новости.';
COMMENT ON COLUMN portal_news.description IS 'Описание новости.';
COMMENT ON COLUMN portal_news.type IS 'Тип новости.';
COMMENT ON COLUMN portal_news.image_url IS 'Ссылка на картинку новости.';
COMMENT ON COLUMN portal_news.is_viewing IS 'Видимость новости, флаг который показывает новость или скрывает';
COMMENT ON COLUMN portal_news.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN portal_news.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN portal_news.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN portal_news.date_start IS 'Запланированная дата начала новости.';
COMMENT ON COLUMN portal_news.date_end IS 'Запланированная дата закрытие новости.';
COMMENT ON COLUMN portal_news.text IS 'Содержание новости.';
COMMENT ON COLUMN portal_news.unit_code IS 'Код юнита';
CREATE INDEX portal_news_type_idx ON portal_news (type);

-- pst_activity_form_group
CREATE TABLE pst_activity_form_group
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL
);
COMMENT ON COLUMN pst_activity_form_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN pst_activity_form_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_activity_form_group.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_activity_form_group.name IS 'Наименование';

-- pst_activity_form
CREATE TABLE pst_activity_form
(
    date_from TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to   TIMESTAMP,
    code      VARCHAR(16)             NOT NULL PRIMARY KEY,
    name      VARCHAR(256)            NOT NULL,
    group_id  BIGINT                  NOT NULL,
    CONSTRAINT "pst_activity_form_group_id_fk" FOREIGN KEY (group_id) REFERENCES pst_activity_form_group (id)
);
COMMENT ON COLUMN pst_activity_form.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_activity_form.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_activity_form.code IS 'Системный код записи';
COMMENT ON COLUMN pst_activity_form.name IS 'Наименование';
COMMENT ON COLUMN pst_activity_form.group_id IS 'Идентификатор группы ВД';
CREATE INDEX pst_activity_form_group_id_idx ON pst_activity_form (group_id);

-- pst_profession
CREATE TABLE pst_profession
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to   TIMESTAMP,
    name      VARCHAR(256)                        NOT NULL
);
COMMENT ON COLUMN pst_profession.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN pst_profession.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_profession.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_profession.name IS 'Наименование';

-- pst_profstandard
CREATE TABLE pst_profstandard
(
    code               VARCHAR(16) PRIMARY KEY NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to            TIMESTAMP,
    name               VARCHAR(256)            NOT NULL,
    activity_form_code VARCHAR(16)             NOT NULL,
    CONSTRAINT "pst_profstandard_activity_form_code_fk" FOREIGN KEY (activity_form_code) REFERENCES pst_activity_form (code)
);
COMMENT ON COLUMN pst_profstandard.code IS 'Системный код записи';
COMMENT ON COLUMN pst_profstandard.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_profstandard.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_profstandard.name IS 'Наименование';
COMMENT ON COLUMN pst_profstandard.activity_form_code IS 'Код вида деятельности';
CREATE INDEX pst_profstandard_activity_form_code_idx ON pst_profstandard (activity_form_code);

-- pst_qualification_level
CREATE TABLE pst_qualification_level
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    name                   VARCHAR(256)                        NOT NULL,
    authority              VARCHAR(1024)                       NOT NULL,
    requirement_skills     VARCHAR(1024)                       NOT NULL,
    requirement_knowledges VARCHAR(1024)                       NOT NULL,
    requirement_actions    VARCHAR(1024)                       NOT NULL
);
COMMENT ON COLUMN pst_qualification_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN pst_qualification_level.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_qualification_level.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_qualification_level.name IS 'Наименование';
COMMENT ON COLUMN pst_qualification_level.authority IS 'Компетенции';
COMMENT ON COLUMN pst_qualification_level.requirement_skills IS 'Требуемые навыки';
COMMENT ON COLUMN pst_qualification_level.requirement_knowledges IS 'Требуемые знания';
COMMENT ON COLUMN pst_qualification_level.requirement_actions IS 'Требуемые действия';

-- pst_work_function
CREATE TABLE pst_work_function
(
    code                   VARCHAR(16) PRIMARY KEY NOT NULL,
    profstandard_code      VARCHAR(16)             NOT NULL,
    date_from              TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to                TIMESTAMP,
    parent_id              BIGINT                  NOT NULL,
    name                   VARCHAR(256)            NOT NULL,
    qualification_level_id BIGINT                  NOT NULL,
    CONSTRAINT pst_work_function_code_profstandard_code_uq UNIQUE (code, profstandard_code),
    CONSTRAINT "pst_work_function_profstandard_code_fk" FOREIGN KEY (profstandard_code) REFERENCES pst_profstandard (code),
    CONSTRAINT "pst_work_function_qualification_level_id_fk" FOREIGN KEY (qualification_level_id) REFERENCES pst_qualification_level (id)
);
COMMENT ON COLUMN pst_work_function.code IS 'Системный код записи';
COMMENT ON COLUMN pst_work_function.profstandard_code IS 'Код профстандарта';
COMMENT ON COLUMN pst_work_function.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_work_function.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_work_function.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN pst_work_function.name IS 'Наименование';
COMMENT ON COLUMN pst_work_function.qualification_level_id IS 'Идентификатор уровня квалификации';
CREATE INDEX pst_work_function_profstandard_code_idx ON pst_work_function (profstandard_code);
CREATE INDEX pst_work_function_qualification_level_id_idx ON pst_work_function (qualification_level_id);

-- pst_work_function_profession
CREATE TABLE pst_work_function_profession
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    work_function_code VARCHAR(16)                         NOT NULL,
    profession_id      BIGINT                              NOT NULL,
    CONSTRAINT "pst_work_function_profession_work_function_code_fk" FOREIGN KEY (work_function_code) REFERENCES pst_work_function (code),
    CONSTRAINT "pst_work_function_profession_profession_id_fk" FOREIGN KEY (profession_id) REFERENCES pst_profession (id)
);
COMMENT ON COLUMN pst_work_function_profession.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN pst_work_function_profession.date_from IS 'Дата создания записи';
COMMENT ON COLUMN pst_work_function_profession.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN pst_work_function_profession.work_function_code IS 'Код рабочей функции профстандарта';
COMMENT ON COLUMN pst_work_function_profession.profession_id IS 'Идентификатор профессий';
CREATE INDEX pst_work_function_profession_work_function_code_idx ON pst_work_function_profession (work_function_code);
CREATE INDEX pst_work_function_profession_profession_id_idx ON pst_work_function_profession (profession_id);

-- reason_type
CREATE TABLE reason_type
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(128) UNIQUE                 NOT NULL
);
COMMENT ON TABLE reason_type IS 'Типы причин отмены аттестации';
COMMENT ON COLUMN reason_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN reason_type.name IS 'Наименование';

-- reason
CREATE TABLE reason
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    type_id     smallint                            NOT NULL,
    name        VARCHAR(128)                        NOT NULL,
    description VARCHAR(1024) DEFAULT ''::VARCHAR   NOT NULL,
    date_from   TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to     TIMESTAMP,
    CONSTRAINT "reason_type_id_idx" FOREIGN KEY (type_id) REFERENCES reason_type (id)
);
COMMENT ON TABLE reason IS 'Причины отмены аттестации';
COMMENT ON COLUMN reason.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN reason.type_id IS 'Ссылка на идентификатор reason_type, типы причин для работы с работниками аттестации';
COMMENT ON COLUMN reason.name IS 'Наименование';
COMMENT ON COLUMN reason.description IS 'Описание';
COMMENT ON COLUMN reason.date_from IS 'Дата создания записи';
COMMENT ON COLUMN reason.date_to IS 'Дата окончания записи';
CREATE INDEX reason_type_id_idx ON reason (type_id);

-- side_panel_menu
CREATE TABLE side_panel_menu
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    number     VARCHAR(128)                        NOT NULL,
    name       VARCHAR(128)                        NOT NULL,
    role_id    BIGINT,
    process_id BIGINT,
    payload    VARCHAR(1024)                       NOT NULL,
    parent_id  BIGINT,
    CONSTRAINT "side_panel_menu_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES side_panel_menu (id)
);
COMMENT ON COLUMN side_panel_menu.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN side_panel_menu.name IS 'Наименование';
COMMENT ON COLUMN side_panel_menu.role_id IS 'Ссылка на идентификатор role, роли';
COMMENT ON COLUMN side_panel_menu.process_id IS 'Ссылка на идентификатор process, процесс';
COMMENT ON COLUMN side_panel_menu.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
CREATE INDEX side_panel_menu_parent_id_idx ON side_panel_menu (parent_id);

-- worker_project
CREATE TABLE worker_project
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    project_type BIGINT,
    project_name VARCHAR(256)                        NOT NULL
);
COMMENT ON TABLE worker_project IS 'Проекты для проектного опыта';
COMMENT ON COLUMN worker_project.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN worker_project.project_type IS 'Тип проекта';
COMMENT ON COLUMN worker_project.project_name IS 'Наименование проекта';

-- worker_project_activity
CREATE TABLE worker_project_activity
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    project_activity VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE worker_project_activity IS 'Направления деятельности для проектного опыта';
COMMENT ON COLUMN worker_project_activity.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN worker_project_activity.project_activity IS 'Наименование вида деятельности, в рамках которой реализуется проект';

-- worker_project_activity_competence
CREATE TABLE worker_project_activity_competence
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    worker_project_activity_id BIGINT,
    competence_id              BIGINT,
    date_from                  TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                    TIMESTAMP,
    CONSTRAINT "worker_project_activity_competence_wpa_id_fk" FOREIGN KEY (worker_project_activity_id) REFERENCES worker_project_activity (id)
);
COMMENT ON TABLE worker_project_activity_competence IS 'Связь компетенции и направления деятельности';
COMMENT ON COLUMN worker_project_activity_competence.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN worker_project_activity_competence.competence_id IS 'Ссылка на идентификатор competence, справочник компетенций';
COMMENT ON COLUMN worker_project_activity_competence.date_from IS 'Дата создания записи';
COMMENT ON COLUMN worker_project_activity_competence.date_to IS 'Дата окончания записи';
CREATE INDEX worker_project_activity_competence_wpa_if_idx ON worker_project_activity_competence (worker_project_activity_id);

-- worker_project_role
CREATE TABLE worker_project_role
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    project_role VARCHAR(128)                        NOT NULL
);
COMMENT ON TABLE worker_project_role IS 'Роль на проекте для проектного опыта';
COMMENT ON COLUMN worker_project_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN worker_project_role.project_role IS 'Проектная роль';
