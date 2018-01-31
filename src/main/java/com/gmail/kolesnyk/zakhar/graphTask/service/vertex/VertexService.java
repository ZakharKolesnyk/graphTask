package com.gmail.kolesnyk.zakhar.graphTask.service.vertex;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.dto.VertexDto;

import java.util.List;

public interface VertexService {

    VertexDto getOrCreateDto(Long id);

    VertexDto createAndReturnDto(VertexDto vertexDto);

    VertexDto deleteAndReturnDto(Long id);

    VertexDto updateAndReturnDto(VertexDto vertexDto);

    List<VertexDto> getAllParentsByIdDto(Long id);

    List<VertexDto> getAllChildrenByIdDto(Long id);
}
