package com.ceiba.fashtoll.tag.dto;

import com.ceiba.fashtoll.enums.TagType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
    private TagType type;
}
