module vkpht.main.common {
    requires transitive jakarta.persistence;
    requires transitive jakarta.validation;
    requires transitive lombok;
    requires transitive org.slf4j;
    requires transitive org.apache.commons.lang3;

    requires spring.data.commons;
    requires spring.web;
    requires spring.beans;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.data.jpa;
    requires com.fasterxml.jackson.databind;
    requires java.compiler;
    requires json.path;
    requires com.github.victools.jsonschema.generator;
    requires com.github.victools.jsonschema.module.jackson;
    requires com.github.victools.jsonschema.module.javax.validation;
    requires com.querydsl.core;
    requires com.querydsl.jpa;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.orm;
    requires spring.webmvc;
    requires spring.security.core;
    requires io.swagger.v3.oas.annotations;

    exports me.goodt.vkpht.common;
    exports me.goodt.vkpht.common.api.annotation;
    exports me.goodt.vkpht.common.api.dto;
    exports me.goodt.vkpht.common.domain.dao;
    exports me.goodt.vkpht.common.domain.entity;
    exports me.goodt.vkpht.common.domain.mapper;
    exports me.goodt.vkpht.common.application;
    exports me.goodt.vkpht.common.application.exception;
    exports me.goodt.vkpht.common.application.util;
    exports me.goodt.vkpht.common.dictionary.core.asm;
    exports me.goodt.vkpht.common.dictionary.core.dto;
    exports me.goodt.vkpht.common.dictionary.core.controller;
    exports me.goodt.vkpht.common.dictionary.core.dao;
    exports me.goodt.vkpht.common.dictionary.core.service;
}