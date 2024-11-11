package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.common.domain.dao.rostalent.CompetenceCatalogDao;
import me.goodt.vkpht.common.domain.dao.rostalent.CompetenceDao;
import me.goodt.vkpht.common.domain.dao.rostalent.CompetenceProfilePositionDao;
import me.goodt.vkpht.common.domain.dao.rostalent.filter.CompetenceFilter;
import com.goodt.drive.rtcore.dictionary.rostalent.filter.ScaleLevelFilterRequest;
import com.goodt.drive.rtcore.dictionary.rostalent.service.ScaleLevelCrudService;
import com.goodt.drive.rtcore.dto.rostalent.Params;
import com.goodt.drive.rtcore.dto.rostalent.competence.*;
import com.goodt.drive.rtcore.dto.tasksetting.*;
import com.goodt.drive.rtcore.dto.tasksetting.IndicatorDto;
import com.goodt.drive.rtcore.dto.tasksetting.IndicatorLevelDto;
import com.goodt.drive.rtcore.factory.rostalent.CompetenceFactory;
import com.goodt.drive.rtcore.factory.rostalent.IndicatorFactory;
import com.goodt.drive.rtcore.factory.rostalent.IndicatorLevelFactory;
import com.goodt.drive.rtcore.factory.rostalent.ScaleLevelFactory;
import com.goodt.drive.rtcore.service.ICompetenceCatalogService;
import com.goodt.drive.rtcore.service.ICompetenceService;
import com.goodt.drive.rtcore.service.IEvaluationService;
import com.goodt.drive.rtcore.service.JsonMapper;
import com.goodt.drive.rtcore.service.tasksetting.task.OldTaskService;
import me.goodt.vkpht.common.api.exception.JsonException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.entity.rostalent.IModelContext;
import me.goodt.vkpht.common.domain.entity.rostalent.entities.*;
import me.goodt.vkpht.module.orgstructure.api.CompetenceService;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

import static java.util.Arrays.asList;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_3705;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_3707;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_681;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_682;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_687;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_824;
import static me.goodt.vkpht.common.application.util.GlobalDefs.STATUS_ID_825;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_213;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_215;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_217;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_218;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_219;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_TYPE_ID_170;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_TYPE_ID_171;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_TYPE_ID_172;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_TYPE_ID_173;

@Service
@Transactional
public class OrgCompetenceServiceImpl implements CompetenceService {

    /**
     * Компаратор сортировки данных компетенций по уровню
     */
    private static final Comparator<CompetenceDataDto> COMPETENCE_CATALOG_COMPARATOR = (data1, data2) -> {
        CompetenceShortDto competence1 = data1.getCompetence();
        CompetenceShortDto competence2 = data2.getCompetence();
        if (competence1 == null || competence2 == null) {
            return 0;
        }

        int rootDiff = Long.compare(competence1.getCatalogRootId(), competence2.getCatalogRootId());
        if (rootDiff != 0) {
            return rootDiff;
        }

        if (competence1.getCatalogParentId() == null && competence2.getCatalogParentId() == null) {
            return Long.compare(competence1.getId(), competence2.getId());
        }
        if (competence1.getCatalogParentId() != null && competence2.getCatalogParentId() != null) {
            if (Objects.equals(competence1.getCatalogParentId(), competence2.getCatalogParentId())) {
                return Long.compare(competence1.getId(), competence2.getId());
            }
            return Long.compare(competence1.getCatalogParentId(), competence2.getCatalogParentId());
        }

        return competence1.getCatalogParentId() != null ? 1 : -1;
    };

    /**
     * Компаратор сортировки рейтинга по ФИО сотрудников.
     */
    private static final Comparator<RatingDto> RATING_EMPLOYEE_COMPARATOR = (rating1, rating2) -> {
        EmployeeDto employee1 = rating1.getEmployee();
        EmployeeDto employee2 = rating2.getEmployee();
        if (employee1 == null || employee2 == null) {
            return 0;
        }

        String fullName1 = String.join(" ", employee1.getSurname(), employee1.getName(), employee1.getPatronymic());
        String fullName2 = String.join(" ", employee2.getSurname(), employee2.getName(), employee2.getPatronymic());
        return StringUtils.compareIgnoreCase(fullName1, fullName2);
    };

