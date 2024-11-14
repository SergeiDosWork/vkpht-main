module vkpht.main.mod.orgstructure {
    requires vkpht.main.common;
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

    exports me.goodt.vkpht.module.orgstructure.api;
    exports me.goodt.vkpht.module.orgstructure.api.dto;
    exports me.goodt.vkpht.module.orgstructure.api.dto.passport;
    exports me.goodt.vkpht.module.orgstructure.api.dto.projection;
    exports me.goodt.vkpht.module.orgstructure.api.dto.request;

    opens me.goodt.vkpht.module.orgstructure.config to spring.core, spring.beans, spring.context;
}