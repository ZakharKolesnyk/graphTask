package com.gmail.kolesnyk.zakhar.graphTask.controller;

import com.gmail.kolesnyk.zakhar.graphTask.service.GraphService;
import com.gmail.kolesnyk.zakhar.graphTask.service.dto.GraphDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/graphs")
public class GraphController {

    @Autowired
    private GraphService service;

    @GetMapping("/{id}/parents")
    public List<GraphDto> getParentsById(@PathVariable Long id) {
        return service.getAllParentsByIdDto(id);
    }

    @GetMapping("/{id}/children")
    public List<GraphDto> getChildrenById(@PathVariable Long id) {
        return service.getAllChildrenByIdDto(id);
    }

    @GetMapping("/{id}")
    public GraphDto getOrCreateGraphById(@PathVariable Long id) {
        return service.getOrCreateDto(id);
    }

    @PostMapping
    public GraphDto createGraph(@RequestBody @Valid GraphDto graphDto) {
        return service.createAndReturnDto(graphDto);
    }

    @PutMapping
    public GraphDto updateGraph(@RequestBody @Valid GraphDto graphDto) {
        return service.updateAndReturnDto(graphDto);
    }

    @DeleteMapping("/{id}")
    public GraphDto deleteGraph(@PathVariable Long id) {
        return service.deleteAndReturnDto(id);
    }
}
