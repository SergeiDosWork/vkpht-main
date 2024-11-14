package me.goodt.vkpht.common.dictionary.core.service;

import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@FunctionalInterface
public interface Updater<T extends AbstractEntity<?>> {

    /**
     * Updates domain object which is represented by the given argument.
     *
     * @param domainObject the domain object
     */
    void update(T domainObject);
}
