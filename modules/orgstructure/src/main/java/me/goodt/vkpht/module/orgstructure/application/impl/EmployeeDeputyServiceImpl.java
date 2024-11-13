package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.Getter;

import lombok.RequiredArgsConstructor;

import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import me.goodt.vkpht.common.api.exception.ForbiddenException;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDeputyDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDeputyEntity;

@Getter
@RequiredArgsConstructor
@Service
@Transactional
public class EmployeeDeputyServiceImpl extends AbstractCrudService<EmployeeDeputyEntity, Long> {

    private final EmployeeDeputyDao dao;

    @Transactional(readOnly = true)
    public Page<EmployeeDeputyEntity> getAll(Long employeeId, Date date, Pageable pageable) {
        return dao.findAllByParams(employeeId, date, pageable);
    }

    @Override
    protected void beforeUpdate(EmployeeDeputyEntity entity) {
        if (entity.getDateTo() != null && !entity.getDateTo().after(new Date())) {
            throw new ForbiddenException(String.format("employee_deputy with id=%d is already closed ", entity.getId()));
        }
    }
}
