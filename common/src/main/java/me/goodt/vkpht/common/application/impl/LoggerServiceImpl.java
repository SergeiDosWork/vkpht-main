package me.goodt.vkpht.common.application.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.common.api.dto.LogInfoAboutMethod;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Marker METHOD_LOG = MarkerFactory.getMarker("METHOD_LOG");

    private final AuthService authService;

    @PostConstruct
    private void init() {
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void createLog(UUID hash, String methodUrl, Map<String, Object> getParams, Object postParams) {
        log.info(METHOD_LOG, getStringLogObject(createLogObject(hash, methodUrl, getParams, postParams)));
    }

    private LogInfoAboutMethod createLogObject(UUID hash, String methodUrl, Map<String, Object> getParams, Object postData) {
        return new LogInfoAboutMethod(
                hash,
                new Timestamp(new Date().getTime()),
                methodUrl,
                authService.getCurrentUser().getEmployeeExternalId(),
                getParams,
                postData
        );
    }

    private String getStringLogObject(Object logObject) {
        try {
            return OBJECT_MAPPER.writeValueAsString(logObject);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }
}
