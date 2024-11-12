module vkpht.main.mod.notification {
    requires vkpht.main.common;
    requires vkpht.main.mod.orgstructure;
    requires vkpht.main.mod.tasksetting;
    requires auth.sur.core;

    requires com.zaxxer.hikari;
    requires jakarta.persistence;
    requires liquibase.core;
    requires lombok;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.orm;
    requires spring.tx;
    requires spring.beans;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.web;
    requires spring.hateoas;
    requires spring.security.core;
    requires io.swagger.v3.oas.annotations;
    requires org.springdoc.openapi.common;
    requires com.querydsl.core;
    requires com.querydsl.jpa;
    requires org.apache.commons.collections4;
    requires com.fasterxml.jackson.annotation;
    requires org.mapstruct;
    requires spring.webmvc;
    requires spring.kafka;
    requires kafka.clients;
    requires org.json;

    exports me.goodt.vkpht.module.notification.api;
    exports me.goodt.vkpht.module.notification.api.dto;
}