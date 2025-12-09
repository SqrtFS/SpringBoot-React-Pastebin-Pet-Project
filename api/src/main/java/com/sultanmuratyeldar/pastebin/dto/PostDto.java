package com.sultanmuratyeldar.pastebin.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String contentHash;
    private UserDto userDto;
}