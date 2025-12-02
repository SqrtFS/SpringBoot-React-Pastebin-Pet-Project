package com.sultanmuratyeldar.pastebin.service;

import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import com.sultanmuratyeldar.pastebin.repository.HashIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashIndexService {
    private final HashIndexRepository hashIndexRepository;

    public HashIndex create(HashIndex hashIndex) {
        hashIndex.setCreatedAt(Instant.now());
        return hashIndexRepository.save(hashIndex);
    }

    public List<HashIndex> getAll() {
        return hashIndexRepository.findAll();
    }

    public HashIndex getByHash(String hash) {
        return hashIndexRepository.findByHash(hash)
                .orElseThrow(() -> new RuntimeException("Hash index not found"));
    }

    public void deleteByHash(String hash) {
        hashIndexRepository.deleteByHash(hash);
    }
}
