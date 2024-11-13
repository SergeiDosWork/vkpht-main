package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class TaskEstimationMarkDto {

    public static final String MARK_AUTO = "mark_auto";
    public static final String TASK_ID = "task_id";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_CODE = "type_code";
    public static final String TYPE_NAME = "type_name";
    private static final String REPRESENTATION_AUTO_TAG = "representation_auto";
    private Long id;
    private Long taskId;
    private Double mark;
    private Double markAuto;
    private String representation;
    private String representationAuto;
    private Long typeId;
    private Long typeCode;
    private String typeName;
    private Long authorEmployeeId;

    public TaskEstimationMarkDto() {

    }

    public TaskEstimationMarkDto(Long id, Long taskId, Double mark, Double markAuto, Long typeId, Long typeCode, String typeName, Long authorEmployeeId) {
        this.id = id;
        this.taskId = taskId;
        this.mark = mark;
        this.markAuto = markAuto;
        this.typeId = typeId;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.authorEmployeeId = authorEmployeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    @JsonSetter(TaskEstimationMarkDto.TASK_ID)
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public Double getMarkAuto() {
        return markAuto;
    }

    @JsonSetter(TaskEstimationMarkDto.MARK_AUTO)
    public void setMarkAuto(Double markAuto) {
        this.markAuto = markAuto;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getRepresentationAuto() {
        return representationAuto;
    }

    @JsonSetter(TaskEstimationMarkDto.REPRESENTATION_AUTO_TAG)
    public void setRepresentationAuto(String representationAuto) {
        this.representationAuto = representationAuto;
    }

    public Long getTypeId() {
        return typeId;
    }

    @JsonSetter(TaskEstimationMarkDto.TYPE_ID)
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getTypeCode() {
        return typeCode;
    }

    @JsonSetter(TaskEstimationMarkDto.TYPE_CODE)
    public void setTypeCode(Long typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    @JsonSetter(TaskEstimationMarkDto.TYPE_NAME)
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getAuthorEmployeeId() {
        return authorEmployeeId;
    }

    @JsonSetter(DtoTagConstants.AUTHOR_EMPLOYEE_ID_TAG)
    public void setAuthorEmployeeId(Long authorEmployeeId) {
        this.authorEmployeeId = authorEmployeeId;
    }
}
