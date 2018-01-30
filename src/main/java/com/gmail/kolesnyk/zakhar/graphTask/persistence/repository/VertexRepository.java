package com.gmail.kolesnyk.zakhar.graphTask.persistence.repository;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.entity.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VertexRepository extends JpaRepository<Vertex, Long> {
}
