package com.gslog.api.Exception;


import lombok.Getter;

@Getter
public class InvalidRequest extends GslogException {
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public int getStatusCode() {
        return 400;
    }
}