    @Autowired
    private OldTaskService taskService;
    @Autowired
    private DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    @Autowired
    private PositionAssignmentDao positionAssignmentDao;
    @Autowired
    private CompetenceDao competenceDao;
    @Autowired
    private ICompetenceService competenceService;
    @Autowired
    private IModelContext dbContext;
    @Autowired
    private CompetenceProfilePositionDao competenceProfilePositionDao;
    @Autowired
    private CompetenceCatalogDao competenceCatalogDao;
    @Autowired
    private ICompetenceCatalogService competenceCatalogService;
    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private IEvaluationService evaluationService;
    @Autowired
    private ScaleLevelCrudService scaleLevelService;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public List<CompetenceDataDto> getCompetencesData(Long employeeId, Long evaluationEventId)
        throws JsonException, NotFoundException {
        //        EvaluationEventDto evaluation = rostalentServiceClient.getEvaluationEvent(evaluationEventId, null);
        Map<TaskDto, List<TaskDto>> tasks172by170 = getTasks172by170(evaluationEventId, employeeId);

        Map<Long, List<Long>> competencesByEmployee = getCompetencesByEmployee(true, tasks172by170);
        Set<Long> competenceIds = competencesByEmployee.values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toSet());
        List<CompetenceDto> competenceList = getCompetencesByIds(competenceIds);

        Map<Long, List<RatingDto>> ratingsByIndicator = getRatingsByIndicator(
            employeeId,
            null,
            evaluationEventId,
            tasks172by170,
            competenceList,
            competencesByEmployee,
            false);

        List<CompetenceCatalogDto> allCatalog = competenceCatalogService.findAll();

        List<CompetenceDataDto> result = new ArrayList<>();
        for (CompetenceDto com : competenceList) {
            CompetenceDataDto competence = new CompetenceDataDto();
            competence.setCompetence(mapCompetenceShortDto(com, allCatalog));

            if (CollectionUtils.isNotEmpty(com.getIndicatorList())) {
                List<IndicatorDto> indicators = new ArrayList<>();
                for (IndicatorShortDto indicator : com.getIndicatorList()) {
                    indicators.add(convertToIndicatorDto(indicator, ratingsByIndicator));
                }
                competence.setIndicators(indicators);
            }
            result.add(competence);
        }

        // Сортируем и упорядочиваем результирующие данные по всей вложенности.
        result.sort(COMPETENCE_CATALOG_COMPARATOR);
        result.forEach(c -> c.getIndicators().forEach(i -> i.getRatings().sort(RATING_EMPLOYEE_COMPARATOR)));

