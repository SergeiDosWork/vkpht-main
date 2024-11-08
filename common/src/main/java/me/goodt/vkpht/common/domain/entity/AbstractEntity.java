package me.goodt.vkpht.common.domain.entity;

import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

/** @author iGurkin - 24.10.2020 */
@MappedSuperclass
public abstract class AbstractEntity<ID> implements Serializable {

    public abstract ID getId();
}