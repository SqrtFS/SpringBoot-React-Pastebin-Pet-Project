package com.sultanmuratyeldar.pastebin.mapper;

import com.sultanmuratyeldar.pastebin.dto.PostDto;
import com.sultanmuratyeldar.pastebin.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper extends Mappable<Post, PostDto> {

    @Mapping(target = "userDto", source = "user")
    PostDto toDto(Post entity);

    @Mapping(target = "user", source = "userDto")
    Post toEntity(PostDto dto);

    default List<PostDto> toDto(List<Post> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
