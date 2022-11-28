package com.gslog.api.service;

import com.gslog.api.Exception.PostNotFound;
import com.gslog.api.domain.Post;
import com.gslog.api.domain.PostEditor;
import com.gslog.api.repository.PostRepository;
import com.gslog.api.request.PostCreate;
import com.gslog.api.request.PostEdit;
import com.gslog.api.request.PostSearch;
import com.gslog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(PostCreate postCreate) {
        // postCreate -> Entity 형태로 변환

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        return postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB 글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이  발생할 수 있다.

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        
        /**
         * PostEditor를 사용한 이유
         * 1. 필드가 늘어날 경우 유지보수 힘듦
         * 2. 도메인 내에서 수정할 수 있는 필드를 제한할 경우 용이
         */
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        editorBuilder.title(postEdit.getTitle());
        editorBuilder.content(postEdit.getContent());

        post.edit(editorBuilder.build());
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
