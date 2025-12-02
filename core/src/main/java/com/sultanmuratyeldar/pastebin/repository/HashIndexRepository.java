package com.sultanmuratyeldar.pastebin.repository;

import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HashIndexRepository extends JpaRepository<HashIndex, Long> {
    Optional<HashIndex> findByHash(String hash);
    void deleteByHash(String hash);
}
