package com.gslog.api.response;

import com.gslog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle().substring(0, Math.min(10, post.getTitle().length()));
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(10, title.length()));
        this.content = content;
    }
}
