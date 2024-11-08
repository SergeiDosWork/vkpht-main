module vkpht.main.common {
    requires spring.data.commons;
    requires spring.web;
    requires spring.beans;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.data.jpa;
    requires lombok;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires jakarta.persistence;
    requires java.compiler;
    requires json.path;
    requires com.github.victools.jsonschema.generator;
    requires com.github.victools.jsonschema.module.jackson;
    requires com.github.victools.jsonschema.module.javax.validation;
    requires com.querydsl.core;
    requires com.querydsl.jpa;

    exports me.goodt.vkpht.common;
}