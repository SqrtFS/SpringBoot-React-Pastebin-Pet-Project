package com.sultanmuratyeldar.pastebin.dto;
import com.sultanmuratyeldar.pastebin.entity.Role;
import lombok.Data;


@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
}