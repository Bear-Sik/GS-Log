package com.gslog.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * ex
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청입니다.",
 *     "validation" : {
 *         "title" : "값을 입력해주세요"
 *     }
 * }
 */

// @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
// 위 어노테이션을 붙이면 빈 필드는 빼고 Json으로 직렬화 함
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation ;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }


}
