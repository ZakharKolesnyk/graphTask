package com.gmail.kolesnyk.zakhar.graphTask.persistence.repository;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.entity.Graph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraphRepository extends JpaRepository<Graph, Long> {
}
