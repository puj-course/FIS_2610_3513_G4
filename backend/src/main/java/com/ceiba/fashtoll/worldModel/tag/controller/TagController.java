package com.ceiba.fashtoll.worldModel.tag.controller;

import com.ceiba.fashtoll.worldModel.tag.dto.TagRequest;
import com.ceiba.fashtoll.worldModel.tag.dto.TagResponse;
import com.ceiba.fashtoll.worldModel.tag.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<TagResponse> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagResponse getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse newTag = tagService.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TagResponse updateTag(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        return tagService.updateTag(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
