package com.gmail.kolesnyk.zakhar.graphTask.persistence.entity;

import com.gmail.kolesnyk.zakhar.graphTask.service.dto.GraphDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "graphs")
public class Graph {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Graph parent;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Graph> children;

    public GraphDto dto() {
        return GraphDto.builder()
                .id(id)
                .parentId(Objects.nonNull(parent) ? parent.getId() : null)
                .childrenIds(Objects.nonNull(children) ? children.stream().map(Graph::getId).collect(Collectors.toList()) : null)
                .build();
    }
}
