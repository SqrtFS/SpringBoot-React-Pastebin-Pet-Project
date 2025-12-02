package com.sultanmuratyeldar.pastebin.controller;

import com.sultanmuratyeldar.pastebin.dto.HashIndexDto;
import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import com.sultanmuratyeldar.pastebin.mapper.HashIndexMapper;
import com.sultanmuratyeldar.pastebin.service.HashIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hash-index")
@RequiredArgsConstructor
public class HashIndexController {
    private final HashIndexService hashIndexService;
    private final HashIndexMapper hashIndexMapper;

    @GetMapping
    public List<HashIndexDto> getAll() {
        return hashIndexService.getAll().stream()
                .map(hashIndexMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{hash}")
    public HashIndexDto getByHash(@PathVariable("hash") String hash) {
        return hashIndexMapper.toDto(hashIndexService.getByHash(hash));
    }

    @PostMapping
    public HashIndexDto create(@RequestBody HashIndexDto dto) {
        HashIndex entity = hashIndexMapper.toEntity(dto);
        return hashIndexMapper.toDto(hashIndexService.create(entity));
    }

    @DeleteMapping("/{hash}")
    public void deleteByHash(@PathVariable("hash") String hash) {
        hashIndexService.deleteByHash(hash);
    }
}
