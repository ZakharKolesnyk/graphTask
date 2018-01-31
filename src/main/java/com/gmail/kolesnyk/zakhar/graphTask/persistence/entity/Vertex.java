package com.gmail.kolesnyk.zakhar.graphTask.persistence.entity;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.dto.VertexDto;
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
@Table(name = "vertices")
public class Vertex {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Vertex parent;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Vertex> children;

    public VertexDto dto() {
        return VertexDto.builder()
                .id(id)
                .parentId(Objects.nonNull(parent) ? parent.getId() : null)
                .childrenIds(Objects.nonNull(children) ? children.stream().map(Vertex::getId).collect(Collectors.toList()) : null)
                .build();
    }
}
