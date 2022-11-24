package com.gslog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gslog.domain.Post;
import com.gslog.repository.PostRepository;
import com.gslog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    /**
     * 주의사항
     * 1. 각각의 테스트 들이 서로 영향이 가지 않도록 짜는 것이 중요하다 !
     */

    // 테스트 케이스가 실행되기 전 동작하는 행위 지정
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // Mock MVC 는 요청을 보낼 때
        // Content-Type을 application/json 형태로 보낸다
        // 예전에는 application/x-www-form-urlencoded을 많이 사용했음

        /**
         * application/x-www-form-urlencoded로 보내는 예시
         *         mockMvc.perform(get("/posts")
         *                 .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
         *                 .param("title", "글 제목")
         *                 .param("content", "글 내용")
         *                 )
         *                 .andExpect(status().isOk())
         *                 .andExpect(content().string("Hello World"))
         *                 .andDo(print());
         */

        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();
        
        ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper는 실무에서 상당히 많이 사용하므로 많이 사용해보세요
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper는 실무에서 상당히 많이 사용하므로 많이 사용해보세요
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper는 실무에서 상당히 많이 사용하므로 많이 사용해보세요
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가

        // then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다", post.getTitle());
        Assertions.assertEquals("내용입니다", post.getContent());
    }
    
    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {

        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        // when
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {

        // given
        Post post1 = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post2);

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value(post1.getTitle().substring(0, Math.min(10, post1.getTitle().length()))))
                .andExpect(jsonPath("$[0].content").value(post1.getContent()))
                .andDo(print());
    }

}