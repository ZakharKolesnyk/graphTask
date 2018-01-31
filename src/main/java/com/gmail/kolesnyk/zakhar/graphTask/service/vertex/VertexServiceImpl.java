package com.gmail.kolesnyk.zakhar.graphTask.service.vertex;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.dto.VertexDto;
import com.gmail.kolesnyk.zakhar.graphTask.persistence.entity.Vertex;
import com.gmail.kolesnyk.zakhar.graphTask.persistence.repository.VertexRepository;
import com.gmail.kolesnyk.zakhar.graphTask.service.exception.DataRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VertexServiceImpl implements VertexService {

    @Autowired
    private VertexRepository repository;

    @Override
    @Transactional
    public VertexDto getOrCreateDto(Long id) {
        Vertex vertex = repository.findOne(id);
        if (Objects.isNull(vertex)) {
            vertex = repository.save(Vertex.builder().id(id).build());
        }
        return vertex.dto();
    }

    @Override
    @Transactional
    public VertexDto createAndReturnDto(VertexDto vertexDto) {
        return saveOrUpdate(vertexDto);
    }

    @Override
    @Transactional
    public VertexDto deleteAndReturnDto(Long id) {
        Vertex vertex = repository.findOne(id);
        if (Objects.isNull(vertex)) {
            throw new DataRequestException(String.format("vertex with id: %s not found", id));
        }
        deleteWithChildren(vertex);
        return vertex.dto();
    }

    private void deleteWithChildren(Vertex vertex) {
        List<Vertex> children = vertex.getChildren();
        for (Vertex child : children) {
            deleteWithChildren(child);
        }
        repository.delete(vertex.getId());
    }

    @Override
    @Transactional
    public VertexDto updateAndReturnDto(VertexDto vertexDto) {
        return saveOrUpdate(vertexDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VertexDto> getAllParentsByIdDto(Long id) {
        if (!repository.exists(id)) {
            throw new DataRequestException(String.format("vertex with id: %s not found", id));
        }
        List<Vertex> parents = new ArrayList<>();
        fillParents(repository.findOne(id), parents);
        return parents.stream().map(Vertex::dto).collect(Collectors.toList());
    }

    private void fillParents(Vertex vertex, List<Vertex> list) {
        Vertex parent = vertex.getParent();
        if (Objects.isNull(parent)) {
            return;
        }
        Vertex fetchedParent = repository.findOne(parent.getId());
        list.add(parent);
        fillParents(fetchedParent, list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VertexDto> getAllChildrenByIdDto(Long id) {
        if (!repository.exists(id)) {
            throw new DataRequestException(String.format("vertex with id: %s not found", id));
        }
        List<Vertex> children = new ArrayList<>();
        fillChildren(repository.findOne(id), children);
        return children.stream().map(Vertex::dto).collect(Collectors.toList());
    }

    private void fillChildren(Vertex vertex, List<Vertex> list) {
        List<Vertex> children = vertex.getChildren();
        if (Objects.isNull(children)) {
            return;
        }
        for (Vertex child : children) {
            Vertex fetchedChild = repository.findOne(child.getId());
            list.add(child);
            fillChildren(fetchedChild, list);
        }
    }

    private VertexDto saveOrUpdate(VertexDto vertexDto) {
        Vertex vertex = Vertex.builder().id(vertexDto.getId()).build();
        if (Objects.nonNull(vertexDto.getParentId())) {
            Vertex parent = repository.findOne(vertexDto.getParentId());
            if (Objects.isNull(parent)) {
                parent = Vertex.builder().id(vertexDto.getParentId()).build();
                repository.save(parent);
            }
            vertex.setParent(parent);
        }
        vertex = repository.save(vertex);
        if (Objects.nonNull(vertexDto.getChildrenIds())) {
            vertex.setChildren(new ArrayList<>());
            for (Long id : vertexDto.getChildrenIds()) {
                Vertex child = repository.findOne(id);
                if (Objects.isNull(child)) {
                    child = Vertex.builder().id(id).build();
                }
                child.setParent(vertex);
                vertex.getChildren().add(child);
            }
            repository.save(vertex.getChildren());
        }
        return vertex.dto();
    }
}
