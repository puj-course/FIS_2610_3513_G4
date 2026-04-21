package com.ceiba.fashtoll.worldModel.tag;

import com.ceiba.fashtoll.worldModel.tag.dto.TagRequest;
import com.ceiba.fashtoll.worldModel.tag.dto.TagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagResponse> getAllTags() {
        this.logger.info("Se devolvieron todos los tags");

        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));

        this.logger.info("Se devolvio el tag '" + tag.getName() + "' con id: " + tag.getId());

        return tagMapper.toResponse(tag);
    }

    @Transactional
    public TagResponse createTag(TagRequest request) {
        Tag tag = tagMapper.toEntity(request);
        Tag savedTag = tagRepository.save(tag);

        this.logger.info("Se creo el tag '" + tag.getName() + "' con id: " + savedTag.getId());

        return tagMapper.toResponse(savedTag);
    }

    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));

        tagMapper.updateEntity(request, tag);
        Tag savedTag = tagRepository.save(tag);

        this.logger.info("Se actualizo el tag '" + tag.getName() + "' con id: " + savedTag.getId());

        return tagMapper.toResponse(savedTag);
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + id));
        tagRepository.delete(tag);

        this.logger.info("Se elimino el tag '" + tag.getName() + "' con id: " + tag.getId());
    }
}