        return result;
    }

    private IndicatorDto convertToIndicatorDto(IndicatorShortDto shortDto, Map<Long, List<RatingDto>> ratingsByIndicator) {
        IndicatorDto dto = new IndicatorDto();
        dto.setId(shortDto.getId());
        dto.setName(shortDto.getName());
        dto.setRatings(ratingsByIndicator.get(shortDto.getId()));

        if (CollectionUtils.isNotEmpty(shortDto.getIndicatorLevelList())) {
            List<IndicatorLevelDto> levels = shortDto.getIndicatorLevelList()
                .stream()
                .map(level -> new IndicatorLevelDto(level.getId(), level.getLevelPoint().intValue(), level.getName()))
                .sorted(Comparator.comparing(IndicatorLevelDto::getPoint))
                .collect(Collectors.toList());

            dto.setIndicatorLevel(levels);
        }

        return dto;
    }

    private CompetenceShortDto mapCompetenceShortDto(CompetenceDto com, List<CompetenceCatalogDto> catalogs) throws JsonException {
        CompetenceShortDto dto = new CompetenceShortDto();
        dto.setId(com.getId());
        dto.setName(com.getName());
        dto.setColor(getColor(com));

        CompetenceCatalogDto catalog = com.getCompetenceCatalog();
        if (catalog != null) {
            dto.setCatalogId(catalog.getId());
            dto.setCatalogParentId(catalog.getParentId());
            dto.setCatalogName(catalog.getName());

            dto.setCatalogRootId(findRoot(catalogs, catalog.getId()));
        }

        return dto;
    }

    private Long findRoot(List<CompetenceCatalogDto> allCatalog, Long id) {
        CompetenceCatalogDto catalog = allCatalog.stream()
            .filter(i -> id.equals(i.getId()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Не найден catalog с id = " + id));

        if (catalog.getParentId() != null) {
            Long root = findRoot(allCatalog, catalog.getParentId());
            return root != null ? root : catalog.getId();
        }

        return catalog.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogsByUserDto getCatalogsByUser(Long employeeId,
                                               Long selectedEmployeeId,
                                               Long evaluationEventId) throws JsonException {
        //        EvaluationEventDto evaluation = rostalentServiceClient.getEvaluationEvent(evaluationEventId, null);
        Map<TaskDto, List<TaskDto>> tasks172by170 = getTasks172by170(evaluationEventId, employeeId);
        Map<Long, List<Long>> competencesByEmployee = getCompetencesByEmployee(true, tasks172by170);
        List<Long> competencesIdsList = competencesByEmployee.get(selectedEmployeeId);
        if (CollectionUtils.isEmpty(competencesIdsList)) {
            throw new RuntimeException("No suitable competencies");
        }
        List<CompetenceDto> competenceList = getCompetences(Collections.singletonList(evaluationEventId));
        if (CollectionUtils.isEmpty(competenceList)) {
            competenceList = getCompetencesByIds(competencesIdsList);
        } else {
            competenceList.addAll(getCompetencesByIds(competencesIdsList));
        }

        Map<Long, List<RatingDto>> ratingsByIndicator = getRatingsByIndicator(
            employeeId,
            selectedEmployeeId,
            evaluationEventId,
            tasks172by170,
            competenceList,
            competencesByEmployee,
            true);

        Set<CompetenceDto> uniqueCompetence = new HashSet<>(competenceList);
        Set<CompetenceCatalogDto> catalogs = uniqueCompetence.stream()
            .map(CompetenceDto::getCompetenceCatalog)
            .collect(Collectors.toSet());

        Map<Long, List<CompetenceInfoDto>> competencesByCatalog = getCompetencesByCatalog(
            selectedEmployeeId, new ArrayList<>(uniqueCompetence), ratingsByIndicator);

        CatalogsByUserDto result = new CatalogsByUserDto();
        result.setIsReadonly(getIsReadonly(selectedEmployeeId, tasks172by170, evaluationEventId));

        if (CollectionUtils.isNotEmpty(catalogs)) {
            Map<Long, CompetenceCatalogDataDto> data = new HashMap<>();
            for (CompetenceCatalogDto c : catalogs) {
                List<CompetenceInfoDto> competences = competencesByCatalog.get(c.getId());
                if (!CollectionUtils.isNotEmpty(competences)) {
                    continue;
                }

                CompetenceCatalogEntity rootCatalog = findRootCatalog(c.getId());
                CompetenceCatalogDataDto catalogData = new CompetenceCatalogDataDto();
                catalogData.setId(rootCatalog.getId());
                catalogData.setName(rootCatalog.getName());
                catalogData.setCompetences(competences);

                if (data.containsKey(rootCatalog.getId())) {
                    data.get(rootCatalog.getId()).getCompetences().addAll(catalogData.getCompetences());
                } else {
                    data.put(rootCatalog.getId(), catalogData);
                }
            }

            List<CompetenceCatalogDataDto> res = new ArrayList<>();

            for (Map.Entry<Long, CompetenceCatalogDataDto> entry : data.entrySet()) {
                res.add(entry.getValue());
            }

            result.setData(res);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompetenceDto> getCompetencesByCompetenceProfile(Long competenceProfileId) {
        List<CompetenceEntity> competenceList = competenceDao.findAll(
            CompetenceFilter.builder()
                .competenceProfileId(competenceProfileId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build());
        List<Long> scaleTypeIds = new ArrayList<>();
        List<Long> competenceIds = competenceList
            .stream()
            .peek(c -> {
                if (c.getScaleType() != null) {
                    scaleTypeIds.add(c.getScaleType().getId());
                }
            })
            .map(CompetenceEntity::getId)
            .collect(Collectors.toList());

        List<IndicatorEntity> indicatorList = dbContext.getIndicatorDataSource().findActualByCompetenceIds(competenceIds);
        List<Long> indicatorIds = new ArrayList<>();
        Map<Long, List<IndicatorEntity>> competenceIdAndIndicators = new HashMap<>();
        indicatorList.forEach(ind -> {
            indicatorIds.add(ind.getId());
            Long competenceId = ind.getCompetence().getId();
            if (competenceIdAndIndicators.containsKey(competenceId)) {
                competenceIdAndIndicators.get(competenceId).add(ind);
            } else {
                competenceIdAndIndicators.put(competenceId, Stream.of(ind).collect(Collectors.toList()));
            }
        });

        List<IndicatorLevelEntity> indicatorLevelList = dbContext.getIndicatorLevelDataSource().findActualByIndicatorIds(indicatorIds);
        Map<Long, List<IndicatorLevelEntity>> indicatorIdAndIndicatorLevels = new HashMap<>();
        indicatorLevelList.forEach(indlev -> {
            Long indicatorId = indlev.getIndicator().getId();
            if (indicatorIdAndIndicatorLevels.containsKey(indicatorId)) {
                indicatorIdAndIndicatorLevels.get(indicatorId).add(indlev);
            } else {
                indicatorIdAndIndicatorLevels.put(indicatorId, Stream.of(indlev).collect(Collectors.toList()));
            }
        });

        var levelFilter = ScaleLevelFilterRequest.builder()
                .scaleTypeIds(scaleTypeIds)
                .build();
        List<com.goodt.drive.rtcore.dictionary.rostalent.dto.ScaleLevelDto> scaleLevelList =
                scaleLevelService.findAll(levelFilter, Pageable.unpaged()).getContent();
        Map<Long, List<com.goodt.drive.rtcore.dictionary.rostalent.dto.ScaleLevelDto>> scaleTypeIdAndScaleLevels =
                new HashMap<>();
        scaleLevelList.forEach(sclev -> {
            Long scaleTypeId = sclev.getScaleTypeId();
            if (scaleTypeIdAndScaleLevels.containsKey(scaleTypeId)) {
                scaleTypeIdAndScaleLevels.get(scaleTypeId).add(sclev);
            } else {
                scaleTypeIdAndScaleLevels.put(scaleTypeId, Stream.of(sclev).collect(Collectors.toList()));
            }
        });

        return competenceList
            .stream()
            .map(competence -> {
                List<IndicatorShortDto> indicatorShortDtoList = new ArrayList<>();
                if (competenceIdAndIndicators.containsKey(competence.getId())) {
                    indicatorShortDtoList = competenceIdAndIndicators.get(competence.getId())
                        .stream()
                        .map(ind -> {
                            List<IndicatorLevelShortDto> indicatorLevelShortDtoList = new ArrayList<>();
                            if (indicatorIdAndIndicatorLevels.containsKey(ind.getId())) {
                                indicatorLevelShortDtoList = indicatorIdAndIndicatorLevels.get(ind.getId())
                                    .stream()
                                    .map(IndicatorLevelFactory::createShort)
                                    .collect(Collectors.toList());
                            }
                            return IndicatorFactory.createShort(ind, indicatorLevelShortDtoList);
                        })
                        .collect(Collectors.toList());
                }
                CompetenceDto result = CompetenceFactory.create(competence, indicatorShortDtoList);
                ScaleTypeDto scaleType = result.getScaleType();
                if (scaleType != null) {
                    if (scaleTypeIdAndScaleLevels.containsKey(scaleType.getId())) {
                        List<ScaleLevelShortDto> scaleLevels = scaleTypeIdAndScaleLevels.get(scaleType.getId())
                            .stream()
                            .map(ScaleLevelFactory::createShortFromDictDto)
                            .collect(Collectors.toList());
                        scaleType.setScaleLevels(scaleLevels);
                    }
                }
                return result;
            })
            .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List<CompetenceDto> getCompetences(List<Long> evaluationEventIds) {
        List<CompetenceEvaluationEventDto> comEvents = competenceService.getAllCompetenceIdsByEvaluationEventIds(evaluationEventIds);
        List<CompetenceDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(comEvents)) {
            for (CompetenceEvaluationEventDto o : comEvents) {
                result.add(getCompetence(o.getCompetenceId()));
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompetenceDto> getCompetencesByIds(Collection<Long> competencesIds) throws NotFoundException {
        List<CompetenceDto> result = new ArrayList<>();
        for (Long id : competencesIds) {
            result.add(this.getCompetence(id));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CompetenceEntity getCompetenceEntity(Long competenceId) {
        return competenceDao.findOne(CompetenceFilter.builder()
                .ids(List.of(competenceId))
                .unitCode(unitAccessService.getCurrentUnit())
                .build())
            .orElseThrow(() -> new NotFoundException(String.format("Competence with id=%d is not found", competenceId)));
    }

    @Override
    @Transactional(readOnly = true)
    public CompetenceDto getCompetence(Long id) throws NotFoundException {
        CompetenceEntity entity = this.getCompetenceEntity(id);

        List<IndicatorEntity> indicatorList = dbContext.getIndicatorDataSource().findActualByCompetenceId(entity.getId());
        List<Long> indicatorIds = indicatorList.stream().map(IndicatorEntity::getId).collect(Collectors.toList());

        List<IndicatorLevelEntity> indicatorLevelList = dbContext.getIndicatorLevelDataSource().findActualByIndicatorIds(indicatorIds);
        Map<Long, List<IndicatorLevelEntity>> indicatorIdAndIndicatorLevels = new HashMap<>();
        indicatorLevelList.forEach(indlev -> {
            Long indicatorId = indlev.getIndicator().getId();
            if (indicatorIdAndIndicatorLevels.containsKey(indicatorId)) {
                indicatorIdAndIndicatorLevels.get(indicatorId).add(indlev);
            } else {
                indicatorIdAndIndicatorLevels.put(indicatorId, Stream.of(indlev).collect(Collectors.toList()));
            }
        });

        List<IndicatorShortDto> indicatorShortDtoList = indicatorList
            .stream()
            .map(ind -> {
                List<IndicatorLevelShortDto> indicatorLevelShortDtoList = new ArrayList<>();
                if (indicatorIdAndIndicatorLevels.containsKey(ind.getId())) {
                    indicatorLevelShortDtoList = indicatorIdAndIndicatorLevels.get(ind.getId())
                        .stream()
                        .map(IndicatorLevelFactory::createShort)
                        .collect(Collectors.toList());
                }
                return IndicatorFactory.createShort(ind, indicatorLevelShortDtoList);
            })
            .collect(Collectors.toList());
        CompetenceDto result = CompetenceFactory.create(entity, indicatorShortDtoList);
        ScaleTypeDto scaleType = result.getScaleType();
        if (scaleType != null) {
            var levelFilter = ScaleLevelFilterRequest.builder()
                    .scaleTypeIds(Collections.singletonList(scaleType.getId()))
                    .build();
            List<ScaleLevelShortDto> scaleLevels = scaleLevelService.findAll(levelFilter, Pageable.unpaged())
                    .getContent()
                    .stream()
                    .map(ScaleLevelFactory::createShortFromDictDto)
                    .collect(Collectors.toList());
            scaleType.setScaleLevels(scaleLevels);
        }
        return result;

    }

    @Override
    @Transactional(readOnly = true)
    public List<CompetenceProfilePositionEntity> getAllCompetenceProfilePosition(Long positionId) {
        return competenceProfilePositionDao.findActualByPositionId(positionId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompetenceProfilePositionEntity getCompetenceProfilePosition(Long id) throws NotFoundException {
        return competenceProfilePositionDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("competence_profile_position with id=%d is not found", id)));
    }

    private Map<Long, List<IndicatorInfoDto>> getIndicatorsByCompetence(
        Long selectedEmployeeId,
        List<CompetenceDto> competenceList,
        Map<Long, List<RatingDto>> ratingsByIndicator) {
        Map<Long, List<IndicatorInfoDto>> result = new HashMap<>();
        for (CompetenceDto com : competenceList) {
            if (CollectionUtils.isEmpty(com.getIndicatorList())) {
                continue;
            }

            List<IndicatorInfoDto> indicators = new ArrayList<>();
            for (IndicatorShortDto indicator : com.getIndicatorList()) {
                List<RatingDto> ratings = ratingsByIndicator.get(indicator.getId());
                if (CollectionUtils.isEmpty(ratings)) {
                    continue;
                }
                ratings.stream()
                    .filter(r -> selectedEmployeeId.equals(r.getEmployee().getId()))
                    .findFirst()
                    .map(rating -> mapIndicatorInfoDto(indicator, rating))
                    .ifPresent(indicators::add);
            }

            if (!indicators.isEmpty()) {
                result.put(com.getId(), indicators);
            }
        }
        return result;
    }

    private IndicatorInfoDto mapIndicatorInfoDto(IndicatorShortDto indicator, RatingDto rating) {
        IndicatorInfoDto dto = new IndicatorInfoDto();
        dto.setId(rating.getId());
        dto.setName(indicator.getName());
        dto.setComment(rating.getComment());
        dto.setSelectedRating(rating.getCurrentRating());

        if (CollectionUtils.isNotEmpty(indicator.getIndicatorLevelList())) {
            List<IndicatorLevelDto> levels = indicator.getIndicatorLevelList().stream()
                .map(l -> new IndicatorLevelDto(l.getId(), l.getLevelPoint().intValue(), l.getName()))
                .sorted(Comparator.comparingInt(IndicatorLevelDto::getPoint))
                .collect(Collectors.toList());

            dto.setLevels(levels);
        }

        return dto;
    }


    private Map<Long, List<CompetenceInfoDto>> getCompetencesByCatalog(Long selectedEmployeeId,
                                                                       List<CompetenceDto> competenceList,
                                                                       Map<Long, List<RatingDto>> ratingsByIndicator) throws JsonException {
        Map<Long, List<IndicatorInfoDto>> indicatorsByCompetence = getIndicatorsByCompetence(selectedEmployeeId,
                                                                                             competenceList, ratingsByIndicator);
        Map<Long, List<CompetenceInfoDto>> result = new HashMap<>();
        for (CompetenceDto com : competenceList) {
            List<IndicatorInfoDto> indicators = indicatorsByCompetence.get(com.getId());
            if (CollectionUtils.isNotEmpty(indicators)) {
                CompetenceInfoDto info = new CompetenceInfoDto(
                    com.getId(), com.getName(), getColor(com), indicators);

                List<CompetenceInfoDto> infos = result.computeIfAbsent(
                    com.getCompetenceCatalog().getId(), key -> new ArrayList<>());
                infos.add(info);
            }
        }
        return result;
    }

    private Map<Long, List<RatingDto>> getRatingsByIndicator(Long employeeId,
                                                             Long selectedEmployeeId,
                                                             Long evaluationEventId,
                                                             Map<TaskDto, List<TaskDto>> tasks172by170,
                                                             List<CompetenceDto> competenceList,
                                                             Map<Long, List<Long>> competencesByEmployee,
                                                             boolean includeReadonly) {
        Map<Long, Long> indicatorsByLevel = getIndicatorsByLevel(competenceList);
        Map<Long, List<RatingDto>> result = new HashMap<>();

        Map<Long, Map<Long, RatingDto>> ratingsByIndicator = fillDefaultIndicators(
            selectedEmployeeId,
            evaluationEventId,
            competenceList,
            tasks172by170,
            competencesByEmployee,
            new HashSet<>(indicatorsByLevel.values())
        );
        for (Map.Entry<TaskDto, List<TaskDto>> t : tasks172by170.entrySet()) {
            List<TaskDto> taskList = t.getValue();
            if (CollectionUtils.isNotEmpty(taskList)) {
                List<DivisionTeamAssignmentEntity> dtas = divisionTeamAssignmentDao.findActualByIds(
                    asList(t.getKey().getUserId()));
                EmployeeEntity emp = dtas.get(0).getEmployee();
                if (selectedEmployeeId != null && !selectedEmployeeId.equals(emp.getId())) {
                    continue;
                }
                if (!includeReadonly && STATUS_ID_825.equals(taskList.get(0).getStatus().getId())) {
                    ratingsByIndicator.forEach((k, v) -> {
                        v.remove(emp.getId());
                    });
                    continue;
                }
                List<TaskDto> tasks173 = findTasks(taskList, asList(TASK_TYPE_ID_173));
                if (CollectionUtils.isNotEmpty(tasks173)) {
                    for (TaskDto task : tasks173) {
                        Long currentRating = task.getFields().stream()
                            .filter(e -> TASK_FIELD_TYPE_ID_218.equals(e.getType().getId()))
                            .map(e -> Long.valueOf(e.getValue()))
                            .findFirst().orElse(null);
                        String comment = task.getFields().stream()
                            .filter(e -> TASK_FIELD_TYPE_ID_219.equals(e.getType().getId()))
                            .map(e -> e.getValue())
                            .findFirst().orElse(null);
                        if (currentRating != null) {
                            Long indicatorId = indicatorsByLevel.get(currentRating);
                            if (ratingsByIndicator.containsKey(indicatorId)) {
                                RatingDto rating = ratingsByIndicator.get(indicatorId).get(emp.getId());
                                if (rating != null) {
                                    rating.setId(task.getId());
                                    rating.setCurrentRating(currentRating);
                                    rating.setComment(comment);
                                }
                            }
                        }
                    }
                }
            }
        }
        ratingsByIndicator.forEach((k, v) -> {
            result.put(k, new ArrayList<>(v.values()));
        });
        return result;
    }

    private Map<Long, List<Long>> getCompetencesByEmployee(boolean includeReadonly,
                                                           Map<TaskDto, List<TaskDto>> tasks172by170) {
        List<Long> tasks170ids = tasks172by170.keySet().stream()
            .map(TaskDto::getId)
            .collect(Collectors.toList());

        TaskFindRequest taskSearchCriteria = new TaskFindRequest();
        taskSearchCriteria.setTaskType(List.of(TASK_TYPE_ID_171));
        taskSearchCriteria.setParentIds(tasks170ids);
        List<TaskDto> tasks171 = taskService.findTask(taskSearchCriteria);

        Map<Long, List<Long>> result = new HashMap<>();
        for (Map.Entry<TaskDto, List<TaskDto>> t : tasks172by170.entrySet()) {
            TaskDto task170 = t.getKey();
            List<TaskDto> taskList = t.getValue();
            if (CollectionUtils.isEmpty(taskList)) {
                continue;
            }

            List<DivisionTeamAssignmentEntity> dtas = divisionTeamAssignmentDao.findActualByIds(
                List.of(task170.getUserId()));
            EmployeeEntity emp = dtas.get(0).getEmployee();
            if (!includeReadonly && STATUS_ID_825.equals(taskList.get(0).getStatus().getId())) {
                continue;
            }

            List<TaskDto> t171 = tasks171.stream()
                .filter(o -> o.getParentId().equals(task170.getId()))
                .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(t171)) {
                continue;
            }

            List<Long> values = result.computeIfAbsent(emp.getId(), key -> new ArrayList<>());
            if (!result.containsKey(emp.getId())) {
                result.put(emp.getId(), new ArrayList<>());
            }
            for (TaskDto dto : t171) {
                dto.getFields().stream()
                    .filter(f -> TASK_FIELD_TYPE_ID_215.equals(f.getType().getId()))
                    .map(TaskFieldDto::getValue)
                    .findFirst()
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(value -> values.add(Long.valueOf(value)));
            }
        }
        return result;
    }

    private Map<Long, List<Long>> getIndicatorsByEmployee(List<CompetenceDto> competenceList,
                                                          Map<Long, List<Long>> competencesByEmployee) {
        if (!CollectionUtils.isNotEmpty(competenceList)) {
            return Collections.emptyMap();
        }

        Map<Long, List<Long>> result = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : competencesByEmployee.entrySet()) {
            List<Long> employeeComps = entry.getValue();
            List<Long> indicators = result.computeIfAbsent(entry.getKey(), key -> new ArrayList<>());

            for (CompetenceDto comp : competenceList) {
                if (CollectionUtils.isNotEmpty(comp.getIndicatorList()) &&
                    employeeComps.contains(comp.getId())) {
                    comp.getIndicatorList().forEach(indicator -> indicators.add(indicator.getId()));
                }
            }
        }
        return result;
    }

    private Map<Long, Map<Long, RatingDto>> fillDefaultIndicators(Long selectedEmployeeId,
                                                                  Long evaluationEventId,
                                                                  List<CompetenceDto> competenceList,
                                                                  Map<TaskDto, List<TaskDto>> tasks172by170,
                                                                  Map<Long, List<Long>> competencesByEmployee,
                                                                  Collection<Long> indicators) {
        Map<Long, Map<Long, RatingDto>> result = new HashMap<>();
        Map<Long, List<Long>> indicatorsByEmployee = getIndicatorsByEmployee(competenceList, competencesByEmployee);

        boolean isReadonly = true;

        EvaluationEventEntity evaluationEvent = evaluationService.getEvaluationEvent(evaluationEventId);
        if (Objects.isNull(evaluationEvent.getDateTo())) {
            if (Objects.nonNull(evaluationEvent.getEndDate())) {
                if (evaluationEvent.getEndDate().after(new Date())) {

                    List<Long> statusIds = tasks172by170.entrySet()
                        .stream()
                        .map(task170 -> task170.getValue())
                        .flatMap(t -> t.stream())
                        .map(task172 -> task172.getStatus().getId())
                        .collect(Collectors.toList());

                    List<Long> acceptableStatusIds = List.of(STATUS_ID_687, STATUS_ID_825, STATUS_ID_824);

                    isReadonly = !statusIds.stream().anyMatch(statusId -> !acceptableStatusIds.contains(statusId));
                }
            }
        }

        for (Map.Entry<TaskDto, List<TaskDto>> t : tasks172by170.entrySet()) {
            TaskDto task170 = t.getKey();
            List<TaskDto> taskList = t.getValue();
            if (CollectionUtils.isEmpty(taskList)) {
                continue;
            }

            List<DivisionTeamAssignmentEntity> dtas = divisionTeamAssignmentDao.findActualByIds(
                List.of(task170.getUserId()));
            EmployeeEntity emp = dtas.get(0).getEmployee();
            if (selectedEmployeeId != null && !selectedEmployeeId.equals(emp.getId())) {
                continue;
            }

            for (Long indicatorId : indicators) {
                List<Long> employeeIndicators = indicatorsByEmployee.get(emp.getId());
                if (CollectionUtils.isEmpty(employeeIndicators) || !employeeIndicators.contains(indicatorId)) {
                    continue;
                }

                RatingDto rating = new RatingDto();
                rating.setIsReadonly(isReadonly);

                rating.setEmployee(new EmployeeDto(
                    emp.getId(),
                    emp.getPerson().getName(),
                    emp.getPerson().getSurname(),
                    emp.getPerson().getPatronymic(),
                    emp.getPerson().getPhoto(),
                    getPosition(emp.getId())
                ));

                Map<Long, RatingDto> ratings = result.computeIfAbsent(indicatorId, key -> new HashMap<>());
                ratings.put(emp.getId(), rating);
            }
        }
        return result;
    }

    private Map<TaskDto, List<TaskDto>> getTasks172by170(Long evaluationEventId, Long employeeId) {
        Set<Long> excludeStatus = new HashSet<>(asList(STATUS_ID_681,STATUS_ID_682,STATUS_ID_3705, STATUS_ID_3707));
        TaskFindRequest taskSearchCriteria = new TaskFindRequest();
        taskSearchCriteria.setTaskType(asList(TASK_TYPE_ID_170));
        taskSearchCriteria.setTaskTypeFieldId(TASK_FIELD_TYPE_ID_213);
        taskSearchCriteria.setTaskFieldValue(evaluationEventId.toString());
        List<TaskDto> tasks170 = taskService.findTask(taskSearchCriteria)
            .stream()
            .filter(x-> !excludeStatus.contains(x.getStatus().getId())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(tasks170)) {
            return Collections.emptyMap();
        }

        TaskFindRequest sc = new TaskFindRequest();
        sc.setTaskType(asList(TASK_TYPE_ID_172));
        sc.setTaskTypeFieldId(TASK_FIELD_TYPE_ID_217);
        sc.setParentIds(tasks170.stream().map(t -> t.getId()).collect(Collectors.toList()));
        sc.setTaskFieldValue(employeeId.toString());
        List<TaskDto> tasks172 = taskService.findTask(sc);

        if (CollectionUtils.isEmpty(tasks172)) {
            return Collections.emptyMap();
        }

        Map<TaskDto, List<TaskDto>> result = new HashMap<>();
        tasks170.forEach(t -> {
            List<TaskDto> tasks = tasks172.stream()
                .filter(t172 -> t.getId().equals(t172.getParentId()))
                .collect(Collectors.toList());
            result.put(t, tasks);
        });

        return result;
    }

    private Boolean getIsReadonly(Long selectedEmployeeId,
                                  Map<TaskDto, List<TaskDto>> tasks172by170,
                                  Long evaluationEventId) {
        EvaluationEventEntity entity = getEvaluationEvent(evaluationEventId);
        if (entity.getEndDate().before(new Date())) {
            return true;
        }
        for (Map.Entry<TaskDto, List<TaskDto>> t : tasks172by170.entrySet()) {
            List<TaskDto> taskList = t.getValue();

            if (CollectionUtils.isNotEmpty(taskList)) {
                List<DivisionTeamAssignmentEntity> dtas = divisionTeamAssignmentDao.findActualByIds(
                    asList(t.getKey().getUserId()));
                if (selectedEmployeeId != null && selectedEmployeeId.equals(dtas.get(0).getEmployee().getId())) {
                    return STATUS_ID_825.equals(taskList.get(0).getStatus().getId());
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public EvaluationEventEntity getEvaluationEvent(Long id) throws NotFoundException {
        return dbContext.getEvaluationEventDataSource().findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("EvaluationEvent with id=%d is not found", id)));
    }

    private String getPosition(Long employeeId) {
        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .employeeId(employeeId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        if (CollectionUtils.isNotEmpty(positionAssignments)) {
            return positionAssignments.get(0).getPosition().getFullName();
        }
        return null;
    }

    private Map<Long, Long> getIndicatorsByLevel(List<CompetenceDto> competenceList) {
        if (CollectionUtils.isEmpty(competenceList)) {
            return Collections.emptyMap();
        }

        Map<Long, Long> result = new HashMap<>();

        for (CompetenceDto com : competenceList) {
            if (CollectionUtils.isEmpty(com.getIndicatorList())) {
                continue;
            }

            for (IndicatorShortDto indicator : com.getIndicatorList()) {
                if (CollectionUtils.isEmpty(indicator.getIndicatorLevelList())) {
                    continue;
                }
                for (IndicatorLevelShortDto level : indicator.getIndicatorLevelList()) {
                    result.put(level.getId(), indicator.getId());
                }
            }
        }
        return result;
    }

    private List<TaskDto> findTasks(List<TaskDto> parents, List<Long> types) {
        List<Long> parentIds = parents.stream().map(e -> e.getId()).collect(Collectors.toList());
        TaskFindRequest criteria = new TaskFindRequest();
        criteria.setTaskType(types);
        criteria.setParentIds(parentIds);
        return taskService.findTask(criteria);
    }

    private String getColor(CompetenceDto com) throws JsonException {
        String jsonParams = com.getCompetenceCatalog().getParams();
        if (jsonParams == null) {
            return null;
        }

        Params params = jsonMapper.deserialize(jsonParams, Params.class);
        return params.getColor();
    }

    private CompetenceCatalogEntity findRootCatalog(Long id) {
        CompetenceCatalogEntity entity = competenceCatalogDao.findById(id).orElseThrow();
        while (entity.getParent() != null) {
            entity = competenceCatalogDao.findById(entity.getParent().getId()).orElseThrow();
        }
        return entity;
    }
}
