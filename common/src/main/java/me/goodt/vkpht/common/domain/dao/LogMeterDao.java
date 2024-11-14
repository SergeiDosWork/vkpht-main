package me.goodt.vkpht.common.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.entity.LogMeter;

/** @author iGurkin - 16.03.2023 */
@Repository
public class LogMeterDao extends AbstractDao<LogMeter, Long> {
    public LogMeterDao(EntityManager em) {
        super(LogMeter.class, em);
    }
}
