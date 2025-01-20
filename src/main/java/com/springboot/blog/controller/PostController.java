package com.springboot.blog.controller;

import com.springboot.blog.payload.Postdto;
import com.springboot.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //Create blog post
    @PostMapping
    public ResponseEntity<Postdto> createPost(@RequestBody Postdto postdto){
        return new ResponseEntity<>(postService.createPost(postdto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Postdto>> getAllPosts(
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo
    ){
        return new ResponseEntity<>(postService.getAllPost(pageSize, pageNo), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postdto> getPostById(@PathVariable(name="id") long id){
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Postdto> updatePost(@RequestBody Postdto postdto, @PathVariable(name="id") long id) {
        return new ResponseEntity<>(postService.updatePost(postdto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name="id") long id) {
        return new ResponseEntity<>(postService.deletePostById(id), HttpStatus.OK);
    }
}
