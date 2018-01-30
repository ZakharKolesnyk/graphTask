package com.gmail.kolesnyk.zakhar.graphTask;

import com.gmail.kolesnyk.zakhar.graphTask.service.dto.VertexDto;

import java.util.Objects;

import static org.junit.Assert.*;

public class AssertUtils {

    private AssertUtils() {
    }

    public static void assertVertexDtoEquals(VertexDto expected, VertexDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getParentId(), actual.getParentId());
        if (!assertAndIsBothNull(expected.getChildrenIds(), actual.getChildrenIds())) {
            assertArrayEquals(expected.getChildrenIds().toArray(), actual.getChildrenIds().toArray());
        }
    }

    private static boolean assertAndIsBothNull(Object expected, Object actual) {
        if (Objects.isNull(expected)) {
            if (Objects.nonNull(actual)) {
                fail();
            }
            return true;
        } else {
            if (Objects.isNull(actual)) {
                fail();
            }
            return false;
        }
    }
}
