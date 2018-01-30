package com.gmail.kolesnyk.zakhar.graphTask.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VertexDto {

    @NonNull
    private Long id;

    private Long parentId;

    private List<Long> childrenIds;
}
