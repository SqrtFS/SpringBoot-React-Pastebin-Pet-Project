package com.sultanmuratyeldar.pastebin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "hash_index")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HashIndex {
    @Id
    private String hash;

    private String storagePath; // s3 path or local path
    private Long size;
    private Instant createdAt;
}