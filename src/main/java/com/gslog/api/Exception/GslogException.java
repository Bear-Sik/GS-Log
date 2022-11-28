package com.gslog.api.Exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GslogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public GslogException(String message) {
        super(message);
    }

    public GslogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
