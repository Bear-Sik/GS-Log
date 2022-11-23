package com.gslog.controller;

import com.gslog.domain.Post;
import com.gslog.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // 글 제목
        // 글 내용
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

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"글제목\", \"content\":\"글내용\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": null, \"content\":\"글내용\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\":\"글내용\"}"))
                .andExpect(status().isOk())
                .andDo(print());    // Test에 대한 Summary를 출력하고싶을 때 추가

        // then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("글내용", post.getContent());
    }
}