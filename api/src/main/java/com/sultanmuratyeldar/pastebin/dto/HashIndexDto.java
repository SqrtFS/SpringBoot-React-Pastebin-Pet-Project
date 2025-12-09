package com.sultanmuratyeldar.pastebin.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class HashIndexDto implements Serializable {
    private String hash;
    private String storagePath;
    private Long size;
    private Instant createdAt;
}

