package com.azeez.learning.service;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@RefreshScope
@Service
public class ConfigService {

    @Value("${foo}")
    private String foo;

    @Value("${bar}")
    private String bar;

    @Value("${baz}")
    private int baz;

    private Map<String, Object> props = new HashMap<>();


    @PostConstruct
    void init() {

        Field[] fields = this.getClass().getDeclaredFields();
        if (fields.length == 0) return;

        String fieldName;
        Object fieldValue;
        for (Field field : fields) {
// Since all the fields of this class are private, make them accessible
// to ensure that we do not get IllegalAccessException.
            field.setAccessible(true);
            fieldName = field.getName();
            if (ignoredField(field)) continue;
            try {
                fieldValue = field.get(this);
            } catch (IllegalAccessException e) {
                fieldValue = null;
            }
            props.put(fieldName, fieldValue);
        }

    }

    private boolean ignoredField(Field field) {
        return field.getName().equals("props") || Modifier.isStatic(field.getModifiers());
    }


    public String getPropertyAsString(String propName) {
        Object propertyValue = props.get(propName);
        return propertyValue == null ? null : propertyValue.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String propName) {
        return (T)props.get(propName);
    }

}


