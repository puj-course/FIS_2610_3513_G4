package com.ceiba.fashtoll.searchEngine.tag.service;

import com.ceiba.fashtoll.searchEngine.tag.dto.TagRequest;
import com.ceiba.fashtoll.searchEngine.tag.dto.TagResponse;
import com.ceiba.fashtoll.searchEngine.tag.entity.Tag;
import com.ceiba.fashtoll.searchEngine.tag.mapper.TagMapper;
import com.ceiba.fashtoll.searchEngine.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));
        return tagMapper.toResponse(tag);
    }

    @Transactional
    public TagResponse createTag(TagRequest request) {
        Tag tag = tagMapper.toEntity(request);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toResponse(savedTag);
    }

    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));

        tagMapper.updateEntity(request, tag);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toResponse(savedTag);
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));
        tagRepository.delete(tag);
    }
}
