package com.springboot.blog.service;

import com.springboot.blog.payload.Postdto;

import java.util.List;

public interface PostService {

    Postdto createPost(Postdto postdto);
    List<Postdto> getAllPost(int pageSize, int pageNo);
    Postdto getPostById(long id);
    Postdto updatePost(Postdto postdto, long id);
    String deletePostById(long id);
}
