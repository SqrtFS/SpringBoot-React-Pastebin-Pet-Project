package com.sultanmuratyeldar.pastebin.listener;

import com.sultanmuratyeldar.pastebin.event.HashProcessingEvent;
import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import com.sultanmuratyeldar.pastebin.repository.HashIndexRepository;
import com.sultanmuratyeldar.pastebin.repository.PostRepository;
import com.sultanmuratyeldar.pastebin.service.HashGeneratorService;
import com.sultanmuratyeldar.pastebin.service.PostService;
import com.sultanmuratyeldar.pastebin.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class HashProcessingListener {

    private final StorageService storageService;
    private final HashGeneratorService hashGeneratorService;
    private final HashIndexRepository hashIndexRepository;
    private final PostService postService;

    @Async("taskExecutor")
    @EventListener
    public void handle(HashProcessingEvent event) {
        System.out.println("Async started on thread: " + Thread.currentThread().getName());
        try {
            byte[] content = event.getContent();
            String hash = hashGeneratorService.sha256Hex(content);

            storageService.upload(hash, new ByteArrayInputStream(content), content.length, event.getContentType());

            HashIndex index = new HashIndex();
            index.setHash(hash);
            index.setStoragePath(hash);
            index.setSize((long) content.length);
            index.setCreatedAt(Instant.now());

            hashIndexRepository.save(index);

            postService.updateContentHash(event.getPostId(), hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
