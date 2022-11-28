package com.gslog.controller;

import com.gslog.request.PostCreate;
import com.gslog.request.PostEdit;
import com.gslog.request.PostSearch;
import com.gslog.response.PostResponse;
import com.gslog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public void post(@RequestBody @Valid PostCreate request) throws Exception {

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

        /**
         * return case
         * 1. 저장한 데이터 Entity를 response로 응답
         *         return postService.write(request);
         * 2. 저장한 데이터의 primary_id를 response로 응답
         *      -> Client에서 응답받은 primary_id를 활용해서 글 조회 API를 다시 쏴서 데이터를 받아온다.
         *         Long postId = postService.write(request);
         *         return Map.of("postId", postId);
         * 3. 응답 필요 없음
         *      -> 클라이언트에서 모든 글 데이터 context를 잘 관리함.
         * Bad Case : 서버에서 반드시 이렇게 할겁니다 ! fix
         *      -> 서버에서 유연하게 대응하는 것이 좋아요.
         *      -> 한 번에 일괄적으로 잘 처리되는 케이스가 없음 -> 잘 관리하는 형태가 중요하다.
         */
        
        postService.write(request);
    }

    /**
     * 조회 API
     * /posts -> 글 전체 조회 (검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // title을 10글자만 출력해야한다. 라는 서비스 정책이 생기면
        // 응답 클래스를 생성하는 것이 좋다.
        // 엔티티에 서비스의 정책을 넣지마세요 절대 !!!
        return postService.get(id);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
