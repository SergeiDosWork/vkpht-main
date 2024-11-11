package me.goodt.vkpht.common.api;

import java.util.Map;
import java.util.UUID;

public interface LoggerService {
    void createLog(UUID hash, String methodUrl, Map<String, Object> getParams, Object postParams);
}
