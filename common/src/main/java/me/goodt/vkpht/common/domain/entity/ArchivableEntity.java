package me.goodt.vkpht.common.domain.entity;

import java.util.Date;

public interface ArchivableEntity {
    
    Date getDateFrom();
    
    Date getDateTo();

    void setDateTo(Date dateTo);
}
