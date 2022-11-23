package com.gslog.controller;

import com.gslog.request.PostCreate;
import com.gslog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    // SSR -> jsp, thymeleaf, mustache, freemarker
    //          서버에서 렌더링
    // SPA-> VUE -> VUE + NUXT || react -> REACT + NEXT
    //          자바 스크립트, API 통신으로 렌더링

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT

    private final PostService postService;

    // 글 등록
    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate request) throws Exception {

        /**
         * 데이터를 검증하는 이유
         *  1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
         *  2. client bug로 값이 누락될 수 있다.
         *  3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
         *  4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
         *  5. 서버 개발자의 편안함을 위해서
         */

        /**
         *  error 처리 1
         *  이런식의 에러처리는 잘못되었다.
         *  1. 빡세다
         *  2. 무언가 3번 이상 반복작업을 할 때 내가 뭔가 잘못하고 있는 건 아닐지 의심한다.
         *  3. 누락 가능성
         *  4. 생각보다 검증해야할 게 많다. (꼼꼼하지 않을 수 있다.)
         *  5. 뭔가 개발자 스럽지 않다.
         *     String title = params.getTitle();
         *     if (title == null || title.equals("")) {
         *         // error
         *         throw new Exception("타이틀 값이 없어요우");
         *     }
         *
         *     String content = params.getContent();
         *     if (content == null || content.equals("")) {
         *         // error
         *         throw new Exception("컨텐츠 값이 없어요우");
         *     }
         */

        /**
         * error 처리 2
         * 아래와 같이 에러처리를 하면 매 함수마다 에러처리 내용을 추가해야한다.
         * 1. 검증 부분에서 버그가 발생할 여지가 높다.
         * 2. 응답값에 HashMap으로 주고있는데, 응답 클래스를 만들어서 응답하는 것이 좋다
         * 3. 여러개의 에러처리가 힘들다.
         * 4. 세 번 이상의 반복적인 작업이 발생할 수 있다 ㅎㅎ
         *      -> 에러처리 뿐만 아니라 코드 && 개발에 관한 모든 부분
         * if (result.hasErrors()) {
         *     List<FieldError> fieldErrors = result.getFieldErrors();
         *     FieldError firstFieldError = fieldErrors.get(0);
         *     String fieldName = firstFieldError.getField(); // title
         *     String errorMessage = firstFieldError.getDefaultMessage(); // 에러 메시지
         *
         *     Map<String, String> error = new HashMap<>();
         *     error.put(fieldName, errorMessage);
         *
         *     return error;
         * }
         */

        // 요청에 대한 처리는 Service Layer를 만들고
        // Service Layer에서 처리하도록 만든다.

        postService.write(request);

        return Map.of();
    }
}
