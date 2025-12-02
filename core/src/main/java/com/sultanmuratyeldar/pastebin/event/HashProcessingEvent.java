package com.sultanmuratyeldar.pastebin.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HashProcessingEvent {
    private final Long postId; // if needed
    private final byte[] content;
    private final String filename;
    private final String contentType;
}
