package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.http.ResponseEntity;

public interface CrudController<I, R> {

    R getById(I id);

    R create(R res);

    R update(I id, R res);

    ResponseEntity<Void> delete(I id);
}
