package com.gmail.kolesnyk.zakhar.graphTask.service;

import com.gmail.kolesnyk.zakhar.graphTask.service.dto.VertexDto;

import java.util.List;

public interface VertexService {

    VertexDto getOrCreateDto(Long id);

    VertexDto createAndReturnDto(VertexDto vertexDto);

    VertexDto deleteAndReturnDto(Long id);

    VertexDto updateAndReturnDto(VertexDto vertexDto);

    List<VertexDto> getAllParentsByIdDto(Long id);

    List<VertexDto> getAllChildrenByIdDto(Long id);
}
