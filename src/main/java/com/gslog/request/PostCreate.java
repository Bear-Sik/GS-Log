package com.gslog.request;

import lombok.Builder;
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

    // 클래스 위에 빌더 어노테이션을 선언할 수 있지만,
    // 멤버의 선언 형태또는 생성자의 형태에 따라 모순이 생길 수 있기 때문에 
    // 생성자 위에 빌더 어노테이션을 선언하는 것이 좋다
    // 빌더의 장점
    //  - 가독성이 좋다
    //  - 값 생성에 대한 유연함
    //  - 필요한 값만 받을 수 있다. -> (오버로딩 가능한 조건 찾아보기)
    //  - * 객체의 불변성 *
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}


