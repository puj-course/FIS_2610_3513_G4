package com.ceiba.fashtoll.tag.mapper;

import com.ceiba.fashtoll.tag.dto.TagDTO;
import com.ceiba.fashtoll.tag.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagDTO toDTO(Tag tag) {
        if (tag == null) return null;
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setType(tag.getType());
        return dto;
    }

    public Tag toEntity(TagDTO dto) {
        if (dto == null) return null;
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        tag.setType(dto.getType());
        return tag;
    }
}
