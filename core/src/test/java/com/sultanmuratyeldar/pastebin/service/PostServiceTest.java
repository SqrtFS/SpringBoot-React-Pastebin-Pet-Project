package com.sultanmuratyeldar.pastebin.service;

import com.sultanmuratyeldar.pastebin.entity.Post;
import com.sultanmuratyeldar.pastebin.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setDescription("Test content");
    }

    @Test
    void findAll_ShouldReturnListOfPosts() {
        when(postRepository.findAll()).thenReturn(List.of(post));

        List<Post> posts = postService.findAll();

        assertEquals(1, posts.size());
        assertEquals("Test Title", posts.get(0).getTitle());
    }

    @Test
    void getPostById_ShouldReturnPost_WhenFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post found = postService.getPostById(1L);

        assertEquals("Test Title", found.getTitle());
    }

    @Test
    void getPostById_ShouldThrowException_WhenNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> postService.getPostById(99L));
        assertTrue(ex.getMessage().contains("Post not found"));
    }

    @Test
    void addPost_ShouldSavePost() {
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post saved = postService.addPost(post);

        assertEquals("Test Title", saved.getTitle());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void deletePost_ShouldCallRepositoryDelete() {
        when(postRepository.existsById(1L)).thenReturn(true);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }
}
