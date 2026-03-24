package com.ceiba.fashtoll.tag.controller;

import com.ceiba.fashtoll.tag.dto.TagDTO;
import com.ceiba.fashtoll.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDTO> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO newTag = tagService.createTag(tagDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TagDTO updateTag(@PathVariable Long id, @RequestBody TagDTO updatedTagDTO) {
        return tagService.updateTag(id, updatedTagDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
