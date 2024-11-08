package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDynamicObject implements Serializable {

    @JsonIgnore
    private Map<String, Object> _embedded;

    public BaseDynamicObject addModel(String key, Object obj) {
        if (_embedded == null) {
            _embedded = new HashMap<>(4);
        }
        _embedded.put(key, obj);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getMap() {
        return _embedded;
    }
}
