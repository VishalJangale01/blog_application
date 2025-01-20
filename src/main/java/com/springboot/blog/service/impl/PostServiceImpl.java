package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.Postdto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Postdto createPost(Postdto postdto) {
        //Convert DTO to entity
        Post post = mapToEntity(postdto);

        Post postReturnedFromDB = postRepository.save(post);

        // Convert Entity to DTO
        Postdto postResponse = mapToDTO(postReturnedFromDB);

        return postResponse;
    }

    @Override
    public List<Postdto> getAllPost(int pageSize, int pageNo) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize);

        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> listOfPosts = posts.getContent();
        return listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Postdto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public Postdto updatePost(Postdto postdto, long id) {
        System.out.println("Entered update method : ");
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        Post updatedPost = new Post();

        updatedPost.setTitle(postdto.getTitle() != null && !postdto.getTitle().isEmpty() ? postdto.getTitle() : post.getTitle());
        updatedPost.setDescription(postdto.getDescription() != null && !postdto.getDescription().isEmpty() ? postdto.getDescription() : post.getDescription());
        updatedPost.setContent(postdto.getContent() != null && !postdto.getContent().isEmpty() ? postdto.getContent() : post.getContent());
        updatedPost.setId(post.getId());

        System.out.println(updatedPost);

        Post returnedPostFromDB = postRepository.save(updatedPost);

        return mapToDTO(returnedPostFromDB);
    }

    @Override
    public String deletePostById(long id) {
        final boolean[] postNotFound = new boolean[1];
        String str;

        Post post = postRepository.findById(id).orElseThrow(() -> {
            postNotFound[0] = true; // Perform side effect if necessary
            System.out.println("Post not found with given id");
            return new ResourceNotFoundException("Post", "id", id); // Return the exception
        });

        //If post doesnt exists
        if(postNotFound[0] == true) {
            str = "Post not found with id:" + id;
        } else {
            postRepository.delete(post);
            str = "Post deleted successfully";
        }
        return str;
    }

    private Post mapToEntity(Postdto postdto) {
        Post post = new Post();
        post.setContent(postdto.getContent());
        post.setDescription(postdto.getDescription());
        post.setTitle(postdto.getTitle());

        return post;
    }

    private Postdto mapToDTO(Post post) {
        Postdto postdto = new Postdto();
        postdto.setId(post.getId());
        postdto.setContent(post.getContent());
        postdto.setTitle(post.getTitle());
        postdto.setDescription(post.getDescription());

        return postdto;
    }
}
