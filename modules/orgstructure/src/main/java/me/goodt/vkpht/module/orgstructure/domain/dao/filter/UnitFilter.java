package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Getter
@Builder
public class UnitFilter {

    public static UnitFilter asDefault() {
        return UnitFilter.builder().build();
    }

    /**
     * Коллекция уникальных кодов бизнес-единиц (юнитов)
     */
    private Collection<String> codes;

    /**
     * Признак актуальности(активной или неудаленной) записи.
     *
     * <p>По умолчанию считается значение {@code true} - поиск только по актуальным записям.
     *
     * <ul>
     *     <li>{@code null} - поиск всех записей без учета признака удаления;
     *     <li>{@code true} - поиск только актуальных, у которых не установлено значение "date_to";
     *     <li>{@code false} - поиск только удаленных. Записи у которых установлено значение "date_to".
     * </ul>
     */
    @Builder.Default
    private Boolean actual = true;
}
