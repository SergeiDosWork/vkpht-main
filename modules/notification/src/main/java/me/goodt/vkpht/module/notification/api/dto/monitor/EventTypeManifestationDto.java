package me.goodt.vkpht.module.notification.api.dto.monitor;

public class EventTypeManifestationDto {

	private Long id;
	private Long code;
	private String name;

	public EventTypeManifestationDto() {
	}

	public EventTypeManifestationDto(Long id, Long code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
