package com.ceiba.fashtoll.tag.mapper;

import com.ceiba.fashtoll.tag.dto.TagRequest;
import com.ceiba.fashtoll.tag.dto.TagResponse;
import com.ceiba.fashtoll.tag.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResponse toResponse(Tag tag) {
        if (tag == null) return null;
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        response.setType(tag.getType());
        return response;
    }

    public Tag toEntity(TagRequest request) {
        if (request == null) return null;
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setType(request.getType());
        return tag;
    }

    public void updateEntity(TagRequest request, Tag tag) {
        if (request == null || tag == null) return;
        tag.setName(request.getName());
        tag.setType(request.getType());
    }
}
