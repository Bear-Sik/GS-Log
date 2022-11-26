package com.gslog.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.")   // 값이 주입될 때 검증해준다. 컨트롤러에서 호출할 때 @Valid 어노테이션을 붙인다.
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
