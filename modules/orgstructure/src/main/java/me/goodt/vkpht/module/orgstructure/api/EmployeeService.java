package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.request.FindEmployeeRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.goodt.drive.rtcore.dto.rostalent.ResponseNumberDto;
import com.goodt.drive.rtcore.dto.tasksetting2.EmployeesByStatusDto;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.PageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.ProcessEmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.EmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.EmployeeByStatusRequest;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.ProcessEmployeeRequest;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeFlatInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeSearchResult;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentInfo;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

public interface EmployeeService {

    EmployeeSearchResult getDivisionTeamHead(Long employeeId, Long divisionTeamId);

    PositionAssignmentInfo getPositionAssignmentInfo(Long employeeId);

    EmployeeSearchResult getDivisionTeamHeadByDivisionTeam(Long divisionTeamId, Long headLevel);

    EmployeeSearchResult getDivisionTeamHeadByEmployee(Long employeeId, Long headLevel);

    EmployeeSearchResult getDivisionTeamHeadDivisionTeamAndEmployee(Long divisionTeamId, Long employeeId, Long headLevel);

    List<EmployeeEntity> findByIds(Collection<Long> ids);

    EmployeeEntity findById(Long id);

    Long getEmployeeIdByExternalId(String externalId);

    Optional<EmployeeEntity> getEmployeeByExternalId(String externalId);

    List<EmployeeInfoDto> findEmployee(List<Long> employeeIds, List<Long> divisionIds, List<Long> functionIds, String searchingValue, List<Long> legalEntityIds, Boolean withPatronymic, Long jobTitleId, Pageable pageable);

    EmployeeInfoResponse findEmployees(FindEmployeeRequest request);

    List<EmployeeInfoDto> findEmployeeNew(FindEmployeeRequest request);

    List<EmployeeInfoDto> getEmployeeInfoList(Collection<EmployeeInfoDto> employees, boolean hasPositionAssignment);

    Optional<EmployeeEntity> getEmployee(Long id, String externalId);

    ResponseNumberDto checkNumbers(MultipartFile file) throws IOException, NotFoundException;

    ByteArrayOutputStream checkNumbersExcel(MultipartFile file) throws IOException, NotFoundException;

    List<EmployeeFlatInfoDto> getEmployeeFlatInfo(Long id, String externalId);

    PageResponse<EmployeesByStatusDto> getEmployeesByStatuses(EmployeeByStatusRequest request, boolean subordinatesOnly);

    List<EmployeeDto> getCycleSubordinates(Long employeeId, Long cycleId);

    FilterAwarePageResponse<ProcessEmployeeDto> findProcessEmployees(ProcessEmployeeRequest request);

    String getUnitCode(Long employeeId);

    Long findIdByExternalId(String externalId);

    EmployeeInfoDto getEmployeeInfo(Long employeeId);

    EmployeeInfoDto getEmployeeInfo(Long employeeId, String externalId);

    EmployeeExtendedInfoDto getEmployeeExtendedInfo(Long employeeId, String externalId);

    EmployeeInfoDto getEmployeeInfoByAssignment(Long divisionTeamAssignmentId);
}
