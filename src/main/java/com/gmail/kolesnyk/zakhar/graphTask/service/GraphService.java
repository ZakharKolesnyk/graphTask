package com.gmail.kolesnyk.zakhar.graphTask.service;

import com.gmail.kolesnyk.zakhar.graphTask.service.dto.GraphDto;

import java.util.List;

public interface GraphService {

    GraphDto getOrCreateDto(Long id);

    GraphDto createAndReturnDto(GraphDto graphDto);

    GraphDto deleteAndReturnDto(Long id);

    GraphDto updateAndReturnDto(GraphDto graphDto);

    List<GraphDto> getAllParentsByIdDto(Long id);

    List<GraphDto> getAllChildrenByIdDto(Long id);
}
