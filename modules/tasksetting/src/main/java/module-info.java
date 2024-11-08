module vkpht.main.mod.tasksetting {
    requires vkpht.main.common;
    requires com.zaxxer.hikari;
    requires jakarta.persistence;
    requires liquibase.core;
    requires lombok;
    requires org.slf4j;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.orm;
    requires spring.tx;
    requires spring.beans;
    requires spring.data.jpa;
    exports me.goodt.vkpht.module.tasksetting.api;
}