package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.IntDomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_position_importance")
public class PositionImportanceEntity extends IntDomainObject implements ArchivableEntity {

    @Column(name = "name", nullable = false, length = 128)
    private String name;
    @Column(name = "successor_count_max", nullable = false)
    @ColumnDefault(value = "3")
    private Long successorCountMax;
    @Column(name = "successor_count_rec", nullable = false)
    @ColumnDefault(value = "3")
    private Long successorCountRec;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "index", nullable = false)
    private Long index;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
    @Column(name = "unit_code", nullable = false, length = 128)
    private String unitCode;

    public PositionImportanceEntity(Integer id, String name, Long successorCountMax, Long successorCountRec,
                                    String description, Long index) {
        setId(id);
        this.name = name;
        this.successorCountMax = successorCountMax;
        this.successorCountRec = successorCountRec;
        this.description = description;
        this.index = index;
    }
}
