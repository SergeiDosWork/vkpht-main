package me.goodt.vkpht.module.orgstructure.application;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import com.goodt.drive.rtcore.data.EmployeeSearchResult;
import com.goodt.drive.rtcore.security.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.application.util.ExpiredConcurrentHashMap;
import me.goodt.vkpht.common.application.util.GlobalDefs;
import me.goodt.vkpht.common.application.util.UtilClass;
import me.goodt.vkpht.common.domain.dao.NativeDao;
import me.goodt.vkpht.module.orgstructure.api.IAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.IEmployeeService;
import me.goodt.vkpht.module.orgstructure.api.dto.CompactAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivTeamRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionAssignmentRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentWithDivisionTeamFullDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.DivisionTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDeputyDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentShort;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleShort;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamShort;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamAssignmentFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamAssignmentRotationFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamRoleFactory;

import static java.util.stream.Collectors.toList;
import static me.goodt.vkpht.common.application.util.GlobalDefs.HEAD_SYSTEM_ROLE_ID;

@Slf4j
@Service
public class AssignmentServiceImpl implements IAssignmentService {

    private static final Integer HSR_ID = HEAD_SYSTEM_ROLE_ID;

    @Autowired
    private DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    @Autowired
    private DivisionTeamAssignmentRotationDao divisionTeamAssignmentRotationDao;
    @Autowired
    private PositionAssignmentDao positionAssignmentDao;
    @Autowired
    private DivisionTeamDao divisionTeamDao;
    @Autowired
    private DivisionTeamRoleDao divisionTeamRoleDao;
    @Autowired
    private NativeDao nativeDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EmployeeDeputyDao employeeDeputyDao;
    @Autowired
    @Qualifier("assignments")
    private ExpiredConcurrentHashMap<DivisionTeamAssignmentKey, List<DivisionTeamAssignmentEntity>> assignmentsCache;
    @Autowired
    @Qualifier("employeeAssignments")
    private ExpiredConcurrentHashMap<Long, DivisionTeamAssignmentDto> employeeAssignments;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    public List<DivisionTeamAssignmentEntity> getTeamAssignments(Long employeeId) {
        return divisionTeamAssignmentDao.findByEmployeeId(employeeId);
    }

    @Override
    public DivisionTeamAssignmentEntity getOneTeamAssignment(Long employeeId, Long divisionTeamId) throws NotFoundException {
        DivisionTeamAssignmentEntity entity = divisionTeamAssignmentDao.findByEmployeeIdAnDivisionTeamId(employeeId, divisionTeamId);
        if (entity == null) {
            throw new NotFoundException(String.format("division team assignment for employee with id=%d and division team with id=%d is not found", employeeId, divisionTeamId));
        }
        return entity;
    }

    @Override
    public List<DivisionTeamAssignmentEntity> getHeadAssignment(Long divisionTeamId) {
        return divisionTeamAssignmentDao.fetchByDivisionTeamIdAndSystemRoleId(divisionTeamId, HSR_ID);
    }

    @Override
    public DivisionTeamAssignmentEntity getDivisionTeamAssignment(Long id) {
        return divisionTeamAssignmentDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Division team assignment %d is not found", id)));
    }

    private List<DivisionTeamAssignmentDto> getAssignmentsCreateWithJobInfo(Collection<DivisionTeamAssignmentEntity> dtaList, boolean withAssignments, boolean withRotations, boolean withEmployee, boolean withDtr) {
        List<Long> assignmentIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();

        dtaList.forEach(dta -> {
            assignmentIds.add(dta.getId());
            employeeIds.add(dta.getEmployeeId());
        });
        StopWatch sw = StopWatch.createStarted();
        final Map<Long, List<DivisionTeamAssignmentRotationEntity>> rotations = withRotations ? divisionTeamAssignmentRotationDao.findActualByAssignmentIds(assignmentIds) : new HashMap<>();
        sw.stop();
        log.info("getAssignmentsCreateWithJobInfo load divisionTeamAssignmentRotationDao {}", sw.getTime());
        sw = StopWatch.createStarted();
        final Map<Long, List<PositionAssignmentEntity>> positionAssignments = withAssignments
            ? positionAssignmentDao.findActualByEmployeeIds(employeeIds, unitAccessService.getCurrentUnit())
            : new HashMap<>();
        sw.stop();
        log.info("getAssignmentsCreateWithJobInfo load positionAssignmentDao {}", sw.getTime());

        return dtaList.stream().map(d -> DivisionTeamAssignmentFactory.createWithJobInfoAndFlags(
                        d,
                        withAssignments ? positionAssignments.getOrDefault(d.getEmployeeId(), Collections.emptyList()) : Collections.emptyList(),
                        withRotations ? rotations.getOrDefault(d.getId(), Collections.emptyList()).stream().map(DivisionTeamAssignmentRotationFactory::createShort).collect(toList()) : Collections.emptyList(),
                        withEmployee,
                        withDtr))
                .collect(toList());
    }

