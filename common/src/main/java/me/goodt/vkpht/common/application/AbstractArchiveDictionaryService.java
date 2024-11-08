package me.goodt.vkpht.common.application;

import me.goodt.vkpht.common.application.exception.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;
import me.goodt.vkpht.common.domain.entity.ArchivableEntity;

public abstract class AbstractArchiveDictionaryService<T extends AbstractEntity<I> & ArchivableEntity, I extends Serializable>
        extends AbstractDictionaryService<T, I> {

    @Override
    protected AbstractArchivableDao<T, I> getDao() {
        return getArchivableDao();
    }

	protected abstract <D extends AbstractArchivableDao<T, I>> D getArchivableDao();

    @Transactional(readOnly = true)
    public T findById(I id) {
        return findActualById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Entity %s not found by id %s", this.getEntityClazz(), id)));
    }

    @Override
    public <R> Page<R> getAll(Pageable paging, Converter<T, R> converter) {
        Page<T> page = getArchivableDao().findAllActual(paging);
        return page.map(converter::convert);
    }

    @Override
    public void delete(I id) {
        T entity = findActualById(id).orElseThrow(() ->
                new NotFoundException(String.format("Entity with id = %s not found or already has been deleted", id)));
        entity.setDateTo(new Date());
        getDao().save(entity);
    }

    protected Optional<T> findActualById(I id) {
        return getArchivableDao()
            .findById(id)
            .filter(entity -> entity.getDateTo() == null);

    }
}
