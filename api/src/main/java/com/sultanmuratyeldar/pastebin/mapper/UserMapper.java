package com.sultanmuratyeldar.pastebin.mapper;

import com.sultanmuratyeldar.pastebin.dto.UserCreateRequest;
import com.sultanmuratyeldar.pastebin.dto.UserDto;
import com.sultanmuratyeldar.pastebin.entity.Role;
import com.sultanmuratyeldar.pastebin.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

    // Role -> String
    default String map(Role role) {
        return role != null ? role.getName() : null;
    }

    // String -> Role
    default Role map(String roleName) {
        if (roleName == null) return null;
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    // UserCreateRequest -> User (default)
    default User toEntity(UserCreateRequest request) {
        if (request == null) return null;
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    //  List<D> -> List<E>
    default List<UserDto> toDto(List<User> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
