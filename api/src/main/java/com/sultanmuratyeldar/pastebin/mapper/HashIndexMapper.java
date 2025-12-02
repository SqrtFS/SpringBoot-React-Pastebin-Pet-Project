package com.sultanmuratyeldar.pastebin.mapper;

import com.sultanmuratyeldar.pastebin.dto.HashIndexDto;
import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashIndexMapper extends Mappable<HashIndex , HashIndexDto>{}
