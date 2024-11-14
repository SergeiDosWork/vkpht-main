package me.goodt.vkpht.module.notification.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Repository
public interface NativeDao extends JpaRepository<DomainObject, Long> {

    @Query(value = "select concat(table_schema, '.', TABLE_NAME) from INFORMATION_SCHEMA.VIEWS " +
        "WHERE table_schema IN :schemas", nativeQuery = true)
    List<String> getAllView(@Param("schemas") List<String> schemas);
}
