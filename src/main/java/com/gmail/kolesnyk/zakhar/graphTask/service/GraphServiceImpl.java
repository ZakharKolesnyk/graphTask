package com.gmail.kolesnyk.zakhar.graphTask.service;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.entity.Graph;
import com.gmail.kolesnyk.zakhar.graphTask.persistence.repository.GraphRepository;
import com.gmail.kolesnyk.zakhar.graphTask.service.dto.GraphDto;
import com.gmail.kolesnyk.zakhar.graphTask.service.exception.DataRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private GraphRepository repository;

    @Override
    @Transactional
    public GraphDto getOrCreateDto(Long id) {
        Graph graph = repository.findOne(id);
        if (Objects.isNull(graph)) {
            graph = repository.save(Graph.builder().id(id).build());
        }
        return graph.dto();
    }

    @Override
    @Transactional
    public GraphDto createAndReturnDto(GraphDto graphDto) {
        return saveOrUpdate(graphDto);
    }

    @Override
    @Transactional
    public GraphDto deleteAndReturnDto(Long id) {
        Graph graph = repository.findOne(id);
        if (Objects.isNull(graph)) {
            throw new DataRequestException(String.format("graph with id: %s not found", id));
        }
        deleteWithChildren(graph);
        return graph.dto();
    }

    private void deleteWithChildren(Graph graph) {
        List<Graph> children = graph.getChildren();
        for (Graph child : children) {
            deleteWithChildren(child);
        }
        repository.delete(graph.getId());
    }

    @Override
    @Transactional
    public GraphDto updateAndReturnDto(GraphDto graphDto) {
        return saveOrUpdate(graphDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GraphDto> getAllParentsByIdDto(Long id) {
        if (!repository.exists(id)) {
            throw new DataRequestException(String.format("graph with id: %s not found", id));
        }
        List<Graph> parents = new ArrayList<>();
        fillParents(repository.findOne(id), parents);
        return parents.stream().map(Graph::dto).collect(Collectors.toList());
    }

    private void fillParents(Graph graph, List<Graph> list) {
        Graph parent = graph.getParent();
        if (Objects.isNull(parent)) {
            return;
        }
        Graph fetchedParent = repository.findOne(parent.getId());
        list.add(parent);
        fillParents(fetchedParent, list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GraphDto> getAllChildrenByIdDto(Long id) {
        if (!repository.exists(id)) {
            throw new DataRequestException(String.format("graph with id: %s not found", id));
        }
        List<Graph> children = new ArrayList<>();
        fillChildren(repository.findOne(id), children);
        return children.stream().map(Graph::dto).collect(Collectors.toList());
    }

    private void fillChildren(Graph graph, List<Graph> list) {
        List<Graph> children = graph.getChildren();
        if (Objects.isNull(children)) {
            return;
        }
        for (Graph child : children) {
            Graph fetchedChild = repository.findOne(child.getId());
            list.add(child);
            fillChildren(fetchedChild, list);
        }
    }

    private GraphDto saveOrUpdate(GraphDto graphDto) {
        Graph graph = Graph.builder().id(graphDto.getId()).build();
        if (Objects.nonNull(graphDto.getParentId())) {
            Graph parent = repository.findOne(graphDto.getParentId());
            if (Objects.isNull(parent)) {
                parent = Graph.builder().id(graphDto.getParentId()).build();
                repository.save(parent);
            }
            graph.setParent(parent);
        }
        graph = repository.save(graph);
        if (Objects.nonNull(graphDto.getChildrenIds())) {
            graph.setChildren(new ArrayList<>());
            for (Long id : graphDto.getChildrenIds()) {
                Graph child = repository.findOne(id);
                if (Objects.isNull(child)) {
                    child = Graph.builder().id(id).build();
                }
                child.setParent(graph);
                graph.getChildren().add(child);
            }
            repository.save(graph.getChildren());
        }
        return graph.dto();
    }
}
