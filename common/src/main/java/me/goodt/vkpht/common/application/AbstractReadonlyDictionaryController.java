package me.goodt.vkpht.common.application;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.MethodNotAllowedException;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractReadonlyDictionaryController<I extends Serializable, R>
        extends AbstractDictionaryController<I, R> {

    @Override
    public R create(R res) {
        throw new MethodNotAllowedException(HttpMethod.POST, List.of(HttpMethod.GET));
    }

    @Override
    public R update(I id, R res) {
        throw new MethodNotAllowedException(HttpMethod.PUT, List.of(HttpMethod.GET));
    }

    @Override
    public ResponseEntity<Void> delete(I id) {
        throw new MethodNotAllowedException(HttpMethod.DELETE, List.of(HttpMethod.GET));
    }
}
