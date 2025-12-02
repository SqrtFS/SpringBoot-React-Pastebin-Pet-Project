package com.sultanmuratyeldar.pastebin.controller;

import com.sultanmuratyeldar.pastebin.dto.PostDto;
import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import com.sultanmuratyeldar.pastebin.entity.Post;
import com.sultanmuratyeldar.pastebin.entity.User;
import com.sultanmuratyeldar.pastebin.mapper.PostMapper;
import com.sultanmuratyeldar.pastebin.service.HashIndexService;
import com.sultanmuratyeldar.pastebin.service.PostService;
import com.sultanmuratyeldar.pastebin.service.StorageService;
import com.sultanmuratyeldar.pastebin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final StorageService storageService;
    private final UserService userService;
    private final PostMapper postMapper;
    private final HashIndexService hashIndexService;

    @GetMapping
    public List<PostDto> getAll() {
        return postService.findAll()
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return postMapper.toDto(post);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deletePost(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        HashIndex hashIndex = hashIndexService.getByHash(post.getContentHash());
        storageService.delete(hashIndex.getStoragePath());
        postService.deletePost(id);
        return HttpStatus.NO_CONTENT;
    }

    @PostMapping
    public PostDto createPostFromText(
            @RequestParam("text") String text,
            Principal principal
    ) throws IOException {

        User user = userService.findByUsername(principal.getName());

        Post post = new Post();
        post.setUser(user);
        post.setTitle("New Post");
        post.setDescription(text.substring(0, Math.min(50, text.length())));

        Post saved = postService.addPost(post);

        postService.submitForStorage(
                saved.getId(),
                text.getBytes(StandardCharsets.UTF_8),
                "post.txt",
                "text/plain"
        );

        return postMapper.toDto(saved);
    }

    @PutMapping
    public PostDto updatePost(@RequestBody PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Post updated = postService.updatePost(post);
        return postMapper.toDto(updated);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> downloadPostContent(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);

        byte[] data = storageService.download(post.getContentHash());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"post-content.txt\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
