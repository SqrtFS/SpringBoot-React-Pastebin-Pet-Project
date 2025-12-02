package com.sultanmuratyeldar.pastebin.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class HashIndexDto {
    private String hash;
    private String storagePath;
    private Long size;
    private Instant createdAt;
}

