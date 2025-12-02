package com.sultanmuratyeldar.pastebin.service;

import com.sultanmuratyeldar.pastebin.entity.HashIndex;
import com.sultanmuratyeldar.pastebin.repository.HashIndexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HashIndexServiceTest {

    @Mock
    private HashIndexRepository hashIndexRepository;

    @InjectMocks
    private HashIndexService hashIndexService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_ShouldSetCreatedAtAndSave() {
        // given
        HashIndex hashIndex = new HashIndex();
        hashIndex.setHash("abc123");
        hashIndex.setStoragePath("/tmp/file.txt");
        hashIndex.setSize(512L);

        HashIndex saved = new HashIndex();
        saved.setHash("abc123");
        saved.setStoragePath("/tmp/file.txt");
        saved.setSize(512L);
        saved.setCreatedAt(Instant.now());

        when(hashIndexRepository.save(any(HashIndex.class))).thenReturn(saved);

        HashIndex result = hashIndexService.create(hashIndex);

        // then
        assertNotNull(result.getCreatedAt());
        assertEquals("abc123", result.getHash());
        verify(hashIndexRepository, times(1)).save(any(HashIndex.class));
    }

    @Test
    void testGetAll_ShouldReturnList() {
        List<HashIndex> list = List.of(
                new HashIndex("a", "/tmp/a.txt", 100L, Instant.now()),
                new HashIndex("b", "/tmp/b.txt", 200L, Instant.now())
        );
        when(hashIndexRepository.findAll()).thenReturn(list);

        List<HashIndex> result = hashIndexService.getAll();

        assertEquals(2, result.size());
        verify(hashIndexRepository, times(1)).findAll();
    }

    @Test
    void testGetByHash_ShouldReturnHashIndex_WhenFound() {
        HashIndex hashIndex = new HashIndex("abc123", "/tmp/file.txt", 512L, Instant.now());
        when(hashIndexRepository.findByHash("abc123")).thenReturn(Optional.of(hashIndex));

        HashIndex result = hashIndexService.getByHash("abc123");

        assertEquals("abc123", result.getHash());
        verify(hashIndexRepository, times(1)).findByHash("abc123");
    }

    @Test
    void testGetByHash_ShouldThrowException_WhenNotFound() {
        when(hashIndexRepository.findByHash("notfound")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> hashIndexService.getByHash("notfound"));

        assertEquals("Hash index not found", exception.getMessage());
    }

    @Test
    void testDeleteByHash_ShouldCallRepository() {
        doNothing().when(hashIndexRepository).deleteByHash("abc123");

        hashIndexService.deleteByHash("abc123");

        verify(hashIndexRepository, times(1)).deleteByHash("abc123");
    }
}