    private List<DivisionTeamAssignmentShortDto> getShortAssignmentsCreateWithJobInfo(Collection<DivisionTeamAssignmentShort> dtaList, boolean withAssignments,
                                                                                      boolean withRotations, boolean withEmployee, boolean withDtr) {
        List<Long> assignmentIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();

        dtaList.forEach(dta -> {
            assignmentIds.add(dta.getId());
            employeeIds.add(dta.getEmployeeId());
        });
        StopWatch sw = StopWatch.createStarted();
        final Map<Long, List<DivisionTeamAssignmentRotationEntity>> rotations = withRotations ? divisionTeamAssignmentRotationDao.findActualByAssignmentIds(assignmentIds) : new HashMap<>();
        sw.stop();
        log.info("getAssignmentsCreateWithJobInfo load divisionTeamAssignmentRotationDao {}", sw.getTime());
        sw = StopWatch.createStarted();
        final Map<Long, List<PositionAssignmentEntity>> positionAssignments = withAssignments
            ? positionAssignmentDao.findActualByEmployeeIds(employeeIds, unitAccessService.getCurrentUnit())
            : new HashMap<>();
        sw.stop();
        log.info("getAssignmentsCreateWithJobInfo load positionAssignmentDao {}", sw.getTime());

        Set<Long> emplyeIds = withEmployee ? dtaList.stream().map(d -> d.getEmployeeId()).collect(Collectors.toSet()) : new HashSet<>(0);
        sw = StopWatch.createStarted();
        Map<Long, EmployeeEntity> emplMap = employeeDao.findAllByIds(emplyeIds).stream().collect(Collectors.toMap(e -> e.getId(), e -> e, (e1, e2) -> e2));
        sw.stop();
        log.info("getAssignmentsCreateWithJobInfo employeeDao.findAllById time {}", sw.getTime());

        List<Long> dtrIds = dtaList.stream().map(d -> d.getDivisionTeamRoleId()).distinct().collect(toList());
        final QDivisionTeamRoleShort qdtrs = QDivisionTeamRoleShort.divisionTeamRoleShort;
        final QDivisionTeamShort qdts = QDivisionTeamShort.divisionTeamShort;
        List<Tuple> divTeamRoles = divisionTeamRoleDao.findAllByIds(dtrIds).select(qdtrs, qdts).fetch();
        final Map<Long, DivisionTeamRoleShort> dtrMap = new HashMap<>();
        final Map<Long, DivisionTeamShort> dtMap = new HashMap<>();
        for (Tuple t : divTeamRoles) {
            DivisionTeamRoleShort dtr = t.get(qdtrs);
            dtrMap.put(dtr.getId(), dtr);
            dtMap.put(dtr.getId(), t.get(qdts));
        }

        return dtaList.stream().map(d -> {
            DivisionTeamAssignmentShortDto dta = DivisionTeamAssignmentFactory.createShortWithJobInfoAndFlags(
                    d,
                    withAssignments ? positionAssignments.getOrDefault(d.getEmployeeId(), Collections.emptyList()) : Collections.emptyList(),
                    withRotations
                            ? rotations.getOrDefault(d.getId(), Collections.emptyList()).stream().map(DivisionTeamAssignmentRotationFactory::createShort)
                            .collect(toList())
                            : Collections.emptyList(),
                    emplMap.get(d.getEmployeeId()),
                    withDtr);
            DivisionTeamShortDto divisionTeam = DivisionTeamFactory.createShort(dtMap.get(d.getDivisionTeamRoleId()));
            dta.setDivisionTeam(divisionTeam);
            DivisionTeamRoleShortDto teamRole = DivisionTeamRoleFactory.createShort(dtrMap.get(d.getDivisionTeamRoleId()));
            teamRole.setDivisionTeam(divisionTeam);
            dta.setDivisionTeamRole(teamRole);
            return dta;
        }).collect(toList());
    }

    @Override
    public List<DivisionTeamAssignmentShortDto> getAssignmentsCreateShortWithJobInfo(List<DivisionTeamAssignmentEntity> assignments, boolean withAssignments, boolean withRotations) {
        List<Long> assignmentIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();

        assignments.forEach(dta -> {
            assignmentIds.add(dta.getId());
            employeeIds.add(dta.getEmployee().getId());
        });

        final Map<Long, List<DivisionTeamAssignmentRotationEntity>> rotationsByAssignmentIds = divisionTeamAssignmentRotationDao.findActualByAssignmentIds(assignmentIds);
        final Map<Long, List<PositionAssignmentEntity>> positionByEmployeeIds = positionAssignmentDao.findActualByEmployeeIds(employeeIds, unitAccessService.getCurrentUnit());
        return assignments
                .stream()
                .map(dta ->
                             DivisionTeamAssignmentFactory.createShortWithJobInfo(
                                     dta,
                                     withAssignments ? positionByEmployeeIds.getOrDefault(dta.getEmployee().getId(), Collections.emptyList()) : Collections.emptyList(),
                                     withRotations ? rotationsByAssignmentIds.getOrDefault(dta.getId(), Collections.emptyList())
                                             .stream()
                                             .map(DivisionTeamAssignmentRotationFactory::createShort)
                                             .collect(toList()) : Collections.emptyList()
                             )
                )
                .collect(toList());
    }

