package com.gmail.kolesnyk.zakhar.graphTask.service;

import com.gmail.kolesnyk.zakhar.graphTask.persistence.entity.Graph;
import com.gmail.kolesnyk.zakhar.graphTask.persistence.repository.GraphRepository;
import com.gmail.kolesnyk.zakhar.graphTask.service.dto.GraphDto;
import com.gmail.kolesnyk.zakhar.graphTask.service.exception.DataRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.gmail.kolesnyk.zakhar.graphTask.AssertUtils.assertGraphDtoEquals;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class GraphServiceImplTest {

    @Autowired
    private GraphService service;

    @Autowired
    private GraphRepository repository;

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getOrCreateDtoIfExistRootTest() {
        //given
        long id = 3L;
        assertTrue(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(repository.findOne(id).dto(), dto);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getOrCreateDtoIfExistMiddleLeafTest() {
        //given
        long id = 4L;
        assertTrue(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(repository.findOne(id).dto(), dto);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getOrCreateDtoIfExistEndLeafTest() {
        //given
        long id = 7L;
        assertTrue(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(repository.findOne(id).dto(), dto);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getOrCreateDtoIfNotExistTest() {
        //given
        long id = 10L;
        assertFalse(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(GraphDto.builder().id(10L).build(), dto);
    }

    @Test
    public void createAndReturnDtoIfNotExistTest() {
        //given
        long id = 10L;
        assertFalse(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(repository.findOne(id).dto(), dto);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void createAndReturnDtoIfExistTest() {
        //given
        long id = 3L;
        assertTrue(repository.exists(id));

        //when
        GraphDto dto = service.getOrCreateDto(id);

        //then
        assertGraphDtoEquals(repository.findOne(id).dto(), dto);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void deleteAndReturnDtoIfExistDtoTest() {
        //given
        long id = 3L;
        assertTrue(repository.exists(id));

        //when
        GraphDto dto = service.deleteAndReturnDto(id);

        //then
        assertFalse(repository.exists(dto.getId()));
        assertEquals(repository.count(), 0);
    }

    @Test(expected = DataRequestException.class)
    @Sql(scripts = "/ddl.sql")
    public void deleteAndReturnDtoIfNotExistDtoTest() {
        //given
        long id = 10L;
        assertFalse(repository.exists(id));

        //when
        service.deleteAndReturnDto(id);

        //then must be unreachable
        fail();
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void updateAndReturnDtoIfAllExistTest() {
        //given
        long id = 6L;
        Long newParentId = 5L;
        assertTrue(repository.exists(newParentId));
        GraphDto storedGraph = repository.findOne(id).dto();
        assertNotEquals(storedGraph.getParentId(), newParentId);
        storedGraph.setParentId(newParentId);

        //when
        GraphDto dto = service.updateAndReturnDto(storedGraph);

        //then
        assertGraphDtoEquals(storedGraph, dto);
    }

    @Test
    public void updateAndReturnDtoIfAllNotExistTest() {
        //given
        long id = 6L;
        assertEquals(repository.count(), 0);
        // +1 graph
        Graph graph = Graph.builder()
                .id(id)
                // +1 parent
                .parent(Graph.builder().id(1L).build())
                // +3 child
                .children(LongStream.range(2, 5).mapToObj(i -> Graph.builder().id(i).build()).collect(Collectors.toList()))
                .build();


        //when
        GraphDto dto = service.updateAndReturnDto(graph.dto());

        //then
        assertGraphDtoEquals(graph.dto(), dto);
        // 5 graph should be created
        assertEquals(repository.count(), 5);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void updateAndReturnDtoIfChildrenNotExistTest() {
        //given
        long id = 8L;
        long initialAmount = repository.count();
        Graph graph = repository.findOne(id);
        graph.setChildren(LongStream.range(10, 15).mapToObj(i -> Graph.builder().id(i).build()).collect(Collectors.toList()));
        // + 5 children


        //when
        GraphDto dto = service.updateAndReturnDto(graph.dto());

        //then
        assertGraphDtoEquals(graph.dto(), dto);
        assertEquals(repository.count() - initialAmount, 5);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void updateAndReturnDtoIfParentNotExistTest() {
        //given
        long id = 8L;
        long parentId = 10L;
        long initialAmount = repository.count();
        Graph graph = repository.findOne(id);
        graph.setParent(Graph.builder().id(parentId).build());
        // + 1 parent


        //when
        GraphDto dto = service.updateAndReturnDto(graph.dto());

        //then
        assertGraphDtoEquals(graph.dto(), dto);
        assertEquals(repository.count() - initialAmount, 1);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getAllParentsByIdDtoIfParentExistTest() {
        //given
        long id = 8L;

        //when
        List<GraphDto> parents = service.getAllParentsByIdDto(id);

        //then
        int count = 0; //count matched
        for (GraphDto parent : parents) {
            if (parent.getId().equals(6L) ||
                    parent.getId().equals(2L) ||
                    parent.getId().equals(3L)) {
                count++;
            }
        }
        assertEquals(parents.size(), count);

    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getAllParentsByIdDtoIfParentNotExistTest() {
        //given
        long id = 3L;

        //when
        List<GraphDto> parents = service.getAllParentsByIdDto(id);

        //then
        int count = 0; //count matched
        assertEquals(parents.size(), count);

    }

    @Test(expected = DataRequestException.class)
    public void getAllParentsByIdDtoIfGraphNotExistTest() {
        //given
        long id = 1L;

        //when
        service.getAllParentsByIdDto(id);

        //then must be unreachable
        fail();
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getAllChildrenByIdDtoIfChildrenExistTest() {
        //given
        long id = 4L;

        //when
        List<GraphDto> children = service.getAllChildrenByIdDto(id);

        //then
        int count = 0; //count matched
        for (GraphDto child : children) {
            if (child.getId().equals(5L) ||
                    child.getId().equals(7L)) {
                count++;
            }
        }
        assertEquals(children.size(), count);
    }

    @Test
    @Sql(scripts = "/ddl.sql")
    public void getAllChildrenByIdDtoIfChildrenNotExistsTest() {
        //given
        long id = 7L;

        //when
        List<GraphDto> children = service.getAllChildrenByIdDto(id);

        //then
        int count = 0; //count matched
        assertEquals(children.size(), count);
    }

    @Test(expected = DataRequestException.class)
    public void getAllChildrenByIdDtoIfGraphNotExistsTest() {
        //given
        long id = 1L;

        //when
        service.getAllChildrenByIdDto(id);

        //then must be unreachable
        fail();
    }
}