package com.gmail.kolesnyk.zakhar.graphTask.controller.rest;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.dto.VertexDto;
import com.gmail.kolesnyk.zakhar.graphTask.service.vertex.VertexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vertices")
@Api(description = "Vertex REST API")
public class VertexController {

    @Autowired
    private VertexService service;

    @GetMapping("/{id}/parents")
    @ApiOperation("Get Vertex by ID")
    public List<VertexDto> getParentsById(@PathVariable Long id) {
        return service.getAllParentsByIdDto(id);
    }

    @GetMapping("/{id}/children")
    @ApiOperation("Get all children of Vertex by it ID")
    public List<VertexDto> getChildrenById(@PathVariable Long id) {
        return service.getAllChildrenByIdDto(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("Create Vertex with specified ID or return stored if exist one")
    public VertexDto getOrCreateVertexById(@PathVariable Long id) {
        return service.getOrCreateDto(id);
    }

    @PostMapping
    @ApiOperation("Create Vertex with specified ID and specified parent and children, if one or parent or any children don't exist they will be created")
    public VertexDto Vertex(@RequestBody @Valid VertexDto vertexDto) {
        return service.createAndReturnDto(vertexDto);
    }

    @PutMapping
    @ApiOperation("Update Vertex with specified ID and specified parent and children, if one or parent or any children don't exist they will be created")
    public VertexDto updateVertex(@RequestBody @Valid VertexDto vertexDto) {
        return service.updateAndReturnDto(vertexDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete Vertex with by ID with all children")
    public VertexDto deleteVertex(@PathVariable Long id) {
        return service.deleteAndReturnDto(id);
    }
}
