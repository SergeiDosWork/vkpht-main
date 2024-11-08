package me.goodt.vkpht.common.dictionary.core.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity<ID> implements Serializable {

    public abstract ID getId();
}
