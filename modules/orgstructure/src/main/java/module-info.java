module vkpht.main.mod.orgstructure {
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

    opens me.goodt.vkpht.module.orgstructure.config to spring.core, spring.beans, spring.context;

    //    exports me.goodt.vkpht.module.orgstructure.api;
}