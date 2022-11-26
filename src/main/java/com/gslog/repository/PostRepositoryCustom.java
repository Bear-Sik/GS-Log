package com.gslog.repository;

import com.gslog.domain.Post;
import com.gslog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