    @Override
    public List<DivisionTeamAssignmentDto> getTeamDivisionTeamAssignments(Long divisionTeamId) {
        return getAssignmentsCreateWithJobInfo(divisionTeamAssignmentDao.findByDivisionTeamId(divisionTeamId), true, true, true, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionTeamAssignmentDto> getDivisionTeamAssignmentsFull(List<Long> ids, List<Long> employeeIds, String externalId, Long divisionTeamId, Boolean withClosed) {
        List<Long> processedEmployeeIds = employeeIds;
        if (CollectionUtils.isEmpty(processedEmployeeIds)) {
            processedEmployeeIds = List.of(authService.getUserEmployeeId());
        }

        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentDto> divisionTeamAssignments = getDivisionTeamAssignments(
                ids,
                ids == null ? processedEmployeeIds : null,
                divisionTeamId,
                BooleanUtils.isTrue(withClosed)
        );
        sw.stop();
        log.info("getDivisionTeamAssignmentsFull, getDivisionTeamAssignments: {}", sw.getTime());

        sw = StopWatch.createStarted();
        divisionTeamAssignments.sort(Comparator.comparing(item -> item.getDivisionTeamRole().getRole().getSystemRole().getId()));
        sw.stop();
        log.info("getDivisionTeamAssignmentsFull, divisionTeamAssignments.sort: {}", sw.getTime());
        return divisionTeamAssignments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionTeamAssignmentDto> getDivisionTeamAssignments(List<Long> ids, List<Long> employeeIds, Long divisionTeamId, Boolean withClosed) {
        if ((ids == null || ids.isEmpty()) && (employeeIds == null || employeeIds.isEmpty()) && divisionTeamId == null) {
            return Collections.emptyList();
        }
        DivisionTeamAssignmentKey cacheKey = new DivisionTeamAssignmentKey(ids, employeeIds, divisionTeamId, withClosed);
        List<DivisionTeamAssignmentEntity> dtaList = null;
        assignmentsCache.get(cacheKey);
        if (dtaList == null) {
            StopWatch sw = StopWatch.createStarted();
            dtaList = divisionTeamAssignmentDao.getDivisionTeamAssignments(ids, employeeIds, divisionTeamId, withClosed);
            sw.stop();
            log.info("getDivisionTeamAssignments, divisionTeamAssignmentDao.getDivisionTeamAssignments: {}", sw.getTime());
            assignmentsCache.put(cacheKey, dtaList);
        }
        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentDto> rez = getAssignmentsCreateWithJobInfo(dtaList, true, true, true, true);
        sw.stop();
        log.info("getDivisionTeamAssignments, getAssignmentsCreateWithJobInfo: {}", sw.getTime());
        return rez;
    }

    @Override
    public List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentCompactList(Collection<Long> ids, Collection<Long> employeeIds) {
        Collection<Long> processedEmployeeIds = employeeIds;
        if (CollectionUtils.isEmpty(processedEmployeeIds)) {
            // Если не передан список сотрудников, то выполняем метод для текущго пользователя
            processedEmployeeIds = List.of(authService.getUserEmployeeId());
        }

        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentCompactProjection> divisionTeamAssignments = getDivisionTeamAssignmentCompactListInner(
                ids,
                ids == null ? processedEmployeeIds : null
        );
        sw.stop();
        log.info("getDivisionTeamAssignmentCompactList, getDivisionTeamAssignmentCompactListInner: {}", sw.getTime());

        return divisionTeamAssignments;
    }

    @Override
    public List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentCompactListInner(Collection<Long> ids, Collection<Long> employeeIds) {
        if ((ids == null || ids.isEmpty()) && (employeeIds == null || employeeIds.isEmpty())) {
            return Collections.emptyList();
        }
        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentCompactProjection> result = divisionTeamAssignmentDao.getDivisionTeamAssignmentCompactList(ids, employeeIds);
        sw.stop();
        log.info("getDivisionTeamAssignmentCompactListInner, divisionTeamAssignmentDao.getDivisionTeamAssignmentCompactList: {}", sw.getTime());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public DivisionTeamAssignmentDto getFirstDivisionTeamAssignment() {
        Long employeeId = authService.getUserEmployeeId();
        final DivisionTeamAssignmentDto rez = employeeAssignments.get(employeeId);
        if (rez != null) {
            return rez;
        }
        final DivisionTeamAssignmentEntity entity = divisionTeamAssignmentDao.getDivisionTeamAssignmentFirstByRole(employeeId);
        if (entity == null) {
            employeeAssignments.put(employeeId, null);
            return null;
        }
        final DivisionTeamAssignmentDto dto = DivisionTeamAssignmentFactory.createWithJobInfo(entity, Collections.emptyList(), Collections.emptyList());
        employeeAssignments.put(employeeId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public CompactAssignmentDto getFirstDivTeamAss() {
        final String sessionEmployeeId = authService.getCurrentUser().getEmployeeExternalId();
        return divisionTeamAssignmentDao.findFirstAssignmentByExtEmployeeId(sessionEmployeeId);
    }

    @Override
    public List<DivisionTeamAssignmentShortDto> getDivisionTeamSubordinates(Long employeeId, String externalEmployeeId, Long divisionTeamId, boolean withChilds) {
        StopWatch sw = StopWatch.createStarted();
        Long id = resolveEmployeeId(employeeId, externalEmployeeId);
        sw.stop();
        log.info("getDivisionTeamSubordinates employeeService.getEmployeeId time {}", sw.getTime());
        try {
            AtomicInteger count = new AtomicInteger();
            //List<DivisionTeamAssignmentEntity> employeeAssignments = new ArrayList<>();
            List<Long> divTeams = new ArrayList<>();
            final List<DivTeamRoleDto> assignments;
            QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
            QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
            QRoleEntity qr = QRoleEntity.roleEntity;
            QBean<DivTeamRoleDto> proj = Projections.bean(DivTeamRoleDto.class, qdta.id.as("divTeamAssignmentId"), qdt.id.as("divTeamId"), qr.systemRoleId.as("systemRoleId"));
            if (divisionTeamId == null) {
                sw = StopWatch.createStarted();
                assignments = divisionTeamAssignmentDao.findQueryEmployeeIdAndSystemRole(id, HSR_ID).select(proj).fetch();
                divTeams = assignments.stream().map(a -> a.getDivTeamId()).collect(toList());
                sw.stop();
                log.info("getDivisionTeamSubordinates divisionTeamIds_1 time {}", sw.getTime());
            } else {
                sw = StopWatch.createStarted();
                DivTeamRoleDto asm = findAssignmentByEmployeeAndDivisionTeamIncParentDivTeam(id, divisionTeamId, count);
                assignments = Collections.singletonList(asm);
                divTeams.add(divisionTeamId);
                sw.stop();
                log.info("getDivisionTeamSubordinates divisionTeamIds_2 time {}", sw.getTime());
            }

            if (assignments.isEmpty()) {
                log.info("DivisionTeamAssignment for employee with id={} and division_teams with id={} is not found", id, divTeams);
                return Collections.emptyList();
            }

            if (assignments.stream().allMatch(i -> !HSR_ID.equals(i.getSystemRoleId())) && !withChilds) {
                log.debug("Employee {} is not HEAD", id);
                return Collections.emptyList();
            }

            Map<Long, DivisionTeamAssignmentShort> subordinates = new HashMap<>();
            Function<Collection<DivisionTeamAssignmentShort>, List<DivisionTeamAssignmentShortDto>> toDto = (l) -> {
                StopWatch sw1 = StopWatch.createStarted();
                List<DivisionTeamAssignmentShortDto> rez = getShortAssignmentsCreateWithJobInfo(l, true, false, true, false);
                sw1.stop();
                log.info("getDivisionTeamSubordinates getShortAssignmentsCreateWithJobInfo time {}", sw1.getTime());
                return rez;
            };
            if (count.get() > 0) {
                sw = StopWatch.createStarted();
                subordinates.putAll(
                        UtilClass.toMap(divisionTeamAssignmentDao.fetchShortByDivTeamAndSysRole(divTeams, HSR_ID))
                );
                sw.stop();
                log.info("getDivisionTeamSubordinates fetchByDivisionTeamIdAndSystemRoleId time {}", sw.getTime());

                // change value of divisionId because employeeId is not from division team with id=divisionTeamId
                // executing get() method of Optional object because it is safe in this case

                // if subordinates is empty then adding into subordinates all from division team with id=divisionTeamId with system_role_id != 1
                sw = StopWatch.createStarted();
                if (subordinates.isEmpty()) {
                    subordinates.putAll(getSubordinatesShortMap(divTeams));
                } else {
                    if (withChilds) {
                        extractChildSubordinatesSh(divTeams, subordinates);
                        extractSubordinatesFromTreeSh(divTeams, subordinates, new HashSet<>());
                    } else {
                        return toDto.apply(subordinates.values());
                    }
                }
                sw.stop();
                log.info("getDivisionTeamSubordinates getSubordinatesMap_1 time {}", sw.getTime());
            } else {
                sw = StopWatch.createStarted();
                subordinates.putAll(getSubordinatesShortMap(divTeams));
                sw.stop();
                log.info("getDivisionTeamSubordinates getSubordinatesMap_2 time {}", sw.getTime());
            }

            if (withChilds) {
                sw = StopWatch.createStarted();
                extractChildSubordinatesSh(divTeams, subordinates);
                extractSubordinatesFromTreeSh(divTeams, subordinates, new HashSet<>());
                sw.stop();
                log.info("getDivisionTeamSubordinates extract_1 time {}", sw.getTime());
            } else {
                sw = StopWatch.createStarted();
                extractSubordinatesRecursion(divTeams, subordinates);
                sw.stop();
                log.info("getDivisionTeamSubordinates extract_2 time {}", sw.getTime());
            }
            List<DivisionTeamAssignmentShortDto> rez = toDto.apply(subordinates.values());
            return rez;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An error occurred during getting subordinates for employee with id={}, message: {}", id, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<DivisionTeamAssignmentShortDto> getDivisionTeamSubordinatesTeam(Long divisionTeamId, Boolean withChilds) {
        Map<Long, DivisionTeamAssignmentShort> subordinates = getSubordinatesShortMap(Collections.singletonList(divisionTeamId));
        if (withChilds) {
            extractChildSubordinates(divisionTeamId, subordinates);
            extractSubordinatesFromTree(divisionTeamId, subordinates, new HashSet<>());
        } else {
            extractSubordinatesRecursion(divisionTeamId, subordinates);
        }
        return getShortAssignmentsCreateWithJobInfo(subordinates.values(), true, true, true, true);
    }

    @Override
    public List<DivisionTeamAssignmentDto> getTeamDivisionAssignmentsByLegalEntity(List<Long> legalEntityIds, boolean isHead, boolean withAssignments, boolean withRotation, boolean withEmployee, boolean withDtr) {
        List<DivisionTeamAssignmentEntity> divisionTeamAssignments = divisionTeamAssignmentDao.getDivisionTeamAssignmentsByLegalEntityIds(legalEntityIds, isHead, HSR_ID);

        if (!withAssignments && !withRotation) {
            return divisionTeamAssignments
                    .stream()
                    .map(i -> DivisionTeamAssignmentFactory.createSuperShort(i, withEmployee, withDtr))
                    .collect(toList());
        }

        return getAssignmentsCreateWithJobInfo(divisionTeamAssignments, withAssignments, withRotation, withEmployee, withDtr);
    }

    @Override
    public DivisionTeamAssignmentDto getTeamDivisionHead(Long employeeId, String externalId, Long divisionTeamId, Long headLevel) {
        String sessionExternalEmployeeId = authService.getCurrentUser().getEmployeeExternalId();
        EmployeeSearchResult headSearchResult;

        if (headLevel == null) {
            headLevel = 1L;
        }

        if (divisionTeamId != null && (employeeId == null && externalId == null)) {
            headSearchResult = employeeService.getDivisionTeamHeadByDivisionTeam(divisionTeamId, headLevel);

        } else if (divisionTeamId == null && (employeeId != null || externalId != null)) {
            Long id = resolveEmployeeId(employeeId, externalId);
            headSearchResult = employeeService.getDivisionTeamHeadByEmployee(id, headLevel);

        } else if (divisionTeamId != null) {
            Long id = resolveEmployeeId(employeeId, externalId);
            headSearchResult = employeeService.getDivisionTeamHeadDivisionTeamAndEmployee(divisionTeamId, id, headLevel);

        } else {
            Long employeeIdSession = employeeService.getEmployeeIdByExternalId(sessionExternalEmployeeId);
            headSearchResult = employeeService.getDivisionTeamHeadByEmployee(employeeIdSession, headLevel);
        }

        if (!headSearchResult.getSearchStatus()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Search result is empty, therefore we couldn't not return info. employee: %d, external_employee: %s, team: %d",
                                                                                  employeeId, externalId, divisionTeamId));
        }
        DivisionTeamAssignmentEntity assignment = getDivisionTeamAssignment(headSearchResult.getDivisionTeamAssignmentId());

        List<DivisionTeamAssignmentRotationShortDto> rotations = divisionTeamAssignmentRotationDao
                .findByAssignmentId(assignment.getId())
                .stream()
                .map(DivisionTeamAssignmentRotationFactory::createShort)
                .collect(toList());

        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .employeeId(assignment.getEmployeeId())
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        return DivisionTeamAssignmentFactory.createWithJobInfo(assignment, positionAssignments, rotations);
    }

    @Override
    public Long getEmployeeTaskRole(Long userId, Long employeeIdTaskOpener) {
        try {
            if (employeeIdTaskOpener == null) {
                log.debug("Current employee id (assocciated to KeyCloak session via Mapper) is null");
                return null;
            }
            List<DivisionTeamAssignmentDto> assignments = getDivisionTeamAssignmentsFull(Collections.singletonList(userId), null, null, null, null);
            if (assignments == null || assignments.size() != 1) {
                log.debug("Empty assignemnts for assignment id (task user id): {}", userId);
                return null;
            }
            Long employeeIdTaskHolder = assignments.get(0).getEmployee().getId();
            if (Objects.equals(employeeIdTaskHolder, employeeIdTaskOpener)) {
                log.debug("Employee {} is a task {} owner", employeeIdTaskOpener, assignments.get(0).getId());
                return GlobalDefs.TASK_OWNER_ROLE_CODE;
            }
            Long divisionTeamIdTaskHolder = assignments.get(0).getDivisionTeamRole().getDivisionTeam().getId();
            DivisionTeamAssignmentDto employeeHeadTaskHolder = getTeamDivisionHead(employeeIdTaskHolder, null, divisionTeamIdTaskHolder, null);
            if (employeeHeadTaskHolder != null && employeeHeadTaskHolder.getEmployee() != null && Objects.equals(employeeHeadTaskHolder.getEmployee().getId(), employeeIdTaskOpener)) {
                log.debug("Employee {} is a employee {} head, therefore it has access to task {}", employeeHeadTaskHolder.getEmployee().getId(), employeeIdTaskOpener, userId);
                return GlobalDefs.TASK_EMPLOYEE_HEAD_ROLE_CODE;
            }
            DivisionTeamAssignmentDto employeeHeadTaskOpener = getTeamDivisionHead(employeeIdTaskOpener, null, divisionTeamIdTaskHolder, null);
            if (employeeHeadTaskOpener != null && employeeHeadTaskOpener.getEmployee() != null && Objects.equals(employeeHeadTaskOpener.getEmployee().getId(), employeeIdTaskHolder)) {
                log.debug("Employee {} is a employee {} subordinate, therefore it has access to task {}", employeeIdTaskOpener, employeeHeadTaskOpener.getEmployee().getId(), userId);
                return GlobalDefs.TASK_EMPLOYEE_SUBORDINATE_ROLE_CODE;
            }
            List<DivisionTeamAssignmentDto> employeeAssignments = getDivisionTeamAssignmentsFull(null, null, null, null, null);
            DivisionTeamAssignmentDto divisionTeamAssignmentDto = employeeAssignments.get(0);
            if (userId.equals(divisionTeamAssignmentDto.getId())) {
                log.debug("Employee {} is a task {} assignment", employeeIdTaskOpener, divisionTeamAssignmentDto.getId());
                return GlobalDefs.TASK_ASSIGNMENT_ROLE_CODE;
            }
            Long employeeDivisionTeamId = divisionTeamAssignmentDto.getDivisionTeamRole().getDivisionTeam().getId();
            List<DivisionTeamAssignmentShortDto> employeeSubordinates = getDivisionTeamSubordinates(employeeIdTaskOpener, null, employeeDivisionTeamId, Boolean.FALSE);
            if (employeeSubordinates.stream().anyMatch(item -> Objects.equals(userId, item.getId()))) {
                log.debug("Employee {} is a employee {} subordinate head, therefore it has access to task {}", employeeIdTaskOpener, divisionTeamIdTaskHolder, userId);
                return GlobalDefs.TASK_EMPLOYEE_SUBORDINATE_HEAD_ROLE_CODE;
            }
            log.debug("Employee {} is NOT a task {} owner or is not a employee head (actual head is {})", employeeIdTaskHolder, userId, employeeHeadTaskHolder != null ? employeeHeadTaskHolder.getEmployee().getId() : null);
            return GlobalDefs.TASK_OTHER_ROLE_CODE;
        } catch (Exception e) {
            log.error("An error occured during employee {} task role fetch: {}", employeeIdTaskOpener, e.getMessage());
            return null;
        }
    }

    @Override
    public List<DivisionTeamAssignmentDto> getAssignmentByPositionCategory(Long positionCategoryId) {
        return getAssignmentsCreateWithJobInfo(divisionTeamAssignmentDao.getAssignmentByPositionCategory(positionCategoryId), true, true, true, true);
    }

    @Override
    public List<DivisionTeamAssignmentWithDivisionTeamFullDto> findDivisionTeamAssignmentWithDivisionTeamFull(Collection<Long> ids) {
        return divisionTeamAssignmentDao.findAllById(ids)
                .stream()
                .map(DivisionTeamAssignmentFactory::createWithDivisionTeamFull)
                .collect(toList());
    }

    @Override
    public Boolean checkSubstitute(Long divisionTeamId, Long sessionEmployee) {
        try {
            Long employeeId = getTeamDivisionHead(null, null, divisionTeamId, null).getEmployee().getId();
            Long count = employeeDeputyDao.findAllActualByEmployees(sessionEmployee, employeeId);
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            log.error("error in checkSubstitute", e);
            return false;
        }
        return false;
    }

    /**
     * Метод ищет назначение по ИД сотрудника и ИД команды и если ничего не нашлось с первого раза, то продолжаем поиск по ИД родительской команды,
     * поиск завершится, когда найдётся назначение или команда будет родительская и рекурсия остановится
     *
     * @param employeeId     - ИД сотрудника
     * @param divisionTeamId - ИД команды
     * @param count          - количество рекурсивных вызовов
     * @return возвращаем назначение
     */
    private DivisionTeamAssignmentEntity findAssignmentByEmployeeIdAndDivisionTeamIdsIncludingParentDivisionTeams(Long employeeId, Long divisionTeamId, AtomicInteger count) {
        DivisionTeamAssignmentEntity result = divisionTeamAssignmentDao.findByEmployeeIdAnDivisionTeamId(employeeId, divisionTeamId);
        if (result == null) {
            Long parentDivisionTeamId = divisionTeamDao.findParentIdById(divisionTeamId);
            if (parentDivisionTeamId != null) {
                count.incrementAndGet();
                return findAssignmentByEmployeeIdAndDivisionTeamIdsIncludingParentDivisionTeams(employeeId, parentDivisionTeamId, count);
            }
        }
        return result;
    }

    private DivTeamRoleDto findAssignmentByEmployeeAndDivisionTeamIncParentDivTeam(Long employeeId, Long divisionTeamId, AtomicInteger count) {
        QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
        QRoleEntity qr = QRoleEntity.roleEntity;
        QBean<DivTeamRoleDto> proj = Projections.bean(DivTeamRoleDto.class, qdta.id.as("divTeamAssignmentId"), qdt.id.as("divTeamId"), qr.systemRoleId.as("systemRoleId"));
        DivTeamRoleDto result = divisionTeamAssignmentDao.findQueryEmployeeIdAnDivisionTeamId(employeeId, divisionTeamId).select(proj).fetchFirst();
        if (result == null) {
            Long parentDivisionTeamId = divisionTeamDao.findParentIdById(divisionTeamId);
            if (parentDivisionTeamId != null) {
                count.incrementAndGet();
                return findAssignmentByEmployeeAndDivisionTeamIncParentDivTeam(employeeId, parentDivisionTeamId, count);
            }
        }
        return result;
    }

    private List<DivisionTeamAssignmentShort> getSubordinates(Long divisionTeamId) {
        return divisionTeamAssignmentDao.fetchShortByNotSystemRoleId(Collections.singletonList(divisionTeamId), HSR_ID);
    }

    private Map<Long, DivisionTeamAssignmentShort> getSubordinatesShortMap(List<Long> divisionTeamIds) {
        List<DivisionTeamAssignmentShort> list = divisionTeamAssignmentDao.fetchShortByNotSystemRoleId(divisionTeamIds, HSR_ID);
        return UtilClass.toMap(list);
    }

    private Map<Long, DivisionTeamAssignmentEntity> getSubordinatesMap(Long divisionTeamId) {
        return UtilClass.toMap(divisionTeamAssignmentDao.fetchByNotSystemRoleId(divisionTeamId, HSR_ID));
    }

    private void extractChildSubordinates(Long divTeamId, Map<Long, DivisionTeamAssignmentShort> subordinates) {
        List<DivisionAssignmentRoleDto> list = divisionTeamAssignmentDao.findShortByParentDivisionTeams(Collections.singletonList(divTeamId));
        List<DivisionTeamAssignmentShort> assignments = list.stream().map(d -> d.getAssignment()).collect(toList());
        subordinates.putAll(UtilClass.toMap(assignments));
        divisionTeamDao.findIdsByParentId(divTeamId).forEach(i -> extractChildSubordinates(i, subordinates));
    }

    private void extractChildSubordinatesSh(List<Long> divisionTeamIds, Map<Long, DivisionTeamAssignmentShort> subordinates) {
        if (divisionTeamIds == null || divisionTeamIds.isEmpty()) {
            return;
        }
        List<DivisionTeamAssignmentShort> list = divisionTeamAssignmentDao.findShortByParentDivisionTeams(divisionTeamIds)
                .stream().map(d -> d.getAssignment()).collect(toList());

        subordinates.putAll(UtilClass.toMap(list));
        List<Long> byParentIds = divisionTeamDao.findIdsByParentIds(divisionTeamIds);
        extractChildSubordinatesSh(byParentIds, subordinates);
    }

    private void extractSubordinatesFromTreeSh(List<Long> divTeams, Map<Long, DivisionTeamAssignmentShort> subordinates, Set<Long> processedDivisionTeamIds) {
        if (processedDivisionTeamIds.contains(divTeams)) {
            log.error("A loop in division team for division: {} during extract ", divTeams);
            throw new RuntimeException(String.format("A LOOP was detected in org structure for division team: %d", divTeams));
        }
        processedDivisionTeamIds.addAll(divTeams);
        nativeDao.findNearestChildIdsWithoutHead(divTeams, HSR_ID).forEach(childId -> {
            Long childNumber = divisionTeamDao.countNearestChildren(childId);
            subordinates.putAll(getSubordinatesShortMap(Collections.singletonList(childId)));
            if (childNumber == 0) {
                processedDivisionTeamIds.add(childId);
            } else {
                extractSubordinatesFromTreeSh(List.of(childId), subordinates, processedDivisionTeamIds);
            }
        });
    }

    private void extractSubordinatesFromTree(Long divisionTeamId, Map<Long, DivisionTeamAssignmentShort> subordinates, Set<Long> processedDivisionTeamIds) {
        if (processedDivisionTeamIds.contains(divisionTeamId)) {
            log.error("A loop in division team for division: {} during extract ", divisionTeamId);
            throw new RuntimeException(String.format("A LOOP was detected in org structure for division team: %d", divisionTeamId));
        }
        processedDivisionTeamIds.add(divisionTeamId);
        nativeDao.findNearestChildIdsWithoutHead(List.of(divisionTeamId), HSR_ID)
                .forEach(childId -> {
                    Long childrenNumber = divisionTeamDao.countNearestChildren(childId);
                    subordinates.putAll(getSubordinatesShortMap(Collections.singletonList(childId)));
                    if (childrenNumber == 0) {
                        processedDivisionTeamIds.add(childId);
                    } else {
                        extractSubordinatesFromTree(childId, subordinates, processedDivisionTeamIds);
                    }
                });
    }

    @Override
    public List<Long> getDivisionTeamSubordinatesByIds(List<Long> employeeIds) {
        StopWatch sw = StopWatch.createStarted();
        sw.stop();
        log.info("getDivisionTeamSubordinates employeeService.getEmployeeId time {}", sw.getTime());
        try {
            //List<DivisionTeamAssignmentEntity> employeeAssignments = new ArrayList<>();
            List<Long> divTeams = new ArrayList<>();
            final List<DivTeamRoleDto> assignments;
            QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
            QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
            QRoleEntity qr = QRoleEntity.roleEntity;
            QBean<DivTeamRoleDto> proj = Projections.bean(DivTeamRoleDto.class, qdta.id.as("divTeamAssignmentId"), qdt.id.as("divTeamId"), qr.systemRoleId.as("systemRoleId"));
            sw = StopWatch.createStarted();
            assignments = divisionTeamAssignmentDao.findQueryEmployeeIdsAndSystemRole(employeeIds, HSR_ID).select(proj).fetch();
            divTeams = assignments.stream().map(a -> a.getDivTeamId()).collect(toList());
            sw.stop();
            log.info("getDivisionTeamSubordinates divisionTeamIds_1 time {}", sw.getTime());


            if (assignments.isEmpty()) {
                log.info("DivisionTeamAssignment for employee with ids={} and division_teams with id={} is not found", employeeIds, divTeams);
                return Collections.emptyList();
            }


            Map<Long, DivisionTeamAssignmentShort> subordinates = new HashMap<>();
            sw = StopWatch.createStarted();
            subordinates.putAll(getSubordinatesShortMap(divTeams));
            sw.stop();
            log.info("getDivisionTeamSubordinates getSubordinatesMap_2 time {}", sw.getTime());



            sw = StopWatch.createStarted();
            extractSubordinatesRecursion(divTeams, subordinates);
            sw.stop();
            log.info("getDivisionTeamSubordinates extract_2 time {}", sw.getTime());

            List<Long> rez = subordinates.keySet().stream().collect(toList());
            return rez;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An error occurred during getting subordinates for employee with ids={}, message: {}", employeeIds, e.getMessage());
            return new ArrayList<>();
        }
    }

    //iGurkin todo: исправить без рекурсии org_division_links
    private void extractSubordinatesRecursion(List<Long> divisionTeamIds, Map<Long, DivisionTeamAssignmentShort> subordinates) {
        StopWatch sw = StopWatch.createStarted();
        List<DivisionAssignmentRoleDto> list = divisionTeamAssignmentDao.findShortByParentDivisionTeams(divisionTeamIds);
        sw.stop();
        log.info("extractSubordinatesRecursion findShortByParentDivisionTeams time {}", sw.getTime());

        sw = StopWatch.createStarted();
        Map<Long, DivisionTeamInfoForSubordinates> subordinatesMap = getDivisionTeamInfoForSubordinatesMap(list);
        sw.stop();
        log.info("extractSubordinatesRecursion getDivisionTeamInfoForSubordinatesMap_1 time {}", sw.getTime());

        subordinatesMap.forEach((dtId, dtInfo) -> {
            if (dtInfo.getHead() != null) {
                subordinates.put(dtInfo.getHead().getId(), dtInfo.getHead());
            } else {
                subordinates.putAll(UtilClass.toMap(dtInfo.noHeadList));
                extractSubordinatesRecursion(List.of(dtId), subordinates);
            }
        });
    }

    @Override
    @Transactional
    public List<Integer> getDivisionTeamSubordinatesEmployeeShort(Long employeeId, Boolean withChilds) {

        DivisionTeamAssignmentDto divisionTeamAssignment = getFirstDivisionTeamAssignment();
        Objects.requireNonNull(divisionTeamAssignment, "divisionTeamAssignment is null for employeeId=" + employeeId);
        if (!divisionTeamAssignment.getDivisionTeamRole().getRole().getSystemRole().getId().equals(HEAD_SYSTEM_ROLE_ID)) {
            throw new IllegalArgumentException(String.format("employee id %d is not division team head", employeeId));
        }
        List<DivisionTeamAssignmentShortDto> subordinates = getDivisionTeamSubordinatesTeam(divisionTeamAssignment.getDivisionTeam().getId(), withChilds);

        return subordinates.stream().map(x -> x.getEmployee().getId().intValue()).collect(toList());
    }
    @Transactional
    public List<EmployeeInfoShortDto> getDivisionTeamSubordinatesEmployeeFull(Long employeeId, Boolean withChilds) {

        DivisionTeamAssignmentDto divisionTeamAssignment = getFirstDivisionTeamAssignment();
        Objects.requireNonNull(divisionTeamAssignment, "divisionTeamAssignment is null for employeeId=" + employeeId);
        if (!divisionTeamAssignment.getDivisionTeamRole().getRole().getSystemRole().getId().equals(HEAD_SYSTEM_ROLE_ID)) {
            throw new IllegalArgumentException(String.format("employee id {} is not division team head", employeeId));
        }
        List<DivisionTeamAssignmentShortDto> subordinates = getDivisionTeamSubordinatesTeam(divisionTeamAssignment.getDivisionTeam().getId(), withChilds);

        return subordinates.stream().map(x -> new EmployeeInfoShortDto(x.getEmployee().getId().intValue(),
                                                                       UtilClass.getFIOFromEmployee(x.getEmployee()),
                                                                       x.getDivisionTeam().getDivisionId(),
                                                                       x.getEmployee().getFirstName(),
                                                                       x.getEmployee().getLastName(),
                                                                       x.getEmployee().getMiddleName(),
                                                                       x.getEmployee().getPhoto(),
                                                                       x.getEmployee().getPositionAssignments().get(0).getShortName()))
            .collect(toList());
    }

    private void extractSubordinatesRecursion(Long divisionTeamId, Map<Long, DivisionTeamAssignmentShort> subordinates) {
        List<DivisionAssignmentRoleDto> childAssignmentList = divisionTeamAssignmentDao.findByDivisionTeamParentId(divisionTeamId);
        Map<Long, DivisionTeamInfoForSubordinates> divisionTeamInfoForSubordinatesMap = getDivisionTeamInfoForSubordinatesMap(childAssignmentList);

        divisionTeamInfoForSubordinatesMap.forEach((dtId, dtInfo) -> {
            if (dtInfo.getHead() != null) {
                subordinates.put(dtInfo.getHead().getId(), dtInfo.getHead());
            } else {
                subordinates.putAll(UtilClass.toMap(dtInfo.noHeadList));
                extractSubordinatesRecursion(dtId, subordinates);
            }
        });
    }

    private Long resolveEmployeeId(Long internalEmployeeId, String externalEmployeeId) {
        if (internalEmployeeId != null) {
            return internalEmployeeId;
        }
        if (externalEmployeeId != null) {
            return employeeService.getEmployeeIdByExternalId(externalEmployeeId);
        }

        return authService.getUserEmployeeId();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DivisionTeamInfoForSubordinates {
        private Long divisionTeamId;
        private DivisionTeamAssignmentShort head;
        private List<DivisionTeamAssignmentShort> noHeadList;
    }

    private static Map<Long, DivisionTeamInfoForSubordinates> getDivisionTeamInfoForSubordinatesMap(List<DivisionAssignmentRoleDto> assignments) {
        Map<Long, DivisionTeamInfoForSubordinates> result = new HashMap<>();
        //DivisionTeamId
        //SystemRoleId
        assignments.forEach(dta -> {
            Long divTeamId = dta.getDivisionTeamId();
            Integer systemRoleId = dta.getSystemRoleId();
            final DivisionTeamAssignmentShort asmnt = dta.getAssignment();
            if (result.containsKey(divTeamId)) {
                DivisionTeamInfoForSubordinates subordinates = result.get(divTeamId);
                if (subordinates.getHead() == null && Objects.equals(systemRoleId, HSR_ID)) {
                    subordinates.setHead(asmnt);
                }
                if (!Objects.equals(systemRoleId, HSR_ID)) {
                    subordinates.getNoHeadList().add(asmnt);
                }
            } else {
                DivisionTeamInfoForSubordinates subordinates = new DivisionTeamInfoForSubordinates(
                        divTeamId,
                        Objects.equals(systemRoleId, HSR_ID) ? asmnt : null,
                        !Objects.equals(systemRoleId, HSR_ID) ? Stream.of(asmnt).collect(toList()) : new ArrayList<>()
                );
                result.put(divTeamId, subordinates);
            }
        });
        return result;
    }

    private static class DivisionTeamAssignmentKey {
        List<Long> ids;
        List<Long> employeeIds;
        Long divisionTeamId;
        Boolean withClosed;

        public DivisionTeamAssignmentKey(List<Long> ids, List<Long> employeeIds, Long divisionTeamId, Boolean withClosed) {
            this.ids = ids;
            this.employeeIds = employeeIds;
            this.divisionTeamId = divisionTeamId;
            this.withClosed = withClosed;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DivisionTeamAssignmentKey)) {
                return false;
            }
            DivisionTeamAssignmentKey other = (DivisionTeamAssignmentKey) o;
            boolean idsEquals = this.ids == other.ids || ((this.ids != null && other.ids != null)
                    && equalsIgnoreOrder(this.ids, other.ids));
            boolean employeeIdsEquals = this.employeeIds == other.employeeIds || ((this.employeeIds != null && other.employeeIds != null)
                    && equalsIgnoreOrder(this.employeeIds, other.employeeIds));
            return idsEquals && employeeIdsEquals && Objects.equals(other.divisionTeamId, this.divisionTeamId) &&
                    Objects.equals(this.withClosed, other.withClosed);
        }

        @Override
        public final int hashCode() {
            int result = 17;
            if (ids != null) {
                result = 31 * result + ids.hashCode();
            }
            if (employeeIds != null) {
                result = 31 * result + employeeIds.hashCode();
            }
            if (divisionTeamId != null) {
                result = 31 * result + divisionTeamId.hashCode();
            }
            if (withClosed != null) {
                result = 31 * result + withClosed.hashCode();
            }
            return result;
        }

        private boolean equalsIgnoreOrder(List<Long> l1, List<Long> l2) {
            return new HashSet<>(l1).equals(new HashSet<>(l2));
        }
    }
}
