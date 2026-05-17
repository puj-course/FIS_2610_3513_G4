package com.ceiba.fashtoll.worldModel.tag;

import com.ceiba.fashtoll.worldModel.tag.dto.TagRequest;
import com.ceiba.fashtoll.worldModel.tag.dto.TagResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de TagService")
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    @Test
    @DisplayName("CP-TAG-01: getAllTags - Retorna lista")
    void getAllTags_returnsList() {
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(tagMapper.toResponse(any())).thenReturn(new TagResponse());

        List<TagResponse> result = tagService.getAllTags();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("CP-TAG-02: getTagById - Retorna tag")
    void getTagById_returnsTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagMapper.toResponse(any())).thenReturn(new TagResponse());

        TagResponse result = tagService.getTagById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("CP-TAG-03: createTag - Crea tag")
    void createTag_createsTag() {
        TagRequest req = new TagRequest();
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagMapper.toEntity(any())).thenReturn(tag);
        when(tagRepository.save(any())).thenReturn(tag);
        when(tagMapper.toResponse(any())).thenReturn(new TagResponse());

        TagResponse result = tagService.createTag(req);

        assertNotNull(result);
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-TAG-04: updateTag - Actualiza tag")
    void updateTag_updatesTag() {
        TagRequest req = new TagRequest();
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any())).thenReturn(tag);
        when(tagMapper.toResponse(any())).thenReturn(new TagResponse());

        TagResponse result = tagService.updateTag(1L, req);

        assertNotNull(result);
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-TAG-05: deleteTag - Elimina tag")
    void deleteTag_deletesTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).delete(tag);
    }
}
