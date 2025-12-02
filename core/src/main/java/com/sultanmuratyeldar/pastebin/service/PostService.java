package com.sultanmuratyeldar.pastebin.service;

import com.sultanmuratyeldar.pastebin.entity.Post;
import com.sultanmuratyeldar.pastebin.event.HashProcessingEvent;
import com.sultanmuratyeldar.pastebin.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    public void submitForStorage(Long postId, byte[] data, String filename, String contentType) {
        publisher.publishEvent(new HashProcessingEvent(postId, data, filename, contentType));
    }
    @Transactional
    public void updateContentHash(Long postId, String hash) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setContentHash(hash);
        postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    @Transactional
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Post post) {
        if (!postRepository.existsById(post.getId())) {
            throw new RuntimeException("Cannot update. Post not found with id: " + post.getId());
        }
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}
