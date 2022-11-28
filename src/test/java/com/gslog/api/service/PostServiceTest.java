package com.gslog.api.service;

import com.gslog.api.domain.Post;
import com.gslog.api.repository.PostRepository;
import com.gslog.api.request.PostCreate;
import com.gslog.api.request.PostEdit;
import com.gslog.api.request.PostSearch;
import com.gslog.api.response.PostResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // when
        postService.write(postCreate);

        // then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다", post.getTitle());
        Assertions.assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);


        // when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        Assertions.assertEquals("foo", response.getTitle());
        Assertions.assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {

        // given
        List<Post> requestPosts = IntStream.range(1, 21)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목 - " + i)
                            .content("하하하 - " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("제목 - 20", posts.get(0).getTitle());
        assertEquals("제목 - 16", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {

        // given

        Post post = Post.builder()
                .title("민광식입니다.")
                .content("테스트입니다 ^^")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("민광식아닙니다.")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("민광식아닙니다.", changedPost.getTitle());
        Assertions.assertEquals("테스트입니다 ^^", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {

        // given
        Post post = Post.builder()
                .title("민광식입니다.")
                .content("테스트입니다 ^^")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("아닙니다.")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("민광식입니다.", changedPost.getTitle());
        Assertions.assertEquals("아닙니다.", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() {

        // given
        Post post = Post.builder()
                .title("민광식입니다.")
                .content("테스트입니다 ^^")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("아닙니다.")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("민광식입니다.", changedPost.getTitle());
        Assertions.assertEquals("아닙니다.", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() {

        // given
        Post post = Post.builder()
                .title("민광식입니다.")
                .content("테스트입니다 ^^")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        Assertions.assertEquals(0, postRepository.count());
    }
}