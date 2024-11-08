package me.goodt.vkpht.common.dictionary.core.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

public abstract class AbstractAsm<E, R extends AbstractRes<?>> {

    public abstract R toRes(E entity);

    public List<R> toList(List<E> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        List<R> rez = new ArrayList<>(list.size());
        for (E e : list) {
            rez.add(toRes(e));
        }
        return rez;
    }

    public abstract void create(E entity, R res);

    public abstract void update(E entity, R res);
}
