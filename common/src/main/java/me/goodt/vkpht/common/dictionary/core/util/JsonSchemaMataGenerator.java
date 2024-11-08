package me.goodt.vkpht.common.dictionary.core.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationOption;

import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

/**
 * Реализация DictionaryMetaBuilder для создания мета-информации в формате JSON-Schema.
 */
public class JsonSchemaMataGenerator implements DictionaryMetaGenerator {

    private final SchemaGenerator generator;

    /**
     * Создает новый экземпляр JsonSchemaMataGenerator с конфигурацией по умолчанию.
     */
    public JsonSchemaMataGenerator() {
        SchemaGeneratorConfig config = createDefaultBuilder().build();
        this.generator = new SchemaGenerator(config);
    }

    /**
     * Создает новый экземпляр JsonSchemaMataGenerator с предоставленной внешней конфигурацией.
     */
    public JsonSchemaMataGenerator(SchemaGeneratorConfig config) {
        this.generator = new SchemaGenerator(config);
    }

    protected SchemaGeneratorConfigBuilder createDefaultBuilder() {
        JavaxValidationModule module = new JavaxValidationModule(
            JavaxValidationOption.INCLUDE_PATTERN_EXPRESSIONS,
            JavaxValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED
        );

        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
            .with(module)
            .with(new JacksonModule());
    }

    @Override
    public ObjectNode generate(Class<?> dtoClass) {
        return this.generator.generateSchema(dtoClass);
    }
}
