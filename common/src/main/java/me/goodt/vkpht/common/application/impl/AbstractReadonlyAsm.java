package me.goodt.vkpht.common.application.impl;

import me.goodt.vkpht.common.application.asm.AbstractAsm;

public abstract class AbstractReadonlyAsm<E, R> extends AbstractAsm<E, R> {

    @Override
    public void create(E entity, R res) {
        throw new UnsupportedOperationException("The create operation is not supported for this dictionary");
    }

    @Override
    public void update(E entity, R res) {
        throw new UnsupportedOperationException("The update operation is not supported for this dictionary");
    }
}
