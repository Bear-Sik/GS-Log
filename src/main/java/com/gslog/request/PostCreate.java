package com.gslog.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")   // 값이 주입될 때 검증해준다. 컨트롤러에서 호출할 때 @Valid 어노테이션을 붙인다.
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
