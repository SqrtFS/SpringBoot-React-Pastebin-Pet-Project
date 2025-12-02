package com.sultanmuratyeldar.pastebin.controller;

import com.sultanmuratyeldar.pastebin.dto.UserCreateRequest;
import com.sultanmuratyeldar.pastebin.dto.UserDto;
import com.sultanmuratyeldar.pastebin.entity.User;
import com.sultanmuratyeldar.pastebin.mapper.UserMapper;
import com.sultanmuratyeldar.pastebin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserCreateRequest req) {
        User user = userMapper.toEntity(req);
        return userMapper.toDto(userService.createUser(user));
    }
}
