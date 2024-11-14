package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import com.goodt.drive.auth.sur.unit.IncorrectUnitException;
import me.goodt.vkpht.module.orgstructure.api.dto.UnitDto;
import me.goodt.vkpht.module.orgstructure.api.dto.request.CurrentUnitRequest;

/**
 * Интерфейс, предоставляющий для внешнего использования ряд методов по работе с бизнес-единицам (юнитами) сотрудника.
 */
public interface UserUnitService {

    /**
     * Возвращает текущую бизнес-единицу (юнит) пользователя. Осуществляет проверку доступа к данной бизнес-единице.
     *
     * @return объект {@link UnitDto}, представляющий информацию о текущей бизнес-единице
     * @throws IncorrectUnitException если у сотрудника выбрана некорректная или недоступная бизнес-единица (юнит).
     */
    UnitDto getCurrentUnit();

    /**
     * Возвращает список всех доступных бизнес-единиц (юнитов) для сотрудника.
     *
     * @return список объектов {@link UnitDto}, содержащих краткую информацию о доступных юнитах.
     */
    List<UnitDto> getAvailableUnits();

    /**
     * Проверяет доступность бизнес-единицы (юнита) для сотрудника.
     *
     * @param unitCode код бизнес-единицы (юнита)
     * @return {@code true}, если доступ к данным по полученной бизнес-единица доступен;
     * {@code false} в противном случае
     */
    boolean isUnitAvailable(String unitCode);

    /**
     * Устанавливает код полученной бизнес-единицы (юнита)
     *
     * @param request тело запроса с информацией о бизнес-единице (юните)
     * @throws IncorrectUnitException если у сотрудника выбрана некорректная или недоступная бизнес-единица (юнит)
     *                                или же если запрос был выполнен не от лица сотрудника.
     */
    void setCurrentUnit(CurrentUnitRequest request);
}
