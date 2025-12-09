package com.sultanmuratyeldar.pastebin.dto;
import com.sultanmuratyeldar.pastebin.entity.Role;
import lombok.Data;

import java.io.Serializable;


@Data
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String role;
}