package com.gslog.api.repository;

import com.gslog.api.domain.Post;
import com.gslog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
