package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.goodt.drive.auth.security.UserDetails;
import com.goodt.drive.auth.sur.unit.IncorrectUnitException;
import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.auth.sur.unit.UnitStorage;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.module.orgstructure.api.UserUnitService;
import me.goodt.vkpht.module.orgstructure.api.dto.UnitDto;
import me.goodt.vkpht.module.orgstructure.api.dto.request.CurrentUnitRequest;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.UnitDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.UnitFilter;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserUnitServiceImpl implements UserUnitService {

    private final UnitAccessService unitService;
    private final UnitStorage unitStorage;
    private final AuthService authService;
    private final EmployeeDao employeeDao;
    private final UnitDao unitDao;

    @Override
    public UnitDto getCurrentUnit() {
        String currentUnitCode = unitService.getCurrentUnit();
        if (!isUnitAvailable(currentUnitCode)) {
            throw new IncorrectUnitException("Доступ к запрашиваемой бизнес-единице (юниту) запрещен.");
        }

        return unitDao.findById(currentUnitCode)
                .filter(e -> e.getDateTo() == null)
                .map(e -> new UnitDto(e.getCode(), e.getName(), e.getDescription()))
                .orElseThrow(() -> new IncorrectUnitException("Бизнес-единица (юнит) = "
                        + currentUnitCode + " не найдена."));
    }

    @Override
    public List<UnitDto> getAvailableUnits() {
        UserDetails currentUser = authService.getCurrentUser();

        if (Boolean.TRUE.equals(currentUser.isTechUser())) {
            // Для системного пользователя доступны все актуальные бизнес-единицы в системе.
            return unitDao.find(UnitFilter.asDefault())
                    .stream()
                    .map(e -> new UnitDto(e.getCode(), e.getName(), e.getDescription()))
                    .collect(Collectors.toList());
        }

        String externalEmployeeId = currentUser.getEmployeeExternalId();
        if (externalEmployeeId == null) {
            log.info("Запрос выполняется не от лица сотрудника, " +
                    "информация о доступных бизнес-единицах (юнитах) не предоставляется.");
            return Collections.emptyList();
        }
        return employeeDao.findAvailableUnits(externalEmployeeId).stream()
                .map(proj -> new UnitDto(proj.getCode(), proj.getName(), proj.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUnitAvailable(String unitCode) {
        UserDetails currentUser = authService.getCurrentUser();
        if (Boolean.TRUE.equals(currentUser.isTechUser())) {
            // Если запрос приходит от системного пользователя, то связь в БД между employee -> unit не учитывается.
            // Выполняется обычная проверка наличия полученного юнит-кода в БД.
            var filter = UnitFilter.builder()
                    .codes(Collections.singletonList(unitCode))
                    .build();
            return unitDao.exists(filter);
        }

        String externalEmployeeId = currentUser.getEmployeeExternalId();
        if (externalEmployeeId == null) {
            log.info("Выполнение запроса произведено не от лица сотрудника.");
            return false;
        }

        return employeeDao.isUnitAvailable(unitCode, externalEmployeeId);
    }

    @Override
    public void setCurrentUnit(CurrentUnitRequest request) {
        String unitCode = request.getUnitCode();

        if (!isUnitAvailable(unitCode)) {
            throw new IncorrectUnitException("Доступ к запрашиваемой бизнес-единице (юниту) = "
                    + unitCode + " запрещен.");
        }

        unitStorage.setUnitCode(unitCode);
    }
}
