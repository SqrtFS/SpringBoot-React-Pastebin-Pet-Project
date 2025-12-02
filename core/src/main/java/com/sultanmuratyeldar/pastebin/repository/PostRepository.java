package com.sultanmuratyeldar.pastebin.repository;


import com.sultanmuratyeldar.pastebin.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("update Post p set p.contentHash = :hash where p.id = :id")
    void updateContentHash(Long id, String hash);
}
